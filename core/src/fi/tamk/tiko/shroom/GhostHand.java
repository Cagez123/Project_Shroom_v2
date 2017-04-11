package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Kalle on 6.4.2017.
 */

public class GhostHand extends Sprite {
    Texture hand;
    private float height;
    private float width;
    private float speedX;
    private float speedY;
    private float rotationAngle;
    boolean turn = true;

    public GhostHand() {
        hand = new Texture(Gdx.files.internal("ani18.png"));
        height = hand.getHeight();
        width = hand.getWidth();
        setX(200);
        setY(100);
        setBounds(getX(),getY(),width,height);
    }
    public void move() {
        speedX = 80;
        speedY = 200;
        if (getY() >= 300) {
            setY(100);
            setX(200);
            turn = true;
        }
        if (turn) {
            setX(getX() - speedX * Gdx.graphics.getDeltaTime());
            if (getX() <= 150) {
                turn = false;
            }
        } else {
            setX(getX() + speedX * Gdx.graphics.getDeltaTime());
            if (getX() >= 250) {
                turn = true;
            }
        }

        setY(getY() + speedY * Gdx.graphics.getDeltaTime());

    }


    public void draw(SpriteBatch batch){
        move();
        batch.draw(new TextureRegion(hand),getX(),getY(),width/2,height/2,width,height,getScaleX(),getScaleY(),getRotation());
    }
    public void dispose() {
        hand.dispose();
    }

}