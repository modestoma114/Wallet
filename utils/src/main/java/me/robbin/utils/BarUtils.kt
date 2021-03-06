package me.robbin.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.setMargins


/**
 * @PackageName: me.robbin.utils
 * @Name:        BarUtils
 * @Description: Status bar tools
 * @UpdateDate:  2021/3/4 13:16
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/4 13:16
 */

object BarUtils {

    private const val TAG = "BarUtils"

    private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
    private const val TAG_OFFSET = "TAG_OFFSET"
    private const val KEY_OFFSET = -123

    /**
     * @Description: get the height of status bar
     * @Return:      status bar height
     * Created by Robbin Ma in 2021/3/4 13:21
     */
    fun getStatusBarHeight(): Int {
        val resources = Utils.getApp().resources
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }

    /**
     * @Description: Setting status bar visibility
     * @Params:      activity: the activity what you want set it visibility
     * @Return:
     * Created by Robbin Ma in 2021/3/4 13:41
     */
    fun setStatusBarVisibility(activity: Activity, isVisible: Boolean) {
        setStatusBarVisibility(activity.window, isVisible)
    }

    fun setStatusBarVisibility(window: Window, isVisible: Boolean) {
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            showStatusBarView(window)
            addMarginTopEqualStatusBarHeight(window)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            hideStatusBarView(window)
            subtractMarginTopEqualStatusBarHeight(window)
        }
    }

    fun isStatusBarVisibility(activity: Activity): Boolean {
        val flags = activity.window.attributes.flags
        return (flags and WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0
    }

    fun setStatusBarLightMode(activity: Activity, isLightMode: Boolean) {
        setStatusBarLightMode(activity.window, isLightMode)
    }

    fun setStatusBarLightMode(window: Window, isLightMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var vis = decorView.systemUiVisibility
            vis = if (isLightMode) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = vis
        }
    }

    fun transparentStatusBar(activity: Activity) {
        transparentStatusBar(activity.window)
    }

    fun transparentStatusBar(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = option or vis
        window.statusBarColor = Color.TRANSPARENT
    }

    fun addMarginTopEqualStatusBarHeight(view: View) {
        view.tag = TAG_OFFSET
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) return
        val layoutParam = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParam.setMargins(
            layoutParam.leftMargin,
            layoutParam.topMargin + getStatusBarHeight(),
            layoutParam.rightMargin,
            layoutParam.bottomMargin
        )
        view.setTag(KEY_OFFSET, true)
    }

    private fun addMarginTopEqualStatusBarHeight(window: Window) {
        val view = window.decorView.findViewWithTag<View>(TAG_OFFSET) ?: return
        addMarginTopEqualStatusBarHeight(view)
    }

    fun subtractMarginTopEqualStatusBarHeight(view: View) {
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) return
        val layoutParam = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParam.setMargins(
            layoutParam.leftMargin,
            layoutParam.topMargin - getStatusBarHeight(),
            layoutParam.rightMargin,
            layoutParam.bottomMargin
        )
        view.setTag(KEY_OFFSET, false)
    }

    private fun subtractMarginTopEqualStatusBarHeight(window: Window) {
        val view = window.decorView.findViewWithTag<View>(TAG_OFFSET) ?: return
        subtractMarginTopEqualStatusBarHeight(view)
    }

    private fun showStatusBarView(window: Window) {
        val decorView: ViewGroup = window.decorView as ViewGroup
        val fakeStatusBarView: View = decorView.findViewWithTag(TAG_STATUS_BAR) ?: return
        fakeStatusBarView.visibility = View.VISIBLE
    }

    private fun hideStatusBarView(window: Window) {
        val decorView: ViewGroup = window.decorView as ViewGroup
        val fakeStatusVarView: View = decorView.findViewWithTag(TAG_STATUS_BAR) ?: return
        fakeStatusVarView.visibility = View.GONE
    }

    private fun createStatusBarView(context: Context, color: Int): View {
        val statusBarView = View(context)
        statusBarView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight())
        statusBarView.setBackgroundColor(color)
        statusBarView.tag = TAG_STATUS_BAR
        return statusBarView
    }

}