package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class CommonModel implements Serializable {
    public String code;

    public String msg;


    @Override
    public String toString() {
        return "CommonModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
