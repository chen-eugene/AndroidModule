package com.eugene.core.utils.encryption

import android.util.Base64

/**
 * Base64还不算加密算法，其实就是一种编码转换
 */
class Base64Util {

    companion object {
        /**
         * @param encodeStr : 需要编码的string
         */
        fun encode(encodeStr: String): ByteArray {
            return Base64.encode(encodeStr.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        }

        /**
         * @param encodeStr : 需要编码的string
         */
        fun encode(encodeByteArray: ByteArray): ByteArray {
            return Base64.encode(encodeByteArray, Base64.DEFAULT)
        }

        /**
         * @param decodeStr : 需要解码的string
         */
        fun decode(decodeStr: String): ByteArray {
            return Base64.decode(decodeStr, Base64.DEFAULT)
        }

        /**
         * @param decodeStr : 需要解码的string
         */
        fun decode(decodeByteArray: ByteArray): ByteArray {
            return Base64.decode(decodeByteArray, Base64.DEFAULT)
        }
    }
}