package com.eugene.core.utils.java;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 属性配置文件管理
 */

public class PropertiesUtil {

    private static PropertiesUtil proUtil;
    private Context context;

    private PropertiesUtil(Context context) {
        this.context = context;
    }

    public static PropertiesUtil getInstance(Context context) {
        if (proUtil == null) {
            synchronized (PropertiesUtil.class) {
                if (proUtil == null) {
                    proUtil = new PropertiesUtil(context);
                }
            }
        }
        return proUtil;
    }

    /**
     * 获取.properties文件的值
     * @param key
     * @return
     */
    public String getProperty(String key) {
        Properties pro = new Properties();
        String value = "";
        InputStream is = null;
        try {
            is = context.getAssets().open("config.properties");
            pro.load(is);
            value = pro.getProperty(key);//读取属性值
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 保存.properties文件的值
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        Properties pro = new Properties();
        //建立输入流，关联Properties和.properties文件
        InputStream is = null;
        try {
            is = context.getAssets().open("config.properties");
            pro.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //通过输出流，写入.properties文件
        OutputStream os = null;
        File file = new File("/assets/config.properties");
        try {
            os = new FileOutputStream(file);
            pro.setProperty(key, value);
            pro.store(os, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
