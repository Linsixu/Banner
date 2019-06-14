/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package cn.magic.myself.banner2.inteface

import android.support.annotation.DrawableRes

/**
 * Created by Magic
 * on 2019-06-05.
 * email: linsixudream@163.com
 */
interface IBannerDataCallback {
    fun getImageUrl(): String

    fun getShowText(): String
}