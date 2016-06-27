package com.j5backup.loseyourmarblesclone.desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.j5backup.loseyourmarblesclone.LoseYourMarblesCloneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Lose Your Marbles: A 2014 Remake";
		
		// The game window will be based on the resolution of the monitor
		// Resizing the game window will not currently be supported
		final float percentageOfScreenHeightForGameWindow = 0.9f;
		final float percentageOfGameWindowHeightForGameWindowWidth = 0.608108108f; // Ratio of width to height : 720 / 1184 (on the android emulator
																				   // the screen size is not actually 1280)
		
		DisplayMode screen = cfg.getDesktopDisplayMode();
		cfg.height = (int) Math.floor(screen.height*percentageOfScreenHeightForGameWindow);
		cfg.width = (int) Math.floor(cfg.height*percentageOfGameWindowHeightForGameWindowWidth);
		//cfg.useGL20 = true;
		
		new LwjglApplication(new LoseYourMarblesCloneGame(cfg.width, cfg.height), cfg);
	}
}
