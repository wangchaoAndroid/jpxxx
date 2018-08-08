package com.jpp.mall.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/4/24.
 */

public class AppConfig {
    public static String LOG_DIRPATH  = "";
    public static String LOG_FILENAME  = "log";
    public final static String SDCARD_PATH = Environment.getExternalStorageDirectory().toString().equals("/storage/sdcard1") ? "/storage/sdcard0" : Environment.getExternalStorageDirectory().toString();
    public final static String Environment_Path = SDCARD_PATH + File.separator;
    public final static String download_Dir = Environment_Path + "download/";
}
