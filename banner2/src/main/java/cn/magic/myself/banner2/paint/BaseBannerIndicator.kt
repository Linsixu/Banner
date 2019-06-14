/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package cn.magic.myself.banner2.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import cn.magic.myself.banner2.bean.BaseIndicatorParams
import cn.magic.myself.banner2.bean.TextIndicatorParams

/**
 * Created by Magic
 * on 2019-06-05.
 * email: linsixudream@163.com
 */
abstract class BaseBannerIndicator : View, ViewPager.OnPageChangeListener {
    protected var mViewPager: ViewPager? = null
    protected var mCurrentPage = 0

    private var mScrollState: Int = 0
    private var mListener: ViewPager.OnPageChangeListener? = null
    protected var mRealSize: Int = 0
    protected var mBackGround: Drawable? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initData(context, attrs)
    }

    fun setRealSize(count: Int) {
        //三点的数量
        mRealSize = count
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawView(canvas)
    }

    fun setViewPager(view: ViewPager) {
        if (mViewPager === view) {
            return
        }
        if (mViewPager != null) {
            mViewPager!!.removeOnPageChangeListener(this)
        }
        if (view.adapter == null) {
            throw IllegalStateException("ViewPager does not have adapter instance.")
        }
        mViewPager = view
        mViewPager!!.addOnPageChangeListener(this)
        invalidate()
    }

    fun setViewPager(view: ViewPager, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    fun setCurrentItem(item: Int) {
        if (mViewPager == null) {
            throw IllegalStateException("ViewPager has not been bound.")
        }
        mViewPager!!.setCurrentItem(item, Math.abs(mCurrentPage - item) == 1)

        mCurrentPage = item
        invalidate()
    }

    fun notifyDataSetChanged() {
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {
        mScrollState = state

        if (mListener != null) {
            mListener!!.onPageScrollStateChanged(state)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mListener != null) {
            mListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        mCurrentPage = position
        requestLayout()
        if (mListener != null) {
            mListener!!.onPageSelected(position)
        }
    }

    fun isScrollIdle(): Boolean {
        return mScrollState == ViewPager.SCROLL_STATE_IDLE
    }

    fun getCurrentPage(): Int {
        return mCurrentPage
    }

    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        mListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getMeasureWidth(widthMeasureSpec)
        val height = getMeasureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    abstract fun getMeasureWidth(widthMeasureSpec: Int): Int
    abstract fun getMeasureHeight(heightMeasureSpec: Int): Int
    abstract fun onDrawView(canvas: Canvas)
    abstract fun initData(context: Context, attrs: AttributeSet?)
}