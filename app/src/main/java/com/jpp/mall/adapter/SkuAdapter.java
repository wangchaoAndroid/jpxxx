package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.bean.BrandModel;
import com.jpp.mall.holder.BranchHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SkuAdapter extends RecyclerView.Adapter<BranchHolder> {
    private List<String> datas;
    private Context context;
    private List<BranchHolder> holderList = new ArrayList<>();
    public SkuAdapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }
    @Override
    public BranchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bracnh, parent, false);
        BranchHolder volder = new BranchHolder(view);
        holderList.add(volder);
        return volder;
    }

    @Override
    public void onBindViewHolder(final BranchHolder holder, final int position) {
        holder.branchTv.setText(datas.get(position));
        holder.branchTv.setOnClickListener(new View.OnClickListener() {
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
