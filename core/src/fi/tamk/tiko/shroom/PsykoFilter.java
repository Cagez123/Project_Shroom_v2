package fi.tamk.tiko.shroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Kalle on 20.4.2017.
 */

public class PsykoFilter extends Sprite{
    Texture psykoTexture;
    float rotationAngle;
    float rotationSpeed;
    boolean larger = true;
    float maxScale = 1.4f;

    public PsykoFilter(){
        psykoTexture = new Texture(Gdx.files.internal("psyko.png"));
    }

    public void move(){
        rotationAngle += rotationSpeed;
        setRotation(rotationAngle);

        if(larger) {
            setScale(getScaleX() + 0.0025f,getScaleY() + 0.0025f);
        }
        if (getScaleX() >= maxScale) {
            larger = false;
        }

        if (!larger) {
            setScale(getScaleX() - 0.0025f, getScaleY() - 0.0025f);
        }
        if (getScaleX() <= 1.0f) {
            larger = true;
        }
    }

    public void draw(SpriteBatch batch){
        move();
        batch.draw(new TextureRegion(psykoTexture),0,0,getWidth()/2,getHeight()/2,getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

}

