package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.bean.ComponetModel;
import com.jpp.mall.holder.CategoryHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SelectComponentAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private List<ComponetModel.Component> datas;
    private Context context;

    public SelectComponentAdapter(List<ComponetModel.Component> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_component, parent, false);
        CategoryHolder volder = new CategoryHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, final int position) {
        holder.phone_name.setText("" + datas.get(position).name);
        holder.phone_price.setText("￥" + datas.get(position).price);
        Glide.with(context).load(datas.get(position).pic).into(holder.phone_iv);
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
