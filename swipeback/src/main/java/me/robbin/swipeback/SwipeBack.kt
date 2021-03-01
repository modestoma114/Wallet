package me.robbin.swipeback

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.Px
import androidx.core.graphics.ColorUtils


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBack
 * @Description:
 * @UpdateDate:  2021/3/1 下午12:37
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午12:37
 */

object SwipeBack {

    @NonNull
    private var mSwipeBackDirection: SwipeBackDirection = SwipeBackDirection.NONE
    @Nullable
    private var mSwipeBackTransformer: SwipeBackTransformer? = null

    private var mSwipeBackOnlyEdge: Boolean = false
    private var mSwipeBackForceEdge: Boolean = false
    @ColorInt
    private var mShadowColor: Int = ColorUtils.setAlphaComponent(Color.BLACK, 50)
    @Px
    private var mShadowSize = 32
    @IntRange(from = 0, to = 255)
    private var mMaskAlpha = 150

    private var mRootSwipeBackEnable = true



}