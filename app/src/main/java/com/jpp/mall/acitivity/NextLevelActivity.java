package com.jpp.mall.acitivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.NextLevelAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.NextLevelModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class NextLevelActivity extends BaseActivity implements NextLevelAdapter.OnItemClickListener {
    private static final String TAG = "NextLevelActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.nextLevelRv)
    RecyclerView nextLevelRv;
    private List<NextLevelModel> mDatas;
    private NextLevelAdapter mNextLevelAdapter;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_next_level);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_next_level;
    }
    @Override
    public void initData() {
        title.setText(R.string.next_level);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        nextLevelRv.setLayoutManager(linearLayoutManager);
        mDatas = new ArrayList<>();
        mNextLevelAdapter = new NextLevelAdapter(mDatas,this);
        mNextLevelAdapter.setOnItemClickListener(this);
        nextLevelRv.setAdapter(mNextLevelAdapter);
        getNextLevelShops();
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public void onItemClick(int pos) {

    }


    public void getNextLevelShops(){
        showLoadingDialog();
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity<List<NextLevelModel>>> observable1 = RetrofitFactory.getInstance().getNextLevelShops(token);
        observable1.compose(compose(this.<BaseEntity<List<NextLevelModel>>>bindToLifecycle())).subscribe(new BaseObserver<List<NextLevelModel>>(NextLevelActivity.this) {
            @Override
            protected void onHandleSuccess(List<NextLevelModel> nextLevelModel) {
                Logger.e(TAG, "onHandleSuccess: "+ nextLevelModel.toString() );
                mDatas.clear();
                mDatas.addAll(nextLevelModel);
                mNextLevelAdapter.notifyDataSetChanged();
                dimissLoadingDialog();
            }

            @Override
            protected void onHandleError(String msg,int code) {
                super.onHandleError(msg,code);
                Logger.e(TAG,msg);
                dimissLoadingDialog();
            }
        });
    }

}
