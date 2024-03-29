package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.Spikes;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.WalkingEnemy;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon.Dragon;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon.DragonFire;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.huntingspider.HuntingSpider;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.SpiderWeb;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;

import java.text.DecimalFormat;

public abstract class PlayerBodyPart extends Sprite {
    protected GameScreen gameScreen;
    protected TextureRegion bodyPartTexture;
    protected World world;
    private int texturePos;
    protected int sideOfBodyPart;
    protected Body b2body;
    protected Body b2bodyInvisible;
    protected Array<Body> b2bodies;
    protected Array<Joint> joints;
    protected Fixture fixture;
    protected boolean touchWall;
    protected String bodyPartName;
    protected int mapHeight;
    protected int spriteNumber;
    private boolean removeJoint;

    public PlayerBodyPart(World world, GameScreen gameScreen, int texturePos, int sideOfBodyPart, int mapHeight, int spriteNumber) {
        //super(gameScreen.getAtlas().findRegion("player"));
        this.world = world;
        this.gameScreen = gameScreen;
        this.texturePos = texturePos;
        this.sideOfBodyPart = sideOfBodyPart;
        this.mapHeight = mapHeight;
        this.spriteNumber = spriteNumber;
        b2bodies = new Array<>();
        joints = new Array<>();
        touchWall = false;
        defineBodyPart();

        bodyPartTexture = new TextureRegion(gameScreen.getPlayerAtlas().findRegion("player" + spriteNumber), 0 + 160 * texturePos, 0, 160, 160);
        setBounds(0, 0, 160 / FallingMan.PPM, 160 / FallingMan.PPM);
        setRegion(bodyPartTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        removeJoint = false;

        if (sideOfBodyPart == Player.RIGHT_BODY_PART) {
            flip(true, false);
        }
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if (bodyPartName.equals("handR")) {
            setFlip(true, true);
            setRotation((float) Math.toDegrees(b2body.getAngle()) - 90);
        } else if (bodyPartName.equals("handL")){
            setFlip(true, false);
            setRotation((float) Math.toDegrees(b2body.getAngle()) - 90);
        } else {
            setRotation((float) Math.toDegrees(b2body.getAngle()));
        }
        DecimalFormat df = new DecimalFormat("#.######");
        if (!this.getClass().equals(Belly.class)) {
            for (int i = 0; i < joints.size; i++) {
                Joint joint = joints.get(i);
                if (Float.parseFloat(df.format(Math.sqrt(Math.pow((joint.getAnchorB().x - joint.getAnchorA().x), 2) + Math.pow((joint.getAnchorB().y - joint.getAnchorA().y), 2))).replace(",", ".")) > 0.31f) {
                    if (touchWall && ((PlayerBodyPart) joint.getBodyB().getUserData()).isTouchWall()) {
                        Body bodyB = joint.getBodyB();
                        for (Joint joint1 : joints) {
                            world.destroyJoint(joint1);
                            joint = null;
                                //Gdx.input.vibrate(50);
                        }
                        joints = new Array<>();
                        setConnectedBodiesBitToDeafault(bodyB);
                        setCategoryFilter(FallingMan.DEFAULT_BIT);
                    }
                }
            }
        }
        if (removeJoint) {
            for (int i = 0; i < joints.size; i++) {
                Joint joint = joints.get(i);
                Body bodyB = joint.getBodyB();
                for (Joint joint1 : joints) {
                    world.destroyJoint(joint1);
                    joint1 = null;
                }
                joints = new Array<>();
                setConnectedBodiesBitToDeafault(bodyB);
                setCategoryFilter(FallingMan.DEFAULT_BIT);
            }
            removeJoint = false;
        }
        touchWall = false;
    }

    public abstract void defineBodyPart();

    public Body getB2body() {
        return b2body;
    }

    public int getSideOfBodyPart() {
        return sideOfBodyPart;
    }

    public Body getB2bodyInvisible() {
        return b2bodyInvisible;
    }

    public Array<Body> getB2bodies() {
        return b2bodies;
    }

    public Array<Joint> getJoints() {
        return joints;
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setConnectedBodiesBitToDeafault(Body body) {
        if (((PlayerBodyPart) body.getUserData()).getFixture().getFilterData().categoryBits != FallingMan.DEFAULT_BIT) {
            ((PlayerBodyPart) body.getUserData()).setCategoryFilter(FallingMan.DEFAULT_BIT);
            if (body.getJointList().size > 0) {
                for (JointEdge jointEdge : body.getJointList()) {
                    if (!jointEdge.joint.getClass().equals(WeldJoint.class)) {
                        setConnectedBodiesBitToDeafault(jointEdge.other);
                    }
                }
            }
        }
    }

    public boolean isTouchWall() {
        return touchWall;
    }

    public void setTouchWall(boolean touchWall) {
        this.touchWall = touchWall;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public String getBodyPartNameFromBody(Body body) {
        if (body.equals(b2body) || body.equals(b2bodyInvisible)) {
            return bodyPartName;
        } else {
            return "unknown";
        }
    }

    public void setTextureToBasic() {
        setRegion(bodyPartTexture);
        if (sideOfBodyPart == Player.RIGHT_BODY_PART && ! isFlipX()) {
            flip(true, false);
        }
    }

    public void setTexture(int spriteNumber) {
        this.spriteNumber = spriteNumber;
        bodyPartTexture = new TextureRegion(gameScreen.getPlayerAtlas().findRegion("player" + spriteNumber), 160 * texturePos, 0, 160, 160);
        setRegion(bodyPartTexture);
        if (sideOfBodyPart == Player.RIGHT_BODY_PART && ! isFlipX()) {
            flip(true, false);
        }
    }

    // checking if mouse position equals bodyPart position
    public boolean mouseOver(Vector2 mousePosition) {
        if(mousePosition.x > b2body.getPosition().x - 60 / FallingMan.PPM && mousePosition.x < b2body.getPosition().x + 60 / FallingMan.PPM
                && mousePosition.y > b2body.getPosition().y - 70 / FallingMan.PPM && mousePosition.y < b2body.getPosition().y + 70 / FallingMan.PPM)
            return true;
        else
            return false;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public void setRemoveJoint(boolean removeJoint, Object object) {
        if (object instanceof HuntingSpider || object instanceof Spikes || object instanceof WalkingEnemy || object instanceof Dragon || object instanceof DragonFire || object instanceof Rock) {
            this.removeJoint = removeJoint;
            if (gameScreen.getSaveData().getSounds()) {
                gameScreen.getAssetManager().getLoseBodyPartsSound().play();
            }
        }/* else if (object instanceof SpiderWeb && this instanceof Belly) {
            ((SpiderWeb) object).setTouchedByBelly(true);
        }*/ else {
            ((InteractiveObjectInterface) object).setTouched(true);
        }
    }
}
