package cn.magic.myself.banner2;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Magic
 * on 2019-06-13.
 * email: linsixu@bilibili.com
 */
public class TestChain {
    @Test
    public void Test() {
        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        list.add("23");
        list.add("34");
        BannerDataChain<String> chain = new BannerDataChain<>();
        BannerData<String> b = chain.buildChain(list);
    }
}
