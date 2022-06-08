package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;

public interface GameScreen extends Screen {
    public TextureAtlas getAtlas();
    public Player getPlayer();
    public void setGameOver(boolean gameOver);
    public void setLoadMenu(boolean loadMenu);
    public void setStopRock(boolean stopRock);
    public GoldAndHighScoresBackground getGoldAndHighScoresBackground();
    public void setGameScreen(GameScreen gameScreen);
    public FallingMan getFallingMan();
    public void setCurrentScreen(byte currentScreen);
}
