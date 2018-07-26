package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.bean.CredibilityModel;
import com.jpp.mall.holder.SellerInfoHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CredibilityAdapter extends RecyclerView.Adapter<SellerInfoHolder> {
    private List<CredibilityModel> datas;
    private Context context;

    public CredibilityAdapter(List<CredibilityModel> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public SellerInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller_info, parent, false);
        SellerInfoHolder volder = new SellerInfoHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final SellerInfoHolder holder, final int position) {
        holder.sellerName.setText("姓名:" + datas.get(position).engineerName);
        holder.sellerNum.setText("员工编号:" +datas.get(position).number);
        Glide.with(context).load(datas.get(position).icon).into(holder.sellerAvater);
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
