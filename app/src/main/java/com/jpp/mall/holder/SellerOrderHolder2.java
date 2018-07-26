package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SellerOrderHolder2 extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView good_name;
    private ImageView good_img;

    public RecyclerView recyclerView;
    public SellerOrderHolder2(View itemView) {
        super(itemView);
        rootView = itemView;
        good_name = (TextView) itemView.findViewById(R.id.good_name);
//        good_price = (TextView) itemView.findViewById(R.id.good_price);
//        good_desc = (TextView) itemView.findViewById(R.id.good_desc);
//        good_color = (TextView) itemView.findViewById(R.id.good_color);
//        good_num = (TextView) itemView.findViewById(R.id.good_num);
        good_img = (ImageView) itemView.findViewById(R.id.good_img);
    }
}
