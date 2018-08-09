package com.jpp.mall.acitivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.download.ApkUpgradeInfo;
import com.jpp.mall.download.Callback;
import com.jpp.mall.download.DownloadManager;
import com.jpp.mall.net.ResponseCallback;
import com.jpp.mall.utils.AppConfig;
import com.jpp.mall.utils.IAppUtil;
import com.jpp.mall.utils.IDisplayUtil;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.utils.StackManager;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.tencent.android.tpush.XGPushManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.shop_info)
    TextView shopInfo;
    @BindView(R.id.help_center)
    TextView helpCenter;
    @BindView(R.id.commoent)
    TextView commoent;
    @BindView(R.id.service_log)
    TextView serviceLog;
    @BindView(R.id.credibility_querry)
    TextView credibilityQuerry;
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.next_look)
    TextView nextLook;
    @BindView(R.id.upgrade)
    TextView upgrade;
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }
    @Override
    public void initData() {
        title.setText("设置");
    }

    @OnClick(R.id.shop_info)
    public void querryShopInfo(View view){
        startActivity(new Intent(SettingActivity.this,ShopInfoActivity.class));
    }

    @OnClick(R.id.help_center)
    public void helpCenter(View view){

    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.commoent)
    public void commoent(View view){

    }

    @OnClick(R.id.service_log)
    public void serviceLog(View view){

    }

    @OnClick(R.id.upgrade)
    public void upgrade(View view){
        upgrade();
    }


    @OnClick(R.id.next_look)
    public void nextLook(View view){
        startActivity(new Intent(this,NextLevelActivity.class));
        overridePendingTransition(0,0);
    }

    @OnClick(R.id.credibility_querry)
    public void credibilityQuerry(View view){
        startActivity(new Intent(this,CredibilityQuerryActivity.class));
    }

    @OnClick(R.id.logout)
    public void logout(View view){
        final Dialog dialog = new Dialog(this,R.style.common_dialog_style);
        View dialogView = View.inflate(this,R.layout.dialog_logout,null);
        dialog.setContentView(dialogView);
        dialog.show();
        Window window = dialog.getWindow();
        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        dialogView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XGPushManager.unregisterPush(SettingActivity.this);
                SpUtils.put(SettingActivity.this,"token","");
                StackManager.getInstance().popAllActivity();
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
            }
        });
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        window.setAttributes(attributes);

    }

    public void upgrade() {
        DownloadManager.getInstance().upgradeInfo(IAppUtil.getPackageName(this), IAppUtil.getVersionName(this), new ResponseCallback<ApkUpgradeInfo>() {
            @Override
            public void onSuccess(ApkUpgradeInfo value) {
                Logger.e("value", "value" + value.toString());
                if (value != null && value.code == 1 && !TextUtils.isEmpty(value.data)) {
                    showDialog();
                    mUpgradeInfo = value;
                }else {
                    if(value != null && !TextUtils.isEmpty(value.msg)){
                        ToastUtils.showShortToast(SettingActivity.this, value.msg + "");
                    }

                }
            }
            @Override
            public void onFailture(String e) {
            }
        });
    }

    private AlertDialog mDialog;
    private TextView mSure;
    private TextView mCancel;
    private ProgressBar mPsb;
    private TextView mTip;
    private ApkUpgradeInfo mUpgradeInfo;
    public void showDialog() {
        mDialog = new AlertDialog.Builder(this).create();
        WindowManager.LayoutParams attributes = mDialog.getWindow().getAttributes();
        attributes.width = (int) (IDisplayUtil.getScreenWidth(this) * 0.75);
        mDialog.getWindow().setAttributes(attributes);
        mDialog.show();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_upgrade_dialog, null);

        mSure = (TextView) dialogView.findViewById(R.id.sure_txt);
        mTip = (TextView) dialogView.findViewById(R.id.tip_txt);
        mCancel = (TextView) dialogView.findViewById(R.id.cancle_txt);
        mPsb = (ProgressBar) dialogView.findViewById(R.id.psb);
        mPsb.setVisibility(View.GONE);
        mTip.setText(R.string.upgrade_tip);
        mDialog.setContentView(dialogView);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure_txt:
                if (mDialog.isShowing() && mUpgradeInfo != null) {
                    String updateUrl = mUpgradeInfo.data;
                    File dir = new File(AppConfig.download_Dir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    mPsb.setVisibility(View.VISIBLE);
                    mCancel.setVisibility(View.GONE);
                    mSure.setVisibility(View.GONE);
                    mTip.setText(R.string.upgrade_loading);
                    File file = new File(AppConfig.download_Dir, "jpp.apk");
                    DownloadManager.getInstance().download(updateUrl, file, new Callback() {
                        @Override
                        public void onProgress(int progress) {
                            //下载的进度
                            mPsb.setProgress(progress);
                            if (progress == 100) {
                                mDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFail(String msg) {
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            //  ILog.e("TAG", "111111");
                            ToastUtils.showShortToast(SettingActivity.this, getResources().getString(R.string.no_network_to_remind));
                        }

                    });
                }
                break;
            case R.id.cancle_txt:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                break;
        }

    }
}
