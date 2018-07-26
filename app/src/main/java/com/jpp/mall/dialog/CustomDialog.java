package com.jpp.mall.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;

/**
 * Created by Administrator on 2018/5/21 0021.
 */

public class CustomDialog extends BaseDialog {

    //这些属性无视就好了，就是布局的控件
    private TextView input,cancel,sure;

    //构造方法还是要的哈
    public CustomDialog(Context context) {    super(context);}

    //设置对话框的样式
    @Override
    protected int getDialogStyleId() {
        return R.style.dialog_custom;
    }

    //继承于BaseDialog的方法，设置布局用的，这样对话框张啥样久随心所欲啦
    @Override
    protected View getView() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_refuse, null);

        //得到各种view
        cancel= (TextView) view.findViewById(R.id.cancel);
        sure = (TextView) view.findViewById(R.id.sure);
        input = (TextView) view.findViewById(R.id.input);
        //初始化一些控件的方法（放下面写啦~）
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public interface OnSureListener{
        void onSure();
    }

    private OnSureListener onSureListener;

    public void setOnSureListener(OnSureListener listener){
        this.onSureListener = listener;
    }


}
