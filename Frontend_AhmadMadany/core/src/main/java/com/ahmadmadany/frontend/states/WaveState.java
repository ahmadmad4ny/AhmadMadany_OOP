package com.ahmadmadany.frontend.states;

import com.ahmadmadany.frontend.systems.LevelWaveManager;

public interface WaveState {
    void onEnter(LevelWaveManager manager);
    void update(LevelWaveManager manager, float delta);
}
