package com.wastelandwarriors.game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wastelandwarriors.game.Entities.Map;
import com.wastelandwarriors.game.handlers.CombatManager;
import com.wastelandwarriors.game.handlers.GameStateManager;

public class Game extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera cam;
	private GameStateManager gsm;
	private CombatManager cm;
	private Map map;

	@Override
	public void create () {
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		cam.update();
		gsm.update();
		gsm.render();
	}
	
	@Override
	public void dispose () {batch.dispose();}

	public void resize(int x, int y){}
	public void pause() {}
	public void resume() {}

	public SpriteBatch getBatch() {return batch;}
	public CombatManager getCm() {return cm;}
	public CombatManager setCm() {return new CombatManager(this);}
	public void setMap() {map = new Map(this, cm);}
	public OrthographicCamera getCam() {return cam;}
	public Map getMap() {return map;}
}