package com.ahmadmadany.frontend.objects;

import com.ahmadmadany.frontend.objects.enemies.Enemy;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.ahmadmadany.frontend.objects.bullets.Bullet;
import com.ahmadmadany.frontend.systems.AssetManager;
import com.ahmadmadany.frontend.systems.EntityFactory;
import com.ahmadmadany.frontend.observers.GameObserver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ahmadmadany.frontend.systems.BulletManager;
import com.ahmadmadany.frontend.objects.patterns.ShootingPattern;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.SideShot;
import com.ahmadmadany.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.ahmadmadany.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.ahmadmadany.frontend.objects.patterns.entityStrategy.FixedMovement;
import java.util.List;
import java.util.ArrayList;


public class Player extends EntityShooter {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;
    private int lives = 3;
    private int grazeCount = 0;
    private boolean focused = false;
    private float shootCooldown = 5f / 60f;
    private float shootTimer = shootCooldown;
    private float homingCooldown = 30f / 60f;
    private float homingShootTimer = homingCooldown;
    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;
    private float invulnerableTimer = 0f;
    private int currentDir = 0;

    private final  List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(280f, 40f, 32f, 48f, 240f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32f, 48f, 240f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp); notifyHpChanged();
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = Math.max(0, Math.min(128, power));
        updatePowerStrategy();
        notifyPowerChanged();
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards; notifySpellCardsChanged();
    }

    public long getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)){
            observers.add(observer);

            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyScoreChanged(){
        for (GameObserver obs : observers){
            obs.onScoreChanged(score);
        }
    }

    public void notifyHpChanged(){
        for (GameObserver obs : observers){
            obs.onHpChanged(hp);
        }
    }

    public void notifyLivesChanged(){
        for (GameObserver obs : observers){
            obs.onLivesChanged(lives);
        }
    }

    public void notifySpellCardsChanged(){
        for (GameObserver obs : observers){
            obs.onSpellCardsChanged(spellCards);
        }
    }

    public void notifyPowerChanged(){
        for (GameObserver obs : observers){
            obs.onPowerChanged(power);
        }
    }

    public void notifyGrazeChanged(){
        for (GameObserver obs : observers){
            obs.onGrazeChanged(grazeCount);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        shootTimer += delta;
        homingShootTimer += delta;
        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();

        if (dx < 0) {
            if (currentDir != -1) {
                currentDir = -1;

                Animation<TextureRegion> anim =
                    assets.getAnimation("player_left");

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else if (dx > 0) {
            if (currentDir != 1) {
                currentDir = 1;

                Animation<TextureRegion> anim =
                    assets.getAnimation("player_right");

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else {
            if (currentDir != 0) {
                currentDir = 0;

                Animation<TextureRegion> anim =
                    assets.getAnimation("player_idle");

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        }
    }

    public void move(float dx, float dy) {
        this.x += dx * speed;
        this.y += dy * speed;

        clampToPlayfield();
    }

    public void clampToPlayfield() {
        float minX = 32f;
        float maxX = 416f - width;
        float minY = 16f;
        float maxY = 560f - height;

        x = Math.max(minX, Math.min(x, maxX));
        y = Math.max(minY, Math.min(y, maxY));
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(
            x + (width / 2f) - 4f,
            y + (height / 2f) - 4f,
            8f,
            8f
        );
    }

    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item){
            System.out.println("Player touches item");
            collectItem((Item) other);
        }
    }


    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(int damage, BulletManager bulletManager, List<GameObject> entities) {
        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(name + " is INVULNERABLE! (0 damage taken)");
            }
            return;
        }

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        notifyHpChanged();

        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);

        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(BulletManager bulletManager, List<GameObject> entities) {
        this.lives--;

        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            if (bulletManager != null) {
                System.out.println("[Player] " + name
                    + " lost a life! Remaining Lives: " + this.lives
                    + ". Respawning at (200, 50)...");
            }

            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println("[Player] GAME OVER! " + name
                    + " has run out of lives!");
            }

            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f;
        this.power = Math.max(10, this.power - 16);
        updatePowerStrategy();
        this.spellCards = 3;

        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 15;
        updatePowerStrategy();
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;

        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();

        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }


    public boolean isAlive() {
        return this.hp > 0 && this.lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }


    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
            System.out.println(getName() + " gained " + points + " pts! Total score: " + this.score);
        }
    }

    public void collectItem(Item item) {
        ItemType type = item.getItemTypeEnum();
        if (item.isDestroyed()) return;
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power = Math.min(128, this.power + type.getPowerBonus());
                    updatePowerStrategy();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POWER item! Power increased to " + power
                    );
                }
                case POINT -> {
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POINT item!"
                    );
                }
                case BOMB -> {
                    this.spellCards += 1;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected BOMB item! SpellCards: " + spellCards
                    );
                }
                case LIFE -> {
                    this.lives++;
                    this.hp = 100;
                    notifyLivesChanged();
                    notifyHpChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected LIFE item! Remaining Lives: " + lives
                    );
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }
        item.destroy();
    }


    public Bullet shootBullet() {
        int damage = 10 + power;

        System.out.println(
            name + " shoots bullet dealing " + damage + " DMG!"
        );

        return EntityFactory.createPlayerBullet(
            x + width / 2 - 4,
            y + height,
            damage
        );
    }

    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null || !isAlive()) return;
        float originX = x + width / 2f - 8f;
        float originY = y + height;
        if (shootTimer >= shootCooldown) {
            if (mainShootingPattern != null) {
                mainShootingPattern.execute(originX, originY, bulletManager, true);
            } else if (shootingPattern != null) {
                shootingPattern.execute(originX, originY, bulletManager, true);
            } else {
                bulletManager.spawnPlayerBullet(originX, originY, 0f, 400f, 10 + power);
            }
            shootTimer = 0f;
        }
        if (sideShootingPattern != null && homingShootTimer >= homingCooldown) {
            sideShootingPattern.execute(originX, originY, bulletManager, true);
            homingShootTimer = 0f;
        }
    }

    public ShootingPattern getMainShootingPattern() { return mainShootingPattern; }
    public ShootingPattern getSideShootingPattern() { return sideShootingPattern; }
    public float getShootCooldown() { return shootCooldown; }
    public float getHomingCooldown() { return homingCooldown; }

    private SideShot homingSide(float speed, int damage, float h, float v, float left, float right) {
        return new SideShot(speed, damage, h, v, left, right, true,
            new HomingBulletMovement(360f), "bullet_amulet_homing");
    }

    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;
        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;
        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;
        float normalSpeed = 720f;
        float homingSpeed = 500f;

        if (power < 8) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;
            homingCooldown = 30f / 60f;
        } else if (power < 16) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = homingSide(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle);
            homingCooldown = 30f / 60f;
        } else if (power < 32) {
            mainShootingPattern = new SpreadShot(normalSpeed, 2, dualSpreadAngle, 30, 8f, null, null, null);
            sideShootingPattern = homingSide(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle);
            homingCooldown = 30f / 60f;
        } else if (power < 48) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = homingSide(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle);
            homingCooldown = 30f / 60f;
        } else if (power < 64) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = homingSide(homingSpeed, 12, sideHOffset, sideVOffset, leftAngle, rightAngle);
            homingCooldown = 15f / 60f;
        } else if (power < 80) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                homingSide(homingSpeed, 9, sideHOffset, sideVOffset, leftAngle, rightAngle),
                homingSide(homingSpeed, 12, sideHOffset + 4f, sideVOffset, leftAngle + 30f, rightAngle - 30f));
            homingCooldown = 15f / 60f;
        } else if (power < 128) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                homingSide(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f),
                homingSide(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f));
            homingCooldown = 15f / 60f;
        } else {
            mainShootingPattern = new SpreadShot(normalSpeed, 4, spreadAngle * 1.2f, 24, 16f, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(
                homingSide(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f),
                homingSide(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f),
                homingSide(homingSpeed, 8, sideHOffset + 8f, sideVOffset, leftAngle + 30f, rightAngle - 30f));
            homingCooldown = 15f / 60f;
        }
        if (sideShootingPattern != null) {
            setShootingPattern(new CompositeShootingPattern(mainShootingPattern, sideShootingPattern));
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards <= 0 || isGameOver()) return;
        spellCards--;
        notifySpellCardsChanged();
        invulnerableTimer = 5.0f;
        if (bulletManager != null) {
            bulletManager.clearEnemyBullets(entities);
            GameObject target = null;
            if (entities != null) {
                for (GameObject obj : entities) {
                    if (obj instanceof Enemy enemy && !enemy.isDestroyed()) {
                        target = enemy;
                        break;
                    }
                }
            }
            bulletManager.spawnBombOrbs(x + width / 2f, y + height / 2f, target, entities);
        }
        System.out.println(name + " unleashes SPELL CARD: Fantasy Seal!");
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {

            if (invulnerableTimer > 0f) {
                boolean visible = ((int) (invulnerableTimer * 16)) % 2 == 0;
                if (!visible) return;
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }

            super.render(batch);

            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f);
            }
        }
    }

    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                4f
            );

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                2f
            );
            shapeRenderer.end();
        }
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
    }

    public int getGrazeCount() {
        return grazeCount;
    }

}
