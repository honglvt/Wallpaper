package com.xieaoze.novawallpaper.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.yechaoa.yutils.ActivityUtil;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化
        YUtils.initialize(this);
        //设置打印开关
        LogUtil.setIsLog(true);
        //注册Activity生命周期
        registerActivityLifecycleCallbacks(ActivityUtil.getActivityLifecycleCallbacks());

        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setCustomFragment(true)
                .setExcludeFontScale(true);

    }
}
