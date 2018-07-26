package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/5/17 10:34
 */
public class PlaceOrderModel implements Serializable {
    public String orderCode;

    @Override
    public String toString() {
        return "PlaceOrderModel{" +
                "orderCode='" + orderCode + '\'' +
                '}';
    }
}
