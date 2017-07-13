package com.wastelandwarriors.game.Entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.wastelandwarriors.game.handlers.CombatManager;

public class Enemy extends ApplicationAdapter {

    private enum Screens{
        RUNNING, PAUSED;
    }
    Screens screenState;

    private String name;
    private int enemyX;
    private int enemyY;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private CombatManager cm;
    private TextureAtlas deathAtlas;
    private Animation deathAnimation;
    private float deathTimePassed;
    private TextureRegion deathFrame;
    private TextureAtlas attack1Atlas;
    private Animation attack1Animation;
    private float attack1TimePassed;
    private TextureRegion attack1Frame;
    private TextureAtlas attack2Atlas;
    private Animation attack2Animation;
    private float attack2TimePassed;
    private TextureRegion attack2Frame;
    private Texture img;
    private Texture walk;
    private Texture jump;
    private Texture fall;
    private Texture bullet;
    private double gravity;
    private double vel;
    private int State;
    private int LastState;
    private final int FALL = 1;
    private final int ATTACK1 = 2;
    private final int ATTACK2 = 3;
    private int STOPATTACK1;
    private int STOPATTACK2;
    private final int RIGHT = 4;
    private final int LEFT = 5;
    private final int WALK = 6;
    private final int ATTACKDELAY1 = 50;
    private final int ATTACKDELAY2 = 75;
    private int COUNTWALK = 50;
    private int COUNTATTACK;
    private int ATTACKCOOLDOWN;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private int SPEED = 4;
    private double HEALTH;
    private double DAMAGE1;
    private double DAMAGE2;

    public Enemy(String name, int enemyX, int enemyY, CombatManager cm) {
        this.name = name;
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.cm = cm;
        batch = cm.getBatch();
        shape = new ShapeRenderer();
        create();
    }

    @Override
    public void create() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map2.tmx");
        attack1TimePassed = 0;
        attack2TimePassed = 0;
        deathTimePassed = 0;
        gravity = 0.35;
        vel = 0;
        COUNTATTACK = 0;
        LastState = LEFT;
        State = LEFT;
        ATTACKCOOLDOWN = 0;
        screenState = Screens.RUNNING;

