package com.ahmadmadany.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.patterns.ShootingPattern;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ahmadmadany.frontend.systems.BulletManager;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ahmadmadany.frontend.objects.patterns.entityStrategy.FixedMovement;



public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(380f, 400f, 64f, 64f, Color.BLUE, name, hp, 5000L);
        setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));
    }

    private void setShootingPattern(SpreadShot bulletDanmakuRed) {
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

}

// This is multilevel and hierarchical inheritance:
// GameObject -> Enemy -> Fairy/Boss
