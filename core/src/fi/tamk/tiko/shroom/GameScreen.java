package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kalle on 11.3.2017.
 */

public class GameScreen implements Screen, GestureDetector.GestureListener {

    final Main game;
    OrthographicCamera camera;
    SpriteBatch batch;

    public static final int GAME_RUNNING = 0;
    public static final int GAME_PAUSED = 1;
    public static final int GAME_START = 2;
    public static final int GAME_END = 3;
    public static final int INFO_1 = 4;
    public static final int INFO_2 = 5;

    int state = 2;

    float clock = 60;
    float startClock = 3.4f;
    int score = 0;
    int multiplier = 1;
    float timerBarWidth = 8 * clock;

    Mushroom flingMe;
    Mushroom mushroom;

    PsykoFilter tripping;

    public ArrayList<Mushroom> mushrooms = new ArrayList<Mushroom>();
    public ArrayList<Mushroom> removableMushrooms = new ArrayList<Mushroom>();
    public ArrayList<Mushroom> flyingMushrooms = new ArrayList<Mushroom>();


    long lastSpawnTime;
    Texture background;
    Texture pause;
    Texture resume;
    Texture help;
    Texture backToMenu;
    Texture cap;
    Texture smokeTexture;
    Texture timerBar;
    Texture infoPage1;
    Texture infoPage2;
    Texture filter;
    Rectangle menuRectangle;
    Rectangle backToMenuRect;
    Rectangle helpRectangle;
    Rectangle infoRectangle;

    Texture basket;
    Rectangle basketRectangle;

    boolean touchedShroom;
    boolean smoke = false;
    boolean puffballTimer = false;
    boolean libertyCapTimer = false;
    boolean trip = false;
    float despawnTimer = 6;
    float explosionTimer = 3;
    float tripTimer = 3;
    float realTime;

    MyTextInputListener listener;
    boolean accepted = false;


    public GameScreen(final Main game) {
        this.game = game;

        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("forest.png"));
        pause = new Texture(Gdx.files.internal("pauseButton.png"));
        resume = new Texture(Gdx.files.internal("playButton.png"));
        basket = new Texture(Gdx.files.internal("basketempty.png"));
        smokeTexture = new Texture(Gdx.files.internal("cloud.png"));
        cap = new Texture(Gdx.files.internal("cap.png"));
        help = new Texture(Gdx.files.internal("infoButton.png"));
        backToMenu = new Texture(Gdx.files.internal("menuButton.png"));
        tripping = new PsykoFilter();
        timerBar = new Texture(Gdx.files.internal("timerBar.png"));
        infoPage1 = new Texture(Gdx.files.internal("infoScreenPage1.png"));
        infoPage2 = new Texture(Gdx.files.internal("infoScreenPage2.png"));
        filter = new Texture(Gdx.files.internal("psyko.png"));

        getBasketTexture();
        basketRectangle = new Rectangle();
        basketRectangle.setX(130);
        basketRectangle.setY(540);
        basketRectangle.setSize(basket.getWidth()/1.5f,basket.getHeight()/1.5f);

        menuRectangle = new Rectangle();
        menuRectangle.setX(480 - pause.getWidth() / 3);
        menuRectangle.setY(800 - pause.getHeight() / 3);
        menuRectangle.setSize(pause.getWidth() / 3, pause.getHeight() / 3);

        backToMenuRect = new Rectangle();
        backToMenuRect.setX(300);
        backToMenuRect.setY(800 - backToMenu.getHeight() / 3);
        backToMenuRect.setSize(backToMenu.getWidth() / 3, backToMenu.getHeight() / 3);

        helpRectangle = new Rectangle();
        helpRectangle.setX(350);
        helpRectangle.setY(720-help.getHeight()/3);
        helpRectangle.setSize(help.getWidth() / 3, help.getHeight() / 3);

