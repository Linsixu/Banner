package cn.magic.myself.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.magic.myself.banner2.banner.Banner;


public class MainActivity extends AppCompatActivity {

    private List<BannerBean.DataBean> iBannerItems;
    private List<ImageTextBanner> mContent = new ArrayList<>();
    private Button mStart, mStop;
    private Banner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBanner = findViewById(R.id.banner);
        mStart = findViewById(R.id.start_flip);
        mStop = findViewById(R.id.stop_flip);

        BannerBean bannerBean = new BannerApiParser(this, "banner.json").getResponseFromLocal();
        iBannerItems = bannerBean.data;

        for (BannerBean.DataBean b : iBannerItems) {
            mContent.add(new ImageTextBanner(b));
        }

        mBanner.setBannerItems(mContent);

        mStart.setOnClickListener(view -> {
            mBanner.startFlippingNow();
        });

        mStop.setOnClickListener(view -> {
            mBanner.stopFlipping();
        });
    }
}
