package com.example.core.utils.kotlin

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class SPUtil {

    companion object {

        /**
         * 保存在手机里面的文件名
         */
        const val FILE_NAME = "share_data"

        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         */
        inline fun <reified V> put(context: Context, key: String, value: V) {

            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()

            when (value) {
                is String -> {
                    editor.putString(key, value)
                }
                is Int -> {
                    editor.putInt(key, value)
                }
                is Boolean -> {
                    editor.putBoolean(key, value)
                }
                is Float -> {
                    editor.putFloat(key, value)
                }
                is Long -> {
                    editor.putLong(key, value)
                }
            }
            SPUtil.SharedPreferencesCompat.apply(editor)
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         */
        inline fun <reified V> get(context: Context, key: String, defValue: V): V {
            val sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE)

            return when (defValue) {
                is String -> {
                    sp.getString(key, defValue) as V
                }
                is Int -> {
                    sp.getInt(key, defValue) as V
                }
                is Boolean -> {
                    sp.getBoolean(key, defValue) as V
                }
                is Float -> {
                    sp.getFloat(key, defValue) as V
                }
                is Long -> {
                    sp.getLong(key, defValue) as V
                }
                else -> defValue
            }
        }

        /**
         * 移除某个key值已经对应的值
         *
         * @param context
         * @param key
         */
        fun remove(context: Context, key: String) {
            val sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.remove(key)
            SPUtil.SharedPreferencesCompat.apply(editor)
        }

        /**
         * 清除所有数据
         *
         * @param context
         */
        fun clear(context: Context) {
            val sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.clear()
            SPUtil.SharedPreferencesCompat.apply(editor)
        }

        /**
         * 查询某个key是否已经存在
         *
         * @param context
         * @param key
         * @return
         */
        fun contains(context: Context, key: String): Boolean {
            val sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE)
            return sp.contains(key)
        }


        /**
         * 返回所有的键值对
         *
         * @param context
         * @return
         */
        fun getAll(context: Context): Map<String, *> {
            val sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE)
            return sp.all
        }

    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                sApplyMethod?.invoke(editor) ?: return
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }

}