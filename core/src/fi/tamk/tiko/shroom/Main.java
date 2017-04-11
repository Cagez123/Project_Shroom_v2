package fi.tamk.tiko.shroom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main extends Game {
	SpriteBatch batch;
	public BitmapFont font;
	public BitmapFont fontClock;
	public BitmapFont fontMultiplier;
    public BitmapFont fontScore;
    public BitmapFont fontHighScore;
    boolean nameAsked = false;



    public static Preferences prefs;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font =  new BitmapFont(Gdx.files.internal("font.txt"));
		fontHighScore =  new BitmapFont(Gdx.files.internal("font.txt"));
		fontClock =  new BitmapFont(Gdx.files.internal("font.txt"));
		fontScore =  new BitmapFont(Gdx.files.internal("font.txt"));
		fontMultiplier =  new BitmapFont(Gdx.files.internal("font.txt"));
        fontMultiplier.getData().setScale(.3f);
        font.getData().setScale(.8f);
        fontScore.getData().setScale(.5f);
        fontHighScore.getData().setScale(.4f);

        prefs = Gdx.app.getPreferences("MyPreferences");
        prefs.putString("name", getName());
        prefs.putBoolean("soundOn", true);
        prefs.putInteger("highscore", 0);
        prefs.flush();
		this.setScreen(new MenuScreen(this));

	}

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }
    public void setAsked(boolean asked) {
        nameAsked = asked;
    }
    public boolean getAsked() {
        return nameAsked;
    }

    public static void setName(String name) {
        prefs.putString("name", name);
        prefs.flush();
    }

    public static String getName() {
        return  prefs.getString("name");
    }
	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
        fontHighScore.dispose();
        fontClock.dispose();
        fontScore.dispose();
        fontMultiplier.dispose();

	}
}
