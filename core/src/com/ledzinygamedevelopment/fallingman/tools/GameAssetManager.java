package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class GameAssetManager {
    private final AssetManager manager = new AssetManager();

    //MenuScreen
    private final String menuScreenDefault = "images/menuScreen/default.atlas";
    private final String menuScreenBigRock = "images/playScreen/big_rock.atlas";
    private final Array<String> rockTexturesPaths = new Array<>();

    //OneArmedBandtScreen
    private final String oneArmedBanditScreenDefault = "images/oneArmedBanditScreen/default.atlas";

    //PlayScreen
    private final String playScreenDefault = "images/playScreen/default.atlas";
    private final String playScreenWindow = "images/playScreen/window.atlas";
    private final String playScreenBigRock = "images/playScreen/big_rock.atlas";

    public void loadPlayScreen() {
        manager.load(playScreenDefault, TextureAtlas.class);
        manager.load(playScreenWindow, TextureAtlas.class);
        manager.load(playScreenBigRock, TextureAtlas.class);
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
    }

    public void loadMenuScreen() {
        manager.load(menuScreenDefault, TextureAtlas.class);
        manager.load(menuScreenBigRock, TextureAtlas.class);
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
    }

    public void loadOneArmedBandit() {
        manager.load(oneArmedBanditScreenDefault, TextureAtlas.class);
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
}
