package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SearchHolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView branchTv;
    public ImageView imageView;
    public SearchHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        branchTv = (TextView) itemView.findViewById(R.id.branch_tv);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
