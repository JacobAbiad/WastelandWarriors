package com.wastelandwarriors.game.states;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wastelandwarriors.game.Entities.Player;
import com.wastelandwarriors.game.handlers.CombatManager;
import com.wastelandwarriors.game.handlers.GameStateManager;
import com.wastelandwarriors.game.main.Game;

public class GameOver extends ApplicationAdapter{

    private GameStateManager gsm;
    private Game game;
    private Player player;
    private SpriteBatch batch;
    private CombatManager cm;
    private int score;

    private Texture back;
    private Texture title;
    private BitmapFont font;
    private OrthographicCamera cam;

    public GameOver(GameStateManager gsm){
        this.gsm = gsm;
        this.game = gsm.game();
        this.player = gsm.getPlayer();
        batch = game.getBatch();
        //gsm.getScore();
        score = 10;
        create();
    }

    @Override
    public void create(){
        back = new Texture("GamePics/Title Page.png");
        title = new Texture("GamePics/GameOverPic.png");
        font = new BitmapFont();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void update(){
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
            cm = game.setCm();
            gsm.makePlayer(player.getName(), player.getGender(), player.getType(), player.getFile(), cm);
            gsm.pushState(3);
        }
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
            gsm.pushState(1);
        }
        cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);
        cam.update();

        batch.begin();
        batch.draw(back, 0, 0, 1125, 450);
        batch.draw(title, 280, 250);
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Your Score Was: " + Integer.toString(score), 445, 200);
        font.draw(batch, "Press ENTER to play again", 400, 125);
        font.draw(batch, "Press ESC to exit", 460, 80);
        batch.end();
    }

    @Override
    public void dispose(){
        batch.dispose();
        back.dispose();
        title.dispose();
        font.dispose();
    }
}
