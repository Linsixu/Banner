/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package com.bilibili.app.comm.list.common.banner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import cn.magic.myself.banner2.R
import cn.magic.myself.banner2.banner.BannerViewModel
import cn.magic.myself.banner2.bean.BaseIndicatorParams
import cn.magic.myself.banner2.bean.TextIndicatorParams
import cn.magic.myself.banner2.paint.BaseBannerIndicator

/**
 * Created by Magic
 * on 2019-06-05.
 * email: linsixudream@163.com
 */
class PointBannerIndicator : BaseBannerIndicator {
    private lateinit var mSelectPaint: Paint
    private lateinit var mUnSelectPaint: Paint
    private lateinit var mBaseIndicatorParams: BaseIndicatorParams

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initData(context: Context, attrs: AttributeSet?) {
        mBaseIndicatorParams = BaseIndicatorParams()
        val array = context.obtainStyledAttributes(attrs, R.styleable.BaseBannerIndicator)
        if (array != null) {
            val dp3 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics).toInt()
            mBaseIndicatorParams.circleRadius = array.getDimension(R.styleable.BaseBannerIndicator_radius, dp3.toFloat()).toInt()
            mBaseIndicatorParams.offset = array.getDimension(R.styleable.BaseBannerIndicator_offset, dp3.toFloat()).toInt()
            mBaseIndicatorParams.selectCircleColor = array.getColor(R.styleable.BaseBannerIndicator_selectColorId, 0)
            mBaseIndicatorParams.unSelectCircleColor = array.getColor(R.styleable.BaseBannerIndicator_unSelectColorId, 0)
            mBaseIndicatorParams.circlePointGravity = array.getInt(R.styleable.BaseBannerIndicator_circlePointGravity, mBaseIndicatorParams.circlePointGravity)
            array.recycle()
        }
        mSelectPaint = Paint()
        mSelectPaint.style = Paint.Style.FILL
        mSelectPaint.color = mBaseIndicatorParams.selectCircleColor
        mSelectPaint.isAntiAlias = true

        mUnSelectPaint = Paint()
        mUnSelectPaint.style = Paint.Style.FILL
        mUnSelectPaint.color = mBaseIndicatorParams.unSelectCircleColor
        mUnSelectPaint.isAntiAlias = true
    }

    override fun getMeasureWidth(widthMeasureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)

        if (specMode == MeasureSpec.EXACTLY || mViewPager == null) {
            //We were told how big to be(match_parent)
            result = specSize
        } else {
            //Calculate the width according the views count
            val count = if (mRealSize == 0) mViewPager!!.adapter!!.count else mRealSize
            result = (paddingLeft + paddingRight + count * 2 * mBaseIndicatorParams.circleRadius + (count - 1) * mBaseIndicatorParams.offset)
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun getMeasureHeight(heightMeasureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        val specSize = MeasureSpec.getSize(heightMeasureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize
        } else {
            //Measure the height
            result = 2 * mBaseIndicatorParams.circleRadius + paddingTop + paddingBottom
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onDrawView(canvas: Canvas) {
        if (mViewPager == null) {
            return
        }
        val count = mViewPager!!.adapter!!.count
        if (count == 0 || mRealSize == 0) {
            return
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1)
            return
        }

        val yOffset = (paddingTop + mBaseIndicatorParams.circleRadius).toFloat()

        var dX: Float
        var dY: Float

        for (iLoop in 0 until mRealSize) {
            dX = switchGravity(iLoop).toFloat()
            dY = yOffset
            // Only paint fill if not completely transparent
            if (mSelectPaint.alpha > 0) {
                canvas.drawCircle(dX, dY, mBaseIndicatorParams.circleRadius.toFloat(), mUnSelectPaint)
            }
        }

        val current = if (mRealSize == 0) mCurrentPage else mCurrentPage % mRealSize
        dX = switchGravity(current).toFloat()
        dY = yOffset
        canvas.drawCircle(dX, dY, mBaseIndicatorParams.circleRadius.toFloat(), mSelectPaint)
        return
    }

    private fun getLeftOffsetX(current: Int): Int {
        return paddingLeft + current * (mBaseIndicatorParams.circleRadius * 2 + mBaseIndicatorParams.offset) + mBaseIndicatorParams.circleRadius
    }

    private fun getCenterOffsetX(current: Int): Int {
        val circleWidth = (mBaseIndicatorParams.circleRadius * 2) * mRealSize + (mRealSize - 1) * mBaseIndicatorParams.offset
        val actualLeft = (width - circleWidth) / 2
        return actualLeft + current * (mBaseIndicatorParams.circleRadius * 2 + mBaseIndicatorParams.offset) + mBaseIndicatorParams.circleRadius
    }

    private fun getRightOffsetX(current: Int): Int {
        val circleWidth = (mBaseIndicatorParams.circleRadius * 2) * mRealSize + (mRealSize - 1) * mBaseIndicatorParams.offset
        val actualLeft = width - paddingRight - circleWidth
        return actualLeft + current * (mBaseIndicatorParams.circleRadius * 2 + mBaseIndicatorParams.offset) + mBaseIndicatorParams.circleRadius
    }

    private fun switchGravity(current: Int): Int {
        when (mBaseIndicatorParams.circlePointGravity) {
            LEFT_OF_PARENT -> return getLeftOffsetX(current)
            CENTER_OF_PARENT -> return getCenterOffsetX(current)
            RIGHT_OF_PARENT -> return getRightOffsetX(current)
            else -> return getRightOffsetX(current)
        }
    }

    companion object {
        const val LEFT_OF_PARENT = 1
        const val CENTER_OF_PARENT = 2
        const val RIGHT_OF_PARENT = 3
    }
}