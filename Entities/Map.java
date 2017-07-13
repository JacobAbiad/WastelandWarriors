package com.wastelandwarriors.game.Entities;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.wastelandwarriors.game.handlers.CombatManager;
import com.wastelandwarriors.game.main.Game;

import java.util.ArrayList;
import java.util.LinkedList;

public class Map extends ApplicationAdapter implements InputProcessor{
    private OrthographicCamera camera;
    private SpriteBatch mapBatch;
    private TmxMapLoader maploader;
    private TiledMap map;
    private ArrayList<String> mapStringList = new ArrayList<String>();
    private String mapString1 = "map1.tmx";
    private String mapString2 = "map2.tmx";
    private String mapString3 = "map3.tmx";
    private OrthogonalTiledMapRenderer renderer;
    private Game game;
    private ArrayList<Rectangle> mapRects;
    private CombatManager cm;
    private int index;

    public Map(Game game, CombatManager cm) {
        this.game = game;
        mapBatch = new SpriteBatch();
        camera = game.getCam();
        this.cm = cm;
        mapStringList.add(mapString1);
        mapStringList.add(mapString2);
        mapStringList.add(mapString3);
        index = 0;
        create();
    }

    @Override
    public void create () {
        mapRects = new ArrayList<Rectangle>();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        maploader = new TmxMapLoader();
        setIndex();
        map = maploader.load(mapStringList.get(index));
        renderer = new OrthogonalTiledMapRenderer(map);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render () {
        mapBatch.begin();
        renderer.setView(camera);
        camera.update();
        renderer.render();
        mapBatch.end();
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            mapRects.add(rect);
        }
    }
    public ArrayList<Rectangle> Rects(){
        return mapRects;
    }

    public void moveRight(){
        camera.position.x += 4;
    }

    public void moveLeft(){
        camera.position.x -= 4;
    }

    public void setIndex() {
        if(index == -1){
            index = 0;
        }
        else if(index == 0){
            index = 1;
        }
        else if(index == 1){
            index = 2;
        }
        else if(index == 2){
            index = 0;
        }
    }

    public TmxMapLoader getMaploader() {
        return maploader;
    }
    public TiledMap getMap() {
        return map;
    }
    public int getIndex() {return index;}
    public OrthographicCamera getCam() {
        return camera;
    }

    @Override
    public void resize(int x, int y) {
        camera.position.set((float)(x + Gdx.graphics.getWidth()/2 - 55),(float)(y + Gdx.graphics.getHeight()/2 - 55),0);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
