package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.scenes.HUD;
import com.ledzinygamedevelopment.fallingman.sprites.Smoke;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ad.WatchAdButton;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.HashMap;

public interface GameScreen extends Screen {

    public TextureAtlas getDefaultAtlas();
    //public TextureAtlas getBigRockAtlas();
    public Player getPlayer();
    public void setGameOver(boolean gameOver);
    public void setLoadMenu(boolean loadMenu);
    public void setStopRock(boolean stopRock);
    public GoldAndHighScoresBackground getGoldAndHighScoresBackground();
    public void setGameScreen(GameScreen gameScreen);
    public FallingMan getFallingMan();
    public void setCurrentScreen(byte currentScreen);
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll);
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll, Vector2 pos, String transactionName);
    public void addOnePartRolls(int typeOfRoll, Vector2 pos, String transactionName);
    public void removeChest(BigChest bigChest);
    public SpinButton getSpinButton();
    public GameAssetManager getAssetManager();
    public TextureAtlas getPlayerAtlas();
    public GoldAndHighScoresIcons getGoldAndHighScoresIcons();
    public HashMap<String, Boolean> getOwnedBodySprites();
    public AdsController getAdsController();
    public void watchAdButtonClicked();
    public void watchAdButtonClicked(WatchAdButton watchAdButton);
    public Array<Button> getButtons();
    public void setNewLife(boolean newLife);
    public FallingMan getGame();
    public SaveData getSaveData();
    public void setSmokeToAddPos(Vector2 smokeToAddPos);
    public Array<Smoke> getSmokes();
    public boolean isReadyToCreateSmoke();
    public void setAddSmoke(boolean addSmoke);
    public TextureAtlas getWindowAtlas();
    public void setGoldX2(boolean goldX2);

}
