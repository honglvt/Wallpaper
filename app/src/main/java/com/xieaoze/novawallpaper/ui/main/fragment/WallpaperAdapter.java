package com.xieaoze.novawallpaper.ui.main.fragment;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xieaoze.novawallpaper.R;
import com.xieaoze.novawallpaper.bean.WallpaperListBean;

public class WallpaperAdapter extends BaseQuickAdapter<WallpaperListBean.DataBean, BaseViewHolder> implements LoadMoreModule {
    private ImageLoader mImageLoader;

    WallpaperAdapter(int layoutResId, ImageLoader imageLoader) {
        super(layoutResId);
        mImageLoader = imageLoader;
    }

    @Override
    public void addData(WallpaperListBean.DataBean data) {
        super.addData(data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WallpaperListBean.DataBean dataBean) {
        ImageSize targetSize = new ImageSize(540, 960);
        mImageLoader.displayImage(dataBean.img, (ImageView) baseViewHolder.getView(R.id.wallpaper_img), targetSize);
    }
}
