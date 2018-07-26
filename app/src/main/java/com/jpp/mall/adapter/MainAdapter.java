package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.holder.MainVolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainVolder> {
    private List<String> datas;
    private Context context;
    private int[] icons = {R.drawable.ic_terraceoder,R.drawable.ic_useroder,R.drawable.ic_wallet,R.drawable.ic_round,R.drawable.ic_lowrlevel,R.drawable.ic_set,R.drawable.ic_purchases};
    private int unReadCount;

    public MainAdapter(List<String> datas, Context context,int count ) {
        this.datas = datas;
        this.context = context;
        unReadCount = count;
    }

    @Override
    public MainVolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mian, parent, false);
        MainVolder volder = new MainVolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(MainVolder holder, final int position) {
//        holder.rootView.setBackground(context.getResources().getDrawable(icons[position]));
        holder.itemImag.setImageResource(icons[position]);
        holder.itemContent.setText(datas.get(position));
        if (position == 4) {
            if(unReadCount != 0){
                holder.red_point.setVisibility(View.VISIBLE);
                holder.red_point.setText("" + unReadCount);
            }else {
                holder.red_point.setVisibility(View.GONE);
            }
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setUnReadCount(int count){
        this.unReadCount = count;
        notifyItemChanged(4,4);
    }



    public interface OnItemClickListener{
        void  onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }





}
