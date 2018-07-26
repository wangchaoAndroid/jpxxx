package com.jpp.mall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class NextLevelVolder extends RecyclerView.ViewHolder {
    public  View rootView;
    public TextView walletMoney,walletType,walletTime,walletGrade;
    public ImageView shop_icon;
    public NextLevelVolder(View itemView) {
        super(itemView);
        rootView = itemView;
        walletMoney = (TextView) itemView.findViewById(R.id.wallet_money);
        walletType = (TextView) itemView.findViewById(R.id.wallet_type);
        walletTime = (TextView) itemView.findViewById(R.id.wallet_time);
        walletGrade = (TextView) itemView.findViewById(R.id.wallet_grade);
        shop_icon = (ImageView) itemView.findViewById(R.id.shop_icon);
    }
}
