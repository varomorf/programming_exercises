package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.stream.IntStream;

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

    private static IntStream firingDelayRandom = new Random().ints(BEAR_MIN_FIRING_DELAY, BEAR_FIRING_RATE_RANGE + 1);

    public TeddyBear(Texture texture, int x, int y, Vector2 velocity) {
        this.texture = texture;
        this.velocity = velocity;

        int centeredX = x - texture.getWidth() / 2;
        int centeredY = y - texture.getHeight() / 2;
        drawRectangle = new Rectangle(centeredX, centeredY, texture.getWidth(), texture.getHeight());

        firingDelay = getRandomFiringDelay();
    }

    protected int getRandomFiringDelay() {
        return firingDelayRandom.findAny().getAsInt();
    }
}
