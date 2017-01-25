package com.platypusit.libgdx.courseraGameProgPort.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platypusit.libgdx.courseraGameProgPort.CourseraGameProgramingPort;
import com.platypusit.libgdx.courseraGameProgPort.GameConstants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Teddy bears defend themselves from an evil burger";
		config.width = GameConstants.WINDOW_WIDTH;
		config.height = GameConstants.WINDOW_HEIGHT;
		config.fullscreen = false;

		new LwjglApplication(new CourseraGameProgramingPort(), config);
	}
}
