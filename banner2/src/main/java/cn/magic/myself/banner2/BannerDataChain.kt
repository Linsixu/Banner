package cn.magic.myself.banner2

/**
 * Created by Magic
 * on 2019-06-13.
 * email: linsixu@bilibili.com
 */
class BannerDataChain<T> {
    var currentIndex: T? = null
    var top = BannerData<T>()
    var first: BannerData<T>? = null
    var last: BannerData<T>? = null
    fun buildChain(list: ArrayList<T>): BannerData<T>? {
        if (list.size <= 0) return null
        for (index in 0 until list.size) {
            val item = list[index]
            if (index == 0) {
                top.t = item
                top.index = index

                first = top
                last = top
            } else {
                var next = BannerData<T>()
                next.t = item
                next.index = index

                next.top = first
                first!!.bottom = next
                first = next
            }
        }
        var finally = BannerData<T>()
        finally.t = null
        finally.index = -1

        finally.top = first
        first!!.bottom = finally

        //build circle chain
        top.top = finally
        finally.bottom = top
        return top
    }

    fun slipLeft(firstItem: BannerData<T>): BannerData<T>? {
        if (firstItem == null) return null
        var newTop = firstItem.top
        if (newTop!!.index == -1 && newTop.t == null) {
            newTop = newTop.top
        }
        return newTop
    }

    fun slipRight(firstItem: BannerData<T>): BannerData<T>? {
        if (firstItem == null) return null
        var newTop = firstItem.bottom
        if (newTop!!.index == -1 && newTop.t == null) {
            newTop = newTop.bottom
        }
        return newTop
    }
}