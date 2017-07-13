package com.wastelandwarriors.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    private String type;
    private int speed;
    private Texture img;
    private int x;
    private int y;
    private int direction;
    private double dmg;
    private final int RIGHT = 4;
    private final int LEFT = 5;

    public Bullet(String type, int speed, Texture img, int x, int y, int direction) {
        this.type = type;
        this.speed = speed;
        this.img = img;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Bullet(String type, int speed, Texture img, int x, int y, int direction, double dmg) {
        this.type = type;
        this.speed = speed;
        this.img = img;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.dmg = dmg;
    }

    public int getX() {return x;}
    public void moveX() {
        if(direction == RIGHT) {
            x += speed;
        }
        else if(direction == LEFT){
            x -= speed;
        }
    }

    public int getY() {return y;}
    public void moveY() {
        y -= speed / 2;
    }

    public Rectangle getBox() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public int height() {return img.getHeight();}
    public int width(){return img.getWidth();}

    public Texture getImg() {return img;}
    public String getType() {return type;}
    public int getDirection() {return direction;}
    public double getDmg() {return dmg;}
}
