package com.jpp.mall.acitivity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.SelectComponentAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.ComponetModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.GridSpacingItemDecoration;
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

public class SelectComponentActivity extends BaseActivity implements SelectComponentAdapter.OnItemClickListener {
    private static final String TAG = "SelectComponentActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.selectComponentRv)
    RecyclerView selectComponentRv;
    @BindView(R.id.rotate_header_grid_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    private List<ComponetModel.Component> datas = new ArrayList<>();
    private SelectComponentAdapter selectComponentAdapter;

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
//        setContentView(R.layout.activity_select_component);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_component;
    }
    @Override
    public void initData() {
        title.setText(R.string.select_component);
        //设置自定义头部样式
        UltraCustomerHeader.setUltraCustomerHeader(mPtrFrame, this);
//设置下拉刷新上拉加载
        mPtrFrame.disableWhenHorizontalMove(true);//解决横向滑动冲突
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                    getCommodityList(TYPE_REFRESH);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                getCommodityList(TYPE_LOAD);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if(hasMore){
                    return super.checkCanDoLoadMore(frame, selectComponentRv, footer);
                }else {
                    return false;
                }

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, selectComponentRv, header);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        selectComponentRv.setLayoutManager(gridLayoutManager);
        datas = new ArrayList<>();
        selectComponentAdapter = new SelectComponentAdapter(datas,this);
        selectComponentAdapter.setOnItemClickListener(this);
        selectComponentRv.addItemDecoration(new GridSpacingItemDecoration(3,30,false));
        selectComponentRv.setAdapter(selectComponentAdapter);
        getCommodityList(TYPE_REFRESH);
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public void onItemClick(int pos) {
        String goodsId = datas.get(pos).goodsId;
        Intent intent = new Intent(SelectComponentActivity.this, RoundDetailActivity.class);
        intent.putExtra("goodsId",goodsId);
        startActivity(intent);
    }

    /**
     * 获取配件列表
     */
    public void getCommodityList( final int type) {
        String token = (String) SpUtils.get(this,"token","");
        if(type == TYPE_LOAD){
            page++;
        }else if(type == TYPE_REFRESH){
            page = 1;
            datas.clear();
        }
        showLoadingDialog();
        Observable<BaseEntity<ComponetModel>> observable = RetrofitFactory.getInstance().getCommodityList("",token,page);
        observable.compose(compose(this.<BaseEntity<ComponetModel>>bindToLifecycle())).subscribe(new BaseObserver<ComponetModel>(this) {
            @Override
            protected void onHandleSuccess(ComponetModel componetModels) {
                datas.addAll(componetModels.goodsList);
                hasMore = componetModels.hasMore == 1;
                selectComponentAdapter.notifyDataSetChanged();
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
