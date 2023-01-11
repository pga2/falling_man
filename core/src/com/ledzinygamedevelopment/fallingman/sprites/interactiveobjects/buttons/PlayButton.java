package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class PlayButton extends Button {


    private PlayScreen playScreen;
    private TextureRegion gamePauseBackground;
    private boolean startingGame;
    private float startingGameTimer;
    private BitmapFont font;

    public PlayButton(PlayScreen playScreen, World world, float posX, float posY) {
        super(playScreen, world, posX, posY);
        this.playScreen = playScreen;
        width = 144 / FallingMan.PPM;
        height = 144 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(playScreen.getDefaultAtlas().findRegion("play_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);

        gamePauseBackground = new TextureRegion(playScreen.getDefaultAtlas().findRegion("game_pause_background"), 0, 0, 1, 1);

        startingGame = false;
        startingGameTimer = 0;
    }

    @Override
    public void touched() {
        if (!startingGame) {
            super.touched();
            setRegion(playScreen.getDefaultAtlas().findRegion("play_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            clicked = true;
        }
    }

    @Override
    public void notTouched() {
        if (!startingGame) {
            super.notTouched();
            if (clicked && !playScreen.isChangeScreen()) {
                setRegion(playScreen.getDefaultAtlas().findRegion("play_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
                clicked = false;

                startingGame = true;
            }
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(playScreen.getDefaultAtlas().findRegion("play_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }

    @Override
    public void update(float dt, Vector2 pos) {
        super.update(dt, pos);
        if (startingGame) {
            if (startingGameTimer < 3) {
                startingGameTimer += dt;
            } else {
                playScreen.setPause(false);
                playScreen.setPausePlayButton(new PauseButton(playScreen, world, FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 256 / 2f / FallingMan.PPM, playScreen.getHud().getHudBackground().getY()));
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(gamePauseBackground, playScreen.getGameCam().position.x - FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM, playScreen.getGameCam().position.y - FallingMan.MAX_WORLD_HEIGHT / 2f / FallingMan.PPM, FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM);

        if (startingGame) {
            font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.setUseIntegerPositions(false);
            font.setColor(238 / 256f, 188 / 256f, 29 / 256f, 1);
            font.getData().setScale(0.03f);
            GlyphLayout glyphLayout = new GlyphLayout(font, String.valueOf((int) ((3 - startingGameTimer) + 0.999f)));
            if ((int) ((3 - startingGameTimer) + 0.999f) > 0)
                font.draw(batch, String.valueOf((int) ((3 - startingGameTimer) + 0.999f)), FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - glyphLayout.width / 2, playScreen.getGameCam().position.y + glyphLayout.height * 1.3f);
        }

        super.draw(batch);
    }
}
