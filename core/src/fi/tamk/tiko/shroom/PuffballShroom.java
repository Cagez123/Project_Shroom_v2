package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Kalle on 23.3.2017.
 */

public class PuffballShroom extends Mushroom {

    private float height;
    private float width;
    private float speedX;
    private float speedY;
    private float rotationAngle;
    private float gravity = 20f;
    private long delay = 3;
    private int score = 5;
    int sidePoints = 5;
    boolean angle;


    public PuffballShroom(float x, float y) {

        playTexture = new Texture("PuffBall.png");

        height = playTexture.getHeight()/2.2f;
        width = playTexture.getWidth()/2.2f;
        setX(x);
        setY(y);
        setScale(0.6f);
        setBounds(getX(),getY(),width,height);
        startTimer();
    }
    public void startTimer() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                explosion = true;
            }
        }, delay);
    }
    public void pulsate() {

        setScale(getScaleX() + 0.002f,getScaleY() + 0.002f);
        if (angle) {
            rotationAngle += 5.0f;
            if(getRotation() >= 5) {
                angle = false;
            }
        } else if (!angle) {
            rotationAngle -= 5.0f;
            if(getRotation() <= -5) {
                angle = true;
            }
        }

        setRotation(rotationAngle);
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
    public float getTime() {
        return time;
    }
    public int getSidePoints() { return sidePoints; }


}


