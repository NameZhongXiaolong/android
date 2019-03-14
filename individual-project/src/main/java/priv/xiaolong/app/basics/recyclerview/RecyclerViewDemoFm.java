package priv.xiaolong.app.basics.recyclerview;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import indi.dependency.packet.base.BaseFragment;
import indi.dependency.packet.util.ImageUrls;
import indi.dependency.packet.util.ViewUtils;
import indi.dependency.packet.view.ZoomImageView;
import priv.xiaolong.app.R;

/**
 * RecyclerView示例
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2017/2/8 10:52.
 */
public class RecyclerViewDemoFm extends BaseFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    @IdRes private final int RECYCLERVIEWID = 0XABCD001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setBackgroundColor(Color.parseColor("#EDEDED"));
        recyclerView.setPadding(0, 3, 0, 0);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        recyclerView.setId(RECYCLERVIEWID);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(RECYCLERVIEWID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.fragmentColor));

        //设置布局管理器
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //设置Adapter
        mRecyclerView.setAdapter(new RecyclerViewDemoAdp());

        //设置item间距
        final int space = 5;
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    outRect.top = space * 2;
                }
                if (layoutManager instanceof GridLayoutManager) {
                    outRect.top = space * 2;
                    int index = parent.getChildAdapterPosition(view);
                    if (index % 2 == 0) {
                        outRect.right = space * 2;
                    }
                }

                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    outRect.top = space * 2;
                    int index = parent.getChildAdapterPosition(view);
                    if (index % 2 == 0) {
                        outRect.right = space * 2;
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, 0, 0, "LinearLayoutManager");
        menu.add(Menu.NONE, 1, 1, "GridLayoutManager");
        menu.add(Menu.NONE, 2, 2, "StaggeredGridLayoutManager");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerView.LayoutManager manager;
        switch (item.getItemId()) {
            case 0:
                manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                break;
            case 1:
                manager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
                break;
            default:
                manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        mRecyclerView.setLayoutManager(manager);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String url = (String) v.getTag();
        ZoomImageView image = (ZoomImageView) LayoutInflater.from(mContext).inflate(R.layout.view_image_zoom, null);
        Picasso.with(mContext).load(url).into(image);
        new AlertDialog.Builder(mContext).setView(image).show();

    }

    /**
     * RecyclerView的Adapter
     */
    private class RecyclerViewDemoAdp extends RecyclerView.Adapter<AdapterHolder> {

        private List<String> mDatas = ImageUrls.getTwoFile().subList(1, 35);

        @Override
        public RecyclerViewDemoFm.AdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_recycle, parent, false);
            RecyclerViewDemoFm.AdapterHolder holder = new RecyclerViewDemoFm.AdapterHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerViewDemoFm.AdapterHolder holder, int position) {
            holder.itemView.setTag(mDatas.get(position));
            holder.itemView.setOnClickListener(RecyclerViewDemoFm.this);
            holder.mTextView.setText("item ----- " + position);
            Picasso.with(mContext).load(mDatas.get(position)).placeholder(R.mipmap.ic_ver).into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mDatas != null ? mDatas.size() : 0;
        }
    }

    /**
     * ViewHolder
     */
    private static class AdapterHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        ImageView mImageView;

        public AdapterHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text);
            mImageView = itemView.findViewById(R.id.image);
        }
    }
}
