package com.ahmadmadany.frontend;

import com.badlogic.gdx.graphics.Color;

public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(380f, 400f, 48f, 48f, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 48f, 48f, Color.BLUE, name, hp, 5000L);
    }
}

// This is multilevel and hierarchical inheritance:
// GameObject -> Enemy -> Fairy/Boss
