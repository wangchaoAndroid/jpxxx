package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23 0023.
 */

public class NotifyModel implements Serializable {


    public int hasMore;

    public List<Msg> msgList;

    @Override
    public String toString() {
        return "NotifyModel{" +
                "hasMore=" + hasMore +
                ", msgList=" + msgList +
                '}';
    }

    public class Msg implements Serializable{
        public String createTime;
        public int isRead;
        public int msgId;
        public String title;
        public String body;
        public int msgCode;

        @Override
        public String toString() {
            return "Msg{" +
                    "createTime='" + createTime + '\'' +
                    ", isRead=" + isRead +
                    ", msgId=" + msgId +
                    ", title='" + title + '\'' +
                    ", body='" + body + '\'' +
                    ", msgCode=" + msgCode +
                    '}';
        }
    }

    public class NotifyModelFast implements Serializable {
        public String name;
        public String phone;
    }




    public class Body implements Serializable{
        public String orderId;
        public String orderCode;
        public String  goodsName;
        public String   goodsPic;
        public List<String> goodsDesc;
        public String orderRemark;

        @Override
        public String toString() {
            return "Body{" +
                    "orderId='" + orderId + '\'' +
                    ", orderCode='" + orderCode + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsPic='" + goodsPic + '\'' +
                    ", goodsDesc=" + goodsDesc +
                    ", orderRemark='" + orderRemark + '\'' +
                    '}';
        }
    }
}
