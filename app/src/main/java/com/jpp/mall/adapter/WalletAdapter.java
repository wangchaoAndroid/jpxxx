package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.bean.WalletModel;
import com.jpp.mall.holder.WalletVolder;
import com.jpp.mall.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletVolder> {
    private List<WalletModel.Record> datas;
    private Context context;

    public WalletAdapter(List<WalletModel.Record> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public WalletVolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_wallet,null);
        WalletVolder volder = new WalletVolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(WalletVolder holder, final int position) {
        holder.walletMoney.setText("￥" + datas.get(position).amount);
//        holder.walletOrder.setText(datas.get(position).orderCode);
        holder.walletTime.setText("" + datas.get(position).time);
        holder.walletType.setText("" + datas.get(position).title);
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
