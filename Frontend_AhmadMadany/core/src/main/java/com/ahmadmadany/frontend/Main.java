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
import java.util.ArrayList;
import java.util.List;

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

        // 1. Polymorphic Update Loop: Items move downward automatically via Item.update(delta)
        for (GameObject obj : entities) {
            obj.update(delta);
        }

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (a.getCoreHitbox().overlaps(b.getCoreHitbox())){
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        // 2. Clear Screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 3. Polymorphic Render Loop: Draw hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject obj : entities) {
            obj.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
