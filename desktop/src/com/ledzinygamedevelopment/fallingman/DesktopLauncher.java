package com.ledzinygamedevelopment.fallingman;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Stick Man Falling");
		config.setWindowSizeLimits(720, 1280, 1000, 2000);
		//config.setForegroundFPS(60);
		new Lwjgl3Application(new FallingMan(null, null), config);
	}
}
