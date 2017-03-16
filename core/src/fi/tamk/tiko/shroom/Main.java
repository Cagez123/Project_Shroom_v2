package fi.tamk.tiko.shroom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class Main extends Game {
	SpriteBatch batch;
	public BitmapFont font;


	@Override
	public void create () {
		batch = new SpriteBatch();
		font =  new BitmapFont(Gdx.files.internal("font.txt"));
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();

	}
}
