package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.math.Vector2;

/**
 * All the constants used in the game
 *
 * Created by alfergon on 17/01/17.
 */
public class GameConstants {

    // resolution
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    // projectile characteristics
    public static final float TEDDY_BEAR_PROJECTILE_SPEED = 0.3f;
    public static final int TEDDY_BEAR_PROJECTILE_DAMAGE = 5;
    public static final int TEDDY_BEAR_PROJECTILE_OFFSET = 20;
    public static final float FRENCH_FRIES_PROJECTILE_SPEED = 0.4f;
    public static final int FRENCH_FRIES_PROJECTILE_DAMAGE = 5;
    public static final int FRENCH_FRIES_PROJECTILE_OFFSET = 10;

    // bear characteristics
    public static final int MAX_BEARS = 5;
    public static final int BEAR_POINTS = 10;
    public static final int BEAR_DAMAGE = 10;
    public static final float MIN_BEAR_SPEED = 0.1f;
    public static final float BEAR_SPEED_RANGE = 0.2f;
    public static final int BEAR_MIN_FIRING_DELAY = 500;
    public static final int BEAR_FIRING_RATE_RANGE = 1000;

    // burger characteristics
    public static final int BURGER_INITIAL_HEALTH = 100;
    public static final int BURGER_MOVEMENT_AMOUNT = 10;
    public static final int BURGER_TOTAL_COOLDOWN_MILLISECONDS = 500;

    // explosion hard-coded animation info. There are better
    // ways to do this, we just don't know enough to use them yet
    public static final int EXPLOSION_FRAMES_PER_ROW = 3;
    public static final int EXPLOSION_NUM_ROWS = 3;
    public static final int EXPLOSION_NUM_FRAMES = 9;
    public static final int EXPLOSION_TOTAL_FRAME_MILLISECONDS = 10;

    // display support
    private static final int DisplayOffset = 35;
    public static final String SCORE_PREFIX = "Score: ";
    public static final Vector2 SCORE_LOCATION = new Vector2(DisplayOffset, DisplayOffset);
    public static final String HEALTH_PREFIX = "Health: ";
    public static final Vector2 HEALTH_LOCATION = new Vector2(DisplayOffset, 2 * DisplayOffset);

    // spawn location support
    public static final int SPAWN_BORDER_SIZE = 100;

}