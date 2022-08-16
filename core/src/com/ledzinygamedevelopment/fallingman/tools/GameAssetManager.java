package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public class GameAssetManager {
    private final AssetManager manager = new AssetManager();

    //MenuScreen
    private final String menuScreenDefault = "images/menuScreen/default.atlas";
    private final String menuScreenBigRock = "images/playScreen/big_rock.atlas";
    private final Array<String> rockTexturesPaths = new Array<>();

    //OneArmedBandtScreen
    private final String oneArmedBanditScreenDefault = "images/oneArmedBanditScreen/default.atlas";
    private final String oneArmedBanditScreenBaloon = "images/oneArmedBanditScreen/baloon.atlas";

    //PlayScreen
    private final String playScreenDefault = "images/playScreen/default.atlas";
    private final String playScreenWindow = "images/playScreen/window.atlas";
    private final String playScreenBigRock = "images/playScreen/big_rock.atlas";
    private final String playScreenDragon = "images/playScreen/dragon.png";
    private final String playScreenDragonFire = "images/playScreen/dragon_fire.png";

    //ShopScreen
    private final String shopScreenMap = "shop_maps/basic_shop_map.tmx";
    private final String shopScreenDefault = "images/shopScreen/default.atlas";

    //InAppPurchasesScreen
    private final String inAppPurchasesMap = "shop_maps/basic_shop_map.tmx";
    private final String inAppPurchasesDefault = "images/inAppPurchasesScreen/default.atlas";

    //SettingsScreen
    private final String settingsDefault = "images/settingsScreen/default.atlas";

    //all screens
    private final String font = "test_font/FSM.fnt";
    private final String playerSprite = "images/playerSprites/player.atlas";

    public void loadPlayScreen() {
        manager.load(playScreenDefault, TextureAtlas.class);
        manager.load(playScreenWindow, TextureAtlas.class);
        manager.load(playScreenBigRock, TextureAtlas.class);
        manager.load(playerSprite, TextureAtlas.class);
        rockTexturesPaths.add("images/menuScreen/rock1.png");
        /*rockTexturesPaths.add("images/menuScreen/rock2.png");
        rockTexturesPaths.add("images/menuScreen/rock3.png");
        rockTexturesPaths.add("images/menuScreen/rock4.png");
        rockTexturesPaths.add("images/menuScreen/rock5.png");
        rockTexturesPaths.add("images/menuScreen/rock6.png");
        rockTexturesPaths.add("images/menuScreen/rock7.png");
        rockTexturesPaths.add("images/menuScreen/rock8.png");
        rockTexturesPaths.add("images/menuScreen/rock9.png");
        rockTexturesPaths.add("images/menuScreen/rock10.png");*/
        for (String path : rockTexturesPaths) {
            manager.load(path, Texture.class);
        }

        manager.load(playScreenDragon, Texture.class);
        manager.load(playScreenDragonFire, Texture.class);

        manager.load(font, BitmapFont.class);
    }

    public void loadMenuScreen() {
        manager.load(menuScreenDefault, TextureAtlas.class);
        manager.load(menuScreenBigRock, TextureAtlas.class);
        manager.load(playerSprite, TextureAtlas.class);

        rockTexturesPaths.add("images/menuScreen/rock1.png");
        rockTexturesPaths.add("images/menuScreen/rock2.png");
        rockTexturesPaths.add("images/menuScreen/rock3.png");
        rockTexturesPaths.add("images/menuScreen/rock4.png");
        rockTexturesPaths.add("images/menuScreen/rock5.png");
        rockTexturesPaths.add("images/menuScreen/rock6.png");
        rockTexturesPaths.add("images/menuScreen/rock7.png");
        rockTexturesPaths.add("images/menuScreen/rock8.png");
        rockTexturesPaths.add("images/menuScreen/rock9.png");
        rockTexturesPaths.add("images/menuScreen/rock10.png");
        for (String path : rockTexturesPaths) {
            manager.load(path, Texture.class);
        }

        manager.load(font, BitmapFont.class);
    }

    public void loadOneArmedBandit() {
        manager.load(oneArmedBanditScreenDefault, TextureAtlas.class);
        manager.load(oneArmedBanditScreenBaloon, TextureAtlas.class);
        manager.load(playerSprite, TextureAtlas.class);

        manager.load(font, BitmapFont.class);
    }

    public void loadShopScreen() {
        manager.load(font, BitmapFont.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(shopScreenMap, TiledMap.class);
        manager.load(playerSprite, TextureAtlas.class);
        manager.load(shopScreenDefault, TextureAtlas.class);
    }

    public void loadInAppPurchasesScreen() {
        manager.load(font, BitmapFont.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(inAppPurchasesMap, TiledMap.class);
        manager.load(inAppPurchasesDefault, TextureAtlas.class);
    }

    public void loadSettingsScreen() {
        manager.load(font, BitmapFont.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(inAppPurchasesMap, TiledMap.class);
        manager.load(settingsDefault, TextureAtlas.class);
    }

    public AssetManager getManager() {
        return manager;
    }

    public String getMenuScreenDefault() {
        return menuScreenDefault;
    }

    public String getOneArmedBanditScreenDefault() {
        return oneArmedBanditScreenDefault;
    }

    public String getPlayScreenDefault() {
        return playScreenDefault;
    }

    public String getPlayScreenWindow() {
        return playScreenWindow;
    }

    public String getPlayScreenBigRock() {
        return playScreenBigRock;
    }

    public String getMenuScreenBigRock() {
        return menuScreenBigRock;
    }

    public Array<String> getRockTexturesPaths() {
        return rockTexturesPaths;
    }

    public String getFont() {
        return font;
    }

    public String getPlayScreenDragon() {
        return playScreenDragon;
    }

    public String getPlayScreenDragonFire() {
        return playScreenDragonFire;
    }

    public String getShopScreenMap() {
        return shopScreenMap;
    }

    public String getPlayerSprite() {
        return playerSprite;
    }

    public String getShopScreenDefault() {
        return shopScreenDefault;
    }

    public String getInAppPurchasesMap() {
        return inAppPurchasesMap;
    }

    public String getInAppPurchasesDefault() {
        return inAppPurchasesDefault;
    }

    public String getOneArmedBanditScreenBaloon() {
        return oneArmedBanditScreenBaloon;
    }

    public String getSettingsDefault() {
        return settingsDefault;
    }
}
