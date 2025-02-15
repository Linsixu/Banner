package cn.magic.myself.banner;

import java.util.List;

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
public class BannerBean {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : [{"title":"小电视无外乎觉得时间啊时间咖啡机啊电视剧大奖啊的手机卡的手机卡的撒的接口设计卡戴珊姐卡的手机卡上","image":"http://i0.hdslb.com/bfs/archive/b5ee3cbe8ef4c8245b929ee14e12cd0fb5af2d32.png"},{"title":"agw","image":"http://i0.hdslb.com/bfs/archive/b5ee3cbe8ef4c8245b929ee14e12cd0fb5af2d32.png"}]
     */

    public int code;
    public String message;
    public int ttl;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * title : 小电视无外乎觉得时间啊时间咖啡机啊电视剧大奖啊的手机卡的手机卡的撒的接口设计卡戴珊姐卡的手机卡上
         * image : http://i0.hdslb.com/bfs/archive/b5ee3cbe8ef4c8245b929ee14e12cd0fb5af2d32.png
         */

        public String title;
        public String image;
    }
}
