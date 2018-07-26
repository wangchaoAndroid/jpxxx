package com.jpp.mall.adapter;

/**
 * Create by wangchao on 2018/5/18 15:30
 */

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter {
    protected List<T> dataSet = new ArrayList<>();


    public void updateData(List dataSet) {
        this.dataSet.clear();
        appendData(dataSet);
    }

    public void appendData(List dataSet) {
        if (dataSet != null && !dataSet.isEmpty()) {
            this.dataSet.addAll(dataSet);
            notifyDataSetChanged();
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}