package cn.magic.myself.banner2.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import cn.magic.myself.banner2.banner.Banner
import cn.magic.myself.banner2.banner.BannerViewModel
import cn.magic.myself.banner2.inteface.IBannerItem

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
class BannerPagerAdapter : PagerAdapter, View.OnClickListener {
    var mBanners = mutableListOf<IBannerItem>()
    var mBannerClickListener: Banner.IBannerClickListener? = null

    constructor(mBanners: ArrayList<IBannerItem>) : super() {
        this.mBanners.addAll(mBanners)
    }

    override fun getItemPosition(`object`: Any?): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onClick(v: View) {
        mBannerClickListener?.let {
            val view = (v.tag as? IBannerItem) ?: return
            it.onClick(view)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        BannerViewModel.getBannerViewModel(container!!.context)!!.mPagerPosition.value = position
        val itemBanner = getBannerItem(position)
        val itemView = itemBanner.getChildView(container)
        itemView.tag = itemBanner
        itemView.setOnClickListener(this)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
        (`object` as? View)?.let {
            container.removeView(it)
        }
    }

    fun getBannerIndex(position: Int): Int {
        return position % mBanners.size
    }

    fun getBannerItem(position: Int): IBannerItem {
        return mBanners[getBannerIndex(position)]
    }

    fun setBanners(banners: List<IBannerItem>) {
        mBanners.clear()
        mBanners.addAll(banners)
    }

    fun setBannerClickListener(listener: Banner.IBannerClickListener?) {
        this.mBannerClickListener = listener
    }
}