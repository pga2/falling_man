package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

import de.golfgl.gdxgamesvcs.GameServiceException;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

public class ShowLeaderboardButton extends Button {

    public ShowLeaderboardButton(GameScreen gameScreen, World world, float posX, float posY) {
        super(gameScreen, world, posX, posY);

        width = 1024 / FallingMan.PPM;
        height = 256 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("show_leaderboard_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        this.posX = getX();
        this.posY = getY();
    }

    @Override
    public void touched() {
        super.touched();
        if (gameScreen.getGame().gsClient.getClass().getName() != NoGameServiceClient.class.getName()) {

            setRegion(gameScreen.getDefaultAtlas().findRegion("show_leaderboard_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            clicked = true;
        }
    }

    @Override
    public void notTouched() {
        super.notTouched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("show_leaderboard_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        if (clicked) {
            try {
                gameScreen.getGame().gsClient.showLeaderboards("CgkI-N6Fv6wJEAIQAg");
            } catch (GameServiceException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("show_leaderboard_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));

    }
}