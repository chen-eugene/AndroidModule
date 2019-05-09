package com.eugene.core.utils.encryption

import java.io.*
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.FileChannel
import java.security.MessageDigest


/**
 * 非对称加密
 * MD5的作用是让大容量信息在用数字签名软件签署私人密钥前被”压缩”成一种保密的格式
 * （就是把一个任意长度的字节串变换成一定长的十六进制数字串）
 * 除了MD5以外，其中比较有名的还有sha-1、RIPEMD以及Haval等。
 */
object MD5Util {

    /**
     * 用来将字节转换成 16 进制表示的字符
     */
    private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f')

    private val messageDigest by lazy {
        MessageDigest.getInstance("MD5")
    }

    fun getMD5FromByte(source: ByteArray): String? {
        var s: String? = null
        val md = java.security.MessageDigest.getInstance("MD5")
        md.update(source)
        val tmp = messageDigest.digest()
        // MD5 的计算结果是一个 128 位的长整数(16个字节)，转为16进制则为32个字符
        val str = CharArray(16 * 2)
        // 每个字节用 16 进制表示的话，使用两个字符(一个8位->2个4位->2个16进制字符)，所以表示成 16 进制需要 32 个字符
        var k = 0 // 表示转换结果中对应的字符位置
        for (i in 0..15) {
            // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            val byte0 = tmp[i] // 取第 i 个字节
            str[k++] = hexDigits[byte0.toInt().ushr(4) and 0xf]
            // 取字节中高 4 位的数字转换,>>>为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0.toInt() and 0xf]
            // 取字节中低 4 位的数字转换
        }
        s = String(str)
        // 换后的结果转换为字符串
        return s
    }

    // 加密文件对象
    fun getFileMD5String(file: File): String? {
        var value: String? = null
        val input = FileInputStream(file)
        try {
            val byteBuffer = input.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(byteBuffer)
            val bi = BigInteger(1, md5.digest())
            value = bi.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return value
    }

    //参数为文件路径
    @Throws(FileNotFoundException::class)
    fun getFileMD5String(filePath: String): String? {
        var value: String? = null
        val file = File(filePath)
        val `in` = FileInputStream(file)
        try {
            val byteBuffer = `in`.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(byteBuffer)
            val bi = BigInteger(1, md5.digest())
            value = bi.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return value
    }

    //参数为url
    fun getFileMD5StringByURL(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        var fis: BufferedInputStream? = null
        fis = BufferedInputStream(connection.inputStream)
        val buffer = ByteArray(1024)
        var numRead = fis.read(buffer)
        while (numRead > 0) {
            messageDigest.update(buffer, 0, numRead)
            numRead = fis.read(buffer)
        }
        fis.close()
        return bufferToHex(messageDigest.digest())
    }

    //参数为字节数组
    fun getMD5String(bytes: ByteArray): String {
        messageDigest.update(bytes)
        return bufferToHex(messageDigest.digest())
    }

    // 将字节数组转换为16进制的字符串
    private fun bufferToHex(bytes: ByteArray): String {
        val m = 0
        val n = bytes.size
        val sb = StringBuffer(2 * n)
        val k = m + n
        for (l in m until k) {
            appendHexPair(bytes[l], sb)
        }
        return sb.toString()
    }

    private fun appendHexPair(bt: Byte, sb: StringBuffer) {
        val c0 = hexDigits[bt.toInt() and 0xf0 shr 4]
        // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
        val c1 = hexDigits[bt.toInt() and 0xf]
        // 取字节中低 4 位的数字转换
        sb.append(c0)
        sb.append(c1)
    }

}