package com.jpp.mall.acitivity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.BankCardAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.BankCardModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class BankCardListActivity extends BaseActivity {
    private static final String TAG = "BankCardListActivity";
    @BindView(R.id.right_btn)
    ImageView rightBtn;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bankCardRv)
    RecyclerView bankCardRv;
    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    private List<BankCardModel> mCardModels = new ArrayList<>();

    private BankCardAdapter bankCardAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card_list;
    }

    @Override
    public void initData() {
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.icon_browser_main_add);
        title.setText(R.string.my_bank_card);
        LinearLayoutManager linearLayoutManagerBr = new LinearLayoutManager(this);
        bankCardRv.setLayoutManager(linearLayoutManagerBr);
        mCardModels = new ArrayList<>();
        bankCardAdapter = new BankCardAdapter(mCardModels,this);
        bankCardRv.setAdapter(bankCardAdapter);
        bankCardRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        getBankCards();
    }

    @OnClick(R.id.right_btn)
    public void addBankCard(View view){
        startActivityForResult(new Intent(this,AddBankCardActivity.class),1);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                getBankCards();
            }
        }
    }

    public void getBankCards(){
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity<List<BankCardModel>>> observable = RetrofitFactory.getInstance().getBankList(token);
        observable.compose(compose(this.<BaseEntity<List<BankCardModel>>>bindToLifecycle())).subscribe(new BaseObserver<List<BankCardModel>>(this) {
            @Override
            protected void onHandleSuccess(List<BankCardModel> bankCardModels) {
                mCardModels.clear();
                mCardModels.addAll(bankCardModels);
                bankCardAdapter.notifyDataSetChanged();
            }
        });
    }
}
