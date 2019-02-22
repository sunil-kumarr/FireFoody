package com.example.rapidfood.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.fragment.app.Fragment;


public class PermissionUtils {

    static  final int VERSION_CODE=Build.VERSION_CODES.LOLLIPOP_MR1;
    // Use for checking current version of API of the Devices
    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > VERSION_CODE;
    }

    // use to check for single permission
    public static boolean hasSinglePermission(Activity activity, String permission) {
        // check the version code
        if(activity!=null && permission!=null)
        if (useRunTimePermissions()) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    // use to check multiple permissions granted or not
    public  static  boolean hasMultiplePermissions(Activity pActivity,String[] pPermissions){
        if(useRunTimePermissions()){
            for(String  permission:pPermissions){
              hasSinglePermission(pActivity,permission);
            }
        }
        return true;
    }

    //use to request permissions from Activity
    public static void requestActivityPermissions(Activity activity, String[] permission, int requestCode) {
        if(activity!=null && permission!=null)
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode);
        }
    }
    //use to request permissions from Fragment
    public static void requestFragmentPermissions(Fragment fragment, String[] permission, int requestCode) {
        if(fragment!=null && permission!=null)
        if (useRunTimePermissions()) {
            fragment.requestPermissions(permission, requestCode);
        }
    }

    // show a string to user telling him why this permissions are important for our app
    public static boolean shouldShowRational(Activity activity, String permission) {
        if(activity!=null && permission!=null)
        if (useRunTimePermissions()) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    //Should we ask for permission or permissions are already asked or denied by the user
    public static boolean shouldAskForPermission(Activity activity, String permission) {
        if (useRunTimePermissions()) {
            return !hasSinglePermission(activity, permission) &&
                    (!hasAskedForPermission(activity, permission) ||
                            shouldShowRational(activity, permission));
        }
        return false;
    }

    // Go to App Settings to give permissions to app
    public static void goToAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    // does we have already asked for permissions
    public static boolean hasAskedForPermission(Activity activity, String permission) {
        return PreferenceManager
                .getDefaultSharedPreferences(activity)
                .getBoolean(permission, false);
    }

    //Mark which permissions are already had been asked and granted by the users
    public static void markedPermissionAsAsked(Activity activity, String permission) {
        PreferenceManager
                .getDefaultSharedPreferences(activity)
                .edit()
                .putBoolean(permission, true)
                .apply();
    }
}
