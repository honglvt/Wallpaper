package com.xieaoze.novawallpaper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xieaoze.novawallpaper.base.BaseActivity;
import com.xieaoze.novawallpaper.base.BasePresenter;
import com.xieaoze.novawallpaper.ui.main.HomeActivity;

public class Splash extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

    }

    @Override
    protected void initData() {
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(3000);//使程序休眠五秒
                    Intent it = new Intent(getApplicationContext(), HomeActivity.class);//启动MainActivity
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程

    }
}
