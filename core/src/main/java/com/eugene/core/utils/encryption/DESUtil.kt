package com.eugene.core.utils.encryption

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

/**
 * 对称加密算法
 * 密钥长度较小(56位)
 */
class DESUtil {

    companion object {
        /**
         * @param encryptStr : 需要加密的数据
         * @param encryptKey : 加密的密匙
         * @return Array<Byte> : 加密结果
         */
        fun encrypt(encryptStr: String, encryptKey: String): ByteArray {

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val desKey = DESKeySpec(encryptKey.toByteArray(Charsets.UTF_8))
            val securitykey = keyFactory.generateSecret(desKey)

            // Cipher对象实际完成加密操作
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            // 偏移量
            val iv = IvParameterSpec(encryptKey.toByteArray(Charsets.UTF_8))
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securitykey, iv)
            // 执行加密操作
            return cipher.doFinal(encryptStr.toByteArray(Charsets.UTF_8))
        }

        /**
         * 功能描述：DES加密方法。
         * base64(des(src,key))
         * @param encryptString String类型 要加密的数据
         * @param encryptKey  String类型 加密的密钥
         * @return byte[]类型 加密后结果
         */
        fun encryptBase64(encryptString: String, encryptKey: String): ByteArray {
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val desKey = DESKeySpec(encryptKey.toByteArray())
            val securekey = keyFactory.generateSecret(desKey)
            // Cipher对象实际完成加密操作
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            // 偏移量
            val iv = IvParameterSpec(encryptKey.toByteArray())
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, iv)
            // 执行加密操作
            val doFinal = cipher.doFinal(encryptString.toByteArray())
            //使用base64再进行一道加密操作
            return Base64Util.encode(doFinal.toString(Charsets.UTF_8))
        }

        /**
         * 功能描述：DES解密  方法。
         * @param decryptString byte[]类型 要解密的数据（需要使用Base64将字符串转换成byte[]）
         * @param decryptKey String类型 解密时使用的KEY
         * @return String类型 解密后结果
         */
        fun decrypt(decryptString: String, decryptKey: String): ByteArray {
            // 创建一个密匙工厂
            val keyFactory = SecretKeyFactory.getInstance("DES")
            // 创建一个DESKeySpec对象
            val desKey = DESKeySpec(decryptKey.toByteArray(charset("UTF-8")))
            // 将DESKeySpec对象转换成SecretKey对象
            val securitykey = keyFactory.generateSecret(desKey)
            // Cipher对象实际完成解密操作
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            // 偏移量
            val iv = IvParameterSpec(decryptKey.toByteArray(charset("UTF-8")))
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securitykey, iv)
            // 执行解密操作
            return cipher.doFinal(decryptString.toByteArray())
        }

        /**
         * 功能描述：DES解密  方法。
         * decryptBase64--->decrtpyDes
         * @param decryptString byte[]类型 要解密的数据（需要使用Base64将字符串转换成byte[]）
         * @param decryptKey String类型 解密时使用的KEY
         * @return String类型 解密后结果
         */
        fun decryptBase64(decryptString: String, decryptKey: String): ByteArray {
            val decode = Base64Util.decode(decryptString)
            // 创建一个密匙工厂
            val keyFactory = SecretKeyFactory.getInstance("DES")
            // 创建一个DESKeySpec对象
            val desKey = DESKeySpec(decryptKey.toByteArray())
            // 将DESKeySpec对象转换成SecretKey对象
            val securitykey = keyFactory.generateSecret(desKey)
            // Cipher对象实际完成解密操作
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            // 偏移量
            val iv = IvParameterSpec(decryptKey.toByteArray())
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securitykey, iv)
            // 执行解密操作
            return cipher.doFinal(decode)
        }
    }
}