package com.platypusit.libgdx.gameportusingashley.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platypusit.libgdx.gameportusingashley.CourseraGamePortUsingAshley;
import com.platypusit.libgdx.gameportusingashley.GameConstants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConstants.WINDOW_WIDTH;
		config.height = GameConstants.WINDOW_HEIGHT;

		new LwjglApplication(new CourseraGamePortUsingAshley(), config);
	}
}
