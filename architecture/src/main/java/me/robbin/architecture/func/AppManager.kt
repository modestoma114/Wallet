package me.robbin.architecture.func

import android.app.Activity
import java.util.*


/**
 * @PackageName: me.robbin.architecture.func
 * @Name:        AppManager
 * @Description: Manage all Activities of the application
 * @UpdateDate:  2021/2/28 23:04
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 23:04
 */

object AppManager {

    var activityStack: Stack<Activity> = Stack()
        private set

    fun addActivity(activity: Activity?) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity?) {
        activityStack.remove(activity)
    }

    fun currentActivity(): Activity? {
        return activityStack.lastElement()
    }

    fun finishActivity() {
        val activity = activityStack.lastElement()
        activity.finish()
    }

    fun finishActivity(activity: Activity?) {
        activityStack.remove(activity)
        activity?.finish()
    }

    fun finishActivity(cls: Class<*>) {
        run breaking@ {
            activityStack.forEach {
                if (it.javaClass == cls) {
                    finishActivity(it)
                    return@breaking
                }
            }
        }
    }

    fun finishAllActivity() {
        activityStack.forEach {
            if (it != null) {
                finishActivity(it)
            }
        }
        activityStack.clear()
    }

    fun killApp() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}