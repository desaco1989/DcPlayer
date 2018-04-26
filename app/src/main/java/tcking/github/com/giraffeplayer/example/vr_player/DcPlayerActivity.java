package tcking.github.com.giraffeplayer.example.vr_player;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.asha.vrlib.model.MDPinchConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import tcking.github.com.giraffeplayer.example.R;
import tcking.github.com.giraffeplayer.example.utils.CommonUtils;
import tcking.github.com.giraffeplayer.example.vr_player.player_setting.CustomProjectionFactory;
import tcking.github.com.giraffeplayer.example.common_player.GiraffePlayer;
import tcking.github.com.giraffeplayer.example.common_player.IjkVideoView;
import tcking.github.com.giraffeplayer.example.vr_player.player_setting.MediaPlayerWrapper;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 引用： https://github.com/ashqal/MD360Player4Android
 * <p>
 * VR视频集成相关：VRPlayerActivity相关类 + vrlib project + ijkplayer-java project +　ijkplayer-armv7a
 * <p>
 * 引用： https://github.com/tcking/GiraffePlayer
 * https://github.com/Bilibili/ijkplayer
 * 普通视频集成相关: MainActivity + GiraffePlayer project + ijkplayer-java project
 */
public class DcPlayerActivity extends FragmentActivity implements View.OnClickListener {

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    private GiraffePlayer player;
    private MDVRLibrary mVRLibrary;

    private String mVideoType;
    private String mVideoUrl;
    private boolean isHorizontalScreen;
    private boolean isSingleDoubleEye;//false为双目
    boolean isPlaying;
    private boolean isStopAndReplayState;//处于视频播放完成状态

    private IjkVideoView mCommonVideoView;
    private GLSurfaceView mVrVideoGlView;

    private SeekBar mVideoSeekBar;
    private ProgressBar mProgressBar;//progress
    private ImageView mSingleDoubleEyeBt;
    private ImageView mFullHalfScreenBt;
    private ImageView mPlayPauseReplayBt;
    private RelativeLayout mVideoRootLayout;

