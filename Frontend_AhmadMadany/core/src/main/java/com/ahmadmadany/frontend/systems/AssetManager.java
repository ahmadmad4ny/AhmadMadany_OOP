
package com.ahmadmadany.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    // Singleton
    private static AssetManager instance;

    // Flyweight caches
    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    // Private constructor
    private AssetManager() {
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    // Load texture with caching
    public Texture loadTexture(String filename) {
        if (!textureMap.containsKey(filename)) {
            if (Gdx.files == null ||
                !Gdx.files.internal(filename).exists()) {
                return null;
            }

            Texture texture =
                new Texture(Gdx.files.internal(filename));

            textureMap.put(filename, texture);
        }

        return textureMap.get(filename);
    }

    // Register a single texture region
    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    // Register region from sprite sheet
    public void registerRegionFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int col) {

        Texture tex = loadTexture(filename);

        if (tex != null) {
            TextureRegion[][] grid =
                TextureRegion.split(tex, tileWidth, tileHeight);

            if (row >= 0 && row < grid.length &&
                col >= 0 && col < grid[row].length) {
                textureRegionMap.put(key, grid[row][col]);
            }
        }
    }

    // Default looping animation
    public void registerAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int numFrames,
        float frameDuration) {

        registerAnimationFromSheet(
            key, filename, tileWidth, tileHeight,
            row, 0, numFrames, frameDuration,
            Animation.PlayMode.LOOP
        );
    }

    // Register animation
    public void registerAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int startCol,
        int numFrames,
        float frameDuration,
        Animation.PlayMode playMode) {

        Texture tex = loadTexture(filename);

        if (tex != null) {
            TextureRegion[][] grid =
                TextureRegion.split(tex, tileWidth, tileHeight);

            if (row < 0 || row >= grid.length ||
                startCol < 0 || numFrames <= 0 ||
                startCol + numFrames > grid[row].length) {
                return;
            }

            TextureRegion[] frames =
                new TextureRegion[numFrames];

            for (int i = 0; i < numFrames; i++) {
                frames[i] = grid[row][startCol + i];
            }

            Animation<TextureRegion> anim =
                new Animation<>(frameDuration, frames);

            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // Default flipped animation
    public void registerFlippedAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int numFrames,
        float frameDuration,
        boolean flipX,
        boolean flipY) {

        registerFlippedAnimationFromSheet(
            key, filename, tileWidth, tileHeight,
            row, 0, numFrames, frameDuration,
            Animation.PlayMode.LOOP,
            flipX, flipY
        );
    }

    // Register flipped animation
    public void registerFlippedAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int startCol,
        int numFrames,
        float frameDuration,
        Animation.PlayMode playMode,
        boolean flipX,
        boolean flipY) {

        Texture tex = loadTexture(filename);

        if (tex != null) {
            TextureRegion[][] grid =
                TextureRegion.split(tex, tileWidth, tileHeight);

            if (row < 0 || row >= grid.length ||
                startCol < 0 || numFrames <= 0 ||
                startCol + numFrames > grid[row].length) {
                return;
            }

            TextureRegion[] frames =
                new TextureRegion[numFrames];

            for (int i = 0; i < numFrames; i++) {
                frames[i] =
                    new TextureRegion(grid[row][startCol + i]);

                frames[i].flip(flipX, flipY);
            }

            Animation<TextureRegion> anim =
                new Animation<>(frameDuration, frames);

            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // Get texture region
    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    // Get animation
    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    // Initialize game assets
    public void init() {

        // Player Reimu (32x48)
        registerAnimationFromSheet(
            "player_idle",
            "player.png",
            32, 48,
            0, 0, 8, 0.125f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "player_left",
            "player.png",
            32, 48,
            1, 0, 4, 0.12f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "player_right",
            "player.png",
            32, 48,
            2, 0, 4, 0.12f,
            Animation.PlayMode.LOOP
        );

        // Boss Rumia (64x64)
        registerAnimationFromSheet(
            "boss_idle",
            "rumia.png",
            64, 64,
            0, 4, 0.15f
        );


        // Boss Moving Left
        registerAnimationFromSheet(
            "boss_left",
            "rumia.png",
            64, 64,
            1, 0, 4, 0.15f,
            Animation.PlayMode.REVERSED
        );

        // Boss Moving Right
        registerAnimationFromSheet(
            "boss_right",
            "rumia.png",
            64, 64,
            2, 0, 4, 0.15f,
            Animation.PlayMode.NORMAL
        );


        // Fairy animations
        registerAnimationFromSheet(
            "fairy_idle",
            "fairy.png",
            32, 32,
            0, 4, 0.15f
        );

        registerFlippedAnimationFromSheet(
            "fairy_left",
            "fairy.png",
            32, 32,
            0, 4, 0.15f,
            true, false
        );

        registerAnimationFromSheet(
            "fairy_right",
            "fairy.png",
            32, 32,
            0, 4, 0.15f
        );


        // CS6 - Player Bullet
        registerRegionFromSheet(
            "bullet_amulet",
            "amulet_reimu.png",
            16, 16,
            0, 0
        );

        // CS6 - Homing Bullet
        registerRegionFromSheet(
            "bullet_amulet_homing",
            "amulet_reimu.png",
            16, 16,
            1, 0
        );


        // Enemy bullet (row 2, col 3)
        registerRegionFromSheet(
            "bullet_danmaku",
            "bullets_small.png",
            16, 16,
            2, 3
        );

        // Item variants
        registerRegionFromSheet(
            "item_power",
            "items.png",
            16, 16,
            0, 0
        );

        registerRegionFromSheet(
            "item_point",
            "items.png",
            16, 16,
            0, 1
        );

        registerRegionFromSheet(
            "item_bomb",
            "items.png",
            16, 16,
            0, 2
        );

        registerRegionFromSheet(
            "item_life",
            "items.png",
            16, 16,
            0, 3
        );

        registerAnimationFromSheet(
            "fairy_idle_red",
            "fairy.png",
            32, 32,
            1, 0, 8, 0.125f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "fairy_idle_blue",
            "fairy.png",
            32, 32,
            0, 0, 8, 0.125f,
            Animation.PlayMode.LOOP
        );

    }

    // Clean up GPU resources
    public void dispose() {
        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }

        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}
