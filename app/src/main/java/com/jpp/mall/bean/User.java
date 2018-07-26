package com.jpp.mall.bean;

/**
 * Created by Administrator on 2018/4/24.
 */

public class User {
    public String phone;

    public String password;


    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
