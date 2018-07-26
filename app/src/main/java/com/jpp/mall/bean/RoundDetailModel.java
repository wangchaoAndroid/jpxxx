package com.jpp.mall.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/15 0015.
 */

public class RoundDetailModel implements Serializable {
    public String goodsId;
    public double price;
    public String name;
    public String pic;

    public List<Sku> skuList;

    public List<AttrName> attrNames;


    @Override
    public String toString() {
        return "RoundDetailModel{" +
                "goodsId='" + goodsId + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", skuList=" + skuList +
                ", attrNames=" + attrNames +
                '}';
    }

    public class Sku implements Serializable{
        public String skuId;
        public double price;
        public String valueIds;

        @Override
        public String toString() {
            return "Sku{" +
                    "skuId='" + skuId + '\'' +
                    ", price='" + price + '\'' +
                    ", valueIds='" + valueIds + '\'' +
                    '}';
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

    public class AttrValue implements Serializable ,IPickerViewData{
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
}
