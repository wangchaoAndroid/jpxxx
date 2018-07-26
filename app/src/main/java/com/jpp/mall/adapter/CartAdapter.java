package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.bean.SureOrderModer;
import com.jpp.mall.holder.CartHolder;
import com.jpp.mall.view.AmountView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CartAdapter extends RecyclerView.Adapter<CartHolder> {
    private List<SureOrderModer.CartOrder> datas;
    private Context context;
    private int pos;

    public CartAdapter(List<SureOrderModer.CartOrder> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    private Map<Integer,Boolean> selectedGoods = new LinkedHashMap<>();

    public Map<Integer, Boolean> getSelectedGoods() {
        return selectedGoods;
    }

    public void setSelectedGoods(Map<Integer, Boolean> selectedGoods) {
        this.selectedGoods = selectedGoods;
    }
    private  boolean isSelectAll;
    public void setSelectAll(){
        isSelectAll = !isSelectAll;
        notifyDataSetChanged();
    }


    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View   view = LayoutInflater.from(context).inflate(R.layout.item_seller_cart, parent, false);
        CartHolder volder = new CartHolder(view);
        return volder;
    }

    @Override
    public void onBindViewHolder(final CartHolder holder, final int position) {
        // holder.branchTv.setText(datas.get(position).brandName);
        SureOrderModer.CartOrder cartOrder = datas.get(position);
        if(cartOrder.isInvalid == 1){//失效
            holder.good_name.setTextColor(context.getResources().getColor(R.color.color_99));
            holder.good_price.setTextColor(context.getResources().getColor(R.color.color_99));
//            holder.rootView.setBackgroundResource(R.color.color_99);
            holder.mAmountView.setGoods_storage(datas.get(position).num );
            holder.rb_cart.setVisibility(View.INVISIBLE);
        }else {
            holder.good_name.setTextColor(context.getResources().getColor(R.color.color_26));
            holder.good_price.setTextColor(context.getResources().getColor(R.color.color_26));
            holder.mAmountView.setGoods_storage(100000);
            holder.rb_cart.setVisibility(View.VISIBLE);
            holder.rb_cart.setChecked(isSelectAll);
        }
        holder.rb_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedGoods.put(position,isChecked);
                if(mGoodsSelectedListener != null){
                    mGoodsSelectedListener.onGoodsSelectedChange(selectedGoods);
                }
            }
        });
        holder.good_name.setText("" + datas.get(position).name);
        holder.good_price.setText("￥" + datas.get(position).price);
       // holder.good_desc.setText("" + datas.get(position).desc.get(0));

        holder.mAmountView.setAmount(datas.get(position).num);
       // holder.good_color.setText(datas.get(position).desc.get(1));
        List<String> desc = datas.get(position).desc;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < desc.size(); i++) {
            TextView tv = new TextView(context);
            tv.setLayoutParams(params);
            tv.setLineSpacing(1.2f, 1.2f);//设置行间距]
            if(cartOrder.isInvalid == 1){
                tv.setTextColor(context.getResources().getColor(R.color.color_99));
            }else {
                tv.setTextColor(context.getResources().getColor(R.color.color_66));
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tv.setPadding(6,6,6,6);
            tv.setText(desc.get(i) + "");
            holder.skuLL.addView(tv,i,params);
        }
        Glide.with(context).load(datas.get(position).pic).into(holder.good_img);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        holder.mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if(mOnNumChangeListener != null){
                    mOnNumChangeListener.onNumChange(amount,position);
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

    public interface OnNumChangeListener{
        void  onNumChange(int amount,int position);
    }

    private OnNumChangeListener mOnNumChangeListener;


    public void setOnNumChangeListener(OnNumChangeListener listener){
        this.mOnNumChangeListener = listener;
    }

    public interface OnGoodsSelectedListener{
        void onGoodsSelectedChange(Map<Integer,Boolean> goodsSelectedMap);
    }

    private OnGoodsSelectedListener mGoodsSelectedListener;

    public void setGoodsSelectedListener(OnGoodsSelectedListener listener){
        this.mGoodsSelectedListener = listener;
    }


}
