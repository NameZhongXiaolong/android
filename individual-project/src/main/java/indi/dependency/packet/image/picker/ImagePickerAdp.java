package indi.dependency.packet.image.picker;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import priv.xiaolong.app.R;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/24 15:33.
 */
class ImagePickerAdp extends BaseAdapter {

    private ArrayList<File> mData;
    private Context mContext;
    @IdRes private final int IMAGEVIEW_ID = 0XADF091;
    @IdRes private final int CHECKBOX_ID = 0XADF092;

    public ImagePickerAdp(ArrayList<File> data, Context context) {
        mData = data;
        mContext = context;
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
        ImagePickerHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image_picker, parent,false);
            holder = new ImagePickerHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ImagePickerHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(mData.get(position)).resize(160, 160).centerCrop().into(holder.mImageView);

        return convertView;
    }

    private class ImagePickerHolder {
        public ImageView mImageView;
        public CheckBox mCheckBox;

        public ImagePickerHolder(View itemView) {
            //获取屏幕信息
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);

            //根据所设比例设置控件占屏幕的比例
            params.height = params.width;
            itemView.setLayoutParams(params);

            mImageView = (ImageView) itemView.findViewById(R.id.iv_image_picker);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_image_picker);
        }
    }

}
