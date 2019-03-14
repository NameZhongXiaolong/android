package indi.dependency.packet.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * ListView BaseAdapter封装
 * Created by 小龙 on 2016/8/19 10:29.
 */
public abstract class CommonAdapter<DATA> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<DATA> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<DATA> datas, int itemLayoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDatas = datas;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public DATA getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, DATA data);

    private ViewHolder getViewHolder(int position, View converView, ViewGroup parent) {
        return ViewHolder.get(mContext, converView, parent, mItemLayoutId, position);
    }
}
