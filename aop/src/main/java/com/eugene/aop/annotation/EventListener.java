package com.eugene.aop.annotation;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Method;

public class EventListener implements View.OnClickListener {
    private String TAG = "EventListener";

    private Object receiver = null;

    private String clickMethodName = "";

    public EventListener(Object receiver) {
        this.receiver = receiver;
    }

    public void setClickMethodName(String clickMethodName) {
        this.clickMethodName = clickMethodName;
    }

    @Override
    public void onClick(View v) {
        Method method = null;
        try {
            method = receiver.getClass().getMethod(clickMethodName);
            if (method != null) {
                method.invoke(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "未找到：" + clickMethodName + "方法");
        }


        try {
            if (method == null) {
                method = receiver.getClass().getMethod(clickMethodName, View.class);
                if (method != null) {
                    method.invoke(receiver, v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "未找到带view类型参数的：" + clickMethodName + "方法");
        }
    }
}