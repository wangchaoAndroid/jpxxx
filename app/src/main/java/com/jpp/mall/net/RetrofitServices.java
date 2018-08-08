package com.jpp.mall.net;

import com.jpp.mall.bean.BankCardModel;
import com.jpp.mall.bean.BrandModel;
import com.jpp.mall.bean.CityModel;
import com.jpp.mall.bean.CommonModel;
import com.jpp.mall.bean.ComponetModel;
import com.jpp.mall.bean.CredibilityModel;
import com.jpp.mall.bean.DetailModel;
import com.jpp.mall.bean.ExpressModel;
import com.jpp.mall.bean.HomeResponse;
import com.jpp.mall.bean.LoginModel;
import com.jpp.mall.bean.NextLevelModel;
import com.jpp.mall.bean.NotifyModel;
import com.jpp.mall.bean.OrdelDetail;
import com.jpp.mall.bean.PayInfoModel;
import com.jpp.mall.bean.PlaceOrderModel;
import com.jpp.mall.bean.RegeistModel;
import com.jpp.mall.bean.RoundDetailModel;
import com.jpp.mall.bean.SearchModel;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.bean.ShopInfoModel;
import com.jpp.mall.bean.SureOrderModer;
import com.jpp.mall.bean.UnReadCount;
import com.jpp.mall.bean.WalletModel;
import com.jpp.mall.download.ApkUpgradeInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface RetrofitServices {

    /**
     * 商家登录
     * @param password
     * @return
     */
//    @FormUrlEncoded
    @GET("user/login")
    Observable<BaseEntity<LoginModel>> login(
            @Query("phone") String account,
            @Query("password") String password,
            @Query("UUID") String uuid,
            @Query("client") String client,
            @Query("userType")String userType
    );
    /**
     * 忘記密碼
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/resetPassword")
    Observable<BaseEntity> forgetPwd(
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("code") String code,
            @Field("userType")String userType
    );

    /**
     * 商家注册1
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/regist")
    Observable<BaseEntity<RegeistModel>> regeist(
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("client") String client,
            @Field("code") String code,
            @Field("UUID") String uuid,
            @Field("userType")String userType
    );

    /**
     * 商家注册2
     * @return
     */
    @FormUrlEncoded
    @POST("shop/registShopInfo")
    Observable<BaseEntity<RegeistModel>> regeist2(
            @Field("token") String token,
            @Field("name") String name,
            @Field("address") String fullAddres,
            @Field("inviteCode") String inviteCode,
            @Field("areaId") String areaId
    );

    /**
     * 商家信息
     * @param token
     * @return
     */
    @GET("user/getUserInfo")
    Observable<BaseEntity<ShopInfoModel>> getShopInfo(@Query("token") String token);

    /**
     * 钱包信息
     * @param token
     * @return
     */
    @GET("wallet/getMyWallet")
    Observable<BaseEntity<WalletModel>> getWalletInfo(@Query("token") String token,@Query("page") int page);

    /**
     * 验证码
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("common/sendCode")
    Observable<BaseEntity> sendMsg(
            @Field("phone") String phone
    );

    /**
     * 品牌机型列表
     * @return
     */
    @GET("brand/getBrandList")
    Observable<BrandModel> getAllBrand();

    /**
     * 查询品牌机型
     * @return
     */
    @GET("brand/searchModels")
    Observable<BaseEntity<List<SearchModel>>> queryBrand(@Query("keywords")String keywords
                                        , @Query("brandId")String brandId);

    /**
     * 周边列表
     * @return
     */
    @GET("goods/getGoodsList")
    Observable<BaseEntity<ComponetModel>> getCommodityList(@Query("classId") String classId,
                                                                 @Query("token")String token,
                                                                 @Query("page") int  page);

    /**
     * 获取购物车
     * @return
     */
    @GET("cart/getCart")
    Observable<SureOrderModer> getCarts(@Query("token") String token);

    /**
     * 删除购物车
     * @return
     */
    @GET("cart/removeCart")
    Observable<CommonModel> deleteCart(@Query("token") String token, @Query("cartId") String cartId);

    /**
     * 编辑购物车
     * @return
     */
    @FormUrlEncoded
    @POST("cart/updateCart")
    Observable<CommonModel> editCart(@Field("token") String token, @Field("cartInfoJson") String cartInfoJson);

    /**
     * 添加购物车
     * @return
     */
    @GET("cart/addCart")
    Observable<CommonModel> addCarts(@Query("token")String token
            , @Query("num")int  amount
            , @Query("skuId")String skuid
            , @Query("goodsId")String goodsId);

    /**
     * 获取商品详情
     */
    @GET("goods/getModelDetail")
    Observable<BaseEntity<DetailModel>> getDetail1(@Query("token") String token, @Query("modelId") String modelId);



    /**
     * 获取周边详情
     */
    @GET("goods/getGoodsDetail")
    Observable<BaseEntity<RoundDetailModel>> getRoundDetail1(@Query("token") String token, @Query("goodsId") String goodsId);

    /**
     * 获取平台订单
     */
    @GET("order/getOrderList")
    Observable<SellerOrder> getSellerOrder(@Query("token") String token, @Query("status") String status);

    /**
     * 获取用户订单
     */
    @GET("order/getRepairOrders")
    Observable<SellerOrder> getMemberOrders(@Query("token") String token, @Query("status") String status);


    /**
     * 订单详情
     */
    @GET("order/getOrderDetial")
    Observable<BaseEntity<OrdelDetail>> getOrderDetial(@Query("token") String token, @Query("orderId") String orderId);


    /**
     * 取消订单
     */
    @GET("order/cancelOrder")
    Observable<BaseEntity> cancelOrder(@Query("token") String token, @Query("orderId") String orderId);


    /**
     * 接受订单
     */
    @GET("order/acceptOrder")
    Observable<BaseEntity> acceptOrder(@Query("token") String token, @Query("orderId") String orderId);

    /**
     * 确认订单
     */
    @GET("order/confirmOrder")
    Observable<BaseEntity> confirmOrder(@Query("token") String token
            , @Query("orderId") String orderId
            ,@Query("secureCode") String secureCode);

    @GET("order/confirmOrder")
    Observable<BaseEntity> confirmOrderShop(@Query("token") String token
            , @Query("orderId") String orderId);

    /**
     * 删除订单
     */
    @GET("order/removeOrder")
    Observable<BaseEntity> removeOrder(@Query("token") String token
            , @Query("orderId") String orderId
    );


    /**
     * 拒绝订单
     */
    @GET("order/refuseOrder")
    Observable<BaseEntity> refuseOrder(@Query("token") String token, @Query("orderId") String orderId,@Query("desc")String desc);

    /**
     * 获取主页
     */
    @GET("home/getHomePage")
    Observable<BaseEntity<HomeResponse>> getHomePage();

    /**
     * 下单
     */
    @FormUrlEncoded
    @POST("order/placeOrder")
    Observable<BaseEntity<PlaceOrderModel>> placeOrder(
            @Field("token")String token
    , @Field("goodsJson") String goodsJson
    , @Field("receiver") String receiver
    , @Field("receiverPhone") String receiverPhone
    , @Field("fullAddress") String fullAddress
    , @Field("orderType")int orderType
    , @Field("shopId")String shopId
    ,@Field("repairId")String repairId
    ,@Field("expressId")String expressId);


    /**
     * 获取城市列表
     * @return
     */
    @GET("common/getRegions")
    Observable<CityModel> getCities();

    /**
     * 推送
     * @return
     */
    @GET("common/pushNotiTest")
    Observable<ResponseBody> test(@Query("token") String token);

    /**
     * 添加收货地址
     * @return
     */
    @FormUrlEncoded
    @POST("address/addAddress")
    Observable<BaseEntity> getReceptAddress(
            @Field("token")String token,
            @Field("fullAddress")String fullAddress,
            @Field("receiver")String receiver,
            @Field("phone")String phone

    );


    /**
     * 收货地址列表
     * @return
     */
    @FormUrlEncoded
    @POST("address/addAddress")
    Observable<BaseEntity> getReceptAddressList(@Field("token")String token);

    /**
     * 修改默认地址
     * @return
     */
    @GET("address/setCurrentAddress")
    Observable<BaseEntity> modifyAddress(
            @Query("token")String token,
            @Query("addressId")String addressId

    );

    @GET("pay/payForOrder")
    Observable<BaseEntity<PayInfoModel>> getPayInfo1(
            @Query("orderCode")String orderCode,
            @Query("payType")int payType,
            @Query("token")String token

    );

    @GET("pay/payForOrder")
    Observable<BaseEntity<String>> getPayInfo(
            @Query("orderCode")String orderCode,
            @Query("payType")int payType,
            @Query("token")String token

    );

    @GET("common/getEngineerByNumber")
    Observable<BaseEntity<CredibilityModel>> getEngineerByengineerInfop(
            @Query("number")String number
    );

    @GET("common/getEngineerList")
    Observable<BaseEntity<List<CredibilityModel>>> getEngineerall(
    );

    @GET("shop/getSubShopList")
    Observable<BaseEntity<List<NextLevelModel>>> getNextLevelShops(
            @Query("token")String token
    );

    @GET("msg/getPushNotiMsg")
    Observable<BaseEntity<NotifyModel>> getAllMsg(
                    @Query("token")String token,
                    @Query("page")int  page,
                    @Query("sortType")int sortType
            );


    @GET("msg/updateAllMsgAsRead")
    Observable<BaseEntity> updateAllMsgAsRead(
            @Query("token")String token
    );

    @GET("msg/getUnReadCount")
    Observable<BaseEntity<UnReadCount>> getUnReadCount(
            @Query("token")String token
    );

    @GET("user/getBankList")
    Observable<BaseEntity<List<BankCardModel>>> getBankList(@Query("token") String token);

    @FormUrlEncoded
    @POST("user/saveBankCard")
    Observable<BaseEntity> bindBankCard(@Field("token") String token,
                                       @Field("bankCard")String bankCard,
                                       @Field("bankName")String bankName,
                                       @Field("bankOpening")String bankOpening,
                                       @Field("phone")String phone);

    @FormUrlEncoded
    @POST("user/saveIDCard")
    Observable<BaseEntity> bindIdCard(@Field("token") String token,
                                        @Field("name")String bankCard,
                                        @Field("idCard")String bankName);

    @FormUrlEncoded
    @POST("wallet/setupPayPassword")
    Observable<BaseEntity> setupPayPassword(@Field("token") String token,
                                      @Field("code")String code,
                                      @Field("password")String password);

    @GET("common/getExpressList")
    Observable<BaseEntity<List<ExpressModel>>> getExpressList();

    @FormUrlEncoded
    @POST("lygetUpdatesInterface")
    Observable<ApkUpgradeInfo> getUpdatesInterface(@Field("packageName") String appVersions, @Field("appVersions")String appCode);
}
