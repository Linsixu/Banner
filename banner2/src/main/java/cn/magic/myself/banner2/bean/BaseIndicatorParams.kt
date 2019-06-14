package cn.magic.myself.banner2.bean

import android.support.annotation.ColorInt

/**
 * Created by Magic
 * on 2019-06-13.
 * email: linsixu@bilibili.com
 */
open class BaseIndicatorParams {
    //选中圆点的颜色
    @ColorInt
    var selectCircleColor: Int = 0
    //非选中圆点的颜色
    @ColorInt
    var unSelectCircleColor: Int = 0
    //圆点半径
    var circleRadius: Int = 0

    //圆点间距
    var offset: Int = 0
}