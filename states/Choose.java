package com.wastelandwarriors.game.states;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.wastelandwarriors.game.handlers.CombatManager;
import com.wastelandwarriors.game.handlers.GameStateManager;
import com.wastelandwarriors.game.main.Game;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Choose extends ApplicationAdapter implements InputProcessor{

    private BitmapFont font;
    private Texture back;
    private ShapeRenderer shape;
    public String name = "";
    private String gender;
    private String classType;
    private LinkedList<String> slides;
    private LinkedList<String> loadSlides;
    private String slide;

    private static ArrayList<String> inFile;
    private static String fileOneName;
    private static String fileTwoName;
    private static String fileThreeName;
    private String selectedFile;
    private boolean wipeFile;

    private Rectangle newRect;
    private Texture newButton;
    private Rectangle loadRect;
    private Texture loadButton;
    private Rectangle maleRect;
    private Texture maleButton;
    private Rectangle femaleRect;
    private Texture femaleButton;
    private Rectangle archerRect;
    private Texture archerButton;
    private Rectangle knightRect;
    private Texture knightButton;
    private Texture MarcherPic;
    private Texture MknightPic;
    private Texture FarcherPic;
    private Texture FknightPic;
    private Rectangle backRect;
    private Texture backButton;
    private Rectangle saveOne;
    private Rectangle saveTwo;
    private Rectangle saveThree;

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private GameStateManager gsm;
    private CombatManager cm;

    public Choose(GameStateManager gsm) {
        this.gsm = gsm;
        this.game = gsm.game();
        cm = game.getCm();
        batch = game.getBatch();
        create();
    }

    public void create() {
        font = new BitmapFont();
        back = new Texture("GamePics/Title Page.png");
        Gdx.input.setInputProcessor(this);
        shape = new ShapeRenderer();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        selectedFile = "";
        wipeFile = false;

        slides = new LinkedList<String>();
        slides.push("chooseClass");
        slides.push("chooseGender");
        slides.push("chooseName");
        slides.push("chooseFile");
        slide = slides.peek();

        loadSlides = new LinkedList<String>();
        loadSlides.push("chooseSave");
        loadSlides.push("chooseFile");

        newButton = new Texture("GamePics/NewGamePic.png");
        newRect = new Rectangle(200, 229, newButton.getWidth() / 2, newButton.getHeight() / 2);
        loadButton = new Texture("GamePics/LoadGamePic.png");
        loadRect = new Rectangle(600, 229, loadButton.getWidth() / 2, loadButton.getHeight() / 2);
        maleButton = new Texture("GamePics/MalePic.png");
        maleRect = new Rectangle(300, 229, maleButton.getWidth() / 2, maleButton.getHeight() / 2);
        femaleButton = new Texture("GamePics/FemalePic.png");
        femaleRect = new Rectangle(650, 229, femaleButton.getWidth() / 2, femaleButton.getHeight() / 2);
        archerButton = new Texture("GamePics/ArcherPic.png");
        archerRect = new Rectangle(295, 284, archerButton.getWidth() / 2, archerButton.getHeight() / 2);
        knightButton = new Texture("GamePics/KnightPic.png");
        knightRect = new Rectangle(630, 284, knightButton.getWidth() / 2, knightButton.getHeight() / 2);
        MarcherPic = new Texture("GamePics/MarcherPic.png");
        FarcherPic = new Texture("GamePics/FarcherPic.png");
        MknightPic = new Texture("GamePics/MknightPic.png");
        FknightPic = new Texture("GamePics/FknightPic.png");
        saveOne = new Rectangle(332, 105, 410, 100);
        saveTwo = new Rectangle(332, 210, 410, 100);
        saveThree = new Rectangle(332, 315, 410, 100);
        backButton = new Texture("GamePics/BackPic.png");
        backRect = new Rectangle(0, 20, backButton.getWidth() / 2, backButton.getHeight() / 2);
    }

    public void update() {
        if(slide.equals("chooseName") && Gdx.input.isKeyPressed(Keys.ENTER) && name.length() != 0){
            moveSlide(true, slides);
        }
        if(slide.equals("chooseSave") && Gdx.input.isKeyJustPressed(Keys.ENTER) && selectedFile != null && !wipeFile) {
            load(selectedFile);
            setPlayer();
            gsm.pushState(3);
        }
        else if(slide.equals("chooseSave") && Gdx.input.isKeyJustPressed(Keys.ENTER) && selectedFile != null && wipeFile) {
            gsm.makePlayer(name, gender, classType, selectedFile, cm);
            gsm.pushState(3);
        }
        cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);
        cam.update();

        batch.begin();
        batch.draw(back, 0, 0, 1125, 450);
        batch.end();
        if(slide.equals("chooseFile")){
            fileSelect();
        }
        else if(slide.equals("chooseName")){
            nameSelect();
        }
        else if(slide.equals("chooseGender")){
            genderSelect();
        }
        else if(slide.equals("chooseClass")){
            classSelect();
        }
        else if(slide.equals("chooseSave")){
            saveSelect();
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        back.dispose();
        shape.dispose();
        newButton.dispose();
        loadButton.dispose();
        maleButton.dispose();
        femaleButton.dispose();
        archerButton.dispose();
        knightButton.dispose();
        MarcherPic.dispose();
        MknightPic.dispose();
        FarcherPic.dispose();
        FknightPic.dispose();
        backButton.dispose();
        batch.dispose();
    }

    public void fileSelect() {
        batch.begin();
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Choose a game", 430, 300);
        batch.draw(newButton, 200, 145, newButton.getWidth() / 2, newButton.getHeight() / 2);
        batch.draw(loadButton, 600, 145, loadButton.getWidth() / 2, loadButton.getHeight() / 2);
        batch.end();
    }

    public void nameSelect() {
        shape.setProjectionMatrix(cam.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);
        shape.rect(340, 160, 410, 100);
        shape.setColor(Color.GREEN);
        shape.rect(345, 165, 400, 90);
        shape.end();
        batch.begin();
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Choose a name", 445, 300);
        batch.draw(backButton, 0, 380, backButton.getWidth() / 2, backButton.getHeight() / 2);
        font.setColor(Color.RED);
        font.getData().setScale(3);
        font.draw(batch, name, 350, 225);
        batch.end();
    }

    public void genderSelect() {
        batch.begin();
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Choose a gender", 430, 300);
        batch.draw(backButton, 0, 380, backButton.getWidth() / 2, backButton.getHeight() / 2);
        batch.draw(maleButton, 300, 145, maleButton.getWidth() / 2, maleButton.getHeight() / 2);
        batch.draw(femaleButton, 650, 145, femaleButton.getWidth() / 2, femaleButton.getHeight() / 2);
        batch.end();
    }

    public void classSelect() {
        batch.begin();
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Choose a class", 440, 300);
        batch.draw(backButton, 0, 380, backButton.getWidth() / 2, backButton.getHeight() / 2);
        batch.draw(archerButton, 295, 90, archerButton.getWidth() / 2, archerButton.getHeight() / 2);
        batch.draw(knightButton, 630, 90, knightButton.getWidth() / 2, knightButton.getHeight() / 2);
        if(gender.equals("male")){
            batch.draw(MarcherPic, 340, 160, MarcherPic.getWidth() * 2, MarcherPic.getHeight() * 2);
            batch.draw(MknightPic, 655, 160, MknightPic.getWidth() * 2, MknightPic.getHeight() * 2);
        }
        else{
            batch.draw(FarcherPic, 340, 160, FarcherPic.getWidth() * 2, FarcherPic.getHeight() * 2);
            batch.draw(FknightPic, 660, 160, FknightPic.getWidth() * 2, FknightPic.getHeight() * 2);
        }
        batch.end();
    }

    public void saveSelect() {
        shape.setProjectionMatrix(cam.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if(selectedFile.equals("SaveOne.txt")) {
            shape.setColor(Color.RED);
        }
        else {
            shape.setColor(Color.BLACK);
        }
        shape.rect(340, 265, 410, 100);
        if(selectedFile.equals("SaveTwo.txt")) {
            shape.setColor(Color.RED);
        }
        else {
            shape.setColor(Color.BLACK);
        }
        shape.rect(340, 160, 410, 100);
        if(selectedFile.equals("SaveThree.txt")) {
            shape.setColor(Color.RED);
        }
        else {
            shape.setColor(Color.BLACK);
        }
        shape.rect(340, 55, 410, 100);
        shape.setColor(Color.GREEN);
        shape.rect(345, 270, 400, 90);
        shape.rect(345, 165, 400, 90);
        shape.rect(345, 60, 400, 90);
        shape.end();
        batch.begin();
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        font.draw(batch, "Choose a save file", 420, 400);
        batch.draw(backButton, 0, 380, backButton.getWidth() / 2, backButton.getHeight() / 2);
        font.setColor(Color.RED);
        font.getData().setScale(3);
        if(fileOneName != null){
            font.draw(batch, fileOneName, 345, 325);
        }
        if(fileTwoName != null){
            font.draw(batch, fileTwoName, 345, 220);
        }
        if(fileThreeName != null){
            font.draw(batch, fileThreeName, 345, 115);
        }
        batch.end();
    }

    public void moveSlide(boolean forward, LinkedList<String> list) {
        int currentSlide = list.indexOf(slide);
        if(forward){
            int next = currentSlide + 1;
            slide = list.get(next);
        }
        else{
            int next = currentSlide - 1;
            slide = list.get(next);
        }
    }

    @Override
    public boolean keyTyped(char character) {
        int code = (int) character;
        if(name.length() < 12 && slide.equals("chooseName")){
            if((65 <= code && 90 >= code) || (97 <= code && 122 >= code)){
                name = name + character;
            }
        }
        if(Gdx.input.isKeyPressed(Keys.BACKSPACE) && name.length() > 0 && slide.equals("chooseName")){
            char[] nameList = name.toCharArray();
            char[] letters = Arrays.copyOfRange(nameList, 0, nameList.length - 1);
            name = "";
            for(char i : letters){
                name = name + i;
            }
        }
        return false;
    }

    public static void load() {
        try {
            Scanner inOne = new Scanner(new BufferedReader(new FileReader("SaveOne.txt")));
            Scanner inTwo = new Scanner(new BufferedReader(new FileReader("SaveTwo.txt")));
            Scanner inThree = new Scanner(new BufferedReader(new FileReader("SaveThree.txt")));
            if(inOne.hasNextLine()) {
                fileOneName = inOne.nextLine();
            }
            if(inTwo.hasNextLine()) {
                fileTwoName = inTwo.nextLine();
            }
            if(inThree.hasNextLine()) {
                fileThreeName = inThree.nextLine();
            }
        }
        catch(IOException ioe){
            System.out.println("file error");
        }
    }

    public static void load(String filename) {
        inFile = new ArrayList<String>();
        try {
            Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
            while(in.hasNextLine()){
                inFile.add(in.nextLine());
            }
        }
        catch(IOException ioe) {
            System.out.println("corrupted game file");
        }
    }

    public void getEmpty() {
        try {
            Scanner inOne = new Scanner(new BufferedReader(new FileReader("SaveOne.txt")));
            Scanner inTwo = new Scanner(new BufferedReader(new FileReader("SaveTwo.txt")));
            Scanner inThree = new Scanner(new BufferedReader(new FileReader("SaveThree.txt")));
            if(!inOne.hasNextLine()) {
                selectedFile = "SaveOne.txt";
            }
            else if(!inTwo.hasNextLine()) {
                selectedFile = "SaveTwo.txt";
            }
            else if(!inThree.hasNextLine()) {
                selectedFile = "SaveThree.txt";
            }
        }
        catch(IOException ioe){
            System.out.println("file error");
        }
    }

    public void setPlayer() {
        name = inFile.get(0);
        gender = inFile.get(1);
        classType = inFile.get(2);
        gsm.makePlayer(name, gender, classType, selectedFile, cm);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Buttons.LEFT) {
            if(slide.equals("chooseFile") && loadRect.contains(Gdx.input.getX(), Gdx.input.getY())){
                load();
                moveSlide(true, loadSlides);
            }
            if(slide.equals("chooseFile") && newRect.contains(Gdx.input.getX(), Gdx.input.getY())){
                moveSlide(true, slides);
            }

            if(slide.equals("chooseGender") && maleRect.contains(Gdx.input.getX(), Gdx.input.getY())){
                gender = "male";
                moveSlide(true, slides);
            }
            else if(slide.equals("chooseGender") && femaleRect.contains(Gdx.input.getX(), Gdx.input.getY())){
                gender = "female";
                moveSlide(true, slides);
            }

            if(slide.equals("chooseClass") && archerRect.contains(Gdx.input.getX(), Gdx.input.getY())) {
                classType = "archer";
                getEmpty();
                if(selectedFile == ""){
                    wipeFile = true;
                    load();
                    slide = "chooseSave";
                }
                else {
                    gsm.makePlayer(name, gender, classType, selectedFile, cm);
                    gsm.pushState(3);
                }
            }
            else if(slide.equals("chooseClass") && knightRect.contains(Gdx.input.getX(), Gdx.input.getY())) {
                classType = "knight";
                getEmpty();
                if(selectedFile == ""){
                    wipeFile = true;
                    load();
                    slide = "chooseSave";
                }
                else{
                    gsm.makePlayer(name, gender, classType, selectedFile, cm);
                    gsm.pushState(3);
                }
            }

            if(backRect.contains(Gdx.input.getX(), Gdx.input.getY()) &&
                    (slide.equals("chooseName") || slide.equals("chooseGender") || slide.equals("chooseClass"))){
                moveSlide(false, slides);
            }
            else if(backRect.contains(Gdx.input.getX(), Gdx.input.getY()) && (slide.equals("chooseSave"))){
                moveSlide(false, loadSlides);
            }

            if(slide.equals("chooseSave") && fileOneName != null && saveOne.contains(Gdx.input.getX(), Gdx.input.getY())){
                selectedFile = "SaveOne.txt";
            }
            else if(slide.equals("chooseSave") && fileTwoName != null && saveTwo.contains(Gdx.input.getX(), Gdx.input.getY())){
                selectedFile = "SaveTwo.txt";
            }
            else if(slide.equals("chooseSave") && fileThreeName != null && saveThree.contains(Gdx.input.getX(), Gdx.input.getY())){
                selectedFile = "SaveThree.txt";
            }
            return true;
        }
        return false;
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int buttons) {
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
