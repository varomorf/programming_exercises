package com.platypusit.libgdx.gameportusingashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.platypusit.libgdx.gameportusingashley.ComponentMappers;
import com.platypusit.libgdx.gameportusingashley.components.DrawableComponent;
import com.platypusit.libgdx.gameportusingashley.components.PositionComponent;
import com.platypusit.libgdx.gameportusingashley.components.VelocityComponent;

/**
 * System for giving movement to entities.
 * Created by alfergon on 30/01/17.
 */
public class MovementSystem extends IteratingSystem{

    private static final Family family = Family.all(PositionComponent.class, DrawableComponent.class).get();

    public MovementSystem() {
        super(family);
    }

    public MovementSystem(int priority) {
        super(family, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        VelocityComponent velocity = ComponentMappers.velocity.get(entity);

        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

}
