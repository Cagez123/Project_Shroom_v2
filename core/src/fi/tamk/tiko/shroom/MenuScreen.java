package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Kalle on 11.3.2017.
 */

public class MenuScreen implements Screen, GestureDetector.GestureListener {
    final Main game;
    PlayShroom playShroom;
    HighscoreShroom highscoreShroom;
    DexShroom dexShroom;
    OrthographicCamera camera;
    Texture background;
    boolean play = false;
    boolean highscore = false;
    boolean shroomdex = false;
    boolean decision = false;

    /*public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
        }

        @Override
        public void canceled () {
        }
    }*/

    public MenuScreen(final Main game) {

        this.game = game;
        playShroom = new PlayShroom(180, 300);
        highscoreShroom = new HighscoreShroom();
        dexShroom = new DexShroom();
        background = new Texture(Gdx.files.internal("background.jpg"));


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);



        game.batch.begin();
        game.batch.draw(background, -400, 0, background.getWidth(),background.getHeight());

        playShroom.draw(game.batch);
        highscoreShroom.draw(game.batch);
        dexShroom.draw(game.batch);
        game.batch.end();

        if(playShroom.getX() < -100 || playShroom.getX() > 480 - playShroom.getWidth()) {
            game.setScreen(new GameScreen(game));
            decision = false;
            dispose();
        }
        if(playShroom.getY() < -100 || playShroom.getY() > 1000 - playShroom.getHeight()) {
            game.setScreen(new GameScreen(game));
            decision = false;
            dispose();
        }
        if(!decision) {

            if (Gdx.input.isTouched()) {

                Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(tmp);
                if (playShroom.getBoundingRectangle().contains(tmp.x, tmp.y)) {
                    play = true;
                    Gdx.app.log("TAG", "TOUCHED");
                    decision = true;
                }
            }
        }
        if(highscoreShroom.getX() < -100 || highscoreShroom.getX() > 480 - highscoreShroom.getWidth()) {
            game.setScreen(new HighscoreScreen(game));
            decision = false;
            dispose();
        }
        if(highscoreShroom.getY() < -100 || highscoreShroom.getY() > 1000 - highscoreShroom.getHeight()) {
            game.setScreen(new HighscoreScreen(game));
            decision = false;
            dispose();
        }
        if(!decision) {
            if (Gdx.input.isTouched()) {

                Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(tmp);
                if (highscoreShroom.getBoundingRectangle().contains(tmp.x, tmp.y)) {
                    highscore = true;
                    Gdx.app.log("TAG", "TOUCHED");
                    decision = true;
                }
            }
        }
        if(dexShroom.getX() < -100 || dexShroom.getX() > 480 - dexShroom.getWidth()) {
            game.setScreen(new ShroomdexScreen(game));
            decision = false;
            dispose();
        }
        if(dexShroom.getY() < -100 || dexShroom.getY() > 1000 - dexShroom.getHeight()) {
            game.setScreen(new ShroomdexScreen(game));
            decision = false;
            dispose();
        }
        if(!decision) {

            if (Gdx.input.isTouched()) {

                Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(tmp);
                if (dexShroom.getBoundingRectangle().contains(tmp.x, tmp.y)) {
                    shroomdex = true;
                    Gdx.app.log("TAG", "TOUCHED");
                    decision = true;
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
        playShroom.dispose();
        dexShroom.dispose();
        highscoreShroom.dispose();
        background.dispose();
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
        if (play) {
            playShroom.move(velocityX, velocityY);
            Gdx.app.log("TAG", "FLINGED X" + velocityX);
            Gdx.app.log("TAG", "FLINGED Y" + velocityY);
            play = false;

        }
        if (highscore) {
            highscoreShroom.move(velocityX, velocityY);
            Gdx.app.log("TAG", "FLINGED X" + velocityX);
            Gdx.app.log("TAG", "FLINGED Y" + velocityY);
            highscore = false;
        }
        if (shroomdex) {
            dexShroom.move(velocityX, velocityY);
            Gdx.app.log("TAG", "FLINGED X" + velocityX);
            Gdx.app.log("TAG", "FLINGED Y" + velocityY);
            shroomdex = false;
        }
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
