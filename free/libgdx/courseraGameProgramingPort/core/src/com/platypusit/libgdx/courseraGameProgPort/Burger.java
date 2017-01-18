package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * A burger.
 *
 * Created by alfergon on 17/01/17.
 */
public class Burger {

    public static final String TEXTURE_PATH = "graphics/burger.png";

    private static final ProjectileType PROJECTILE_TYPE = ProjectileType.FRENCH_FRIES;

    // graphic and drawing info
    Texture texture;
    Rectangle drawRectangle;

    // burger stats
    int health = 100;

    // shooting support
    boolean canShoot = true;
    int elapsedCooldownMilliseconds = 0;

    // sound effect TODO
    //SoundEffect shootSound;

    /**
     * <p>Creates a new Burger specifying its texture and position.</p>
     *
     * TODO sound
     * @param texture The burger's texture.
     * @param x The x position.
     * @param y The y position.
     */
    public Burger(Texture texture, int x, int y) {
        this.texture = texture;

        int centeredX = x - texture.getWidth() / 2;
        int centeredY = y - texture.getHeight() / 2;
        drawRectangle = new Rectangle(centeredX, centeredY, texture.getWidth(), texture.getHeight());
    }

    public Rectangle getCollisionRectangle(){
        return drawRectangle;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if(health < 0){
            this.health = 0;
        }

        this.health = health;
    }

    public void update(){

    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, drawRectangle.x, drawRectangle.y);
    }
}
