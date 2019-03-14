package priv.xiaolong.app.basics.image;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import indi.dependency.packet.custom.CardAdapterHelper;
import indi.dependency.packet.view.ZoomImageView;
import priv.xiaolong.app.R;

/**
 * Created by jameson on 8/30/16.
 */
public class PhotoWallAdp extends RecyclerView.Adapter<PhotoWallAdp.ViewHolder> {

    private List<String> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private Context mContext;

    public PhotoWallAdp(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fm_photo_wall, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Picasso.with(mContext).load(mList.get(position)).placeholder(R.mipmap.ic_ver).into(holder.mImageView);

        holder.mImageView.setOnClickListener(view -> {
            ZoomImageView v = (ZoomImageView) LayoutInflater.from(mContext).inflate(R.layout.view_image_zoom, null);
            Picasso.with(mContext).load(mList.get(position)).into(v);
            new AlertDialog.Builder(mContext).setView(v).show();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
        }

    }

}
