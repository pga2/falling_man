package com.ledzinygamedevelopment.fallingman.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;


public class HUD {
    private final BitmapFont font;
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

    public HUD(SpriteBatch sb) {
        gold = 0;
        distance = 0;
        previousDist = 0;
        wholeDistance = 0;

        viewport = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        font = new BitmapFont(Gdx.files.internal("test_font/FSM.fnt"), false);
        font.getData().setScale(0.01f);
        font.setUseIntegerPositions(false);
        goldLabel = new Label("GOLD", new Label.LabelStyle(font, Color.GOLD));
        distanceLabel = new Label("DIST", new Label.LabelStyle(font, Color.BLACK));
        goldIntLabel = new Label(String.valueOf(gold), new Label.LabelStyle(font, Color.GOLD));
        distanceIntLabel = new Label(String.valueOf(distance), new Label.LabelStyle(font, Color.BLACK));

        table.add(goldLabel).expandX().padTop(0.001f);
        table.add(distanceLabel).expandX().padTop(0.001f);
        table.row();
        table.add(goldIntLabel).expandX();
        table.add(distanceIntLabel).expandX();

        stage.addActor(table);

    }

    public void update(float dt, float playerYPos) {
        distance = (int) (FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM - playerYPos) > distance ? (int) (FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM - playerYPos) : distance;
        wholeDistance = distance + previousDist;
        distanceIntLabel.setText(wholeDistance);

        goldIntLabel.setText(gold);
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }
}
