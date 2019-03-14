package priv.xiaolong.app.basics.popup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import priv.xiaolong.app.R;

/**
 * Activity弹出框
 * 继承Activity 并设置主题为@android:style/Theme.Translucent.NoTitleBar
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/1/10 22:07.
 */
public class PopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置阴影颜色
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setContentView(R.layout.pop_window);
        findViewById(R.id.ll_pop_parent).setBackgroundColor(Color.parseColor("#F9F9F9"));
        findViewById(R.id.btn_confirm).setOnClickListener(v -> finish());
    }

}
