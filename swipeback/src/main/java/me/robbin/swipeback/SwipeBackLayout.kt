package me.robbin.swipeback

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import me.robbin.swipeback.utils.ScrollCompat
import kotlin.math.abs


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackLayout
 * @Description: TODO
 * @UpdateDate:  2021/3/1 下午5:25
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午5:25
 */

class SwipeBackLayout(mContext: Context) : FrameLayout(mContext) {

    private val mDragHelper: ViewDragHelper
    private val mShadowRect: Rect = Rect()

    private var mSwiping: Boolean = false
    private var mFraction: Float = 0F
    private var mDownX: Float? = 0F
    private var mDownY: Float? = 0F
    private var mShadowDrawable: GradientDrawable? = null

    @ColorInt
    private var mShadowColor: Int = Color.TRANSPARENT

    @Px
    private var mShadowSize = 0

    @IntRange(from = 0, to = 255)
    private var mMaskAlpha: Int = 150
    private var mSwipeBackDirection: SwipeBackDirection = SwipeBackDirection.NONE
    private var mSwipeBackForceEdge: Boolean = true
    private var mSwipeBackOnlyEdge: Boolean = false
    private var mSwipeBackFactor: Float = 0.5F
    private var mSwipeBackVelocity: Float = 2000F

    private lateinit var mSwipeBackListener: SwipeBackListener

    init {
        mDragHelper = ViewDragHelper.create(this, 1F, DragHelperCallback())
        mDragHelper.minVelocity = mSwipeBackVelocity
        setEdgeTrackingEnabledByDirection()
    }

    fun isSwipeBackEnable(): Boolean = mSwipeBackDirection != SwipeBackDirection.NONE

    fun setSwipeBackForceEdge(enable: Boolean) {
        mSwipeBackForceEdge = enable
    }

    fun isSwipeBackForceEdge(): Boolean = mSwipeBackForceEdge

    fun setSwipeBackFactor(@FloatRange(from = 0.0, to = 1.0) swipeBackFactor: Float) {
        mSwipeBackFactor = when {
            swipeBackFactor > 1 -> 1F
            swipeBackFactor < 0 -> 0F
            else -> swipeBackFactor
        }
    }

    fun getSwipeBackFactor(): Float = mSwipeBackFactor

    fun isShadowEnable(): Boolean = mShadowSize > 0 && mShadowColor != Color.TRANSPARENT

    fun setShadowColor(@ColorInt colorInt: Int) {
        mShadowColor = colorInt
    }

    fun setShadowSize(@Px px: Int) {
        mShadowSize = px
    }

    fun setMaskAlpha(@IntRange(from = 0, to = 255) maskAlpha: Int) {
        mMaskAlpha = when {
            maskAlpha > 255 -> 255
            maskAlpha < 0 -> 0
            else -> maskAlpha
        }
    }

    fun setMaskAlpha(@FloatRange(from = 0.0, to = 1.0) maskFloat: Float) {
        setMaskAlpha((255 * maskFloat).toInt())
    }

    fun getMaskAlpha() = mMaskAlpha

    fun setSwipeBackDirection(direction: SwipeBackDirection) {
        if (direction == mSwipeBackDirection) return
        mSwipeBackDirection = direction
        setEdgeTrackingEnabledByDirection()
        mShadowDrawable = null
    }

