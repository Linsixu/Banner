package cn.magic.myself.banner

import android.content.Context
import com.alibaba.fastjson.JSON

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
class BannerApiParser(context: Context, jsonFile: String) : BaseApiParser(context, jsonFile) {
    fun getResponseFromLocal(): BannerBean {
        val jsonObject = JSON.parseObject(getJson())
        val feedCard = BannerBean()

        feedCard.code = jsonObject.getIntValue("code")
        feedCard.message = jsonObject.getString("message")
        if (jsonObject.containsKey("data")) {
            val result = jsonObject.getJSONArray("data").toJSONString()
            val t = JSON.parseArray(result, BannerBean.DataBean::class.java)
            feedCard.data = t
        }
        return feedCard
    }
}