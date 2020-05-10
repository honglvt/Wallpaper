package com.xieaoze.novawallpaper.http;

import com.xieaoze.novawallpaper.http.cookie.CookiesManager;
import com.xieaoze.novawallpaper.http.gson.BaseConverterFactory;
import com.yechaoa.yutils.YUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitService {
    private volatile static RetrofitService apiRetrofit;
    private API.NWApi apiServer;

    //单例调用
    public static RetrofitService getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new RetrofitService();
                }
            }
        }
        return apiRetrofit;
    }

    //获取api对象
    public API.NWApi getApiService() {
        return apiServer;
    }

    //初始化retrofit
    private RetrofitService() {

        //配置okhttp并设置时间、日志信息和cookies
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager(YUtils.getApplication()))
                .build();

        //关联okhttp并加上rxjava和gson的配置和baseurl
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(BaseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API.BASE_URL)
                .build();

        apiServer = retrofit.create(API.NWApi.class);
    }
}
