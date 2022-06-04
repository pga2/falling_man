package com.ledzinygamedevelopment.fallingman.screens.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class GoldAndHighScoresIcons extends Sprite {

    private BitmapFont font;
    private GameScreen gameScreen;
    private World world;
    private int gold;
    private int highScore;
    private float width;
    private float height;

    public GoldAndHighScoresIcons(GameScreen gameScreen, World world, int gold, int highScore) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.gold = gold;
        this.highScore = highScore;

        font = new BitmapFont(Gdx.files.internal("test_font/FSM.fnt"), false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.007f);

        width = 112 / FallingMan.PPM;
        height = 224 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getAtlas().findRegion("gold_and_high_score_icons"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition((FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM - width) / 2, gameScreen.getPlayer().b2body.getPosition().y - height / 2);

    }

    public void update(float dt, Vector2 playerPos) {
        setPosition(106 / FallingMan.PPM, playerPos.y + 1046 / FallingMan.PPM);
    }

    public void draw (Batch batch) {
        super.draw(batch);
        font.setColor(238/256f, 188/256f, 29/256f, 1);
        font.draw(batch, String.valueOf(gold), getX() + 150 / FallingMan.PPM, getY() + 218 / FallingMan.PPM);
        font.setColor(Color.BLACK);
        font.draw(batch, String.valueOf(highScore), getX() + 150 / FallingMan.PPM, getY() + 106 / FallingMan.PPM);
    }
}
