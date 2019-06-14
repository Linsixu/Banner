package cn.magic.myself.banner2.paint

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Created by Magic
 * on 2019-06-14.
 * email: linsixu@bilibili.com
 */
class DefaultBannerIndicator : BaseBannerIndicator {
    override fun getMeasureWidth(widthMeasureSpec: Int): Int {
        return 0
    }

    override fun getMeasureHeight(heightMeasureSpec: Int): Int {
        return 0
    }

    override fun onDrawView(canvas: Canvas) {
    }

    override fun initData(context: Context, attrs: AttributeSet?) {
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}