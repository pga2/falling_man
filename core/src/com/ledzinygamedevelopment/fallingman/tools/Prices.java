package com.ledzinygamedevelopment.fallingman.tools;

import java.util.HashMap;

public class Prices {
    private HashMap<String, Long> pricesMap;

    private static final long BASIC_PRICE_HEAD = 3000;
    private static final long BASIC_PRICE_BODY = 3000;
    private static final long BASIC_PRICE_ARM = 2000;
    private static final long BASIC_PRICE_FOREARM = 1000;
    private static final long BASIC_PRICE_HAND = 500;
    private static final long BASIC_PRICE_THIGH = 2000;
    private static final long BASIC_PRICE_SHEEN = 1000;
    private static final long BASIC_PRICE_FOOT = 500;

    public Prices() {
        pricesMap = new HashMap<>();

        //sprite 0
        pricesMap.put("pricePlayer0head", 0L);
        pricesMap.put("pricePlayer0belly", 0L);
        pricesMap.put("pricePlayer0armL", 0L);
        pricesMap.put("pricePlayer0foreArmL", 0L);
        pricesMap.put("pricePlayer0handL", 0L);
        pricesMap.put("pricePlayer0armR", 0L);
        pricesMap.put("pricePlayer0foreArmR", 0L);
        pricesMap.put("pricePlayer0handR", 0L);
        pricesMap.put("pricePlayer0thighL", 0L);
        pricesMap.put("pricePlayer0shinL", 0L);
        pricesMap.put("pricePlayer0footL", 0L);
        pricesMap.put("pricePlayer0thighR", 0L);
        pricesMap.put("pricePlayer0shinR", 0L);
        pricesMap.put("pricePlayer0footR", 0L);

        //sprite 1
        pricesMap.put("pricePlayer1head", BASIC_PRICE_HEAD * 2 / 5);
        pricesMap.put("pricePlayer1belly", BASIC_PRICE_BODY / 5);
        pricesMap.put("pricePlayer1armL", BASIC_PRICE_ARM / 5);
        pricesMap.put("pricePlayer1foreArmL", BASIC_PRICE_FOREARM / 5);
        pricesMap.put("pricePlayer1handL", BASIC_PRICE_HAND / 5);
        pricesMap.put("pricePlayer1armR", BASIC_PRICE_ARM / 5);
        pricesMap.put("pricePlayer1foreArmR", BASIC_PRICE_FOREARM / 5);
        pricesMap.put("pricePlayer1handR", BASIC_PRICE_HAND / 5);
        pricesMap.put("pricePlayer1thighL", BASIC_PRICE_THIGH / 5);
        pricesMap.put("pricePlayer1shinL", BASIC_PRICE_SHEEN / 5);
        pricesMap.put("pricePlayer1footL", BASIC_PRICE_FOOT / 5);
        pricesMap.put("pricePlayer1thighR", BASIC_PRICE_THIGH / 5);
        pricesMap.put("pricePlayer1shinR", BASIC_PRICE_SHEEN / 5);
        pricesMap.put("pricePlayer1footR", BASIC_PRICE_FOOT / 5);

        //sprite 2
        pricesMap.put("pricePlayer2head", BASIC_PRICE_HEAD / 5);
        pricesMap.put("pricePlayer2belly", BASIC_PRICE_BODY * 2 / 5);
        pricesMap.put("pricePlayer2armL", BASIC_PRICE_ARM / 5);
        pricesMap.put("pricePlayer2foreArmL", BASIC_PRICE_FOREARM * 3 / 5);
        pricesMap.put("pricePlayer2handL", BASIC_PRICE_HAND / 5);
        pricesMap.put("pricePlayer2armR", BASIC_PRICE_ARM / 5);
        pricesMap.put("pricePlayer2foreArmR", BASIC_PRICE_FOREARM * 3 / 5);
        pricesMap.put("pricePlayer2handR", BASIC_PRICE_HAND / 5);
        pricesMap.put("pricePlayer2thighL", BASIC_PRICE_THIGH / 5);
        pricesMap.put("pricePlayer2shinL", BASIC_PRICE_SHEEN * 3 / 5);
        pricesMap.put("pricePlayer2footL", BASIC_PRICE_FOOT / 5);
        pricesMap.put("pricePlayer2thighR", BASIC_PRICE_THIGH / 5);
        pricesMap.put("pricePlayer2shinR", BASIC_PRICE_SHEEN * 3 / 5);
        pricesMap.put("pricePlayer2footR", BASIC_PRICE_FOOT / 5);

        //sprite 3
        pricesMap.put("pricePlayer3head", BASIC_PRICE_HEAD);
        pricesMap.put("pricePlayer3belly", BASIC_PRICE_BODY * 3);
        pricesMap.put("pricePlayer3armL", BASIC_PRICE_ARM);
        pricesMap.put("pricePlayer3foreArmL", BASIC_PRICE_FOREARM);
        pricesMap.put("pricePlayer3handL", BASIC_PRICE_HAND * 2);
        pricesMap.put("pricePlayer3armR", BASIC_PRICE_ARM);
        pricesMap.put("pricePlayer3foreArmR", BASIC_PRICE_FOREARM);
        pricesMap.put("pricePlayer3handR", BASIC_PRICE_HAND * 2);
        pricesMap.put("pricePlayer3thighL", BASIC_PRICE_THIGH);
        pricesMap.put("pricePlayer3shinL", BASIC_PRICE_SHEEN);
        pricesMap.put("pricePlayer3footL", BASIC_PRICE_FOOT * 2);
        pricesMap.put("pricePlayer3thighR", BASIC_PRICE_THIGH);
        pricesMap.put("pricePlayer3shinR", BASIC_PRICE_SHEEN);
        pricesMap.put("pricePlayer3footR", BASIC_PRICE_FOOT * 2);

        //sprite 4
        pricesMap.put("pricePlayer4head", BASIC_PRICE_HEAD * 8);
        pricesMap.put("pricePlayer4belly", BASIC_PRICE_BODY * 2);
        pricesMap.put("pricePlayer4armL", BASIC_PRICE_ARM * 2);
        pricesMap.put("pricePlayer4foreArmL", BASIC_PRICE_FOREARM * 2);
        pricesMap.put("pricePlayer4handL", BASIC_PRICE_HAND * 2);
        pricesMap.put("pricePlayer4armR", BASIC_PRICE_ARM * 2);
        pricesMap.put("pricePlayer4foreArmR", BASIC_PRICE_FOREARM * 2);
        pricesMap.put("pricePlayer4handR", BASIC_PRICE_HAND * 2);
        pricesMap.put("pricePlayer4thighL", BASIC_PRICE_THIGH * 2);
        pricesMap.put("pricePlayer4shinL", BASIC_PRICE_SHEEN * 2);
        pricesMap.put("pricePlayer4footL", BASIC_PRICE_FOOT * 2);
        pricesMap.put("pricePlayer4thighR", BASIC_PRICE_THIGH * 2);
        pricesMap.put("pricePlayer4shinR", BASIC_PRICE_SHEEN * 2);
        pricesMap.put("pricePlayer4footR", BASIC_PRICE_FOOT * 2);

        //sprite 5
        pricesMap.put("pricePlayer5head", BASIC_PRICE_HEAD * 5);
        pricesMap.put("pricePlayer5belly", BASIC_PRICE_BODY * 5);
        pricesMap.put("pricePlayer5armL", BASIC_PRICE_ARM * 5);
        pricesMap.put("pricePlayer5foreArmL", BASIC_PRICE_FOREARM * 5);
        pricesMap.put("pricePlayer5handL", BASIC_PRICE_HAND * 5);
        pricesMap.put("pricePlayer5armR", BASIC_PRICE_ARM * 5);
        pricesMap.put("pricePlayer5foreArmR", BASIC_PRICE_FOREARM * 5);
        pricesMap.put("pricePlayer5handR", BASIC_PRICE_HAND * 5);
        pricesMap.put("pricePlayer5thighL", BASIC_PRICE_THIGH * 5);
        pricesMap.put("pricePlayer5shinL", BASIC_PRICE_SHEEN * 5);
        pricesMap.put("pricePlayer5footL", BASIC_PRICE_FOOT * 5);
        pricesMap.put("pricePlayer5thighR", BASIC_PRICE_THIGH * 5);
        pricesMap.put("pricePlayer5shinR", BASIC_PRICE_SHEEN * 5);
        pricesMap.put("pricePlayer5footR", BASIC_PRICE_FOOT * 5);

        //sprite 6
        pricesMap.put("pricePlayer6head", BASIC_PRICE_HEAD * 20);
        pricesMap.put("pricePlayer6belly", BASIC_PRICE_BODY * 20);
        pricesMap.put("pricePlayer6armL", BASIC_PRICE_ARM * 20);
        pricesMap.put("pricePlayer6foreArmL", BASIC_PRICE_FOREARM * 20);
        pricesMap.put("pricePlayer6handL", BASIC_PRICE_HAND * 20);
        pricesMap.put("pricePlayer6armR", BASIC_PRICE_ARM * 20);
        pricesMap.put("pricePlayer6foreArmR", BASIC_PRICE_FOREARM * 20);
        pricesMap.put("pricePlayer6handR", BASIC_PRICE_HAND * 20);
        pricesMap.put("pricePlayer6thighL", BASIC_PRICE_THIGH * 20);
        pricesMap.put("pricePlayer6shinL", BASIC_PRICE_SHEEN * 20);
        pricesMap.put("pricePlayer6footL", BASIC_PRICE_FOOT * 20);
        pricesMap.put("pricePlayer6thighR", BASIC_PRICE_THIGH * 20);
        pricesMap.put("pricePlayer6shinR", BASIC_PRICE_SHEEN * 20);
        pricesMap.put("pricePlayer6footR", BASIC_PRICE_FOOT * 20);

        //sprite 7
        pricesMap.put("pricePlayer7head", BASIC_PRICE_HEAD * 25);
        pricesMap.put("pricePlayer7belly", BASIC_PRICE_BODY * 25);
        pricesMap.put("pricePlayer7armL", BASIC_PRICE_ARM * 25);
        pricesMap.put("pricePlayer7foreArmL", BASIC_PRICE_FOREARM * 25);
        pricesMap.put("pricePlayer7handL", BASIC_PRICE_HAND * 25);
        pricesMap.put("pricePlayer7armR", BASIC_PRICE_ARM * 25);
        pricesMap.put("pricePlayer7foreArmR", BASIC_PRICE_FOREARM * 25);
        pricesMap.put("pricePlayer7handR", BASIC_PRICE_HAND * 25);
        pricesMap.put("pricePlayer7thighL", BASIC_PRICE_THIGH * 25);
        pricesMap.put("pricePlayer7shinL", BASIC_PRICE_SHEEN * 25);
        pricesMap.put("pricePlayer7footL", BASIC_PRICE_FOOT * 25);
        pricesMap.put("pricePlayer7thighR", BASIC_PRICE_THIGH * 25);
        pricesMap.put("pricePlayer7shinR", BASIC_PRICE_SHEEN * 25);
        pricesMap.put("pricePlayer7footR", BASIC_PRICE_FOOT * 25);

        //sprite 8
        pricesMap.put("pricePlayer8head", BASIC_PRICE_HEAD * 30);
        pricesMap.put("pricePlayer8belly", BASIC_PRICE_BODY * 30);
        pricesMap.put("pricePlayer8armL", BASIC_PRICE_ARM * 30);
        pricesMap.put("pricePlayer8foreArmL", BASIC_PRICE_FOREARM * 30);
        pricesMap.put("pricePlayer8handL", BASIC_PRICE_HAND * 30);
        pricesMap.put("pricePlayer8armR", BASIC_PRICE_ARM * 30);
        pricesMap.put("pricePlayer8foreArmR", BASIC_PRICE_FOREARM * 30);
        pricesMap.put("pricePlayer8handR", BASIC_PRICE_HAND * 30);
        pricesMap.put("pricePlayer8thighL", BASIC_PRICE_THIGH * 30);
        pricesMap.put("pricePlayer8shinL", BASIC_PRICE_SHEEN * 30);
        pricesMap.put("pricePlayer8footL", BASIC_PRICE_FOOT * 30);
        pricesMap.put("pricePlayer8thighR", BASIC_PRICE_THIGH * 30);
        pricesMap.put("pricePlayer8shinR", BASIC_PRICE_SHEEN * 30);
        pricesMap.put("pricePlayer8footR", BASIC_PRICE_FOOT * 30);

        //sprite 9
        pricesMap.put("pricePlayer9head", BASIC_PRICE_HEAD * 40);
        pricesMap.put("pricePlayer9belly", BASIC_PRICE_BODY * 40);
        pricesMap.put("pricePlayer9armL", BASIC_PRICE_ARM * 40);
        pricesMap.put("pricePlayer9foreArmL", BASIC_PRICE_FOREARM * 40);
        pricesMap.put("pricePlayer9handL", BASIC_PRICE_HAND * 40);
        pricesMap.put("pricePlayer9armR", BASIC_PRICE_ARM * 40);
        pricesMap.put("pricePlayer9foreArmR", BASIC_PRICE_FOREARM * 40);
        pricesMap.put("pricePlayer9handR", BASIC_PRICE_HAND * 40);
        pricesMap.put("pricePlayer9thighL", BASIC_PRICE_THIGH * 40);
        pricesMap.put("pricePlayer9shinL", BASIC_PRICE_SHEEN * 40);
        pricesMap.put("pricePlayer9footL", BASIC_PRICE_FOOT * 40);
        pricesMap.put("pricePlayer9thighR", BASIC_PRICE_THIGH * 40);
        pricesMap.put("pricePlayer9shinR", BASIC_PRICE_SHEEN * 40);
        pricesMap.put("pricePlayer9footR", BASIC_PRICE_FOOT * 40);
    }

    public HashMap<String, Long> getPricesMap() {
        return pricesMap;
    }
}
