package com.jpp.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.bean.SellerOrderModel;
import com.jpp.mall.holder.SellerOrderHolder4;
import com.jpp.mall.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SellerOrderAdapter4 extends RecyclerView.Adapter<SellerOrderHolder4> {
    private List<SellerOrder.Data> datas;
    private Context context;
    private int pos;

    public SellerOrderAdapter4(List<SellerOrder.Data> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public SellerOrderHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(context).inflate(R.layout.item_seller_order4, parent, false);
        SellerOrderHolder4 volder = new SellerOrderHolder4(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final SellerOrderHolder4 holder, final int position) {
       // holder.branchTv.setText(datas.get(position).brandName);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    holder.rootView.setBackgroundColor(Color.WHITE);
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
