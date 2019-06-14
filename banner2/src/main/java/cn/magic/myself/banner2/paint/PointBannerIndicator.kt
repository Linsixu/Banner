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
    private lateinit var mBaseIndicator: BaseIndicatorParams

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initData(context: Context, attrs: AttributeSet?) {
        mBaseIndicator = BaseIndicatorParams()
        val array = context.obtainStyledAttributes(attrs, R.styleable.BaseBannerIndicator)
        if (array != null) {
            val dp3 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics).toInt()
            mBaseIndicator.circleRadius = array.getDimension(R.styleable.BaseBannerIndicator_radius, dp3.toFloat()).toInt()
            mBaseIndicator.offset = array.getDimension(R.styleable.BaseBannerIndicator_offset, dp3.toFloat()).toInt()
            mBaseIndicator.selectCircleColor = array.getColor(R.styleable.BaseBannerIndicator_selectColorId, 0)
            mBaseIndicator.unSelectCircleColor = array.getColor(R.styleable.BaseBannerIndicator_unSelectColorId, 0)
            array.recycle()
        }
        mSelectPaint = Paint()
        mSelectPaint.style = Paint.Style.FILL
        mSelectPaint.color = mBaseIndicator.selectCircleColor
        mSelectPaint.isAntiAlias = true

        mUnSelectPaint = Paint()
        mUnSelectPaint.style = Paint.Style.FILL
        mUnSelectPaint.color = mBaseIndicator.unSelectCircleColor
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
            result = (paddingLeft + paddingRight + count * 2 * mBaseIndicator.circleRadius + (count - 1) * mBaseIndicator.offset)
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
            result = 2 * mBaseIndicator.circleRadius + paddingTop + paddingBottom
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

        val yOffset = (paddingTop + mBaseIndicator.circleRadius).toFloat()

        var dX: Float
        var dY: Float

        for (iLoop in 0 until mRealSize) {
            dX = getCurrentOffsetX(iLoop).toFloat()
            dY = yOffset
            // Only paint fill if not completely transparent
            if (mSelectPaint.alpha > 0) {
                canvas.drawCircle(dX, dY, mBaseIndicator.circleRadius.toFloat(), mSelectPaint)
            }
        }

        val current = if (mRealSize == 0) mCurrentPage else mCurrentPage % mRealSize
        dX = getCurrentOffsetX(current).toFloat()
        dY = yOffset
        canvas.drawCircle(dX, dY, mBaseIndicator.circleRadius.toFloat(), mUnSelectPaint)
        return
    }

    private fun getCurrentOffsetX(current: Int): Int {
        return paddingLeft + current * (mBaseIndicator.circleRadius * 2 + mBaseIndicator.offset) + mBaseIndicator.circleRadius
    }
}