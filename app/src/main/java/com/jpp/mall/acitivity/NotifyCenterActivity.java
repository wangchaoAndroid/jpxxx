package com.jpp.mall.acitivity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.MsgAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.NotifyModel;
import com.jpp.mall.event.MsgEvent;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.utils.UltraCustomerHeader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;

public class NotifyCenterActivity extends BaseActivity implements MsgAdapter.OnItemClickListener {
    private static final String TAG = "NotifyCenterActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_btn)
    ImageView right_btn;
    @BindView(R.id.msgRV)
    RecyclerView msgRv;
    @BindView(R.id.rotate_header_grid_view_frame)
    PtrClassicFrameLayout mPtrFrame;

    private List<NotifyModel.Msg> mDatas = new ArrayList<>();
    private MsgAdapter msgAdapter;

    private static final int Default_Sort_type = 1;

    private int sortType = Default_Sort_type;

    private int page = 1; //默认从1开始刷新

    private static final int TYPE_REFRESH = 1;
    private static final int TYPE_LOAD= 2;
    private boolean hasMore ;
    @Override
    public int getLayoutId() {
        return R.layout.activity_notify_center;
    }
    @Override
    public void initData() {
        title.setText(R.string.msg_center);
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setImageResource(R.drawable.sort_down);
        msgAdapter = new MsgAdapter(mDatas,this);
        msgAdapter.setOnItemClickListener(this);
        //设置自定义头部样式
        UltraCustomerHeader.setUltraCustomerHeader(mPtrFrame, this);
//设置下拉刷新上拉加载
        mPtrFrame.disableWhenHorizontalMove(true);//解决横向滑动冲突
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMsg(TYPE_REFRESH);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                getMsg(TYPE_LOAD);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if(hasMore){
                    return super.checkCanDoLoadMore(frame, msgRv, footer);
                }else {
                    return false;
                }

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, msgRv, header);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRv.setLayoutManager(linearLayoutManager);
        msgRv.setAdapter(msgAdapter);
        getMsg(TYPE_REFRESH);
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.right_btn)
    public void right_btn(View view){
        if(sortType == Default_Sort_type){
            sortType = 0;
            right_btn.setImageResource(R.drawable.sort_up);
        }else {
            sortType = Default_Sort_type;
            right_btn.setImageResource(R.drawable.sort_down);
        }
        getMsg(TYPE_REFRESH);
    }

    public void getMsg(final int type) {
        String token = (String) SpUtils.get(this,"token","");
        if(type == TYPE_LOAD){
            page++;
        }else if(type == TYPE_REFRESH){
            page = 1;
            mDatas.clear();
        }
        showLoadingDialog();
        Observable<BaseEntity<NotifyModel>> observable1 = RetrofitFactory.getInstance().getAllMsg(token,page,sortType);
        observable1.compose(compose(this.<BaseEntity<NotifyModel>>bindToLifecycle())).subscribe(new BaseObserver<NotifyModel>(this) {
            @Override
            protected void onHandleSuccess(NotifyModel notifyModels) {
                Logger.e(TAG, "onHandleSuccess: "+ notifyModels.toString() );
                dimissLoadingDialog();
                if(notifyModels != null){
                    hasMore = notifyModels.hasMore == 1;
                    if(page == 1){
                        readAllMsg();
                    }
                    mDatas.clear();
                    mDatas.addAll(notifyModels.msgList);
                    msgAdapter.notifyDataSetChanged();
                    mPtrFrame.refreshComplete();
                }

            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);
                dimissLoadingDialog();
            }
        });
    }

    public void readAllMsg(){
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity> observable1 = RetrofitFactory.getInstance().updateAllMsgAsRead(token);
        observable1.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this){
            @Override
            protected void onHandleSuccess(Object o) {
                Logger.e(TAG,"success");
                EventBus.getDefault().postSticky(new MsgEvent());
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
        startActivity(new Intent(NotifyCenterActivity.this,UserOrderActivity.class));
    }
}
