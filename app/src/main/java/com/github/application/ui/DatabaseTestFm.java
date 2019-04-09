package com.github.application.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.BaseSuperFragment;
import com.github.application.main.Constants;
import com.github.application.main.MainApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ZhongXiaolong on 2019/4/9 14:18.
 */
public class DatabaseTestFm extends BaseSuperFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        LinearLayout parent = new LinearLayout(getContext());
        Button button1 = new Button(getContext());
        button1.setText("存");
        button1.setId(R.id.button);
        Button button2 = new Button(getContext());
        button2.setText("读取");
        button2.setId(R.id.btn_confirm);
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setId(R.id.list);

        parent.setOrientation(LinearLayout.VERTICAL);
        parent.addView(button1, Constants.WRAP_CONTENT, Constants.WRAP_CONTENT);
        parent.addView(button2, Constants.WRAP_CONTENT, Constants.WRAP_CONTENT);
        parent.addView(recyclerView, Constants.MATCH_PARENT, Constants.MATCH_PARENT);
        return parent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EducationDbHelper helper = new EducationDbHelper(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final BaseAdapter<Education> adapter = new BaseAdapter<Education>() {
            @NonNull
            @Override
            public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseHolder.instance(parent, R.layout.item_menu_1);
            }

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final Education education = get(position);
                holder.text(R.id.text, format.format(education.saveTimeMillis));
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.delete(education.id);
                        remove(education);
                    }
                });

                holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Education data = new Education(new Random().nextInt(1000), System.currentTimeMillis());
                        helper.update(education.id,data);
                        set(position, data);
                        return true;
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        //存
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long insert = helper.insert(new Education(new Random().nextInt(1000), System.currentTimeMillis()));
                MainApplication.outToast("添加第" + insert + "条成功");
            }
        });

        //取
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                List<Education> list = helper.query();
                adapter.addAll(list);
            }
        });
    }

    class Education {
        long id;
        long serverId;
        long saveTimeMillis;

        Education(long serverId, long saveTimeMillis) {
            this.serverId = serverId;
            this.saveTimeMillis = saveTimeMillis;
        }

        Education(long id, long serverId, long saveTimeMillis) {
            this.id = id;
            this.serverId = serverId;
            this.saveTimeMillis = saveTimeMillis;
        }

        @Override
        public String toString() {
            return "Education{" +
                    "id=" + id +
                    ", serverId=" + serverId +
                    ", saveTimeMillis=" + saveTimeMillis +
                    '}';
        }
    }

    class EducationDbHelper extends SQLiteOpenHelper {
        EducationDbHelper(Context context) {
            super(context, "education.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE Education( id BIGINT PRIMARY KEY AUTOINCREMENT, serverId BIGINT,saveTimeMillis " +
                    "BIGINT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private static final String TABLE = "Education";

        long insert(Education data) {
            if (data == null) return -1;
            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("serverId", data.serverId);
            values.put("saveTimeMillis", data.saveTimeMillis);

            long insert = database.insert(TABLE, null, values);

            database.close();
            return insert;
        }

        List<Education> query() {
            List<Education> list = new ArrayList<>();
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from Education", null);
            while (cursor.moveToNext()) {
                Education education = new Education(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2));
                list.add(education);
            }
            cursor.close();
            database.close();
            return list;
        }

        int delete(long id) {
            SQLiteDatabase database = getWritableDatabase();
            int delete = database.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
            database.close();
            return delete;
        }

        void update(long id,Education data) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("serverId", data.serverId);
            values.put("saveTimeMillis", data.saveTimeMillis);
            database.update(TABLE, values, "id=?", new String[]{String.valueOf(id)});
        }
    }
}
