package com.github.application.view.picasso;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

/**
 * Created by ZhongXiaolong on 2020/4/25 9:54.
 *
 * 根据加载的ImageView的宽度等比例压缩宽高
 */
public class FitXYTransform implements Transformation {

    private int mTargetWidth;
    private ImageView mImageView;

    public FitXYTransform(int targetWidth) {
        mTargetWidth = targetWidth;
    }

    public FitXYTransform(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    public Bitmap transform(Bitmap source) {

        int targetWidth = mTargetWidth == 0 ? mImageView != null ? mImageView.getWidth() : 0 : mTargetWidth;

        Log.d("FitXYTransform", "targetWidth:" + targetWidth);

        if (targetWidth == 0 || source.getWidth() == 0) {
            return source;
        }

        //如果图片小于设置的宽度，则返回原图
        if (source.getWidth() < targetWidth) {
            return source;
        } else {
            //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            if (targetHeight != 0) {
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    source.recycle();
                }
                return result;
            } else {
                return source;
            }
        }
    }

    @Override
    public String key() {
        return "transformation" + " desiredWidth";
    }
}
