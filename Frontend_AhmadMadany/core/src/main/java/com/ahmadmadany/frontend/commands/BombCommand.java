package com.ahmadmadany.frontend.commands;

import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.systems.BulletManager;

import java.util.List;

public class BombCommand implements Command {

    private List<GameObject> entities;

    public BombCommand() {
    }

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.useBomb(bulletManager, entities);
        }
    }
}
