package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle2D;

import static com.platypusit.libgdx.courseraGameProgPort.GameConstants.EXPLOSION_FRAMES_PER_ROW;
import static com.platypusit.libgdx.courseraGameProgPort.GameConstants.EXPLOSION_NUM_ROWS;

/**
 * An animated explosion object.
 */
public class Explosion {

    public static final String EXPLOSION_TEXTURE_PATH = "graphics/explosion.png";

    // object location
    private Rectangle2D drawRectangle;

    // animation strip info
    private Texture strip;
    private int frameWidth;
    private int frameHeight;

    // fields used to track and draw animations
    private Rectangle2D sourceRectangle;
    private int currentFrame;
    private float elapsedFrameTime = 0;

    // playing or not
    private boolean playing = false;
    private boolean finished = false;

    /**
     * <p>Constructs a new explosion object</p>
     * @param spriteStrip the sprite strip for the explosion
     * @param x the x location of the center of the explosion
     * @param y the y location of the center of the explosion
     * @param explosionSound Sound for the explosion
     */
    public Explosion(Texture spriteStrip, float x, float y, Sound explosionSound)
    {
        // initialize animation to start at frame 0
        currentFrame = 0;

        initialize(spriteStrip);
        play(x, y);
        explosionSound.play();
    }

    /**
     * <p>Gets the collision rectangle for the explosion</p>
     * @return the collision rectangle
     */
    public Rectangle2D getCollisionRectangle()
    {
        return drawRectangle;
    }

    /**
     * <p>Gets whether or not the explosion is finished</p>
     * @return true if finished, false otherwise
     */
    public boolean isFinished()
    {
        return finished;
    }

    /**
     * <p>Updates the explosion. This only has an effect if the explosion animation is playing</p>
     * @param deltaSeconds the game time in seconds
     */
    public void update(float deltaSeconds)
    {
        if (playing)
        {
            // check for advancing animation frame
            elapsedFrameTime += deltaSeconds;
            if (elapsedFrameTime > GameConstants.EXPLOSION_TOTAL_FRAME_SECONDS)
            {
                // reset frame timer
                elapsedFrameTime = 0;

                // advance the animation
                if (currentFrame < GameConstants.EXPLOSION_NUM_FRAMES - 1)
                {
                    currentFrame++;
                    setSourceRectangleLocation(currentFrame);
                }
                else
                {
                    // reached the end of the animation
                    playing = false;
                    finished = true;
                }
            }
        }
    }

    /**
     * <p>Draws the explosion. This only has an effect if the explosion animation is playing</p>
     * @param spriteBatch the spritebatch
     */
    public void draw(SpriteBatch spriteBatch)
    {
        if (playing)
        {
            spriteBatch.draw(strip, drawRectangle.x, drawRectangle.y, (int)sourceRectangle.x, (int)sourceRectangle.y, frameWidth, frameHeight);
        }
    }

    /**
     * <p>Loads the content for the explosion</p>
     * @param spriteStrip the sprite strip for the explosion
     */
    private void initialize(Texture spriteStrip)
    {
        // load the animation strip
        strip = spriteStrip;

        // calculate frame size
        frameWidth = strip.getWidth() / EXPLOSION_FRAMES_PER_ROW;
        frameHeight = strip.getHeight() / EXPLOSION_NUM_ROWS;

        // set initial draw and source rectangles
        drawRectangle = new Rectangle2D(0, 0, frameWidth, frameHeight);
        sourceRectangle = new Rectangle2D(0, 0, frameWidth, frameHeight);
    }

    /**
     * <p>Starts playing the animation for the explosion</p>
     * @param x the x location of the center of the explosion
     * @param y the y location of the center of the explosion
     */
    private void play(float x, float y)
    {
        // reset tracking values
        playing = true;
        elapsedFrameTime = 0;
        currentFrame = 0;

        // set draw location and source rectangle
        drawRectangle.x = x - drawRectangle.width / 2;
        drawRectangle.y = y - drawRectangle.height / 2;
        setSourceRectangleLocation(currentFrame);
    }

    /**
     * <p>Sets the source rectangle location to correspond with the given frame</p>
     * @param frameNumber the frame number
     */
    private void setSourceRectangleLocation(int frameNumber)
    {
        // calculate X and Y based on frame number
        sourceRectangle.x = (frameNumber % EXPLOSION_FRAMES_PER_ROW) * frameWidth;
        sourceRectangle.y = (frameNumber / EXPLOSION_FRAMES_PER_ROW) * frameHeight;
    }

}
