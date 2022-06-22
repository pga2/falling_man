package com.ledzinygamedevelopment.fallingman.screens.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    private long gold;
    private int highScore;
    private float width;
    private float height;
    private float goldTextScale;

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
        //setPosition((FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM - width) / 2, gameScreen.getPlayer().b2body.getPosition().y - height / 2);
        goldTextScale = 1;

    }

    public void update(float dt, Vector2 playerPos, float screenHeight) {
        setPosition(106 / FallingMan.PPM, playerPos.y + screenHeight / 2 - 260 / FallingMan.PPM);
        GlyphLayout glyphLayout = new GlyphLayout(font, String.valueOf(gold));
        if (glyphLayout.width + 150 / FallingMan.PPM > gameScreen.getGoldAndHighScoresBackground().getScaleX() * (640 / FallingMan.PPM)) {
            gameScreen.getGoldAndHighScoresBackground().setScale((glyphLayout.width * FallingMan.PPM + 150) / 640, 1);
        }
        glyphLayout = new GlyphLayout(font, String.valueOf(highScore));
        if (glyphLayout.width + 150 / FallingMan.PPM > gameScreen.getGoldAndHighScoresBackground().getScaleX() * (640 / FallingMan.PPM)) {
            gameScreen.getGoldAndHighScoresBackground().setScale((glyphLayout.width * FallingMan.PPM + 150) / 640, 1);
        }
        //gameScreen.getGoldAndHighScoresBackground().setScale(2, 1);
    }

    public void draw (Batch batch) {
        super.draw(batch);
        font.setColor(238/256f, 188/256f, 29/256f, 1);
        GlyphLayout glyphLayoutBeforeScaling = new GlyphLayout(font, String.valueOf(gold));
        float beforeWidth = glyphLayoutBeforeScaling.width;
        float beforeHeight = glyphLayoutBeforeScaling.height;
        font.getData().setScale(goldTextScale * 0.007f);
        GlyphLayout glyphLayoutAfterScaling = new GlyphLayout(font, String.valueOf(gold));
        font.draw(batch, String.valueOf(gold), getX() + 150 / FallingMan.PPM - (glyphLayoutAfterScaling.width / 2 - beforeWidth / 2), getY() + 218 / FallingMan.PPM + (glyphLayoutAfterScaling.height / 2 - beforeHeight / 2));
        if (goldTextScale -0.05f > 1) {
            goldTextScale -= 0.05f;
        } else {
            goldTextScale = 1;
        }
        font.getData().setScale(0.007f);
        font.setColor(Color.BLACK);
        font.draw(batch, String.valueOf(highScore), getX() + 150 / FallingMan.PPM, getY() + 106 / FallingMan.PPM);
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public void setGoldTextScale(float goldTextScale) {
        this.goldTextScale = goldTextScale;
    }
}
