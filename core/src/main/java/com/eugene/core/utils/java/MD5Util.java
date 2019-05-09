package com.eugene.core.utils.java;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MD5Util {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String encrypt(String filename) {
        String md5Str = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5Str = toHexString(md5.digest());
        } catch (Exception e) {
            System.out.println("error");
        }
        return TextUtils.isEmpty(md5Str) ? "" : md5Str;
    }


}
