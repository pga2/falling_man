package com.ledzinygamedevelopment.fallingman.tools;

import java.util.Random;

public class MapGenUtils {
    public static String getRandomMap(Long score) {
        StringBuilder mapNameBuilder = new StringBuilder();
        Random random = new Random();
        mapNameBuilder.append("maps/");
        if (score < 10) {
            mapNameBuilder.insert(mapNameBuilder.length(), "maps_1/");
            if(random.nextInt(10) == 0) {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map_special");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(2));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            } else {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(10));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            }
        } else if (score < 20) {
            mapNameBuilder.insert(mapNameBuilder.length(), "maps_2/");
            if(random.nextInt(12) == 0) {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map_special");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(3));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            } else {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(10));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            }
        } else if (score < 30) {
            mapNameBuilder.insert(mapNameBuilder.length(), "maps_3/");
            if(random.nextInt(14) == 0) {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map_special");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(4));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            } else {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(10));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            }
        } else if (score < 40) {
            mapNameBuilder.insert(mapNameBuilder.length(), "maps_4/");
            if(random.nextInt(16) == 0) {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map_special");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(5));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            } else {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(10));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            }
        } else {
            mapNameBuilder.insert(mapNameBuilder.length(), "maps_5/");
            if(random.nextInt(16) == 0) {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map_special");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(6));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            } else {
                mapNameBuilder.insert(mapNameBuilder.length(), "playscreen_map");
                mapNameBuilder.insert(mapNameBuilder.length(), random.nextInt(10));
                mapNameBuilder.insert(mapNameBuilder.length(), ".tmx");
            }
        }

        return mapNameBuilder.toString();
    }
}
