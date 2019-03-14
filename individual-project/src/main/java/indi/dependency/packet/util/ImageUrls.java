package indi.dependency.packet.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片链接
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2017/1/24 15:58.
 */
public class ImageUrls {

    private static final String ROOT_URL = "https://raw.githubusercontent.com/NameZhongXiaolong/beauty/master/";

    /**
     * 十二张图
     */
    public static List<String> getOneFile() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 97; i < 97 + 19; i++) {
            String url = ROOT_URL + "one/image_" + (char) i + ".jpg";
            list.add(url);
        }
        return list;
    }

    /**
     * 68张图
     */
    public static List<String> getTwoFile() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 69; i++) {
            String url = ROOT_URL + "two/" + i + ".jpg";
            list.add(url);
        }
        return list;
    }

    /**
     * Picasso使用Transformation代替fit、resize
     * @param view
     * @return
     */
    public static Transformation getTransformation(final ImageView view) {
        return new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = view.getWidth();

                //返回原图
                if (source.getWidth() == 0 || source.getWidth() < targetWidth) {
                    return source;
                }

                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight == 0 || targetWidth == 0) {
                    return source;
                }
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
    }

}
