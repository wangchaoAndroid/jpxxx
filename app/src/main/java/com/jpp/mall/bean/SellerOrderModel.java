package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/25.
 */

public class SellerOrderModel implements Serializable {
    public String sellerName;

    public String phoneImg;

    public String goodName;

    public String des;

    public String color;

    public String money;

    public String num;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getPhoneImg() {
        return phoneImg;
    }

    public void setPhoneImg(String phoneImg) {
        this.phoneImg = phoneImg;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "SellerOrderModel{" +
                "sellerName='" + sellerName + '\'' +
                ", phoneImg='" + phoneImg + '\'' +
                ", goodName='" + goodName + '\'' +
                ", des='" + des + '\'' +
                ", color='" + color + '\'' +
                ", money='" + money + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
