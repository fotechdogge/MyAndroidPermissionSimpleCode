package com.example.dog.demo.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import java.lang.annotation.Documented;

public class Utils {

    public static boolean isSdkVersionHigherM() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M;
    }


    public static boolean isSdkVersionHigherKitkat() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


    /**
     * 取得該 Object 之 Activity.
     *
     * @param object 受識別之 object
     * @return 若無，返回 null.
     */
    public static Activity getActivity(Object object) {
        if (object instanceof Activity)
            return (Activity) object;
        else if (object instanceof Fragment)
            return ((Fragment) object).getActivity();
        else if (object instanceof android.support.v4.app.Fragment)
            return ((android.support.v4.app.Fragment) object).getActivity();

        return null;
    }

    public static android.support.v4.app.FragmentManager getSupportFragmentManager(Object object) {
        if (object instanceof FragmentActivity)
            return ((FragmentActivity) object).getSupportFragmentManager();
        else if (object instanceof android.support.v4.app.Fragment)
            return ((android.support.v4.app.Fragment) object).getFragmentManager();

        return null;
    }

    public static FragmentManager getFragmentManager(Object object) {
        if (object instanceof FragmentActivity)
            return ((FragmentActivity) object).getFragmentManager();
        else if (object instanceof Fragment)
            return ((Fragment) object).getFragmentManager();

        return null;
    }

    /**
     * 檢查 Object 是否為 Activity 或 Fragment，若都不是，則包出異常 Exception.
     *
     * @param object 受識別之 object.
     */
    public static void checkCallingObjectSuitability(Object object) {
        // Make sure Object is an Activity or Fragment
        boolean isActivity        = object instanceof Activity;
        boolean isAppFragment     = object instanceof Fragment;
        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;
        boolean isMinSdkM         = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (!(isSupportFragment || isActivity || (isAppFragment && isMinSdkM))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }
}
