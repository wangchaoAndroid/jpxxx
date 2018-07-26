package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/17 0017.
 */

public class SearchModel implements Serializable {
    public String modelId;
    public String name;
    public String pic;

    @Override
    public String toString() {
        return "SearchModel{" +
                "modelId='" + modelId + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
