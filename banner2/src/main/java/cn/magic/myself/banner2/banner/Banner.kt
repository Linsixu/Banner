package cn.magic.myself.banner2.banner

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import cn.magic.myself.banner2.R
import cn.magic.myself.banner2.adapter.BannerPagerAdapter
import cn.magic.myself.banner2.bean.BannerTextParams
import cn.magic.myself.banner2.inteface.IBannerItem
import cn.magic.myself.banner2.inteface.IBannerDataCallback
import cn.magic.myself.banner2.paint.BaseBannerIndicator
import cn.magic.myself.banner2.view.RoundCircleFrameLayout
import com.bilibili.app.comm.list.common.banner.PointBannerIndicator
import com.bilibili.app.comm.list.common.banner.TextAndPointIndicator
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Magic
 * on 2019-06-12.
 * email: linsixudream@163.com
 */
class Banner : RoundCircleFrameLayout, Handler.Callback, ViewPager.OnPageChangeListener {
    val INDEX_PAGER = 0
    val INDEX_INDICATOR = 1

    private lateinit var mPager: ViewPager
    private var mIndicator: BaseBannerIndicator? = null
    private var mAdapter: BannerPagerAdapter? = null
    private val POSITION_OFFSET = 10000
    private var mFliping: Boolean = false

    private val mBannerChildren = ArrayList<IBannerItem>()
    private var mOnBannerClickListener: IBannerClickListener? = null
    private var mOnBannerSlideListener: OnBannerSlideListener? = null
    private var mHandler: Handler? = null

    private var mBannerParams = BannerTextParams()

    companion object {
        private const val POSITION_OFFSET = 10000

        private const val MSG_FLIP = 110
        private const val BANNER_START_DELAY = 1500
        private const val BANNER_FLIP_INTERVAL = 2500

        private const val THREE_POINT_INDICATOR = 1
        private const val TEXT_AND_POINT_INDICATOR = 2
    }

