package com.example.mynet.buttonmove;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.mynet.utils.DensityUtil;


/**
 * 属性动画画对勾
 */
public class PathTextView extends AppCompatTextView {

    private final Context mContext;
    private Paint mPaint;
    private PathMeasure mPathMeasure;//测量
    private Path mPath;
    private Path mDst;//微分的一段
    private float animatedValue;//动画执行进度
    private float mLength;
    private int height;
    private int width;
    private OnOkAnimatorListener okAnimatorListener;

    public PathTextView(Context context) {
        this(context, null);
    }

    public PathTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        width = DensityUtil.dp2px(mContext, 14);
        height = DensityUtil.dp2px(mContext, 14);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#9e100e"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DensityUtil.dp2px(mContext,2));

        //对号总路径
        mPath = new Path();
        mPath.moveTo(0, DensityUtil.dp2px(mContext, 6));
        mPath.lineTo(DensityUtil.dp2px(mContext, 5), DensityUtil.dp2px(mContext, 11));
        mPath.lineTo(DensityUtil.dp2px(mContext, 13), DensityUtil.dp2px(mContext, 3));

        //微分路径，把总路径微分成一段段的小路径，每次调用onDraw()画一小段
        mDst = new Path();

        //相当于尺子，测量总路径
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, false);
        mLength = mPathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (okAnimatorListener != null) {
                    okAnimatorListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画之前重置微分路径
        mDst.reset();
        mDst.lineTo(0, 0);

        //计算此次应该画的路径长度
        float stop = mLength * animatedValue;
        float start = 0;

        //给mDst赋值，绘制
        mPathMeasure.getSegment(start, stop, mDst, true);
        canvas.drawPath(mDst, mPaint);
    }

    public void setOkAnimatorListener(OnOkAnimatorListener listener) {
        okAnimatorListener = listener;
    }

    public interface OnOkAnimatorListener {
        void onEnd();
    }
}