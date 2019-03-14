package priv.xiaolong.app.basics.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import indi.dependency.packet.base.BaseActivity;
import priv.xiaolong.app.R;

/**
 * 测试
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:08.
 */
public class TestAct extends BaseActivity {

    @IdRes private final int LAYOUT_PARENT = 0XAAAA01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setId(LAYOUT_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(LayoutInflater.from(this).inflate(R.layout.action_bar, layout, false));
        setContentView(layout);

        setSupportActionBar(R.id.action_bar);
        setWindowThemeColor(Color.rgb(78, 29, 76));
        getSupportActionBar().setTitle("图片选择器 ");

        getFmTransaction().replace(LAYOUT_PARENT, new TestFm()).commitAllowingStateLoss();
    }

}
