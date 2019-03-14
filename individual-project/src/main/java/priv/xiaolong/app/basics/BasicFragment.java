package priv.xiaolong.app.basics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;

import indi.dependency.packet.base.ListActivity;
import indi.dependency.packet.base.ListFragment;
import indi.dependency.packet.map.KeyValue;
import priv.xiaolong.app.MainMenuAdapter;
import priv.xiaolong.app.basics.image._ImageAboutAct;
import priv.xiaolong.app.basics.material.design._MaterialDesignAct;
import priv.xiaolong.app.basics.other.Dp2PxAct;
import priv.xiaolong.app.basics.other.HttpTestAct;
import priv.xiaolong.app.basics.popup._PopupAct;
import priv.xiaolong.app.basics.recyclerview._RecyclerViewAct;
import priv.xiaolong.app.basics.test.TestAct;

/**
 * Basic
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 9:59.
 */
public class BasicFragment extends ListFragment implements MainMenuAdapter.onItemClickListener {

    private static KeyValue<String, Class<? extends AppCompatActivity>> MENU_DATA = new KeyValue<>();

    static {
        MENU_DATA.put("material design + 四大组件", _MaterialDesignAct.class);
        MENU_DATA.put("弹出框", _PopupAct.class);
        MENU_DATA.put("RecyclerView", _RecyclerViewAct.class);
        MENU_DATA.put("图片相关", _ImageAboutAct.class);
        MENU_DATA.put("dx与px互转", Dp2PxAct.class);
        MENU_DATA.put("网络请求测试", HttpTestAct.class);
        MENU_DATA.put("测试", TestAct.class);
    }

    private final String SHAREDPREF_NAME = this.getClass().getName();
    private final String DEF_ACTIVITY = "home_activity";
    private final String TOP_KEY = "SET_TOP";
    private ArrayList<String> mData;
    private MainMenuAdapter mAdapter;
    private SharedPreferences mSp;

    //设置是否返回主页设置
    private long mStartTime;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.WHITE);
        getListView().setPadding(0, 0, 0, 0);
        getListView().setDivider(new ColorDrawable(Color.parseColor("#EDEDED")));
        getListView().setDividerHeight(3);
        mSp = getActivity().getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE);
        String defActivity = mSp.getString(DEF_ACTIVITY, null);
        if (!TextUtils.isEmpty(defActivity)) {
            try {
                Class<? extends Activity> activityClass = (Class<? extends Activity>) Class.forName(defActivity);
                startActivityForResult(new Intent(getActivity(), activityClass), 0);
                getView().setVisibility(View.INVISIBLE);
                mStartTime = System.currentTimeMillis();
            } catch (ClassNotFoundException e) {
                onCreateItemView();
            }
        } else {
            onCreateItemView();
        }
    }

    /**
     * 初始化Item
     */
    private void onCreateItemView() {
        getView().setVisibility(View.VISIBLE);
        mSp.edit().putString(DEF_ACTIVITY, null).commit();
        mData = new ArrayList<>();
        mData.addAll(MENU_DATA.getKeys());

        //获取顶项key
        String topkey = mSp.getString(TOP_KEY, null);
        if (!TextUtils.isEmpty(topkey)) {
            int index = mData.indexOf(topkey);
            if (index != -1) mData.remove(index);
            mData.add(0, topkey);
        }

        mAdapter = new MainMenuAdapter(getContext(), mData);
        mAdapter.setOnItemClick(this);
        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        long time = System.currentTimeMillis() - mStartTime;

        if (requestCode != 0 || time > 6000 || time < 2000) {
            getActivity().finish();
            return;
        }

        new AlertDialog.Builder(mContext)
                .setTitle("Prompt!")
                .setMessage("Do you want to quit the application?")
                .setCancelable(false)
                .setOnKeyListener((dialog, keyCode, evet) -> onDialogKeyEvent(keyCode))
                .setPositiveButton("NO", (dialog, which) -> onCreateItemView())
                .setNegativeButton("YES", (dialog, which) -> getActivity().finish())
                .show();
    }

    /**
     * 进入对应详情
     *
     * @param position
     */
    @Override
    public void onItemclick(int position) {
        String key = mData.get(position);
        Class<? extends AppCompatActivity> value = MENU_DATA.getValue(key);
        Intent starter = new Intent(getActivity(), value);
        starter.putExtra(ListActivity.KEY_ACTIVITY_TITLE, key);
        startActivity(starter);
    }

    /**
     * 置顶
     */
    @Override
    public void onSetTop(int index) {
        String key = mData.get(index);
        mData.remove(index);
        mData.add(0, key);
        mSp.edit().putString(TOP_KEY, key).commit();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置为首页
     *
     * @param position
     */
    @Override
    public void onSetHome(int position) {
        Class<? extends AppCompatActivity> value = MENU_DATA.getValue(mData.get(position));
        mSp.edit().putString(DEF_ACTIVITY, value.getCanonicalName()).commit();
        startActivity(new Intent(getActivity(), value));
        getActivity().finish();
    }

    //Dialog返回键重写
    private boolean onDialogKeyEvent(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

}