package com.wastelandwarriors.game.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.wastelandwarriors.game.Entities.Bullet;
import com.wastelandwarriors.game.Entities.Enemy;
import com.wastelandwarriors.game.Entities.Player;
import com.wastelandwarriors.game.main.Game;
import com.wastelandwarriors.game.Entities.Map;
import java.util.*;

public class CombatManager {

    private Game game;
    private LinkedList<Bullet> playerBullets;
    private LinkedList<Bullet> enemyBullets;
    private LinkedList<Enemy> enemyList;
    private LinkedList<Enemy> deadList;
    private LinkedList<Bullet> bossOneBullets;
    private LinkedList<Bullet> bossThreeBullets;
    private Player player;
    private SpriteBatch batch;
    private final int FALL = 1;
    private final int ATTACK1 = 2;
    private final int ATTACK2 = 3;
    private final int RIGHT = 4;
    private final int LEFT = 5;
    private final int WALK = 6;
    private final int bossOneCharge = 100;
    private int bossOneCool = 100;
    private boolean bossAttacking = false;
    private boolean bossThreeHit = false;
    private final int bossThreeCharge = 500;
    private int bossThreeCool = 500;
    private Map map;
    private boolean isDead = false;
    private double difficulty;

    public CombatManager(Game game) {
        this.game = game;
        batch = game.getBatch();
        game.setMap();
        map = game.getMap();
        difficulty = 1;
        playerBullets = new LinkedList<Bullet>();
        enemyBullets = new LinkedList<Bullet>();
        enemyList = new LinkedList<Enemy>();
        deadList = new LinkedList<Enemy>();
        bossOneBullets = new LinkedList<Bullet>();
        bossThreeBullets = new LinkedList<Bullet>();
    }

    public void addBullet(String origin, String type, int speed, Texture img, int x, int y, int direction) {
        Bullet bullet = new Bullet(type, speed, img, x, y, direction);
        if(origin.equals("good")){
            playerBullets.add(bullet);
        }
        else{
            enemyBullets.add(bullet);
        }
    }

    public void addBullet(String origin, String type, int speed, Texture img, int x, int y, int direction, double dmg) {
        Bullet bullet = new Bullet(type, speed, img, x, y, direction, dmg);
        if(origin.equals("good")){
            playerBullets.add(bullet);
        }
        else{
            enemyBullets.add(bullet);
        }
    }

