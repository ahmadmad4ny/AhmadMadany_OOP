
package com.ahmadmadany.frontend;

import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.enemies.Boss;
import com.ahmadmadany.frontend.objects.enemies.Enemy;
import com.ahmadmadany.frontend.objects.enemies.Fairy;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.ahmadmadany.frontend.systems.AssetManager;
import com.ahmadmadany.frontend.systems.EntityFactory;

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

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        entities = new ArrayList<>();
        fairies = new ArrayList<>();

        // Initialize graphics assets
        assetManager = AssetManager.getInstance();
        assetManager.init();

        // Create player using Factory Pattern
        player = EntityFactory.createPlayer(
            280f,
            40f,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        Fairy redFairy = EntityFactory.createFairy(
            150f, 380f, "Red Fairy", 20
        );

        Fairy blueFairy = EntityFactory.createFairy(
            250f, 380f, "Blue Fairy", 20, "fairy_idle_blue"
        );

        fairies.add(redFairy);
        fairies.add(blueFairy);

        boss = EntityFactory.createBoss(
            380f, 400f, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            200f, 450f, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            320f, 480f, ItemType.POINT
        );

        entities.add(player);
        entities.addAll(fairies);
        entities.add(boss);
        entities.add(pointItem);
        entities.add(powerItem);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // Shoot using Z
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            entities.add(player.shootBullet());
        }

        // Update and clean entities
        updateAndClean(
            entities,
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        // Collision detection
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {

                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed() && !b.isDestroyed()) {
                    if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        }

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

