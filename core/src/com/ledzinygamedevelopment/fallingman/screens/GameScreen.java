package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;

import java.util.HashMap;

public interface GameScreen extends Screen {

    public TextureAtlas getDefaultAtlas();
    public TextureAtlas getBigRockAtlas();
    public Player getPlayer();
    public void setGameOver(boolean gameOver);
    public void setLoadMenu(boolean loadMenu);
    public void setStopRock(boolean stopRock);
    public GoldAndHighScoresBackground getGoldAndHighScoresBackground();
    public void setGameScreen(GameScreen gameScreen);
    public FallingMan getFallingMan();
    public void setCurrentScreen(byte currentScreen);
    public void addCoinsFromChest(int numberOfCoins);
    public void removeChest(BigChest bigChest);
    public SpinButton getSpinButton();
    public GameAssetManager getAssetManager();
    public TextureAtlas getPlayerAtlas();
    public GoldAndHighScoresIcons getGoldAndHighScoresIcons();
    public HashMap<String, Boolean> getOwnedBodySprites();
}
