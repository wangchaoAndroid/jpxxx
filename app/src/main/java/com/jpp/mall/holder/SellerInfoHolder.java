package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SellerInfoHolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView sellerName;
    public TextView sellerNum;
    public ImageView sellerAvater;
    public SellerInfoHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        sellerName = (TextView) itemView.findViewById(R.id.seller_name);
        sellerNum = (TextView) itemView.findViewById(R.id.seller_num);
        sellerAvater = (ImageView) itemView.findViewById(R.id.seller_avater);
    }
}
