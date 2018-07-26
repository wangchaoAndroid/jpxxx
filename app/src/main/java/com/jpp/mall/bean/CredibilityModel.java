package com.jpp.mall.bean;

import java.io.Serializable;

/**
 engineerId
 String
 工程师编号
 响应内容
 名称
 类型
 生成规则
 初始值
 简介
 code
 String
 data
 Object
 id
 Number
 工程师id
 engineername
 String
 工程师姓名
 engineerid
 String
 工程师编号
 engineerjpg
 工程师图片
 msg
 */
public class CredibilityModel implements Serializable {
    public String id;
    public String engineerName;
    public String number;
    public String icon;

    @Override
    public String toString() {
        return "CredibilityModel{" +
                "id='" + id + '\'' +
                ", engineername='" + engineerName + '\'' +
                ", number='" + number + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
