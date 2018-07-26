package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class BrandModel implements Serializable {
   public String code;

   public List<Data> data;

   public String msg;

   public class Data implements Serializable{
       public List<Model> models;

       public String brandName;

       public String brandId;

       public String brandLogo;

       @Override
       public String toString() {
           return "Data{" +
                   "models=" + models +
                   ", brandName='" + brandName + '\'' +
                   ", brandId='" + brandId + '\'' +
                   '}';
       }
   }

    public class Model implements Serializable{
        public String modelName;

        public String modelId;

        public String modelPic;


        @Override
        public String toString() {
            return "Model{" +
                    "modelName='" + modelName + '\'' +
                    ", modelId='" + modelId + '\'' +
                    ", modelPic='" + modelPic + '\'' +
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
