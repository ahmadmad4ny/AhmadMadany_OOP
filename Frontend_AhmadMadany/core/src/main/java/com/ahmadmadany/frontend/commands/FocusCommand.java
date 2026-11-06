package com.ahmadmadany.frontend.commands;

import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.systems.BulletManager;

public class FocusCommand implements Command {

    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.setFocused(focused);
        }
    }
}

