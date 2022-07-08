package com.ledzinygamedevelopment.fallingman.sprites.windows;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class PriceBackground extends Sprite {
    private GameScreen gameScreen;
    private World world;
    private float width;
    private float height;
    private long price;
    private BitmapFont font;

    public PriceBackground(GameScreen gameScreen, World world) {
        this.gameScreen = gameScreen;
        this.world = world;

        width = 500 / FallingMan.PPM;
        height = 132 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("price_background"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setOrigin(width / 2, height / 2);
        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());
    }

    public void update(float dt, Vector2 pos) {
        setPosition(pos.x - width / 2, pos.y + 120 / FallingMan.PPM);
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public void draw(Batch batch) {
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.0067f);
        font.setColor(238/256f, 188/256f, 29/256f, 1);

        String priceToDraw;

        if (price > 10000000000000L) {
            priceToDraw = String.valueOf(price / 1000000000000L) + "T";
        } else if (price >= 10000000000L) {
            priceToDraw = String.valueOf(price / 1000000000) + "B";

        } else if (price >= 10000000L) {
            priceToDraw = String.valueOf(price / 1000000) + "M";

        } else if (price >= 10000L) {
            priceToDraw = String.valueOf(price / 1000) + "K";

        } else {
            priceToDraw = String.valueOf(price);
        }
        GlyphLayout glyphLayout = new GlyphLayout(font, priceToDraw);
        setScale((glyphLayout.width + 40 / FallingMan.PPM) / getWidth(), getScaleY());

        super.draw(batch);

        font.draw(batch, priceToDraw, getX() + width / 2 - glyphLayout.width / 2, getY() + glyphLayout.height + 17 / FallingMan.PPM);
    }
}
