package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class UserOrderHolderRv extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView good_name,good_price,good_desc;
    public ImageView good_img;
    public UserOrderHolderRv(View itemView) {
        super(itemView);
        rootView = itemView;
        good_name = (TextView) itemView.findViewById(R.id.good_name);
        good_price = (TextView) itemView.findViewById(R.id.good_price);
        good_desc = (TextView) itemView.findViewById(R.id.good_desc);
//        delete_order = (TextView) itemView.findViewById(R.id.delete_order);
//        pay = (TextView) itemView.findViewById(R.id.pay);
//        shop_name = (TextView) itemView.findViewById(R.id.shop_name);
        good_img = (ImageView) itemView.findViewById(R.id.good_img);
    }
}
