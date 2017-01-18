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
    private float elapsedShotSeconds = 0;
    private float firingDelay;

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
     * Updates the teddy bear's location, bouncing if necessary. Also has the teddy bear fire a projectile when it's time to.
     * @param deltaSeconds delta time in seconds
     */
    public void update(float deltaSeconds)
    {
        // move the teddy bear
        drawRectangle.x += (int)(velocity.x * deltaSeconds);
        drawRectangle.y += (int)(velocity.y * deltaSeconds);

        // bounce as necessary
        bounceTopBottom();
        bounceLeftRight();

        // fire projectile as appropriate
        elapsedShotSeconds += deltaSeconds;

        if(elapsedShotSeconds > firingDelay)
        {
            elapsedShotSeconds = 0;
            firingDelay = getRandomFiringDelay();

            // TODO firing
//            Texture projectileSprite = Game1.GetProjectileSprite(projectileType);
//            Vector2 center = Vector2.Zero;
//            drawRectangle.getCenter(center);
//
//            int projectileY = (int) (center.y + GameConstants.TEDDY_BEAR_PROJECTILE_OFFSET);
//            Projectile projectile = new Projectile(projectileType, projectileSprite, drawRectangle.Center.X, projectileY, -GetProjectileYVelocity());
//            Game1.AddProjectile(projectile);

            // use instance to lower volume as it was horrible TODO sound
            //SoundEffectInstance instance = shootSound.CreateInstance();
            //instance.Volume = 0.2f;
            //instance.Play();
        }
        // timer concept (for animations) introduced in Chapter 7

    }

    /**
     * Draws the teddy bear
     * @param batch the sprite batch to use
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, drawRectangle.x, drawRectangle.y);
    }

    /**
     * Bounces the teddy bear off the top and bottom window borders if necessary
     */
    private void bounceTopBottom()
    {
        if (drawRectangle.y < 0)
        {
            // bounce off top
            drawRectangle.y = 0;
            velocity.y *= -1;
            //bounceSound.Play();
        }
        else if ((drawRectangle.y + drawRectangle.height) > GameConstants.WINDOW_HEIGHT)
        {
            // bounce off bottom
            drawRectangle.y = GameConstants.WINDOW_HEIGHT - drawRectangle.height;
            velocity.y *= -1;
            //bounceSound.Play();
        }
    }

    /**
     * Bounces the teddy bear off the left and right window borders if necessary
     */
    private void bounceLeftRight()
    {
        if (drawRectangle.x < 0)
        {
            // bounc off left
            drawRectangle.x = 0;
            velocity.x *= -1;
            //bounceSound.Play();
        }
        else if ((drawRectangle.x + drawRectangle.width) > GameConstants.WINDOW_WIDTH)
        {
            // bounce off right
            drawRectangle.x = GameConstants.WINDOW_WIDTH - drawRectangle.width;
            velocity.x *= -1;
            //bounceSound.Play();
        }
    }

    /**
     * Gets a random firing delay between MIN_FIRING_DELAY and MIN_FIRING_DELAY + FIRING_RATE_RANGE
     * @return the random firing delay
     */
    protected float getRandomFiringDelay() {
        return RandomNumberGenerator.nextFloat(BEAR_MIN_FIRING_DELAY, BEAR_FIRING_RATE_RANGE);
    }

}
