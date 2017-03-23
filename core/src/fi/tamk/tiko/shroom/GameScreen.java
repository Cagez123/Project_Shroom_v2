package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by Kalle on 11.3.2017.
 */

public class GameScreen implements Screen, GestureDetector.GestureListener {
    final Main game;
    OrthographicCamera camera;
    float clock = 60;
    int score = 0;
    EdibleShroom flingMe;
    EdibleShroom mush;
    EdibleShroom mushroom;
    Array<EdibleShroom> mushrooms = new Array<EdibleShroom>();
    long lastSpawnTime;
    Texture background;
    Texture menu;
    Rectangle menuRectangle;
    boolean touchedShroom;


    public GameScreen(final Main game) {
        this.game = game;
        spawnMushroom();
        background = new Texture(Gdx.files.internal("background.jpg"));
        menu = new Texture(Gdx.files.internal("back-icon.png"));

        menuRectangle = new Rectangle();
        menuRectangle.setX(480 - menu.getWidth() / 6);
        menuRectangle.setY(800 - menu.getHeight() / 6);
        menuRectangle.setSize(menu.getWidth() / 6, menu.getHeight() / 6);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);


    }

    private void spawnMushroom() {

        mushroom = new EdibleShroom(MathUtils.random(10, 340), MathUtils.random(10, 400));

        for (EdibleShroom mush : mushrooms) {

            while (mushroom.getBoundingRectangle().overlaps(mush.getBoundingRectangle())) {
                mushroom.setPosition(MathUtils.random(10, 340), MathUtils.random(10, 400));
            }
        }

        mushrooms.add(mushroom);

        lastSpawnTime =TimeUtils.millis();
    }
    @Override
    public void show() {

    }
    public void gameOver() {
        if (clock <= 0) {
            game.setScreen(new HighscoreScreen(game));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, background.getWidth(),background.getHeight());
        game.batch.draw(menu, 480-menu.getWidth()/6,800-menu.getHeight()/6 , menu.getWidth()/6,menu.getHeight()/6);
        game.font.draw(game.batch, String.format("%.01f",clock), 180, 770);
        game.font.draw(game.batch, Integer.toString(score), 20, 770);
        for (EdibleShroom mush : mushrooms) {
            mush.draw(game.batch);
        }
        game.batch.end();

        clock-=delta;
        gameOver();

        if (TimeUtils.millis() - lastSpawnTime > 2000) {
            if(mushrooms.size <= 9) {
                spawnMushroom();
            }
        }

        Iterator<EdibleShroom> iter = mushrooms.iterator();
        while(iter.hasNext()) {
            mush = iter.next();
            if (mush.getY() >= 800) {
                score = score + 100;
                iter.remove();
            } else if (mush.getX() <= -100 || mush.getX() >= 600 || mush.getY() <= -100) {
                score = score - 100;
                iter.remove();
            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(tmp);
            if (menuRectangle.contains(tmp.x, tmp.y)) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        }
        if (Gdx.input.isTouched()) {

            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(tmp);

            for (EdibleShroom mush : mushrooms) {
                if (mush.getBoundingRectangle().contains(tmp.x, tmp.y)) {
                    touchedShroom = true;
                    Gdx.app.log("TAG", "TOUCHED");
                    flingMe = mushrooms.get(mushrooms.indexOf(mush,true));

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
        for (EdibleShroom mush : mushrooms) {
            mush.dispose();
        }

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
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
