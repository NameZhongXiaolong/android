package priv.xiaolong.app.basics.image;

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
 * 图片相关
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:05.
 */
public class _ImageAboutAct extends ListActivity {

    private KeyValue<String,Fragment> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new KeyValue<>();
        mList.put("图片画廊", new PhotoWallFm());
        mList.put("ScaleType属性", new ScaleTypeFm());
        mList.put("ViewPager无限滑动", new ViewPagerInfiniteSlideFm());
        setListAdapter(new ArrayAdapter<>(mContent, R.layout.button_main, R.id.button_name, mList.getKeys()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        addFmToStack(mList.getValue(position));
    }
}
