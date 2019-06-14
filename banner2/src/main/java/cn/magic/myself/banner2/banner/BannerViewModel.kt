package cn.magic.myself.banner2.banner

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by Magic
 * on 2019-06-13.
 * email: linsixu@bilibili.com
 */
class BannerViewModel : ViewModel() {
    var mPagerPosition: MutableLiveData<Int> = MutableLiveData()

    companion object {
        fun getBannerViewModel(context: Context): BannerViewModel? {
            if (context is FragmentActivity) {
                return ViewModelProviders.of(context).get(BannerViewModel::class.java)
            } else if (context is Fragment) {
                return ViewModelProviders.of(context).get(BannerViewModel::class.java)
            }
            return null
        }
    }
}