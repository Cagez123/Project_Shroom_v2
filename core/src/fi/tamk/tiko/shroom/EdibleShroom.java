package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Kalle on 17.3.2017.
 */

public class EdibleShroom extends Sprite {
    private Texture playTexture;
    private float height;
    private float width;
    private float speedX;
    private float speedY;
    private float rotationAngle;
    private int min = 1;
    private int max = 3;
    int random;
    private float gravity = 20f;



    public EdibleShroom(float x, float y) {

        randomTexture();
        if(random == 1) {
            height = playTexture.getHeight() / 2;
            width = playTexture.getWidth() / 2;
        } else if(random == 2) {
            height = playTexture.getHeight() / 8;
            width = playTexture.getWidth() / 8;
        } else {
            height = playTexture.getHeight() / 4;
            width = playTexture.getWidth() / 4;
        }
        setX(x);
        setY(y);
        setBounds(getX(),getY(),width,height);
    }

    public Texture randomTexture() {
        random = MathUtils.random(min, max);
        if(random == 1) {
            playTexture = new Texture("red.png");
            return playTexture;
        } else if(random == 2) {
            playTexture = new Texture("purple.png");
            return playTexture;
        } else {
            playTexture = new Texture("yellow.png");
            return playTexture;
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
        batch.draw(new TextureRegion(playTexture),getX(),getY(),width/2,height/2,width,height,1,1,getRotation());
    }

    public void dispose() {
        playTexture.dispose();
    }

}


