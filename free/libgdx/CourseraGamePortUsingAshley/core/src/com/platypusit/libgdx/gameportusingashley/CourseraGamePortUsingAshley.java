package com.platypusit.libgdx.gameportusingashley;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.platypusit.libgdx.gameportusingashley.entities.TeddyBear;
import com.platypusit.libgdx.gameportusingashley.systems.DrawingSystem;
import com.platypusit.libgdx.gameportusingashley.systems.MovementSystem;

import java.util.List;

import static com.platypusit.libgdx.gameportusingashley.GameConstants.*;

public class CourseraGamePortUsingAshley extends ApplicationAdapter {

	private Engine engine;

	private SpriteBatch batch;

    private Texture teddyBearTexture;
    private static Texture teddyBearProjectileSprite;
    private static Texture frenchFriesSprite;
    private static Texture explosionSpriteStrip;


//    private Burger burger;
//    private static List<Projectile> projectiles = new ArrayList<>();
//    List<Explosion> explosions = new ArrayList<>();

    private BitmapFont bitmapFont;

    // scoring support
    private int score = 0;
    private String scoreString = GameConstants.SCORE_PREFIX + 0;

    // health support
    private String healthString = GameConstants.HEALTH_PREFIX + GameConstants.BURGER_INITIAL_HEALTH;
    private boolean burgerDead = false;

    // sound effects
    Sound burgerDamage;
    Sound burgerDeath;
    Sound burgerShot;
    Sound explosion;
    Sound teddyBounce;
    Sound teddyShot;

	@Override
	public void create () {
		engine = new Engine();
		batch = new SpriteBatch();

		// load textures
        Texture burgerTexture = new Texture("graphics/burger.png");
        teddyBearTexture = new Texture("graphics/teddybear.png");
        teddyBearProjectileSprite = new Texture("graphics/teddybearprojectile.png");
        frenchFriesSprite = new Texture("graphics/frenchfries.png");
        explosionSpriteStrip = new Texture("graphics/explosion.png");

        // load audio content
        burgerDamage = Gdx.audio.newSound(Gdx.files.internal("audio/BurgerDamage.wav"));
        burgerDeath = Gdx.audio.newSound(Gdx.files.internal("audio/BurgerDeath.wav"));
        burgerShot = Gdx.audio.newSound(Gdx.files.internal("audio/BurgerShot.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("audio/Explosion.wav"));
        teddyBounce = Gdx.audio.newSound(Gdx.files.internal("audio/TeddyBounce.wav"));
        teddyShot = Gdx.audio.newSound(Gdx.files.internal("audio/TeddyShot.wav"));

        // spawn bears
        for (int i = 0; i < MAX_BEARS; i++) {
            spawnBear();
        }

		// add systems
        engine.addSystem(new DrawingSystem(batch));
        engine.addSystem(new MovementSystem());
	}

    @Override
	public void render () {
		float deltaSeconds = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		engine.update(deltaSeconds);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

    private void spawnBear() {
        // generate random location
        int bearX = getRandomLocation(SPAWN_BORDER_SIZE, WINDOW_WIDTH - SPAWN_BORDER_SIZE);
        int bearY = getRandomLocation(SPAWN_BORDER_SIZE, WINDOW_HEIGHT - SPAWN_BORDER_SIZE);

        // generate random velocity and create a vector with it
        float velMagnitude = RandomNumberGenerator.nextFloat(BEAR_SPEED_RANGE);
        if (velMagnitude < MIN_BEAR_SPEED) {
            velMagnitude = MIN_BEAR_SPEED;
        }
        float velAngle = RandomNumberGenerator.nextFloat((float) (2 * Math.PI));
        float velX = (float) (velMagnitude * Math.cos(velAngle));
        float velY = (float) (velMagnitude * Math.sin(velAngle));
        Vector2 bearVelocity = new Vector2(velX, velY);

        // add new bear to engine
        engine.addEntity(new TeddyBear(teddyBearTexture, bearX, bearY, bearVelocity, teddyBounce, teddyShot));
    }

    /**
     * Gets a random location using the given min and range
     *
     * @param min   the minimum
     * @param range the range
     * @return the random location
     */
    private int getRandomLocation(int min, int range) {
        return min + RandomNumberGenerator.next(range);
    }
}
