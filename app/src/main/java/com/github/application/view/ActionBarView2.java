package com.github.application.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.github.application.R;

/**
 * Created by ZhongXiaolong on 2020/1/13 15:51.
 */
public class ActionBarView2 extends View {

    private int mWidth;
    private int mHeight;
    private Bitmap mNavigationBmp;
    private Paint mPaint;

    public ActionBarView2(Context context) {
        this(context,null);
    }

    public ActionBarView2(Context context,  @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ActionBarView2(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ActionBarView2(Context context,  @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        mNavigationBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_back);
//        mNavigationBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(R.drawable.ic_back);
            mNavigationBmp = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mNavigationBmp);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            mNavigationBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_back);
        }
        mPaint = new Paint();
//        mPaint.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC));
//        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //当bmp不为空,将bmp绘制在ImageView中间
        if (mNavigationBmp != null) {
            int bmpHeight = mNavigationBmp.getHeight();
            if (bmpHeight > mHeight * 0.6 || mNavigationBmp.getWidth() > mWidth * 0.6) {
                float s = (float) Math.min((mHeight * 0.6) / bmpHeight, (mWidth * 0.6) / mNavigationBmp.getWidth());
                Matrix matrix = new Matrix();
                matrix.postScale(s, s);  //长和宽放大缩小的比例
                mNavigationBmp = Bitmap.createBitmap(mNavigationBmp, 0, 0, mNavigationBmp.getWidth(), bmpHeight, matrix, true);
                bmpHeight = mNavigationBmp.getHeight();
            }
            float left = (float) ((mWidth - mNavigationBmp.getWidth()) * 0.5);
             left = 0;
            float top = (float) ((mHeight - mNavigationBmp.getHeight()) * 0.5);
            canvas.drawBitmap(mNavigationBmp, left, top, mPaint);
        }
    }
}
