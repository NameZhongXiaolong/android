package priv.xiaolong.app.basics.material.design;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 广播测试
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2016/12/5 10:23.
 */
public class TestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
    }
}
