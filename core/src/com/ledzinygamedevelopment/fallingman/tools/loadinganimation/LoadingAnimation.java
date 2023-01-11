package com.ledzinygamedevelopment.fallingman.tools.loadinganimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class LoadingAnimation {

    private GameScreen gameScreen;
    private float posX;
    private float posY;
    private LoadingAnimationPart loadingAnimationPartLT;
    private LoadingAnimationPart loadingAnimationPartRT;
    private LoadingAnimationPart loadingAnimationPartLB;
    private LoadingAnimationPart loadingAnimationPartRB;
    private Array<LoadingAnimationPart> loadingAnimationParts;
    private enum ColorChange {LT, RT, LB, RB};
    private ColorChange colorChangeFirstPart;
    private ColorChange colorChangeSecondPart;
    private boolean colorUpFirstPart;
    private boolean colorUpSecondPart;
    private float colorChangeSpeed;

    public LoadingAnimation(GameScreen gameScreen, float posX, float posY) {

        this.gameScreen = gameScreen;
        this.posX = posX;
        this.posY = posY;

        loadingAnimationPartLT = new LoadingAnimationPart(gameScreen, "LT", posX - 48 / FallingMan.PPM, posY + 48 / FallingMan.PPM);
        loadingAnimationPartRT = new LoadingAnimationPart(gameScreen, "RT", posX + 48 / FallingMan.PPM, posY + 48 / FallingMan.PPM);
        loadingAnimationPartLB = new LoadingAnimationPart(gameScreen, "LB", posX - 48 / FallingMan.PPM, posY - 48 / FallingMan.PPM);
        loadingAnimationPartRB = new LoadingAnimationPart(gameScreen, "RB", posX + 48 / FallingMan.PPM, posY - 48 / FallingMan.PPM);
        loadingAnimationPartLB.setrColor(0);
        loadingAnimationPartLT.setrColor(0);
        loadingAnimationPartLB.updateColor();
        loadingAnimationPartLT.updateColor();
        loadingAnimationParts = new Array<>();
        loadingAnimationParts.add(loadingAnimationPartLT);
        loadingAnimationParts.add(loadingAnimationPartRT);
        loadingAnimationParts.add(loadingAnimationPartRB);
        loadingAnimationParts.add(loadingAnimationPartLB);
        colorChangeFirstPart = ColorChange.LT;
        colorChangeSecondPart = ColorChange.RB;
        colorUpFirstPart = true;
        colorUpSecondPart = true;
        colorChangeSpeed = 12f;
    }

    public void update(float dt) {
        colorChangeSpeed = 12 * 60 * dt;
        switch (colorChangeFirstPart) {
            case LT:
                if (loadingAnimationParts.get(0).getrColor() <= colorChangeSpeed / 255f ) {
                    colorChangeFirstPart = ColorChange.RT;
                } else {
                    loadingAnimationParts.get(2).setrColor(loadingAnimationParts.get(2).getrColor() + colorChangeSpeed / 255f);
                    loadingAnimationParts.get(0).setrColor(loadingAnimationParts.get(0).getrColor() - colorChangeSpeed / 255f);
                    loadingAnimationParts.get(2).updateColor();
                    loadingAnimationParts.get(0).updateColor();
                }
                break;
            case RT:

                if (loadingAnimationParts.get(1).getrColor() <= colorChangeSpeed / 255f ) {
                    colorChangeFirstPart = ColorChange.RB;
                } else {
                    loadingAnimationParts.get(3).setrColor(loadingAnimationParts.get(3).getrColor() + colorChangeSpeed / 255f);
                    loadingAnimationParts.get(1).setrColor(loadingAnimationParts.get(1).getrColor() - colorChangeSpeed / 255f);
                    loadingAnimationParts.get(3).updateColor();
                    loadingAnimationParts.get(1).updateColor();
                }
                break;
            case RB:

                if (loadingAnimationParts.get(2).getrColor() <= colorChangeSpeed / 255f ) {
                    colorChangeFirstPart = ColorChange.LB;
                } else {
                    loadingAnimationParts.get(0).setrColor(loadingAnimationParts.get(0).getrColor() + colorChangeSpeed / 255f);
                    loadingAnimationParts.get(2).setrColor(loadingAnimationParts.get(2).getrColor() - colorChangeSpeed / 255f);
                    loadingAnimationParts.get(0).updateColor();
                    loadingAnimationParts.get(2).updateColor();
                }
                break;
            case LB:

                if (loadingAnimationParts.get(3).getrColor() <= colorChangeSpeed / 255f ) {
                    colorChangeFirstPart = ColorChange.LT;
                } else {
                    loadingAnimationParts.get(1).setrColor(loadingAnimationParts.get(1).getrColor() + colorChangeSpeed / 255f);
                    loadingAnimationParts.get(3).setrColor(loadingAnimationParts.get(3).getrColor() - colorChangeSpeed / 255f);
                    loadingAnimationParts.get(1).updateColor();
                    loadingAnimationParts.get(3).updateColor();
                }
                break;
        }

        /*if (colorUpFirstPart) {
            switch (colorChangeFirstPart) {
                case LT:
                    if (loadingAnimationPartLT.getrColor() >= colorChangeSpeed / 255f) {
                        loadingAnimationPartLT.setrColor(loadingAnimationPartLT.getrColor() - colorChangeSpeed / 255f);
                        if (loadingAnimationPartLT.getrColor() <= 1/2f) {
                            loadingAnimationPartRT.setrColor(loadingAnimationPartRT.getrColor() - colorChangeSpeed / 255f);
                            loadingAnimationPartRT.updateColor();
                        }
                        loadingAnimationPartLT.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.RT;
                    }
                    break;
                case RT:
                    if (loadingAnimationPartRT.getrColor() >= colorChangeSpeed / 255f) {
                        loadingAnimationPartRT.setrColor(loadingAnimationPartRT.getrColor() - colorChangeSpeed / 255f);
                        if (loadingAnimationPartRT.getrColor() <= 1/2f) {
                            loadingAnimationPartRB.setrColor(loadingAnimationPartRB.getrColor() - colorChangeSpeed / 255f);
                            loadingAnimationPartRB.updateColor();
                        }
                        loadingAnimationPartRT.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.RB;
                    }

                    break;
                case RB:
                    if (loadingAnimationPartRB.getrColor() >= colorChangeSpeed / 255f) {
                        loadingAnimationPartRB.setrColor(loadingAnimationPartRB.getrColor() - colorChangeSpeed / 255f);
                        if (loadingAnimationPartRB.getrColor() <= 1/2f) {
                            loadingAnimationPartLB.setrColor(loadingAnimationPartLB.getrColor() - colorChangeSpeed / 255f);
                            loadingAnimationPartLB.updateColor();
                        }
                        loadingAnimationPartRB.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.LB;
                    }

                    break;
                case LB:
                    if (loadingAnimationPartLB.getrColor() >= colorChangeSpeed / 255f) {
                        loadingAnimationPartLB.setrColor(loadingAnimationPartLB.getrColor() - colorChangeSpeed / 255f);
                        if (loadingAnimationPartLB.getrColor() <= 1/1.3f) {
                            loadingAnimationPartLT.setrColor(loadingAnimationPartLT.getrColor() + colorChangeSpeed / 255f);
                            loadingAnimationPartLT.updateColor();
                        }
                        loadingAnimationPartLB.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.LT;
                        colorUpFirstPart = !colorUpFirstPart;
                    }

                    break;
            }
        } else {
            switch (colorChangeFirstPart) {
                case LT:
                    if (loadingAnimationPartLT.getrColor() <= 1 - colorChangeSpeed / 255f) {
                        loadingAnimationPartLT.setrColor(loadingAnimationPartLT.getrColor() + colorChangeSpeed / 255f);
                        if (loadingAnimationPartLT.getrColor() >= 1/2f) {
                            loadingAnimationPartRT.setrColor(loadingAnimationPartRT.getrColor() + colorChangeSpeed / 255f);
                            loadingAnimationPartRT.updateColor();
                        }
                        loadingAnimationPartLT.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.RT;
                    }
                    break;
                case RT:
                    if (loadingAnimationPartRT.getrColor() <= 1 - colorChangeSpeed / 255f) {
                        loadingAnimationPartRT.setrColor(loadingAnimationPartRT.getrColor() + colorChangeSpeed / 255f);
                        if (loadingAnimationPartRT.getrColor() >= 1/2f) {
                            loadingAnimationPartRB.setrColor(loadingAnimationPartRB.getrColor() + colorChangeSpeed / 255f);
                            loadingAnimationPartRB.updateColor();
                        }
                        loadingAnimationPartRT.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.RB;
                    }

                    break;
                case RB:
                    if (loadingAnimationPartRB.getrColor() <= 1 - colorChangeSpeed / 255f) {
                        loadingAnimationPartRB.setrColor(loadingAnimationPartRB.getrColor() + colorChangeSpeed / 255f);
                        if (loadingAnimationPartRB.getrColor() >= 1/2f) {
                            loadingAnimationPartLB.setrColor(loadingAnimationPartLB.getrColor() + colorChangeSpeed / 255f);
                            loadingAnimationPartLB.updateColor();
                        }
                        loadingAnimationPartRB.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.LB;
                    }

                    break;
                case LB:
                    if (loadingAnimationPartLB.getrColor() <= 1 - colorChangeSpeed / 255f) {
                        loadingAnimationPartLB.setrColor(loadingAnimationPartLB.getrColor() + colorChangeSpeed / 255f);*//*
                        if (loadingAnimationPartLB.getrColor() >= 1/1.2f) {
                            loadingAnimationPartLT.setrColor(loadingAnimationPartLT.getrColor() - colorChangeSpeed / 255f);
                            loadingAnimationPartLT.updateColor();
                        }*//*
                        loadingAnimationPartLB.updateColor();
                    } else {
                        colorChangeFirstPart = ColorChange.LT;
                        colorUpFirstPart = !colorUpFirstPart;
                    }

                    break;
            }
        }*/
    }

    public void draw(Batch batch) {
        loadingAnimationPartLT.draw(batch);
        loadingAnimationPartRT.draw(batch);
        loadingAnimationPartLB.draw(batch);
        loadingAnimationPartRB.draw(batch);
    }

}
