package com.jpp.mall.acitivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.WalletAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.WalletModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.utils.UltraCustomerHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;

public class WalletActivity extends BaseActivity implements WalletAdapter.OnItemClickListener {
    @BindView(R.id.wallet_rv)
    RecyclerView walletRv;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.rotate_header_grid_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    private List<WalletModel.Record> datas = new ArrayList<>();
    private WalletAdapter walletAdapter;
    private boolean hasMore ;
    private int page = 1; //默认从1开始刷新

    private static final int TYPE_REFRESH = 1;
    private static final int TYPE_LOAD= 2;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_wallet);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }
    @Override
    public void initData() {
        title.setText("我的钱包");

        UltraCustomerHeader.setUltraCustomerHeader(mPtrFrame, this);
//设置下拉刷新上拉加载
        mPtrFrame.disableWhenHorizontalMove(true);//解决横向滑动冲突
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getWallInfo(TYPE_REFRESH);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                getWallInfo(TYPE_LOAD);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if(hasMore){
                    return super.checkCanDoLoadMore(frame, walletRv, footer);
                }else {
                    return false;
                }

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, walletRv, header);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        walletRv.setLayoutManager(linearLayoutManager);
        datas = new ArrayList<>();
        walletAdapter = new WalletAdapter(datas,this);
        walletAdapter.setOnItemClickListener(this);
        walletRv.setAdapter(walletAdapter);
        getWallInfo(1);
    }

    @Override
    public void onItemClick(int pos) {
        //startActivity(new Intent(WalletActivity.this,NextLevelActivity.class));
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    public void getWallInfo(int type){
        if(isDialogShowing()){
            return;
        }
        showLoadingDialog();
        String token = (String) SpUtils.get(this,"token","");
        if(type == TYPE_LOAD){
            page++;
        }else if(type == TYPE_REFRESH){
            page = 1;
            datas.clear();
        }
        Observable<BaseEntity<WalletModel>> observable = RetrofitFactory.getInstance().getWalletInfo(token,page);
        observable.compose(compose(this.<BaseEntity<WalletModel>>bindToLifecycle())).subscribe(new BaseObserver<WalletModel>(this) {
            @Override
            protected void onHandleSuccess(WalletModel walletModel) {
                // 保存用户信息等操作
                money.setText("" + walletModel.balance);
                datas.clear();
                hasMore = walletModel.hasMore == 1;
                datas.addAll(walletModel.records);
                walletAdapter.notifyDataSetChanged();
                mPtrFrame.refreshComplete();
                dimissLoadingDialog();
            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);
                dimissLoadingDialog();
            }
        });
    }

}
