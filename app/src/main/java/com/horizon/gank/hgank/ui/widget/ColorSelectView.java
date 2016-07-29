package com.horizon.gank.hgank.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.horizon.gank.hgank.R;

public class ColorSelectView extends View {

    private int mColor;
    private Paint mPaint;
    private int mRadius;
    private int mBorderWidth;
    private int mInRadius;
    private boolean isSelected;

    public ColorSelectView(Context context) {
        super(context);
        init(context, null);
    }

    public ColorSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorSelectView);
        mColor = a.getColor(R.styleable.ColorSelectView_color, Color.WHITE);
        mRadius = a.getDimensionPixelSize(R.styleable.ColorSelectView_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
        mBorderWidth = a.getDimensionPixelSize(R.styleable.ColorSelectView_border_width, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics() ));
        mInRadius = a.getDimensionPixelOffset(R.styleable.ColorSelectView_in_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
    }

    public void setColor(int color){
        mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }

    public void setSelected(boolean selected){
        isSelected = selected;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 2 * mRadius  + getPaddingLeft() + getPaddingRight();
        int height = 2 * mRadius + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isSelected) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            canvas.drawCircle(getPaddingLeft() + mRadius, getPaddingTop() + mRadius, mRadius - mBorderWidth / 2, mPaint);
        }

        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getPaddingLeft() + mRadius, getPaddingTop() + mRadius, mInRadius, mPaint);
    }
}
