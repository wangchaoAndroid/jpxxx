package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create by wangchao on 2018/5/17 10:39
 */
public class PayInfoModel implements Serializable{
    public String id;
    public String object;
    public long created;
    public boolean livemode;
    public boolean paid;
    public boolean refunded;
    public boolean reversed;
    public String app;
    public String channel;
    public String orderNo;
    public String clientIp;
    public int  amount;
    public int amountSettle;
    public String currency;
    public String subject;
    public String body;
    public Object timePaid;
    public long  timeExpire;
    public Object timeSettle;
    public Object transactionNo;
    public Refunds refunds;
    public int  amountRefunded;
    public Object  failureCode;
    public Object  failureMsg;
    public Object  metadata;
    public Credential  credential;
    public Object  extra;
    public Object  description;

    @Override
    public String toString() {
        return "PayInfoModel{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created='" + created + '\'' +
                ", livemode=" + livemode +
                ", paid=" + paid +
                ", refunded=" + refunded +
                ", reversed=" + reversed +
                ", app='" + app + '\'' +
                ", channel='" + channel + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", amount=" + amount +
                ", amountSettle=" + amountSettle +
                ", currency='" + currency + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", timePaid='" + timePaid + '\'' +
                ", timeExpire=" + timeExpire +
                ", timeSettle='" + timeSettle + '\'' +
                ", transactionNo='" + transactionNo + '\'' +
                ", refunds=" + refunds +
                ", amountRefunded=" + amountRefunded +
                ", failureCode='" + failureCode + '\'' +
                ", failureMsg='" + failureMsg + '\'' +
                ", metadata=" + metadata +
                ", credential=" + credential +
                ", extra=" + extra +
                ", description='" + description + '\'' +
                '}';
    }

    public class Refunds implements Serializable{
        public String object;
        public String url;
        public boolean hasMore;
        public List<Object> data;

        @Override
        public String toString() {
            return "Refunds{" +
                    "object='" + object + '\'' +
                    ", url='" + url + '\'' +
                    ", hasMore=" + hasMore +
                    ", data=" + data +
                    '}';
        }
    }

    public class Credential implements Serializable{
        public String object;

        public Alipay alipay;

        @Override
        public String toString() {
            return "Credential{" +
                    "object='" + object + '\'' +
                    ", alipay=" + alipay +
                    '}';
        }
    }

    public class Alipay implements Serializable{
        public String orderInfo;

        @Override
        public String toString() {
            return "Alipay{" +
                    "orderInfo='" + orderInfo + '\'' +
                    '}';
        }
    }
}
