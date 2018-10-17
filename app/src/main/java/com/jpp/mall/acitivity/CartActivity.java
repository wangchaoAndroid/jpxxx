package com.jpp.mall.acitivity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jpp.mall.Contanst;
import com.jpp.mall.R;
import com.jpp.mall.adapter.CartAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.CartInfoJson;
import com.jpp.mall.bean.CommonModel;
import com.jpp.mall.bean.SureOrderModer;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class CartActivity extends BaseActivity implements CartAdapter.OnNumChangeListener, CartAdapter.OnGoodsSelectedListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.orderRv)
    SwipeMenuRecyclerView orderRv;
    @BindView(R.id.sum)
    TextView sum;

    @BindView(R.id.total_rl)
    RelativeLayout totalRl;
    @BindView(R.id.right_btn)
    ImageView right_btn;
    @BindView(R.id.sumbit_tv)
    TextView submitTv;
    private static final String TAG = "CartActivity";
    double sumMoney = 0;
    public SureOrderModer sureOrderModer;
    private CartAdapter sellerOrderAdapter;
    private List<SureOrderModer.CartOrder> datas = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_cart);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        StackManager.getInstance().pushActivity(this);
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cart;
    }
    @Override
    public void initData() {
        title.setText(R.string.cart);
        // 设置菜单创建器。
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setImageResource(R.drawable.select_all);
        orderRv.setSwipeMenuCreator(mSwipeMenuCreator);
// 设置菜单Item点击监听。
        orderRv.setSwipeMenuItemClickListener(mMenuItemClickListener);
        mLayoutManager = new LinearLayoutManager(this);
        orderRv.setLayoutManager(mLayoutManager);
        sellerOrderAdapter = new CartAdapter(datas,CartActivity.this);
        orderRv.setAdapter(sellerOrderAdapter);
        sellerOrderAdapter.setOnNumChangeListener(CartActivity.this);
        sellerOrderAdapter.setGoodsSelectedListener(this);
        orderRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        getCartList();
    }
    public void getCartList() {
        showLoadingDialog();
        String token = (String) SpUtils.get(CartActivity.this,"token","");
        Observable<SureOrderModer> observable = RetrofitFactory.getInstance().getCarts(token);
        observable.compose(compose(this.<SureOrderModer>bindToLifecycle())).subscribe(
                new Consumer<SureOrderModer>() {
                    @Override
                    public void accept(SureOrderModer brandModel) throws Exception {
                        if(brandModel != null && "1".equals(brandModel.code)){
                            Logger.e(TAG,brandModel.toString());
                            datas.clear();
                            datas.addAll(brandModel.data);
                            sellerOrderAdapter.notifyDataSetChanged();
                            sureOrderModer = brandModel;
                            if(!datas.isEmpty()){
                                totalRl.setVisibility(View.VISIBLE);
                            }
                        }
                        dimissLoadingDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dimissLoadingDialog();
                    }
                }
        );
    }

    /**
     * RecyclerView的Item中的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                if(sureOrderModer !=null && !datas.isEmpty()){
                    SureOrderModer.CartOrder cartOrder = datas.get(adapterPosition);
                    deletCart(adapterPosition ,cartOrder.cartId);
                }

                //点击删除
              //  Toast.makeText(CartActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
               // Toast.makeText(CartActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * 删除购物车
     * @param pos
     * @param cartId
     */
    private void deletCart(final int pos,String cartId) {
        showLoadingDialog();
        String token = (String) SpUtils.get(CartActivity.this,"token","");
        Observable<CommonModel> observable = RetrofitFactory.getInstance().deleteCart(token,cartId);
        observable.compose(compose(this.<CommonModel>bindToLifecycle())).subscribe(
                new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) throws Exception {
                        if(commonModel != null && "1".equals(commonModel.code)){
                            Logger.e(TAG, commonModel.toString());
                            if(sellerOrderAdapter != null && !datas.isEmpty()){
                                datas.remove(datas.get(pos));
                               // sellerOrderAdapter.notifyItemRemoved(pos);
                                sellerOrderAdapter.notifyDataSetChanged();
                                if(datas.isEmpty()){
                                    sumMoney = 0;
                                    totalRl.setVisibility(View.GONE);
                                }
                            }
                            dimissLoadingDialog();
                            ToastUtils.showShortToast(CartActivity.this,"删除成功");
                        }else {
                            dimissLoadingDialog();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //Log.e("111",throwable.getMessage());
                        dimissLoadingDialog();
                    }
                }
        );
    }

    /**
     * 编辑购物车
     * @param pos
     * @param num
     */
    public void editCart(final int pos,final int num){
        showLoadingDialog();
        String token = (String) SpUtils.get(CartActivity.this,"token","");
        SureOrderModer.CartOrder cartOrder = datas.get(pos);
        CartInfoJson cartInfoJson = new CartInfoJson();
        cartInfoJson.cartId = cartOrder.cartId;
        cartInfoJson.num = num;
        Gson gson = new Gson();
        List<CartInfoJson> cartInfoJsons = new ArrayList<>();
        cartInfoJsons.add(cartInfoJson);
        String s = gson.toJson(cartInfoJsons);
        Logger.e(TAG, "editCart: " + s );
        Observable<CommonModel> observable = RetrofitFactory.getInstance().editCart(token,s);
        observable.compose(compose(this.<CommonModel>bindToLifecycle())).subscribe(
                new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel)  {
                        dimissLoadingDialog();
                        if(commonModel != null && "1".equals(commonModel.code)){
                            Logger.e(TAG, commonModel.toString());
                            //ToastUtils.showShortToast(CartActivity.this,"修改成功");
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //Log.e("111",throwable.getMessage());
                    }
                }
        );
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_80);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

