package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class OrderRvHolder extends RecyclerView.ViewHolder {

    public TextView good_total,order_time,order_code,pay,delete_order;
    public ImageView good_img;
    public View itemView;
    public OrderRvHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        good_total = (TextView) itemView.findViewById(R.id.good_total);
        order_code = (TextView) itemView.findViewById(R.id.order_code);
        order_time = (TextView) itemView.findViewById(R.id.order_time);
        good_img = (ImageView) itemView.findViewById(R.id.good_img);
        pay = (TextView) itemView.findViewById(R.id.pay);
        delete_order = (TextView) itemView.findViewById(R.id.delete_order);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.sell_order_rv);
    }



    public RecyclerView recyclerView;
}
