package com.jpp.mall.acitivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
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

public class AddBankCardActivity extends BaseActivity {
    private static final String TAG = "AddBankCardActivity";
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
    @BindView(R.id.bank_open_et)
    TextView openName;
    @BindView(R.id.bank_phone_et)
    TextView phone;
    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.bind_bank_card)
    public void bindCard(View view){
        String bankNameS = bankName.getText().toString().trim();
        String cardNumS = cardNum.getText().toString().trim();
        String openNameS = openName.getText().toString().trim();
        String phoneS = phone.getText().toString().trim();
        if(TextUtils.isEmpty(bankNameS)){
            ToastUtils.showShortToast(this,"请输入银行名称");
            return;
        }
        if(TextUtils.isEmpty(cardNumS)){
            ToastUtils.showShortToast(this,"请输入银行卡号");
            return;
        }
        if(TextUtils.isEmpty(openNameS)){
            ToastUtils.showShortToast(this,"请输入开户行名称");
            return;
        }
        if(TextUtils.isEmpty(phoneS)){
            ToastUtils.showShortToast(this,"请输入预留手机号");
            return;
        }
        String token = (String) SpUtils.get(this,"token","");
        if(isDialogShowing()){
            return;
        }
        showLoadingDialog();
        getBankList(token,bankNameS,cardNumS,openNameS,phoneS);
    }

    private void getBankList(String token, String bankNameS, String cardNumS, String openNameS, String phoneS) {
        //6214837844459143
        Logger.e(TAG,token + "---" + cardNumS + "---" + bankNameS + "--" + openNameS + "---" + phoneS);
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().bindBankCard(token,cardNumS,bankNameS,openNameS,phoneS);
        observable.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this) {
            @Override
            protected void onHandleSuccess(Object o) {
                ToastUtils.showShortToast(AddBankCardActivity.this,"绑定成功");
                dimissLoadingDialog();
                finish();
            }
            @Override
            protected void onHandleError(String msg,int code) {
                super.onHandleError(msg,code);
                Logger.e(TAG,msg + "---" + code);
                ToastUtils.showShortToast(AddBankCardActivity.this,"绑定失败");
                dimissLoadingDialog();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    public void initData() {
        title.setText(R.string.add_bank_card);
    }


}
