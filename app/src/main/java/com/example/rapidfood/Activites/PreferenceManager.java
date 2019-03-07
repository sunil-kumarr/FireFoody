package com.example.rapidfood.Activites;

import android.content.Context;
import android.content.SharedPreferences;

class PreferenceManager {

    //Class to save Preference
    // So that the intro screen is shown only once
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    Context context;

    private static final String FIRST_LAUNCH = "firstLaunch";
    int MODE = 0;
    private static final String PREFERENCE = "Javapapers";
    public static final String USER_TYPE="USER_TYPE";

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE, MODE);
        spEditor = sharedPreferences.edit();
    }

    //Save the Key-value pair  FirstLaunch = TRUE/FALSE
    public void setFirstTimeLaunch(boolean isFirstTime) {
        spEditor.putBoolean(FIRST_LAUNCH, isFirstTime);
        spEditor.commit();
    }
    //Get the Saved value For FirstLaunch checking
    public boolean FirstLaunch() {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true);
    }
}
