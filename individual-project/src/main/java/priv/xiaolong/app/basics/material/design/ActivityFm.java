package priv.xiaolong.app.basics.material.design;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
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
 * Activity: 四种启动模式
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/2 11:00.
 */
public class ActivityFm extends BaseFragment {

    @IdRes private final int MAINLAYOUT = 0XAAA001;
    private final String desc = "启动模式要在注册表android:launchMode属性中设置";
    private final String standard = "默认的启动模式，只要激活Activity，就会创建一个新的实例，并放入任务栈中，这样任务栈中可能同时有一个Activity的多个实例";
    private final String singleTop = "激活Activity时，如果栈顶是这个Activity，就不会创建新的实例；如果栈顶不是这个Activity，则会创建新的实例。";
    private final String singleTask = "如果栈中存在Activity的实例，则将栈中该实例以上的其他Activity的实例移除，让该Activity的实例在栈顶；如果栈中不存在实例，则创建新的实例。";
    private final String singleInstance = "多个应用共享Activity的一个实例，不论是否是同一个应用，只要是激活该Activity，都重用这个实例。";
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
        scrollView.addView(layout, new ScrollView.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainLayout = view.findViewById(MAINLAYOUT);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addTitle(desc + "\n");
        addTitle("standard模式");
        addDescribe(standard);
        addTitle("singleTop模式");
        addDescribe(singleTop);
        addTitle("singleTask模式");
        addDescribe(singleTask);
        addTitle("singleInstance模式");
        addDescribe(singleInstance);
    }

    private void addTitle(String title) {
        TextView text = new TextView(mContext);
        text.setPadding(0, 12, 0, 0);
        text.setAllCaps(false);
        text.setTextSize(20);
        text.setTextColor(Color.parseColor("#71AFA4"));
        text.setText(title);
        mMainLayout.addView(text);
    }

    private void addDescribe(String title) {
        TextView text = new TextView(mContext);
        text.setPadding(5, 8, 5, 0);
        text.setAllCaps(false);
        text.setTextSize(16);
        text.setTextColor(Color.parseColor("#71AFA4"));
        text.setText(title);
        mMainLayout.addView(text);
    }
}
