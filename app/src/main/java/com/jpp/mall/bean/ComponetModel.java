package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class ComponetModel implements Serializable {

    public int hasMore;

    public List<Component> goodsList;

    public class Component implements Serializable{
        public String goodsId;
        public double price;
        public String name;
        public String pic;

        @Override
        public String toString() {
            return "Component{" +
                    "goodsId='" + goodsId + '\'' +
                    ", price='" + price + '\'' +
                    ", name='" + name + '\'' +
                    ", pic='" + pic + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ComponetModel{" +
                "hasMore=" + hasMore +
                ", goodsList=" + goodsList +
                '}';
    }
}
