package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.holder.UserOrderHolderRv;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class UserOrderAdapterRv extends RecyclerView.Adapter<UserOrderHolderRv> {
    private List<SellerOrder.Detail> datas;
    private Context context;
    private int orderType;

    public UserOrderAdapterRv(List<SellerOrder.Detail> datas, int orderType,Context context) {
        this.datas = datas;
        this.context = context;
        this.orderType =orderType;
    }

    @Override
    public UserOrderHolderRv onCreateViewHolder(ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(context).inflate(R.layout.item_user_order_list, parent, false);
        UserOrderHolderRv volder = new UserOrderHolderRv(view);
        return volder;
    }
    //1到店维修 2自主维修 3上门维修）
    @Override
    public void onBindViewHolder(final UserOrderHolderRv holder, final int position) {
        holder.good_name.setText("" +datas.get(position).name);

        if(orderType == 1){
            holder.good_desc.setText("到店维修");
        }else if(orderType == 2){
            holder.good_desc.setText("自主维修");
        }else if(orderType == 3){
            holder.good_desc.setText("上门维修");
        }else if(orderType == 0){
            holder.good_desc.setText("平台订单");
        }
//        holder.shop_name.setText("" + datas.get(position).shopName);
        holder.good_price.setText("￥ " + datas.get(position).price);
//        Log.e("pic",datas.get(position).pic + "");
        Glide.with(context).load(datas.get(position).pic).into(holder.good_img);

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