        infoRectangle = new Rectangle();
        infoRectangle.setX(-75);
        infoRectangle.setY(75);
        infoRectangle.setSize(infoPage1.getWidth(), infoPage1.getHeight());


        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
    }
    private Texture getBasketTexture() {
        if (score >= 1000 && score < 6000) {
            basket.dispose();
            basket = new Texture(Gdx.files.internal("basketsome.png"));
        } else if (score >= 6000) {
            basket.dispose();
            basket = new Texture(Gdx.files.internal("Basketfull.png"));
        } else {
            basket = new Texture(Gdx.files.internal("basketempty.png"));
        }
        return basket;
    }
    private void spawnLibertyCap() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                libertyCapTimer = false;
            }
        }, 6);
    }
    private void spawnPuffBall() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                puffballTimer = false;
            }
        }, 6);
    }
    private void spawnMushroom() {
        int shroom = MathUtils.random(1,10);
        switch(shroom) {
            case 1: case 2: case 3: case 4: case 5: case 6:
                mushroom = new EdibleShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    break;
            case 7: case 8:
                mushroom = new PoisonousShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    break;
            case 9:
                if(!smoke && !puffballTimer){
                mushroom = new PuffballShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    puffballTimer = true;
                    spawnPuffBall();
                    break;
                } else {
                    mushroom = new EdibleShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    break;
                }
            case 10:
                if(!libertyCapTimer) {
                    mushroom = new LibertyCapShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    mushroom.libertycap = true;
                    libertyCapTimer = true;
                    spawnLibertyCap();
                    break;
                }  else {
                    mushroom = new EdibleShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    break;
                }

        }
        for (int i = 0; i < mushrooms.size(); i++) {
            try {
                while (mushroom.getBoundingRectangle().overlaps(mushrooms.get(i).getBoundingRectangle())) {
                    mushroom.setPosition(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    i = 0;
                    Gdx.app.log("TAG:", "LOOP");
                }
            } catch (Exception e) {
                break;
            }
        }
        mushrooms.add(mushroom);

        lastSpawnTime =TimeUtils.millis();
    }

    @Override
    public void show() {

    }
    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input(String text) {
            if(text.length() > 0 && text.length() < 7) {
                game.setName(text);
                accepted = true;
            } else {
                askName();
            }

        }

        @Override
        public void canceled() {
            game.setName("No One");
        }
    }
    public void askName() {

        listener = new MyTextInputListener();
        if (!accepted) {
            Gdx.input.getTextInput(listener, "New Highscore!", "", "Max 6 letters");
        }

    }

    public void gameOver() {

        if (clock <= 0) {
            state = 3;
        }
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, 480,800);
        batch.draw(basket, 100,490,basket.getWidth(),basket.getHeight());


        if (state == 3) {
            game.font.draw(batch,"GAME OVER", 20, 500);
            game.font.draw(batch,"Your score: ", 20, 450);
            game.font.draw(batch,Integer.toString(score), 20, 400);
            game.font.draw(batch,"Highscore: ", 20, 350);
            game.font.draw(batch,Integer.toString(game.getHighScore()), 20, 300);

            batch.draw(resume, 475 - resume.getWidth() / 3,10, resume.getWidth() / 3, resume.getHeight() / 3);

            batch.draw(backToMenu,5,10,backToMenu.getHeight()/3,backToMenu.getWidth()/3);

        }
        if (state == 2) {
            game.fontScore.draw(batch,"score: ", 20, 770);
            game.fontScore.draw(batch,Integer.toString(score), 20, 730);
            game.fontMultiplier.draw(batch, "combo: x" + Integer .toString(multiplier), 20, 700);
            batch.draw(timerBar,0,0,timerBarWidth,20);
            game.fontMultiplier.draw(batch, String.format("%.01f",clock), 0, 40);

            if (startClock <= 0.5) {
                game.font.draw(batch, "GO!", 180, 400);
            } else {
                game.font.draw(batch, String.format("%.0f",startClock), 180, 400);
            }
        }
        if (state == 1) {
            batch.draw(cap,280,590);
            batch.draw(resume, 480 - resume.getWidth() / 3, 800 - resume.getHeight() / 3, resume.getWidth() / 3, resume.getHeight() / 3);

            batch.draw(backToMenu,300,800-backToMenu.getHeight()/3,backToMenu.getHeight()/3,backToMenu.getWidth()/3);
            batch.draw(help,350,720-help.getHeight()/3,help.getHeight()/3,help.getWidth()/3);
        }
        if (state == 0) {
            if (score < 0){
                score = 0;
            }
            game.fontScore.draw(batch,"score: ", 20, 770);
            game.fontScore.draw(batch,Integer.toString(score), 20, 730);
            game.fontMultiplier.draw(batch, "combo: x" + Integer .toString(multiplier), 20, 700);
            game.fontMultiplier.draw(batch, String.format("%.01f",clock), 0, 40);
            batch.draw(pause, 480-pause.getWidth()/3,800-pause.getHeight()/3 , pause.getWidth()/3,pause.getHeight()/3);
            batch.draw(timerBar,0,0,timerBarWidth,20);
            Gdx.app.log("TAG", " " + trip );
            for (Mushroom mush : mushrooms) {
                mush.draw(batch);
            }
            for (Mushroom mush : flyingMushrooms) {
                mush.draw(batch);
            }

            if (trip){
                tripping.draw(batch);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        trip = false;
                    }
                }, tripTimer);
            }

            if (smoke) {
                batch.draw(smokeTexture, -140, -140,smokeTexture.getWidth()*1.7f,smokeTexture.getHeight()*1.7f);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        smoke = false;
                    }
                }, explosionTimer);
            }
        }
        if (state == 4){
            batch.draw(cap,280,590);
            batch.draw(resume, 480 - resume.getWidth() / 3, 800 - resume.getHeight() / 3, resume.getWidth() / 3, resume.getHeight() / 3);

            batch.draw(backToMenu,300,800-backToMenu.getHeight()/3,backToMenu.getHeight()/3,backToMenu.getWidth()/3);
            batch.draw(help,350,720-help.getHeight()/3,help.getHeight()/3,help.getWidth()/3);
            batch.draw(infoPage1, -75, 75, infoPage1.getWidth()*1.1f,infoPage1.getHeight()*1.1f);
        }
        if (state == 5){
            batch.draw(cap,280,590);
            batch.draw(resume, 480 - resume.getWidth() / 3, 800 - resume.getHeight() / 3, resume.getWidth() / 3, resume.getHeight() / 3);

            batch.draw(backToMenu,300,800-backToMenu.getHeight()/3,backToMenu.getHeight()/3,backToMenu.getWidth()/3);
            batch.draw(help,350,720-help.getHeight()/3,help.getHeight()/3,help.getWidth()/3);
            batch.draw(infoPage2, -75, 75, infoPage2.getWidth()*1.1f,infoPage2.getHeight()*1.1f);
        }
        batch.end();

        //STATES
        switch(state){
            case  GAME_END:

                menuRectangle.setX( 475 - resume.getWidth() / 3);
                menuRectangle.setY(10);
                backToMenuRect.setX(5);
                backToMenuRect.setY(10);
                if (score > game.getHighScore()) {
                    game.setHighScore(score);
                    askName();
                }
                if(Gdx.input.justTouched()) {
                    Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(tmp);
                    if (menuRectangle.contains(tmp.x, tmp.y)) {
                        mushrooms.clear();
                        flyingMushrooms.clear();
                        removableMushrooms.clear();
                        clock = 60;
                        realTime = 0;
                        score = 0;
                        startClock = 3.4f;
                        state = 2;
                        multiplier = 1;
                    }
                    if (backToMenuRect.contains(tmp.x, tmp.y)) {
                        dispose();
                        mushrooms.clear();
                        flyingMushrooms.clear();
                        removableMushrooms.clear();
                        game.setScreen(new MenuScreen(game));
                    }
                }

                break;
            case GAME_START:
                startClock-=delta;
                if (startClock <= -0.5f) {
                    state = 0;
                }
                break;
            case GAME_RUNNING:
                menuRectangle.setX(480 - pause.getWidth() / 3);
                menuRectangle.setY(800 - pause.getHeight() / 3);
                backToMenuRect.setX(300);
                backToMenuRect.setY(800 - backToMenu.getHeight() / 3);
                clock-=delta;
                timerBarWidth = 8 * clock;
                realTime+=delta;
                gameOver();

                // THIS IS THE SPAWING RATE TIMER
                if(realTime < 5) {

                    if (TimeUtils.millis() - lastSpawnTime > 1500) {
                        spawnMushroom();
                    }
                } else if (realTime > 5 && realTime < 10) {
                    if (TimeUtils.millis() - lastSpawnTime > 1400) {
                        spawnMushroom();
                    }
                } else if (realTime > 10 && realTime < 15) {
                    if (TimeUtils.millis() - lastSpawnTime > 1300) {
                        spawnMushroom();
                    }
                } else if (realTime > 15 && realTime < 20) {
                    if (TimeUtils.millis() - lastSpawnTime > 1200) {
                        spawnMushroom();
                    }
                } else if (realTime > 20 && realTime < 25) {
                    if (TimeUtils.millis() - lastSpawnTime > 1100) {
                        spawnMushroom();

                    }
                } else if (realTime > 25 && realTime < 30) {
                    if (TimeUtils.millis() - lastSpawnTime > 1000) {
                        spawnMushroom();

                    }
                } else if (realTime > 30 && realTime < 35) {
                    if (TimeUtils.millis() - lastSpawnTime > 900) {
                        spawnMushroom();

                    }
                } else if (realTime > 35 && realTime < 40) {
                    if (TimeUtils.millis() - lastSpawnTime > 800) {
                        spawnMushroom();

                    }
                } else if (realTime > 40 && realTime < 45) {
                    if (TimeUtils.millis() - lastSpawnTime > 700) {
                        spawnMushroom();

                    }
                } else if (realTime > 45 && realTime < 55) {
                    if (TimeUtils.millis() - lastSpawnTime > 600) {
                        spawnMushroom();
                    }
                }  else {
                    if (TimeUtils.millis() - lastSpawnTime > 500) {
                        spawnMushroom();
                    }
                }
                //MUSHROOM ITERATOR
                for(final Mushroom mush : flyingMushrooms) {
                    mush.pulsate();
                    if (mush.getBoundingRectangle().overlaps(basketRectangle)) {

                        if (mush.getScore() < 0) {
                            score = score + mush.getScore();
                            clock = clock + mush.getTime();
                            getBasketTexture();
                            mush.flying = false;
                            multiplier = 1;
                        } else {
                            score = score + mush.getScore() * multiplier;
                            clock = clock + mush.getTime();
                            mush.flying = false;
                            getBasketTexture();
                            if(multiplier <= 9) {
                                multiplier = multiplier + 1;
                            } else {
                                multiplier = 10;
                            }
                        }

                    } else if (mush.getX() <= -100 || mush.getX() >= 600 ||
                            mush.getY() <= -100 || mush.getY() >= 900) {
                        score = score + mush.getSidePoints() * multiplier;
                        clock = clock + mush.getBonusTime();
                        mush.flying = false;
                    }
                    if (!mush.getTimerSet()) {

                        mush.setTimerSet(true);

                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                removableMushrooms.add(mush);

                            }
                        }, 2);
                    }



                    removableMushrooms.add(mush);
                }
                for (final Mushroom mush : mushrooms) {
                    mush.pulsate();

                    if (mush.explosion) {
                        removableMushrooms.add(mush);
                        multiplier = 1;
                        smoke = true;

                    }
                    if (!mush.getTimerSet()) {

                        mush.setTimerSet(true);

                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                removableMushrooms.add(mush);
                            }
                        }, despawnTimer);
                    }
                }
                if (mushrooms.size() > 8) {
                    mushrooms.remove(0);
                }
                for (final Mushroom removable : removableMushrooms) {

                    if(!removable.flying)
                        flyingMushrooms.remove(removable);


                    mushrooms.remove(removable);
                }
                /*Gdx.app.log("Flying LIST SIZE", "" + flyingMushrooms.size());
                Gdx.app.log("mushrooms LIST SIZE", "" + mushrooms.size());
                Gdx.app.log("removable LIST SIZE", "" + removableMushrooms.size());*/
                removableMushrooms.clear();

                //BACK BUTTON
                if(Gdx.input.justTouched()) {
                    Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(tmp);
                    if (menuRectangle.contains(tmp.x, tmp.y)) {
                        state = 1;
                    }
                }


                break;
            case GAME_PAUSED:

                if(Gdx.input.justTouched()) {
                    Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(tmp);
                    if (menuRectangle.contains(tmp.x, tmp.y)) {
                        state = 0;
                    }
                    if (backToMenuRect.contains(tmp.x, tmp.y)) {
                        dispose();
                        mushrooms.clear();
                        flyingMushrooms.clear();
                        removableMushrooms.clear();
                        game.setScreen(new MenuScreen(game));
                    }
                    if (helpRectangle.contains(tmp.x, tmp.y)) {
                        state = 4;
                    }
                }
                break;
            case INFO_1:
                if(Gdx.input.justTouched()) {
                    Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(tmp);
                    if (menuRectangle.contains(tmp.x, tmp.y)) {
                        state = 0;
                    }
                    if (backToMenuRect.contains(tmp.x, tmp.y)) {
                        dispose();
                        mushrooms.clear();
                        flyingMushrooms.clear();
                        removableMushrooms.clear();
                        game.setScreen(new MenuScreen(game));
                    }
                    if (infoRectangle.contains (tmp.x, tmp.y)){
                        state = 5;
                    }

                }
                break;
            case INFO_2:
                if(Gdx.input.justTouched()) {
                    Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(tmp);
                    if (menuRectangle.contains(tmp.x, tmp.y)) {
                        state = 0;
                    }
                    if (backToMenuRect.contains(tmp.x, tmp.y)) {
                        dispose();
                        mushrooms.clear();
                        flyingMushrooms.clear();
                        removableMushrooms.clear();
                        game.setScreen(new MenuScreen(game));
                    }
                    if (infoRectangle.contains (tmp.x, tmp.y)){
                        state = 4;
                    }

                }


        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        pause.dispose();
        resume.dispose();
        basket.dispose();
        smokeTexture.dispose();
        backToMenu.dispose();
        help.dispose();
        cap.dispose();
        infoPage1.dispose();
        infoPage2.dispose();
        for (Mushroom mush : mushrooms) {
            mush.dispose();
        }
        for (Mushroom mush : flyingMushrooms) {
            mush.dispose();
        }


        batch.dispose();

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);

        for (Mushroom mush : mushrooms) {
            if (mush.getBoundingRectangle().contains(tmp.x, tmp.y)) {
                touchedShroom = true;
                Gdx.app.log("TAG", "TOUCHED");

                if (mush.libertycap) {
                    // TRIP
                    trip = true;
                    removableMushrooms.add(mush);



                } else {
                    flingMe = mushrooms.get(mushrooms.indexOf(mush));
                    flyingMushrooms.add(flingMe);
                    removableMushrooms.add(flingMe);

                    flingMe.flying = true;
                }
            }

        }
        for (Mushroom mush : flyingMushrooms) {
            if (mush.getBoundingRectangle().contains(tmp.x, tmp.y)) {
                touchedShroom = true;
                Gdx.app.log("TAG", "TOUCHED");

                flingMe = flyingMushrooms.get(flyingMushrooms.indexOf(mush));
                removableMushrooms.add(flingMe);

                flingMe.flying = true;
            }

        }

        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        if(touchedShroom) {
            flingMe.move(velocityX, velocityY);
            touchedShroom = false;
        }
        Gdx.app.log("TAG", "FLINGED X" + velocityX);
        Gdx.app.log("TAG", "FLINGED Y" + velocityY);

        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }


}
