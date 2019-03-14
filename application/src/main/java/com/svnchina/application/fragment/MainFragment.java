package com.svnchina.application.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.svnchina.application.R;
import com.svnchina.application.activity.ContainerActivity;
import com.svnchina.application.activity.TestActivity;
import com.svnchina.application.activity.UnitConversionActivity;
import com.svnchina.application.base.BaseRecyclerAdapter;
import com.svnchina.application.base.MainApplication;
import com.svnchina.application.base.RecyclerViewFragment;
import com.svnchina.application.base.RecyclerViewHolder;

import java.util.Arrays;

/**
 * Created by ZhongXiaolong on 2018/5/25 10:00.
 */
public class MainFragment extends RecyclerViewFragment implements BaseRecyclerAdapter.OnItemClickListener,
        BaseRecyclerAdapter.BindViewHolder<MainFragment.Index> {

    enum Index {
        LoginTest("登录",LoginFm.class),
        DeviceInfo("设备信息", PhoneInfoFm.class),
        dp2px("单位转换 : dp2px", UnitConversionActivity.dp2px(MainApplication.getContext())),
        px2dp("单位转换 : px2dp", UnitConversionActivity.px2dp(MainApplication.getContext())),
        sp2px("单位转换 : sp2px", UnitConversionActivity.sp2px(MainApplication.getContext())),
        px2sp("单位转换 : px2sp", UnitConversionActivity.px2sp(MainApplication.getContext())),
        Note("笔记", NoteFm.class),
        ListPopupWindow("ListPopupWindow", ListPopupWindowFm.class),
        VideoPlayer("视频播放器", VideoPlayerFm.class),
        test1("测试Activity", new Intent(MainApplication.getContext(), TestActivity.class)),

        test11("测试", TestFm.class),
        test12("测试", TestFm.class),
        test13("测试", TestFm.class),
        test14("测试", TestFm.class),
        test15("测试", TestFm.class),
        test16("测试", TestFm.class),
        test17("测试", TestFm.class),
        ;
        String title;
        String fragmentCanonicalName;
        Intent activityStarter;

        Index(String title, Class<? extends Fragment> fragment) {
            this.title = title;
            this.fragmentCanonicalName = fragment.getCanonicalName();
            this.activityStarter = null;
        }

        Index(String title, Intent activityStarter) {
            this.title = title;
            this.activityStarter = activityStarter;
            this.fragmentCanonicalName = null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseRecyclerAdapter<Index> adapter = new BaseRecyclerAdapter<>(R.layout.simple_text, this);
        adapter.setOnItemClickListener(this).addAllItem(Arrays.asList(Index.values()));
        getRecyclerView().setAdapter(adapter);
    }

    @Override
    public void onItemClick(RecyclerViewHolder holder, int position) {
        Index index = Index.values()[position];
        Intent starter = index.activityStarter;
        String fm = index.fragmentCanonicalName;

        if (starter != null) startActivity(starter);
        if (fm != null) ContainerActivity.start(getContext(), fm, index.title);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, Index model, int position) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.getContentView().getLayoutParams();

        int bottom = 0;
        int top = 0;
        int margin = dp2px(10);

        if (position == Index.values().length - 1) {
            bottom = margin;
            if (hasNavigationBar()) bottom += getResources().getDimension(R.dimen.navigationBarSize);
        }

        if (position == 0) {
            Resources r = getResources();
            top = (int) (r.getDimension(R.dimen.statusBarSize) + r.getDimension(R.dimen.actionBarSize));
        }

        lp.setMargins(margin, top + margin, margin, bottom);
        holder.getContentView().setBackgroundColor(Color.parseColor("#90FFFFFF"));
        holder.text(R.id.text, model.title).textSizeId(R.id.text, 18);
    }

}
