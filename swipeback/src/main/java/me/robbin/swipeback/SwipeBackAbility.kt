package me.robbin.swipeback

import android.app.Activity
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.Px


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackAbility
 * @Description: TODO
 * @UpdateDate:  2021/3/2 下午3:14
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/2 下午3:14
 */

object SwipeBackAbility {

    fun getSwipeBackDirectionForActivity(activity: Activity): SwipeBackDirection {
        return if (activity is Direction) {
            activity.swipeBackDirection()
        } else {
            SwipeBack.getSwipeBackDirection()
        }
    }

    interface Direction {
        fun swipeBackDirection(): SwipeBackDirection
    }

    fun getSwipeBackTransformerForActivity(activity: Activity): SwipeBackTransformer? {
        return if (activity is Transformer) {
            activity.swipeBackTransformer()
        } else {
            SwipeBack.getSwipeBackTransformer()
        }
    }

    interface Transformer {
        fun swipeBackTransformer(): SwipeBackTransformer?
    }

    fun isSwipeBackOnlyEdgeForActivity(activity: Activity): Boolean {
        return if (activity is OnlyEdge) {
            activity.swipeBackOnlyEdge()
        } else {
            SwipeBack.isSwipeBackOnlyEdge()
        }
    }

    interface OnlyEdge {
        fun swipeBackOnlyEdge(): Boolean
    }

    fun isSwipeBackForceEdgeForActivity(activity: Activity): Boolean {
        return if (activity is ForceEdge) {
            activity.swipeBackForceEdge()
        } else {
            SwipeBack.isSwipeBackForceEdge()
        }
    }

    interface ForceEdge {
        fun swipeBackForceEdge(): Boolean
    }

    @ColorInt
    fun getSwipeBackShadowColorForActivity(activity: Activity): Int {
        return if (activity is ShadowColor) {
            activity.swipeBackShadowColor()
        } else {
            SwipeBack.getSwipeBackShadowColor()
        }
    }

    interface ShadowColor {
        @ColorInt
        fun swipeBackShadowColor(): Int
    }

    @Px
    fun getSwipeBackShadowSizeForActivity(activity: Activity): Int {
        return if (activity is ShadowMask) {
            activity.swipeBackShadowSize()
        } else {
            SwipeBack.getSwipeBackShadowSize()
        }
    }

    interface ShadowMask {
        @Px
        fun swipeBackShadowSize(): Int
    }

    @IntRange(from = 0, to = 255)
    fun getSwipeBackMaskAlphaForActivity(activity: Activity): Int {
        return if (activity is MaskAlpha) {
            activity.swipeBackMaskAlpha()
        } else {
            SwipeBack.getSwipeBackMaskAlpha()
        }
    }

    interface MaskAlpha {
        @IntRange(from = 0, to = 255)
        fun swipeBackMaskAlpha(): Int
    }

}