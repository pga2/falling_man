package com.ledzinygamedevelopment.fallingman.sprites.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.scenes.HUD;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.WatchAdButton;
import com.ledzinygamedevelopment.fallingman.tools.loadinganimation.LoadingAnimation;

public class DefaultWindow extends Sprite {

    private PlayScreen playScreen;
    private World world;
    private BitmapFont font;
    private byte typeOfWindowText;
    private float width;
    private float height;
    private float timer;
    private boolean drawText;
    private long gold;
    private long wholeDistance;
    private boolean tapGrow;
    private float scale;
    private boolean tapExist;
    private LoadingAnimation adLoadingAnimation;
    private boolean isRewardedVideoAdLoaded;
    private boolean watchAdToGetSecondLifeReady;
    /*private boolean clicked;
    private boolean locked;*/

    public DefaultWindow(PlayScreen playScreen, World world, byte typeOfWindowText) {
        this.playScreen = playScreen;
        this.world = world;
        this.typeOfWindowText = typeOfWindowText;
        /*clicked = false;
        locked = false;*/

        width = 1312 / FallingMan.PPM;
        height = 2048 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(playScreen.getWindowAtlas().findRegion("window"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition((FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM - width) / 2, playScreen.getPlayer().b2body.getPosition().y - height / 2);

        font = playScreen.getAssetManager().getManager().get(playScreen.getAssetManager().getFont());
        //font = new BitmapFont(Gdx.files.internal("test_font/FSM.fnt"), false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(Color.GOLD);
        font.getData().setScale(0.006f);
        scale = 0.01f;
        timer = 0;
        drawText = false;
        tapGrow = true;
        tapExist = false;
        adLoadingAnimation = new LoadingAnimation(playScreen, getX() + getWidth() / 2, getY() + getHeight() / 4.3f);
        isRewardedVideoAdLoaded = false;
        watchAdToGetSecondLifeReady = false;
    }

    public void update(float dt, HUD hud, Vector2 playerPos){
        setPosition(getX(), playerPos.y - getHeight() / 2);
        if (typeOfWindowText == FallingMan.GAME_OVER_WINDOW) {
            int loadingTapTime = 5;
            if (timer < loadingTapTime) {
                gold = (int) (hud.getGold() * Math.sqrt(Math.sqrt(timer / loadingTapTime)));
                wholeDistance = (int) (hud.getWholeDistance() * Math.sqrt(Math.sqrt(timer / loadingTapTime)));
            } else {
                tapExist = true;
                gold = hud.getGold();
                wholeDistance = hud.getWholeDistance();
            }
        }
        if (!playScreen.isNewLife() && playScreen.getAdsController() != null && !playScreen.getAdsController().showRewardedVideo(true)) {
            adLoadingAnimation.update(dt);
        } else {
            if (!isRewardedVideoAdLoaded) {
                playScreen.getButtons().add(new WatchAdButton(playScreen, world, getX() + getWidth() / 2, getY() + getHeight() / 4.3f));
            }
            watchAdToGetSecondLifeReady = true;
            isRewardedVideoAdLoaded = true;
        }
        timer += dt;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (!isRewardedVideoAdLoaded) {
            adLoadingAnimation.draw(batch);
        }
        font.getData().setScale(0.006f);
        font.setColor(Color.GOLD);
        font.draw(batch, "Gold:", getX() + 120 / FallingMan.PPM, getY() + 1900 / FallingMan.PPM);
        GlyphLayout glyphLayoutGold = new GlyphLayout(font, String.valueOf(gold));
        font.draw(batch, String.valueOf(gold), getX() + 1200 / FallingMan.PPM - glyphLayoutGold.width, getY() + 1900 / FallingMan.PPM, glyphLayoutGold.width, Align.left, false);

        font.setColor(Color.BLACK);
        font.draw(batch, "Distance:", getX() + 120 / FallingMan.PPM, getY() - glyphLayoutGold.height * 1.2f + 1900 / FallingMan.PPM);
        GlyphLayout glyphLayoutDist = new GlyphLayout(font, String.valueOf(wholeDistance));
        font.draw(batch, String.valueOf(wholeDistance), getX() + 1200 / FallingMan.PPM - glyphLayoutDist.width, getY() - glyphLayoutGold.height * 1.2f + 1900 / FallingMan.PPM, glyphLayoutDist.width, Align.left, false);

        if (tapExist) {
            if (tapGrow) {
                if (scale < 0.013f) {
                    scale += 0.0005;
                } else {
                    tapGrow = false;
                }
            } else {
                if (scale > 0.008) {
                    scale -= 0.0005;
                } else {
                    tapGrow = true;
                }
            }
            font.getData().setScale(scale);
            font.setColor(Color.CHARTREUSE);
            GlyphLayout glyphLayoutTap = new GlyphLayout(font, "TAP!");
            font.draw(batch, "TAP!", getX() + 656 / FallingMan.PPM, getY() + 200 / FallingMan.PPM + glyphLayoutTap.height / 2, 0, Align.center, false);
        } else {
            scale = 0.01f;
            font.getData().setScale(scale);
            font.setColor(Color.CHARTREUSE);
            GlyphLayout glyphLayoutTap = new GlyphLayout(font, String.valueOf(Math.round(5-timer)));
            font.draw(batch, String.valueOf(Math.round(5-timer)), getX() + 656 / FallingMan.PPM, getY() + 200 / FallingMan.PPM + glyphLayoutTap.height / 2, 0, Align.center, false);
        }
    }

    public byte getTypeOfWindowText() {
        return typeOfWindowText;
    }

    public boolean isTapExist() {
        return tapExist;
    }

    public boolean isWatchAdToGetSecondLifeReady() {
        return watchAdToGetSecondLifeReady;
    }
}
