package cn.magic.myself.banner2.bean

import android.graphics.drawable.Drawable
import android.view.ViewGroup

/**
 * Created by Magic
 * on 2019-06-13.
 * email: linsixu@bilibili.com
 */
data class BannerTextParams(
    var paddingLeft: Int = 0,
    var paddingRight: Int = 0,
    var paddingTop: Int = 0,
    var paddingBottom: Int = 0,
    var backGround: Drawable? = null,
    var flipTime: Int = 3000,
    var indicatorType: Int = 0,
    var aspectTextWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    var aspectTextHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
)