    private fun setEdgeTrackingEnabledByDirection() {
        when (mSwipeBackDirection) {
            SwipeBackDirection.BOTTOM -> mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
            SwipeBackDirection.LEFT -> mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT)
            SwipeBackDirection.RIGHT -> mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
            SwipeBackDirection.TOP -> mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM)
            SwipeBackDirection.NONE -> mDragHelper.setEdgeTrackingEnabled(0)
        }
    }

    fun getSwipeBackDirection(): SwipeBackDirection = mSwipeBackDirection

    fun getSwipeBackVelocity(): Float = mSwipeBackVelocity

    fun setSwipeBackVelocity(@FloatRange(from = 0.0) swipeBackVelocity: Float) {
        this.mSwipeBackVelocity = swipeBackVelocity
        mDragHelper.minVelocity = mSwipeBackVelocity
    }

    fun isSwipeBackOnlyEdge(): Boolean = mSwipeBackOnlyEdge

    fun setSwipeBackOnlyEdge(enable: Boolean) {
        this.mSwipeBackOnlyEdge = enable
    }

    fun isSwiping(): Boolean = mSwiping

    fun setSwipeBackListener(swipeBackListener: SwipeBackListener) {
        this.mSwipeBackListener = swipeBackListener
    }

    fun canSwipeBack(): Boolean = if (mSwipeBackOnlyEdge) isDirectionEdgeTouched() else isSwipeBackEnable()

    fun isDirectionEdgeTouched(): Boolean {
        return when (mSwipeBackDirection) {
            SwipeBackDirection.BOTTOM -> mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_TOP)
            SwipeBackDirection.TOP -> mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_BOTTOM)
            SwipeBackDirection.RIGHT -> mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT)
            SwipeBackDirection.LEFT -> mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_RIGHT)
            SwipeBackDirection.NONE -> false
        }
    }

    private var mShouldIntercept: Boolean = false
    private var mCheckedIntercept: Boolean = false

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 遇见点击事件不进行拦截
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mCheckedIntercept = false
                mShouldIntercept = false
                beforeSwipe()
            }
        }
        // 如果没有启用滑动返回，则不拦截点击事件
        if (!isSwipeBackEnable()) super.dispatchTouchEvent(ev)
        val x = ev?.rawX
        val y = ev?.rawY
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = x
                mDownY = y
            }
            MotionEvent.ACTION_MOVE -> {
                if (!mCheckedIntercept) {
                    if (mSwipeBackForceEdge && isDirectionEdgeTouched()) {
                        mCheckedIntercept = true
                        mShouldIntercept = true
                    } else {
                        val dx = x!!.minus(mDownX!!)
                        val dy = y!!.minus(mDownY!!)
                        // 如果滑动距离大于最小距离
                        if (abs(dx) > mDragHelper.touchSlop || abs(dy) > mDragHelper.touchSlop) {
                            mCheckedIntercept = true
                            // 横向滑动
                            if (abs(dx) > abs(dy)) {
                                // 向右滑动
                                if (dx > 0) {
                                    if (mSwipeBackDirection == SwipeBackDirection.RIGHT)
                                        mShouldIntercept = !ScrollCompat.hasViewCanScrollLeft(this, mDownX!!, mDownY!!)
                                } else
                                    if (mSwipeBackDirection == SwipeBackDirection.LEFT)
                                        mShouldIntercept =
                                            !ScrollCompat.hasViewCanScrollRight(this, mDownX!!, mDownY!!)
                            } else {
                                if (dy > 0) {
                                    if (mSwipeBackDirection == SwipeBackDirection.BOTTOM)
                                        mShouldIntercept =
                                            !ScrollCompat.hasViewCanScrollUp(this, mDownX!!, mDownY!!)
                                } else
                                    if (mSwipeBackDirection == SwipeBackDirection.TOP)
                                        mShouldIntercept =
                                            !ScrollCompat.hasViewCanScrollDown(this, mDownX!!, mDownY!!)
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mCheckedIntercept = false
                mShouldIntercept = false
            }
            MotionEvent.ACTION_CANCEL -> {
                mCheckedIntercept = false
                mShouldIntercept = false
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!isSwipeBackEnable()) return false
        when (ev?.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                if (mCheckedIntercept)
                    if (mShouldIntercept)
                        return mDragHelper.shouldInterceptTouchEvent(ev)
                return false
            }
            else -> return mDragHelper.shouldInterceptTouchEvent(ev!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isSwipeBackEnable()) return false
        when (event?.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                if (mCheckedIntercept)
                    if (mShouldIntercept) {
                        mDragHelper.processTouchEvent(event)
                        return true
                    }
                return false
            }
            else -> {
                mDragHelper.processTouchEvent(event!!)
                return true
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isSwipeBackEnable()) {
            canvas?.drawARGB(mMaskAlpha - (mMaskAlpha * mFraction).toInt(), 0, 0, 0)
        }
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        Log.i("SwipeBackLayout", "start draw child")
        val ret = super.drawChild(canvas, child, drawingTime)
        if (isSwipeBackEnable() && isShadowEnable()) {
            if (child == getChildAt(0))
                drawShadow(canvas, child)
        }
        return ret
    }

    private fun drawShadow(canvas: Canvas?, child: View?) {
        Log.i("SwipeBackLayout", "start draw shadow: $child")
        val childRect = mShadowRect
        child?.getHitRect(childRect)
        val shadow = getNonNullShadowDrawable()
        when (mSwipeBackDirection) {
            SwipeBackDirection.RIGHT ->
                shadow.setBounds(
                    childRect.left - shadow.intrinsicWidth,
                    childRect.top,
                    childRect.left,
                    childRect.bottom
                )
            SwipeBackDirection.LEFT ->
                shadow.setBounds(
                    childRect.left,
                    childRect.top,
                    childRect.right + shadow.intrinsicWidth,
                    childRect.bottom
                )
            SwipeBackDirection.BOTTOM ->
                shadow.setBounds(
                    childRect.left,
                    childRect.top - shadow.intrinsicHeight,
                    childRect.right,
                    childRect.top
                )
            SwipeBackDirection.TOP ->
                shadow.setBounds(
                    childRect.left,
                    childRect.bottom,
                    childRect.right,
                    childRect.bottom + shadow.intrinsicHeight
                )
            else -> {
            }
        }
        mShadowDrawable?.draw(canvas!!)
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true))
            ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun shouldBackBySpeed(xvel: Float, yvel: Float): Boolean {
        return when (mSwipeBackDirection) {
            SwipeBackDirection.RIGHT -> xvel > mSwipeBackVelocity
            SwipeBackDirection.BOTTOM -> yvel > mSwipeBackVelocity
            SwipeBackDirection.LEFT -> xvel < mSwipeBackVelocity
            SwipeBackDirection.TOP -> yvel < mSwipeBackVelocity
            SwipeBackDirection.NONE -> false
        }
    }

    private fun smoothScrollToX(finalLeft: Int) {
        if (mDragHelper.settleCapturedViewAt(finalLeft, paddingTop))
            ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun smoothScrollToY(finalTop: Int) {
        if (mDragHelper.settleCapturedViewAt(paddingLeft, finalTop))
            ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun refreshFraction(view: View) {
        when (mSwipeBackDirection) {
            SwipeBackDirection.RIGHT -> mFraction = 1F * abs(view.left) / (width + mShadowSize)
            SwipeBackDirection.LEFT -> mFraction = 1F * abs(view.left) / (width + mShadowSize)
            SwipeBackDirection.BOTTOM -> mFraction = 1F * abs(view.top) / (height + mShadowSize)
            SwipeBackDirection.TOP -> mFraction = 1F * abs(view.top) / (height + mShadowSize)
            SwipeBackDirection.NONE -> {
            }
        }
        mFraction = maxOf(0F, mFraction)
        mFraction = minOf(1F, mFraction)
    }

    @NonNull
    private fun getNonNullShadowDrawable(): GradientDrawable {
        Log.i("SwipeBackLayout", "getNonNullShadowDrawable")
        if (mShadowDrawable == null) {
            val colors = IntArray(3) {
                mShadowColor
                ColorUtils.setAlphaComponent(mShadowColor, (Color.alpha(mShadowColor) * 0.3).toInt())
                Color.TRANSPARENT
            }
            when (mSwipeBackDirection) {
                SwipeBackDirection.RIGHT -> {
                    mShadowDrawable = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors)
                    mShadowDrawable!!.setSize(mShadowSize, 0)
                }
                SwipeBackDirection.LEFT -> {
                    mShadowDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
                    mShadowDrawable!!.setSize(mShadowSize, 0)
                }
                SwipeBackDirection.BOTTOM -> {
                    mShadowDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
                    mShadowDrawable!!.setSize(mShadowSize, 0)
                }
                SwipeBackDirection.TOP -> {
                    mShadowDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
                    mShadowDrawable!!.setSize(mShadowSize, 0)
                }
                else -> {
                    mShadowDrawable = GradientDrawable()
                    mShadowDrawable!!.setSize(0, 0)
                }
            }
        }
        Log.i("SwipeBackLayout", mShadowDrawable.toString())
        return mShadowDrawable!!
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return canSwipeBack()
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
            refreshFraction(capturedChild)
            onSwipeStart()
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return if (isSwipeBackEnable()) width + mShadowSize else 0
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return if (isSwipeBackEnable()) height + mShadowSize else 0
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var newLeft = 0
            if (mSwipeBackDirection == SwipeBackDirection.RIGHT)
                if (mSwipeBackOnlyEdge) {
                    if (mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT)) {
                        newLeft = minOf(maxOf(left, 0), width + mShadowSize)
                    }
                } else
                    newLeft = minOf(maxOf(left, 0), width + mShadowSize)
            else if (mSwipeBackDirection == SwipeBackDirection.LEFT)
                if (mSwipeBackOnlyEdge) {
                    if (mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_RIGHT))
                        newLeft = minOf(maxOf(left, -width - mShadowSize), 0)
                } else
                    newLeft = minOf(maxOf(left, -width - mShadowSize), 0)
            return newLeft
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            refreshFraction(changedView)
            onSwiping()
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            if (!isSwipeBackEnable()) return
            if (!canSwipeBack()) return
            val isBackEnd = shouldBackBySpeed(xvel, yvel) || mFraction >= mSwipeBackFactor
            if (isBackEnd) {
                when (mSwipeBackDirection) {
                    SwipeBackDirection.RIGHT -> smoothScrollToX(width + mShadowSize)
                    SwipeBackDirection.BOTTOM -> smoothScrollToY(height + mShadowSize)
                    SwipeBackDirection.LEFT -> smoothScrollToX(-width - mShadowSize)
                    SwipeBackDirection.TOP -> smoothScrollToY(-width - mShadowSize)
                    SwipeBackDirection.NONE -> {
                    }
                }
            } else {
                when (mSwipeBackDirection) {
                    SwipeBackDirection.RIGHT -> smoothScrollToX(0)
                    SwipeBackDirection.LEFT -> smoothScrollToX(0)
                    SwipeBackDirection.TOP -> smoothScrollToY(0)
                    SwipeBackDirection.BOTTOM -> smoothScrollToY(0)
                    SwipeBackDirection.NONE -> {
                    }
                }
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mFraction == 0F)
                    onSwipeEnd()
                else if (mFraction == 1F)
                    onSwipeEnd()
            }
        }

    }

    fun beforeSwipe() {
        if (::mSwipeBackListener.isInitialized)
            mSwipeBackListener.onBeforeSwipe(mFraction, mSwipeBackDirection)
    }

    fun onSwipeStart() {
        mSwiping = true
        if (::mSwipeBackListener.isInitialized)
            mSwipeBackListener.onStartSwipe(mFraction, mSwipeBackDirection)
    }

    fun onSwiping() {
        invalidate()
        if (::mSwipeBackListener.isInitialized)
            mSwipeBackListener.onSwiping(mFraction, mSwipeBackDirection)
    }

    fun onSwipeEnd() {
        mSwiping = false
        if (::mSwipeBackListener.isInitialized)
            mSwipeBackListener.onEndSwipe(mFraction, mSwipeBackDirection)
    }

    interface SwipeBackListener {

        fun onBeforeSwipe(swipeFraction: Float, @NonNull swipeDirection: SwipeBackDirection)

        fun onStartSwipe(swipeFraction: Float, @NonNull swipeDirection: SwipeBackDirection)

        fun onSwiping(swipeFraction: Float, @NonNull swipeDirection: SwipeBackDirection)

        fun onEndSwipe(swipeFraction: Float, @NonNull swipeDirection: SwipeBackDirection)

    }

}