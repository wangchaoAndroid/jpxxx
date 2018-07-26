package com.jpp.mall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/4/30 0030.
 */

public class MainPagerAdapter extends PagerAdapter {

    private List<String> resIds;

    private Context context;

    public MainPagerAdapter(Context context ,List<String> resIds) {
        this.resIds =resIds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//          if (object instanceof ScaleView) {
//              ScaleView scaleView = (ScaleView) object;
//              container.removeView(scaleView);
//          }
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView scaleView = new ImageView(context.getApplicationContext());
        scaleView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(!resIds.isEmpty()){
            Glide.with(context.getApplicationContext()).load(resIds.get(position % resIds.size() )).into(scaleView);
        }
        scaleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPicClickListener != null){
                    mOnPicClickListener.onClick(position % resIds.size());
                }
            }
        });
        container.addView(scaleView);
        return scaleView;
    }

    public interface OnPicClickListener{
        void onClick(int position);
    }

    private OnPicClickListener mOnPicClickListener;

    public void setOnPicClickListener(OnPicClickListener listener){
        this.mOnPicClickListener = listener;
    }

}
