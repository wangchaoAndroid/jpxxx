package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SureOrderHolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView good_name,good_price,good_num;
    public ImageView good_img;
    public LinearLayout skull;
    public SureOrderHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        good_name = (TextView) itemView.findViewById(R.id.good_name);
        good_price = (TextView) itemView.findViewById(R.id.good_price);
        good_num = (TextView) itemView.findViewById(R.id.good_num);
        good_img = (ImageView) itemView.findViewById(R.id.good_img);
        skull = (LinearLayout) itemView.findViewById(R.id.sku_ll);
    }
}
