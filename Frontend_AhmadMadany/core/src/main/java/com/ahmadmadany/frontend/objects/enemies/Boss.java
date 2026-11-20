package com.ahmadmadany.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ahmadmadany.frontend.objects.patterns.entityStrategy.FixedMovement;



public class Boss extends Enemy {
    private boolean survivesAtZeroHp = false;

    public Boss(String name, int hp) {
        super(380f, 400f, 64f, 64f, Color.BLUE, name, hp, 5000L);
        setShootingPattern(new SpreadShot(200f, 3, 30f));
        setMovementPattern(new FixedMovement());
        setShotHitboxSize(48f, 56f);
    }

public Boss(float x, float y, String name, int hp) {
        super(x, y, 64f, 64f, Color.BLUE, name, hp, 5000L);
        setShootingPattern(new SpreadShot(200f, 3, 30f));
        setMovementPattern(new FixedMovement());
        setShotHitboxSize(48f, 56f);
    }

    public void setLife(int life) {
        this.hp = Math.max(0, life);
        this.maxHp = this.hp;
    }

    @Override
    public boolean takeDamage(int damage) {
        if (!damageable) return false;
        if (!survivesAtZeroHp) return super.takeDamage(damage);
        this.hp = Math.max(0, this.hp - damage);
        System.out.println(nameForLog() + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        return false;
    }

    private String nameForLog() { return getName(); }
    public boolean isSurvivesAtZeroHp() { return survivesAtZeroHp; }
    public void setSurvivesAtZeroHp(boolean value) { this.survivesAtZeroHp = value; }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player){
            System.out.println("Player touches boss");
        }
    }

}

// This is multilevel and hierarchical inheritance:
// GameObject -> Enemy -> Fairy/Boss
