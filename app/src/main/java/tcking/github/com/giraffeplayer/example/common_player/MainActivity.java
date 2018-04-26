package tcking.github.com.giraffeplayer.example.common_player;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import tcking.github.com.giraffeplayer.GiraffePlayer;
//import tcking.github.com.giraffeplayer.GiraffePlayerActivity;
import tcking.github.com.giraffeplayer.example.R;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 引用： https://github.com/tcking/GiraffePlayer
 * https://github.com/Bilibili/ijkplayer
 * 普通视频集成相关: MainActivity + GiraffePlayer project + ijkplayer-java project
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    GiraffePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        initView();
        initListener();

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (v.getId() == R.id.btn_play) {//
//                    String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
//                    url = "http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/07/14/649_61090e7afda541f5974c2e571b96b5a1_20_854x480.mp4";
//                    player.play(url);
//                    player.setTitle(url);
//                }

//                else if (v.getId() == R.id.btn_play_sample_1) {
//                    String url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
//                    ((EditText) findViewById(R.id.et_url)).setText(url);
//                    player.play(url);
//                    player.setTitle(url);
//                } else if (v.getId() == R.id.btn_play_sample_2) {
//                    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//                    ((EditText) findViewById(R.id.et_url)).setText(url);
//                    player.play(url);
//                    player.setTitle(url);
//                    player.setShowNavIcon(false);
//                }
//                else if (v.getId() == R.id.btn_play_sample_3) {//TODO Https
//                    String url = "https://r13---sn-o097znes.googlevideo.com/videoplayback?mt=1455852432&mv=m&ms=au&source=youtube&key=yt6&requiressl=yes&mm=31&mn=sn-o097znes&initcwndbps=16485000&id=o-AEGdeTbgSTzVGqwV2s8MjH5mlDPz3APWVwGfftr9GDqy&upn=D3A5w5WYU1k&lmt=1410665930307178&ip=2600:3c01::f03c:91ff:fe70:35ff&sparams=dur,id,initcwndbps,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,nh,pl,ratebypass,requiressl,source,upn,expire&fexp=9416126,9420452,9422596,9423341,9423661,9423662,9424038,9424862,9425077,9425730,9426472,9426698,9427379,9428544,9428649,9429218,9429237,9429435,9429589&pl=32&dur=106.370&sver=3&expire=1455874197&nh=IgpwcjAxLnNqYzA3KgkxMjcuMC4wLjE&ratebypass=yes&mime=video/mp4&itag=18&signature=22C4633FCD1259D5F6CD1E0B54AB649982895534.378BAAC5AFAAEA737246C5CE5B92212E40B765BD&ipbits=0";
//                    ((EditText) findViewById(R.id.et_url)).setText(url);
//                    player.play(url);
//                    player.setTitle(url);
//                    player.setShowNavIcon(false);
//                }
//                else if (v.getId() == R.id.btn_open) {
//                    String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
//                    GiraffePlayerActivity.configPlayer(MainActivity.this).setTitle(url).play(url);
//                    more configuration example:
//                    GiraffePlayerActivity.configPlayer(MainActivity.this)
//                            .setScaleType(GiraffePlayer.SCALETYPE_FITPARENT)
//                            .setDefaultRetryTime(5 * 1000)
//                            .setFullScreenOnly(false)
//                            .setTitle(url)
//                            .play(url);
//                }

//                else if (v.getId() == R.id.btn_start) {
//                    player.start();
//                } else if (v.getId() == R.id.btn_pause) {
//                    player.pause();
//                }

//                else if (v.getId() == R.id.btn_toggle) {
//                    player.toggleFullScreen();
//                }

//                else if (v.getId() == R.id.btn_forward) {
//                    player.forward(0.2f);
//                } else if (v.getId() == R.id.btn_back) {
//                    player.forward(-0.2f);
//                }

//                else if (v.getId() == R.id.btn_toggle_ratio) {
//                    player.toggleAspectRatio();
//                }
            }
        };

//        findViewById(R.id.btn_play).setOnClickListener(clickListener);
//        findViewById(R.id.btn_play_sample_1).setOnClickListener(clickListener);
//        findViewById(R.id.btn_play_sample_2).setOnClickListener(clickListener);
//        findViewById(R.id.btn_play_sample_3).setOnClickListener(clickListener);
//        findViewById(R.id.btn_pause).setOnClickListener(clickListener);
//        findViewById(R.id.btn_start).setOnClickListener(clickListener);
//        findViewById(R.id.btn_toggle).setOnClickListener(clickListener);
//        findViewById(R.id.btn_open).setOnClickListener(clickListener);
//        findViewById(R.id.btn_forward).setOnClickListener(clickListener);
//        findViewById(R.id.btn_back).setOnClickListener(clickListener);
//        findViewById(R.id.btn_toggle_ratio).setOnClickListener(clickListener);

//        String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
        //TODO
        //http://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv
        //http://demos.webmproject.org/exoplayer/glass_vp9_vorbis.webm
        //http://demos.webmproject.org/exoplayer/glass.mp4
        //http://vod.leasewebcdn.com/bbb.flv?ri=1024&rs=150&start=0
        //http://storage.googleapis.com/exoplayer-test-media-0/play.mp3
        //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/07/14/649_61090e7afda541f5974c2e571b96b5a1_20_854x480.mp4
        String url = "http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/07/14/649_61090e7afda541f5974c2e571b96b5a1_20_854x480.mp4";
//        String url = "rtmp://rtmp1.hongshiyun.net/live/ofqeepdd_be18b248b858477a836eae293542a6bc";//直播地址
        player.play(url);
    }

    private EditText mInputUrlEt;
    private Button mPlayVideoBt;

    private void initView() {
        mInputUrlEt = (EditText) findViewById(R.id.input_video_url);
        mPlayVideoBt = (Button) findViewById(R.id.play_video_bt);
        mPlayVideoBt.setOnClickListener(this);
    }

    private void initListener() {
        player = new GiraffePlayer(this);
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                Toast.makeText(getApplicationContext(), "video play completed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "video play error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video_bt:
                String url = mInputUrlEt.getText().toString();
                if (url.equals("") || null == url) {
                    return;
                }
                if(player != null){
                    player.pause();
                    player.play(url);
                }
                break;
            default:

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}
