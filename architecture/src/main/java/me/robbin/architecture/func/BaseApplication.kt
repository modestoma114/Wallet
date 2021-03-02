package me.robbin.architecture.func

import android.app.Activity
import android.app.Application
import android.os.Bundle
import me.robbin.utils.Utils


/**
 * @PackageName: me.robbin.architecture.func
 * @Name:        BaseApplication
 * @Description: Base Application
 * @UpdateDate:  2021/2/28 23:02
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 23:02
 */

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                AppManager.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.removeActivity(activity)
            }

        })
    }

}