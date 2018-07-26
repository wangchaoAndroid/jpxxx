package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/5/15 0015.
 */

public class DetailSkuHolder extends RecyclerView.ViewHolder {

    public  View rootView;
    public TextView itemContent;
    public DetailSkuHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        itemContent = (TextView) itemView.findViewById(R.id.tv_item);
    }

}
