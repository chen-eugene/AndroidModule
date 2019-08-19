package com.eugene.aop.bindview.annotation;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import java.lang.reflect.Field;

public class ButterKnife {

    public static void bind(Activity activity) {
        Class clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 如果这个字段有注入的注解
            if (field.isAnnotationPresent(BindView.class)) {
                // 获取注解对象
                BindView b = field.getAnnotation(BindView.class);
                int value = b.value();
                // 私有属性需要设置访问权限
                field.setAccessible(true);
                View view = null;
                try {
                    view = activity.findViewById(value);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                try {
                    if (view != null) {
                        View v = (View) view;
                        // 获取点击事件的触发的方法名称
                        String methodName = b.click();
                        EventListener eventListener = null;
                        if (!TextUtils.isEmpty(methodName)) {
                            eventListener = new EventListener(activity);
                            v.setOnClickListener(eventListener);
                            eventListener.setClickMethodName(methodName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