    private LinearLayout mReplayLayout;
    private ImageView mRepalyVideoIvBt;
    private LinearLayout mNetTipsLayout;
    private TextView mRetryPlayTvBt;
    private LinearLayout mFlowTipsLayout;
    private TextView mFlowPlayTvBt;
    private RelativeLayout mVideoControlBoxLayout;


    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    static {
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_TOUCH,"TOUCH");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dc_player);

        initPublicPartView();
        boolean isLive;//是否是直播视频
        mVideoType = getIntent().getStringExtra("video_type");
        mVideoUrl = getIntent().getStringExtra("video_url");
        if (mVideoType.equals("vr_video")) {//VR视频
            initVRVideoParams();
        } else if (mVideoType.equals("live_video")) {//TODO 直播视频，会去隐藏进度条，时间显示等
            isLive = true;
            initCommonViewAndParams(isLive);
        } else {//点播视频
            isLive = false;
            initCommonViewAndParams(isLive);
        }
        InputStream inS;
        OutputStream outS;
        InputStreamReader inSR;
        OutputStreamWriter outSW;

    }

    private void initPublicPartView() {//初始化VR和普通视频的公共部分的UI
        mVideoRootLayout = (RelativeLayout) findViewById(R.id.app_video_box);
        mCommonVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVrVideoGlView = (GLSurfaceView) findViewById(R.id.gl_view);
        mSingleDoubleEyeBt = (ImageView) findViewById(R.id.single_double_eye_iv);
        //重播
        mReplayLayout = (LinearLayout) findViewById(R.id.replay_layout);
        mRepalyVideoIvBt = (ImageView) findViewById(R.id.replay_video_iv);
        mRepalyVideoIvBt.setOnClickListener(this);
        //无WIFI或手机网络提示
        mNetTipsLayout = (LinearLayout) findViewById(R.id.net_tips_layout);
        mRetryPlayTvBt = (TextView) findViewById(R.id.replay_retry_tv);
        mRetryPlayTvBt.setOnClickListener(this);
        //有网，但是第一次使用流量连接；或无网切有网，第一次使用4G或流量播放。提示用户
        mFlowTipsLayout = (LinearLayout) findViewById(R.id.flow_tips_layout);
        mFlowPlayTvBt = (TextView) findViewById(R.id.flow_play_bt_tv);
        mFlowPlayTvBt.setOnClickListener(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        params.height = CommonUtils.getDeviceScreenWidth(DcPlayerActivity.this) * 9 / 16;
        mVideoRootLayout.setLayoutParams(params);
    }

    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //--------------------------------------- 直播和点播 video start----------------------------------------------//

    private void initCommonViewAndParams(boolean isLive) {
        mVrVideoGlView.setVisibility(View.GONE);
        mCommonVideoView.setVisibility(View.VISIBLE);
        initCommonVideoParams(isLive);
    }

    private void initCommonVideoParams(boolean isLive) {
        initCommonVideoListener(isLive);
        playOrReplayOrPlayOthersLivePoint();
    }

    private void initCommonVideoListener(boolean isLive) {
        //普通的视频都在GiraffePlayer做了UI初始化的工作
        player = new GiraffePlayer(this);
        player.live(isLive);
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                Toast.makeText(getApplicationContext(), "视频播放完成了！", Toast.LENGTH_SHORT).show();
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //do something when buffering end
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //download speed
//                        ((TextView) findViewById(R.id.tv_speed)).setText(Formatter.formatFileSize(getApplicationContext(), extra) + "/s");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //do something when video rendering
//                        findViewById(R.id.tv_speed).setVisibility(View.GONE);
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {//错误码到哪里找？？
            @Override
            public void onError(int what, int extra) {
                String error = String.format("Play Error what=%d extra=%d", what, extra);
                Toast.makeText(DcPlayerActivity.this, "普通视频播放出错了，请重试！" + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void playOrReplayOrPlayOthersLivePoint() {//TODO 重播或播放其他
        if (CommonUtils.isNetworkConnected(DcPlayerActivity.this)) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.pause();
                }
                player.play(mVideoUrl);
            }
        } else {
            Toast.makeText(DcPlayerActivity.this, "未连接网络，请检查网络设置！", Toast.LENGTH_LONG).show();
        }
    }
    //-------------------------------------------- 直播和点播 video end -------------------------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //--------------------------------------- VR video  start ----------------------------------------------//
    private void initVRVideoParams() {
        initVRView();
        // init VR Library
        mVRLibrary = createVRLibrary();
        //初始化IjkMediaPlayer播放器参数
        initVRIjkMediaPlayer();
        initVRListener();
    }

    private void initVRView() {
        mProgressBar = (ProgressBar) findViewById(R.id.app_video_loading);
        mProgressBar.setVisibility(View.VISIBLE);

        mSingleDoubleEyeBt.setVisibility(View.VISIBLE);
        mVrVideoGlView.setVisibility(View.VISIBLE);
        mCommonVideoView.setVisibility(View.GONE);

        mFullHalfScreenBt = (ImageView) findViewById(R.id.app_video_fullscreen);
        mPlayPauseReplayBt = (ImageView) findViewById(R.id.app_video_play);
        mVideoSeekBar = (SeekBar) findViewById(R.id.app_video_seekBar);
        mVideoSeekBar.setMax(1000);

        mVideoControlBoxLayout = (RelativeLayout) findViewById(R.id.app_video_bottom_box);
    }


    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayerWrapper.setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(DcPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .pinchConfig(new MDPinchConfig().setMin(1.0f).setMax(8.0f).setDefaultValue(0.1f))
                .pinchEnabled(true)
                .directorFactory(new MD360DirectorFactory() {
                    @Override
                    public MD360Director createDirector(int index) {
                        return MD360Director.builder()
                                .setLookX(-2.0f)//TODO
                                .setEyeX(-2.0f)//TODO
                                .setPitch(90)
                                .build();
                    }
                })
                .projectionFactory(new CustomProjectionFactory())
                .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false).setScale(0.95f))//TODO 这里可以裁剪屏幕setScale(0.95f)，设置所占屏幕的百分比
                .build(findViewById(R.id.gl_view));
    }

    public MDVRLibrary getVRLibrary() {
        return mVRLibrary;
    }

    private void initVRIjkMediaPlayer() {
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.getPlayer().setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                getVRLibrary().onTextureResize(width, height);
            }
        });
        playVr();
    }

    Handler handler = new Handler() {//实时更新进度条的handler
        @Override
        public void handleMessage(Message msg) {
            if (mMediaPlayerWrapper.getPlayer().getDuration() == 0) {
                mVideoSeekBar.setProgress((int) (0));
            } else {
                mVideoSeekBar
                        .setProgress((int) (mMediaPlayerWrapper.getPlayer().getCurrentPosition() * 1000 / mMediaPlayerWrapper.getPlayer().getDuration()));
            }
        }

    };
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            try {
                handler.postDelayed(this, 1000);
            } catch (Exception e) {
            }
        }
    };

    private void initVRListener() {

        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {//视频准备好了
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (getVRLibrary() != null) {
                    getVRLibrary().notifyPlayerChanged();
                    isPlaying = true;
                    handler.postDelayed(runnable, 1000);
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {//TODO
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }, 600);
                }
                mPlayPauseReplayBt.setImageResource(R.mipmap.ic_stop_white_24dp);

                long totalTime = mMediaPlayerWrapper.getPlayer().getDuration();
                long minutes = totalTime/1000 / 60;
                long seconds = totalTime % 60;
                Toast.makeText(DcPlayerActivity.this, "视频时长："+minutes+"分"+ seconds+"秒", Toast.LENGTH_LONG).show();
//                mMediaPlayerWrapper.getPlayer().getCurrentPosition();

            }
        });

        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {//视频出错了
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                String error = String.format("Play Error what=%d extra=%d", what, extra);
                Toast.makeText(DcPlayerActivity.this, "VR视频播放出错了，请重试！" + error, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mMediaPlayerWrapper.getPlayer().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {//视频播放完成
            @Override
            public void onCompletion(IMediaPlayer mp) {
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }
                Toast.makeText(DcPlayerActivity.this, "VR视频播放完成了！！", Toast.LENGTH_SHORT).show();
//                mPlayPauseReplayBt.setBackgroundResource(R.mipmap.ic_stop_white_24dp);
                mPlayPauseReplayBt.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
                isStopAndReplayState = true;
            }
        });
        mMediaPlayerWrapper.getPlayer().setOnInfoListener(new IMediaPlayer.OnInfoListener() {//缓冲状态
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
//                Log.e(TAG,
//                        "Bad interleaving of media file, audio/video are not well-formed, extra is "
//                                + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        if (mProgressBar.getVisibility() == View.VISIBLE) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (mProgressBar.getVisibility() == View.GONE) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
//                Log.w(TAG, "A new set of metadata is available, extra is " + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
//                Log.e(TAG, "The stream cannot be seeked, extra is " + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
//                Log.w(TAG, "It's too complex for the decoder, extra is " + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_UNKNOWN:
//                Log.w(TAG, "Unknown info, extra is " + extra);
                        break;

                    default:
//                Log.i(TAG, "Unknown info code: " + what + ", extra is " + extra);
                        break;
                }
                return true;
            }
        });

        mPlayPauseReplayBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStopAndReplayState) {//重播
                    mPlayPauseReplayBt.setVisibility(View.GONE);
                    replayOrPlayOthersVr();
                    isStopAndReplayState = false;
                    return;
                }
                if (isPlaying) {//播放转暂停
                    if (mMediaPlayerWrapper != null) {
                        mMediaPlayerWrapper.pause();
                        isPlaying = false;
//                        mPlayPauseReplayBt.setBackgroundResource(R.mipmap.ic_play_arrow_white_24dp);
                        mPlayPauseReplayBt.setImageResource(R.mipmap.ic_play_arrow_white_24dp);

                    }
                } else {
                    mMediaPlayerWrapper.resume();
                    isPlaying = true;
//                    mPlayPauseReplayBt.setBackgroundResource(R.mipmap.ic_stop_white_24dp);
                    mPlayPauseReplayBt.setImageResource(R.mipmap.ic_stop_white_24dp);
                }
            }
        });
        findViewById(R.id.app_video_finish).setOnClickListener(new View.OnClickListener() {//顶部虚拟返回键
            @Override
            public void onClick(View v) {
                if (isHorizontalScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    isHorizontalScreen = false;
                } else {
                    finish();
                }
            }
        });
        mSingleDoubleEyeBt.setOnClickListener(new View.OnClickListener() {//单双目切换
            @Override
            public void onClick(View v) {//TODO
                if (isSingleDoubleEye) { //双目切单目 icon_single
                    mSingleDoubleEyeBt.setBackgroundResource(R.mipmap.icon_double);
                    isSingleDoubleEye = false;
                    mVRLibrary.setAntiDistortionEnabled(false);//不用裁剪屏幕， 抗失真
                    mVRLibrary.switchDisplayMode(DcPlayerActivity.this, MDVRLibrary.DISPLAY_MODE_NORMAL);
                } else { //单目切双目 icon_double
                    mSingleDoubleEyeBt.setBackgroundResource(R.mipmap.icon_single);
                    isSingleDoubleEye = true;
                    mVRLibrary.setAntiDistortionEnabled(true);//裁剪屏幕
                    mVRLibrary.switchDisplayMode(DcPlayerActivity.this, MDVRLibrary.DISPLAY_MODE_GLASS);

                }
            }
        });
        mFullHalfScreenBt.setOnClickListener(new View.OnClickListener() {//是否全屏
            @Override
            public void onClick(View v) {
                if (isHorizontalScreen) {//横屏切竖屏 ，
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    mFullHalfScreenBt.setBackgroundResource(R.drawable.icon_full);
                    mFullHalfScreenBt.setImageResource(R.mipmap.icon_full);
                    isHorizontalScreen = false;
                } else {//竖屏切横屏 icon_half
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    mFullHalfScreenBt.setBackgroundResource(R.drawable.icon_half);
                    mFullHalfScreenBt.setImageResource(R.mipmap.icon_half);
                    isHorizontalScreen = true;
                    mVRLibrary.switchInteractiveMode(DcPlayerActivity.this, 5);//手指可以滑动视频画面
                }
            }
        });


        mVideoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // userOperate判断是用户改变的滑块的值
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayerWrapper.getPlayer().seekTo(seekBar.getProgress() * mMediaPlayerWrapper.getPlayer().getDuration() / 1000);
            }
        });

        mVrVideoGlView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
            }
        });
    }

    private void replayOrPlayOthersVr() {//重播或视频Item的点击
        mMediaPlayerWrapper.pause();
        mMediaPlayerWrapper.destroy();
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.openRemoteFile(mVideoUrl);
        mMediaPlayerWrapper.prepare();
    }

    private void playVr() {
        if (CommonUtils.isNetworkConnected(DcPlayerActivity.this)) {//TODO 开始播放VR视频
            if (mVideoUrl != null) {
                mMediaPlayerWrapper.openRemoteFile(mVideoUrl);
                mMediaPlayerWrapper.prepare();
            }
        } else {
            Toast.makeText(DcPlayerActivity.this, "未连接网络，请检查网络设置！", Toast.LENGTH_LONG).show();
        }
    }

    //----------------------------------------- VR  end--------------------------------------------
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    //-------------------------------------- TODO  --------------------------------//
    @Override
    public void onConfigurationChanged(Configuration newConfig) {//横竖屏切换
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
        if (mVRLibrary != null) {
            mVRLibrary.onOrientationChanged(this);
            int mCurrentOrientation = getResources().getConfiguration().orientation;
            if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                params.width = CommonUtils.getDeviceScreenWidth(DcPlayerActivity.this);
                params.height = CommonUtils.getDeviceScreenWidth(DcPlayerActivity.this) * 9 / 16;
                mVideoRootLayout.setLayoutParams(params);
            } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                mVideoRootLayout.setLayoutParams(params);
            }
        }

    }

    @Override
    public void onClick(View v) {//TODO
        switch (v.getId()) {
            case R.id.replay_video_iv: //重播
                //检查是否有网;有网的话，当前重播按钮UI隐藏，开始播放视频；没网只是提示用户无网
                if (!CommonUtils.isNetworkConnected(this)) {
                    Toast.makeText(DcPlayerActivity.this, "未连接网络，请检查网络设置！", Toast.LENGTH_LONG).show();
                    return;
                } else {

                }
                break;
            case R.id.replay_retry_tv: //无WIFI或手机网络提示
                //检查是否有网;有网的话，当前UI隐藏，检查是否是4G；是4G的话。询问用户4G播放，否则直接在WIfi下播放；没网只是提示用户无网
                if (!CommonUtils.isNetworkConnected(this)) {
                    Toast.makeText(DcPlayerActivity.this, "未连接网络，请检查网络设置！", Toast.LENGTH_LONG).show();
                    return;
                } else {

                }
                break;
            case R.id.flow_play_bt_tv:  //有网，但是第一次使用流量连接；或无网切有网，第一次使用4G或流量播放。提示用户
                //检查是否有网;有网的话，当前UI隐藏，开始播放视频；没网只是提示用户无网
                if (!CommonUtils.isNetworkConnected(this)) {
                    Toast.makeText(DcPlayerActivity.this, "未连接网络，请检查网络设置！", Toast.LENGTH_LONG).show();
                    return;
                } else {

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVRLibrary != null) {
            mVRLibrary.onResume(this);
            mMediaPlayerWrapper.resume();
        }
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVRLibrary != null) {
            mVRLibrary.onPause(this);
            mMediaPlayerWrapper.pause();
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
        }
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVRLibrary != null) {
            mVRLibrary.onDestroy();
            mMediaPlayerWrapper.destroy();
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
        }
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mVRLibrary != null) {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                if (isHorizontalScreen) {
                    // SCREEN_ORIENTATION_PORTRAIT   SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    isHorizontalScreen = false;
                    return true;
                } else {
                    finish();
                    return true;
                }

            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mVRLibrary != null) {
            if (!isHorizontalScreen) {
                finish();
            } else {
                isHorizontalScreen = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
        if (player != null) {
            if (player.onBackPressed()) {
                return;
            }
            super.onBackPressed();
        }
    }
}