package com.ahmadmadany.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.patterns.ShootingPattern;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ahmadmadany.frontend.systems.BulletManager;



public class Boss extends Enemy {

    private ShootingPattern shootingPattern =
        new SpreadShot(200f, 3, 30f);

    public Boss(String name, int hp) {
        super(380f, 400f, 64f, 64f, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64f, 64f, Color.BLUE, name, hp, 5000L);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player){
            System.out.println("Player touches boss");
        }
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager != null &&
            shootingPattern != null &&
            !isDestroyed()) {

            shootingPattern.execute(
                x + width / 2f - 8f,
                y,
                bulletManager,
                false
            );
        }
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }


}

// This is multilevel and hierarchical inheritance:
// GameObject -> Enemy -> Fairy/Boss
