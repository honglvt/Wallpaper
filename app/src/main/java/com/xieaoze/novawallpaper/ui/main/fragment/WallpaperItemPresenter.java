package com.xieaoze.novawallpaper.ui.main.fragment;

import com.xieaoze.novawallpaper.base.BaseBean;
import com.xieaoze.novawallpaper.base.BaseObserver;
import com.xieaoze.novawallpaper.base.BasePresenter;
import com.xieaoze.novawallpaper.bean.WallpaperListBean;

class WallpaperItemPresenter extends BasePresenter<IWallpaperItemView> {

    WallpaperItemPresenter(IWallpaperItemView baseView) {
        super(baseView);
    }

    void getWallpaper(int catId, int page, int perPage) {
        addDisposable(apiServer.getWallpaperList(page, perPage, catId), new BaseObserver<BaseBean<WallpaperListBean>>(baseView) {

            @Override
            public void onSuccess(BaseBean<WallpaperListBean> res) {
                baseView.getWallpaper(res.data);
            }

            @Override
            public void onError(String msg) {
                baseView.showFailed(msg);
            }
        });
    }
}
