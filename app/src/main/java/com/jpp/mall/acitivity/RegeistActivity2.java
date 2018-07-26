package com.jpp.mall.acitivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.CityModel;
import com.jpp.mall.bean.RegeistModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.Interface.OnCityItemClickListener;
import com.jpp.mall.view.citypickerview.bean.CityBean;
import com.jpp.mall.view.citypickerview.bean.DistrictBean;
import com.jpp.mall.view.citypickerview.bean.ProvinceBean;
import com.jpp.mall.view.citypickerview.citywheel.CityConfig;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.jpp.mall.view.citypickerview.style.citypickerview.CityPickerView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class RegeistActivity2 extends BaseActivity {
    private static final String TAG = "RegeistActivity2";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.shop_et)
    EditText etShop;

    @BindView(R.id.area_et)
    EditText etArea;
    @BindView(R.id.address_et)
    EditText etAddress;



    @BindView(R.id.invite_et)
    EditText etInvite;

    //申明对象
    CityPickerView mPicker=new CityPickerView();
    private String shop;
    private String area;
    private String address;
    private String inviteCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_regeist2;
    }
    @Override
    public void initData() {
        title.setText(R.string.regeist);
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }


    private String   pro ;

    private String cy;

    private String dist;

    private String areaId;
    @OnClick(R.id.area_et)
    public void selectArea(View view){
        getCities();
        //添加默认的配置，不需要自己定义
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        //监听选择点击事件及返回结果
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //省份
                if (province != null) {
                    pro = province.getName();
                }

                //城市
                if (city != null) {
                    cy = city.getName();
                }

                //地区
                if (district != null) {
                    dist = district.getName();
                    areaId = district.getId();
                }

                etArea.setText(pro + cy + dist);

            }

            @Override
            public void onCancel() {
                ToastUtils.showLongToast(RegeistActivity2.this, "已取消");
            }
        });


    }


    @OnClick(R.id.submit)
    public void submit(View view){;
        shop = etShop.getText().toString().trim();
        area = etArea.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        if(TextUtils.isEmpty(shop)){
            Toast.makeText(this,"请输入商铺名称",Toast.LENGTH_SHORT).show();
            return;
        }
//
//
        if(TextUtils.isEmpty(area)){
            Toast.makeText(this,"请选择地区信息",Toast.LENGTH_SHORT).show();
            return;
        }
//
        if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"请输入街道门牌信息",Toast.LENGTH_SHORT).show();
            return;
        }
        inviteCode = etInvite.getText().toString().trim();
        if(TextUtils.isEmpty(inviteCode)){
            Toast.makeText(this,"请输入邀请码",Toast.LENGTH_SHORT).show();
            return;
        }
        regeist();
    }

    /**
     * 获取城市列表
     */
    public void getCities(){
        Observable<CityModel> observable1 = RetrofitFactory.getInstance().getCities();
        observable1.compose(compose(this.<CityModel>bindToLifecycle()))
                .subscribe(new Consumer<CityModel>() {
                    @Override
                    public void accept(CityModel cityModel) throws Exception {
                        Logger.e(TAG,cityModel.toString());
                        SpUtils.put(RegeistActivity2.this,"cities",cityModel);
                        mPicker.init(RegeistActivity2.this,cityModel.data);
                        //显示
                        mPicker.showCityPicker( );
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //Logger.e(TAG,throwable.toString());
                    }
                });

    }


    private void regeist() {
        if(isDialogShowing()){
            return;
        }
        showLoadingDialog();
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity<RegeistModel>> observable1 = RetrofitFactory.getInstance().regeist2(token, shop, address,inviteCode,areaId);
        observable1.compose(compose(this.<BaseEntity<RegeistModel>>bindToLifecycle())).subscribe(new BaseObserver<RegeistModel>(RegeistActivity2.this) {
            @Override
            protected void onHandleSuccess(RegeistModel user) {
                // 保存用户信息等操作
                Toast.makeText(RegeistActivity2.this,"添加商铺成功",Toast.LENGTH_SHORT).show();
                //Logger.e(TAG,user.toString());
                startActivity(new Intent(RegeistActivity2.this,LoginActivity.class));
                RegeistActivity2.this.finish();
                dimissLoadingDialog();
            }

            @Override
            protected void onHandleError(String msg,int code) {
                super.onHandleError(msg,code);
                dimissLoadingDialog();
            }
        });


    }


}
