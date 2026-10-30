package com.ahmadmadany.frontend.objects.patterns;

import com.ahmadmadany.frontend.systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY,
                 BulletManager bulletManager, boolean isPlayer);
}

