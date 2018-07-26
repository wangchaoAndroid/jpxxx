package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class MainVolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView itemContent,red_point;
    public ImageView itemImag;
    public MainVolder(View itemView) {
        super(itemView);
        rootView = itemView;
        itemContent = (TextView) itemView.findViewById(R.id.tv_item);
        red_point = (TextView) itemView.findViewById(R.id.red_point);
        itemImag = (ImageView) itemView.findViewById(R.id.iv_item);
    }
}
