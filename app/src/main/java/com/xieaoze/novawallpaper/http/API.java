package com.xieaoze.novawallpaper.http;

import com.xieaoze.novawallpaper.base.BaseBean;
import com.xieaoze.novawallpaper.bean.CategoryBean;
import com.xieaoze.novawallpaper.bean.WallpaperDetailBean;
import com.xieaoze.novawallpaper.bean.WallpaperListBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class API {

    static final String BASE_URL = "http://wallpaper.lasermobi.com/wp-json/";

    public interface NWApi {

        //-----------------------【首页相关】----------------------

        // 获取壁纸分类列表
        @GET("v/cats")
        Observable<BaseBean<List<CategoryBean>>> getCategory();

        // 根据分类ID，页数，每页个数 获取壁纸列表
        @GET("v/cat?")
        Observable<BaseBean<WallpaperListBean>> getWallpaperList(@Query("p") Integer page, @Query("perPage") Integer perPage, @Query("cat") Integer catId);

        // 根据壁纸ID获取壁纸详情
        @GET("v/post?")
        Observable<BaseBean<WallpaperDetailBean>> getWallpaperDetail(@Query("id") Integer id);
    }

}
