package cn.magic.myself.banner

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author 林思旭
 * @since 2019/5/9
 */
open class BaseApiParser(val context: Context, val fileName: String) {

    protected fun getJson(): String {
        val stringBuilder = StringBuilder()
        //获得assets资源管理器
        val assetManager: AssetManager = context.assets
        //使用IO流读取json文件内容
        val bufferedReader = BufferedReader(InputStreamReader(assetManager.open(fileName), "utf-8"))
        var line: String?
        line = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
        return stringBuilder.toString()
    }
}