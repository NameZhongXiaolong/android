package priv.xiaolong.app.basics.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import indi.dependency.packet.custom.CardScaleHelper;
import indi.dependency.packet.custom.SpeedRecyclerView;
import indi.dependency.packet.util.ImageUrls;
import priv.xiaolong.app.R;

/**
 * 照片墙,3D滑动效果
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2017/1/23 10:32.
 */
public class PhotoWallFm extends Fragment {

    /**
     * 高斯模糊背景
     */
    private ImageView mIvBlurBg;

    /**
     * 自定义RecyclerView
     */
    private SpeedRecyclerView mSpeedRecyclerView;

    private List<String> mList = ImageUrls.getOneFile();
    private CardScaleHelper mCardScaleHelper = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_photo_wall, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvBlurBg = (ImageView) view.findViewById(R.id.blurView);
        mSpeedRecyclerView = (SpeedRecyclerView) view.findViewById(R.id.speed_recycler_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSpeedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSpeedRecyclerView.setAdapter(new PhotoWallAdp(getContext(), mList));

        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(2);
        mCardScaleHelper.attachToRecyclerView(mSpeedRecyclerView);
    }

}
