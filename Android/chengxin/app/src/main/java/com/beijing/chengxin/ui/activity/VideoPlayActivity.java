package com.beijing.chengxin.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.listener.OnCallbackListener;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class VideoPlayActivity extends BaseFragmentActivity {

    public final String TAG = VideoPlayActivity.class.getName();
    private TextView txtTitle, txtContent;
//    private VideoView videoView;
//    private ImageButton btnPlay;

    private String videoUrl;

    View mBottomLayout, mTitleLayout;
    View mVideoLayout;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    boolean mIsFullscreen;
    private int mCachedHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.video_play));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        txtTitle = (TextView)findViewById(R.id.txt_title);
        txtContent = (TextView)findViewById(R.id.txt_content);
//        videoView = (VideoView)findViewById(R.id.video_view);
//        btnPlay = (ImageButton)findViewById(R.id.btn_play);
//        btnPlay.setOnClickListener(mButtonClickListener);

        txtTitle.setText(getIntent().getStringExtra("title"));
        txtContent.setText(getIntent().getStringExtra("comment"));
        videoUrl = Constants.FILE_ADDR + getIntent().getStringExtra("path");

        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoLayout = findViewById(R.id.video_layout);
        mTitleLayout = findViewById(R.id.title_layout);

        mVideoLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mCachedHeight == 0) {
                    mCachedHeight = bottom - top;
                }
            }
        });

        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);

        mVideoView.setVideoPath(videoUrl);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                btnPlay.setVisibility(View.VISIBLE);
            }
        });

        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
                mIsFullscreen = isFullscreen;
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoLayout.setLayoutParams(layoutParams);
                    //GONE the unconcerned views to leave room for video and controller
                    mBottomLayout.setVisibility(View.GONE);
                    mTitleLayout.setVisibility(View.GONE);
                } else {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = mCachedHeight;
                    mVideoLayout.setLayoutParams(layoutParams);
                    mBottomLayout.setVisibility(View.VISIBLE);
                    mTitleLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d(TAG, "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d(TAG, "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d(TAG, "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
            }

        });


//        MediaController mediaController= new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        videoView.setVideoPath(videoUrl);
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                btnPlay.setVisibility(View.VISIBLE);
//            }
//        });
//
//        videoView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (videoView.isPlaying()) {
//                    videoView.pause();
//                    btnPlay.setVisibility(View.VISIBLE);
//                }
//                return false;
//            }
//        });

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        //For 3G check
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        //For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (isWifi) {
            mVideoView.start();

        } else if (is3g) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setMessage("当前属于3G/4G网络环境，您确认要播放？")
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mVideoView.start();
                            dialog.cancel();
                        }
                    })
                    .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
//                            btnPlay.setVisibility(View.VISIBLE);
                        }

                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        setVideoAreaSize();

    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    onBackActivity();
                    break;
//                case R.id.btn_play:
//                    mVideoView.start();
//                    btnPlay.setVisibility(View.GONE);
////                    try {
////                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)));
////                    } catch (Exception e) {}
//                    break;
            }
        }
    };

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
//                int width = mVideoLayout.getWidth();
//                mCachedHeight = (int) (width * 405f / 720f);
////                cachedHeight = (int) (width * 3f / 4f);
////                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = mCachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(videoUrl);
                mVideoView.requestFocus();
            }
        });
    }
}
