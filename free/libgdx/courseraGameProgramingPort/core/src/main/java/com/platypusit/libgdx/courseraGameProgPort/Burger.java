package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle2D;
import com.badlogic.gdx.math.Vector2;

import static com.platypusit.libgdx.courseraGameProgPort.CourseraGameProgramingPort.addProjectile;
import static com.platypusit.libgdx.courseraGameProgPort.CourseraGameProgramingPort.getProjectileSprite;

/**
 * <p>A burger.</p>
 * Created by alfergon on 17/01/17.
 */
public class Burger {

    public static final String TEXTURE_PATH = "graphics/burger.png";

    private static final ProjectileType PROJECTILE_TYPE = ProjectileType.FRENCH_FRIES;

    // graphic and drawing info
    private Texture texture;
    private Rectangle2D drawRectangle;
    private Vector2 center = new Vector2();

    // burger stats
    private int health = 100;

    // shooting support
    private boolean canShoot = true;
    private float elapsedCooldownSeconds = 0;

    // sound effect TODO
    //SoundEffect shootSound;

    /**
     * <p>Creates a new Burger specifying its texture and position.</p>
     * TODO sound
     *
     * @param texture The burger's texture.
     * @param x       The x position.
     * @param y       The y position.
     */
    public Burger(Texture texture, int x, int y) {
        this.texture = texture;

        drawRectangle = new Rectangle2D(0, 0, texture.getWidth(), texture.getHeight());
        drawRectangle.setCenter(x, y);
    }

    public Rectangle2D getCollisionRectangle() {
        return drawRectangle;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
        }

        this.health = health;
    }

    /**
     * Updates the burger's location
     * @param deltaSeconds delta time in seconds
     */
    public void update(float deltaSeconds) {
        // burger should only respond to input if it still has health
        if(health > 0)
        {
            // move burger using keyboard
            if (Gdx.input.isKeyPressed(Input.Keys.W))
            {
                drawRectangle.y += GameConstants.BURGER_MOVEMENT_AMOUNT;
            }else if (Gdx.input.isKeyPressed(Input.Keys.S))
            {
                drawRectangle.y -= GameConstants.BURGER_MOVEMENT_AMOUNT;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A))
            {
                drawRectangle.x -= GameConstants.BURGER_MOVEMENT_AMOUNT;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D))
            {
                drawRectangle.x += GameConstants.BURGER_MOVEMENT_AMOUNT;
            }

            // clamp burger in window
            if(drawRectangle.x < 0)
            {
                drawRectangle.x = 0;
            }else if(drawRectangle.getRight() > GameConstants.WINDOW_WIDTH)
            {
                drawRectangle.x = GameConstants.WINDOW_WIDTH - drawRectangle.width;
            }
            if (drawRectangle.y < 0)
            {
                drawRectangle.y = 0;
            }
            else if (drawRectangle.getBottom() > GameConstants.WINDOW_HEIGHT)
            {
                drawRectangle.y = GameConstants.WINDOW_HEIGHT - drawRectangle.height;
            }

            // update shooting allowed
            if (!canShoot)
            {
                elapsedCooldownSeconds += deltaSeconds;

                if(elapsedCooldownSeconds >= GameConstants.BURGER_TOTAL_COOLDOWN_SECONDS || !Gdx.input.isKeyPressed(Input.Keys.SPACE))
                {
                    canShoot = true;
                    elapsedCooldownSeconds = 0;
                }
            }

            // timer concept (for animations) introduced in Chapter 7

            // shoot if appropriate
            if (canShoot && Gdx.input.isKeyPressed(Input.Keys.SPACE))
            {
                canShoot = false;

                Texture projectileSprite = getProjectileSprite(PROJECTILE_TYPE);

                drawRectangle.getCenter(center);
                float projectileY = center.y + GameConstants.FRENCH_FRIES_PROJECTILE_OFFSET;
                Projectile projectile = new Projectile(PROJECTILE_TYPE, projectileSprite, center.x, projectileY, GameConstants.FRENCH_FRIES_PROJECTILE_SPEED);
                addProjectile(projectile);
                //TODO sound
                //shootSound.Play();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, drawRectangle.x, drawRectangle.y);
    }
}
