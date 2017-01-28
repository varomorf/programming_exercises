package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static com.platypusit.libgdx.courseraGameProgPort.GameConstants.*;

public class CourseraGameProgramingPort extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture teddyBearTexture;
    private static Texture teddyBearProjectileSprite;
    private static Texture frenchFriesSprite;

	private Burger burger;
    private List<TeddyBear> bears = new ArrayList<>();
    private static List<Projectile> projectiles = new ArrayList<>();

    private BitmapFont bitmapFont;

    // scoring support
    private int score = 0;
    private String scoreString = GameConstants.SCORE_PREFIX + 0;

    // health support
    private String healthString = GameConstants.HEALTH_PREFIX + GameConstants.BURGER_INITIAL_HEALTH;
    private boolean burgerDead = false;

    /**
     * <p>Gets the projectile sprite for the given projectile type.</p>
     * @param type the projectile type.
     * @return the projectile sprite for the type.
     */
    public static Texture getProjectileSprite(ProjectileType type)
    {
        if (type.equals(ProjectileType.TEDDY_BEAR))
        {
            return teddyBearProjectileSprite;
        }

        // defaults to frenchFriesSprite
        return frenchFriesSprite;
    }

    /**
     * <p>Adds the given projectile to the game.</p>
     * @param projectile the projectile to add.
     */
    public static void addProjectile(Projectile projectile)
    {
        projectiles.add(projectile);
    }

    @Override
	public void create () {
		batch = new SpriteBatch();

		// load textures
		Texture burgerTexture = new Texture(Burger.TEXTURE_PATH);
		teddyBearTexture = new Texture(TeddyBear.TEXTURE_PATH);
        teddyBearProjectileSprite = new Texture(Projectile.TEDDY_BEAR_PROJECTILE_TEXTURE_PATH);
        frenchFriesSprite = new Texture(Projectile.FRENCH_FRIES_PROJECTILE_TEXTURE_PATH);

        // load fonts
        bitmapFont = new BitmapFont();

        // add burger object calculating x and y (no need to center sprite as Burger constructor does it)
        int burgerX = WINDOW_WIDTH / 2;
        int burgerY = WINDOW_HEIGHT / 8;
        burger = new Burger(burgerTexture, burgerX, burgerY);

		// spawn bears
		for(int i = 0; i < GameConstants.MAX_BEARS; i++)
		{
			spawnBear();
		}
	}

	@Override
	public void render () {
		update();
		draw();
	}

	private void update(){
        // turn delta time from seconds to millis
        float deltaSeconds = Gdx.graphics.getDeltaTime();
        burger.update(deltaSeconds);
        for (TeddyBear bear : bears) {
            bear.update(deltaSeconds);
        }
        for (Projectile projectile : projectiles) {
            projectile.update(deltaSeconds);
        }
    }

    private void draw(){
        Color clearColor = Color.TEAL;
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		burger.draw(batch);

        for (TeddyBear bear : bears) {
            bear.draw(batch);
        }

        for (Projectile projectile : projectiles) {
            projectile.draw(batch);
        }

        // draw score and health
        bitmapFont.draw(batch, healthString, HEALTH_LOCATION.x, HEALTH_LOCATION.y);
        bitmapFont.draw(batch, scoreString, SCORE_LOCATION.x, SCORE_LOCATION.y);

        batch.end();
	}

	private void spawnBear() {
        // generate random location
        int bearX = getRandomLocation(SPAWN_BORDER_SIZE, WINDOW_WIDTH - SPAWN_BORDER_SIZE);
        int bearY = getRandomLocation(SPAWN_BORDER_SIZE, WINDOW_HEIGHT - SPAWN_BORDER_SIZE);

        // generate random velocity and create a vector with it
        float velMagnitude = RandomNumberGenerator.nextFloat(BEAR_SPEED_RANGE);
        if(velMagnitude < MIN_BEAR_SPEED)
        {
            velMagnitude = MIN_BEAR_SPEED;
        }
        float velAngle = RandomNumberGenerator.nextFloat((float) (2 * Math.PI));
        float velX = (float) (velMagnitude * Math.cos(velAngle));
        float velY = (float) (velMagnitude * Math.sin(velAngle));
        Vector2 bearVelocity = new Vector2(velX, velY);

        // create new bear
        TeddyBear newBear = new TeddyBear(teddyBearTexture, bearX, bearY, bearVelocity);

        // add new bear to list
        bears.add(newBear);
	}

    /**
     * Updates the health string after damaging.
     * @param amount The amount of damage to the burger.
     */
    private void burgerDamaged(int amount)
    {
        burger.setHealth(-amount);
        healthString = GameConstants.HEALTH_PREFIX + burger.getHealth();
        //burgerDamage.Play(); TODO sound
    }

    /**
     * Updates the score string.
     */
    private void updateScoreString()
    {
        scoreString = GameConstants.SCORE_PREFIX + score;
    }

    /**
     * Gets a random location using the given min and range
     * @param min the minimum
     * @param range the range
     * @return the random location
     */
    private int getRandomLocation(int min, int range)
    {
        return min + RandomNumberGenerator.next(range);
    }

}
