package com.jpp.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.holder.SellerOrderHolder2;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SellerOrderAdapter2 extends RecyclerView.Adapter<SellerOrderHolder2> {
    private List<SellerOrder.Data> datas;
    private Context context;
    private int pos;

    public SellerOrderAdapter2(List<SellerOrder.Data> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public SellerOrderHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller_order2, parent, false);
        SellerOrderHolder2 volder = new SellerOrderHolder2(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final SellerOrderHolder2 holder, final int position) {
       // holder.branchTv.setText(datas.get(position).brandName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
       // SellerOrderAdapterRv adapter = new SellerOrderAdapterRv(datas.get(position),context);
        //holder.recyclerView.setAdapter(adapter);
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
