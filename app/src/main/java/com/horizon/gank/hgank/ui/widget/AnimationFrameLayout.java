package com.horizon.gank.hgank.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.AutoFrameLayout;

public class AnimationFrameLayout extends AutoFrameLayout {

    int mHeight;
    ValueAnimator mValueAnimator;

    public AnimationFrameLayout(Context context) {
        this(context, null);
    }

    public AnimationFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeight = getMeasuredHeight();
    }

    public void setAnimationVisibility(int visibility){
        if (View.GONE == visibility) {
            getGoneAnimation().start();
        } else if(View.VISIBLE == visibility) {
            getVisibilityAnimation().start();
        }  else {
            setVisibility(visibility);
        }
    }

    private ValueAnimator getGoneAnimation(){
        mValueAnimator = new ValueAnimator().ofFloat(1f, 0f);
        mValueAnimator.addUpdateListener(new AnimatorUpdateListener(0));
        mValueAnimator.setDuration(2000);
        return mValueAnimator;
    }

    class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        int type;

        public AnimatorUpdateListener(int type){
            this.type = type;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            setAlpha(value);

            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.topMargin = (int)  (mHeight * (value-1));
            setLayoutParams(lp);
            if(type == 0 && value == 0){
                setVisibility(View.GONE);
            } else if(type == 1 && value == 0){
                setVisibility(View.VISIBLE);
            }
        }
    }

    private  ValueAnimator getVisibilityAnimation(){
        mValueAnimator = new ValueAnimator().ofFloat(0f, 1f);
        mValueAnimator.addUpdateListener(new AnimatorUpdateListener(1));
        mValueAnimator.setDuration(2000);
        return mValueAnimator;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(mValueAnimator != null){
            mValueAnimator.end();
        }
    }
}
