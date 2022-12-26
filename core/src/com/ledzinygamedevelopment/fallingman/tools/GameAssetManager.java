package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    //private final String menuScreenBigRock = "images/playScreen/big_rock.atlas";
    private final Array<String> rockTexturesPaths = new Array<>();

    //OneArmedBandtScreen
    private final String oneArmedBanditScreenDefault = "images/oneArmedBanditScreen/default.atlas";
    private final String oneArmedBanditScreenBaloon = "images/oneArmedBanditScreen/baloon.atlas";

    //PlayScreen
    private final String playScreenDefault = "images/playScreen/default.atlas";
    private final String playScreenWindow = "images/playScreen/window.atlas";
    private final String playScreenBigRockSpine = "spine_animations/big_rock.atlas";
    private final String playScreenDragonSpine = "spine_animations/dragon.atlas";
    private final String playScreenSpiderSpine = "spine_animations/spider.atlas";
    private final String playScreenWarriorSpine = "spine_animations/warrior.atlas";
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
    private final String font = "font/LilitaOne.fnt";
    private final String playerSprite = "images/playerSprites/player.atlas";
    private final String backgroundImage = "images/background/background.png";

    //EveryScreenSounds
    private final String clickButtonSoundPath = "sounds/sounds/12. Sound of clicking button.wav";
    private final String releaseButtonSoundPath = "sounds/sounds/13. Sound of releasing button.wav";
    private Sound clickButtonSound;
    private Sound releaseButtonSound;

    //PlayScreem sounds
    private final String playScreenMusicPath = "sounds/music/PlayScreen.wav";
    private final String playerDieSoundPath = "sounds/sounds/1. Sound of player dying.wav";
    private final String touchCoinSoundPath = "sounds/sounds/2. Sound of player touching coin.wav";
    private final String touchSpinSoundPath = "sounds/sounds/3. Sound of player touching spin.wav";
    private final String touchReviveSoundPath = "sounds/sounds/4. Sound of player touching revive.wav";
    private final String touchSpiderSoundPath = "sounds/sounds/5. Sound of player touching spider.wav";
    private final String teleportSoundEntryPath = "sounds/sounds/6. Sound of teleporting Entry.wav";
    private final String teleportSoundExitPath = "sounds/sounds/6. Sound of teleporting Exit.wav";
    private final String walkingWarriorSoundPath = "sounds/sounds/7. Sound of walking warrior close to player.wav";
    private final String bigRockSoundPath = "sounds/sounds/8. Sound of big enemy on top close to player.wav";
    private final String dragonSoundPath = "sounds/sounds/9. Sound of dragon close to player.wav";
    private final String adGoldSoundPath = "sounds/sounds/10. Sound of getting extra gold for watching ad.wav";
    private final String highScoreSoundPath = "sounds/sounds/11. Sound of getting high score.wav";
    private final String loseBodyPartsSoundPath = "sounds/sounds/14. Sound of losing body parts.wav";

    private Music playScreenMusic;
    private Sound playerDieSound;
    private Sound touchCoinSound;
    private Sound touchSpinSound;
    private Sound touchReviveSound;
    private Sound touchSpiderSound;
    private Sound teleportEntrySound;
    private Sound teleportExitSound;
    private Sound walkingWarriorSound;
    private Sound bigRockSound;
    private Sound dragonSound;
    private Sound adGoldSound;
    private Sound highScoreSound;
    private Sound loseBodyPartsSound;


    //no PlayScreen sounds
    private final String spinSoundPath = "sounds/sounds/15. Sound of spinning one armed bandit.wav";
    private final String winOneArmedBanditSoundPath = "sounds/sounds/16. Sound of winning one armed bandit.wav";
    private final String destroyedBalloonSoundPath = "sounds/sounds/17. Sound of destroying balloon.wav";
    private final String buyBodyPartSoundPath = "sounds/sounds/18. Sound of buying new body part.wav";
    private final String selectBodyPartSoundPath = "sounds/sounds/19. Sound of selecting new body part.wav";

    private Sound spinSound;
    private Sound winOneArmedBanditSound;
    private Sound destroyedBalloonSound;
    private Sound buyBodyPartSound;
    private Sound selectBodyPartSound;

    private boolean playScreenGetSounds;

    public GameAssetManager() {
        manager.load(clickButtonSoundPath, Sound.class);
        manager.load(releaseButtonSoundPath, Sound.class);
        playScreenGetSounds = false;
    }

    public void loadPlayScreen() {
        manager.load(playScreenDefault, TextureAtlas.class);
        manager.load(playScreenWindow, TextureAtlas.class);
        manager.load(playerSprite, TextureAtlas.class);
        manager.load(playScreenBigRockSpine, TextureAtlas.class);
        manager.load(playScreenDragonSpine, TextureAtlas.class);
        manager.load(playScreenSpiderSpine, TextureAtlas.class);
        manager.load(playScreenWarriorSpine, TextureAtlas.class);
        //rockTexturesPaths.add("images/menuScreen/rock1.png");
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

        loadPlayScreenSounds();

    }

    public void loadPlayScreenSounds() {
        manager.load(playScreenMusicPath, Music.class);
        manager.load(playerDieSoundPath, Sound.class);
        manager.load(touchCoinSoundPath, Sound.class);
        manager.load(touchSpinSoundPath, Sound.class);
        manager.load(touchReviveSoundPath, Sound.class);
        manager.load(touchSpiderSoundPath, Sound.class);
        manager.load(teleportSoundEntryPath, Sound.class);
        manager.load(teleportSoundExitPath, Sound.class);
        manager.load(walkingWarriorSoundPath, Sound.class);
        manager.load(bigRockSoundPath, Sound.class);
        manager.load(dragonSoundPath, Sound.class);
        manager.load(adGoldSoundPath, Sound.class);
        manager.load(highScoreSoundPath, Sound.class);
        manager.load(loseBodyPartsSoundPath, Sound.class);

        //playScreenGetSounds = true;
    }

    /*public void playScreenGetSoundsMethod() {
        playerDieSound = getPlayerDieSound();
        touchCoinSound = getTouchCoinSound();
        touchSpinSound = getTouchSpinSound();
        touchReviveSound = getTouchReviveSound();
        touchSpiderSound = getTouchSpiderSound();
        teleportEntrySound = getTeleportEntrySound();
        teleportExitSound = getTeleportExitSound();
        walkingWarriorSound = getWalkingWarriorSound();
        bigRockSound = getBigRockSound();
        dragonSound = getDragonSound();
        adGoldSound = getAdGoldSound();
        highScoreSound = getHighScoreSound();
        loseBodyPartsSound = getLoseBodyPartsSound();
    }*/

    public void loadMenuScreen() {
        manager.load(menuScreenDefault, TextureAtlas.class);
        manager.load(playScreenWindow, TextureAtlas.class);
        manager.load(playerSprite, TextureAtlas.class);

        manager.load(font, BitmapFont.class);
    }

    public void loadOneArmedBandit() {
        manager.load(oneArmedBanditScreenDefault, TextureAtlas.class);
        manager.load(oneArmedBanditScreenBaloon, TextureAtlas.class);
        manager.load(playerSprite, TextureAtlas.class);

        manager.load(font, BitmapFont.class);
        manager.load(backgroundImage, Texture.class);

        manager.load(destroyedBalloonSoundPath, Sound.class);
        manager.load(spinSoundPath, Sound.class);
        manager.load(winOneArmedBanditSoundPath, Sound.class);
    }

    public void loadShopScreen() {
        manager.load(font, BitmapFont.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(shopScreenMap, TiledMap.class);
        manager.load(playerSprite, TextureAtlas.class);
        manager.load(shopScreenDefault, TextureAtlas.class);
        manager.load(backgroundImage, Texture.class);

        manager.load(selectBodyPartSoundPath, Sound.class);
        manager.load(buyBodyPartSoundPath, Sound.class);
    }

    public void loadInAppPurchasesScreen() {
        manager.load(font, BitmapFont.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(inAppPurchasesMap, TiledMap.class);
        manager.load(inAppPurchasesDefault, TextureAtlas.class);
        manager.load(backgroundImage, Texture.class);
    }

    public void loadSettingsScreen() {
        manager.load(font, BitmapFont.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(inAppPurchasesMap, TiledMap.class);
        manager.load(settingsDefault, TextureAtlas.class);
        manager.load(backgroundImage, Texture.class);
    }

    public AssetManager getManager() {
        return manager;
    }

    public void finishLoading() {
        manager.finishLoading();
        clickButtonSound = manager.get(clickButtonSoundPath);
        releaseButtonSound = manager.get(releaseButtonSoundPath);

        /*if (playScreenGetSounds) {
            playScreenGetSoundsMethod();
            playScreenGetSounds = false;
        }*/
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

    public String getPlayScreenBigRockSpine() {
        return playScreenBigRockSpine;
    }

    /*public String getMenuScreenBigRock() {
        return menuScreenBigRock;
    }*/

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

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getPlayScreenDragonSpine() {
        return playScreenDragonSpine;
    }

    public String getPlayScreenSpiderSpine() {
        return playScreenSpiderSpine;
    }

    public String getPlayScreenWarriorSpine() {
        return playScreenWarriorSpine;
    }

    public Sound getClickButtonSound() {
        return clickButtonSound;
    }

    public Sound getReleaseButtonSound() {
        return releaseButtonSound;
    }

    public Music getPlayScreenMusic() {
        if (playScreenMusic == null) {
            playScreenMusic = manager.get(playScreenMusicPath);
        }
        return playScreenMusic;
    }

    public Sound getPlayerDieSound() {
        if (playerDieSound == null) {
            playerDieSound = manager.get(playerDieSoundPath);
        }
        return playerDieSound;
    }

    public Sound getTouchCoinSound() {
        if (touchCoinSound == null) {
            touchCoinSound = manager.get(touchCoinSoundPath);
        }
        return touchCoinSound;
    }

    public Sound getTouchSpinSound() {
        if (touchSpinSound == null) {
            touchSpinSound = manager.get(touchSpinSoundPath);
        }
        return touchSpinSound;
    }

    public Sound getTouchReviveSound() {
        if (touchReviveSound == null) {
            touchReviveSound = manager.get(touchReviveSoundPath);
        }
        return touchReviveSound;
    }

    public Sound getTouchSpiderSound() {
        if (touchSpiderSound == null) {
            touchSpiderSound = manager.get(touchSpiderSoundPath);
        }
        return touchSpiderSound;
    }

    public Sound getTeleportEntrySound() {
        if (teleportEntrySound == null) {
            teleportEntrySound = manager.get(teleportSoundEntryPath);
        }
        return teleportEntrySound;
    }

    public Sound getTeleportExitSound() {
        if (teleportExitSound == null) {
            teleportExitSound = manager.get(teleportSoundExitPath);
        }
        return teleportExitSound;
    }

    public Sound getWalkingWarriorSound() {
        if (walkingWarriorSound == null) {
            walkingWarriorSound = manager.get(walkingWarriorSoundPath);
        }
        return walkingWarriorSound;
    }

    public Sound getBigRockSound() {
        if (bigRockSound == null) {
            bigRockSound = manager.get(bigRockSoundPath);
        }
        return bigRockSound;
    }

    public Sound getDragonSound() {
        if (dragonSound == null) {
            dragonSound = manager.get(dragonSoundPath);
        }
        return dragonSound;
    }

    public Sound getAdGoldSound() {
        if (adGoldSound == null) {
            adGoldSound = manager.get(adGoldSoundPath);
        }
        return adGoldSound;
    }

    public Sound getHighScoreSound() {
        if (highScoreSound == null) {
            highScoreSound = manager.get(highScoreSoundPath);
        }
        return highScoreSound;
    }

    public Sound getLoseBodyPartsSound() {
        if (loseBodyPartsSound == null) {
            loseBodyPartsSound = manager.get(loseBodyPartsSoundPath);
        }
        return loseBodyPartsSound;
    }

    public Sound getSpinSound() {
        if (spinSound == null) {
            spinSound = manager.get(spinSoundPath);
        }
        return spinSound;
    }

    public Sound getWinOneArmedBanditSound() {
        if (winOneArmedBanditSound == null) {
            winOneArmedBanditSound = manager.get(winOneArmedBanditSoundPath);
        }
        return winOneArmedBanditSound;
    }

    public Sound getDestroyedBalloonSound() {
        if (destroyedBalloonSound == null) {
            destroyedBalloonSound = manager.get(destroyedBalloonSoundPath);
        }
        return destroyedBalloonSound;
    }

    public Sound getBuyBodyPartSound() {
        if (buyBodyPartSound == null) {
            buyBodyPartSound = manager.get(buyBodyPartSoundPath);
        }
        return buyBodyPartSound;
    }

    public Sound getSelectBodyPartSound() {
        if (selectBodyPartSound == null) {
            selectBodyPartSound = manager.get(selectBodyPartSoundPath);
        }
        return selectBodyPartSound;
    }
}
