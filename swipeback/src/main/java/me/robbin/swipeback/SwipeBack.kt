package me.robbin.swipeback

import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils
import me.robbin.utils.Utils


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBack
 * @Description:
 * @UpdateDate:  2021/3/1 下午12:37
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午12:37
 */

object SwipeBack {

    private var mSwipeBackDirection: SwipeBackDirection = SwipeBackDirection.NONE
    private var mSwipeBackTransformer: SwipeBackTransformer? = null

    private var mSwipeBackOnlyEdge: Boolean = false
    private var mSwipeBackForceEdge: Boolean = true
    @ColorInt
    private var mShadowColor: Int = ColorUtils.setAlphaComponent(Color.BLACK, 50)
    @Px
    private var mShadowSize = 32
    @IntRange(from = 0, to = 255)
    private var mMaskAlpha = 150

    private var mRootSwipeBackEnable = false

    fun init() {
        SwipeBackManager.init()
        mShadowSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12F, Utils.getApp().resources.displayMetrics).toInt()
    }

    fun setRootSwipeBackEnable(enable: Boolean) {
        this.mRootSwipeBackEnable = enable
    }

    fun isRootSwipeBackEnable(): Boolean = mRootSwipeBackEnable

    fun setSwipeBackDirection(direction: SwipeBackDirection) {
        this.mSwipeBackDirection = direction
    }

    fun getSwipeBackDirection(): SwipeBackDirection = mSwipeBackDirection

    fun setSwipeBackTransformer(transformer: SwipeBackTransformer) {
        this.mSwipeBackTransformer = transformer
    }

    fun getSwipeBackTransformer(): SwipeBackTransformer? = mSwipeBackTransformer

    fun setSwipeBackForceEdge(enable: Boolean) {
        this.mSwipeBackForceEdge = enable
    }

    fun isSwipeBackForceEdge(): Boolean = mSwipeBackForceEdge

    fun setSwipeBackOnlyEdge(enable: Boolean) {
        this.mSwipeBackOnlyEdge = enable
    }

    fun isSwipeBackOnlyEdge(): Boolean = mSwipeBackOnlyEdge

    fun setSwipeBackShadowColor(@ColorInt shadowColor: Int) {
        this.mShadowColor = shadowColor
    }

    @ColorInt
    fun getSwipeBackShadowColor(): Int = mShadowColor

    fun setSwipeBackShadowSize(@Px shadowSize: Int) {
        this.mShadowSize = shadowSize
    }

    @Px
    fun getSwipeBackShadowSize(): Int = mShadowSize

    fun setSwipeBackMaskAlpha(@IntRange(from = 0, to = 255) maskAlpha: Int) {
        this.mMaskAlpha = maskAlpha
    }

    @IntRange(from = 0, to = 255)
    fun getSwipeBackMaskAlpha(): Int = mMaskAlpha

}