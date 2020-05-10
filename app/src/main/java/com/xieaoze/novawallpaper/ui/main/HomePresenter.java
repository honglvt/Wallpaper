package com.xieaoze.novawallpaper.ui.main;

import com.xieaoze.novawallpaper.base.BaseBean;
import com.xieaoze.novawallpaper.base.BaseObserver;
import com.xieaoze.novawallpaper.base.BasePresenter;
import com.xieaoze.novawallpaper.bean.CategoryBean;

import java.util.List;

class HomePresenter extends BasePresenter<IHomeView> {
    HomePresenter(IHomeView baseView) {
        super(baseView);
    }

    void getCategory() {
        addDisposable(apiServer.getCategory(), new BaseObserver<BaseBean<List<CategoryBean>>>(baseView,true) {

            @Override
            public void onSuccess(BaseBean<List<CategoryBean>> res) {
                baseView.getCategory(res.data);
            }

            @Override
            public void onError(String msg) {
                baseView.showFailed(msg);
            }
        });
    }
}
