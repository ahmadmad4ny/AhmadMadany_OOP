package com.ahmadmadany.frontend;

import com.badlogic.gdx.graphics.Color;

public class Fairy extends Enemy {

    public Fairy(String name, int hp) {
        super(150f, 380f, 24f, 24f, Color.PINK, name, hp, 500L);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24f, 24f, Color.PINK, name, hp, 500L);
    }
}
