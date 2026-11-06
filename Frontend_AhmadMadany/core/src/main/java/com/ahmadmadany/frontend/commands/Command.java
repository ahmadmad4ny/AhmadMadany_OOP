package com.ahmadmadany.frontend.commands;

import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}

