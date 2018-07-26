package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.bean.SearchModel;
import com.jpp.mall.holder.SearchHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {
    private List<SearchModel> datas;
    private Context context;

    private int lastClickItem = -1;


    public SearchAdapter(List<SearchModel> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        SearchHolder volder = new SearchHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final SearchHolder holder, final int position) {
        holder.branchTv.setText(datas.get(position).name);
        Glide.with(context).load(datas.get(position).pic).into(holder.imageView);
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
