package com.ledzinygamedevelopment.fallingman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class FallingMan extends Game {
	public SpriteBatch batch;
	public static final int MIN_WORLD_WIDTH = 1440;
	public static final int MIN_WORLD_HEIGHT = 2560;
	public static final int MAX_WORLD_WIDTH = 1440;
	public static final int MAX_WORLD_HEIGHT = 3360;
	public static final float PPM = 100;
	public static final int CELL_SIZE = 32;
	public static final int PLAYER_STARTING_X_POINT = 720;
	public static final int PLAYER_STARTING_Y_POINT = 8640;

	public static final short DEFAULT_BIT = 1;
	public static final short INTERACTIVE_TILE_OBJECT_BIT = 2;
	public static final short DEAD_MACHINE_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short PLAYER_HEAD_BIT = 16;
	public static final short PLAYER_BELLY_BIT = 32;
	public static final short PLAYER_ARM_BIT = 64;
	public static final short PLAYER_THIGH_BIT = 128;
	public static final short PLAYER_FORE_ARM_BIT = 256;
	public static final short PLAYER_SHIN_BIT = 512;
	public static final short PLAYER_HAND_BIT = 1024;
	public static final short PLAYER_FOOT_BIT = 2048;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
