package priv.xiaolong.app.basics.material.design;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
 * 广播接收者 : 动态广播&静态广播
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/2 11:00.
 */
public class BroadcastReceiversFm extends BaseFragment {

    @IdRes private final int MAINLAYOUT = 0XABCF001;
    public static final String ACTION_STATIC_RECEIVER = "ACTION_STATIC_RECEIVER";
    public static final String ACTION_DYNAMIC_RECEIVER = "ACTION_DYNAMIC_RECEIVER";
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
        ((TextView) btn1.findViewById(R.id.button_name)).setText("发送动态广播");
        layout.addView(btn1);
        View btn2 = LayoutInflater.from(mContext).inflate(R.layout.button_secondary, layout, false);
        ((TextView) btn2.findViewById(R.id.button_name)).setText("发送静态广播");
        layout.addView(btn2);
        scrollView.addView(layout, new ScrollView.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainLayout = view.findViewById(MAINLAYOUT);
        //发送静态广播(优先级比静态广播高)
        mMainLayout.getChildAt(0).setOnClickListener(v -> dynamicBroadcast());

        //发送静态广播
        mMainLayout.getChildAt(1).setOnClickListener(view1 -> staticBroadcast());
    }

    /** 动态广播 */
    private void dynamicBroadcast() {
        Intent intent;

        //实例化广播
        BroadcastReceiversFm.DynamicReceiver receiver = new BroadcastReceiversFm.DynamicReceiver();

        //获取注册器
        IntentFilter filter = new IntentFilter();

        //添加Action
        filter.addAction(ACTION_DYNAMIC_RECEIVER);

        //注册广播
        mContext.registerReceiver(receiver, filter);

        //设置Action
        intent = new Intent(ACTION_DYNAMIC_RECEIVER);

        //传递数据
        intent.putExtra("key", "动态注册的广播");

        //发送广播
        mContext.sendBroadcast(intent);

        //注销广播
        //mContext.unregisterReceiver(receiver);
    }

    /** 静态广播 */
    private void staticBroadcast() {
        Intent intent;

        //设置Action
        intent = new Intent(ACTION_STATIC_RECEIVER);

        //传递数据
        intent.putExtra("name", "静态注册的广播");

        //发送广播
        mContext.sendBroadcast(intent);
    }

    /**
     * 广播接收者(动态注册的)
     */
    private class DynamicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("key");
            Snackbar.make(mMainLayout, value, Snackbar.LENGTH_LONG).show();

            //注销广播
            mContext.unregisterReceiver(this);
        }
    }
}
