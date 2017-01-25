package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle2D;

/**
 * <p>A class for a projectile.</p>
 * Created by alfergon on 19/01/17.
 */
public class Projectile {

    private boolean active = true;
    private ProjectileType type;

    // drawing support
    private Texture texture;
    private Rectangle2D drawRectangle;

    // velocity information
    /**
     * Number of pixels to move vertically per second.
     */
    private float yVelocity;

    /**
     * Constructs a projectile with the given y velocity
     *
     * @param type      the projectile type
     * @param texture   the texture for the projectile
     * @param x         the x location for the center of the projectile
     * @param y         the y location for the center of the projectile
     * @param yVelocity the y velocity for the projectile
     */
    public Projectile(ProjectileType type, Texture texture, int x, int y, float yVelocity) {
        this.type = type;
        this.texture = texture;
        this.yVelocity = yVelocity;

        drawRectangle = new Rectangle2D(0, 0, texture.getWidth(), texture.getHeight());
        drawRectangle.setCenter(x, y);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ProjectileType getType() {
        return type;
    }

    /**
     * <p>Gets the collision rectangle for the projectile.</p>
     *
     * @return the collision rectangle for the projectile
     */
    public Rectangle2D getCollisionRectangle() {
        return drawRectangle;
    }

    /**
     * <p>Update the projectile.</p>
     * @param deltaSeconds The elapsed seconds.
     */
    public void update(float deltaSeconds) {
        // move projectile
        drawRectangle.y += yVelocity * deltaSeconds;

        // check for outside game window
        if (drawRectangle.getBottom() >= GameConstants.WINDOW_HEIGHT || drawRectangle.getTop() <= 0) {
            active = false;
        }
    }

    /**
     * Draws the projectile
     *
     * @param batch the sprite batch to use
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, drawRectangle.x, drawRectangle.y);
    }

}
