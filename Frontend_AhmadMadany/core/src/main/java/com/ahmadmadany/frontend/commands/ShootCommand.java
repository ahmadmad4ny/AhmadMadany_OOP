package com.ahmadmadany.frontend.commands;

import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.systems.BulletManager;

public class ShootCommand implements Command {

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.shootBullet(bulletManager);
        }
    }
}