//            SwipeMenuItem addItem = new SwipeMenuItem(CartActivity.this)
//                    .setBackground(R.drawable.selector_green)
//                    .setImage(R.drawable.ic_add)
//                    .setWidth(width)
//                    .setHeight(height);
//            swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

            SwipeMenuItem closeItem = new SwipeMenuItem(CartActivity.this)
                    .setBackground(R.drawable.selector_green)
                    .setImage(R.mipmap.delete)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(closeItem); // 添加菜单到右侧。
        }
    };

    @OnClick(R.id.back)
    public void back(View view){

        finish();
    }


    private boolean isSelectAll;
    @OnClick(R.id.right_btn)
    public void selectAll(View view){
       sellerOrderAdapter.setSelectAll();


    }

    @OnClick(R.id.sumbit_tv)
    public void submitCart(View view){
        Map<Integer, Boolean> selectedGoods = sellerOrderAdapter.getSelectedGoods();
        Set<Integer> integers = selectedGoods.keySet();
        SureOrderModer orderModer = new SureOrderModer();
        orderModer.data = new ArrayList<>();
        for(int i : integers){
            if(selectedGoods.get(i)){
                SureOrderModer.CartOrder cartOrder = sureOrderModer.data.get(i);
                Logger.e(TAG,"cartOrder ---" + cartOrder.toString() );
                orderModer.data.add(sureOrderModer.data.get(i));
            }
        }
        Intent intent = new Intent(this, SureOrderActivity.class);
        intent.putExtra(Contanst.GOODS_BY_CART,orderModer);
        startActivity(intent);
    }

    @Override
    public void onNumChange(int amount,int position) {
        if(sureOrderModer != null){
            sureOrderModer.data.get(position).num = amount;
            double sumMonmey = 0;
            for(SureOrderModer.CartOrder cartOrder : datas){
                if(cartOrder != sureOrderModer.data){
                    sumMonmey += (cartOrder.price * cartOrder.num);
                }else {
                    sumMonmey += cartOrder.price * amount;
                }
            }
            totalRl.setVisibility(View.VISIBLE);
            sum.setText("合计：￥" +   sumMonmey);
            editCart(position,amount);
        }
    }


    @Override
    public void onGoodsSelectedChange(Map<Integer,Boolean> goodsSelectedMap) {
        if(goodsSelectedMap != null && !goodsSelectedMap.isEmpty()){
            sumMoney = 0;
            Set<Integer> integers = goodsSelectedMap.keySet();
            for(int i : integers){
                if(goodsSelectedMap.get(i)){
                    SureOrderModer.CartOrder cartOrder = datas.get(i);
                    sumMoney += (cartOrder.price * cartOrder.num);
                }
            }
            sum.setText("合计:￥" + sumMoney);
        }
    }
}
