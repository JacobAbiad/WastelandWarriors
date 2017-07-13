package com.wastelandwarriors.game.handlers;

import com.wastelandwarriors.game.Entities.Player;
import com.wastelandwarriors.game.main.Game;
import com.wastelandwarriors.game.states.*;

import java.util.Stack;

public class GameStateManager {

    private Game game;
    private Stack<Integer> gameStates;
    private Play playState;
    private Title titleState;
    private Choose chooseState;
    private GameOver gameoverState;
    private Player player;
    private int score;

    public static final int TITLE = 1;
    public static final int CHOOSE = 2;
    public static final int PLAY = 3;
    public static final int GAMEOVER = 4;

    public GameStateManager(Game game){
        this.game = game;
        gameStates = new Stack<Integer>();
        pushState(TITLE);
    }

    public void update(){
        if(gameStates.peek() == TITLE){
            titleState.update();
        }
        else if(gameStates.peek() == CHOOSE){
            chooseState.update();
        }
        else if(gameStates.peek() == PLAY) {
            playState.update();
        }
        else if(gameStates.peek() == GAMEOVER){
            gameoverState.update();
        }
    }

    public void render(){
        if(gameStates.peek() == TITLE){
            titleState.render();
        }
        else if(gameStates.peek() == CHOOSE){
            chooseState.render();
        }
        else if(gameStates.peek() == PLAY) {
            playState.render();
        }
        else if(gameStates.peek() == GAMEOVER){
            gameoverState.render();
        }
    }

    public void pushState(int state){
        if(gameStates.size() != 0){
            gameStates.pop();
        }
        gameStates.push(state);
        if(TITLE == state){
            titleState = new Title(this);
        }
        else if(CHOOSE == state){
            chooseState = new Choose(this);
        }
        else if(PLAY == state){
            playState = new Play(this);
        }
        else if(GAMEOVER == state){
            gameoverState = new GameOver(this);
        }
    }

    public void makePlayer(String name, String gender, String classType, String file, CombatManager cm) {
        cm = game.setCm();
        player = new Player(name, gender, classType, file, cm, game());
    }
    public Player getPlayer() {
        return player;
    }

    public void giveScore(int s) {
        score = s;
    }
    public int getScore() {return score;}

    public Game game() {return game;}
}