package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.bean.RoundDetailModel;
import com.jpp.mall.holder.DetailSkuHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class RoundDetailSkuAdapter extends RecyclerView.Adapter<DetailSkuHolder> {
    private List<RoundDetailModel.AttrName> datas;
    private Context context;

    public RoundDetailSkuAdapter(List<RoundDetailModel.AttrName> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public DetailSkuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sku, parent, false);
        DetailSkuHolder volder = new DetailSkuHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(DetailSkuHolder holder, final int position) {
        holder.itemContent.setText(datas.get(position).getPickerViewText());

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
