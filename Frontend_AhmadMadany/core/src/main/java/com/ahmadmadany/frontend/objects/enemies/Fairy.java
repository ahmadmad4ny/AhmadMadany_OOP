package com.ahmadmadany.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.ahmadmadany.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;

public class Fairy extends Enemy {
    private float shootTimer = 0f;
    private float shootDelay = 0.67f;
    private float shootInterval = 1.4f;
    private int maxShots = 2;
    private int shotsFired = 0;


    public Fairy(String name, int hp) {
        super(150f, 380f, 24f, 24f, Color.PINK, name, hp, 500L);
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
        setShotHitboxSize(28f, 28f);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24f, 24f, Color.PINK, name, hp, 500L);
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
        setShotHitboxSize(28f, 28f);
    }

    public Fairy(float x, float y, String name, int hp, float shootDelay, float shootInterval, int maxShots) {
        this(x, y, name, hp);
        setShootTiming(shootDelay, shootInterval, maxShots);
    }

    public boolean canShoot(float delta) {
        shootTimer += delta;
        if (shotsFired == 0 && maxShots > 0 && shootTimer >= shootDelay) {
            shotsFired++;
            shootTimer = 0f;
            return true;
        }
        if (shotsFired > 0 && shotsFired < maxShots && shootTimer >= shootInterval) {
            shotsFired++;
            shootTimer = 0f;
            return true;
        }
        return false;
    }

    public void setShootTiming(float shootDelay, float shootInterval, int maxShots) {
        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = maxShots;
        this.shootTimer = 0f;
        this.shotsFired = 0;
    }

    public int getShotsFired() { return shotsFired; }
    public float getShootTimer() { return shootTimer; }
    public float getShootDelay() { return shootDelay; }
    public float getShootInterval() { return shootInterval; }
    public int getMaxShots() { return maxShots; }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches fairy");
        }
    }
}
