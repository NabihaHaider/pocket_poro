package com.bitspice.pocketporo;

import android.content.Context;
import android.content.SharedPreferences;

public class TimeUtils {

    // Calculates the difference between current time and when app is first open using SharedPreferences
    public static long timeSinceOpened(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        long timeSinceFirstOpened = sharedPreferences.getLong("WhenFirstOpen", System.currentTimeMillis());
        return System.currentTimeMillis() - timeSinceFirstOpened;
    }

    // SharedPreferences returns the value of the skin
    public static int getPoroSkin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        int selectedPoroSkin = sharedPreferences.getInt("skin", 0);
        return selectedPoroSkin;
    }

    // Saves the value of the skin in SharedPreferences
    public static void setPoroSkin(Context context, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("skin", value);
        editor.putLong("WhenFirstOpen", System.currentTimeMillis());
        editor.commit();

    }

    // SharedPreferences saves the current time whenever Poro is fed
    public static void fedPoro(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("timeFed", System.currentTimeMillis());
        editor.commit();
    }

    // Calculates the difference between current time and when Poro was last fed using SharedPreferences
    public static long timeSinceLastFed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        long timeSinceLastFed = sharedPreferences.getLong("timeFed", 0);
        return System.currentTimeMillis() - timeSinceLastFed;
    }


    // Reset SharedPreferences
    public static void resetPoro(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("BoolFirstOpen", true);
        editor.putLong("WhenFirstOpen", System.currentTimeMillis());
        editor.putLong("timeFed", 0);
        editor.putInt("skin", 0);
        editor.commit();
    }
}