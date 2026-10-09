
package com.ahmadmadany.frontend.systems;

import com.ahmadmadany.frontend.Test;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.ahmadmadany.frontend.objects.Player;
import com.ahmadmadany.frontend.objects.BulletType;
import com.ahmadmadany.frontend.objects.bullets.Bullet;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.ahmadmadany.frontend.objects.enemies.Fairy;
import com.ahmadmadany.frontend.objects.enemies.Boss;
import org.w3c.dom.Text;

public class EntityFactory {

    // Factory: Create Player
    public static Player createPlayer(
        float x, float y,
        String name, int hp,
        int power, int spellCards) {

        Player player = new Player(
            x, y, name, hp, power, spellCards
        );

        Animation<TextureRegion> anim =
            AssetManager.getInstance()
                .getAnimation("player_idle");

        player.setAnimation(anim);

        return player;
    }

    public static Boss createBoss(float x, float y, String name, int hp) {
        Boss boss = new Boss(x, y, name, hp);
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("boss_idle");
        boss.setAnimation(idleAnim);
        return boss;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    public static Fairy createFairy(float x, float y, String name, int hp, String keyString) {
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation(keyString);
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    // Factory: Create Item
    public static Item createItem(
        float x, float y,
        ItemType itemType) {

        Item item = new Item(x, y, itemType);

        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB -> "item_bomb";
            case LIFE -> "item_life";
        };

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion(key);

        item.setSprite(sprite);

        return item;
    }


    // CS6 - Create Player Bullet with selected sprite
    public static Bullet createPlayerBullet(
        float x, float y, int damage, String spriteKey) {

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion(spriteKey);

        Bullet bullet = new Bullet(
            x, y, BulletType.AMULET, damage
        );

        bullet.setSprite(sprite);

        return bullet;
    }

    // CS6 - Create default Player Bullet
    public static Bullet createPlayerBullet(
        float x, float y, int damage) {

        return createPlayerBullet(
            x, y, damage, "bullet_amulet"
        );
    }


    // Factory: Create Enemy Bullet
    public static Bullet createEnemyBullet(
        float x, float y, int damage) {

        Bullet bullet = new Bullet(
            x, y, 0f,
            BulletType.DANMAKU,
            damage
        );

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion("bullet_danmaku");

        bullet.setSprite(sprite);

        return bullet;
    }
}
