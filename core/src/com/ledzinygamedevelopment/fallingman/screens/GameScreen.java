package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;

public interface GameScreen extends Screen {

    public TextureAtlas getAtlas();
    public Player getPlayer();
    public void setGameOver(boolean gameOver);
    public void setLoadNewGame(boolean loadNewGame);
}
