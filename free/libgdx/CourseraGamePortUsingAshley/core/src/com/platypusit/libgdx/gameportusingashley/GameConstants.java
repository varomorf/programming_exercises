package com.platypusit.libgdx.gameportusingashley;

import com.badlogic.gdx.math.Vector2;

/**
 * All the constants used in the game.
 *
 * Speeds or time related values are in units per second.
 *
 * Created by alfergon on 17/01/17.
 */
public class GameConstants {

    // resolution
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    // projectile characteristics
    public static final int TEDDY_BEAR_PROJECTILE_SPEED = 250;
    public static final int TEDDY_BEAR_PROJECTILE_DAMAGE = 5;
    public static final int TEDDY_BEAR_PROJECTILE_OFFSET = 30;
    public static final int FRENCH_FRIES_PROJECTILE_SPEED = 400;
    public static final int FRENCH_FRIES_PROJECTILE_DAMAGE = 5;
    public static final int FRENCH_FRIES_PROJECTILE_OFFSET = 10;

    // bear characteristics
    public static final int MAX_BEARS = 5;
    public static final int BEAR_POINTS = 10;
    public static final int BEAR_DAMAGE = 10;
    public static final int MIN_BEAR_SPEED = 100;
    public static final int BEAR_SPEED_RANGE = 200;
    public static final float BEAR_MIN_FIRING_DELAY = 0.5f;
    public static final float BEAR_FIRING_RATE_RANGE = 1f;

    // burger characteristics
    public static final int BURGER_INITIAL_HEALTH = 100;
    public static final int BURGER_MOVEMENT_AMOUNT = 10;
    public static final float BURGER_TOTAL_COOLDOWN_SECONDS = 0.5f;

    // explosion hard-coded animation info. There are better
    // ways to do this, we just don't know enough to use them yet
    public static final int EXPLOSION_FRAMES_PER_ROW = 3;
    public static final int EXPLOSION_NUM_ROWS = 3;
    public static final int EXPLOSION_NUM_FRAMES = 9;
    public static final float EXPLOSION_TOTAL_FRAME_SECONDS = 0.01f;

    // display support
    private static final int DisplayOffset = 35;
    public static final String SCORE_PREFIX = "Score: ";
    public static final Vector2 SCORE_LOCATION = new Vector2(DisplayOffset, DisplayOffset);
    public static final String HEALTH_PREFIX = "Health: ";
    public static final Vector2 HEALTH_LOCATION = new Vector2(DisplayOffset, 2 * DisplayOffset);

    // spawn location support
    public static final int SPAWN_BORDER_SIZE = 100;

}
