package cn.magic.myself.banner

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}