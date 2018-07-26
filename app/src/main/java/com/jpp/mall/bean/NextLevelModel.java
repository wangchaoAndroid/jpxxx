package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/18.
 */

public class NextLevelModel implements Serializable {
    public String userName;
    public String phone;
    public String shopName;
    public String shopIcon;
    public String grade;

    @Override
    public String toString() {
        return "NextLevelModel{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopIcon='" + shopIcon + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
