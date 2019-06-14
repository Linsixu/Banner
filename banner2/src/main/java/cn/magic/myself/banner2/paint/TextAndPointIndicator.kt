/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package com.bilibili.app.comm.list.common.banner

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.ColorRes
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
open class TextAndPointIndicator : BaseBannerIndicator {
    private lateinit var mTextPaint: Paint
    //    private var mOffset: Int = 0
    private lateinit var mSelectPaint: Paint
    private lateinit var mUnSelectPaint: Paint

    private lateinit var mTextIndicator: TextIndicatorParams

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initData(context: Context, attrs: AttributeSet?) {
        mTextIndicator = TextIndicatorParams()

        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextAndPointIndicator)
        if (array != null) {
            mTextIndicator.textSize = array.getDimensionPixelSize(R.styleable.TextAndPointIndicator_textSizeOfBanner, 0)
            mTextIndicator.textColorId = array.getColor(R.styleable.TextAndPointIndicator_textColorOfBanner, 0)
            array.recycle()
        }
        val array1 = context.obtainStyledAttributes(attrs, R.styleable.BaseBannerIndicator)
        if (array1 != null) {
            val dp3 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics).toInt()
            mTextIndicator.circleRadius = array1.getDimension(R.styleable.BaseBannerIndicator_radius, dp3.toFloat()).toInt()
            mTextIndicator.offset = array1.getDimensionPixelSize(R.styleable.BaseBannerIndicator_offset, dp3)
            mTextIndicator.selectCircleColor = array1.getColor(R.styleable.BaseBannerIndicator_selectColorId, 0)
            mTextIndicator.unSelectCircleColor = array1.getColor(R.styleable.BaseBannerIndicator_unSelectColorId, 0)
            array1.recycle()
        }

        //draw text
        mTextPaint = Paint()
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = mTextIndicator.textSize.toFloat()
        mTextPaint.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint.color = mTextIndicator.textColorId
        mTextPaint.isAntiAlias = true

        //draw circle
        mSelectPaint = Paint()
        mSelectPaint.style = Paint.Style.FILL
        mSelectPaint.color = mTextIndicator.selectCircleColor
        mSelectPaint.isAntiAlias = true

        mUnSelectPaint = Paint()
        mUnSelectPaint.color = mTextIndicator.unSelectCircleColor
        mUnSelectPaint.style = Paint.Style.FILL
        mUnSelectPaint.isAntiAlias = true

        invalidatePaint()
    }

    private fun invalidatePaint() {
        if (mTextIndicator.textColorId != 0 || mTextIndicator.selectCircleColor != 0 || mTextIndicator.unSelectCircleColor != 0) {
            invalidate()
        }
    }

    fun setText(text: String?) {
        if (text.isNullOrBlank()) return
        this.mTextIndicator.initText = text!!
    }

    companion object {
        val ELLIPSIS_NORMAL = charArrayOf('\u2026') // this is "..."
        val ELLIPSIS_STRING = String(ELLIPSIS_NORMAL)
    }

    override fun getMeasureWidth(widthMeasureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        val threePointLength = mTextPaint.measureText(ELLIPSIS_STRING)
        val textWidth: Float = if (mTextIndicator.initText.isNullOrBlank()) 0f else mTextPaint.measureText(mTextIndicator.initText)
        val count = if (mRealSize == 0) mViewPager!!.adapter!!.count else mRealSize
        result = (paddingLeft + paddingRight + count * 2 * mTextIndicator.circleRadius + (count - 1) * mTextIndicator.offset)
        mTextIndicator.showText = mTextIndicator.initText
        if (result + textWidth > specSize) {
            mTextIndicator.showText = computeNewWidth(textWidth, specSize, threePointLength = threePointLength, result = result)
        }
        if (specMode == MeasureSpec.EXACTLY || mViewPager == null) {
            //We were told how big to be(match_parent)
            result = specSize
        } else {
            //Calculate the width according the views count
            result += textWidth.toInt()
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    private fun computeNewWidth(textWidth: Float, parentWidth: Int, threePointLength: Float, result: Int): String {
        mTextIndicator.showText.let {
            val wordItemWidth = textWidth / it.length
            val more = parentWidth - threePointLength - result
            val textNumber = (more / wordItemWidth).toInt()
            val newString = StringBuilder(it.substring(0, textNumber))
            return newString.append(ELLIPSIS_STRING).toString()
        }
        return ""
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
            result = 2 * mTextIndicator.circleRadius + paddingTop + paddingBottom
            val mFontMetrics = mTextPaint.fontMetrics
            val mTextHeight = mFontMetrics.descent - mFontMetrics.ascent + paddingTop + paddingBottom
            if (result < mTextHeight) {
                result = mTextHeight.toInt()
            }
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onDrawView(canvas: Canvas) {
        val textDescentY = paddingTop + 2 * mTextIndicator.circleRadius
        val textWidth = if (mTextIndicator.showText.isNullOrBlank()) 0f else mTextPaint.measureText(mTextIndicator.showText)
        val mFontMetrics = mTextPaint.fontMetrics

        mTextIndicator.showText.let {
            drawText(canvas, it, mTextIndicator.textColorId, 0, it.length, paddingLeft.toFloat(), textDescentY.toFloat() - 2, mTextPaint)
        }

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

        val yOffset = (paddingTop + mTextIndicator.circleRadius).toFloat()

        var dX: Float = 0f
        var dY: Float = 0f

        for (iLoop in 0 until mRealSize) {
            dX = getCurrentOffsetX(iLoop, textWidth.toInt(), mRealSize).toFloat()
            dY = yOffset
            // Only paint fill if not completely transparent
            if (mSelectPaint.alpha > 0) {
                canvas.drawCircle(dX, dY, mTextIndicator.circleRadius.toFloat(), mSelectPaint)
            }
        }

        val current = if (mRealSize == 0) mCurrentPage else mCurrentPage % mRealSize
        dX = getCurrentOffsetX(current, textWidth.toInt(), mRealSize).toFloat()
        dY = yOffset
        canvas.drawCircle(dX, dY, mTextIndicator.circleRadius.toFloat(), mUnSelectPaint)
    }

    protected fun drawText(
        canvas: Canvas,
        text: CharSequence,
        textColor: Int,
        start: Int,
        end: Int,
        textOffsetX: Float,
        textOffsetY: Float,
        paint: Paint
    ) {
        paint.pathEffect = null
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL
        paint.color = textColor
        canvas.drawText(text, start, end, textOffsetX, textOffsetY, paint)
    }

    private fun getCurrentOffsetX(current: Int, textWidth: Int, realSize: Int): Int {
        val leftX = width - ((realSize - 1) * mTextIndicator.offset + (realSize) * 2 * mTextIndicator.circleRadius) - paddingRight
        return leftX + current * (mTextIndicator.circleRadius * 2 + mTextIndicator.offset) + mTextIndicator.circleRadius
    }
}