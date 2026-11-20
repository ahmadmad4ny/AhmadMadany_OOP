package com.ahmadmadany.frontend.systems;

import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.bullets.Bullet;
import com.ahmadmadany.frontend.objects.enemies.Enemy;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {
    private int defeatCounter = 0;
    private int itemDropCount = 0;

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager) {

        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {

                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {

                    if (bullet.getShotHitbox().overlaps(enemy.getShotHitbox())) {
                        boolean defeated = enemy.isDamageable() && enemy.takeDamage(bullet.getDamage());
                        bullet.destroy();
                        if (defeated) {
                            player.addScore(enemy.getScoreValue());
                            ItemType dropType = enemy.getItemDrop();
                            if (dropType == null && defeatCounter++ % 3 == 0) {
                                dropType = (itemDropCount++ % 2 == 0) ? ItemType.POWER : ItemType.POINT;
                            }
                            if (dropType != null) {
                                newDrops.add(EntityFactory.createItem(enemy.getX(), enemy.getY(), dropType));
                            }
                        }
                        break;
                    }
                }
            }
        }

        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

            if (bullet.isDestroyed()) continue;

            if (!player.isDestroyed()) {

                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {

                    player.takeDamage(bullet.getDamage());
                    bullet.destroy();

                } else if (!bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {

                    bullet.setGrazed(true);
                    player.addGraze();

                    System.out.println(
                        "[CollisionReferee] GRAZE! " +
                            player.getName() +
                            " grazed an enemy bullet (+50 pts)!"
                    );
                }
            }
        }

        for (GameObject entity : entities) {

            if (entity.isDestroyed() || player.isDestroyed()) {
                continue;
            }

            if (entity instanceof Item item) {

                if (item.getCoreHitbox().overlaps(player.getCoreHitbox())
                    || item.getCoreHitbox().overlaps(player.getGrazeHitbox())) {

                    player.collectItem(item);
                }

            } else if (entity instanceof Enemy enemy && enemy.isCollidable()) {

                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                    if (!player.isInvulnerable()){
                        System.out.println("[CollisionReferee] CORE COLLISION! Player collided with " + enemy.getName() + "!");
                        player.takeDamage(100, bulletManager, entities);
                    }
                }
            }
        }

        entities.addAll(newDrops);
    }
}
