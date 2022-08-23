package com.ledzinygamedevelopment.fallingman.sprites.windows;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.MenuScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.DailyRewardButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;

public class RewardCalendarWindow extends Sprite {

    private float height;
    private float width;
    private World world;
    private long day;
    private MenuScreen menuScreen;
    private Array<DailyRewardButton> dailyRewardButtons;
    private boolean toRemove;
    private boolean toRemoveAnimationEnded;
    private float toRemoveAnimationTimer;

    public RewardCalendarWindow(MenuScreen menuScreen, World world, long day) {
        this.menuScreen = menuScreen;
        this.world = world;
        this.day = day;
        /*clicked = false;
        locked = false;*/

        width = 1312 / FallingMan.PPM;
        height = 2048 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(menuScreen.getWindowAtlas().findRegion("window"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition((FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM - width) / 2, menuScreen.getPlayer().b2body.getPosition().y - height / 2);

        dailyRewardButtons = new Array<>();
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 160 / FallingMan.PPM, 540 / FallingMan.PPM + 20 / FallingMan.PPM - 270 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, "1"));
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 540 / FallingMan.PPM, 540 / FallingMan.PPM + 20 / FallingMan.PPM - 270 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, "2"));
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 920 / FallingMan.PPM, 540 / FallingMan.PPM + 20 / FallingMan.PPM - 270 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, "3"));
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 160 / FallingMan.PPM, 0 - 270 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, "4"));
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 540 / FallingMan.PPM, 0 - 270 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, "5"));
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 920 / FallingMan.PPM, 0 - 270 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, "6"));
        dailyRewardButtons.add(new DailyRewardButton(menuScreen, 160 / FallingMan.PPM, -540 / FallingMan.PPM - 20 / FallingMan.PPM - 270 / FallingMan.PPM, 1120 / FallingMan.PPM, 540 / FallingMan.PPM, "7"));
        toRemove = false;
        toRemoveAnimationEnded = false;
        toRemoveAnimationTimer = 0;

        for (int i = 0; i < dailyRewardButtons.size; i++) {
            DailyRewardButton dailyRewardButton = dailyRewardButtons.get(i);
            if ((long) dailyRewardButton.getTypeOfReward() > day) {
                dailyRewardButton.setColor(dailyRewardButton.getColor().r, dailyRewardButton.getColor().g, dailyRewardButton.getColor().b, 0.5f);
            }
        }
    }

    public void update(float dt, float playerPosY) {
        setPosition(getX(), playerPosY - height / 2);
        for (DailyRewardButton button : dailyRewardButtons) {
            button.update(dt, playerPosY);
        }
        if (toRemove) {
            if (toRemoveAnimationTimer < 1) {
                setColor(getColor().r, getColor().g, getColor().b, 1 - toRemoveAnimationTimer);
                for (DailyRewardButton dailyRewardButton : dailyRewardButtons) {
                    if (1 - toRemoveAnimationTimer < dailyRewardButton.getColor().a) {
                        dailyRewardButton.setColor(dailyRewardButton.getColor().r, dailyRewardButton.getColor().g, dailyRewardButton.getColor().b, 1 - toRemoveAnimationTimer);
                    }
                }
                toRemoveAnimationTimer += dt;
            } else if (toRemoveAnimationTimer < 1.2) {
                setScale(1- (toRemoveAnimationTimer - 1) * 5);
                toRemoveAnimationTimer += dt;
                setColor(getColor().r, getColor().g, getColor().b, 0.005f);
                for (DailyRewardButton dailyRewardButton : dailyRewardButtons) {
                    dailyRewardButton.setColor(dailyRewardButton.getColor().r, dailyRewardButton.getColor().g, dailyRewardButton.getColor().b, 0.005f);
                }
            } else {
                toRemoveAnimationEnded = true;
                setColor(getColor().r, getColor().g, getColor().b, 0.005f);
                for (DailyRewardButton dailyRewardButton : dailyRewardButtons) {
                    //if (1 - toRemoveAnimationTimer < dailyRewardButton.getColor().a) {
                        dailyRewardButton.setColor(dailyRewardButton.getColor().r, dailyRewardButton.getColor().g, dailyRewardButton.getColor().b, 0.005f);
                    //}
                }
            }
        }
    }

    public void getReward() {
        if (!toRemove) {
            DailyRewardButton dailyRewardCurrentDayButton = dailyRewardButtons.first();
            for (DailyRewardButton dailyRewardButton : dailyRewardButtons) {
                if ((long) dailyRewardButton.getTypeOfReward() == day) {
                    dailyRewardCurrentDayButton = dailyRewardButton;
                }
            }

            if (day == 1L) {

                for (int i = 0; i < 100; i++) {
                    OnePartRoll tempRoll = new OnePartRoll(menuScreen, dailyRewardCurrentDayButton.getX() + dailyRewardCurrentDayButton.getWidth() / 2, dailyRewardCurrentDayButton.getY() + dailyRewardCurrentDayButton.getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                    tempRoll.setAmount(10);
                    tempRoll.startFlying();
                    menuScreen.getFlyingRolls().add(tempRoll);
                }
            } else if (day == 2L) {

                for (int i = 0; i < 10; i++) {
                    OnePartRoll tempRoll = new OnePartRoll(menuScreen,  dailyRewardCurrentDayButton.getX() + dailyRewardCurrentDayButton.getWidth() / 2, dailyRewardCurrentDayButton.getY() + dailyRewardCurrentDayButton.getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 0);
                    tempRoll.setAmount(1);
                    tempRoll.startFlying();
                    menuScreen.getFlyingRolls().add(tempRoll);
                }
            } else if (day == 3L) {

                for (int i = 0; i < 100; i++) {
                    OnePartRoll tempRoll = new OnePartRoll(menuScreen,  dailyRewardCurrentDayButton.getX() + dailyRewardCurrentDayButton.getWidth() / 2, dailyRewardCurrentDayButton.getY() + dailyRewardCurrentDayButton.getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                    tempRoll.setAmount(20);
                    tempRoll.startFlying();
                    menuScreen.getFlyingRolls().add(tempRoll);
                }
            } else if (day == 4L) {

                for (int i = 0; i < 20; i++) {
                    OnePartRoll tempRoll = new OnePartRoll(menuScreen,  dailyRewardCurrentDayButton.getX() + dailyRewardCurrentDayButton.getWidth() / 2, dailyRewardCurrentDayButton.getY() + dailyRewardCurrentDayButton.getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 0);
                    tempRoll.setAmount(2);
                    tempRoll.startFlying();
                    menuScreen.getFlyingRolls().add(tempRoll);
                }
            } else if (day == 5L) {

                for (int i = 0; i < 100; i++) {
                    OnePartRoll tempRoll = new OnePartRoll(menuScreen,  dailyRewardCurrentDayButton.getX() + dailyRewardCurrentDayButton.getWidth() / 2, dailyRewardCurrentDayButton.getY() + dailyRewardCurrentDayButton.getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                    tempRoll.setAmount(40);
                    tempRoll.startFlying();
                    menuScreen.getFlyingRolls().add(tempRoll);
                }
            } else if (day == 6L) {

                for (int i = 0; i < 100; i++) {
                    OnePartRoll tempRoll = new OnePartRoll(menuScreen,  dailyRewardCurrentDayButton.getX() + dailyRewardCurrentDayButton.getWidth() / 2, dailyRewardCurrentDayButton.getY() + dailyRewardCurrentDayButton.getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                    tempRoll.setAmount(80);
                    tempRoll.startFlying();
                    menuScreen.getFlyingRolls().add(tempRoll);
                }
            } else if (day >= 7L) {
                BigChest bigChest = new BigChest(menuScreen, FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM, menuScreen.getPlayer().b2body.getPosition().y);
                bigChest.setShouldChangePosAccordToPlayer(true);
                menuScreen.getBigChests().add(bigChest);
            }
            toRemove = true;
        }

    }

    public void draw(Batch batch) {
        super.draw(batch);
        for (DailyRewardButton button : dailyRewardButtons) {
            button.draw(batch);
        }
    }

    public boolean isToRemoveAnimationEnded() {
        return toRemoveAnimationEnded;
    }
}
