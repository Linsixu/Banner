/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package cn.magic.myself.banner2.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import cn.magic.myself.banner2.R;

/**
 * Created by Magic
 * on 2019/3/20.
 * email: linsixudream@163.com
 */
public class RoundCircleFrameLayout extends FrameLayout {
    public static final int TOP_CIRCLE = 1; //头部圆
    public static final int BOTTOM_CIRCLE = 2;//底部圆
    public static final int ALL_CIRCLE = 3;//布局整体圆角
    private float mRadius;
    private float[] mRadii = new float[8];
    private Rect mBoundsI;
    @Nullable
    private Path mRoundRectPath;

    public RoundCircleFrameLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoundCircleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoundCircleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("NullAway")
    public RoundCircleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCircleFrameLayout, defStyleAttr, 0);
        mRadii[0] = a.getDimension(R.styleable.RoundCircleFrameLayout_cornerTopRadius, 0f);
        mRadii[4] = a.getDimension(R.styleable.RoundCircleFrameLayout_cornerButtomRadius, 0f);
        boolean isNeedAllcorner = a.getBoolean(R.styleable.RoundCircleFrameLayout_isNeedAllcorner, false);
        float allRadius = a.getDimension(R.styleable.RoundCircleFrameLayout_cornerAllRadius, 0f);
        mBoundsI = new Rect();
        if (isNeedAllcorner) {
            setRadius(allRadius);
        } else {
            if (mRadii[0] >= mRadii[4]) {
                //头部圆半径比底部大的，只最大值大的值
                setTopCornerRadii(mRadii[0]);
            } else {
                //底部圆半径比头部大的，只最大值大的值
                setBottomCornerRadii(mRadii[4]);
            }
        }
        a.recycle();
    }

    public void setRadius(float radius) {
        if (mRadius != radius) {
            mRadius = radius;
            this.mRadii[1] = this.mRadii[0] = radius;
            this.mRadii[3] = this.mRadii[2] = radius;
            this.mRadii[5] = this.mRadii[4] = radius;
            this.mRadii[7] = this.mRadii[6] = radius;
            onSetRadius(ALL_CIRCLE);
        }
    }

    public void setTopCornerRadii(float topLeftAndTopRight) {
        mRadius = topLeftAndTopRight;
        this.mRadii[1] = this.mRadii[0] = topLeftAndTopRight;
        this.mRadii[3] = this.mRadii[2] = topLeftAndTopRight;
        this.mRadii[5] = this.mRadii[4] = 0f;
        this.mRadii[7] = this.mRadii[6] = 0f;
        onSetRadius(TOP_CIRCLE);
    }

    public void setBottomCornerRadii(float bottomLeftAndRight) {
        mRadius = bottomLeftAndRight;
        this.mRadii[1] = this.mRadii[0] = 0f;
        this.mRadii[3] = this.mRadii[2] = 0f;
        this.mRadii[5] = this.mRadii[4] = bottomLeftAndRight;
        this.mRadii[7] = this.mRadii[6] = bottomLeftAndRight;
        onSetRadius(BOTTOM_CIRCLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onSetRadius(int circle) {
        final boolean needClip = mRadius > 0f;
        setWillNotDraw(!needClip); // only call draw(canvas) when set radius
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            if (needClip) {
                setClipToOutline(true);
                setOutlineProvider(newViewOutlineProvider(circle));
            } else {
                setClipToOutline(false);
            }
        } else if (Build.VERSION_CODES.HONEYCOMB <= sdk && sdk < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (needClip && getLayerType() != LAYER_TYPE_SOFTWARE)
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            else
                setLayerType(LAYER_TYPE_HARDWARE, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ViewOutlineProvider newViewOutlineProvider(int circle) {
        if (circle == TOP_CIRCLE) {
            return new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), (int) (view.getHeight() + mRadius), mRadius);
                }
            };
        } else if (circle == BOTTOM_CIRCLE) {
            return new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, -(int) (mRadius), view.getWidth(), view.getHeight(), mRadius);
                }
            };
        }
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mRadius);
            }
        };
    }

    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (mRoundRectPath == null && mRadius != 0) {
                Path clipPath = new Path();
                mBoundsI.set(0, 0, getWidth(), getHeight());
                RectF rectF = new RectF(mBoundsI);
                clipPath.addRoundRect(rectF, mRadii, Path.Direction.CW);
                mRoundRectPath = clipPath;
            }
            canvas.clipPath(mRoundRectPath);
        }
        super.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRoundRectPath = null;
    }
}
