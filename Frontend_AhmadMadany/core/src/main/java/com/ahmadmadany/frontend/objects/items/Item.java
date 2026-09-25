package com.ahmadmadany.frontend.objects.items;

import com.ahmadmadany.frontend.objects.GameObject;
import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;

public class Item extends GameObject {

    private String itemType;
    private long scoreValue;
    private ItemType itemTypeEnum;
    private boolean collected = false;

    public Item(float x, float y, String itemType) {
        super(x, y, 16f, 16f, 100f, Color.WHITE);

        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType) {
        super(x, y, width, height, speed, Color.WHITE);

        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, ItemType itemTypeEnum) {
        super(x, y, 16f, 16f, 100f, Color.WHITE);

        this.itemTypeEnum = itemTypeEnum;
        this.itemType = itemTypeEnum.name();
        this.scoreValue = itemTypeEnum.getScoreValue();
    }

    public Item(float x, float y, float width, float height, float speed, ItemType itemTypeEnum, long scoreValue) {
        super(x, y, width, height, speed, Color.WHITE);

        this.itemTypeEnum = itemTypeEnum;
        this.itemType = itemTypeEnum.name();
        this.scoreValue = scoreValue;
    }

    public Item(float x, float y, float width, float height, float speed,
                String itemType, long scoreValue) {

        super(x, y, width, height, speed, Color.WHITE);

        this.itemType = itemType;
        this.scoreValue = scoreValue;
    }

    public String getItemType() {
        return itemType;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public ItemType getItemTypeEnum() {
        return itemTypeEnum;
    }

    @Override
    public void update(float delta) {
        y = y - speed * delta;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            // Item pickup is handled on the Player side via collectItem()
        }
    }

    // Item overrides update because items move downward automatically.
    // Player and Enemy do not need this automatic movement.
}
