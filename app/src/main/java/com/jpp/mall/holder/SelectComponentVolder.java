package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SelectComponentVolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView itemContent;
    public SelectComponentVolder(View itemView) {
        super(itemView);
        rootView = itemView;
        itemContent = (TextView) itemView.findViewById(R.id.tv_item);
    }
}
