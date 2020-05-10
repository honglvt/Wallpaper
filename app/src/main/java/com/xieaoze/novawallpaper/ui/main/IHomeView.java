package com.xieaoze.novawallpaper.ui.main;

import com.xieaoze.novawallpaper.base.BaseView;
import com.xieaoze.novawallpaper.bean.CategoryBean;

import java.util.List;

public interface IHomeView extends BaseView {

    void showFailed(String errorMessage);

    void getCategory(List<CategoryBean> beans);
}
