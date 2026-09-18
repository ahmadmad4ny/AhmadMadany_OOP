package com.ahmadmadany.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ahmadmadany.frontend.objects.Collidable;
import com.ahmadmadany.frontend.objects.Player;

public class Fairy extends Enemy {

    public Fairy(String name, int hp) {
        super(150f, 380f, 24f, 24f, Color.PINK, name, hp, 500L);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24f, 24f, Color.PINK, name, hp, 500L);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches fairy");
        }
    }
}
