package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Kalle on 23.3.2017.
 */

public class PoisonousShroom extends Mushroom {

    private float height;
    private float width;
    private float speedX;
    private float speedY;
    private float rotationAngle;
    private int min = 1;
    private int max = 2;
    private int random;
    private float gravity = 20f;
    int score = -50;
    int sidePoints = 5;
    float time = -0.5f;
    float bonusTime = 0.3f;

    boolean larger = true;
    float maxScale = 1f;

    public PoisonousShroom(float x, float y) {

        randomTexture();

            height = playTexture.getHeight() / 2;
            width = playTexture.getWidth() / 2;

        setX(x);
        setY(y);
        setScale(0.9f);
        setBounds(getX(),getY(),width,height);

    }

    public Texture randomTexture() {
        random = MathUtils.random(min, max);
        if(random == 1) {
            playTexture = new Texture("kärpässieniglow.png");
            return playTexture;
        }else if(random == 2) {
            playTexture = new Texture("ValkokärpäsSieniglow.png");
            return playTexture;
        }
        return playTexture;
    }
    public void pulsate() {
        if(larger) {
            setScale(getScaleX() + 0.0025f,getScaleY() + 0.0025f);
        }
        if (getScaleX() >= maxScale) {
            larger = false;
        }

        if (!larger) {
            setScale(getScaleX() - 0.0025f, getScaleY() - 0.0025f);
        }
        if (getScaleX() <= 0.9) {
            larger = true;
        }

    }
    public void move(float velocityX, float velocityY) {
        speedX = velocityX;
        speedY = velocityY;

        if(velocityY !=0 || velocityX !=0) {
            speedY = velocityY + gravity;
        }

        if (speedX < 0) {
            rotationAngle += 7.0f;
        } else if (speedX > 0) {
            rotationAngle -= 7.0f;
        }

        setRotation(rotationAngle);
        setX(getX() + speedX/2 * Gdx.graphics.getDeltaTime());
        setY(getY() - speedY/2 * Gdx.graphics.getDeltaTime());
    }


    public void draw(SpriteBatch batch){
        move(speedX,speedY);
        batch.draw(new TextureRegion(playTexture),getX(),getY(),width/2,height/2,width,height,getScaleX(),getScaleY(),getRotation());

    }

    public void dispose() {
        playTexture.dispose();

    }

    public int getScore() {
        return score;
    }
    public int getSidePoints() { return sidePoints; }
    public float getTime() {
        return time;
    }
    public float getBonusTime() { return bonusTime;}


}




