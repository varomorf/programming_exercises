package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.platypusit.libgdx.courseraGameProgPort.GameConstants.BEAR_FIRING_RATE_RANGE;
import static com.platypusit.libgdx.courseraGameProgPort.GameConstants.BEAR_MIN_FIRING_DELAY;

/**
 * A class for a teddy bear.
 *
 * Created by alfergon on 18/01/17.
 */
public class TeddyBear {

    public static final String TEXTURE_PATH = "graphics/teddybear.png";

    private static final ProjectileType PROJECTILE_TYPE = ProjectileType.TEDDY_BEAR;

    private boolean active = true;

    // drawing support
    private Texture texture;
    private Rectangle drawRectangle;

    // velocity information
    private Vector2 velocity = new Vector2(0, 0);

    // shooting support
    private int elapsedShotMilliseconds = 0;
    private int firingDelay;

    // sound effects TODO
    //private SoundEffect bounceSound;
    //private SoundEffect shootSound;

    /**
     * Constructs a teddy bear centered on the given x and y with the given velocity.
     * @param texture The texture to be used for the teddy bear.
     * @param x the x location of the center of the teddy bear.
     * @param y the y location of the center of the teddy bear.
     * @param velocity the velocity of the teddy bear.
     */
    public TeddyBear(Texture texture, int x, int y, Vector2 velocity) {
        this.texture = texture;
        this.velocity = velocity;

        int centeredX = x - texture.getWidth() / 2;
        int centeredY = y - texture.getHeight() / 2;
        drawRectangle = new Rectangle(centeredX, centeredY, texture.getWidth(), texture.getHeight());

        firingDelay = getRandomFiringDelay();
    }

    /**
     * Draws the teddy bear
     * @param batch the sprite batch to use
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, drawRectangle.x, drawRectangle.y);
    }

    /**
     * Gets a random firing delay between MIN_FIRING_DELAY and MIN_FIRING_DELAY + FIRING_RATE_RANGE
     * @return the random firing delay
     */
    protected int getRandomFiringDelay() {
        return RandomNumberGenerator.next(BEAR_MIN_FIRING_DELAY, BEAR_FIRING_RATE_RANGE);
    }
}
