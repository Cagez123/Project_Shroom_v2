package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Kalle on 23.3.2017.
 */

public abstract class Mushroom extends Sprite {

    protected Texture playTexture;

    public boolean flying;

    public boolean explosion = false;

    private boolean timerSet;
    private float height;
    private float width;
    private float speedX;
    private float speedY;
    private float rotationAngle;
    private float gravity = 20f;
    protected int score;
    protected float time;
    protected  int sidePoints;
    protected float bonusTime;
    public void dispose(Texture texture) {
        texture.dispose();
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

    public Texture getPlayTexture() {

        return playTexture;
    }

    public void draw(SpriteBatch batch){
        move(speedX,speedY);
        batch.draw(new TextureRegion(playTexture),getX(),getY(),width/2,height/2,width,height,1,1,getRotation());
    }

    public int getScore() {
        return score;
    }
    public float getTime() {
        return time;
    }
    public int getSidePoints() { return sidePoints; }
    public float getBonusTime() { return bonusTime; }
    public boolean getTimerSet() { return timerSet; }
    public void setTimerSet(boolean timerSet) {

        this.timerSet = timerSet;
    }
}


