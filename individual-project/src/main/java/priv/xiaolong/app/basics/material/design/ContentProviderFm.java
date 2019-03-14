package priv.xiaolong.app.basics.material.design;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import indi.dependency.packet.base.BaseFragment;
import indi.dependency.packet.util.ViewUtils;
import priv.xiaolong.app.R;

/**
 * 内容提供者
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/2 11:00.
 */
public class ContentProviderFm extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        NestedScrollView scrollView = new NestedScrollView(mContext);
        scrollView.setBackgroundResource(R.color.fragmentColor);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        TextView text = new TextView(mContext);
        text.setText("内容提供者");
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.WHITE);
        scrollView.addView(text, new ScrollView.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
