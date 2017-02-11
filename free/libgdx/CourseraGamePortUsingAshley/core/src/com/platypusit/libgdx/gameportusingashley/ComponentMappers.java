package com.platypusit.libgdx.gameportusingashley;

import com.badlogic.ashley.core.ComponentMapper;
import com.platypusit.libgdx.gameportusingashley.components.DrawableComponent;
import com.platypusit.libgdx.gameportusingashley.components.PositionComponent;
import com.platypusit.libgdx.gameportusingashley.components.VelocityComponent;

/**
 * Mapper for components.
 * Created by alfergon on 30/01/17.
 */
public class ComponentMappers {
		public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
		public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
		public static final ComponentMapper<DrawableComponent> drawable = ComponentMapper.getFor(DrawableComponent.class);
}
