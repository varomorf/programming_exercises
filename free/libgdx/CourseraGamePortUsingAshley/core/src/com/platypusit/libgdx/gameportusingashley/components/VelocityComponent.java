package com.platypusit.libgdx.gameportusingashley.components;

import com.badlogic.ashley.core.Component;

/**
 * Component for velocity.
 *
 * Created by alfergon on 30/01/17.
 */
public class VelocityComponent implements Component{

	public float x = 0.0f;
	public float y = 0.0f;

	public VelocityComponent() {
	}

	public VelocityComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
