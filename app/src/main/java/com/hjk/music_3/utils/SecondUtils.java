package com.hjk.music_3.utils;

public class SecondUtils {
    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        String minuteString="";
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
//        System.out.println("houres:"+ hours);
//        System.out.println("minutes:"+ minutes);
//        System.out.println("seconds:"+ seconds);

        if (hours > 0) {
            finalTimerString = hours + "";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if(minutes<10){
            minuteString="0"+minutes;
            finalTimerString = finalTimerString + minuteString + secondsString;

        }
        else {
            finalTimerString = finalTimerString + minutes + secondsString;
        }

        return finalTimerString;
    }
}
