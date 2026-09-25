package com.ahmadmadany.frontend;

import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.enemies.Boss;
import com.ahmadmadany.frontend.objects.enemies.Fairy;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    private Player player;
    private Fairy fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;

    private List<GameObject> entities;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        entities = new ArrayList<>();

        player = new Player(
            280f,
            40f,
            "Reimu Hakurei",
            100,
            15,
            3
        );


        fairy = new Fairy(
            150f,
            380f,
            "Stage 1 Fairy",
            20
        );

        boss = new Boss(
            380f,
            400f,
            "Cirno",
            150
        );

        pointItem = new Item(320, 480, 12, 12, 120f, ItemType.POINT, 1000L);

        powerItem = new Item(200, 450, 16, 16, 80f, ItemType.POWER, 500L);

        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(pointItem);
        entities.add(powerItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            entities.add(player.shootBullet());
        }

        updateAndClean(
            entities,
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

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

        // 2. Clear Screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 3. Polymorphic Render Loop: Draw hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()){
                entity.render(shapeRenderer);
            }
        }
        shapeRenderer.end();
    }

    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()){
            T entity = iterator.next();
            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()){
                System.out.println(
                    "Removed via Generic Iterator: " + entity.getClass().getSimpleName()
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
    }
}
