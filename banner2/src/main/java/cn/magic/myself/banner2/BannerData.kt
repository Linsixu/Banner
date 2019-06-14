package cn.magic.myself.banner2

/**
 * Created by Magic
 * on 2019-06-13.
 * email: linsixu@bilibili.com
 */
class BannerData<T> {
    var t: T? = null
    var top: BannerData<T>? = null
    var bottom: BannerData<T>? = null
    var index: Int = 0
}