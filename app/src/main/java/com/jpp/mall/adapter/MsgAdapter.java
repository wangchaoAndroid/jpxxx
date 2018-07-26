package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jpp.mall.R;
import com.jpp.mall.bean.NotifyModel;
import com.jpp.mall.holder.MsgHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgHolder> {
    private static final String TAG = "BranchAdapter";
    private List<NotifyModel.Msg> datas;
    private Context context;

    private int lastClickItem = -1;

    private List<NotifyModel> holderList = new ArrayList<>();

    public MsgAdapter(List<NotifyModel.Msg> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public MsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg, parent, false);
        MsgHolder volder = new MsgHolder(view);
        return volder;
    }

    private boolean isReadMsg(int isRead){
        return isRead == 1;
    }


    @Override
    public void onBindViewHolder(final MsgHolder holder, final int position) {
        NotifyModel.Msg notifyModel = datas.get(position);
        if(isReadMsg(notifyModel.isRead)){
            holder.tv_msgTitle.setTextColor(context.getResources().getColor(R.color.color_99));
            holder.tv_msgCreateTime.setTextColor(context.getResources().getColor(R.color.color_99));
            holder.tv_msgBody.setTextColor(context.getResources().getColor(R.color.color_99));
            holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.color_99));
            holder.tv_msgNum.setTextColor(context.getResources().getColor(R.color.color_99));

        }else {
            holder.tv_msgTitle.setTextColor(context.getResources().getColor(R.color.color_26));
            holder.tv_msgCreateTime.setTextColor(context.getResources().getColor(R.color.color_26));
            holder.tv_msgBody.setTextColor(context.getResources().getColor(R.color.color_26));
            holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.color_26));
            holder.tv_msgNum.setTextColor(context.getResources().getColor(R.color.color_26));
        }
        holder.tv_msgTitle.setText(notifyModel.title);
        holder.tv_msgCreateTime.setText(notifyModel.createTime);
        holder.tv_msgBody.setText(notifyModel.body);
        switch (notifyModel.msgCode) {
            case 1000:
                holder.tv_msgType.setText("下单通知");
                holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
            case 1001:
                holder.tv_msgType.setText("接单通知");
                holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 1002:
                holder.tv_msgType.setText("拒单通知");
                holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 1003:
                holder.tv_msgType.setText("结单通知");
                holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.color_text_gray));
                break;
            case 1004:
                holder.tv_msgType.setText("快捷下单通知");
                holder.tv_msgType.setTextColor(context.getResources().getColor(R.color.orange));
                break;
        }
        Gson gson = new Gson();
        if(notifyModel.msgCode == 1000 || notifyModel.msgCode == 1001 || notifyModel.msgCode == 1002 || notifyModel.msgCode ==1003){
            NotifyModel.Body msgBody = gson.fromJson(notifyModel.body, NotifyModel.Body.class);
            holder.tv_msgNum.setText(msgBody.orderCode);
            Glide.with(context).load(msgBody.goodsPic).into(holder.iv_msgPic);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(msgBody.goodsName).append("    ");
            for (String desc : msgBody.goodsDesc) {
                stringBuilder.append(desc).append(" ");
            }

            stringBuilder.append("\n").append("备注：").append(msgBody.orderRemark);
            holder.tv_msgBody.setText(stringBuilder.toString());
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }else if(notifyModel.msgCode == 1004){
            holder.iv_msgPic.setVisibility(View.GONE);
            NotifyModel.NotifyModelFast msgBody = gson.fromJson(notifyModel.body, NotifyModel.NotifyModelFast.class);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(msgBody.name).append("    ").append(msgBody.phone);
            holder.tv_msgBody.setText(stringBuilder.toString());
        }


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public interface OnItemClickListener{
        void  onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void notifyAllRb(int lasetClickItem, int position){
        Log.e(TAG, "notifyAllRb: "+ lasetClickItem  + "---" + position);

    }

}
