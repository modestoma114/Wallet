package me.robbin.swipeback

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.customview.widget.ViewDragHelper


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackLayout
 * @Description: TODO
 * @UpdateDate:  2021/3/1 下午5:25
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午5:25
 */

class SwipeBackLayout(private val mContext: Context): FrameLayout(mContext) {

    private val mDragHelper: ViewDragHelper? = ViewDragHelper.create(this, 1F, Draghel)
    private val mShadowRect: Rect = Rect()

    private var mSwing: Boolean = false
    private var mFraction: Float = 0F
    private var mDownX: Float = 0F
    private var mDownY: Float = 0F
    private var mShadowDrawable: GradientDrawable? = null

    @ColorInt
    private var mShadowColor: Int = Color.TRANSPARENT
    @Px
    private var mShadowSize = 0
    @IntRange(from = 0, to = 255)
    private var mMaskAlpha: Int = 150
    @NonNull
    private var mSwipeBackDirection: SwipeBackDirection = SwipeBackDirection.NONE
    private var mSwipeBackForceEdge: Boolean = true
    private var mSwipeBackOnlyEdge: Boolean = false
    private var mSwipeBackFactor: Float = 0.5F
    private var mSwipeBackVelocity: Float = 2000F

    public fun isSwipeBackEnable(): Boolean = mSwipeBackDirection != SwipeBackDirection.NONE

    public fun setSwipeBackForceEdge(enable: Boolean) { mSwipeBackForceEdge = enable }

    public fun isSwipeBackForceEdge(): Boolean = mSwipeBackForceEdge

    public fun setSwipeBackFactor(@FloatRange(from = 0.0, to = 1.0) swipeBackFactor: Float) {
        mSwipeBackFactor = when {
            swipeBackFactor > 1 -> 1F
            swipeBackFactor < 0 -> 0F
            else -> swipeBackFactor
        }
    }

    public fun getSwipeBackFactor(): Float = mSwipeBackFactor

    public fun isShadowEnable(): Boolean = mShadowSize > 0 && mShadowColor != Color.TRANSPARENT

    public fun setShadowColor(@ColorInt colorInt: Int) { mShadowColor = colorInt }

    public fun setShadowSize(@Px px: Int) { mShadowSize = px }

    public fun setMaskAlpha(@IntRange(from = 0, to = 255) maskAlpha: Int) {
        mMaskAlpha = when {
            maskAlpha > 255 -> 255
            maskAlpha < 0 -> 0
            else -> maskAlpha
        }
    }

    public fun setMaskAlpha(@FloatRange(from = 0.0, to = 1.0) maskFloat: Float) {
        setMaskAlpha((255 * maskFloat).toInt())
    }

    public fun getMaskAlpha() = mMaskAlpha

    public fun setSwipeBackDirection(@NonNull direction: SwipeBackDirection) {
        if (direction == mSwipeBackDirection) return
        mSwipeBackDirection = direction
    }

    private fun setEdgeTrackingEnabledByDirection() {
        when (mSwipeBackDirection) {
            SwipeBackDirection.BOTTOM -> mDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
            SwipeBackDirection.LEFT -> mDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT)
            SwipeBackDirection.RIGHT -> mDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
            SwipeBackDirection.TOP -> mDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM)
            SwipeBackDirection.NONE -> mDragHelper?.setEdgeTrackingEnabled(0)
        }
    }

    private inner class DragHelperCallback: ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return canSwipeBack()
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
            refreshFraction(capturedChild)
            onSwipeStart()
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return isSwipeBackEnable()
        }

    }

}