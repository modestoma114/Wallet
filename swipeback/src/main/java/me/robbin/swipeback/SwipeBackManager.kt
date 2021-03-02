package me.robbin.swipeback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import me.robbin.utils.Utils


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackManager
 * @Description: Swipe Back Manager
 * @UpdateDate:  2021/3/1 下午4:18
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午4:18
 */

object SwipeBackManager {

    const val TAG = "SwipeBackManager"

    private var mNodes: MutableList<SwipeBackNode> = mutableListOf()

    fun init() {
        Utils.getApp().registerActivityLifecycleCallbacks(SwipeBackManagerActivityLifecycle())
    }

    fun isRootNode(node: SwipeBackNode): Boolean {
        if (mNodes.isEmpty()) return false
        return mNodes[0] === node
    }

    fun isRootNode(activity: Activity): Boolean {
        if (mNodes.isEmpty()) return false
        val node = mNodes[0]
        return node.getActivity() === activity
    }

    fun findPreviousNode(node: SwipeBackNode): SwipeBackNode? {
        if (mNodes.isEmpty()) return null
        val index = mNodes.indexOf(node)
        if (index < 1) return null
        return mNodes[index - 1]
    }

    fun findFirstNode(): SwipeBackNode? {
        if (mNodes.isEmpty()) return null
        return mNodes[mNodes.size - 1]
    }

    fun findNode(activity: Activity): SwipeBackNode? {
        mNodes.forEach {
            if (it.getActivity() === activity)
                return it
        }
        return null
    }

    private fun addNode(activity: Activity) {
        val node = SwipeBackNode(activity)
        mNodes.add(node)
    }

    private fun removeNode(activity: Activity) {
        for (i in mNodes.size downTo 0) {
            val node = mNodes[i]
            if (node.getActivity() === activity) {
                mNodes.removeAt(i)
                break
            }
        }
    }

    private class SwipeBackManagerActivityLifecycle : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.i(TAG, "mNodes insert activity: $activity")
            addNode(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            findNode(activity)?.inject()
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
            removeNode(activity)
        }
    }

}