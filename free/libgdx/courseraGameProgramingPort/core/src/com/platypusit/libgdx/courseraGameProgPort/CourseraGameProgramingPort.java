package com.platypusit.libgdx.courseraGameProgPort;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CourseraGameProgramingPort extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture burgerTexture;

	private Burger burger;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		burgerTexture = new Texture(Burger.TEXTURE_PATH);

        // add burger object calculating x and y (no need to center sprite as Burger constructor does it)
        int burgerX = GameConstants.WINDOW_WIDTH / 2;
        int burgerY = GameConstants.WINDOW_HEIGHT / 8;
        burger = new Burger(burgerTexture, burgerX, burgerY);
	}

	@Override
	public void render () {
		update();
		draw();
	}

	private void update(){

	}

    private void draw(){
        Color clearColor = Color.TEAL;
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		burger.draw(batch);
		batch.end();
	}
}
