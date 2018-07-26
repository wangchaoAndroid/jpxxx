package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jpp.mall.bean.NextLevelModel;
import com.jpp.mall.holder.NextLevelVolder;
import com.jpp.mall.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class NextLevelAdapter extends RecyclerView.Adapter<NextLevelVolder> {
    private List<NextLevelModel> datas;
    private Context context;

    public NextLevelAdapter(List<NextLevelModel> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public NextLevelVolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_next_level,parent,false);
        NextLevelVolder volder = new NextLevelVolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(NextLevelVolder holder, final int position) {
        holder.walletMoney.setText( "" + datas.get(position).phone);
        holder.walletTime.setText("" + datas.get(position).userName);
        holder.walletType.setText("" + datas.get(position).shopName);
        holder.walletGrade.setText("店铺等级:" + datas.get(position).grade);
        Glide.with(context).load(datas.get(position).shopIcon).into(holder.shop_icon);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
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
}
