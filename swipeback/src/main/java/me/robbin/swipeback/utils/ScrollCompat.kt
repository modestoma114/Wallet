package me.robbin.swipeback.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.view.ScrollingView
import org.jetbrains.annotations.NonNls


/**
 * @PackageName: me.robbin.swipeback.utils
 * @Name:        ScrollCompat
 * @Description: TODO
 * @UpdateDate:  2021/3/2 上午11:08
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/2 上午11:08
 */

object ScrollCompat {

    const val SCROLL_DIRECTION_UP = 1
    const val SCROLL_DIRECTION_DOWN = 2
    const val SCROLL_DIRECTION_LEFT = 3
    const val SCROLL_DIRECTION_RIGHT = 4

    fun hasViewCanScrollUp(@NonNull view: View, x: Float, y: Float): Boolean {
        return hasViewCanScrollDirection(view, x, y, SCROLL_DIRECTION_UP)
    }

    fun hasViewCanScrollDown(@NonNull view: View, x: Float, y: Float): Boolean {
        return hasViewCanScrollDirection(view, x, y, SCROLL_DIRECTION_DOWN)
    }

    fun hasViewCanScrollLeft(@NonNull view: View, x: Float, y: Float): Boolean {
        return hasViewCanScrollDirection(view, x, y, SCROLL_DIRECTION_LEFT)
    }

    fun hasViewCanScrollRight(@NonNls view: View, x: Float, y: Float): Boolean {
        return hasViewCanScrollDirection(view, x, y, SCROLL_DIRECTION_RIGHT)
    }

    fun hasViewCanScrollDirection(@NonNull view: View, x: Float, y: Float, direction: Int): Boolean {
        if (!isPointInView(view, x, y)) return false
        if (canScrollDirection(view, direction)) return true
        if (view is ViewGroup) {
            for (i in (0..view.childCount)) {
                val child = view.getChildAt(i)
                if (hasViewCanScrollDirection(child, x, y, direction)) return true
            }
        }
        return false
    }

    fun canScrollDirection(@NonNull view: View, direction: Int): Boolean {
        return when (direction) {
            SCROLL_DIRECTION_UP -> canScrollUp(view)
            SCROLL_DIRECTION_DOWN -> canScrollDown(view)
            SCROLL_DIRECTION_RIGHT -> canScrollRight(view)
            SCROLL_DIRECTION_LEFT -> canScrollLeft(view)
            else -> false
        }
    }

    fun canScrollUp(@NonNull view: View): Boolean {
        return canScrollVertically(view, -1)
    }

    fun canScrollDown(@NonNull view: View): Boolean {
        return canScrollVertically(view, 1)
    }

    fun canScrollLeft(@NonNull view: View): Boolean {
        return canScrollHorizontally(view, -1)
    }

    fun canScrollRight(@NonNull view: View): Boolean {
        return canScrollHorizontally(view, 1)
    }

    private fun canScrollHorizontally(@NonNull view: View, direction: Int): Boolean {
        return if (view is ScrollingView) canScrollingViewScrollHorizontally(view, direction)
        else view.canScrollHorizontally(direction)
    }

    private fun canScrollVertically(@NonNull view: View, direction: Int): Boolean {
        return if (view is ScrollingView) canScrollingViewScrollVertically(view, direction)
        else view.canScrollVertically(direction)
    }

    private fun canScrollingViewScrollHorizontally(@NonNull view: ScrollingView, direction: Int): Boolean {
        val offset = view.computeHorizontalScrollOffset()
        val range = view.computeHorizontalScrollRange() - view.computeHorizontalScrollExtent()
        if (range == 0) return false
        return if (direction < 0) offset > 0 else offset < range - 1
    }

    private fun canScrollingViewScrollVertically(@NonNull view: ScrollingView, direction: Int): Boolean {
        val offset = view.computeVerticalScrollOffset()
        val range = view.computeVerticalScrollRange() - view.computeVerticalScrollExtent()
        if (range == 0) return false
        return if (direction < 0) offset > 0 else offset < range - 1
    }

    fun isPointInView(view: View, x: Float, y: Float): Boolean {
        val localRect = Rect()
        view.getGlobalVisibleRect(localRect)
        return localRect.contains(x.toInt(), y.toInt())
    }

}