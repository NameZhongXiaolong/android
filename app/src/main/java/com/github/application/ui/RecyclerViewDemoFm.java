package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.BaseSuperFragment;
import com.github.application.view.NineGridlayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ZhongXiaolong on 2019/4/11 18:13.
 */
public class RecyclerViewDemoFm extends BaseSuperFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setId(R.id.list);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TestAdapter testAdapter = new TestAdapter();
                recyclerView.setAdapter(testAdapter);
                List<AdapterData> data = new ArrayList<>();
                data.add(new AdapterData("a", Collections.singletonList("https://raw.githubusercontent" +
                        ".com/NameZhongXiaolong/beauty/master/three/8.jpg")));
                for (int i = 0; i < 100; i++) {
                    data.add(new AdapterData("b", getUrl()));
                    data.add(new AdapterData("c", getUrl()));
                    data.add(new AdapterData("d", getUrl()));
                    data.add(new AdapterData("e", getUrl()));
                    data.add(new AdapterData("f", getUrl()));
                    data.add(new AdapterData("g", getUrl()));
                    data.add(new AdapterData("h", getUrl()));
                }
                testAdapter.addAll(data);
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }

    private String[] getUrl() {
        Random random = new Random();
        int size = random.nextInt(9);
        String[] urls = new String[size];
        for (int i = 0; i < size; i++) {
            urls[i] =
                    "https://raw.githubusercontent.com/NameZhongXiaolong/beauty/master/three/" + random.nextInt(53) + ".jpg";
        }
        return urls;
    }

    private class TestAdapter extends BaseAdapter<AdapterData> {

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return BaseHolder.instance(parent, R.layout.item_test);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            NineGridlayout nineGridlayout = holder.findViewById(R.id.nine_gridlayout);
            AdapterData adapterData = get(position);
            List<String> urls = adapterData.urls;
            nineGridlayout.setAdapter(new NineGridlayout.SimpleImageAdapter(urls));
            holder.text(R.id.text, adapterData.title);
        }
    }

    private class AdapterData {
        private String title;
        private List<String> urls;

        public AdapterData(String title, String[] urls) {
            this.title = title;
            this.urls = Arrays.asList(urls);
        }

        public AdapterData(String title, List<String> urls) {
            this.title = title;
            this.urls = urls;
        }
    }
}
