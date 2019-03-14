package com.svnchina.application.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.svnchina.application.R;
import com.svnchina.application.activity.VideoPlayerActivity;
import com.svnchina.application.base.BaseRecyclerAdapter;
import com.svnchina.application.base.RecyclerViewFragment;
import com.svnchina.application.base.RecyclerViewHolder;
import com.svnchina.application.view.VerticalDivider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2018/2/12 10:38.
 * <p>
 * 视频播放器
 */
public class VideoPlayerFm extends RecyclerViewFragment {

    private final static String KEY_PATH = "abc";
    private final static String KEY_DISPLAY_NAME = "cba";
    private BaseRecyclerAdapter<VideoInfo> mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("正在读取视频");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        mAdapter = new BaseRecyclerAdapter<>(R.layout.simple_text, (h, m, i) -> h.text(R.id.text, m.displayName));
        addItemDecoration(new VerticalDivider(getContext(), Color.parseColor("#EDEDED"), 1));
        mAdapter.setOnItemClickListener(this::onVideoItemClick);
        setAdapter(mAdapter);

        getVideo();
    }

    @SuppressLint("StaticFieldLeak")
    private void getVideo() {
        new AsyncTask<Void, Integer, List<VideoInfo>>() {
            @Override
            protected List<VideoInfo> doInBackground(Void... voids) {
                List<VideoInfo> videoInfos = new ArrayList<>();
                getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
                videoInfos = filterVideo(videoInfos);
                return videoInfos;
            }

            @Override
            protected void onPostExecute(List<VideoInfo> videoInfos) {
                super.onPostExecute(videoInfos);
                mAdapter.addAllItem(videoInfos);
                mProgressDialog.dismiss();
            }

        }.execute();
    }

    private void onVideoItemClick(RecyclerViewHolder holder, int position) {
        VideoPlayerActivity.start(getContext(), mAdapter.getItem(position).path);
    }

    private List<VideoInfo> getVideoFile(final List<VideoInfo> list, File file) {

        file.listFiles(file1 -> {

            String name = file1.getName();

            int i = name.indexOf('.');
            if (i != -1) {
                name = name.substring(i);
                if (name.equalsIgnoreCase(".mp4")
                        || name.equalsIgnoreCase(".3gp")
                        || name.equalsIgnoreCase(".wmv")
                        || name.equalsIgnoreCase(".ts")
                        || name.equalsIgnoreCase(".rmvb")
                        || name.equalsIgnoreCase(".mov")
                        || name.equalsIgnoreCase(".m4v")
                        || name.equalsIgnoreCase(".avi")
                        || name.equalsIgnoreCase(".m3u8")
                        || name.equalsIgnoreCase(".3gpp")
                        || name.equalsIgnoreCase(".3gpp2")
                        || name.equalsIgnoreCase(".mkv")
                        || name.equalsIgnoreCase(".flv")
                        || name.equalsIgnoreCase(".divx")
                        || name.equalsIgnoreCase(".f4v")
                        || name.equalsIgnoreCase(".rm")
                        || name.equalsIgnoreCase(".asf")
                        || name.equalsIgnoreCase(".ram")
                        || name.equalsIgnoreCase(".mpg")
                        || name.equalsIgnoreCase(".v8")
                        || name.equalsIgnoreCase(".swf")
                        || name.equalsIgnoreCase(".m2v")
                        || name.equalsIgnoreCase(".asx")
                        || name.equalsIgnoreCase(".ra")
                        || name.equalsIgnoreCase(".ndivx")
                        || name.equalsIgnoreCase(".xvid")) {
                    VideoInfo video = new VideoInfo();
                    file1.getUsableSpace();
                    video.displayName = file1.getName();
                    video.path = file1.getAbsolutePath();
                    list.add(video);
                    return true;
                }
                //判断是不是目录
            } else if (file1.isDirectory()) {
                getVideoFile(list, file1);
            }
            return false;
        });

        return list;
    }

    /**
     * 10M=10485760 b,小于10m的过滤掉
     * 过滤视频文件
     *
     * @param videoInfos
     *
     * @return
     */
    private List<VideoInfo> filterVideo(List<VideoInfo> videoInfos) {
        List<VideoInfo> newVideos = new ArrayList<>();
        for (VideoInfo videoInfo : videoInfos) {
            File f = new File(videoInfo.path);
            if (f.exists() && f.isFile() && f.length() > 10485760 / 2) {
                newVideos.add(videoInfo);
                Log.i("TGA", "文件大小" + f.length());
            } else {
                Log.i("TGA", "文件太小或者不存在");
            }
        }
        return newVideos;
    }

    private static class VideoInfo {
        String displayName;
        String path;
    }
}
