package com.jpp.mall.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class DetailModel implements Serializable {
    public String name;
    public String pic;
    public List<Part> parts;
//    public int type;
//    public int minPrice;
//    public int maxPrice;
//    public int price;


//    public String skuId;


    @Override
    public String toString() {
        return "DetailModel{" +
                "name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", parts=" + parts +
                '}';
    }

    public class Part implements Serializable ,IPickerViewData{
        public String goodsId;
        public double price;
        public List<SubSkus> skuList;
        public List<AttrName> attrNames;
        public String name;

        @Override
        public String toString() {
            return "Part{" +
                    "goodsId='" + goodsId + '\'' +
                    ", price='" + price + '\'' +
                    ", skuList=" + skuList +
                    ", attrNames=" + attrNames +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    public class AttrName implements Serializable,IPickerViewData{
        public String name;
        public List<AttrValue> attrValues;

        @Override
        public String toString() {
            return "AttrName{" +
                    "name='" + name + '\'' +
                    ", attrValues=" + attrValues +
                    '}';
        }

        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    public class AttrValue implements Serializable,IPickerViewData{
        public String valueId;
        public String name;

        @Override
        public String toString() {
            return "AttrValue{" +
                    "valueId='" + valueId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public String getPickerViewText() {
            return name;
        }
    }


    public  class SubSkus implements Serializable{

        public String skuId;
        public double price;
        public String valueIds;

        @Override
        public String toString() {
            return "SubSkus{" +
                    "skuId='" + skuId + '\'' +
                    ", price='" + price + '\'' +
                    ", valueIds='" + valueIds + '\'' +
                    '}';
        }
    }

}
