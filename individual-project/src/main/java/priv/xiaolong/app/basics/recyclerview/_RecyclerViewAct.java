package priv.xiaolong.app.basics.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import indi.dependency.packet.base.ListActivity;
import indi.dependency.packet.map.KeyValue;
import priv.xiaolong.app.R;

/**
 * RecyclerView
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:06.
 */
public class _RecyclerViewAct extends ListActivity {

    private KeyValue<String, Fragment> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new KeyValue<>();
        mList.put("RecyclerView示例", new RecyclerViewDemoFm());
        mList.put("RecyclerView加载更多", new RecyclerLoadMoreFm());
        mList.put("RecyclerView刷新和加载更多", new RecyclerRefreshLoadFm());
        setListAdapter(new ArrayAdapter<>(this, R.layout.button_main, R.id.button_name, mList.getKeys()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        addFmToStack(mList.getValue(position));
    }

}
