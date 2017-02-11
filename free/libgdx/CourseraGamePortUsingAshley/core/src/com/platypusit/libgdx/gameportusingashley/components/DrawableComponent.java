package com.platypusit.libgdx.gameportusingashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

/**
 * A component for drawable entities.
 * Created by alfergon on 30/01/17.
 */
public class DrawableComponent implements Component{

	public Texture texture;

	public DrawableComponent() {
	}

	public DrawableComponent(Texture texture) {
		this.texture = texture;
	}

}
