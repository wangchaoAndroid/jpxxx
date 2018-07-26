package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/20 0020.
 */

public class OrdelDetail implements Serializable {
    public int orderId;
    public int orderType;
    public String orderCode;
    public double sumPrice;
    public String createTime;
    public String payTime;
    public String completeTime;
    public AddressInfo addressInfo;
    public ShopInfo shopInfo;
    public List<Detail> details;
    public MemberInfo memberInfo;
    public ExpressInfo expressInfo;

    @Override
    public String toString() {
        return "OrdelDetail{" +
                "orderId=" + orderId +
                ", orderType=" + orderType +
                ", orderCode='" + orderCode + '\'' +
                ", sumPrice=" + sumPrice +
                ", createTime='" + createTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", completeTime='" + completeTime + '\'' +
                ", addressInfo=" + addressInfo +
                ", shopInfo=" + shopInfo +
                ", details=" + details +
                ", memberInfo=" + memberInfo +
                ", expressInfo=" + expressInfo +
                '}';
    }

    public class ExpressInfo implements Serializable{
        public String expressNumber;
        public String expressName;
        public String expressId;
    }

    public class Detail implements Serializable{
        public double price;
        public int num;
        public String name;
        public String pic;
        public List<String> desc;

        @Override
        public String toString() {
            return "Detail{" +
                    "price=" + price +
                    ", num=" + num +
                    ", name='" + name + '\'' +
                    ", pic='" + pic + '\'' +
                    ", desc=" + desc +
                    '}';
        }
    }

    public class  MemberInfo implements Serializable{
        public String name;
        public String pic;
        public String phone;
    }

    public class AddressInfo implements Serializable{
        public String address;
        public String receiver;
        public String phone;

        @Override
        public String toString() {
            return "AddressInfo{" +
                    "address='" + address + '\'' +
                    ", receiver='" + receiver + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    public class ShopInfo implements Serializable{
        public String pic;
        public String name;
        public String phone;
        public String fullAddress;

        @Override
        public String toString() {
            return "ShopInfo{" +
                    "pic='" + pic + '\'' +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", fullAddress='" + fullAddress + '\'' +
                    '}';
        }
    }
}
