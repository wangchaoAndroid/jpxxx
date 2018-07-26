package com.jpp.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.acitivity.SureOrderActivity;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.holder.UserOrderHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderHolder> {
    private List<SellerOrder.Data> datas = new ArrayList<>();
    private static final String TAG = "UserOrderAdapter";
    private Context context;
    private int pos;

    public UserOrderAdapter(List<SellerOrder.Data> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public UserOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(context).inflate(R.layout.item_user_order, parent, false);
        UserOrderHolder volder = new UserOrderHolder(view);
        return volder;
    }
    //1到店维修 2自主维修 3上门维修）
    @Override
    public void onBindViewHolder(final UserOrderHolder holder, final int position) {
       // holder.branchTv.setText(datas.get(position).brandName);
        holder.order_code.setText("" +datas.get(position).orderCode);
        holder.order_time.setText("" +datas.get(position).createTime);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.orderRv.setLayoutManager(layoutManager);
        UserOrderAdapterRv adapter = new UserOrderAdapterRv(datas.get(position).details,datas.get(position).orderType,context);
        holder.orderRv.setAdapter(adapter);
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SureOrderActivity.class));
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    holder.rootView.setBackgroundColor(Color.WHITE);
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        adapter.setOnItemClickListener(new UserOrderAdapterRv.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onPayListener != null){
                    onPayListener.onPay(position);
                }
            }
        });
        holder.delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onDeleteListener != null){
                    onDeleteListener.onDelete(position);
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


    public void setOnPayListener(OnPayListener listener){
        this.onPayListener = listener;
    }

    public interface OnPayListener{
        void  onPay(int pos);
    }

    private OnPayListener onPayListener;


    public void setOnDeleteListener(OnDeleteListener listener){
        this.onDeleteListener = listener;
    }

    public interface OnDeleteListener{
        void  onDelete(int pos);
    }

    private OnDeleteListener onDeleteListener;
}
