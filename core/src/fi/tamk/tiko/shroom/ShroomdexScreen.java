package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Kalle on 11.3.2017.
 */

public class ShroomdexScreen implements Screen {
    final Main game;
    OrthographicCamera camera;
    Texture menu;
    Rectangle menuRectangle;
    Texture background;



    public ShroomdexScreen(final Main game) {
        this.game = game;
        background = new Texture(Gdx.files.internal("forest.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        menu = new Texture(Gdx.files.internal("menuButton.png"));

        menuRectangle = new Rectangle();
        menuRectangle.setX(480 - menu.getWidth()/3);
        menuRectangle.setY(800 - menu.getHeight()/3);
        menuRectangle.setSize(menu.getWidth()/3, menu.getHeight()/3);

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
        game.batch.draw(background, 0, 0, 480,800);
        game.batch.draw(menu, 480-menu.getWidth()/3,800-menu.getHeight()/3 , menu.getWidth()/3,menu.getHeight()/3);
        game.font.draw(game.batch, "SHROOMDEX ", 60, 150);
        game.batch.end();
        if(Gdx.input.justTouched()) {
            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(tmp);
            if (menuRectangle.contains(tmp.x, tmp.y)) {
                game.setScreen(new MenuScreen(game));
                dispose();
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
        menu.dispose();
    }
}
