package com.wastelandwarriors.game.Entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.wastelandwarriors.game.handlers.CombatManager;
import com.wastelandwarriors.game.main.Game;

public class Player extends ApplicationAdapter {

    private enum Screens{
        RUNNING, PAUSED;
    }
    Screens screenState;

    private String name;
    private String gender;
    private String classType;
    private String file;
    private int playerX;
    private int playerY;
    private SpriteBatch batch;
    private CombatManager cm;

    private Texture heart;
    private BitmapFont font;
    private TextureAtlas playerAtlas;
    private Animation animation;
    private float timePassed;
    private TextureAtlas attack1Atlas;
    private Animation attack1Animation;
    private float attack1TimePassed;
    private TextureAtlas attack2Atlas;
    private Animation attack2Animation;
    private float attack2TimePassed;
    private Texture img;
    private Texture jump;
    private Texture fall;
    private Texture arrow1;
    private Texture arrow2;
    private double gravity;
    private double vel;
    private int State;
    private final int JUMP = 0;
    private final int FALL = 1;
    private final int ATTACK1 = 2;
    private final int ATTACK2 = 3;
    private final int STOPATTACK1 = 20;
    private final int STOPATTACK2 = 40;
    private final int RIGHT = 4;
    private final int LEFT = 5;
    private boolean IDLE = true;
    private final int ATTACKDELAY1 = 50;
    private final int ATTACKDELAY2 = 75;
    private int COUNTATTACK;
    private int LastState;
    private int ATTACKCOOLDOWN;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private Game game;
    private boolean isJump = false;
    private int SPEED = 4;
    private double HEALTH;
    private double DAMAGE1;
    private double DAMAGE2;
    private int score;

    public Player(String name, String gender, String classType, String file, CombatManager cm, Game game) {
        this.name = name;
        this.gender = gender;
        this.classType = classType;
        this.file = file;
        this.cm = cm;
        this.game = game;
        cm.setPlayer(this);
        batch = cm.getBatch();
        playerX = 55;
        playerY = 55;
        makeMap();
        create();
    }

    public String getName() {return name;}
    public String getGender() {return gender;}
    public String getType() {return classType;}
    public String getFile() {return file;}
    public int getPlayerX() {return playerX;}
    public int getPlayerY() {return playerY;}

