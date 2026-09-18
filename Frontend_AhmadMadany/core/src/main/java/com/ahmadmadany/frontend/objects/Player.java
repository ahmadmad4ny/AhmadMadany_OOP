package com.ahmadmadany.frontend.objects;

import com.ahmadmadany.frontend.objects.enemies.Enemy;
import com.ahmadmadany.frontend.objects.items.Item;
import com.ahmadmadany.frontend.objects.items.ItemType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280f, 40f, 32f, 32f, 200f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32f, 32f, 200f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public long getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void update(float delta) {
        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) ||
            Gdx.input.isKeyPressed(Input.Keys.UP)){
                y += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                y -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                x -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                x += speed * delta;
            }
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item){
            System.out.println("Player touches item");
            collectItem((Item) other);
        }
    }

    public void takeDamage(int damage) {
        setHp(getHp() - damage);

        System.out.println(getName() + " took " + damage + " damage! HP: " + getHp());

        if (getHp() == 0) {
            System.out.println(getName() + " was defeated:");
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();

        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained " + points + " pts! Total score: " + this.score);
        }
    }

    public void collectItem(Item item) {
        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
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
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected BOMB item! SpellCards: " + spellCards
                    );
                }
                case LIFE -> {
                    this.hp += 20;
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected LIFE item! HP: " + hp
                    );
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }
    }
}
