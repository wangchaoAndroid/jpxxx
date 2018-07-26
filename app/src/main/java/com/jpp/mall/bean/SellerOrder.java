package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class SellerOrder implements Serializable {
    public String code;

    public List<Data> data;

    public String msg;

    @Override
    public String toString() {
        return "SellerOrder{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class  Data implements  Serializable {
        public int orderType;
        public double sumPrice;
        public String orderCode;
        public String createTime;
        public String orderId;
        public List<Detail> details;

        @Override
        public String toString() {
            return "Data{" +
                    "orderCode='" + orderCode + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", orderId=" + orderId +
                    ", orderType=" + orderType +
                    ", sumPrice=" + sumPrice +
                    ", details=" + details +
                    '}';
        }
    }

        public class  Detail implements Serializable{
            public  double  price;
            public  int  num;
            public  String  name;
            public  String  pic;
            public  List<String> desc;

            @Override
            public String toString() {
                return "Detail{" +
                        "price=" + price +
                        ", num=" + num +
                        ", name='" + name + '\'' +
                        ", pic='" + pic + '\'' +
                        ", desc=" + desc +
                        '}';
            }
        }


}
