package com.ledzinygamedevelopment.fallingman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ledzinygamedevelopment.fallingman.screens.InAppPurchasesScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class FallingMan extends Game {
	public SpriteBatch batch;

	public static PurchaseManager purchaseManager;

	public static final int MIN_WORLD_WIDTH = 1440;
	public static final int MIN_WORLD_HEIGHT = 2560;
	public static final int MAX_WORLD_WIDTH = 1440;
	public static final int MAX_WORLD_HEIGHT = 3360;
	public static final float PPM = 100;
	public static final int CELL_SIZE = 32;
	public static final int PLAYER_STARTING_X_POINT = 720;

	public static final short DEFAULT_BIT = 1;
	public static final short INTERACTIVE_TILE_OBJECT_BIT = 2;
	public static final short DEAD_MACHINE_BIT = 4;
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

	public static final byte GAME_OVER_WINDOW = 0;

	public static final int ALL_BODY_SPRITES_LENGHT = 9;

	public static final String GOLD_100 = "GOLD_100";
	public static final String GOLD_200 = "GOLD_200";
	public static final String GOLD_500 = "GOLD_500";
	public static final String GOLD_1000 = "GOLD_1000";

	@Override
	public void create () {
		batch = new SpriteBatch();
		//setScreen(new PlayScreen(this));
		//setScreen(new MenuScreen(this, new Array<Vector2>(), 0));
		//setScreen(new ShopScreen(this, null, 0));
		setScreen(new InAppPurchasesScreen(this, null, 0));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	private void initPurchaseManager() {


		PurchaseManagerConfig pmc = new PurchaseManagerConfig();
		//pmc.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(YOUR_ITEM_SKU));
		pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(GOLD_100));
//		pmc.addStoreParam(GOLD_100, );
		pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(GOLD_200));
		pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(GOLD_500));
		pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(GOLD_1000));
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
			throw new GdxRuntimeException(e);
		}

		@Override
		public void handleRestore(Transaction[] transactions) {
			/*for (int i = 0; i < transactions.length; i++) {
				if (checkTransaction(transactions[i].getIdentifier()) == true) break;
			}
			// to make a purchase (results are reported to the observer)
			PurchaseSystem.purchase(SKU_REMOVE_ADS);*/
		}

		@Override
		public void handleRestoreError(Throwable e) {
			Gdx.app.log("ERROR", "PurchaseObserver: handleRestoreError!: " + e.getMessage());
			throw new GdxRuntimeException(e);
		}

		@Override
		public void handlePurchase(Transaction transaction) {
			//checkTransaction(transaction.getIdentifier());
		}

		@Override
		public void handlePurchaseError(Throwable e) {
			if (e.getMessage().equals("There has been a Problem with your Internet connection. Please try again later")) {
				e.printStackTrace();
				// this check is needed because user-cancel is a handlePurchaseError too)
				// getPlatformResolver().showToast("handlePurchaseError: " + e.getMessage());
			}
			throw new GdxRuntimeException(e);
		}

		@Override
		public void handlePurchaseCanceled() {

		}
	};

	private ArrayList<String> getAllTypesOfPurchases() {
		ArrayList<String> allTypesOfPurchases = new ArrayList<>();
		allTypesOfPurchases.add(GOLD_100);
		allTypesOfPurchases.add(GOLD_200);
		allTypesOfPurchases.add(GOLD_500);
		allTypesOfPurchases.add(GOLD_1000);

		return allTypesOfPurchases;
	}
}
