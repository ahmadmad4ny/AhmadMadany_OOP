
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
import com.ahmadmadany.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.ahmadmadany.frontend.objects.patterns.entityStrategy.SCurveMovement;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;
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
    private float phaseTimer = 0f;
    private int currentPhase = 0;
    private float waveTimer = 0f;
    private int wave1SpawnIndex = 0;

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

        boss = EntityFactory.createBoss(200f, 420f, "Rumia", 150);
        boss.setMovementPattern(new TargetPointMovement(200f, 420f, 150f));
        boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));

        powerItem = EntityFactory.createItem(
            180f, 450f, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220f, 480f, ItemType.POINT
        );

        entities.add(player);
        entities.add(boss);
        entities.add(pointItem);
        entities.add(powerItem);

        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);
    }

    private void updateFairyWave(float delta) {
        float[] spawnX = {60f, 68f, 76f, 84f, 92f};
        waveTimer += delta;
        if (wave1SpawnIndex < spawnX.length && waveTimer >= 1.0f + wave1SpawnIndex * 0.267f) {
            Fairy fairy = EntityFactory.createFairy(spawnX[wave1SpawnIndex++], 480f,
                "Left S-Curve Fairy", 8, "fairy_idle_blue");
            fairy.setMovementPattern(new SCurveMovement(130f, (float)Math.toRadians(270), false));
            fairy.setShootingPattern(null);
            fairies.add(fairy);
            entities.add(fairy);
        }
    }

    private void resetFairyWave() {
        waveTimer = 0f;
        wave1SpawnIndex = 0;
    }

    private void updateEnemyScheduler(float delta) {
        updateFairyWave(delta);
        fairies.removeIf(f -> f.isDestroyed() || f.isOffScreen(416, 560));
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1.5f && boss != null && !boss.isDestroyed()) {
            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }
        phaseTimer += delta;
        if (phaseTimer >= 4.0f && boss != null && !boss.isDestroyed()) {
            phaseTimer = 0f;
            currentPhase = (currentPhase + 1) % 2;
            if (currentPhase == 0) {
                boss.setMovementPattern(new TargetPointMovement(120f, 420f, 150f));
                boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));
            } else {
                boss.setMovementPattern(new TargetPointMovement(280f, 420f, 150f));
                boss.setShootingPattern(new AlternatingRingShot(180f, 16, 10));
            }
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
        resetFairyWave();
        boss = EntityFactory.createBoss(200f, 420f, "Rumia", 150);
        boss.setMovementPattern(new TargetPointMovement(200f, 420f, 150f));
        boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));
        entities.add(boss);
        currentPhase = 0;
        phaseTimer = 0f;
        enemyShootTimer = 0f;
    }


    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null &&
            Gdx.input.isKeyJustPressed(Input.Keys.R) &&
            player.isGameOver()) {

            restartGame();
        }

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
            416,
            560
        );

        bulletManager.update(
            delta,
            416,
            560
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
        gameHUD.render(spriteBatch);

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

        if (gameHUD != null) {
            gameHUD.dispose();
        }

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

