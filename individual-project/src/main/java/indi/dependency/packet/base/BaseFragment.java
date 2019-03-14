package indi.dependency.packet.base;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 9:56.
 */
public class BaseFragment extends Fragment {

    protected Context mContext;
    protected AppCompatActivity mCompatActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void startActivity(Intent intent) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mCompatActivity);
        ActivityCompat.startActivity(mContext,
                intent,
                optionsCompat.toBundle());
    }
}
