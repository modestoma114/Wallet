package me.robbin.swipeback

import android.app.Activity
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.FrameLayout
import me.robbin.swipeback.utils.ActivityTranslucentConverter


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackNode
 * @Description: SwipeBackNode
 * @UpdateDate:  2021/3/1 下午4:19
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午4:19
 */

class SwipeBackNode(private val mActivity: Activity) {

    private val mTranslucentConverter: ActivityTranslucentConverter = ActivityTranslucentConverter(mActivity)
    private val mThemeTranslucent: Boolean = mTranslucentConverter.isThemeTranslucent()

    private var mLayout: SwipeBackLayout? = null

    private var mTransformer: SwipeBackTransformer? = null
    private var mPreviousNode: SwipeBackNode? = null

    fun inject() {
        if (mLayout != null) return
        val window = mActivity.window ?: return
        val decorView: FrameLayout = window.decorView as FrameLayout
        if (decorView.childCount == 0) return
        val decorChildView: View = decorView.getChildAt(0) ?: return
        val typedArray: TypedArray =
            mActivity.theme.obtainStyledAttributes(IntArray(1) { android.R.attr.windowBackground })
        val background = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        decorView.background = ColorDrawable(Color.TRANSPARENT)
        decorChildView.setBackgroundResource(background)
        val swipeBackLayout = SwipeBackLayout(mActivity)
        swipeBackLayout.setSwipeBackListener(SwipeBackListener())
        decorView.removeViewInLayout(decorChildView)
        val userContentLayoutParams = decorChildView.layoutParams
        decorChildView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        swipeBackLayout.addView(decorChildView)
        swipeBackLayout.layoutParams = userContentLayoutParams
        decorView.addView(swipeBackLayout)
        mLayout = swipeBackLayout
        configLayout()
        mTransformer = SwipeBackAbility.getSwipeBackTransformerForActivity(mActivity)
    }

    fun getActivity(): Activity = mActivity

    private fun configLayout() {
        if (mLayout != null) {
            if (SwipeBackManager.isRootNode(this) && !SwipeBack.isRootSwipeBackEnable())
                mLayout?.setSwipeBackDirection(SwipeBackDirection.NONE)
            else
                mLayout?.setSwipeBackDirection(SwipeBackAbility.getSwipeBackDirectionForActivity(mActivity))
            mLayout?.setSwipeBackForceEdge(SwipeBackAbility.isSwipeBackForceEdgeForActivity(mActivity))
            mLayout?.setSwipeBackOnlyEdge(SwipeBackAbility.isSwipeBackOnlyEdgeForActivity(mActivity))
            mLayout?.setMaskAlpha(SwipeBackAbility.getSwipeBackMaskAlphaForActivity(mActivity))
            mLayout?.setShadowColor(SwipeBackAbility.getSwipeBackShadowColorForActivity(mActivity))
            mLayout?.setShadowSize(SwipeBackAbility.getSwipeBackShadowSizeForActivity(mActivity))
        }
    }

    fun findPreviousNode(): SwipeBackNode? {
        return SwipeBackManager.findPreviousNode(this)
    }

    fun getPreviousView(): View? {
        if (mPreviousNode == null) return null
        val window = mPreviousNode?.mActivity?.window ?: return null
        val decorView: FrameLayout = window.decorView as FrameLayout
        if (decorView.childCount == 0) return null
        return decorView.getChildAt(0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val node = other as SwipeBackNode
        return mActivity == node.mActivity
    }

    override fun hashCode(): Int {
        return mActivity.hashCode()
    }

    private inner class SwipeBackListener: SwipeBackLayout.SwipeBackListener {
        override fun onBeforeSwipe(swipeFraction: Float, swipeDirection: SwipeBackDirection) {
            configLayout()
            mTransformer = SwipeBackAbility.getSwipeBackTransformerForActivity(mActivity)
        }

        override fun onStartSwipe(swipeFraction: Float, swipeDirection: SwipeBackDirection) {
            mPreviousNode = findPreviousNode()
            if (mLayout != null && mTransformer != null && swipeFraction == 0F) {
                mTransformer?.initialize(mLayout!!, getPreviousView())
            }
            if (!mThemeTranslucent) {
                mTranslucentConverter.toTranslucent()
            }
        }

        override fun onSwiping(swipeFraction: Float, swipeDirection: SwipeBackDirection) {
            if (mLayout != null && mTransformer != null && mTranslucentConverter.isTranslucent())
                mTransformer?.transform(mLayout!!, getPreviousView(), swipeFraction, swipeDirection)
        }

        override fun onEndSwipe(swipeFraction: Float, swipeDirection: SwipeBackDirection) {
            if (swipeFraction != 1F) {
                if (!mThemeTranslucent)
                    mTranslucentConverter.fromTranslucent()
                if (mLayout != null && mTransformer != null)
                    mTransformer?.restore(mLayout!!, getPreviousView())
            } else {
                if (mLayout != null && mTransformer != null)
                    mTransformer?.restore(mLayout!!, getPreviousView())
                mActivity.finish()
                mActivity.overridePendingTransition(0, 0)
            }
            mPreviousNode = null
        }

    }

}