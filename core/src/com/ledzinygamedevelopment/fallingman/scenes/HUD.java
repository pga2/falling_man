package com.ledzinygamedevelopment.fallingman.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;


public class HUD {
    private final BitmapFont hudFont;
    private Stage stage;
    private Viewport viewport;

    private Integer gold;
    private Integer distance;
    private Integer previousDist;
    private Integer wholeDistance;

    private Label goldLabel;
    private Label distanceLabel;
    private Label goldIntLabel;
    private Label distanceIntLabel;
    private SpriteBatch sb;
    private PlayScreen playScreen;

    private float gameOverTimer;
    private Label goldLabelGameOver;
    private Label distanceLabelGameOver;
    private Label goldIntLabelGameOver;
    private Label distanceIntLabelGameOver;
    private boolean gameOverStage;
    private boolean spawnReturnButton;
    private Table table;
    private boolean stopUpdatingGOHud;

    public HUD(SpriteBatch sb, PlayScreen playScreen) {
        this.sb = sb;
        this.playScreen = playScreen;
        gold = 0;
        distance = 0;
        previousDist = 0;
        wholeDistance = 0;
        gameOverTimer = 0;
        gameOverStage = false;
        spawnReturnButton = false;
        stopUpdatingGOHud = false;

        viewport = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, new OrthographicCamera());

        stage = new Stage(viewport, sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        hudFont = new BitmapFont(Gdx.files.internal("test_font/FSM.fnt"), false);
        hudFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hudFont.setUseIntegerPositions(false);
        hudFont.getData().setScale(0.01f);
        goldLabel = new Label("GOLD", new Label.LabelStyle(hudFont, new Color(174/255f, 132/255f, 26/255f, 1)));
        distanceLabel = new Label("DIST", new Label.LabelStyle(hudFont, Color.BLACK));
        goldIntLabel = new Label(String.valueOf(gold), new Label.LabelStyle(hudFont, new Color(174/255f, 132/255f, 26/255f, 1)));
        distanceIntLabel = new Label(String.valueOf(distance), new Label.LabelStyle(hudFont, Color.BLACK));

        table.add(goldLabel).expandX().padTop(-1f);
        table.add(distanceLabel).expandX().padTop(-1f);
        table.row();
        table.add(goldIntLabel).expandX().padTop(-1);
        table.add(distanceIntLabel).expandX().padTop(-1);

        stage.addActor(table);

    }

    public void update(float dt, float playerYPos) {
        if(!gameOverStage) {
            distance = (int) (FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM - playerYPos) > distance ? (int) (FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM - playerYPos) : distance;
            wholeDistance = distance + previousDist;
            distanceIntLabel.setText(wholeDistance);

            goldIntLabel.setText(gold);
        }/* else {
            if(gameOverTimer < 2) {
                goldIntLabelGameOver.setText((int) (gold * Math.sqrt(Math.sqrt(gameOverTimer / 2))));
                goldIntLabelGameOver.setPosition(table.getWidth() / 2f - goldIntLabelGameOver.getWidth() / 2, goldIntLabelGameOver.getY());

                distanceIntLabelGameOver.setText((int) (wholeDistance * Math.sqrt(Math.sqrt(gameOverTimer / 2))));
                distanceIntLabelGameOver.setPosition(table.getWidth() / 2f - distanceIntLabelGameOver.getWidth() / 2, distanceIntLabelGameOver.getY());

                gameOverTimer += dt;
                spawnReturnButton = true;
            } else {
                if(!stopUpdatingGOHud) {
                    if (spawnReturnButton) {
                        //playScreen.getButtons().add(new PlayAgainButton(playScreen, playScreen.getWorld(), 224 / FallingMan.PPM, playScreen.getPlayer().b2body.getPosition().y - 850 / FallingMan.PPM, 992 / FallingMan.PPM, 480 / FallingMan.PPM));
                        spawnReturnButton = false;
                    }
                    goldIntLabelGameOver.setText(gold);
                    distanceIntLabelGameOver.setText(wholeDistance);
                    stopUpdatingGOHud = true;
                }
                distanceIntLabelGameOver.setPosition(table.getWidth() / 2f - distanceIntLabelGameOver.getWidth() / 2, distanceIntLabelGameOver.getY());
                goldIntLabelGameOver.setPosition(table.getWidth() / 2f - goldIntLabelGameOver.getWidth() / 2, goldIntLabelGameOver.getY());
            }
        }*/
        //Gdx.app.log("HUD height", String.valueOf());

    }

    public void newStageGameOver() {
        gameOverStage = true;
        stage = new Stage(viewport, sb);

        /*goldLabelGameOver = new Label("GOLD", new Label.LabelStyle(hudFont, Color.GOLD));
        goldLabelGameOver.setAlignment(Align.center);
        distanceLabelGameOver = new Label("DISTANCE", new Label.LabelStyle(hudFont, Color.BLACK));
        distanceLabelGameOver.setAlignment(Align.center);
        goldIntLabelGameOver = new Label(String.valueOf(0), new Label.LabelStyle(hudFont, Color.GOLD));
        goldIntLabelGameOver.setAlignment(Align.center);
        distanceIntLabelGameOver = new Label(String.valueOf(0), new Label.LabelStyle(hudFont, Color.BLACK));
        distanceIntLabelGameOver.setAlignment(Align.center);

        table = new Table();
        table.pack();
        table.add(goldLabelGameOver).expandX().padTop(0.0001f).row();
        table.add(goldIntLabelGameOver).expandX().padTop(-1).row();
        table.add(distanceLabelGameOver).expandX().padTop(-1).row();
        table.add(distanceIntLabelGameOver).padTop(-1).row();
        table.pack();
        //table.debugAll();
        table.setPosition(FallingMan.MIN_WORLD_WIDTH / 2f / FallingMan.PPM - table.getWidth() / 2,
                (viewport.getWorldHeight() / 2f - table.getHeight() / 2) + 500 / FallingMan.PPM);
        stage.addActor(table);*/
    }

    public Stage getStage() {
        return stage;
    }

    public Integer getPreviousDist() {
        return previousDist;
    }

    public void setPreviousDist(Integer previousDist) {
        this.previousDist = previousDist;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getWholeDistance() {
        return wholeDistance;
    }

    public boolean isGameOverStage() {
        return gameOverStage;
    }
}
