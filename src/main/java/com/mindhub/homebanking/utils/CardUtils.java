package com.mindhub.homebanking.utils;

public final class CardUtils {

    public static  Integer  getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max-min))+min);
    }
    public static int generateCVV() {
        return getRandomNumber(100,999);
    }
    public static String createRandomCard(){
        return getRandomNumber(1000,9999).toString()+"-"+getRandomNumber(1000,9999).toString()+"-"+getRandomNumber(1000,9999).toString()+"-"+getRandomNumber(1000,9999).toString();
    }


}