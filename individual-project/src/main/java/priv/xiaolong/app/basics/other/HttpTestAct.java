package priv.xiaolong.app.basics.other;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import indi.dependency.packet.base.BaseActivity;
import priv.xiaolong.app.R;

import static indi.dependency.packet.util.ViewUtils.BASE_VIEW_ROOT_ID;

/**
 * 网络请求测试
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:10.
 */
public class HttpTestAct extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = Color.rgb(101, 147, 74);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setId(BASE_VIEW_ROOT_ID);
        root.addView(LayoutInflater.from(this).inflate(R.layout.action_bar, root, false));
        root.setBackgroundColor(Color.rgb(225, 238, 210));

        setContentView(root);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setWindowStatusBarColor(color,toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("网络请求测试");

        getFragmentTransaction().add(BASE_VIEW_ROOT_ID, new HttpRequestFm()).commit();
    }

}