    constructor(context: Context?) : super(context) {
        init(context!!, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context!!, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        mHandler = Handler(this)
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner)
        val count = array.indexCount
        try {
            for (i in 0 until count) {
                val flag = array.getIndex(i)
                when (flag) {
                    R.styleable.Banner_innerPaddingLeft -> mBannerParams.paddingLeft = array.getDimensionPixelSize(flag, mBannerParams.paddingLeft)
                    R.styleable.Banner_innerPaddingRight -> mBannerParams.paddingRight = array.getDimensionPixelSize(flag, mBannerParams.paddingRight)
                    R.styleable.Banner_innerPaddingTop -> mBannerParams.paddingTop = array.getDimensionPixelSize(flag, mBannerParams.paddingTop)
                    R.styleable.Banner_innerPaddingBottom -> mBannerParams.paddingBottom = array.getDimensionPixelSize(flag, mBannerParams.paddingBottom)
                    R.styleable.Banner_textOfBannerBackground -> mBannerParams.backGround = array.getDrawable(flag)
                    R.styleable.Banner_aspectRadioHeight -> mBannerParams.aspectTextHeight = array.getInt(flag, mBannerParams.aspectTextHeight)
                    R.styleable.Banner_aspectRadioWidth -> mBannerParams.aspectTextWidth = array.getInt(flag, mBannerParams.aspectTextWidth)
                    R.styleable.Banner_flipTime -> mBannerParams.flipTime = array.getInt(flag, mBannerParams.flipTime)
                    R.styleable.Banner_indicatorType -> mBannerParams.indicatorType = array.getInt(flag, mBannerParams.indicatorType)
                }
            }
        } finally {
            array.recycle()
        }

        initViewPager(context)

        initAdapter()
        mPager.adapter = mAdapter

        initIndicator(context, attrs!!)
        mIndicator?.setViewPager(mPager)
    }

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            MSG_FLIP -> {
                mHandler?.let {
                    it.removeMessages(MSG_FLIP)
                    if (mIndicator != null) {
                        if (mIndicator!!.isScrollIdle()) {
                            it.sendEmptyMessageDelayed(MSG_FLIP, mBannerParams.flipTime.toLong())
                            showNextItem()
                        } else {
                            it.sendEmptyMessageDelayed(MSG_FLIP, BANNER_START_DELAY.toLong())
                        }
                    } else {
                        it.sendEmptyMessageDelayed(MSG_FLIP, mBannerParams.flipTime.toLong())
                        showNextItem()
                    }
                }
            }
        }
        return true
    }

    fun showNextItem() {
        var currentPage = if (mIndicator == null) {
            mPager.currentItem
        } else {
            mIndicator!!.getCurrentPage()
        }
        if (currentPage < 0) {
            currentPage += POSITION_OFFSET
        }
        setCurrentItem(currentPage + 1)
    }

    fun setCurrentItem(position: Int) {
        if (mBannerChildren.isEmpty()) {
            return
        }

        if (mBannerChildren.size == 1) {
            if (position == POSITION_OFFSET) {
                val bannerItem = mBannerChildren[0]
                // only one item, no need loop
                stopFlipping()
                if (mOnBannerSlideListener != null) {
                    mOnBannerSlideListener!!.onSlideTo(bannerItem)
                }
            }
            return
        }
        if (mIndicator == null) {
            mPager.setCurrentItem(position, Math.abs(position - mPager.currentItem) == 1)
        } else {
            mIndicator!!.setCurrentItem(position)
        }
    }

    fun startFlippingNow() {
        mFliping = true
        mHandler!!.removeMessages(MSG_FLIP)
        mHandler!!.sendEmptyMessageDelayed(MSG_FLIP, 100)
    }

    fun stopFlipping() {
        mFliping = false
        mHandler!!.removeMessages(MSG_FLIP)
    }

    private fun initViewPager(context: Context) {
        mPager = ViewPager(context)
        mPager.id = R.id.banner

//        mPager.pageMargin = mInnerPadding
        mPager.offscreenPageLimit = 1 //force 3 pages
        addViewInLayout(mPager, INDEX_PAGER, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    protected fun initAdapter() {
        if (mAdapter == null) {
            mAdapter = BannerPagerAdapter(mBannerChildren)
            mAdapter!!.setBannerClickListener(mOnBannerClickListener)
        }
    }

    fun setBannerItems(banners: List<IBannerItem>?) {
        val newSize = banners?.size ?: 0
        val oldSize = mBannerChildren.size
        if (newSize == 0) {
            return
        }

        mBannerChildren.clear()
        mBannerChildren.addAll(banners!!)
        mIndicator?.setRealSize(mBannerChildren.size)
        if (mAdapter != null) {
            mAdapter!!.setBanners(mBannerChildren)
            mAdapter!!.notifyDataSetChanged()
        }

        if (oldSize == 0) {
            if (mAdapter != null && mAdapter!!.mBanners.size > 0) {
                val text = (mAdapter!!.getBannerItem(0) as? IBannerDataCallback)?.getShowText()
                (mIndicator as? TextAndPointIndicator)?.setText(text)
            }
            requestLayout()
        }
    }

    private fun initIndicator(context: Context, attrs: AttributeSet) {
        when (mBannerParams.indicatorType) {
            THREE_POINT_INDICATOR -> {
                mIndicator = PointBannerIndicator(context, attrs)
                mIndicator!!.setOnPageChangeListener(this)
                val params = LayoutParams(mBannerParams.aspectTextWidth, mBannerParams.aspectTextHeight)
                params.gravity = Gravity.BOTTOM or Gravity.RIGHT
                mIndicator!!.setRealSize(getCount())
                mIndicator!!.setPadding(mBannerParams.paddingLeft, mBannerParams.paddingTop, mBannerParams.paddingRight, mBannerParams.paddingBottom)
                addViewInLayout(mIndicator, INDEX_INDICATOR, params, true)
            }

            TEXT_AND_POINT_INDICATOR -> {
                mIndicator = TextAndPointIndicator(context, attrs)
                mIndicator!!.setOnPageChangeListener(this)
                val params = LayoutParams(mBannerParams.aspectTextWidth, mBannerParams.aspectTextHeight)
                params.gravity = Gravity.BOTTOM or Gravity.RIGHT
                mIndicator!!.setRealSize(getCount())
                mIndicator!!.setPadding(mBannerParams.paddingLeft, mBannerParams.paddingTop, mBannerParams.paddingRight, mBannerParams.paddingBottom)
                addViewInLayout(mIndicator, INDEX_INDICATOR, params, true)
            }

//            else -> {
//                mIndicator = DefaultBannerIndicator(context, attrs)
//                mIndicator!!.setOnPageChangeListener(this)
//            }
        }
        mIndicator?.let {
            it.background = mBannerParams.backGround
        }
    }

    fun getCount(): Int {
        return mBannerChildren.size
    }

    fun addItem(item: IBannerItem) {
        mBannerChildren.add(item)
    }

    override fun onPageScrollStateChanged(p0: Int) {
        if (mIndicator != null && mIndicator!!.isScrollIdle()) {
            mHandler!!.removeMessages(MSG_FLIP)
            mHandler!!.sendEmptyMessageDelayed(MSG_FLIP, mBannerParams.flipTime.toLong())
        }
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (mOnBannerSlideListener != null && mAdapter != null) {
            mOnBannerSlideListener!!.onSlideTo(mAdapter!!.getBannerItem(position))
        }
        val text = (mAdapter!!.getBannerItem(position) as? IBannerDataCallback)?.getShowText()
        (mIndicator as? TextAndPointIndicator)?.setText(text)
    }

    fun getPager(): ViewPager {
        return mPager
    }

    fun setOnBannerSlideListener(l: OnBannerSlideListener) {
        mOnBannerSlideListener = l
    }

    fun setOnBannerClickListener(l: IBannerClickListener) {
        mOnBannerClickListener = l
        if (mAdapter != null)
            mAdapter!!.setBannerClickListener(l)
    }

    interface IBannerClickListener {
        fun onClick(item: IBannerItem)
    }

    interface OnBannerSlideListener {
        fun onSlideTo(item: IBannerItem)
    }

    abstract class BaseBannerItem : IBannerItem {
        private var mItemViewCaches: SparseArray<View>? = null

        companion object {
            //原子操作
            private val mAutoAddId: AtomicInteger = AtomicInteger(1)
        }

        private fun getViewId(): Int {
            do {
                val result = mAutoAddId.get()
                var newResult = result + 1
                //超过16进制的最大值，重新赋值为1
                if (newResult > 0x00FFFFFF) newResult = 1
                if (mAutoAddId.compareAndSet(result, newResult)) {
                    return result
                }
            } while (true)
        }

        override fun getChildView(parentView: ViewGroup): View {
            if (mItemViewCaches == null) {
                mItemViewCaches = SparseArray(4)
            }
            var mView: View? = null

            for (i in 0 until mItemViewCaches!!.size()) {
                mView = mItemViewCaches!!.get(i)
                if (mView?.parent == null) {
                    //View已经不在Pager缓存机制里面，可以回收利用
                    break
                } else {
                    //如果还有着依赖，就先置空，下面再重新创建View
                    mView = null
                }
            }

            if (mView == null) {
                mView = createItemView(parentView)
                mView.let {
                    if (it.id == View.NO_ID) {
                        //为View设置id
                        it.id = getViewId()
                    }
                    mItemViewCaches!!.put(it.id, it)
                }
            } else {
                useOldView(mView)
            }
            return mView
        }

        fun onDestroy() {
            mItemViewCaches?.let {
                it.clear()
            }
        }

        abstract fun useOldView(itemView: View)
        abstract fun createItemView(viewGroup: ViewGroup): View
        abstract fun getLayoutId(): Int
    }
}