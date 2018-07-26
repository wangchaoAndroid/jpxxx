package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class UserOrderHolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView order_code,good_price,delete_order,pay,order_time;
    public ImageView good_img;
    public RecyclerView orderRv;
    public UserOrderHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        order_code = (TextView) itemView.findViewById(R.id.order_code);
//        good_price = (TextView) itemView.findViewById(R.id.good_price);
       // good_desc = (TextView) itemView.findViewById(R.id.good_desc);
        delete_order = (TextView) itemView.findViewById(R.id.delete_order);
        pay = (TextView) itemView.findViewById(R.id.pay);
        order_time = (TextView) itemView.findViewById(R.id.order_time);
//        good_img = (ImageView) itemView.findViewById(R.id.good_img);
        orderRv = (RecyclerView) itemView.findViewById(R.id.orderRv);
    }
}
