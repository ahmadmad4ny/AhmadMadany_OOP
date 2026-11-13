package com.ahmadmadany.frontend.objects.patterns.entityStrategy;

import com.ahmadmadany.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
