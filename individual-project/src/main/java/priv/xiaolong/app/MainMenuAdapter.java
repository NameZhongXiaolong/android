package priv.xiaolong.app;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 13:08.
 */
public class MainMenuAdapter extends BaseAdapter {

    private ArrayList<String> mData;
    private Context mContext;
    private onItemClickListener mListener;
    private ArrayList<SlidingPaneLayout> mSlidingPane;

    public MainMenuAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        mSlidingPane = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_menu, parent, false);
        TextView mainMenu = (TextView) convertView.findViewById(R.id.tv_main_menu);
        mainMenu.setText(mData.get(position).toString());
        final SlidingPaneLayout spLayout = (SlidingPaneLayout) convertView.findViewById(R.id.sliding_pane_layout);
        spLayout.setPanelSlideListener(new SlidingPaneLayout.SimplePanelSlideListener() {

            @Override
            public void onPanelOpened(View panel) {
                mSlidingPane.add(spLayout);
            }

            @Override
            public void onPanelClosed(View panel) {
                mSlidingPane.remove(spLayout);
            }
        });

        ((FrameLayout) mainMenu.getParent()).setOnClickListener(v -> {
            if (mSlidingPane.size() > 0) {
                for (int i = 0; i < mSlidingPane.size(); i++) {
                    mSlidingPane.get(i).closePane();
                }
                mSlidingPane.clear();
            } else {
                mListener.onItemclick(index);
            }
        });

        convertView.findViewById(R.id.tv_Top).setOnClickListener(v -> mListener.onSetTop(index));

        convertView.findViewById(R.id.tv_set_home).setOnClickListener(v -> mListener.onSetHome(index));
        return convertView;
    }

    public void setOnItemClick(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener {

        void onItemclick(int position);

        void onSetTop(int position);

        void onSetHome(int position);
    }

}
