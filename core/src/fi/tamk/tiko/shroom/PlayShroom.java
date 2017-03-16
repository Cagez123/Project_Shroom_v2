package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Kalle on 11.3.2017.
 */

public class PlayShroom extends Sprite {

    private Texture playTexture;
    private float height;
    private float width;
    private float speedX;
    private float speedY;
    private float rotationAngle;



    public PlayShroom(float x, float y) {

        playTexture = new Texture("red.png");
        height = playTexture.getHeight();
        width = playTexture.getWidth();
        setX(x);
        setY(y);
        setBounds(getX(),getY(),width,height);
    }

    public void move(float velocityX, float velocityY) {
        speedX = velocityX;
        speedY = velocityY;
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
