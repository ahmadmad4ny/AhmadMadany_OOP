package com.ahmadmadany.frontend.states;

import com.ahmadmadany.frontend.objects.enemies.Boss;
import com.ahmadmadany.frontend.systems.LevelWaveManager;

public class StageClearState implements WaveState {
    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[Stage] Stage 1 Clear!");
        Boss boss = manager.getBoss();
        if (boss != null && !boss.isDestroyed()) boss.destroy();
    }
    @Override
    public void update(LevelWaveManager manager, float delta) {}
}
