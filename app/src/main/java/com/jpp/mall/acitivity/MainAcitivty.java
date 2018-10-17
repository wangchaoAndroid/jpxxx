package com.jpp.mall.acitivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpp.mall.App;
import com.jpp.mall.R;
import com.jpp.mall.adapter.MainAdapter;
import com.jpp.mall.adapter.MainPagerAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.HomeModel;
import com.jpp.mall.bean.HomeResponse;
import com.jpp.mall.bean.ShopInfoModel;
import com.jpp.mall.bean.UnReadCount;
import com.jpp.mall.download.ApkUpgradeInfo;
import com.jpp.mall.download.Callback;
import com.jpp.mall.download.DownloadManager;
import com.jpp.mall.event.MsgEvent;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.ResponseCallback;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.AppConfig;
import com.jpp.mall.utils.IAppUtil;
import com.jpp.mall.utils.IDisplayUtil;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.MD5Util;
import com.jpp.mall.utils.NotifyUtils;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.utils.StackManager;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainAcitivty extends BaseActivity implements MainAdapter.OnItemClickListener, ViewPager.OnPageChangeListener, MainPagerAdapter.OnPicClickListener, View.OnClickListener {
    private static final String TAG = "MainAcitivty";
    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    @BindView(R.id.point_view)
    LinearLayout pointView;
    @BindView(R.id.select_phone)
    TextView selectPhone;
    @BindView(R.id.shop_id)
    TextView shopId;
    @BindView(R.id.invite_code)
    TextView inviteCode;
    @BindView(R.id.cart)
    ImageView cart;
    private List<String> paths;
    private MainPagerAdapter mainPagerAdapter;
    private String token;
    private int unReadCount;
//定义Handler接收发送消息
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 999) {
                //获取viewPager当前的位置
                int currentItem = vpMain.getCurrentItem();
                currentItem++;
                //设置viewPager的位置
                vpMain.setCurrentItem(currentItem);
                // 继续轮播
                //Logger.i(TAG, "bobobo.....");//测试
                //调用发送消息的方法
                startRool();
            }
        };
    };

    @OnClick(R.id.cart)
    public void cart(View view){
        startActivity(new Intent(this,CartActivity.class));
    }

    private boolean isPausing;
    private List<HomeModel> mData = new ArrayList<>();
    private MainAdapter mainAdapter;

    /**
     * 每隔2秒发送一次消息
     */
    private void startRool() {
        // 开始轮播
        handler.sendEmptyMessageDelayed(999, 3000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    public void initData() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getPayData();
//            }
//        }).start();

        EventBus.getDefault().register(this);
        token = (String) SpUtils.get(this, "token","");
        regeistXG(token);
        getUserInfo(token);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mainRv.setLayoutManager(gridLayoutManager);
        List<String> datas = new ArrayList<>();
        datas.add("采购订单");
        datas.add("平台订单");
        datas.add("我的钱包");
        datas.add("周边采购");
        datas.add("消息中心");
        datas.add("管理");
       // datas.add("商品采购");
        mainAdapter = new MainAdapter(datas,this,unReadCount);
        mainAdapter.setOnItemClickListener(this);
        mainRv.setAdapter(mainAdapter);
        paths = new ArrayList<>();
        Observable<BaseEntity<HomeResponse>> observable = RetrofitFactory.getInstance().getHomePage();
        observable.compose(compose(this.<BaseEntity<HomeResponse>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<HomeResponse>(this) {
                    @Override
                    protected void onHandleSuccess(HomeResponse homeResponse) {
                        if(homeResponse != null){
                            Logger.e(TAG,homeResponse.toString());
                            mData.clear();
                            mData.addAll(homeResponse.activityInfos);
                            if(mData != null && !mData.isEmpty()){
                                for(HomeModel homeModel : mData) {
                                    paths.add(homeModel.pic);
                                }
                                // 设置当前页
                                mainPagerAdapter = new MainPagerAdapter(MainAcitivty.this, paths);
                                mainPagerAdapter.setOnPicClickListener(MainAcitivty.this);
                                vpMain.setAdapter(mainPagerAdapter);
                                vpMain.setCurrentItem(paths.size() * 10000);
                                vpMain.setOnPageChangeListener(MainAcitivty.this);
                                initDots();
                                startRool();
                            }
                        }
                    }
                });


        //test();
        NotifyUtils.isNotificationEnabled(this);
        upgrade();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPausing = true;
        handler.removeCallbacksAndMessages(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recevierUnRead(MsgEvent messageEvent) {
        getUnReadCount();
    }

    public void getUnReadCount(){
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity<UnReadCount>> observable1 = RetrofitFactory.getInstance().getUnReadCount(token);
        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UnReadCount>(this){
                    @Override
                    protected void onHandleSuccess(UnReadCount readCount) {
                        unReadCount = readCount.count;
                        Logger.e("unReadCount",unReadCount + "");
                        mainAdapter.setUnReadCount(unReadCount);
                    }

                    @Override
                    protected void onHandleError(String msg, int code) {
                        super.onHandleError(msg, code);
                        Logger.e("unReadCount",msg + "---" +code );
                    }
                });
    }

    public void getPayData() {
        String url = "http://120.79.13.43:8080/fh_battery/payInterface?token=39139054-47fd-4cc2-98c0-c1729599c22f&payType=200&productNumber=dawd4545&batteryId=23";
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(30000);
            String msg = "";
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200){
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    msg += line + "\n";
                }
                Log.e(TAG,"MSG" + msg);
                reader.close(); // 关闭流
                pay(this,msg);
            }

        } catch (IOException e) {
        }
    }

    public void pay(Activity context , String data ){
        //Pingpp.createPayment(context, data);

        PingppUI.createPay(context, data, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                int code = data.getExtras().getInt("code");
                String result = data.getExtras().getString("result");
                Log.e(TAG,"code" + code + "----result" + result);
//                if(code == 1){
//                    details.remove(pos);
//                    orderDetailAdapter.notifyItemRemoved(pos);
//                }else {
//                    ToastUtils.showShortToast(OrderDetailActivity.this,""+ result);
//                }
                //Log.e(TAG, "handlePaymentResult: "+ code + "---" + result );
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        if(isPausing){
            startRool();
            isPausing= false;
        }
    }

    public void test(){
        Observable<ResponseBody> observable = RetrofitFactory.getInstance().test(token);
        observable.compose(compose(this.<ResponseBody>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody homeResponse) throws Exception {
                        //Log.e(TAG,"" +homeResponse.string());
                    }
                });
    }

    public void getUserInfo(String token ) {
        Observable<BaseEntity<ShopInfoModel>> observable = RetrofitFactory.getInstance().getShopInfo(token);
        observable.compose(compose(this.<BaseEntity<ShopInfoModel>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ShopInfoModel>(this) {
                    @Override
                    protected void onHandleSuccess(ShopInfoModel shopInfoModel) {
                        if(shopInfoModel != null){
                            shopId.setText("店铺编号:" + shopInfoModel.shopInfo.shopId );
                            inviteCode.setText("邀请码:" + shopInfoModel.shopInfo.inviteCode );
                        }
                    }

                    @Override
                    protected void onHandleError(String msg,int code) {
                        super.onHandleError(msg,code);
                        if(code == 404){
                            StackManager.getInstance().popAllActivity();
                            XGPushManager.unregisterPush(MainAcitivty.this);
                            startActivity(new Intent(MainAcitivty.this,RegeistActivity2.class));
                        }else {
                            XGPushManager.unregisterPush(MainAcitivty.this);
                            SpUtils.put(MainAcitivty.this, "token","");
                            startActivity(new Intent(MainAcitivty.this,LoginActivity.class));
                            MainAcitivty.this.finish();
                        }
                    }
                });

    }


    @Override
    public void onItemClick(int pos) {
        switch (pos){
            case 0:
                startActivity(new Intent(MainAcitivty.this,SellerOrderActivity.class));
                overridePendingTransition(0,0);
                break;
            case 1:
                startActivity(new Intent(MainAcitivty.this,UserOrderActivity.class));
                overridePendingTransition(0,0);
                break;
            case 2:
                startActivity(new Intent(MainAcitivty.this,WalletActivity.class));
                overridePendingTransition(0,0);
                break;
            case 3:
                Intent intentxx = new Intent(this, SelectComponentActivity.class);
               startActivity(intentxx);
                overridePendingTransition(0,0);
                break;
            case 4:
                startActivity(new Intent(MainAcitivty.this,NotifyCenterActivity.class));
                overridePendingTransition(0,0);
                break;
            case 5:
                startActivity(new Intent(MainAcitivty.this,SettingActivity.class));
                overridePendingTransition(0,0);
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //遍历存放图片的数组
        for (int i = 0; i < paths.size(); i++) {
            int i1 = position % paths.size();
            ImageView iv = (ImageView) dotsList.get(i);
            Drawable drawable = null;
            //判断小点点与当前的图片是否对应，对应设置为亮色 ，否则设置为暗色
            if (i == position % paths.size()) {
                drawable = getResources().getDrawable(R.drawable.main_point);
                iv.setImageDrawable(drawable);
            } else {
                drawable = getResources().getDrawable(R.drawable.main_point66);
                iv.setImageDrawable(drawable);

            }
        }
    }

    private List<ImageView> dotsList;

    /**
     * 初始化小点
     */
    private void initDots() {
        //创建存放小点点的集合
        dotsList = new ArrayList<ImageView>();
        //每次初始化之前清空集合
        dotsList.clear();
        // 每次初始化之前  移除  布局中的所有小点
        pointView.removeAllViews();
        for (int i = 0; i < paths.size(); i++) {
            //创建小点点图片
            ImageView imageView = new ImageView(this);
            Drawable drawable = null;
            if (i == 0) {
                // 亮色图片
                drawable = getResources().getDrawable(R.drawable.main_point);

            } else {
                drawable = getResources().getDrawable(R.drawable.main_point66);
            }
            imageView.setImageDrawable(drawable);
            // 考虑屏幕适配
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(this, 5), dip2px(this, 5));
            //设置小点点之间的间距
            params.setMargins(dip2px(this, 5), 0, dip2px(this, 5), 0);
            //将小点点添加大线性布局中
            pointView.addView(imageView, params);
            // 将小点的控件添加到集合中
            dotsList.add(imageView);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.select_phone)
    public void selectPhone(View view){
        startActivity(new Intent(MainAcitivty.this,SelectPhoneActivity.class));
        overridePendingTransition(0,0);
    }


    @Override
    public void onClick(int position) {
        HomeModel homeModel = mData.get(position);
        if(homeModel != null){
            int  linkType = homeModel.linkType;
            if(linkType == 0){

            }else if(linkType == 1){
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mData.get(position).linkURL);
                intent.setData(content_url);
                startActivity(intent);
            }else if(linkType == 2){
                //下载
                String url = homeModel.linkURL;
                File cacheFile = new File(App.getAppContext().getExternalCacheDir(), MD5Util.getStringMD5(url) + ".apk");
                DownloadManager.getInstance().download(url, cacheFile, new Callback() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        }
    }

    @Override
    public void doInDestory() {
        super.doInDestory();
        EventBus.getDefault().unregister(this);
    }

    public void regeistXG(String token){
        XGPushManager.registerPush(this, token,new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
//token在设备卸载重装的时候有可能会变
                Logger.e("TPush", "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Logger.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

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
//                    if(value != null && !TextUtils.isEmpty(value.msg)){
//                        ToastUtils.showShortToast(MainAcitivty.this, value.msg + "");
//                    }

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
                            ToastUtils.showShortToast(MainAcitivty.this, getResources().getString(R.string.no_network_to_remind));
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
