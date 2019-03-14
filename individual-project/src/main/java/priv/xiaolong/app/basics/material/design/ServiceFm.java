package priv.xiaolong.app.basics.material.design;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import indi.dependency.packet.base.BaseFragment;
import indi.dependency.packet.util.ViewUtils;
import priv.xiaolong.app.R;

/**
 * 服务
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/2 11:00.
 */
public class ServiceFm extends BaseFragment {

    @IdRes private final int MAINLAYOUT = 0XABCF101;
    private LinearLayout mMainLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        NestedScrollView scrollView = new NestedScrollView(mContext);
        scrollView.setBackgroundResource(R.color.fragmentColor);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(MAINLAYOUT);
        layout.setPadding(15, 0, 15, 0);
        View btn1 = LayoutInflater.from(mContext).inflate(R.layout.button_secondary, layout, false);
        ((TextView) btn1.findViewById(R.id.button_name)).setText("Start-Service");
        layout.addView(btn1);
        View btn2 = LayoutInflater.from(mContext).inflate(R.layout.button_secondary, layout, false);
        ((TextView) btn2.findViewById(R.id.button_name)).setText("Bind-Service");
        layout.addView(btn2);
        scrollView.addView(layout, new ScrollView.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainLayout = view.findViewById(MAINLAYOUT);
        mMainLayout.getChildAt(0).setOnClickListener(v ->
                Snackbar.make(mMainLayout, "Start-Service(待完成)", Snackbar.LENGTH_LONG).show());

        mMainLayout.getChildAt(1).setOnClickListener(v ->
                Snackbar.make(mMainLayout, "Bind-Service(待完成)", Snackbar.LENGTH_LONG).show());
    }

}
