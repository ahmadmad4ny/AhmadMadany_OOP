package com.ahmadmadany.frontend.commands;

import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.systems.BulletManager;

public class MoveCommand implements Command {

    private float dx;
    private float dy;

    public MoveCommand(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void set(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.move(dx, dy);
            player.updateAnimationState(dx);
        }
    }
}
