package com.vgaw.nrfconnect.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caojin
 * @date 2016/10/24
 */

public class ContextUtil {
    private static Context mContext;
    private static Handler mHandler;
    private static WeakReference<Activity> mCurrentActivityWeakRef;
    private static List<Activity> activityList = new ArrayList<>();

    public static void init(Context context) {
        mContext = context;
        mHandler = new Handler();
    }

    public static Context getApplicationContext() {
        return mContext;
    }

    public static void setCurrentActivity(Activity activity){
        mCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    public static Activity getCurrentActivity() {
        if (mCurrentActivityWeakRef != null){
            return mCurrentActivityWeakRef.get();
        }
        return  null;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void removeRemainingActivity(Activity me) {
        for (Activity activity : activityList) {
            if (activity != me) {
                activity.finish();
            }
        }
    }

    public static void removeAllActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public static void release() {
        activityList.clear();
        mCurrentActivityWeakRef = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    public static int getDimen(@DimenRes int id) {
        return getApplicationContext().getResources().getDimensionPixelSize(id);
    }

    public static int getColor(@ColorRes int id) {
        return getApplicationContext().getResources().getColor(id);
    }

    public static String getString(@StringRes int id) {
        return getApplicationContext().getResources().getString(id);
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return getApplicationContext().getResources().getDrawable(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return getApplicationContext().getResources().getStringArray(id);
    }
}

