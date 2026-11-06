
package com.ahmadmadany.frontend;

import com.ahmadmadany.frontend.commands.Command;
import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.enemies.Boss;
import com.ahmadmadany.frontend.objects.enemies.Enemy;
import com.ahmadmadany.frontend.objects.enemies.Fairy;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.ahmadmadany.frontend.systems.AssetManager;
import com.ahmadmadany.frontend.systems.EntityFactory;
import com.ahmadmadany.frontend.systems.BulletManager;
import com.ahmadmadany.frontend.systems.CollisionReferee;
import com.ahmadmadany.frontend.commands.InputHandler;
import com.ahmadmadany.frontend.ui.GameHUD;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private InputHandler inputHandler;
    private GameHUD gameHUD;
    private float enemyShootTimer = 0f;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private AssetManager assetManager;

    private Player player;
    private List<Fairy> fairies;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;

    private List<GameObject> entities;

    @Override
    public void create() {

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();

        inputHandler = new InputHandler();
        gameHUD = new GameHUD();

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        entities = new ArrayList<>();
        fairies = new ArrayList<>();

        // Initialize graphics assets
        assetManager = AssetManager.getInstance();
        assetManager.init();

        // Create player using Factory Pattern
        player = EntityFactory.createPlayer(
            200f,
            50f,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        player.registerObserver(gameHUD);

        Fairy redFairy = EntityFactory.createFairy(
            150f, 380f, "Red Fairy", 20
        );

        Fairy blueFairy = EntityFactory.createFairy(
            250f, 380f, "Blue Fairy", 20, "fairy_idle_blue"
        );

        fairies.add(redFairy);
        fairies.add(blueFairy);

        boss = EntityFactory.createBoss(
            250f, 400f, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            180f, 450f, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220f, 480f, ItemType.POINT
        );

        entities.add(player);
        entities.addAll(fairies);
        entities.add(boss);
        entities.add(pointItem);
        entities.add(powerItem);

        inputHandler.setEntities(entities);
    }

    private void updateEnemyScheduler(float delta) {
        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f) {
            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }
    }

    private void restartGame() {
        System.out.println("[Main] Restarting game...");

        entities.clear();
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        entities.add(player);
        player.resetGame();

        fairies.clear();
        fairies.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairies.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));

        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        entities.addAll(fairies);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }


    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        List<Command> commands = inputHandler.handleInput(delta);

        for (Command command : commands) {
            command.execute(player, bulletManager);
        }
        player.clampToPlayfield();

        updateEnemyScheduler(delta);

        // Update and clean entities
        updateAndClean(
            entities,
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        bulletManager.update(
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        collisionReferee.resolveCollisions(player, entities, bulletManager);

        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Render objects without sprites using ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()
                && entity.getSprite() == null
                && entity.getAnimation() == null) {

                entity.render(shapeRenderer);
            }
        }

        shapeRenderer.end();

        // Render sprites and animations using SpriteBatch
        spriteBatch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()
                && (entity.getSprite() != null
                || entity.getAnimation() != null)) {

                entity.render(spriteBatch);
            }
        }

        bulletManager.render(spriteBatch);

        spriteBatch.end();
    }

    // Generic update and cleanup
    public <T extends GameObject> void updateAndClean(
        List<T> list,
        float delta,
        float screenWidth,
        float screenHeight) {

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {

            T entity = iterator.next();

            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight)
                || entity.isDestroyed()) {

                System.out.println(
                    "Removed via Generic Iterator: "
                        + entity.getClass().getSimpleName()
                );

                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        if (spriteBatch != null) {
            spriteBatch.dispose();
        }

        if (assetManager != null) {
            assetManager.dispose();
        }
    }
}

