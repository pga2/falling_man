package com.ledzinygamedevelopment.fallingman.screens.windows;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class GoldAndHighScoresBackground extends Sprite {

    private GameScreen gameScreen;
    private World world;
    private float width;
    private float height;

    public GoldAndHighScoresBackground(GameScreen gameScreen, World world) {
        this.gameScreen = gameScreen;
        this.world = world;

        width = 640 / FallingMan.PPM;
        height = 224 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getAtlas().findRegion("gold_and_high_score_background"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition((FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM - width) / 2, gameScreen.getPlayer().b2body.getPosition().y - height / 2);

    }

    public void update(float dt, Vector2 playerPos) {
        setPosition(106 / FallingMan.PPM, playerPos.y + 1046 / FallingMan.PPM);
    }
}
