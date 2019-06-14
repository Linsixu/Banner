/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package cn.magic.myself.banner2.bean

import android.support.annotation.ColorInt

/**
 * Created by Magic
 * on 2019-06-10.
 * email: linsixudream@163.com
 */
class TextIndicatorParams : BaseIndicatorParams() {
    var textSize: Int = 0
    @ColorInt
    var textColorId: Int = 0
    var initText: String = ""
    var isNeedThreePoint: Boolean = false
    var showText: String = ""
}
