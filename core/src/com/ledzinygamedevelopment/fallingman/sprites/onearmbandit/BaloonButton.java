package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.OneArmedBanditScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;

import java.util.Random;

public class BaloonButton extends Button {
    private BitmapFont font;
    private Animation animation;
    private OneArmedBanditScreen oneArmedBanditScreen;
    private boolean destroyBaloon;
    private float destroyBaloonTimer;
    private float animationTimer;
    private float speedX;
    private boolean addGoldFont;
    private float goldFontPosY;
    private float goldFontDrawTimer;
    private int goldToDraw;

    public BaloonButton(OneArmedBanditScreen gameScreen, World world, float posX, float posY, float width, float height) {
        super(gameScreen, world, posX, posY, width, height);
        oneArmedBanditScreen = gameScreen;
        setBounds(0, 0, width, height);
        setRegion(oneArmedBanditScreen.getBaloonAtlas().findRegion("baloon"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        if (new Random().nextBoolean())
            flip(true, false);
        destroyBaloon = false;
        destroyBaloonTimer = 0;
        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());

        Array<TextureRegion> textureRegions = new Array<>();
        for (int i = 1; i < 8; i++) {
            textureRegions.add(new TextureRegion(oneArmedBanditScreen.getBaloonAtlas().findRegion("baloon" + i), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM)));
        }

        animation = new Animation(0.0166f, textureRegions);
        animationTimer = 0;
        if (posX < 0)
            speedX = (new Random().nextFloat() + 0.5f) / 30f;
        else
            speedX = -(new Random().nextFloat() + 0.5f) / 30f;

        int color = new Random().nextInt(3);
        if (color == 0) {
            setColor(Color.BLUE);
        } else if (color == 1) {
            setColor(Color.RED);
        } else if (color == 2) {
            setColor(Color.GREEN);
        }
        addGoldFont = false;
        goldFontPosY = getY() + getHeight() / 1.5f;
        goldFontDrawTimer = 0;
        //setPosition(500 / FallingMan.PPM, 500 / FallingMan.PPM);
        goldToDraw = 0;
    }

    public void update(float dt) {
        posX += speedX * 60 * Gdx.graphics.getDeltaTime();
        setPosition(posX, posY);
        if (destroyBaloon)
            setRegion(getFrame(dt));
        if (animation.isAnimationFinished(animationTimer) && !addGoldFont) {
            speedX = 0;
            addGoldFont = true;
            if(random.nextInt(20) == 0) {
                oneArmedBanditScreen.getSaveData().addGold(1);
                oneArmedBanditScreen.getGoldAndHighScoresIcons().setGold(oneArmedBanditScreen.getSaveData().getGold());
                goldToDraw = 1;
            } else {
                goldToDraw = 0;
            }
        }
        if (addGoldFont) {
            goldFontDrawTimer += dt;
        }
        if (posX > FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM && speedX > 0) {
            toRemove = true;
        } else if (posX < 0 - width && speedX < 0) {
            toRemove = true;
        }
    }

    @Override
    public void touched() {
        if (!destroyBaloon) {
            super.touched();
            destroyBaloon = true;
            clicked = true;
            if (gameScreen.getSaveData().getSounds()) {
                gameScreen.getAssetManager().getDestroyedBalloonSound().play();
            }
        }
    }

    @Override
    public void notTouched() {
    }

    @Override
    public void restoreNotClickedTexture() {

    }

    @Override
    public void draw(Batch batch) {
        if (addGoldFont && goldFontDrawTimer < 1) {
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.setUseIntegerPositions(false);
            font.setColor(100 / 256f, 80 / 256f, 0 / 256f, 1);
            font.setColor(Color.GOLD);
            font.getData().setScale(0.005f);
            GlyphLayout glyphLayout = new GlyphLayout(font, "1");
            goldFontPosY += 0.08;
            if (goldFontDrawTimer > 0.5) {
                font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, (1 - goldFontDrawTimer) * 2);
            }
            font.draw(batch, String.valueOf(goldToDraw), getX() + getWidth() / 2 - glyphLayout.width / 2, goldFontPosY);
        } else if (addGoldFont){
            toRemove = true;
        } else {
            super.draw(batch);
        }
    }

    private TextureRegion getFrame(float dt) {
        animationTimer += dt;
        return (TextureRegion) animation.getKeyFrame(animationTimer, false);
    }
}
