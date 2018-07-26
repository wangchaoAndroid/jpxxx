package com.jpp.mall.bean;


import com.jpp.mall.view.citypickerview.bean.CityBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class CityModel implements Serializable {
    public String code;

    public List<Data> data;

    public String msg;

    @Override
    public String toString() {
        return "CityModel{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class Data implements Serializable{
        public String name;
        public String regionId;
        public List<CityBean> cities;

        @Override
        public String toString() {
            return "Data{" +
                    "name='" + name + '\'' +
                    ", regionId='" + regionId + '\'' +
                    ", cities=" + cities +
                    '}';
        }
    }

    public class City implements Serializable{
        public String regionId;
        public String name;
        public List<Area> areas;

        @Override
        public String toString() {
            return "City{" +
                    "regionId='" + regionId + '\'' +
                    ", name='" + name + '\'' +
                    ", areas=" + areas +
                    '}';
        }
    }

    public class Area implements Serializable{
        public String regionId;
        public String name;

        @Override
        public String toString() {
            return "Area{" +
                    "regionId='" + regionId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
