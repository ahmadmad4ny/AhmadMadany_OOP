package com.ahmadmadany.frontend;

public class Enemy {
    String name;
    int hp;
    int maxHp;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }
}
