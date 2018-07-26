package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/25.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {
    public View rootView;
    public TextView phone_name;
    public TextView phone_price;
    public ImageView phone_iv;

    public CategoryHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        phone_name = (TextView) itemView.findViewById(R.id.phone_name);
        phone_price = (TextView) itemView.findViewById(R.id.phone_price);
        phone_iv= (ImageView) itemView.findViewById(R.id.phone_iv);
    }
}
