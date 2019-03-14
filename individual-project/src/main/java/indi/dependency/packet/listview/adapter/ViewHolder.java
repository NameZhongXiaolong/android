package indi.dependency.packet.listview.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ListView的ViewHolder基类
 * Created by 小龙 on 2016/8/19 9:24.
 */
public class ViewHolder {

    private final SparseArray<View> mViews;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context,View convertView,
                                 ViewGroup parent,int layoutId,int position){
        if(convertView==null) {
            return new ViewHolder(context, parent,layoutId,position);
        }
        return (ViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * @param viewId
     * @return
     */
    public <VIEW extends View> VIEW getView(int viewId){
        View view = mViews.get(viewId);
        if(view==null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (VIEW) view;
    }

    public void setText(int viewId,String text){
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public View getConvertView(){
        return mConvertView;
    }
}
