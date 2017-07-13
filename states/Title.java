package com.wastelandwarriors.game.states;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wastelandwarriors.game.handlers.GameStateManager;
import com.wastelandwarriors.game.main.Game;

public class Title extends ApplicationAdapter{

    private Texture back;
    private Texture title;
    private BitmapFont font;

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private GameStateManager gsm;

    public Title(GameStateManager gsm){
        this.gsm = gsm;
        this.game = gsm.game();
        batch = game.getBatch();
        create();
    }

    @Override
    public void create() {
        back = new Texture("GamePics/Title Page.png");
        title = new Texture("GamePics/Title.png");
        font = new BitmapFont();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void update() {
        if(Gdx.input.isKeyPressed(Keys.ENTER)){
            gsm.pushState(2);
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
        batch.draw(title, 185, 125);
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Press ENTER to start", 425, 125);
        batch.end();
    }

    @Override
    public void dispose() {
        back.dispose();
        batch.dispose();
        title.dispose();
        font.dispose();
    }
}
