package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class WalletVolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView walletMoney,walletOrder,walletType,walletTime;
    public WalletVolder(View itemView) {
        super(itemView);
        rootView = itemView;
        walletMoney = (TextView) itemView.findViewById(R.id.wallet_money);
        walletOrder = (TextView) itemView.findViewById(R.id.wallet_order);
        walletType = (TextView) itemView.findViewById(R.id.wallet_type);
        walletTime = (TextView) itemView.findViewById(R.id.wallet_time);
    }
}
