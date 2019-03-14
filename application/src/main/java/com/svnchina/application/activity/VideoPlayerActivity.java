package com.svnchina.application.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.svnchina.application.R;
import com.svnchina.application.base.SideslipSignOutActivity;

/**
 * Created by ZhongXiaolong on 2018/2/12 14:44.
 * <p>
 * 视频播放器
 */
public class VideoPlayerActivity extends SideslipSignOutActivity {

    private VideoView mVideoView;

    public static void start(Context context, String videoPath) {
        Intent starter = new Intent(context, VideoPlayerActivity.class);
        starter.putExtra(KEY_STRING, videoPath);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_video_player);

        final String videoPath = getIntent().getStringExtra(KEY_STRING);

        mVideoView = findViewById(R.id.video_view);
        mVideoView.setMediaController(new MediaController(this));//设置VedioView与MediaController相关联
        Uri rawUri = Uri.parse(videoPath);
        mVideoView.setVideoURI(rawUri);
        mVideoView.start();

    }

//    @Override
//    public void onBackPressed() {
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
//            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            mVideoView.setLayoutParams(lp);
//        } else {
//            super.onBackPressed();
//        }
//    }
}