    @Override
    public void create () {
        heart = new Texture("GamePics/heart.png");
        font = new BitmapFont();
        timePassed = 0;
        attack1TimePassed = 0;
        attack2TimePassed = 0;
        gravity = 0.35;
        vel = 0;
        COUNTATTACK = 0;
        LastState = 4;
        ATTACKCOOLDOWN = 0;
        screenState = Screens.RUNNING;
        State = RIGHT;

        if(gender.equals("male") && classType.equals("knight")){
            img = new Texture("MaleKnight/Idle.png");
            jump = new Texture("MaleKnight/jumping.png");
            fall = new Texture("MaleKnight/falling.png");
            playerAtlas = new TextureAtlas(Gdx.files.internal("MaleKnight/walk.pack"));
            animation = new Animation(0.1f, playerAtlas.getRegions());
            attack1Atlas = new TextureAtlas(Gdx.files.internal("MaleKnight/attack1.txt"));
            attack1Animation = new Animation(0.07f, attack1Atlas.getRegions());
            attack2Atlas = new TextureAtlas(Gdx.files.internal("MaleKnight/attack2.txt"));
            attack2Animation = new Animation(0.07f, attack2Atlas.getRegions());
            HEALTH = 50;
            DAMAGE1 = 3;
            DAMAGE2 = 5;
        }
        else if(gender.equals("female") && classType.equals("knight")){
            img = new Texture("FemaleKnight/Idle.png");
            jump = new Texture("FemaleKnight/jumping.png");
            fall = new Texture("FemaleKnight/falling.png");
            playerAtlas = new TextureAtlas(Gdx.files.internal("FemaleKnight/walk.txt"));
            animation = new Animation(0.1f, playerAtlas.getRegions());
            attack1Atlas = new TextureAtlas(Gdx.files.internal("FemaleKnight/attack1.txt"));
            attack1Animation = new Animation(0.07f, attack1Atlas.getRegions());
            attack2Atlas = new TextureAtlas(Gdx.files.internal("FemaleKnight/attack2.txt"));
            attack2Animation = new Animation(0.07f, attack2Atlas.getRegions());
            HEALTH = 45;
            DAMAGE1 = 4;
            DAMAGE2 = 7;
        }
        else if(gender.equals("male") && classType.equals("archer")){
            img = new Texture("MaleArcher/Idle.png");
            jump = new Texture("MaleArcher/jumping.png");
            fall = new Texture("MaleArcher/falling.png");
            arrow1 = new Texture("MaleArcher/arrow1.png");
            arrow2 = new Texture("MaleArcher/arrow2.png");
            playerAtlas = new TextureAtlas(Gdx.files.internal("MaleArcher/walk.txt"));
            animation = new Animation(0.1f, playerAtlas.getRegions());
            attack1Atlas = new TextureAtlas(Gdx.files.internal("MaleArcher/attack1.txt"));
            attack1Animation = new Animation(0.1f, attack1Atlas.getRegions());
            attack2Atlas = new TextureAtlas(Gdx.files.internal("MaleArcher/attack2.txt"));
            attack2Animation = new Animation(0.1f, attack2Atlas.getRegions());
            HEALTH = 50;
            DAMAGE1 = 4;
            DAMAGE2 = 4;
        }
        else if(gender.equals("female") && classType.equals("archer")){
            img = new Texture("FemaleArcher/Idle.png");
            jump = new Texture("FemaleArcher/jumping.png");
            fall = new Texture("FemaleArcher/falling.png");
            arrow1 = new Texture("FemaleArcher/arrow1.png");
            arrow2 = new Texture("FemaleArcher/arrow2.png");
            playerAtlas = new TextureAtlas(Gdx.files.internal("FemaleArcher/walk.txt"));
            animation = new Animation(0.1f, playerAtlas.getRegions());
            attack1Atlas = new TextureAtlas(Gdx.files.internal("FemaleArcher/attack1.txt"));
            attack1Animation = new Animation(0.1f, attack1Atlas.getRegions());
            attack2Atlas = new TextureAtlas(Gdx.files.internal("FemaleArcher/attack2.txt"));
            attack2Animation = new Animation(0.1f, attack2Atlas.getRegions());
            HEALTH = 45;
            DAMAGE1 = 5;
            DAMAGE2 = 5;
        }
    }

