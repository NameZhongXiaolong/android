package priv.xiaolong.app.basics.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import priv.xiaolong.app.R;

/**
 * 图片属性
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2016/12/2 16:21.
 */
public class ScaleTypeFm extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_image_scale, parent, false);
    }

}
