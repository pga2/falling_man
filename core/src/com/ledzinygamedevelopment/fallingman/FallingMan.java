package com.ledzinygamedevelopment.fallingman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.MenuScreen;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.ToastCreator;

import java.util.ArrayList;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.IGameServiceListener;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

//import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class FallingMan extends Game implements IGameServiceListener {

    private final AdsController adsController;

    public Batch batch;
    public TwoColorPolygonBatch twoColorPolygonBatch;

    public IGameServiceClient gsClient;

    public ToastCreator toastCreator;

    public static PurchaseManager purchaseManager;
    public static byte currentScreen;
    public static GameScreen gameScreen;

    public static final float SUN_SPEED = 0.005f;

    public static final int MIN_WORLD_WIDTH = 1440;
    public static final int MIN_WORLD_HEIGHT = 2560;
    public static final int MAX_WORLD_WIDTH = 1440;
    public static final int MAX_WORLD_HEIGHT = 3360;
    public static final float PPM = 100;
    public static final int CELL_SIZE = 32;
    public static final int PLAYER_STARTING_X_POINT = 720;

    public static final short DEFAULT_BIT = 1;
    public static final short INTERACTIVE_TILE_OBJECT_BIT = 2;
    public static final short STOP_WALKING_ENEMY_BIT = 4;
    public static final short DESTROYED_BIT = 8;
    public static final short PLAYER_HEAD_BIT = 16;
    public static final short PLAYER_BELLY_BIT = 32;
    public static final short PLAYER_ARM_BIT = 64;
    public static final short PLAYER_THIGH_BIT = 128;
    public static final short PLAYER_FORE_ARM_BIT = 256;
    public static final short PLAYER_SHIN_BIT = 512;
    public static final short PLAYER_HAND_BIT = 1024;
    public static final short PLAYER_FOOT_BIT = 2048;
    public static final short ROCK_BIT = 4096;
    public static final short WALL_INSIDE_TOWER = 8192;
    public static final short INVISIBLE_BODY_PART_BIT = 16384;

    public static final byte CURRENT_SCREEN = 0;
    public static final byte MENU_SCREEN = 1;
    public static final byte PLAY_SCREEN = 2;
    public static final byte ONE_ARMED_BANDIT_SCREEN = 3;
    public static final byte SHOP_SCREEN = 4;
    public static final byte IN_APP_PURCHASES_SCREEN = 5;
    public static final byte SETTINGS_SCREEN = 6;

    public static final byte GAME_OVER_WINDOW = 6;

    public static final int ALL_BODY_SPRITES_LENGHT = 9;

    public static final String gold_10000 = "gold_10000";
    public static final String gold_24000 = "gold_24000";
    public static final String gold_54000 = "gold_54000";
    public static final String gold_120000 = "gold_120000";
    public static final String gold_280000 = "gold_280000";
    public static final String gold_750000 = "gold_750000";
    public static final String spin_250 = "spin_250";
    public static final String spin_550 = "spin_550";
    public static final String spin_1350 = "spin_1350";
    public static final String spin_3000 = "spin_3000";
    public static final String spin_7000 = "spin_7000";
    public static final String spin_20000 = "spin_20000";


    public FallingMan(AdsController adsController, ToastCreator toastCreator) {
        this.adsController = adsController;
        this.toastCreator = toastCreator;
    }

    @Override
    public void create() {
        // ...awesome initialization code...

        if (gsClient == null)
            gsClient = new NoGameServiceClient();

        // for getting callbacks from the client
        gsClient.setListener(this);

        // establish a connection to the game service without error messages or login screens
        gsClient.resumeSession();


        //gsClient.submitToLeaderboard("CgkI-N6Fv6wJEAIQAg", 2, "lol");

        //gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQAQ");

        batch = new TwoColorPolygonBatch();
        //twoColorPolygonBatch = new TwoColorPolygonBatch();
        //setScreen(new PlayScreen(this));
        //setScreen(new MenuScreen(this, new Array<Vector2>(), 0));
        //setScreen(new ShopScreen(this, null, 0));
        try {
            initPurchaseManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentScreen = FallingMan.MENU_SCREEN;
        gameScreen = new MenuScreen(this, new Array<Vector2>(), 0, 1, 1, 1, Color.BLACK, true);
		/*currentScreen = FallingMan.IN_APP_PURCHASES_SCREEN;
		gameScreen = new InAppPurchasesScreen(this, null, 0);*/
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

        batch.dispose();
    }

    private void initPurchaseManager() {


        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        //pmc.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(YOUR_ITEM_SKU));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(gold_10000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(gold_24000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(gold_54000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(gold_120000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(gold_280000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(gold_750000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(spin_250));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(spin_550));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(spin_1350));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(spin_3000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(spin_7000));
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(spin_20000));
        //pmc.addOffer(new Offer().setType(OfferType.SUBSCRIPTION).setIdentifier(YOUR_ITEM_SKU));
        // some payment services might need special parameters, see documentation
        //pmc.addStoreParam(storename, param)

        purchaseManager.install(myPurchaseObserver, pmc, true);

    }

    private PurchaseObserver myPurchaseObserver = new PurchaseObserver() {
        @Override
        public void handleInstall() {
            Gdx.app.log("init", "success");
        }

        @Override
        public void handleInstallError(Throwable e) {
            Gdx.app.log("ERROR", "PurchaseObserver: handleInstallError!: " + e.getMessage());
            //throw new GdxRuntimeException(e);
        }

        @Override
        public void handleRestore(Transaction[] transactions) {
            SaveData saveData = new SaveData();
            for (Transaction transaction : transactions) {
                /*if (currentScreen == IN_APP_PURCHASES_SCREEN) {
                    gameScreen.addOnePartRolls(100, transaction.getIdentifier().startsWith("gold") ? 2 : 0, new Vector2(MIN_WORLD_WIDTH / 2f / FallingMan.PPM, MIN_WORLD_HEIGHT / 2f / FallingMan.PPM), transaction.getIdentifier());
                } else {*/
                    if (transaction.getIdentifier().startsWith("spin")) {
                        saveData.addSpins(Integer.parseInt(transaction.getIdentifier().substring(5)));
                    } else if (transaction.getIdentifier().startsWith("gold")) {
                        saveData.addGold(Long.parseLong(transaction.getIdentifier().substring(5)));
                        if (gameScreen != null && gameScreen.getGoldAndHighScoresIcons() != null) {
                            gameScreen.getGoldAndHighScoresIcons().setGold(saveData.getGold());
                        }
                    } else {
                        throw new NullPointerException("transaction identifier incorrect" + transaction.getOrderId());
                    }
                //}
            }
        }

        @Override
        public void handleRestoreError(Throwable e) {
            Gdx.app.log("ERROR", "PurchaseObserver: handleRestoreError!: " + e.getMessage());
            throw new GdxRuntimeException(e);
        }

        @Override
        public void handlePurchase(Transaction transaction) {
            //checkTransaction(transaction.getIdentifier());
            SaveData saveData = new SaveData();
            if (currentScreen == IN_APP_PURCHASES_SCREEN) {
                gameScreen.addOnePartRolls(transaction.getIdentifier().startsWith("gold") ? 2 : 0, new Vector2(MIN_WORLD_WIDTH / 2f / FallingMan.PPM, MIN_WORLD_HEIGHT / 2f / FallingMan.PPM), transaction.getIdentifier());
            } else {
                if (transaction.getIdentifier().startsWith("spin")) {
                    saveData.addSpins(Integer.parseInt(transaction.getIdentifier().substring(5)));
                } else if (transaction.getIdentifier().startsWith("gold")) {
                    saveData.addGold(Long.parseLong(transaction.getIdentifier().substring(5)));
                    if (gameScreen != null && gameScreen.getGoldAndHighScoresIcons() != null) {
                        gameScreen.getGoldAndHighScoresIcons().setGold(saveData.getGold());
                    }
                } else {
                    throw new NullPointerException("transaction identifier incorrect");
                }
            }
        }

        @Override
        public void handlePurchaseError(Throwable e) {
            if (e.getMessage().equals("There has been a Problem with your Internet connection. Please try again later")) {
                e.printStackTrace();
                // this check is needed because user-cancel is a handlePurchaseError too)
                // getPlatformResolver().showToast("handlePurchaseError: " + e.getMessage());
            }
            //throw new GdxRuntimeException(e);
        }

        @Override
        public void handlePurchaseCanceled() {
            Gdx.app.log("handlePurchaseCanceled", "purchase canceled");
        }
    };

    private ArrayList<String> getAllTypesOfPurchases() {
        ArrayList<String> allTypesOfPurchases = new ArrayList<>();
        allTypesOfPurchases.add(gold_10000);
        allTypesOfPurchases.add(gold_24000);
        allTypesOfPurchases.add(gold_54000);
        allTypesOfPurchases.add(gold_120000);
        allTypesOfPurchases.add(gold_280000);
        allTypesOfPurchases.add(gold_750000);
        allTypesOfPurchases.add(spin_250);
        allTypesOfPurchases.add(spin_550);
        allTypesOfPurchases.add(spin_1350);
        allTypesOfPurchases.add(spin_3000);
        allTypesOfPurchases.add(spin_7000);
        allTypesOfPurchases.add(spin_20000);

        return allTypesOfPurchases;
    }

    public AdsController getAdsController() {
        return adsController;
    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }

    @Override
    public void pause() {
        super.pause();

        gsClient.pauseSession();
    }

    @Override
    public void resume() {
        super.resume();

        gsClient.resumeSession();
    }

    @Override
    public void gsOnSessionActive() {

    }

    @Override
    public void gsOnSessionInactive() {

    }

    @Override
    public void gsShowErrorToUser(GsErrorType et, String msg, Throwable t) {
        Gdx.app.log("\n\n\n\n\n\n\n\n\n----------------\n\n\n\n\n\n\nerror gamesvcs", msg + "\n\n\n\n\n\n\n\n\n\n\n");
    }
}
