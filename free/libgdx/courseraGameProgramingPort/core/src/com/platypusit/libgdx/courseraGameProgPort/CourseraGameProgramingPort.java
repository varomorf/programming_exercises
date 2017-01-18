package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.platypusit.libgdx.courseraGameProgPort.GameConstants.*;

public class CourseraGameProgramingPort extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture burgerTexture;
	private Texture teddyBearTexture;

	private Burger burger;
    private List<TeddyBear> bears = new ArrayList<>();

    @Override
	public void create () {
		batch = new SpriteBatch();
		burgerTexture = new Texture(Burger.TEXTURE_PATH);
		teddyBearTexture = new Texture(TeddyBear.TEXTURE_PATH);

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
        long deltaMillis = TimeUtils.millisToNanos((long) Gdx.graphics.getDeltaTime());
        for (TeddyBear bear : bears) {
            bear.update(deltaMillis);
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

        // make sure we don't spawn into a collision TODO
//        List<Rectangle> collisionRectangles = GetCollisionRectangles();
//        while(CollisionUtils.IsCollisionFree(newBear.DrawRectangle, collisionRectangles))
//        {
//            bearX = getRandomLocation(GameConstants.SpawnBorderSize, GameConstants.WindowWidth - GameConstants.SpawnBorderSize);
//            bearY = getRandomLocation(GameConstants.SpawnBorderSize, GameConstants.WindowHeight - GameConstants.SpawnBorderSize);
//
//            newBear.X = bearX;
//            newBear.Y = bearY;
//        }

        // add new bear to list
        bears.add(newBear);
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
