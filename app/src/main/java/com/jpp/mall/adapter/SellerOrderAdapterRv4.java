package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.holder.OrderRvHolder4;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SellerOrderAdapterRv4 extends RecyclerView.Adapter<OrderRvHolder4> {
    private List<SellerOrder.Data> datas;
    private Context context;
    private int pos;

    public SellerOrderAdapterRv4(List<SellerOrder.Data> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public OrderRvHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(context).inflate(R.layout.item_seller_order4, parent, false);
        OrderRvHolder4 volder = new OrderRvHolder4(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final OrderRvHolder4 holder, final int position) {
       // holder.branchTv.setText(datas.get(position).brandName);
//        holder.shop_name.setText("" + datas.get(position).details.);
        holder.order_time.setText("" + datas.get(position).createTime);
        holder.order_code.setText("" + datas.get(position).orderCode);
        holder.good_total.setText("合计：￥" + datas.get(position).sumPrice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        SellerOrderAdapter adapter = new SellerOrderAdapter(datas.get(position).details,context);
        holder.recyclerView.setAdapter(adapter);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        adapter.setOnItemClickListener(new SellerOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
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

    public void setOnPayListener(OnPayListener listener){
        this.onPayListener = listener;
    }

    public interface OnPayListener{
        void  onPay(int pos);
    }

    private OnPayListener onPayListener;


}
