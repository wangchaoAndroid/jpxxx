package com.jpp.mall.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpp.mall.App;
import com.jpp.mall.bean.User;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/24.
 */

public class RetrofitFactory {

//    public static final String BASE_URL = "http://106.15.200.22/api/";
    public static final String BASE_URL = "https://api.jipaopao.cn/";

    private static final long TIMEOUT = 30;

    private static final String CACHE_NAME = "net_cache";
    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            // 添加通用的Header
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    File cacheFile = new File(App.getAppContext().getExternalCacheDir(), CACHE_NAME);
                    //生成缓存，50M
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
                    //缓存拦截器
                    RequestBody body = chain.request().body();
                    Request request = chain.request();
                    Logger.e("url",request.url().toString());
                    //网络不可用
                    if (!NetworkUtils.isConnected()) {
                        //在请求头中加入：强制使用缓存，不访问网络
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    //网络可用
                    if (NetworkUtils.isConnected()) {
                        int maxAge = 0;
                        // 有网络时 在响应头中加入：设置缓存超时时间0个小时
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .build();
                    } else {
                        // 无网络时，在响应头中加入：设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .build();
                    }
                    return response;
                }

            })
            /*
            这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {

                }
            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static RetrofitServices retrofitService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create(buildGson()))
            // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitServices.class);

    public static RetrofitServices getInstance() {
        return retrofitService;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                // 此处可以添加Gson 自定义TypeAdapter
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .create();
    }
}

