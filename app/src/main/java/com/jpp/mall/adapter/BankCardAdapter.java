package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.bean.BankCardModel;
import com.jpp.mall.holder.WalletVolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class BankCardAdapter extends RecyclerView.Adapter<WalletVolder> {
    private List<BankCardModel> datas;
    private Context context;

    public BankCardAdapter(List<BankCardModel> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public WalletVolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wallet , parent, false);
        WalletVolder volder = new WalletVolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final WalletVolder holder, final int position) {
        holder.walletType.setText("开户行:" + datas.get(position).bankOpen);
        holder.walletOrder.setText("银行卡号:" +datas.get(position).bankCard);
        holder.walletMoney.setText("银行名称：" + datas.get(position).bankName);
//        Glide.with(context).load(datas.get(position).icon).into(holder.sellerAvater);
//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onItemClickListener != null){
//                    onItemClickListener.onItemClick(position);
//                }
//            }
//        });
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
