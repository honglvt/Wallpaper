package com.xieaoze.novawallpaper.ui.main.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.multistate.view.MultiStateView;
import com.xieaoze.novawallpaper.R;
import com.xieaoze.novawallpaper.base.BaseFragment;
import com.xieaoze.novawallpaper.bean.WallpaperListBean;
import com.xieaoze.novawallpaper.ui.detail.WallpaperDetail;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class WallpaperItemFragment extends BaseFragment<WallpaperItemPresenter> implements IWallpaperItemView, OnItemClickListener, OnLoadMoreListener, MultiStateView.StateListener {

    private static final String ARG_SECTION_NUMBER = "select catId";
    private int TOTAL_COUNTER; // 壁纸总数
    private int page = 1; // 页数
    private int catId;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;


    private List<WallpaperListBean.DataBean> wallPaperList = new ArrayList<>();
    private WallpaperAdapter wallpaperAdapter;

    private BaseLoadMoreModule baseLoadMoreModule;
    private Handler.Callback mHandler = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.arg1 == -1) {
                startLoading(multiStateView);
                presenter.getWallpaper(catId, page, 10);
            }
            return false;
        }
    };

    public static WallpaperItemFragment newInstance(int catId) {
        WallpaperItemFragment fragment = new WallpaperItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, catId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected WallpaperItemPresenter createPresenter() {
        return new WallpaperItemPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        // 下拉刷新初始化
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading(multiStateView);
                page = 1;
                wallPaperList = new ArrayList<>();
                wallpaperAdapter.setList(wallPaperList);
                presenter.getWallpaper(catId, page, 10);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // 状态页面初始化
        multiStateView.setStateListener(this);
        startLoading(multiStateView);
        TextView errorTextView =
                multiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.msg);
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

        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        wallpaperAdapter = new WallpaperAdapter(R.layout.item_wallpaper_list, imageLoader);
        mRecyclerView.setAdapter(wallpaperAdapter);
        wallpaperAdapter.setOnItemClickListener(this);
        baseLoadMoreModule = wallpaperAdapter.getLoadMoreModule();
        baseLoadMoreModule.setEnableLoadMore(true);
        baseLoadMoreModule.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        assert getArguments() != null;
        catId = getArguments().getInt(ARG_SECTION_NUMBER);
        presenter.getWallpaper(catId, page, 10);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showFailed(String msg) {
        loadFail(multiStateView);
        ToastUtil.showCenterToast(msg);
    }

    @Override
    public void getWallpaper(WallpaperListBean bean) {
        if (bean.data.size() > 0) {
            loadSuccess(multiStateView);
        } else {
            loadEmpty(multiStateView);
        }
        TOTAL_COUNTER = bean.total;
        wallPaperList.addAll(bean.data);
        for (int i = 0; i < bean.data.size(); i++) {
            wallpaperAdapter.addData(bean.data.get(i));
        }
        baseLoadMoreModule.loadMoreComplete();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(mContext, WallpaperDetail.class);
        intent.putExtra(WallpaperDetail.WID, wallPaperList.get(position).id);
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        mRecyclerView.postDelayed(() -> {
            if (wallPaperList.size() >= TOTAL_COUNTER) {
                //Data are all loaded.
                baseLoadMoreModule.loadMoreEnd();
            } else {
                page++;
                presenter.getWallpaper(catId, page, 10);
            }
        }, 1000);
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

}