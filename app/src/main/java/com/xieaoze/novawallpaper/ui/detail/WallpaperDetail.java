package com.xieaoze.novawallpaper.ui.detail;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.multistate.view.MultiStateView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xieaoze.novawallpaper.R;
import com.xieaoze.novawallpaper.base.BaseActivity;
import com.xieaoze.novawallpaper.bean.WallpaperDetailBean;
import com.xieaoze.novawallpaper.utils.CommonUtil;
import com.xieaoze.novawallpaper.widget.FancyButton;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class WallpaperDetail extends BaseActivity<WallpaperDetailPresenter> implements IWallpaperDetailView, MultiStateView.StateListener {

    public static final String WID = "com.xieaoze.novawallpaper.ui.detail.WallpaperDetail.WID";
    private static final String GOOGLE_PLAY = "com.android.vending";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_img)
    ImageView imageView;
    @BindView(R.id.btn_download)
    FancyButton btnDownload;
    @BindView(R.id.btn_rate)
    FancyButton btnRate;
    @BindView(R.id.btn_set)
    FancyButton btnSet;
    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;

    private WallpaperManager wpManager = null;
    private Bitmap bitmap;

    private Handler.Callback mHandler = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.arg1 == -1) {
                startLoading(multiStateView);
                presenter.getWallpaperDetail(getWid());
            }
            return false;
        }
    };

    @Override
    protected WallpaperDetailPresenter createPresenter() {
        return new WallpaperDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 状态页面初始化
        multiStateView.setStateListener(this);
        startLoading(multiStateView);
        TextView errorTextView = multiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.msg);
        errorTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        errorTextView.getPaint().setAntiAlias(true);//抗锯齿
        errorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                Message msg = new Message();
                msg.arg1 = -1;
                mHandler.handleMessage(msg);
            }
        });
    }

    @Override
    protected void initData() {
        wpManager = WallpaperManager.getInstance(this);
        presenter.getWallpaperDetail(getWid());
    }

    public int getWid() {
        return getIntent().getIntExtra(WID, -1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_download, R.id.btn_rate, R.id.btn_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                final RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions.request(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                // 下载图片保存到本地
                                CommonUtil.saveImage(this, bitmap);
                                ToastUtil.showCenterToast(getResources().getString(R.string.save_success));
                            } else {
                                ToastUtil.showCenterToast(getResources().getString(R.string.save_fail));
                            }
                        });

                break;
            case R.id.btn_rate:
                // 去Google play评分
                rateNow(this);
                break;
            case R.id.btn_set:
                // 设置为系统壁纸
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wpManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    } else {
                        wpManager.setBitmap(bitmap);
                    }
                    ToastUtil.showToast(getResources().getString(R.string.set_success));
                } catch (Exception e) {
                    ToastUtil.showToast(getResources().getString(R.string.set_fail));
                    e.printStackTrace();
                }
                break;
        }
    }

    public void rateNow(final Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            intent.setPackage(GOOGLE_PLAY);//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {//没有应用市场，通过浏览器跳转到Google Play
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                if (intent2.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent2);
                } else {
                    //没有Google Play 也没有浏览器
                    ToastUtil.showToast(getResources().getString(R.string.miss_browser));
                }
            }
        } catch (ActivityNotFoundException activityNotFoundException1) {
            LogUtil.e("GoogleMarket Intent not found");
        }
    }

    @Override
    public void showFailed(String errorMessage) {
        loadFail(multiStateView);
        ToastUtil.showCenterToast(errorMessage);
    }

    @Override
    public void getWallpaperDetail(WallpaperDetailBean bean) {
        if (bean.img.isEmpty()) {
            loadEmpty(multiStateView);
        } else {
            imageLoader.displayImage(bean.img, imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    LogUtil.e(failReason.getType() + "");
                    loadFail(multiStateView);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bitmap = loadedImage;
                    loadSuccess(multiStateView);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }

    }

    private void startLoading(MultiStateView multiStateView) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    private void loadFail(MultiStateView multiStateView) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    private void loadSuccess(MultiStateView multiStateView) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    private void loadEmpty(MultiStateView multiStateView) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void onStateChanged(int viewState) {
        LogUtil.e("状态切换: " + viewState);
    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
}
