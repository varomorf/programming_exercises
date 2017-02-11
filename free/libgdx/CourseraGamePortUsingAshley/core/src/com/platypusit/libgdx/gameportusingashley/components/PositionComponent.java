package com.platypusit.libgdx.gameportusingashley.components;

import com.badlogic.ashley.core.Component;

/**
 * Component for position. The position will be related to the center of the entity.
 * <p>
 * Created by alfergon on 30/01/17.
 */
public class PositionComponent implements Component {

    public float x = 0.0f;
    public float y = 0.0f;

    public PositionComponent() {
    }

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
