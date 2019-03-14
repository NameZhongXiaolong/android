package priv.xiaolong.app.basics.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import indi.dependency.packet.base.BaseFragment;
import indi.dependency.packet.recyclerview.adapter.CommonBaseAdapter;
import indi.dependency.packet.recyclerview.adapter.OnItemChildClickListener;
import indi.dependency.packet.recyclerview.adapter.OnItemClickListener;
import indi.dependency.packet.recyclerview.adapter.OnLoadMoreListener;
import indi.dependency.packet.recyclerview.adapter.ViewHolder;
import indi.dependency.packet.util.ImageUrls;
import indi.dependency.packet.util.ViewUtils;
import indi.dependency.packet.view.ZoomImageView;
import priv.xiaolong.app.R;

/**
 * RecyclerView加载更多
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/3/1 18:02.
 */
public class RecyclerLoadMoreFm extends BaseFragment
        implements OnItemChildClickListener<String>, OnItemClickListener<String>, Runnable {

    private RecyclerView mRecyclerView;
    private final List<String> list = ImageUrls.getTwoFile();
    private List<String> mData;
    private RecyclerLoadMoreFm.LoadMoreAdp mAdapter;
    private int start = 0;

    @IdRes private final int RECYCLERVIEWID = 0XABCD101;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setBackgroundResource(R.color.fragmentColor);
        recyclerView.setPadding(0, 3, 0, 0);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        recyclerView.setId(RECYCLERVIEWID);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(RECYCLERVIEWID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mData = new ArrayList<>();

        //初始化Adapter
        mAdapter = new LoadMoreAdp(mContext, mData, true);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        //获取RecyclerView父布局
        ViewGroup parent = (ViewGroup) mRecyclerView.getParent();

        //设置第一次加载时的页面
        View emptyView = inflater.inflate(R.layout.recycler_empty,  parent, false);
        mAdapter.setEmptyView(emptyView);

        //设置滑动到底部的脚布局
        View loadingView = inflater.inflate(R.layout.recycler_load_loading,  parent, false);
        mAdapter.setLoadingView(loadingView);

        //加载更多监听(默认第一次就加载)
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                new Handler().postDelayed(RecyclerLoadMoreFm.this, 2000);
            }
        });

        //设置item点击事件
        mAdapter.setOnItemClickListener(this);

        //设置item内部控件的监听
        mAdapter.setOnItemChildClickListener(R.id.image, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 加载更多
     */
    @Override
    public void run() {
        if (mData.size() == 0) {
            mData.addAll(ImageUrls.getOneFile());
        }
        if (mData.size() < 75) {
            for (int i = 0; i < 5; i++) {
                mData.add(list.get(start));
                start++;
            }
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setLoadEndView(R.layout.recycler_load_end);
        }

        //设置加载失败页面
        if (mData.size() == -11) mAdapter.setLoadFailedView(null);

        //设置无数据页面
        if (mData.size() == -22) mAdapter.setReloadView(null);

    }

    /**
     * item内部空间的点击事件
     *
     * @param viewHolder
     * @param data
     * @param position
     */
    @Override
    public void onItemChildClick(ViewHolder viewHolder, String data, int position) {
        ZoomImageView image = (ZoomImageView) LayoutInflater.from(mContext).inflate(R.layout.view_image_zoom, null);
        Picasso.with(mContext).load(mData.get(position)).into(image);
        new AlertDialog.Builder(mContext).setView(image).show();
    }

    /**
     * item点击事件
     *
     * @param viewHolder
     * @param data
     * @param position
     */
    @Override
    public void onItemClick(ViewHolder viewHolder, String data, int position) {
        Snackbar.make((View) mRecyclerView.getParent(), "下标" + position, Snackbar.LENGTH_SHORT).show();
    }

    private static class LoadMoreAdp extends CommonBaseAdapter<String> {

        /**
         * @param context        Activity
         * @param datas          数据集合
         * @param isOpenLoadMore 是否加载更多(true为可以加载更多)
         */
        public LoadMoreAdp(Context context, List<String> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
        }

        @Override
        protected void convert(ViewHolder holder, String data) {
            holder.setText(R.id.text, "position\r" + mDatas.indexOf(data));
            ImageView imageView = holder.getView(R.id.image);
            Picasso.with(mContext).load(data).placeholder(R.mipmap.ic_hor).resize(120, 80).centerCrop().into(imageView);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.item_load_more;
        }
    }

}
