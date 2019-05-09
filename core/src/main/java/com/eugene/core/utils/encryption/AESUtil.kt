package com.eugene.core.utils.encryption

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * 对称加密算法
 * AES设计有三个密钥长度:128,192,256位
 */
class AESUtil {

    companion object {
        /**
         * 加密
         *  aes(src.getBytes,key)
         * @param encryptStr
         * @return
         */
        fun encrypt(encrytionStr: String, encryptionKey: String): ByteArray {
            // 返回实现指定算法的密码对象实例
            val cipher = Cipher.getInstance("AES")
            // 根据key产生指定算法的秘钥
            val secretKey = SecretKeySpec(encryptionKey.toByteArray(), "AES")
            //设置密钥和加密形式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            // 依据init,执行具体操作
            return cipher.doFinal(encrytionStr.toByteArray())
        }

        /**
         * 加密
         * base64(aes(src,key))
         * @param encryptStr
         * @return
         */
        fun encryptBase64(src: String, key: String): ByteArray {
            val cipher = Cipher.getInstance("AES")
            val secretkey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretkey)//设置密钥和加密形式
            val doFinal = cipher.doFinal(src.toByteArray())
            return Base64Util.encode(doFinal)
        }

        /**
         * 解密
         * aes(src.getBytes,key)
         * @param decryptStr
         * @return
         */
        fun decrypt(decryptByteArray: ByteArray, key: String): ByteArray {
            val cipher = Cipher.getInstance("AES")
            //设置加密Key
            val secretkey = SecretKeySpec(key.toByteArray(), "AES")
            //设置密钥和解密形式
            cipher.init(Cipher.DECRYPT_MODE, secretkey)
            return cipher.doFinal(decryptByteArray)
        }

        /**
         * 解密
         * decryptBase64--->decryptAes
         * @param decryptStr
         * @return
         */
        fun decryptBase64(decryptStr: ByteArray, key: String): ByteArray {
            val decode = Base64Util.decode(decryptStr)
            val cipher = Cipher.getInstance("AES")
            //设置加密Key
            val secretkey = SecretKeySpec(key.toByteArray(), "AES")
            //设置密钥和解密形式
            cipher.init(Cipher.DECRYPT_MODE, secretkey)
            return cipher.doFinal(decode)
        }
    }
}