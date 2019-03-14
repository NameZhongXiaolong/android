package priv.xiaolong.app.basics.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * 刷新和加载更多
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/3/6 14:40.
 */
public class RecyclerRefreshLoadFm extends BaseFragment implements OnItemClickListener<String>,OnItemChildClickListener<String> {

    private final List<String> list = ImageUrls.getTwoFile();
    private List<String> mData;
    private RecyclerRefreshLoadFm.RefreshLoadAdp mAdapter;
    private int start = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @IdRes private final int RECYCLERVIEWID = 0XABCD201;
    @IdRes private final int SWIPEREFRESHLAYOUTID = 0XABCD202;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(mContext);
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        swipeRefreshLayout.setId(SWIPEREFRESHLAYOUTID);
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setBackgroundResource(R.color.fragmentColor);
        recyclerView.setPadding(0, 3, 0, 0);
        recyclerView.setLayoutParams(new SwipeRefreshLayout.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        recyclerView.setId(RECYCLERVIEWID);
        swipeRefreshLayout.addView(recyclerView);
        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(SWIPEREFRESHLAYOUTID);
        initRecyclerView((RecyclerView) view.findViewById(RECYCLERVIEWID));
    }

    /**
     * 初始化刷新
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //为SwipeRefreshLayout设置监听事件
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            start = 0;
            mSwipeRefreshLayout.setRefreshing(true);//
            new Handler().postDelayed(mRefresh, 2000);
        });
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        mData = new ArrayList<>();

        //初始化Adapter
        mAdapter = new RecyclerRefreshLoadFm.RefreshLoadAdp(getContext(), mData, true);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        //获取RecyclerView父布局
        ViewGroup parent = (ViewGroup) recyclerView.getParent();

        //设置第一次加载时的页面
        View emptyView = inflater.inflate(R.layout.recycler_empty, parent, false);
        mAdapter.setEmptyView(emptyView);

        //设置滑动到底部的脚布局
        View loadingView = inflater.inflate(R.layout.recycler_load_loading, parent, false);
        mAdapter.setLoadingView(loadingView);

        //加载更多监听(默认第一次就加载)
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                new Handler().postDelayed(mLoadmore, 2000);
            }
        });

        //设置item点击事件
        mAdapter.setOnItemClickListener(this);

        //设置图片的点击事件
        mAdapter.setOnItemChildClickListener(R.id.image, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, String data, int position) {
        Snackbar.make((View) mSwipeRefreshLayout.getParent(), "下标" + position, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemChildClick(ViewHolder viewHolder, String data, int position) {
        ZoomImageView image = (ZoomImageView) LayoutInflater.from(mContext).inflate(R.layout.view_image_zoom, null);
        Picasso.with(getContext()).load(mData.get(position)).into(image);
        new AlertDialog.Builder(getContext()).setView(image).show();
    }

    /**
     * 刷新
     */
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            mAdapter.setNewData(ImageUrls.getOneFile());
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    /**
     * 加载更多
     */
    private Runnable mLoadmore = new Runnable() {
        @Override
        public void run() {
            if (mData.size() == 0) {
                mAdapter.setNewData(ImageUrls.getOneFile());
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
            mSwipeRefreshLayout.setEnabled(true);
            //设置加载失败页面
            if (mData.size() == -11) mAdapter.setLoadFailedView(null);

            //设置无数据页面
            if (mData.size() == -22) mAdapter.setReloadView(null);
        }
    };

    /**
     * Adapter
     */
    private static class RefreshLoadAdp extends CommonBaseAdapter<String> {

        /**
         * @param context        Activity
         * @param datas          数据集合
         * @param isOpenLoadMore 是否加载更多(true为可以加载更多)
         */
        public RefreshLoadAdp(Context context, List<String> datas, boolean isOpenLoadMore) {
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
