package com.horizon.gank.hgank.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.util.ThemeUtils;

public class FlashBackGroundTextView extends TextView {

	private Paint mPaint;
	private LinearGradient mLinearGradient = null;
	private int mViewWidth;
	private Matrix mGradientMatrix = null;
	private int mTranslateSpeed;
	private boolean mAnimating = true;

	public FlashBackGroundTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlashBackGroundTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlashBackGroundTextView(Context context) {
		this(context, null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mViewWidth == 0) {
			mViewWidth = getMeasuredWidth();
			if (mViewWidth > 0) {
				mPaint = getPaint();
				mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0,
						new int[] {Color.WHITE, ThemeUtils.getThemeColor(getContext(), R.attr.colorPrimary), Color.WHITE}, null,
						Shader.TileMode.CLAMP);
				mPaint.setShader(mLinearGradient);
				mGradientMatrix = new Matrix();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mAnimating && mGradientMatrix != null) {
			mTranslateSpeed += mViewWidth/10;
            if(mTranslateSpeed > 2*mViewWidth){
                mTranslateSpeed = -mViewWidth;
            }
			mGradientMatrix.setTranslate(mTranslateSpeed, 0);
			mLinearGradient.setLocalMatrix(mGradientMatrix);
			
			postInvalidateDelayed(20);
		}
		
		super.onDraw(canvas);
	}


	public void setmAnimating(boolean mAnimating) {
		this.mAnimating = mAnimating;
	}
}
