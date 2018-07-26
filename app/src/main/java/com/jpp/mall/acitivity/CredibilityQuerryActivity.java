package com.jpp.mall.acitivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.CredibilityAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.CredibilityModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CredibilityQuerryActivity extends BaseActivity implements CredibilityAdapter.OnItemClickListener, TextWatcher {
    private static final String TAG = "CredibilityQuerryActivi";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.seller_info_rv)
    RecyclerView sellerInfoRv;

    @BindView(R.id.input_num)
    EditText inputNum;

    private List<CredibilityModel> sellerInfos = new ArrayList<>();

    private CredibilityAdapter sellerInfoAdapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_credibility_querry);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//        initEvent();
//    }

    private void initEvent() {
        inputNum.addTextChangedListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_credibility_querry;
    }
    @Override
    public void initData() {
        initEvent();
        title.setText(R.string.credibility_querry);
        LinearLayoutManager linearLayoutManagerBr = new LinearLayoutManager(CredibilityQuerryActivity.this);
        sellerInfoRv.setLayoutManager(linearLayoutManagerBr);
        sellerInfos = new ArrayList<>();
        sellerInfoAdapter = new CredibilityAdapter(sellerInfos,this);
        sellerInfoRv.setAdapter(sellerInfoAdapter);
        sellerInfoAdapter.setOnItemClickListener(this);
        sellerInfoRv.addItemDecoration(new SpaceItemDecoration(30));
        getEngineerall();
    }


    @Override
    public void onItemClick(int pos) {
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    /**
     * 获取维修师傅列表信息
     */
    public void getEngineerall(){
        showLoadingDialog();
        Observable<BaseEntity<List<CredibilityModel>>> observable = RetrofitFactory.getInstance().getEngineerall();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<CredibilityModel>>(this) {
                    @Override
                    protected void onHandleSuccess(List<CredibilityModel> payInfoModel) {
                        sellerInfos.clear();
                        sellerInfos.addAll(payInfoModel);
                        sellerInfoAdapter.notifyDataSetChanged();
                        dimissLoadingDialog();
                    }

                    @Override
                    protected void onHandleError(String msg, int code) {
                        super.onHandleError(msg, code);
                        dimissLoadingDialog();
                    }
                });
    }
    /**
     * 查询单个维修师傅信息
     */
    private void getEngineer(String engineerId) {
        showLoadingDialog();
        Observable<BaseEntity<CredibilityModel>> observable = RetrofitFactory.getInstance().getEngineerByengineerInfop(engineerId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<CredibilityModel>(this) {
                    @Override
                    protected void onHandleSuccess(CredibilityModel payInfoModel) {
                        Logger.e(TAG, payInfoModel.toString() );
                        dimissLoadingDialog();
                        if(sellerInfos != null){
                            sellerInfos.clear();
                            sellerInfos.add(payInfoModel);
                            sellerInfoAdapter.notifyDataSetChanged();
                        }
                    }

                    //重写，取消toast
                    @Override
                    protected void onHandleError(String msg,int code) {
//                        super.onHandleError(msg);
                        dimissLoadingDialog();
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {
        Logger.e(TAG, "afterTextChanged: "  + s);
        if(s != null){
            String keyword = s.toString().trim();
            if(!TextUtils.isEmpty(keyword)){
                Logger.e(TAG, "afterTextChanged: " );
                getEngineer(keyword);
            }else {//清空输入框，重新查询数据
                getEngineerall();
            }
        }

    }
}
