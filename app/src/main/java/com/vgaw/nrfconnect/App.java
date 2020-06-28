package com.vgaw.nrfconnect;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.vgaw.nrfconnect.data.MyObjectBox;
import com.vgaw.nrfconnect.util.ContextUtil;
import com.vgaw.nrfconnect.util.Utils;

import io.objectbox.BoxStore;

import static android.os.Process.myPid;

/**
 * @author caojin
 * @date 2018/2/28
 */

public class App extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.init(this);
        boxStore = MyObjectBox.builder().androidContext(App.this).build();

        // 手机右键退出程序再打开不会再走此处，除非任务管理器关闭
        if (uiProcess()){
            initBugly();
        }
    }

    public BoxStore getBoxStore() {
        return this.boxStore;
    }

    private boolean uiProcess(){
        int pid = myPid();
        String processName = "";
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        String packageName = getPackageName();
        return processName.equals(packageName);
    }

    private void initBugly() {
        Beta.autoDownloadOnWifi = true;
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog_layout;
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                TextView upgradeDialogTitle = view.findViewById(R.id.upgrade_dialog_title);
                TextView upgradeDialogSize = view.findViewById(R.id.upgrade_dialog_size);
                TextView upgradeDialogFeature = view.findViewById(R.id.upgrade_dialog_feature);
                upgradeDialogTitle.setText(getString(R.string.upgrade_dialog_title, upgradeInfo.versionName));
                int fileSize = (int) (upgradeInfo.fileSize / 1024 / 1024);
                upgradeDialogSize.setText(getString(R.string.upgrade_dialog_size, fileSize));
                upgradeDialogFeature.setText(upgradeInfo.newFeature);
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {}

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {}

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {}

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {}

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {}
        };

        // check for upgrade manually
        // isManual: whether clicked by user.
        // isSilence: whether showDialog dialog and toast.
        // Beta.checkUpgrade(true, false);
        Beta.enableHotfix = false;

        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(this);
        //userStrategy.setAppChannel();
        //userStrategy.setAppReportDelay(10 * 1000);
        try {
            PackageInfo packageInfo = Utils.getPackageInfo(this);
            userStrategy.setAppVersion(packageInfo.versionName);
            userStrategy.setAppPackageName(packageInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // for bugly bug report only
        //CrashReport.initCrashReport(getApplicationContext(), getString(R.string.bugly_appid), BuildConfig.DEBUG, userStrategy);
        Bugly.init(getApplicationContext(), getString(R.string.bugly_appid), false, userStrategy);
    }
}
