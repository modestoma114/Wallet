package me.robbin.swipeback.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.res.TypedArray
import android.os.Build
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

    public fun isThemeTranslucent(): Boolean {
        return try {
            val typedArray: TypedArray = mActivity.theme.obtainStyledAttributes(IntArray(1) {android.R.attr.windowIsTranslucent})
            val windowIsTranslucent = typedArray.getBoolean(0, false)
            typedArray.recycle()
            windowIsTranslucent
        } catch (e: Throwable) {
            false
        }
    }

    public fun isTranslucent(): Boolean = mIsTranslucent

    public fun toTranslucent() {
        if (mIsTranslucent) return
        mToConverter.convert(object : TranslucentCallback {
            override fun onTranslucentCallback(translucent: Boolean) {
                mIsTranslucent = translucent
            }
        })
    }

    public fun fromTranslucent() {
        if (!mIsTranslucent) return
        mFromConverter.convert()
        this.mIsTranslucent = false
    }

    private inner class FromConverter {

        private var mInitialedConvertFromTranslucent = false
        private var mMethodConvertFromTranslucent: Method? = null

        fun convert() {
            try {
                if (mMethodConvertFromTranslucent == null) {
                    if (mInitialedConvertFromTranslucent)
                        return
                    mInitialedConvertFromTranslucent = true
                    val method = Activity::class.java.getDeclaredMethod("convertFromTranslucent")
                    method.isAccessible = true
                    mMethodConvertFromTranslucent = method
                }
                mMethodConvertFromTranslucent?.invoke(mActivity)
            } catch (e: Throwable) {
            }
        }

    }

    private inner class ToConverter {

        private var mInitialedConvertToTranslucent = false
        private var mTranslucentConversionListenerClass: Class<*>? = null
        private var mMethodConvertToTranslucent: Method? = null
        private var mMethodGetActivityOptions: Method? = null

        fun convert(callback: TranslucentCallback?) {
            if (mInitialedConvertToTranslucent && mMethodConvertToTranslucent == null) {
                callback?.onTranslucentCallback(false)
                return
            }
            try {
                val translucentConversionListener = getTranslucentConversionListener(callback)
                convertActivityToTranslucent(translucentConversionListener)
                if (translucentConversionListener == null) {
                    callback?.onTranslucentCallback(false)
                }
            } catch (e: Throwable) {
                callback?.onTranslucentCallback(false)
            }
        }

        @Throws(Throwable::class)
        private fun getTranslucentConversionListener(@Nullable callback: TranslucentCallback?): Any? {
            if (mTranslucentConversionListenerClass == null) {
                val clazzArray: Array<Class<*>> = Activity::class.java.declaredClasses
                clazzArray.forEach {
                    if (it.simpleName.contains("TranslucentConversionListener"))
                        mTranslucentConversionListenerClass = it
                }
            }
            if (mTranslucentConversionListenerClass != null) {
                val invocationHandler = InvocationHandler { proxy, method, args ->
                    var translucent = false
                    if (args != null && args.size == 1)
                        translucent = args[0] as Boolean
                    callback?.onTranslucentCallback(translucent)
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

        @Throws(Throwable::class)
        private fun convertActivityToTranslucent(@Nullable translucentConversionListener: Any?) {
            if (mMethodConvertToTranslucent == null) {
                mInitialedConvertToTranslucent = true
                val getActivityOptions: Method? = Activity::class.java.getDeclaredMethod("getActivityOptions")
                getActivityOptions?.isAccessible = true
                mMethodGetActivityOptions = getActivityOptions
                val method: Method? = Activity::class.java.getDeclaredMethod("convertToTranslucent", mTranslucentConversionListenerClass, ActivityOptions::class.java)
                method?.isAccessible = true
                mMethodConvertToTranslucent = method
            }
        }

    }

    public interface TranslucentCallback {
        fun onTranslucentCallback(translucent: Boolean)
    }

}