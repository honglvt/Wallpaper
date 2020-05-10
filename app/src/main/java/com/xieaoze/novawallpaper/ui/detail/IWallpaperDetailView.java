package com.xieaoze.novawallpaper.ui.detail;

import com.xieaoze.novawallpaper.base.BaseView;
import com.xieaoze.novawallpaper.bean.WallpaperDetailBean;

public interface IWallpaperDetailView extends BaseView {

    void showFailed(String errorMessage);

    void getWallpaperDetail(WallpaperDetailBean bean);
}
