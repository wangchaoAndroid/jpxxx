package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.holder.SellerOrderHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderHolder> {
    private List<SellerOrder.Detail> datas;
    private Context context;
    private int pos;

    public SellerOrderAdapter(List<SellerOrder.Detail> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public SellerOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(context).inflate(R.layout.item_seller_order_detail, parent, false);
        SellerOrderHolder volder = new SellerOrderHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final SellerOrderHolder holder, final int position) {
       // holder.branchTv.setText(datas.get(position).brandName);
        holder.good_name.setText("" + datas.get(position).name);
        holder.good_price.setText("￥" + datas.get(position).price);
       // holder.good_color.setText(datas.get(position).desc.get(0));
        //holder.good_desc.setText(datas.get(position).desc.get(1));
        List<String> desc = datas.get(position).desc;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < desc.size(); i++) {
            TextView tv = new TextView(context);
            tv.setLayoutParams(params);
            tv.setLineSpacing(1.2f, 1.2f);//设置行间距
            tv.setTextColor(context.getResources().getColor(R.color.color_66));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tv.setPadding(6,6,6,6);
            tv.setText(desc.get(i) + "");
            holder.skuLL.addView(tv,i,params);
        }
        holder.good_num.setText("x"+ datas.get(position).num);
        Glide.with(context).load(datas.get(position).pic).into(holder.good_img);

//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onItemClickListener != null){
//                    holder.rootView.setBackgroundColor(Color.WHITE);
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
