package com.unionbankng.future.futurejobservice.util;

public class Utility {

    public static String convertMinutesToWords(int time){

        String timeInWords = "";

        int days = time/24/60;
        int minutes = time/60%24;
        int seconds =  time%60;

        if(days > 0)
            timeInWords = timeInWords.concat(days + (days > 1 ? " days ":" day "));

        if(minutes > 0)
            timeInWords = timeInWords.concat(minutes + (minutes > 1 ? " hours ":" hour "));

        if(seconds > 0)
            timeInWords = timeInWords.concat(seconds + (seconds > 1 ? " minutes":" minute"));

        return timeInWords;

    }

}
