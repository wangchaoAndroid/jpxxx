package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.view.AmountView;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CartHolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView good_name,good_price,shop_name;
    public AmountView mAmountView;
    public ImageView good_img;
    public LinearLayout skuLL;
    public CheckBox rb_cart;
    public CartHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        good_name = (TextView) itemView.findViewById(R.id.good_name);
        good_price = (TextView) itemView.findViewById(R.id.good_price);
//        good_desc = (TextView) itemView.findViewById(R.id.good_desc);
//        good_color = (TextView) itemView.findViewById(R.id.good_color);
        mAmountView = (AmountView) itemView.findViewById(R.id.amout_view);
//        shop_name = (TextView) itemView.findViewById(R.id.shop_name);
        skuLL = (LinearLayout) itemView.findViewById(R.id.sku_ll);
        good_img = (ImageView) itemView.findViewById(R.id.good_img);
        rb_cart = (CheckBox) itemView.findViewById(R.id.rb_cart);
    }
}
