package me.robbin.architecture.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.reflect.InvocationTargetException


/**
 * @PackageName: me.robbin.architecture.utils
 * @Name:        Utils
 * @Description: Utils
 * @UpdateDate:  2021/2/28 22:50
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 22:50
 */

object Utils {

    private lateinit var sApplication: Application

    fun init(context: Context) {
        init((context as Application).applicationContext)
    }

    fun init(app: Application) {
        if (!::sApplication.isInitialized) {
            sApplication = app
        }
    }

    fun getApp(): Application {
        if (::sApplication.isInitialized)
            return sApplication
        val app = getApplicationByReflect()
        init(app)
        return app
    }

    @SuppressLint("PrivateApi")
    private fun getApplicationByReflect(): Application {
        try {
            val activityThread = Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw NullPointerException("u should init first")
    }

}