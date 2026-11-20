package com.ahmadmadany.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ahmadmadany.frontend.commands.Command;
import com.ahmadmadany.frontend.commands.InputHandler;
import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.observers.GameObserver;
import com.ahmadmadany.frontend.states.StageClearState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StageFacade {
    private static final float PLAYFIELD_WIDTH = 416f;
    private static final float PLAYFIELD_HEIGHT = 464f;

    private final List<GameObject> entities = new ArrayList<>();
    private final BulletManager bulletManager = new BulletManager();
    private final CollisionReferee collisionReferee = new CollisionReferee();
    private final InputHandler inputHandler = new InputHandler();
    private final Player player;
    private LevelWaveManager waveManager;

    public StageFacade(GameObserver hud) {
        player = EntityFactory.createPlayer(200f, 50f, "Reimu Hakurei", 100, 0, 3);
        player.registerObserver(hud);
        entities.add(player);
        bulletManager.setEntities(entities);
        inputHandler.setEntities(entities);
        waveManager = new LevelWaveManager(entities, bulletManager, player);
    }

    public void update(float delta) {
        if (isStageClear() || isGameOver()) return;
        for (Command command : inputHandler.handleInput(delta)) {
            command.execute(player, bulletManager);
        }
        player.clampToPlayfield();
        waveManager.update(delta);
        updateAndClean(entities, delta, PLAYFIELD_WIDTH, PLAYFIELD_HEIGHT);
        bulletManager.update(delta, PLAYFIELD_WIDTH, PLAYFIELD_HEIGHT);
        collisionReferee.resolveCollisions(player, entities, bulletManager);
    }

    public void render(SpriteBatch batch) {
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) entity.render(batch);
        }
        bulletManager.render(batch);
    }

    public void renderOverlay(ShapeRenderer shapeRenderer) {
        player.renderFocusIndicator(shapeRenderer);
    }

    public boolean isGameOver() { return player.isGameOver(); }
    public boolean isStageClear() {
        return waveManager != null && waveManager.getCurrentState() instanceof StageClearState;
    }
    public long getScore() { return player.getScore(); }

    public void restart() {
        entities.clear();
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();
        player.resetGame();
        entities.add(player);
        waveManager = new LevelWaveManager(entities, bulletManager, player);
    }

    public <T extends GameObject> void updateAndClean(List<T> list, float delta,
                                                       float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);
            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                iterator.remove();
            }
        }
    }
}
