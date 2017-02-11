package com.platypusit.libgdx.gameportusingashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.platypusit.libgdx.gameportusingashley.ComponentMappers;
import com.platypusit.libgdx.gameportusingashley.components.DrawableComponent;
import com.platypusit.libgdx.gameportusingashley.components.PositionComponent;

/**
 * System for drawing entities.
 * Created by alfergon on 30/01/17.
 */
public class DrawingSystem extends IteratingSystem{

    private static final Family family = Family.all(PositionComponent.class, DrawableComponent.class).get();

    private SpriteBatch batch;

    public DrawingSystem(SpriteBatch batch) {
        super(family);
        this.batch = batch;
    }

    public DrawingSystem(int priority, SpriteBatch batch) {
        super(family, priority);
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        DrawableComponent drawable = ComponentMappers.drawable.get(entity);

        float x = position.x - (drawable.texture.getWidth() / 2);
        float y = position.y - (drawable.texture.getHeight() / 2);
        batch.draw(drawable.texture, x, y);
    }
}
