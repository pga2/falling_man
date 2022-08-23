package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class DailyRewardButton extends Sprite{
    Sprite buttonImage;
    private float posX;
    private float posY;
    private byte typeOfReward;
    private BitmapFont font;
    private String day;
    private String amount;
    private float fontA;

    public DailyRewardButton(GameScreen gameScreen, float posX, float posY, float width, float height, String typeOfButton) {
        this.posX = posX;
        this.posY = posY;
        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());

        setBounds(0, 0, width, height);
        if (typeOfButton.equals("7")) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("daily_reward_wide"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        } else {
            setRegion(gameScreen.getDefaultAtlas().findRegion("daily_reward"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        }
        setPosition(posX, gameScreen.getPlayer().b2body.getPosition().y + posY);

        buttonImage = new Sprite();

        switch (typeOfButton) {
            case "1":
                typeOfReward = 1; //coin
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 1), 0, 0, 192, 192);
                amount = "1000 coins";
                break;
            case "2":
                typeOfReward = 2; //spin
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 0), 0, 0, 192, 192);
                amount = "10 rolls";
                break;
            case "3":
                typeOfReward = 3; //coin
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 1), 0, 0, 192, 192);
                amount = "2000 coins";
                break;
            case "4":
                typeOfReward = 4; //spin
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 0), 0, 0, 192, 192);
                amount = "20 rolls";
                break;
            case "5":
                typeOfReward = 5; //gold chest
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 2), 0, 0, 192, 192);
                amount = "4000 coins";
                break;
            case "6":
                typeOfReward = 6; //gold chest
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 2), 0, 0, 192, 192);
                amount = "8000 coins";
                break;
            case "7":
                typeOfReward = 7; //chest
                buttonImage.setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + 3), 0, 0, 192, 192);
                amount = "MYSTERY CHEST";
                break;
            default:
                Gdx.app.log("", "");
                break;
        }
        fontA = 1;
        day = "day " + typeOfButton;
        buttonImage.setBounds(0, 0, 192 / FallingMan.PPM, 192 / FallingMan.PPM);
        buttonImage.setPosition(posX, posY);
        buttonImage.setOrigin(buttonImage.getWidth() / 2, buttonImage.getHeight() / 2);
    }

    public void update(float dt, float playerPosY) {
        setPosition(posX, playerPosY + posY);
        buttonImage.setPosition(getX() + getWidth() / 2 - buttonImage.getWidth() / 2, getY() + getHeight() / 2 - buttonImage.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(10 / 256f, 8 / 256f, 0 / 256f, fontA);
        font.getData().setScale(0.0043f);
        GlyphLayout glyphLayout = new GlyphLayout(font, day);
        font.draw(batch, day, getX(), getY() + glyphLayout.height + 468 / FallingMan.PPM, getWidth(), Align.center, false);

        if (typeOfReward == 7) {
            font.getData().setScale(0.005f);
        } else {
            font.getData().setScale(0.003f);
        }
        GlyphLayout glyphLayoutAmount = new GlyphLayout(font, amount);
        font.draw(batch, amount, getX(), getY() + glyphLayout.height + 60 / FallingMan.PPM, getWidth(), Align.center, false);

        buttonImage.draw(batch);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        super.setColor(r, g, b, a);
        if (buttonImage != null) {
            buttonImage.setColor(r, g, b, a);
            fontA = a;
        }
    }

    public byte getTypeOfReward() {
        return typeOfReward;
    }
}
