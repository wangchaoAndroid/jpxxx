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

public class BranchAdapter extends RecyclerView.Adapter<BranchHolder> {
    private static final String TAG = "BranchAdapter";
    private List<BrandModel.Data> datas;
    private Context context;

    private int lastClickItem = -1;

    private List<BranchHolder> holderList = new ArrayList<>();

    public BranchAdapter(List<BrandModel.Data> datas, Context context) {
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
        holder.branchTv.setText(datas.get(position).brandName);
        if(lastClickItem == -1 && position == 0){
            notifyAllRb(lastClickItem,0);
        }
//        notifyAllRb(lastClickItem,0);
        holder.branchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    notifyAllRb(lastClickItem,position);
                    lastClickItem = position;
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

    public void notifyAllRb(int lasetClickItem, int position){
        if(lasetClickItem == -1){
            holderList.get(position).rootView.setBackgroundResource(R.color.color_white);
            holderList.get(position).branchTv.setTextColor(context.getResources().getColor(R.color.color_26));
            this.lastClickItem = position;
        }else {
            holderList.get(lasetClickItem).rootView.setBackgroundResource(R.color.color_f1f1f1);
            holderList.get(position).rootView.setBackgroundResource(R.color.color_white);

            holderList.get(lasetClickItem).branchTv.setTextColor(context.getResources().getColor(R.color.color_33));
            holderList.get(position).branchTv.setTextColor(context.getResources().getColor(R.color.color_26));
        }

    }

}
