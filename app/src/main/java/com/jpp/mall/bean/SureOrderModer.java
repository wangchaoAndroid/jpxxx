package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class SureOrderModer implements Serializable{
    public String code;

    public List<CartOrder> data;

    public String msg;

    public class  CartOrder implements Serializable{
        public String createTime;
        public double price;
        public String cartId;
        public int isInvalid;
        public int num;
        public String name;
        public String pic;
        public String skuId;

        public String goodsId;


        public List<String> desc;


        @Override
        public String toString() {
            return "CartOrder{" +
                    "createTime='" + createTime + '\'' +
                    ", price=" + price +
                    ", cartId='" + cartId + '\'' +
                    ", num=" + num +
                    ", name='" + name + '\'' +
                    ", pic='" + pic + '\'' +
                    ", skuId='" + skuId + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", desc=" + desc +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BrandModel{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }


}
