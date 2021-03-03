package me.robbin.swipeback.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.res.TypedArray
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


/**
 * @PackageName: me.robbin.swipeback.utils
 * @Name:        ActivityTranslucentConverter
 * @Description: ActivityTranslucentConverter
 * @UpdateDate:  2021/3/1 下午4:22
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午4:22
 */

class ActivityTranslucentConverter(@NonNull private val mActivity: Activity) {

    private val mToConverter: ToConverter = ToConverter()
    private val mFromConverter: FromConverter = FromConverter()
    private var mIsTranslucent: Boolean = isThemeTranslucent()

    fun isThemeTranslucent(): Boolean {
        return try {
            val typedArray: TypedArray =
                mActivity.theme.obtainStyledAttributes(IntArray(1) { android.R.attr.windowIsTranslucent })
            val windowIsTranslucent = typedArray.getBoolean(0, false)
            typedArray.recycle()
            windowIsTranslucent
        } catch (e: Throwable) {
            false
        }
    }

    fun isTranslucent(): Boolean = mIsTranslucent

    fun toTranslucent() {
        if (mIsTranslucent) return
        mToConverter.convert { translucent ->
            mIsTranslucent = translucent
        }
    }

    fun fromTranslucent() {
        if (!mIsTranslucent) return
        mFromConverter.convert()
        this.mIsTranslucent = false
    }

    /**
     * @Description: 将 Activity 转变为不透明
     * Created by Robbin Ma in 2021/3/3 下午5:16
     */
    private inner class FromConverter {

        private var mInitialedConvertFromTranslucent = false
        private var mMethodConvertFromTranslucent: Method? = null

        fun convert() {
            try {
                if (mMethodConvertFromTranslucent == null) {
                    if (mInitialedConvertFromTranslucent) return
                    mInitialedConvertFromTranslucent = true
                    // 将 Activity 从透明转变为不透明
                    val method = Activity::class.java.getDeclaredMethod("convertFromTranslucent")
                    method.isAccessible = true
                    mMethodConvertFromTranslucent = method
                }
                mMethodConvertFromTranslucent?.invoke(mActivity)
            } catch (e: Throwable) {
            }
        }

    }

    /**
     * @Description: 将 Activity 转变为透明
     * Created by Robbin Ma in 2021/3/3 下午5:16
     */
    private inner class ToConverter {

        private var mInitialedConvertToTranslucent = false
        private var mTranslucentConversionListenerClass: Class<*>? = null
        private var mMethodConvertToTranslucent: Method? = null

        fun convert(block: (translucent: Boolean) -> Unit) {
            if (mInitialedConvertToTranslucent && mMethodConvertToTranslucent == null) {
                block(false)
                return
            }
            try {
                val translucentConversionListener = getTranslucentConversionListener(block)
                convertActivityToTranslucent(translucentConversionListener)
                if (translucentConversionListener == null) {
                    block(false)
                }
            } catch (e: Throwable) {
                block(false)
            }
        }

        /**
         * @Description: 获取 TranslucentConversionListener
         * @Params:      block: (translucent: Boolean) -> Unit
         * @Return:      Any?
         * Created by Robbin Ma in 2021/3/3 下午5:41
         */
        @Throws(Throwable::class)
        private fun getTranslucentConversionListener(block: (translucent: Boolean) -> Unit): Any? {
            if (mTranslucentConversionListenerClass == null) {
                val clazzArray: Array<Class<*>> = Activity::class.java.declaredClasses
                clazzArray.forEach {
                    if (it.simpleName.contains("TranslucentConversionListener"))
                        mTranslucentConversionListenerClass = it
                }
            }
            if (mTranslucentConversionListenerClass != null) {
                val invocationHandler = InvocationHandler { _, _, args ->
                    var translucent = false
                    if (args != null && args.size == 1)
                        translucent = args[0] as Boolean
                    block(translucent)
                    return@InvocationHandler null
                }
                return Proxy.newProxyInstance(
                    mTranslucentConversionListenerClass!!.classLoader,
                    Array(1) { mTranslucentConversionListenerClass },
                    invocationHandler
                )
            }
            return null
        }

        /**
         * @Description: 将 Activity 转变为透明
         * @Params:
         * @Return:
         * Created by Robbin Ma in 2021/3/3 下午5:29
         */
        @SuppressLint("DiscouragedPrivateApi")
        @Throws(Throwable::class)
        private fun convertActivityToTranslucent(@Nullable translucentConversionListener: Any?) {
            var mMethodGetActivityOptions: Method? = null
            if (mMethodConvertToTranslucent == null) {
                mInitialedConvertToTranslucent = true
                val getActivityOptions: Method? = Activity::class.java.getDeclaredMethod("getActivityOptions")
                getActivityOptions?.isAccessible = true
                mMethodGetActivityOptions = getActivityOptions
                val method: Method? = Activity::class.java.getDeclaredMethod(
                    "convertToTranslucent",
                    mTranslucentConversionListenerClass,
                    ActivityOptions::class.java
                )
                method?.isAccessible = true
                mMethodConvertToTranslucent = method
            }
            val options = mMethodGetActivityOptions?.invoke(mActivity)
            mMethodConvertToTranslucent?.invoke(mActivity, translucentConversionListener, options)
        }

    }

}