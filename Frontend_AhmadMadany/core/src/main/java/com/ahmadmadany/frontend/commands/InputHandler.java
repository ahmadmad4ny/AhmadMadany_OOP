package com.ahmadmadany.frontend.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.ahmadmadany.frontend.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    private final ShootCommand shootCommand = new ShootCommand();
    private final BombCommand bombCommand = new BombCommand();
    private final FocusCommand focusOn = new FocusCommand(true);
    private final FocusCommand focusOff = new FocusCommand(false);
    private final MoveCommand moveCommand = new MoveCommand(0f, 0f);

    private final List<Command> activeCommands = new ArrayList<>();

    public InputHandler() {
    }

    public InputHandler(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    public void setEntities(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    public int getKeyShoot() {
        return keyShoot;
    }

    public void setKeyShoot(int keyShoot) {
        this.keyShoot = keyShoot;
    }

    public int getKeyBomb() {
        return keyBomb;
    }

    public void setKeyBomb(int keyBomb) {
        this.keyBomb = keyBomb;
    }

    public int getKeyFocus() {
        return keyFocus;
    }

    public void setKeyFocus(int keyFocus) {
        this.keyFocus = keyFocus;
    }

    public List<Command> handleInput(float delta) {
        activeCommands.clear();
        if (Gdx.input == null) return activeCommands;

        boolean isFocusHeld = Gdx.input.isKeyPressed(keyFocus) ||
            Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        activeCommands.add((isFocusHeld ? focusOn : focusOff));

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) ||
        Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) ||
            Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) ||
            Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += 1f;
        }

        float speedMult = isFocusHeld ? 0.45f : 1.0f;

        moveCommand.set(dx * delta *speedMult, dy * delta * speedMult);

        activeCommands.add(moveCommand);

        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        if (Gdx.input.isKeyPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }
}

