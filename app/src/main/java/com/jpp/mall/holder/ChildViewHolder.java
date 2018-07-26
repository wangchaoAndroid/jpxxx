package com.jpp.mall.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.ChildAdapter;
import com.jpp.mall.adapter.ChildPartAdapter;
import com.jpp.mall.bean.DataBean;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ChildViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;
    private TextView childLeftText;
    private TextView childRightText;
    private RecyclerView childRv;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final DataBean dataBean, final int pos){

        childLeftText = (TextView) view.findViewById(R.id.child_left_text);
        childRightText = (TextView) view.findViewById(R.id.child_right_text);
        childLeftText.setText(dataBean.getChildLeftTxt());
        childRightText.setText(dataBean.getChildRightTxt());
        childRv = (RecyclerView)view.findViewById(R.id.child_rv);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(mContext);
        childRv.setLayoutManager(linearLayoutManager);
        if(pos == 0){
            ChildPartAdapter childPartAdapter = new ChildPartAdapter(dataBean.getParts(),mContext);
            childRv.setAdapter(childPartAdapter);
        }else {
            ChildAdapter childAdapter = new ChildAdapter(dataBean.getAttrNames(),mContext);
            childRv.setAdapter(childAdapter);
        }




    }
}
