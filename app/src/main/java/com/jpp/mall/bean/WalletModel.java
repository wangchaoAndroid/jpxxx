package com.jpp.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class WalletModel implements Serializable {
    public double balance;
    public int hasMore;
    public int Number;
    public List<Record> records;


    public class Record implements Serializable{
        public int amount;
        public String time;
        public String title;

    }

}
