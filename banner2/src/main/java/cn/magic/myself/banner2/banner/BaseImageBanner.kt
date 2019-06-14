package cn.magic.myself.banner2.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
abstract class BaseImageBanner : Banner.BaseBannerItem() {


    override fun createItemView(viewGroup: ViewGroup): View {
        val view = LayoutInflater.from(viewGroup.context).inflate(getLayoutId(), viewGroup, false)
        onViewCreate(view)
        return view
    }

    abstract fun onViewCreate(itemView: View)
}