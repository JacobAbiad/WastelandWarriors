package com.wastelandwarriors.game.states;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.wastelandwarriors.game.Entities.Enemy;
import com.wastelandwarriors.game.Entities.Map;
import com.wastelandwarriors.game.Entities.Player;
import com.wastelandwarriors.game.handlers.CombatManager;
import com.wastelandwarriors.game.handlers.GameStateManager;
import com.wastelandwarriors.game.main.Game;
import java.io.*;
import java.awt.*;

public class Play extends ApplicationAdapter{

    private enum State{
        RUNNING, PAUSED;
    }
    State currentState;

    private Map map;
    private BitmapFont font;
    private Texture back;
    private final int STARTX = 100;
    private final int STARTY = 100;
    private final int JUMP = 0;
    private final int ATTACK2 = 3;
    private final int RIGHT = 4;
    private final int LEFT = 5;
    private int x;
    private int y;
    private ShapeRenderer shape;
    private Player player;
    private String fileName;

    private Texture resumeButton;
    private Rectangle resumeRect;
    private Texture saveQuitButton;
    private Rectangle saveQuitRect;
    private Texture quitButton;
    private Rectangle quitRect;

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private GameStateManager gsm;
    private CombatManager cm;

    public Play(GameStateManager gsm){
        this.gsm = gsm;
        this.game = gsm.game();
        this.player = gsm.getPlayer();
        cm = player.getCm();
        this.fileName = player.getFile();
        batch = game.getBatch();
        map = game.getMap();
        create();
    }

    @Override
    public void create() {
        x = 100;
        y = 100;
        currentState = State.RUNNING;
        shape = new ShapeRenderer();

        resumeButton = new Texture("GamePics/ResumePic.png");
        resumeRect = new Rectangle(x + 340, 81, resumeButton.getWidth() / 2, resumeButton.getHeight() / 2);
        saveQuitButton = new Texture("GamePics/SaveQuitPic.png");
        saveQuitRect = new Rectangle(x + 270, 181, saveQuitButton.getWidth() / 2, saveQuitButton.getHeight() / 2);
        quitButton = new Texture("GamePics/QuitPic.png");
        quitRect = new Rectangle(x + 380, 281, quitButton.getWidth() / 2, quitButton.getHeight() / 2);
    }

    public void update() {
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
            if(currentState == State.RUNNING){
                currentState = State.PAUSED;
                player.changeScreens();
                cm.changeScreens();
            }
            else{
                currentState = State.RUNNING;
                player.changeScreens();
                cm.changeScreens();
            }
        }

        switch(currentState){
            case RUNNING:
                if(Gdx.input.isKeyPressed(Keys.D) && player.moveCheck(RIGHT)){
                    x += 4;
                    player.setPlayerX((int)cam.position.x - 512);
                    map.moveRight();
                }
                else if(Gdx.input.isKeyPressed(Keys.A) && player.moveCheck(LEFT)){
                    x -= 4;
                    player.setPlayerX((int)cam.position.x - 512);
                    map.moveLeft();
                }
                break;
            case PAUSED:
                if(resumeRect.contains(Gdx.input.getX(), Gdx.input.getY()) && Gdx.input.isButtonPressed(Buttons.LEFT)){
                    currentState = State.RUNNING;
                    player.changeScreens();
                    cm.changeScreens();
                }
                else if(saveQuitRect.contains(Gdx.input.getX(), Gdx.input.getY()) && Gdx.input.isButtonPressed(Buttons.LEFT)){
                    try{
                        Writer writer = new FileWriter(fileName);
                        writer.write(player.getName() + "\n");
                        writer.write(player.getGender() + "\n");
                        writer.write(player.getType() + "\n");
                        writer.flush();
                        writer.close();
                        gsm.pushState(4);
                    }
                    catch(IOException ioe) {
                        System.out.println("error occurred");
                    }
                }
                else if(quitRect.contains(Gdx.input.getX(), Gdx.input.getY()) && Gdx.input.isButtonPressed(Buttons.LEFT)){
                    gsm.pushState(1);
                }
                break;
        }
        cam.position.set((float) (x + (Gdx.graphics.getWidth() / 2 - STARTX)),(float) (y + (Gdx.graphics.getHeight() / 2 - STARTY)), 0);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam = map.getCam();
        batch.setProjectionMatrix(cam.combined);
        shape.setProjectionMatrix(cam.combined);
        cam.update();

        switch(currentState){
            case RUNNING:
                map.render();
                batch.begin();
                player.render();
                cm.drawEnemies();
                cm.moveBullets();
                cm.moveEnemies();
                cm.attackEnemies();
                cm.removeDead();
                batch.end();
                if(cm.getIsDead()){
                    gsm.giveScore(player.getScore());
                    gsm.pushState(4);
                }
                break;
            case PAUSED:
                map.render();
                batch.begin();
                player.render();
                cm.drawEnemies();
                batch.end();
                Gdx.gl.glEnable(GL20.GL_BLEND);
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(new Color(0, 0, 0, 0.5f));
                shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                shape.end();
                batch.begin();
                batch.draw(resumeButton, x + 340, 300, resumeButton.getWidth() / 2, resumeButton.getHeight() / 2);
                batch.draw(saveQuitButton, x + 270, 200, saveQuitButton.getWidth() / 2, saveQuitButton.getHeight() / 2);
                batch.draw(quitButton, x + 380, 100, quitButton.getWidth() / 2, quitButton.getHeight() / 2);
                batch.end();
                break;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        back.dispose();
        shape.dispose();
        resumeButton.dispose();
        saveQuitButton.dispose();
        quitButton.dispose();
    }
}
