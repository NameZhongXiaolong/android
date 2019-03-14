package com.svnchina.application.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.svnchina.application.R;
import com.svnchina.application.base.BaseSuperFragment;

/**
 * Created by ZhongXiaolong on 2017/12/13 14:47.
 * <p>
 * ListPopupWindow
 */
public class ListPopupWindowFm extends BaseSuperFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_text, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view).setText("ListPopupWindow");
        view.setOnClickListener(this::popShow);
    }

    private void popShow(View view) {
        final Context context = view.getContext();
        final TextView textView = (TextView) view;
        final ListPopupWindow pop = new ListPopupWindow(context);
        pop.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(context);
                textView.setText("item\r" + i);
                return textView;
            }
        });


        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setModal(true);
        pop.setAnchorView(view);

        pop.setOnItemClickListener((adapterView, view1, i, l) -> {
            textView.setText("item\r" + i);
            pop.dismiss();
        });
        pop.show();
    }
}
