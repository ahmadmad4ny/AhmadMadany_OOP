package com.ahmadmadany.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;



public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(380f, 400f, 48f, 48f, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 48f, 48f, Color.BLUE, name, hp, 5000L);
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