    public void addEnemy(String name, int enemyX, int enemyY, CombatManager cm){
        Enemy enemy = new Enemy(name, enemyX, enemyY, cm);
        enemyList.add(enemy);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public Player getPlayer() {return player;}

    public void moveBullets() {
        for(Bullet b : playerBullets){
            if(b.getType().equals("one")){
                b.moveX();
            }
            else if(b.getType().equals("two")){
                b.moveX();
                b.moveY();
            }
            if(b.getDirection() == RIGHT){
                batch.draw(b.getImg(), b.getX(), b.getY());
            }
            else if(b.getDirection() == LEFT){
                batch.draw(b.getImg(), b.getX() + b.width(), b.getY(), -b.width(), b.height());
            }
            for(Enemy e : enemyList){
                if(b.getBox().overlaps(e.getBox())){
                    if(b.getType().equals("one")){
                        e.setHealth(player.getDamageOne());
                        playerBullets.remove(b);
                    }
                    else if(b.getType().equals("two")){
                        e.setHealth(player.getDamageTwo());
                        playerBullets.remove(b);
                    }
                }
            }
        }
        for(Bullet b : enemyBullets){
            if(b.getType().equals("one")) {
                b.moveX();
            }
            else if(b.getType().equals("blast")){
                b.moveY();
            }
            if (b.getDirection() == RIGHT) {
                batch.draw(b.getImg(), b.getX(), b.getY());
            }
            else if (b.getDirection() == LEFT) {
                batch.draw(b.getImg(), b.getX() + b.width(), b.getY(), -b.width(), b.height());
            }
            if(b.getBox().overlaps(player.getBox())){
                player.setHealth(b.getDmg());
                enemyBullets.remove(b);
            }
        }
    }

    public void drawEnemies() {
        for(Enemy e : enemyList){
            if(e.getEnemyX() - player.getPlayerX() > 0 && isFree(e)){
                e.setState(LEFT);
            }
            else if(e.getEnemyX() - player.getPlayerX() < 0 && isFree(e)){
                e.setState(RIGHT);
            }
            e.render();
        }
    }

    public void moveEnemies() {
        for(Enemy e : enemyList){
            if(e.getName().equals("rhino") || e.getName().equals("tiger")) {
                if (Math.abs(e.getEnemyX() - player.getPlayerX()) <= 500) {
                    if (isFree(e)) {
                        Random rand = new Random();
                        int randomMove = rand.nextInt(150);
                        if(randomMove == 1) {
                            e.setState(WALK);
                        }
                    }
                }
            }
        }
    }

    public void attackEnemies() {
        for(Enemy e : enemyList){
            if(e.getName().equals("rhino") || e.getName().equals("beetle") || e.getName().equals("boss2")) {
                if (Math.abs(e.getEnemyX() - player.getPlayerX()) <= 500) {
                    if (isFree(e)) {
                        Random rand = new Random();
                        int randomMove = rand.nextInt(125);
                        if(randomMove == 1) {
                            e.addBullet();
                            e.setState(ATTACK2);
                        }
                    }
                }
            }
            if(e.getName().equals("tiger") || e.getName().equals("beetle")) {
                if (Math.abs(e.getEnemyX() - player.getPlayerX()) <= 50) {
                    if (isFree(e)) {
                        Random rand = new Random();
                        int randomMove = rand.nextInt(20);
                        if(randomMove == 1) {
                            e.setState(ATTACK1);
                        }
                    }
                }
            }
            if(e.getName().equals("boss2")) {
                if (Math.abs(e.getEnemyX() - player.getPlayerX()) <= 500) {
                    if (isFree(e)) {
                        Random rand = new Random();
                        int randomMove = rand.nextInt(100);
                        if(randomMove == 1) {
                            e.setVel();
                            e.setState(ATTACK1);
                        }
                    }
                }
            }
            if(e.getName().equals("boss1")) {
                if (Math.abs(e.getEnemyX() - player.getPlayerX()) <= 1000) {
                    if(e.isCool()){
                        e.setState(ATTACK2);
                        bossAttacking = true;
                    }
                }
                if(bossAttacking){
                    if(bossOneCool == bossOneCharge){
                        bossOneCool = 0;
                        if (player.getState() == LEFT && !player.isIDLE()) {
                            enemyBullets.add(new Bullet("blast", 24, e.getBullet(), player.getPlayerX() - 100, 450, RIGHT, e.getDamageTwo()));
                        }
                        else if (player.getState() == RIGHT && !player.isIDLE()) {
                            enemyBullets.add(new Bullet("blast", 24, e.getBullet(), player.getPlayerX() + 100, 450, RIGHT, e.getDamageTwo()));
                        }
                        else if (player.isIDLE()) {
                            enemyBullets.add(new Bullet("blast", 24, e.getBullet(), player.getPlayerX() - 20, 450, RIGHT, e.getDamageTwo()));
                        }
                        else {
                            enemyBullets.add(new Bullet("blast", 24, e.getBullet(), player.getPlayerX() + 20, 450, RIGHT, e.getDamageTwo()));
                        }
                    }
                    else{
                        bossOneCool++;
                    }
                }
            }
            if(e.getName().equals("boss3")) {
                if (Math.abs(e.getEnemyX() - player.getPlayerX()) <= 200) {
                    if (isFree(e)) {
                        if(!bossThreeHit && Math.abs(player.getPlayerX() - e.getEnemyX()) < 50) {
                            bossThreeHit = true;
                            e.setState(ATTACK1);
                        }
                        if(bossThreeCool == bossThreeCharge){
                            bossThreeHit = false;
                            bossThreeCool = 0;
                            e.setState(ATTACK2);
                        }
                        else if(bossThreeCool != bossThreeCharge){
                            bossThreeCool++;
                        }
                    }
                }
            }
        }
    }

    public void checkHit(Player p, int attack) {
        for (Enemy e : enemyList) {
            if (p.getType().equals("knight")) {
                if (p.getBox().overlaps(e.getBox())) {
                    if(attack == ATTACK1){
                        e.setHealth(p.getDamageOne());
                    }
                    else if(attack == ATTACK2) {
                        e.setHealth(p.getDamageTwo());
                    }
                }
            }
        }
    }

    public void checkHit(Enemy e, int attack) {
        if(e.getName() == "boss3"){
            if(e.getBulletRect().overlaps(player.getBox())){
                player.setHealth(e.getDamageTwo());
            }
        }
        else{
            if (e.getBox().overlaps(player.getBox())) {
                if(attack == ATTACK1){
                    player.setHealth(e.getDamageOne());
                }
                else if(attack == ATTACK2) {
                    player.setHealth(e.getDamageTwo());
                }
            }
        }
    }

    public void removeDead() {
        for (Enemy e : enemyList) {
            if (e.getHealth() <= 0) {
                deadList.add(e);
            }
        }
        for (Enemy d : deadList) {
            if(d.getName().equals("tiger")){
                player.addScore(5);
            }
            else if(d.getName().equals("rhino")){
                player.addScore(10);
            }
            else if(d.getName().equals("beetle")){
                player.addScore(15);
            }
            else if(d.getName().equals("boss1")){
                player.addScore(80);
            }
            else if(d.getName().equals("boss2")){
                player.addScore(60);
            }
            else if(d.getName().equals("boss3")){
                player.addScore(40);
            }
            enemyList.remove(d);
            if (enemyList.size() == 0) {
                upDifficulty();
                map.setIndex();
                map.create();
                player.makeMap();
            }
        }
        if (player.getHealth() <= 0) {
            isDead = true;
        }
    }

    public boolean isFree(Enemy e){
        if(e.getState() == LEFT || e.getState() == RIGHT){
            return true;
        }
        else{
            return false;
        }
    }

    public void stopBossAttacking() {
        bossAttacking = false;
    }

    public void changeScreens() {
        for(Enemy e : enemyList){
            e.changeScreens();
        }
    }

    public void upDifficulty() {
        difficulty += 0.5;
    }
    public double getDifficulty() {
        return difficulty;
    }

    public Map getMap() {
        return map;
    }

    public void populate() {
        if(map.getIndex() == 0){
            addEnemy("tiger", 940, 62, this);
            addEnemy("rhino", 1263, 186, this);
            addEnemy("tiger", 1850, 62, this);
            addEnemy("beetle", 4235, 186, this);
            addEnemy("beetle", 5550, 62, this);
            addEnemy("boss2", 6040, 62, this);
        }
        else if(map.getIndex() == 1){
            addEnemy("beetle", 1653, 62, this);
            addEnemy("rhino", 2433, 256, this);
            addEnemy("tiger", 3376, 191, this);
            addEnemy("tiger", 4436, 62, this);
            addEnemy("beetle", 5160, 62, this);
            addEnemy("boss3", 5860, 62, this);
        }
        else if(map.getIndex() == 2){
            addEnemy("rhino", 1090, 62, this);
            addEnemy("rhino", 1846, 320, this);
            addEnemy("beetle", 2515, 62, this);
            addEnemy("tiger", 3571, 222, this);
            addEnemy("tiger", 5223, 190, this);
            addEnemy("boss1", 6330, 62, this);
        }
    }

    public boolean getBossAttacking() {return bossAttacking;}
    public boolean getIsDead() {return isDead;}

    public SpriteBatch getBatch() {return batch;}
}
