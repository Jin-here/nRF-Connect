package com.vgaw.nrfconnect.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public final class DensityUtil {

    private static float density = -1f;
    private static float scaledDensity = -1f;

    private DensityUtil() {
    }

    public static float getDensity() {
        if (density == -1) {
            density = ContextUtil.getApplicationContext().getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static float getScaledDensity(){
        if (scaledDensity == -1){
            scaledDensity = ContextUtil.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        }
        return scaledDensity;
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5f);
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    public static int sp2px(float spValue){
        return (int) (spValue * getScaledDensity() + 0.5f);
    }

    public static int px2sp(float pxValue){
        return (int) (pxValue / getScaledDensity() + 0.5f);
    }

    public static int getScreenWidth() {
        Resources res = ContextUtil.getApplicationContext().getResources();
        if (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return res.getDisplayMetrics().widthPixels;
        }
        return res.getDisplayMetrics().widthPixels + getNavigationBarHeight();
    }


    public static int getScreenHeight() {
        return ContextUtil.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        Context context = ContextUtil.getApplicationContext();
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取导航栏高度
     * @return
     */
    public static int getNavigationBarHeight() {
        Context context = ContextUtil.getApplicationContext();
        if (checkDeviceHasNavigationBar()){
            int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid !=0 ){
                int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean checkDeviceHasNavigationBar() {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(ContextUtil.getApplicationContext())
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }
}
