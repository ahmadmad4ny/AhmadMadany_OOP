package com.ahmadmadany.frontend.objects.enemies;
import com.ahmadmadany.frontend.objects.EntityShooter;
import com.ahmadmadany.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.ahmadmadany.frontend.objects.items.ItemType;

public class Enemy extends EntityShooter {

    private String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;
    protected ItemType itemDrop = null;
    protected boolean damageable = true;
    protected boolean collidable = true;
    protected float shotHitboxWidth = 0f;
    protected float shotHitboxHeight = 0f;

    public Enemy(String name, int hp) {
        super(200f, 380f, 24f, 24f, 0f, Color.PINK);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100L;
    }

    public Enemy(float x, float y, float width, float height, Color color,
                 String name, int hp, long scoreValue) {

        super(x, y, width, height, 0f, color);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getHp(){
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp(){
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getName() {
        return name;
    }

    public boolean takeDamage(int damage) {
        if (!damageable) return false;
        boolean wasAlive = isAlive();

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        System.out.println(
            name + " took " + damage + " damage! HP: "
                + this.hp + "/" + this.maxHp
        );

        if (wasAlive && this.hp == 0) {
            System.out.println(name + " was defeated!");
            destroy();
            return true;
        }

        return false;
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " unleashes bullet barrage on " + player.getName() + "!");

        player.takeDamage(damage);
    }

    public ItemType getItemDrop() { return itemDrop; }
    public void setItemDrop(ItemType itemDrop) { this.itemDrop = itemDrop; }
    public boolean isDamageable() { return damageable; }
    public void setDamageable(boolean damageable) { this.damageable = damageable; }
    public boolean isCollidable() { return collidable; }
    public void setCollidable(boolean collidable) { this.collidable = collidable; }
    public Rectangle getShotHitbox() {
        if (shotHitboxWidth <= 0f || shotHitboxHeight <= 0f) return getCoreHitbox();
        return new Rectangle(x + (width - shotHitboxWidth) / 2f,
            y + (height - shotHitboxHeight) / 2f, shotHitboxWidth, shotHitboxHeight);
    }
    public void setShotHitboxSize(float width, float height) {
        shotHitboxWidth = width;
        shotHitboxHeight = height;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
