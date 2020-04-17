package com.github.application.base.choice.gallery;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2020/4/16 12:16.
 *
 * 扫描本地图片Service
 */
public class ScanningLocalPhotoService extends IntentService {

    public ScanningLocalPhotoService() {
        super("scanning_local_photo_service");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    protected void onHandleIntent( @Nullable Intent intent) {
        List<PhotoData> data = new ArrayList<>();
        ContentResolver contentResolver = this.getContentResolver();

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED
        };
        //只查询jpeg和png的图片
//        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " +
//                MediaStore.Images.Media.MIME_TYPE + "=? or " +
//                MediaStore.Images.Media.MIME_TYPE + "=? ";

        String selection = "_size >524288 and (mime_type = ? or mime_type = ? or mime_type =?) ";
        String[] selectionArgs = {"image/jpeg", "image/jpg", "image/png"};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc ";

        Cursor cursor = contentResolver.query(imageUri, projection, selection, selectionArgs, sortOrder);

        if (cursor == null) {
            return;
        }

        data.add(new PhotoData().setCatalog("全部").setPhotoList(new ArrayList<>()));

        while (cursor.moveToNext()) {
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            //往全部里添加
            data.get(0).getPhotoList().add(path);

            //按路径名添加,获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile != null) {
                String parentName = parentFile.getName();
                PhotoData photoData = new PhotoData().setCatalog(parentName);
                int index = data.indexOf(photoData);
                if (index > 0) {
                    data.get(index).getPhotoList().add(path);
                }else{
                    photoData.setPhotoList(new ArrayList<>(Collections.singleton(path)));
                    data.add(photoData);
                }
            }
        }

        Collections.sort(data, (o1, o2) -> Integer.compare(o2.getPhotoList().size(),o1.getPhotoList().size()));

        EventBus.getDefault().post(data);

        cursor.close();

        stopSelf();
    }

}
