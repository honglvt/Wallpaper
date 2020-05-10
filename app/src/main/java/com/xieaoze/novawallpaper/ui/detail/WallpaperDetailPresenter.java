package com.xieaoze.novawallpaper.ui.detail;

import com.xieaoze.novawallpaper.base.BaseBean;
import com.xieaoze.novawallpaper.base.BaseObserver;
import com.xieaoze.novawallpaper.base.BasePresenter;
import com.xieaoze.novawallpaper.bean.WallpaperDetailBean;

class WallpaperDetailPresenter extends BasePresenter<IWallpaperDetailView> {
    WallpaperDetailPresenter(IWallpaperDetailView baseView) {
        super(baseView);
    }

    void getWallpaperDetail(int id) {
        addDisposable(apiServer.getWallpaperDetail(id), new BaseObserver<BaseBean<WallpaperDetailBean>>(baseView) {

            @Override
            public void onSuccess(BaseBean<WallpaperDetailBean> bean) {
                baseView.getWallpaperDetail(bean.data);
            }

            @Override
            public void onError(String msg) {
                baseView.showFailed(msg);
            }
        });
    }
}
