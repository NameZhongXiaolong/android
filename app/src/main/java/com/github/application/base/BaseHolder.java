package com.github.application.base;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ZhongXiaolong on 2019/3/11 11:04.
 */
public class BaseHolder extends RecyclerView.ViewHolder{

    private BaseHolder(View itemView) {
        super(itemView);
    }

    public static BaseHolder instance(View itemView){
        return new BaseHolder(itemView);
    }

    public static BaseHolder instance(ViewGroup parent, @LayoutRes int itemView){
        View view = LayoutInflater.from(parent.getContext()).inflate(itemView, parent, false);
        return new BaseHolder(view);
    }

    public View getItemView(){
        return itemView;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    public TextView text(@IdRes int id, CharSequence text) {
        View viewById = findViewById(id);
        if (viewById instanceof TextView) {
            TextView textView = (TextView) viewById;
            if (!TextUtils.isEmpty(text)) {
                textView.setText(text);
            }
            return textView;
        } else {
            throw new ClassCastException("View most is TextView");
        }
    }

    public void setOnClickListener(final OnClickListener l){
        getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onClick(v, getLayoutPosition());
            }
        });
    }

    public interface  OnClickListener{
        void onClick(View item,int position);
    }
}
