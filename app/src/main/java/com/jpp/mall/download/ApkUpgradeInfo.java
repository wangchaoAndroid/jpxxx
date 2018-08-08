package com.jpp.mall.download;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/1/15 18:13
 */
public class ApkUpgradeInfo implements Serializable {
    public int code;
    public String msg;
    public String data;


    @Override
    public String toString() {
        return "ApkUpgradeInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
