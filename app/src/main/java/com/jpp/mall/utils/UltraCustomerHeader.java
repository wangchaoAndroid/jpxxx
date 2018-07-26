package com.jpp.mall.utils;

import android.content.Context;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Create by wangchao on 2018/5/19 14:58
 */
public class UltraCustomerHeader {
    /**
     * 设置自定义Header
     * @param mPtrFrame
     * @param context
     */
    public static void setUltraCustomerHeader(PtrClassicFrameLayout mPtrFrame, Context context){

        mPtrFrame.setLastUpdateTimeRelateObject(context);
        // the following are default settings
        mPtrFrame.setResistanceHeader(1.7f); // 您还可以单独设置脚,头
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(1000);  // 您还可以单独设置脚,头
        // default is false
        mPtrFrame.setPullToRefresh(false);

        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //以下为自定义header需要
//        StoreHouseHeader header = new StoreHouseHeader(context);
//        header.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
//        header.setTextColor(context.getResources().getColor(R.color.cmbkb_black));
//        header.initWithString("Loading...");
//        mPtrFrame.setDurationToCloseHeader(1500);
//        mPtrFrame.setHeaderView(header);
//        mPtrFrame.addPtrUIHandler(header);
//        mPtrFrame.setBackgroundColor(context.getResources().getColor(R.color.white));
    }
}
