package me.robbin.architecture.ui

import androidx.appcompat.app.AppCompatActivity
import me.robbin.swipeback.SwipeBackAbility
import me.robbin.swipeback.SwipeBackDirection
import me.robbin.swipeback.SwipeBackTransformer


/**
 * @PackageName: me.robbin.architecture.ui
 * @Name:        BaseSwipeBackActivity
 * @Description: TODO
 * @UpdateDate:  2021/3/2 下午5:17
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/2 下午5:17
 */

abstract class BaseSwipeBackActivity :
    AppCompatActivity(),
    SwipeBackAbility.Direction,
    SwipeBackAbility.Transformer,
    SwipeBackAbility.OnlyEdge,
    SwipeBackAbility.ForceEdge {

    protected var mSwipeBackDirection: SwipeBackDirection = SwipeBackDirection.RIGHT
    protected var mSwipeBackTransformer: SwipeBackTransformer? = null
    protected var mSwipeBackOnlyEdge: Boolean = false
    protected var mSwipeBackForceEdge: Boolean = false

    override fun swipeBackDirection(): SwipeBackDirection = mSwipeBackDirection

    override fun swipeBackTransformer(): SwipeBackTransformer? = mSwipeBackTransformer

    override fun swipeBackOnlyEdge(): Boolean = mSwipeBackOnlyEdge

    override fun swipeBackForceEdge(): Boolean = mSwipeBackForceEdge
}