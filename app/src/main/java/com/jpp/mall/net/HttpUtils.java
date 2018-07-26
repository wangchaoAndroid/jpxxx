package com.jpp.mall.net;

/**
 * Created by Administrator on 2018/4/24.
 */

public class HttpUtils {

    private static HttpUtils instance;

    public static HttpUtils getInstance(){
        if(instance == null){
            synchronized (HttpUtils.class){
                if(instance == null){
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

}