    @Override
    public void render () {
        TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(timePassed, true);
        TextureRegion attack1Frame = (TextureRegion) attack1Animation.getKeyFrame(attack1TimePassed, true);
        TextureRegion attack2Frame = (TextureRegion) attack2Animation.getKeyFrame(attack2TimePassed, true);

        switch(screenState){
            case RUNNING:
                batch.draw(heart, playerX - 35, 400, heart.getWidth() / 8, heart.getHeight() / 8);
                String lives = (Double.toString(HEALTH));
                font.setColor(Color.GREEN);
                font.draw(batch, "x" + lives, playerX + 20, 430);
                if(ATTACKCOOLDOWN > 0){
                    ATTACKCOOLDOWN--;
                }

                if(State == JUMP || State == FALL){
                    jump();
                    if(Gdx.input.isKeyPressed(Input.Keys.D)){
                        playerX += SPEED;
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.A)){
                        playerX -= SPEED;
                    }
                    if(vel > 0){
                        if(LastState == RIGHT){
                            batch.draw(jump, playerX, playerY);
                        }
                        else if(LastState == LEFT){
                            batch.draw(jump, playerX + width(jump), playerY, -width(jump), height(jump));
                        }
                    }
                    else if(vel <= 0){
                        if(LastState == RIGHT){
                            batch.draw(fall, playerX, playerY);
                        }
                        else if(LastState == LEFT){
                            batch.draw(fall, playerX + width(jump), playerY, -width(jump), height(jump));
                        }
                    }
                }
                else if(State == ATTACK1){
                    if(Gdx.input.isKeyPressed(Input.Keys.D)){
                        playerX += SPEED;
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.A)){
                        playerX -= SPEED;
                    }
                    attack1(attack1Frame);
                }
                else if(State == ATTACK2){
                    if(Gdx.input.isKeyPressed(Input.Keys.D)){
                        playerX += SPEED;
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.A)){
                        playerX -= SPEED;
                    }
                    if(classType.equals("archer") && ATTACKCOOLDOWN == 55){
                        cm.addBullet("good", "two", 8,  arrow2, playerX + 25, playerY + 15, LastState);
                    }
                    attack2(attack2Frame);
                }
                else if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    IDLE = false;
                    LastState = State;
                    State = JUMP;
                    vel = 10;
                }

                else if(Gdx.input.isKeyPressed(Input.Keys.D)){
                    if(moveCheck(FALL)){
                        vel = 0;
                        LastState = State;
                        State = FALL;
                    }
                    else{
                        IDLE = false;
                        LastState = State;
                        State = RIGHT;
                        movePlayer(currentFrame);
                    }
                }
                else if(Gdx.input.isKeyPressed(Input.Keys.A)){
                    if(moveCheck(FALL)){
                        vel = 0;
                        LastState = State;
                        State = FALL;
                    }
                    else{
                        IDLE = false;
                        LastState = State;
                        State = LEFT;
                        movePlayer(currentFrame);
                    }
                }
                else if(Gdx.input.isKeyJustPressed(Input.Keys.K) && ATTACKCOOLDOWN == 0){
                    IDLE = false;
                    if(classType.equals("archer")){
                        cm.addBullet("good", "one", 8,  arrow1, playerX + 25, playerY + 25, State);
                    }
                    ATTACKCOOLDOWN = ATTACKDELAY1;
                    LastState = State;
                    State = ATTACK1;
                }
                else if(Gdx.input.isKeyJustPressed(Input.Keys.J) && ATTACKCOOLDOWN == 0){
                    IDLE = false;
                    ATTACKCOOLDOWN = ATTACKDELAY2;
                    vel = 10;
                    LastState = State;
                    State = ATTACK2;
                }
                else{
                    if(LastState == LEFT){
                        IDLE = true;
                        batch.draw(img, playerX + width(img), playerY, -width(img), height(img));
                    }
                    else{
                        IDLE = true;
                        batch.draw(img, playerX, playerY);
                    }
                }
                break;

            case PAUSED:
                if(State == JUMP && LastState == RIGHT){
                    batch.draw(jump, playerX, playerY);
                }
                else if(State == JUMP && LastState == LEFT){
                    batch.draw(jump, playerX + width(jump), playerY, -width(jump), height(jump));
                }
                else if(State == ATTACK1){
                    if(LastState == RIGHT){
                        batch.draw(attack1Frame, playerX, playerY);
                    }
                    else if(LastState == LEFT){
                        batch.draw(attack1Frame, playerX + width(attack1Frame), playerY, -width(attack1Frame), height(attack1Frame));
                    }
                }
                else if(State == ATTACK2){
                    if(LastState == RIGHT){
                        batch.draw(attack2Frame, playerX, playerY);
                    }
                    else if(LastState == LEFT){
                        batch.draw(attack2Frame, playerX + width(attack2Frame), playerY, -width(attack2Frame), height(attack2Frame));
                    }
                }
                else{
                    if(LastState == LEFT){
                        batch.draw(img, playerX + width(img), playerY, -width(img), height(img));
                    }
                    else{
                        batch.draw(img, playerX, playerY);
                    }
                }
                break;
        }
    }

    public void jump(){
        vel -= gravity;
        playerY += (int)(vel);
        if(classType.equals("knight") && State == ATTACK2 && vel <= 0){
            playerY += ((int)(vel) * 24);
            moveCheck(State);
        }
        else if(vel <= 0 && State == ATTACK2 && classType.equals("archer")){
            State = FALL;
            moveCheck(State);
        }
        else if(vel <= 0 && State != ATTACK2){
            State = FALL;
            moveCheck(State);
        }
    }

    public void movePlayer(TextureRegion currentFrame) {
        timePassed += Gdx.graphics.getDeltaTime();
        if(State == RIGHT){
            if(moveCheck(RIGHT)){
                batch.draw(currentFrame, playerX, playerY);
            }
            else{
                batch.draw(currentFrame, playerX, playerY);
            }
        }
        if(State == LEFT){
            if(moveCheck(LEFT)){
                batch.draw(currentFrame,playerX + width(currentFrame), playerY, -width(currentFrame), height(currentFrame));
            }
            else{
                batch.draw(currentFrame,playerX + width(currentFrame), playerY, -width(currentFrame), height(currentFrame));
            }
        }
    }

    public void attack1(TextureRegion attackFrame) {
        attack1TimePassed += Gdx.graphics.getDeltaTime();
        COUNTATTACK++;
        if(LastState == RIGHT){
            batch.draw(attackFrame, playerX, playerY);
        }
        else if(LastState == LEFT){
            batch.draw(attackFrame, playerX + width(attackFrame), playerY, -width(attackFrame), height(attackFrame));
        }
        if(STOPATTACK1 == COUNTATTACK) {
            State = LastState;
            COUNTATTACK = 0;
            attack1TimePassed = 0;
            if(classType.equals("knight")){
                cm.checkHit(this, ATTACK1);
            }
        }
    }

    public void attack2(TextureRegion attackFrame){
        attack2TimePassed += Gdx.graphics.getDeltaTime();
        jump();
        if(COUNTATTACK == STOPATTACK2) {
            LastState = ATTACK2;
            State = FALL;
            if(classType.equals("knight")){
                cm.checkHit(this, ATTACK2);
            }
        }
        else if(LastState == RIGHT && COUNTATTACK != STOPATTACK2){
            COUNTATTACK++;
            batch.draw(attackFrame, playerX, playerY);
        }
        else if(LastState == LEFT && COUNTATTACK != STOPATTACK2){
            COUNTATTACK++;
            batch.draw(attackFrame, playerX + width(attackFrame), playerY, -width(attackFrame), height(attackFrame));
        }
    }

    public boolean moveCheck(int dir){
        boolean flag = true;
        Rectangle test = null;

        if(dir == LEFT){
            test = new Rectangle(playerX-5, playerY+11,35,50);
        }
        if(dir == RIGHT){
            test = new Rectangle(playerX+5, playerY+11,35,50);
        }
        if(dir == JUMP){
            test = new Rectangle(playerX,playerY+(int)(vel),35,50);
        }
        if(dir == FALL){
            test = new Rectangle(playerX, playerY + (int)(vel),35,50);
        }
        if(dir == ATTACK2){
            test = new Rectangle(playerX, playerY + (int)(vel) * 24,35,50);
        }
        for(MapObject b : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle r = ((RectangleMapObject)b).getRectangle();
            if(test.overlaps(r) && (State == FALL || State == ATTACK2)){
                State = LastState;
                attack2TimePassed = 0;
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
        return new Rectangle(playerX, playerY, img.getWidth(), img.getHeight());
    }

    public void changeScreens() {
        if(screenState == Screens.RUNNING){
            screenState = Screens.PAUSED;
        }
        else{
            screenState = Screens.RUNNING;
        }
    }

    public void makeMap() {
        mapLoader = cm.getMap().getMaploader();
        map = cm.getMap().getMap();
        cm.populate();
        playerX = 55;
        playerY = 55;
        cm.getMap().getCam().position.x = playerX;
        cm.getMap().getCam().position.y = playerY;
    }

    public void addScore(int s) {
        score += s;
    }
    public int getScore() {return score;}

    @Override
    public void dispose(){
        batch.dispose();
        playerAtlas.dispose();
        attack1Atlas.dispose();
        attack2Atlas.dispose();
        img.dispose();
        jump.dispose();
        fall.dispose();
        arrow1.dispose();
        arrow2.dispose();
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
    public boolean isIDLE() {return IDLE;}
    public int getLastState() {return LastState;}
    public int getState() {return State;}
    public double getHealth() {return HEALTH;}
    public void setHealth(double d) {HEALTH = HEALTH - d;}
    public double getDamageOne() {return DAMAGE1;}
    public double getDamageTwo() {return DAMAGE2;}
    public void setPlayerX(int x) {
        playerX = x;
    }
}
