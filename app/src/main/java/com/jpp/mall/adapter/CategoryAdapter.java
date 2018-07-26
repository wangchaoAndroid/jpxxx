package com.jpp.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jpp.mall.bean.BrandModel;
import com.jpp.mall.bean.CategoryModel;
import com.jpp.mall.holder.CategoryHolder;
import com.jpp.mall.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private List<BrandModel.Model> datas;
    private Context context;

    public CategoryAdapter(List<BrandModel.Model> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_phone, parent, false);
        CategoryHolder volder = new CategoryHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, final int position) {
        holder.phone_name.setText(datas.get(position).modelName);
//        holder.phone_price.setText(datas.get(position));
        Glide.with(context).load(datas.get(position).modelPic).into(holder.phone_iv);
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
