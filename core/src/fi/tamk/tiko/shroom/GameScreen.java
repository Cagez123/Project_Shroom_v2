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

    int state = 0;

    float clock = 60;
    int score = 0;
    int multiplier = 1;

    Mushroom flingMe;
    Mushroom mushroom;

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
    Rectangle menuRectangle;
    Rectangle backToMenuRect;
    Rectangle helpRectangle;

    Texture basket;
    Rectangle basketRectangle;

    boolean touchedShroom;
    boolean smoke = false;
    boolean puffballTimer = false;
    float despawnTimer = 6;
    float explosionTimer = 3;
    float realTime;

    MyTextInputListener listener;



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
    private void spawnPuffBall() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                puffballTimer = false;
            }
        }, 6);
    }
    private void spawnMushroom() {
        int shroom = MathUtils.random(1,8);
        switch(shroom) {
            case 1: case 2: case 3: case 4: case 8: case 9:
                mushroom = new EdibleShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    break;
            case 5: case 6:
                mushroom = new PoisonousShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    break;
            case 7:
                if(!smoke && !puffballTimer){
                mushroom = new PuffballShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));
                    puffballTimer = true;
                    spawnPuffBall();
                    break;
                } else {
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
            game.setName(text);
        }

        @Override
        public void canceled() {
        }
    }
    public void gameOver() {
        if (clock <= 0) {
            if(score > game.getHighScore()) {
                game.setHighScore(score);
                listener = new MyTextInputListener();
                Gdx.input.getTextInput(listener, "New Highscore!", "", "Mushroom Master");
            }

            dispose();
            flyingMushrooms.clear();
            mushrooms.clear();
            removableMushrooms.clear();
            game.setScreen(new MenuScreen(game));

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
        game.fontClock.draw(batch, String.format("%.01f",clock), 180, 640);
        game.fontScore.draw(batch,"score: ", 20, 770);
        game.fontScore.draw(batch,Integer.toString(score), 20, 730);
        game.fontMultiplier.draw(batch, "combo: x" + Integer .toString(multiplier), 20, 700);

        if (state == 1) {
            batch.draw(cap,280,590);
            batch.draw(resume, 480 - resume.getWidth() / 3, 800 - resume.getHeight() / 3, resume.getWidth() / 3, resume.getHeight() / 3);


            batch.draw(backToMenu,300,800-backToMenu.getHeight()/3,backToMenu.getHeight()/3,backToMenu.getWidth()/3);
            batch.draw(help,350,720-help.getHeight()/3,help.getHeight()/3,help.getWidth()/3);
        }
        if (state == 0) {
            batch.draw(pause, 480-pause.getWidth()/3,800-pause.getHeight()/3 , pause.getWidth()/3,pause.getHeight()/3);


            for (Mushroom mush : mushrooms) {
                mush.draw(batch);
            }
            for (Mushroom mush : flyingMushrooms) {
                mush.draw(batch);
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
        batch.end();

        //STATES
        switch(state){
            case GAME_RUNNING:
                clock-=delta;
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
                            mush.flying = false;
                            multiplier = 1;
                        } else {
                            score = score + mush.getScore() * multiplier;
                            clock = clock + mush.getTime();
                            mush.flying = false;
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
                Gdx.app.log("Flying LIST SIZE", "" + flyingMushrooms.size());
                Gdx.app.log("mushrooms LIST SIZE", "" + mushrooms.size());
                Gdx.app.log("removable LIST SIZE", "" + removableMushrooms.size());
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
                }
                break;
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

                flingMe = mushrooms.get(mushrooms.indexOf(mush));
                flyingMushrooms.add(flingMe);
                removableMushrooms.add(flingMe);

                flingMe.flying = true;
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
