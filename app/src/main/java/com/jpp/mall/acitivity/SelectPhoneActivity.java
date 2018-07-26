package com.jpp.mall.acitivity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.BranchAdapter;
import com.jpp.mall.adapter.CategoryAdapter;
import com.jpp.mall.adapter.SearchAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.BrandModel;
import com.jpp.mall.bean.SearchModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.GridSpacingItemDecoration;
import com.jpp.mall.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectPhoneActivity extends BaseActivity implements TextWatcher {
    private static final String TAG = "SelectPhoneActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.branchRv)
    RecyclerView branchRv;
    @BindView(R.id.categoryRv)
    RecyclerView categoryRv;
    @BindView(R.id.searchRv)
    RecyclerView searchRv;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @BindView(R.id.search_et)
    EditText search_et;
    private List<BrandModel.Data> branchs;
    private BranchAdapter branchAdapter;
    private SearchAdapter searchAdapter;
    private List<BrandModel.Model> categories;
    private CategoryAdapter categoryAdapter;
    private List<SearchModel> searchModelsList = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_select_phone;
    }
    @Override
    public void initData() {
        title.setText(R.string.select_phone);
        searchRv.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManagerBr = new LinearLayoutManager(SelectPhoneActivity.this);
        LinearLayoutManager linearLayoutManagerSearch = new LinearLayoutManager(SelectPhoneActivity.this);
        GridLayoutManager linearLayoutManagerCa = new GridLayoutManager(SelectPhoneActivity.this,3);
        branchRv.setLayoutManager(linearLayoutManagerBr);
        categoryRv.setLayoutManager(linearLayoutManagerCa);
        searchRv.setLayoutManager(linearLayoutManagerSearch);
        branchs = new ArrayList<>();
        branchAdapter = new BranchAdapter(branchs,this);
        searchAdapter = new SearchAdapter(searchModelsList,this);
        categoryRv.addItemDecoration(new GridSpacingItemDecoration(3,20,false));
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categories,this);
        branchRv.setAdapter(branchAdapter);
        categoryRv.setAdapter(categoryAdapter);
        searchRv.setAdapter(searchAdapter);
        branchAdapter.setOnItemClickListener(new BranchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                if(branchs != null && !branchs.isEmpty()){
                    categories.clear();
                    categories.addAll(branchs.get(pos).models);
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                if(categories != null && !categories.isEmpty()){
                    BrandModel.Model model = categories.get(pos);
                    if(model != null){
                        String modelId = model.modelId;
                        Intent intent = new Intent(SelectPhoneActivity.this, DetailActivity.class);
                        intent.putExtra("modelId",modelId);
                        startActivity(intent);
                    }
                }

            }
        });
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                SearchModel searchModel = searchModelsList.get(pos);
                if(searchModel != null){
                    String modelId = searchModel.modelId;
                    Intent intent = new Intent(SelectPhoneActivity.this, DetailActivity.class);
                    intent.putExtra("modelId",modelId);
                    startActivity(intent);
                }
            }
        });
        search_et.addTextChangedListener(this);

        getAllBrand();
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    public void getAllBrand(){
        showLoadingDialog();
        Observable<BrandModel> observable = RetrofitFactory.getInstance().getAllBrand();
        observable.compose(compose(this.<BrandModel>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BrandModel>() {
                    @Override
                    public void accept(BrandModel brandModel) throws Exception {
                        dimissLoadingDialog();
                        if(brandModel != null){
                            branchs.clear();
                            branchs.addAll(brandModel.data);
                            branchAdapter.notifyDataSetChanged();
                            categories.addAll(branchs.get(0).models);
                            categoryAdapter.notifyDataSetChanged();
                        }


                        Logger.e(TAG,brandModel.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(TAG,throwable.getMessage() + "");
                        dimissLoadingDialog();
                    }
                });
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String str = editable.toString().trim();
        if(TextUtils.isEmpty(str)){
            searchModelsList.clear();
            searchAdapter.notifyDataSetChanged();
            searchRv.setVisibility(View.GONE);
        }else {
            searchRv.setVisibility(View.VISIBLE);

            Observable<BaseEntity<List<SearchModel>>> observable = RetrofitFactory.getInstance().queryBrand(str,"");
            observable.compose(compose(this.<BaseEntity<List<SearchModel>>>bindToLifecycle()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<List<SearchModel>>(this) {
                        @Override
                        protected void onHandleSuccess(List<SearchModel> searchModels) {
                            Logger.e(TAG, "onHandleSuccess: "+ searchModels.toString() );
                            searchModelsList.clear();
                            searchModelsList.addAll(searchModels);
                            searchAdapter.notifyDataSetChanged();
                        }

                        @Override
                        protected void onHandleError(String msg, int code) {
                            super.onHandleError(msg, code);
                        }
                    });
        }

    }
}
