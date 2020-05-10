package com.xieaoze.novawallpaper.ui.main;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;

import com.xieaoze.novawallpaper.R;
import com.xieaoze.novawallpaper.base.BaseActivity;
import com.xieaoze.novawallpaper.bean.CategoryBean;
import com.xieaoze.novawallpaper.ui.main.fragment.WallpaperItemFragment;
import com.yechaoa.yutils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity<HomePresenter> implements IHomeView {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {
        presenter.getCategory();
    }

    @Override
    public void showFailed(String errorMessage) {
        ToastUtil.showCenterToast(errorMessage);
        presenter.getCategory();
    }

    @Override
    public void getCategory(List<CategoryBean> beans) {
        for (int j = 0; j < beans.size(); j++) {
            sectionsPagerAdapter.addFragment(WallpaperItemFragment.newInstance(beans.get(j).id));
            sectionsPagerAdapter.addData(beans.get(j));
        }
    }

}