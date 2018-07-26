package com.jpp.mall;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class BindIdCardActivity extends BaseActivity {
    private static final String TAG = "BindIdCardActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bind_bank_card)
    TextView bindCard;
    @BindView(R.id.bank_name_et)
    TextView bankName;
    @BindView(R.id.bank_card_num_et)
    TextView cardNum;
    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.bind_bank_card)
    public void bindCard(View view){
        String bankNameS = bankName.getText().toString().trim();
        String cardNumS = cardNum.getText().toString().trim();
        if(TextUtils.isEmpty(bankNameS)){
            ToastUtils.showShortToast(this,"请输入姓名");
            return;
        }
        if(TextUtils.isEmpty(cardNumS)){
            ToastUtils.showShortToast(this,"请输入身份证号");
            return;
        }

        String token = (String) SpUtils.get(this,"token","");
        if(isDialogShowing()){
            return;
        }
        showLoadingDialog();
        bindIdCard(token,bankNameS,cardNumS);
    }

    private void bindIdCard(String token, String bankNameS, String cardNumS) {
        Logger.e(TAG,token + "---" + bankNameS + "---" + cardNumS);
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().bindIdCard(token,bankNameS,cardNumS);
        observable.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this) {
            @Override
            protected void onHandleSuccess(Object o) {
                ToastUtils.showShortToast(BindIdCardActivity.this,"绑定成功");
                dimissLoadingDialog();
                finish();
            }
            @Override
            protected void onHandleError(String msg,int code) {
                super.onHandleError(msg,code);
                Logger.e(TAG,msg + "---" + code);
                ToastUtils.showShortToast(BindIdCardActivity.this,"绑定失败");
                dimissLoadingDialog();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_id_card;
    }

    @Override
    public void initData() {
        title.setText(R.string.add_id_card);
    }

}
