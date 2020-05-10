package com.xieaoze.novawallpaper.ui.main;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.xieaoze.novawallpaper.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentStatePagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private List<CategoryBean> beans = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    private FragmentManager fragmentManager;

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    void addData(CategoryBean bean) {
        this.beans.add(bean);
        notifyDataSetChanged();
    }

    void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return beans.get(position).name;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @NonNull
    @Override
    public Fragment instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}