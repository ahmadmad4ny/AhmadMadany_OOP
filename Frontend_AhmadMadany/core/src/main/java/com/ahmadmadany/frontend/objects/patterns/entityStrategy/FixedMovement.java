package com.ahmadmadany.frontend.objects.patterns.entityStrategy;

import com.ahmadmadany.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {

    @Override
    public void move(GameObject entity, float delta) {
        // Entity stays in the same position
    }
}
