package com.github.application.main;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

/**
 * Created by ZhongXiaolong on 2019/12/25 11:54.
 */
public class DatabaseHelper {

    public static SQLiteDatabase getInstance() {
        String path = MainApplication.getFileRoot() + "/database";
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                path += "/app.db";
            } else {
                path = MainApplication.getFileRoot() + "/app.db";
            }
        } else {
            path += "/app.db";
        }
        Log.d("DatabaseHelper", path);

        return SQLiteDatabase.openOrCreateDatabase(path, null);
    }

}
