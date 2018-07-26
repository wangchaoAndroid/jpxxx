package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class ShopInfoModel implements Serializable {
    public ShopInfo  shopInfo;
    public String phone;
    public String name;
    public String icon;

    @Override
    public String toString() {
        return "ShopInfoModel{" +
                "shopInfo=" + shopInfo +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public class ShopInfo implements Serializable{
        public String fullAddress;
        public String grade;
        public String name;
        public String logo;
        public String shopId;
        public String inviteCode;

        @Override
        public String toString() {
            return "ShopInfo{" +
                    "fullAddress='" + fullAddress + '\'' +
                    ", grade='" + grade + '\'' +
                    ", name='" + name + '\'' +
                    ", logo='" + logo + '\'' +
                    ", shopId='" + shopId + '\'' +
                    ", inviteCode='" + inviteCode + '\'' +
                    '}';
        }
    }


}
