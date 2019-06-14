package cn.magic.myself.banner2.inteface

import android.view.View
import android.view.ViewGroup

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
interface IBannerItem {
    fun getChildView(parentView: ViewGroup): View
}