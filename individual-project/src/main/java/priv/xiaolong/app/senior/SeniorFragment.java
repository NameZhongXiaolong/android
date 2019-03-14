package priv.xiaolong.app.senior;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import indi.dependency.packet.base.BaseFragment;
import priv.xiaolong.app.R;

/**
 * Senior
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 11:15.
 */
public class SeniorFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_senior, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
