package com.jpp.mall.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Create by wangchao on 2018/5/23 10:45
 */
public final class StackManager {
    private static final String TAG = "StackManager";
    private StackManager() {};

    private static Stack<Activity> activityStack;

    private static StackManager sManager;
    public static StackManager getInstance(){
        if(sManager == null){
            synchronized (StackManager.class){
                if(sManager == null){
                    sManager = new StackManager();
                }
            }
        }
        return sManager;
    }

    public boolean containClass(Class clazz){
        if(activityStack != null){
            return activityStack.contains(clazz);
        }else {
            return  false;
        }

    }

    public void pushActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivity(){

        while(true){
            if(activityStack.empty()){
                break;
            }
            Activity activity=currentActivity();
            popActivity(activity);
        }
    }

    public void popActivity(Activity activity){
        if(activity!=null){
            activity.finish();
            activityStack.remove(activity);
            activity=null;
        }
    }
    private Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }
}
