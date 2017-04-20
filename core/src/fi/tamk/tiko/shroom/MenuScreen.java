package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Kalle on 11.3.2017.
 */

public class MenuScreen implements Screen, GestureDetector.GestureListener {
    final Main game;
    PlayShroom playShroom;
    SpriteBatch batch;
    DexShroom dexShroom;

    GhostHand hand;

    OrthographicCamera camera;
    Texture background;
    Texture logo;
    Texture sign;


    boolean play = false;
    boolean shroomdex = false;


    /*public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
        }

        @Override
        public void canceled () {
        }
    }*/

    public MenuScreen(final Main game) {
        batch = new SpriteBatch();
        this.game = game;
        playShroom = new PlayShroom(140, 270);
       // hand = new GhostHand();
        dexShroom = new DexShroom();
        background = new Texture(Gdx.files.internal("forest.png"));
        logo = new Texture(Gdx.files.internal("FungusFrenzy.png"));
        sign = new Texture(Gdx.files.internal("sign.png"));

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
        batch.setProjectionMatrix(camera.combined);

        //game.setHighScore(0);
        //game.setName("Mush");
        batch.begin();
        batch.draw(background, 0, 0, 480,800);
        batch.draw(logo,0,500,logo.getWidth()/4,logo.getHeight()/4);
        batch.draw(sign,5,-20,sign.getWidth()/1.2f, sign.getHeight()/1.2f);
        playShroom.draw(batch);
       // hand.draw(batch);
        dexShroom.draw(batch);
        game.fontHighScore.draw(batch, "HIGHSCORE", 50, 170);
        game.fontHighScore.draw(batch,game.getName() + "    " + Integer.toString(game.getHighScore()),50,120);
        batch.end();

        if(playShroom.getX() < -100 || playShroom.getX() > 480 - playShroom.getWidth()) {
            dispose();
            game.setScreen(new GameScreen(game));

        }
        if(playShroom.getY() < -100 || playShroom.getY() > 1000 - playShroom.getHeight()) {
            dispose();
            game.setScreen(new GameScreen(game));


        }


        if(dexShroom.getX() < -100 || dexShroom.getX() > 480 - dexShroom.getWidth()) {
            dispose();
            game.setScreen(new ShroomdexScreen(game));


        }
        if(dexShroom.getY() < -100 || dexShroom.getY() > 1000 - dexShroom.getHeight()) {
            dispose();
            game.setScreen(new ShroomdexScreen(game));


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
       // hand.dispose();
        background.dispose();
        sign.dispose();
        logo.dispose();
        batch.dispose();

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(tmp);
        if (dexShroom.getBoundingRectangle().contains(tmp.x, tmp.y)) {
            shroomdex = true;
            Gdx.app.log("TAG", "TOUCHED");
        }
        if (playShroom.getBoundingRectangle().contains(tmp.x, tmp.y)) {
            play = true;
            Gdx.app.log("TAG", "TOUCHED");
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
        if (play) {
            playShroom.move(velocityX, velocityY);
            Gdx.app.log("TAG", "FLINGED X" + velocityX);
            Gdx.app.log("TAG", "FLINGED Y" + velocityY);
            play = false;

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
