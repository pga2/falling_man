package com.ledzinygamedevelopment.fallingman.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomBodyPart {
    public static String getRandomBodyPart(SaveData saveData) {
        HashMap<String, Boolean> bodySpritesAll = saveData.getBodySpritesOwned();
        ArrayList<String> bodyPartsNotOwned = new ArrayList<>();
        Random random = new Random();
        for (String name : bodySpritesAll.keySet()) {
            if (!bodySpritesAll.get(name)) {
                bodyPartsNotOwned.add(name.replace("owned", ""));
            }
        }
        int randomBodyPart = random.nextInt(bodyPartsNotOwned.size());

        return bodyPartsNotOwned.get(randomBodyPart);
    }

    public static String getRandomBodyPart(SaveData saveData, int minSpriteNumber, int maxSpriteNumber, List<String> bodyPartsNotWanted) {
        HashMap<String, Boolean> bodySpritesAll = saveData.getBodySpritesOwned();
        ArrayList<String> bodyPartsNotOwned = new ArrayList<>();
        Random random = new Random();
        for (String name : bodySpritesAll.keySet()) {
            if (!bodySpritesAll.get(name)) {
                int spriteNumber = Integer.parseInt(extractNumber(name));
                if (spriteNumber >= minSpriteNumber && spriteNumber <= maxSpriteNumber) {
                    boolean checkPart = true;
                    for (String bodyPartNotWanted : bodyPartsNotWanted) {
                        if (name.contains(bodyPartNotWanted)) {
                            checkPart = false;
                        }
                    }
                    if (checkPart) {
                        bodyPartsNotOwned.add(name.replace("owned", ""));
                    }
                }
            }
        }
        int randomBodyPart = random.nextInt(bodyPartsNotOwned.size());

        return bodyPartsNotOwned.get(randomBodyPart);
    }

    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }
}