        if(name.equals("beetle")){
            img = new Texture("MechBeetle/Idle.png");
            bullet = new Texture("MechBeetle/Bullet.png");
            attack1Atlas = new TextureAtlas(Gdx.files.internal("MechBeetle/attack1.txt"));
            attack1Animation = new Animation(0.13f, attack1Atlas.getRegions());
            attack2Atlas = new TextureAtlas(Gdx.files.internal("MechBeetle/attack2.txt"));
            attack2Animation = new Animation(0.1f, attack2Atlas.getRegions());
            STOPATTACK1 = 33;
            STOPATTACK2 = 20;
            HEALTH = 20;
            DAMAGE1 = 1;
            DAMAGE2 = 2;
        }
        else if(name.equals("rhino")){
            img = new Texture("MechRhino/Idle.png");
            bullet = new Texture("MechRhino/Bullet.png");
            walk = new Texture("MechRhino/walk.png");
            attack2Atlas = new TextureAtlas(Gdx.files.internal("MechRhino/attack2.txt"));
            attack2Animation = new Animation(0.1f, attack2Atlas.getRegions());
            STOPATTACK2 = 40;
            HEALTH = 15;
            DAMAGE1 = 0;
            DAMAGE2 = 4;
        }
        else if(name.equals("tiger")){
            img = new Texture("MechTiger/Idle.png");
            walk = new Texture("MechTiger/walk.png");
            attack1Atlas = new TextureAtlas(Gdx.files.internal("MechTiger/attack1.txt"));
            attack1Animation = new Animation(0.12f, attack1Atlas.getRegions());
            STOPATTACK1 = 20;
            HEALTH = 10 * cm.getDifficulty();
            DAMAGE1 = 4 * cm.getDifficulty();
            DAMAGE2 = 0 * cm.getDifficulty();
        }
        else if(name.equals("boss1")){
            img = new Texture("BossOne/Idle.png");
            bullet = new Texture("BossOne/Bullet.png");
            attack2Atlas = new TextureAtlas(Gdx.files.internal("BossOne/attack1.txt"));
            attack2Animation = new Animation(0.1f, attack2Atlas.getRegions());
            STOPATTACK2 = 1000;
            HEALTH = 50 * cm.getDifficulty();
            DAMAGE1 = 0 * cm.getDifficulty();
            DAMAGE2 = 10 * cm.getDifficulty();
        }
        else if(name.equals("boss2")){
            img = new Texture("BossTwo/Idle.png");
            bullet = new Texture("BossTwo/Bullet.png");
            jump = new Texture("BossTwo/jumping.png");
            fall = new Texture("BossTwo/falling.png");
            attack2Atlas = new TextureAtlas(Gdx.files.internal("BossTwo/attack2.txt"));
            attack2Animation = new Animation(0.15f, attack2Atlas.getRegions());
            STOPATTACK1 = 20;
            STOPATTACK2 = 40;
            HEALTH = 40 * cm.getDifficulty();
            DAMAGE1 = 10 * cm.getDifficulty();
            DAMAGE2 = 10 * cm.getDifficulty();
        }
        else if(name.equals("boss3")){
            img = new Texture("BossThree/Idle.png");
            bullet = new Texture("BossThree/Bullet.png");
            attack1Atlas = new TextureAtlas(Gdx.files.internal("BossThree/attack1.txt"));
            attack1Animation = new Animation(0.2f, attack1Atlas.getRegions());
            attack2Atlas = new TextureAtlas(Gdx.files.internal("BossThree/attack2.txt"));
            attack2Animation = new Animation(1f, attack2Atlas.getRegions());
            STOPATTACK1 = 30;
            STOPATTACK2 = 400;
            HEALTH = 30 * cm.getDifficulty();
            DAMAGE1 = 5 * cm.getDifficulty();
            DAMAGE2 = 15 * cm.getDifficulty();
        }
    }

    @Override
    public void render() {
        if(deathAnimation != null){
            deathFrame = (TextureRegion) deathAnimation.getKeyFrame(deathTimePassed, true);
        }
        if(attack1Animation != null){
            attack1Frame = (TextureRegion) attack1Animation.getKeyFrame(attack1TimePassed, true);

        }
        if(attack2Animation != null){
            attack2Frame = (TextureRegion) attack2Animation.getKeyFrame(attack2TimePassed, true);
        }

        switch(screenState){
            case RUNNING:

                if(State == FALL){
                    jump();
                }
                else if(State == ATTACK1){
                    if(name.equals("boss2")){
                        jump();
                    }
                    else{
                        COUNTATTACK++;
                        attack1(attack1Frame);
                    }
                }
                else if(State == ATTACK2){
                    if(name.equals("boss3") && COUNTATTACK > 125 && COUNTATTACK < 160){
                        batch.draw(bullet, enemyX - 1105, enemyY + 20, 1125, 20);
                        cm.getPlayer().setHealth(DAMAGE2);
                    }
                    COUNTATTACK++;
                    attack2(attack2Frame);
                }
                else if(State == WALK && LastState == RIGHT){
                    COUNTWALK--;
                    if(COUNTWALK == 0){
                        COUNTWALK = 50;
                        State = RIGHT;
                    }
                    moveEnemy(walk);
                }
                else if(State == WALK && LastState == LEFT){
                    COUNTWALK--;
                    if(COUNTWALK == 0){
                        COUNTWALK = 50;
                        State = LEFT;
                    }
                    moveEnemy(walk);
                }
                else{
                    if(State == LEFT){
                        batch.draw(img, enemyX, enemyY);
                    }
                    else{
                        batch.draw(img, enemyX + width(img), enemyY, -width(img), height(img));
                    }
                }
                break;

            case PAUSED:
                if(State == ATTACK1){
                    if(LastState == RIGHT){
                        batch.draw(attack1Frame, enemyX + width(attack1Frame), enemyY, -width(attack1Frame), height(attack1Frame));
                    }
                    else if(LastState == LEFT){
                        batch.draw(attack1Frame, enemyX, enemyY);
                    }
                }
                else if(State == ATTACK2){
                    if(LastState == RIGHT){
                        batch.draw(attack2Frame, enemyX + width(attack2Frame), enemyY, -width(attack2Frame), height(attack2Frame));
                    }
                    else if(LastState == LEFT){
                        batch.draw(attack2Frame, enemyX, enemyY);
                    }
                }
                else{
                    if(LastState == LEFT){
                        batch.draw(img, enemyX, enemyY);
                    }
                    else{
                        batch.draw(img, enemyX + width(img), enemyY, -width(img), height(img));
                    }
                }
                break;
        }
    }

    public void jump(){
        vel -= gravity;
        enemyY += (int)(vel);
        if(vel > 0){
            batch.draw(jump, enemyX, enemyY);
        }
        else if(vel <= 0 && name.equals("boss2")){
            batch.draw(fall, enemyX, enemyY);
            if(!moveCheck(FALL)){
                State = LastState;
            }
        }
        else{
            batch.draw(img, enemyX, enemyY);
            if(!moveCheck(FALL)){
                State = LastState;
            }
        }
    }

    public void moveEnemy(Texture walk) {
        if(!moveCheck(FALL)){
            if(moveCheck(RIGHT)){
                if(LastState == RIGHT){
                    enemyX += 4;
                    batch.draw(walk, enemyX + width(walk), enemyY, -width(walk), height(walk));
                }
                else{
                    batch.draw(walk, enemyX + width(walk), enemyY, -width(walk), height(walk));
                }
            }
            else if(moveCheck(LEFT)){
                if(LastState == RIGHT){
                    enemyX += 4;
                    batch.draw(walk, enemyX + width(walk), enemyY, -width(walk), height(walk));
                }
                else{
                    batch.draw(walk, enemyX + width(walk), enemyY, -width(walk), height(walk));
                }
            }
        }
        else if(moveCheck(FALL)){
            vel = 0;
            State = FALL;
        }
    }

    public void attack1(TextureRegion attackFrame) {
        attack1TimePassed += Gdx.graphics.getDeltaTime();
        if(LastState == RIGHT){
            batch.draw(attackFrame, enemyX + width(attackFrame), enemyY, -width(attackFrame), height(attackFrame));
        }
        else if(LastState == LEFT){
            batch.draw(attackFrame, enemyX, enemyY);
        }
        if(STOPATTACK1 == COUNTATTACK) {
            State = LastState;
            COUNTATTACK = 0;
            attack1TimePassed = 0;
            cm.checkHit(this, ATTACK1);
        }
    }

    public void attack2(TextureRegion attackFrame){
        attack2TimePassed += Gdx.graphics.getDeltaTime();
        if(LastState == RIGHT){
            batch.draw(attackFrame, enemyX + width(attackFrame), enemyY, -width(attackFrame), height(attackFrame));
        }
        else if(LastState == LEFT){
            batch.draw(attackFrame, enemyX, enemyY);
        }
        if(COUNTATTACK == STOPATTACK2){
            State = LastState;
            COUNTATTACK = 0;
            attack2TimePassed = 0;
            cm.stopBossAttacking();
        }
    }

    public boolean moveCheck(int dir){
        boolean flag = true;
        Rectangle test = null;
        if(dir == LEFT){
            test = new Rectangle(enemyX-5, enemyY+11,img.getWidth(),img.getHeight());
        }
        if(dir == RIGHT){
            test = new Rectangle(enemyX+5, enemyY+11,img.getWidth(),img.getHeight());
        }
        if(dir == FALL){
            test = new Rectangle(enemyX, enemyY + (int)(vel),img.getWidth(),img.getHeight());
        }
        for(MapObject b : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle r = ((RectangleMapObject)b).getRectangle();
            if(test.overlaps(r) && State == ATTACK1 && name.equals("boss2") && cm.getPlayer().moveCheck(FALL)){
                System.out.print("here");
                attack1TimePassed = 0;
                COUNTATTACK = 0;
                cm.getPlayer().setHealth(DAMAGE1);
                flag = false;
            }
            else if(test.overlaps(r) && State == FALL){
                attack1TimePassed = 0;
                COUNTATTACK = 0;
                flag = false;
            }
            else if(test.overlaps(r)){
                flag = false;
            }
        }
        return flag;
    }

    public Rectangle getBox() {
        return new Rectangle(enemyX, enemyY, img.getWidth(), img.getHeight());
    }

    public void changeScreens() {
        if(screenState == Enemy.Screens.RUNNING){
            screenState = Enemy.Screens.PAUSED;
        }
        else{
            screenState = Enemy.Screens.RUNNING;
        }
    }

    public void setState(int s) {
        LastState = State;
        State = s;
    }

    public void addBullet() {
        cm.addBullet("bad", "one", SPEED,  bullet, enemyX, enemyY + 25, State, DAMAGE2);
    }

    public boolean isCool() {
        if(ATTACKCOOLDOWN == 0){
            ATTACKCOOLDOWN = (int)(STOPATTACK2 * 1.5);
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        deathAtlas.dispose();
        attack1Atlas.dispose();
        attack2Atlas.dispose();
        img.dispose();
        walk.dispose();
        jump.dispose();
        fall.dispose();
        bullet.dispose();
    }

    public int height(TextureRegion t){
        return t.getRegionHeight();
    }
    public int height(Texture t) {return t.getHeight();}

    public int width(TextureRegion t){
        return t.getRegionWidth();
    }
    public int width(Texture t){
        return t.getWidth();
    }

    public CombatManager getCm() {return cm;}
    public SpriteBatch getBatch() {return batch;}
    public int getState() {return State;}
    public int getLastState() {return LastState;}
    public String getName() {return name;}
    public int getEnemyX() {return enemyX;}
    public int getEnemyY() {return enemyY;}
    public void setVel() {vel = 10;}
    public Texture getBullet() {return bullet;}
    public Rectangle getBulletRect() {return new Rectangle(enemyX, enemyY, bullet.getWidth(), bullet.getHeight());}
    public double getHealth() {return HEALTH;}
    public void setHealth(double d) {HEALTH = HEALTH - d;}
    public double getDamageOne() {return DAMAGE1;}
    public double getDamageTwo() {return DAMAGE2;}
}
