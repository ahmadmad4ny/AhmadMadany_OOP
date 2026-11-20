package com.ahmadmadany.frontend.systems;

import com.ahmadmadany.frontend.objects.bullets.Bullet;
import com.ahmadmadany.frontend.objects.enemies.Enemy;
import com.ahmadmadany.frontend.objects.GameObject;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ahmadmadany.frontend.objects.BulletType;
import com.ahmadmadany.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.ahmadmadany.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.RingShot;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BulletManager {

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;
    private List<GameObject> entities;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage, String spriteKey) { // Add String spriteKey as a parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet"; // Add local String variable key
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key)); // Add setSprite using key
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key); // Add key as 4th argument
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage, String spriteKey) { // Add String spriteKey as a parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku"; // Add local String variable key
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key)); // Add setSprite using key
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key); // Add key as 4th argument
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }


    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy, List<GameObject> entities) {
        List<GameObject> targetEntities = entities != null ? entities : this.entities;
        RingShot ring = new RingShot(150f, 8, 35,
            new FantasySealMovement(targetEnemy, targetEntities));
        ring.execute(originX, originY, this, true);
        System.out.println("[BulletManager] Spawned 8 Fantasy Seal orbs!");
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy) {
        spawnBombOrbs(originX, originY, targetEnemy, null);
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIterator = activePlayerBullets.iterator();

        while (playerIterator.hasNext()) {

            Bullet bullet = playerIterator.next();

            if (entities != null && bullet.getMovementPattern() instanceof HomingBulletMovement homing && homing.getEntities() == null) {
                homing.setEntities(entities);
            }

            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight)
                || bullet.isDestroyed()) {

                bullet.destroy();
                playerIterator.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator = activeEnemyBullets.iterator();

        while (enemyIterator.hasNext()) {

            Bullet bullet = enemyIterator.next();

            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight)
                || bullet.isDestroyed()) {

                bullet.destroy();
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }  // - Enemy bullets that go off-screen or are destroyed are returned to enemyBulletPool via offer()
    }

    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> iterator = activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entities != null) {
                Item item = EntityFactory.createItem(
                    bullet.getX(),
                    bullet.getY(),
                    ItemType.POINT
                );
                entities.add(item);
            }

            bullet.destroy();
            iterator.remove();
            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();

        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) {
                b.render(batch);
            }
        }

        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) {
                b.render(batch);
            }
        }
    }

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }

    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }
}
