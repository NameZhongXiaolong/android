package com.github.application.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseSuperActivity;

public class MainActivity extends BaseSuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MenuAdapter());
    }

    public class MenuAdapter extends RecyclerView.Adapter<MenuHolder>{

        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new MenuHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_1,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    public class MenuHolder extends RecyclerView.ViewHolder{

        public MenuHolder(View itemView) {
            super(itemView);
        }
    }
}
