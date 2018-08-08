package com.jpp.mall.download;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.jpp.mall.App;
import com.jpp.mall.net.ResponseCallback;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.StreamUtil;

import java.io.File;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Create by wangchao on 2017/12/13 09:44
 * 下载管理类
 */
public class DownloadManager {
    private static volatile DownloadManager downloadManager;

    private static final String DEFAULT_TYPE = "apk";

    public static final String TYPE_APK = "apk";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_VIDEO = "video";

    private String type ;

    public static DownloadManager getInstance(){
        if(downloadManager == null){
            synchronized (DownloadManager.class){
                if(downloadManager == null){
                    downloadManager = new DownloadManager();
                }
            }
        }
        return downloadManager;
    }


    /**
     * 获取更新信息
     * @param packageName
     * @param callback
     */
    public void upgradeInfo(String packageName, String appVersion , final ResponseCallback<ApkUpgradeInfo> callback){
        RetrofitFactory.getInstance().getUpdatesInterface(packageName,appVersion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }


    /**
     * 下载更新apk
     * @param url
     * @param file
     * @param callback
     */
    public void download(String url , final File file , final Callback callback) {
        download(url,file,DEFAULT_TYPE,callback);
    }


    /**
     * @param url  下载url
     * @param file  下载的文件
     * @param type      下载类型
     * @param callback  下载进度的回调
     */
    public void download(String url , final File file , String type, final Callback callback){
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(RetrofitFactory.BASE_URL);
        OkHttpClient.Builder builder = ProgressHelper.addProgress(new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                //
                if(callback != null){
                    callback.onProgress((int) ((100 * progress) / total));
                }
                if(file.length() == total){
                    installApk(file.getAbsolutePath());
                }

            }
        });
        DownloadApi downloadApi = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);
        downloadApi.retrofitDownload(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@NonNull ResponseBody responseBody) throws Exception {
                        InputStream inputStream = responseBody.byteStream();
                        StreamUtil.converyStream2File(inputStream,file);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    private void installApk(String filePath){
            // TODO Auto-generated method stub
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        App.getAppContext().startActivity(intent);
    }


}
