package cn.magic.myself.banner

import android.view.View
import cn.magic.myself.banner2.banner.BaseImageBanner
import cn.magic.myself.banner2.inteface.IBannerDataCallback
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
class ImageTextBanner(val b: BannerBean.DataBean) : BaseImageBanner(), IBannerDataCallback {

    override fun getImageUrl(): String {
        return b.image
    }

    override fun isNeedShowLogo(): Boolean {
        return false
    }

    override fun getLogoDrawableRes(): Int {
        return 0
    }

    override fun getShowText(): String {
        return b.title
    }

    lateinit var mImageView: SimpleDraweeView
    override fun useOldView(itemView: View) {
        mImageView = itemView.findViewById(R.id.draweeView) as SimpleDraweeView
        mImageView.setImageURI(b.image)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_banner
    }

    override fun onViewCreate(itemView: View) {
        mImageView = itemView.findViewById(R.id.draweeView) as SimpleDraweeView
        mImageView.setImageURI(b.image)
    }
}