package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class MsgHolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView tv_msgNum,tv_msgCreateTime,tv_msgTitle,tv_msgType,tv_msgBody;
    public ImageView iv_msgPic;
//    public LinearLayout skuLL;
    public MsgHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        tv_msgNum = (TextView) itemView.findViewById(R.id.tv_msgNum);
        tv_msgCreateTime = (TextView) itemView.findViewById(R.id.tv_msgCreateTime);
        tv_msgType = (TextView) itemView.findViewById(R.id.tv_msgType);
        tv_msgBody = (TextView) itemView.findViewById(R.id.tv_msgBody);
        tv_msgTitle = (TextView) itemView.findViewById(R.id.tv_msgTitle);
        iv_msgPic = (ImageView) itemView.findViewById(R.id.iv_msgPic);
    }
}
