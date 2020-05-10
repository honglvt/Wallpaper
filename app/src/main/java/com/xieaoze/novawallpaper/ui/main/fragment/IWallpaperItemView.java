package com.xieaoze.novawallpaper.ui.main.fragment;

import com.xieaoze.novawallpaper.base.BaseView;
import com.xieaoze.novawallpaper.bean.WallpaperListBean;

public interface IWallpaperItemView extends BaseView {

    void showFailed(String msg);

    void getWallpaper(WallpaperListBean bean);
}
