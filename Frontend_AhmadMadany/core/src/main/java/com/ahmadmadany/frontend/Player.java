package com.ahmadmadany.frontend;

public class Player {
    String name;
    int hp;
    int power;
    int spellCards;

    public Player(String name, int hp, int power, int spellCards) {
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public void takeDamage(int damage) {
        hp = hp - damage;

       if (hp < 0){
           hp = 0;
       }

       if (hp > 0){
           System.out.println(name + " took " + damage + " damage! Remaining HP: " + hp);
       } else {
           System.out.println(name + " was defeated!");
       }
    }

    public void shoot(Enemy target) {
        int damage = 10 + power;

        System.out.println(name + " shoots " + target.name + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
