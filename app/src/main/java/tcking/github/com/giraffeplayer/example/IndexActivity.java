package tcking.github.com.giraffeplayer.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tcking.github.com.giraffeplayer.example.common_player.MainActivity;
import tcking.github.com.giraffeplayer.example.vr250_example.DemoActivity;
import tcking.github.com.giraffeplayer.example.vr_player.DcPlayerActivity;

/**
 * Created by desaco on 2017/8/23.
 */
public class IndexActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
    }

    private void initView() {
        Button commonVideoBt = (Button) findViewById(R.id.common_video_bt);
        commonVideoBt.setOnClickListener(this);
        Button vrVideoBt = (Button) findViewById(R.id.vr_video_bt);
        vrVideoBt.setOnClickListener(this);
        //
        Button vr250Bt = (Button) findViewById(R.id.jump_vr250);
        vr250Bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_video_bt://普通视频
                //http://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv
                //http://demos.webmproject.org/exoplayer/glass_vp9_vorbis.webm
                //http://demos.webmproject.org/exoplayer/glass.mp4
                //http://vod.leasewebcdn.com/bbb.flv?ri=1024&rs=150&start=0
                //http://storage.googleapis.com/exoplayer-test-media-0/play.mp3
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/09/30/649_4830ed2a98e6427696099d9d3410311f_20_854x480.mp4
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/10/11/649_ff96ab30af2a438e8d55897e2e9808f4_20_854x480.mp4

                //http://cache.utovr.com/275fb5545211474b9109b30abac9bc2b/L2_aoo1oouobumjfdfw.m3u8
                //http://cache.utovr.com/275fb5545211474b9109b30abac9bc2b/L2_aoo1oouobumjfdfw.m3u8

//                String url = "rtmp://rtmp1.hongshiyun.net/live/ofqeepdd_be18b248b858477a836eae293542a6bc";//直播地址
                //rtmp://rtmp1.hongshiyun.net/live/ofqeepdd_25e0b9d132fc48d5a24792c5aa48c8a2
                String commonVideo = "common_video";
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/10/11/649_ff96ab30af2a438e8d55897e2e9808f4_20_854x480.mp4
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/10/12/649_963157011ce54b5283811327a04aa46f_20_854x480.mp4
                //rtmp://rtmp1.hongshiyun.net/live/ofqeepdd_d9ef8eb727754c3585b9d53955c93d34
                String commonUrl = "http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/10/12/649_963157011ce54b5283811327a04aa46f_20_854x480.mp4";
                jump2GoalActivity(commonVideo, commonUrl);

//                Intent intent = new Intent();
//                intent.setClass(IndexActivity.this,MainActivity.class);
//                startActivity(intent);
                break;

            case R.id.vr_video_bt://VR
//                http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/16/649_85f5d005ba3a47d78f18d34254579573_20_854x480.mp4
//                http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/09/649_7845895162e644268346d02ca2c29f80_20_854x480.mp4
//                http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/01/649_afafa5246a29484e8c55ac40cfd1ff7e_20_854x480.mp4、
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/07/13/649_85e48adacac74989b9fe5bcd4c9f294f_20_854x480.mp4
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/09/649_7845895162e644268346d02ca2c29f80_20_854x480.mp4

                //rtmp://rtmp1.hongshiyun.net/live/ofqeepdd_25e0b9d132fc48d5a24792c5aa48c8a2
                String vrVideo = "vr_video";
                //rtmp://rtmp1.hongshiyun.net/live/ofqeepdd_25e0b9d132fc48d5a24792c5aa48c8a2

//                http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/16/649_85f5d005ba3a47d78f18d34254579573_20_854x480.mp4
//                http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/09/649_7845895162e644268346d02ca2c29f80_20_854x480.mp4
//                http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/01/649_afafa5246a29484e8c55ac40cfd1ff7e_20_854x480.mp4

                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/08/10/649_422a7343a707421898aeb6ef829a8d80_20_854x480.mp4   4:10分钟
                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/07/19/649_dd578f98b58d4b17995e1c20fee72251_20_854x480.mp4   4:01

                //http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/08/10/649_422a7343a707421898aeb6ef829a8d80_20_854x480.mp4
                String vrUrl = "http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/08/10/649_422a7343a707421898aeb6ef829a8d80_20_854x480.mp4";
                jump2GoalActivity(vrVideo, vrUrl);

//                String url = "http://ofqeepdd.vod2.hongshiyun.net/target/mp4/2017/03/09/649_7845895162e644268346d02ca2c29f80_20_854x480.mp4";
//                Intent vtIntent =new Intent();
//                vtIntent.setData(Uri.parse(url));
//                startActivity(vtIntent);
                break;
            case R.id.jump_vr250:
                Intent videoIntent = new Intent();
                videoIntent.setClass(IndexActivity.this, DemoActivity.class);
                startActivity(videoIntent);
                break;
            default:
                Intent intent = new Intent();
                intent.setClass(IndexActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void jump2GoalActivity(String videoType, String videoUrl) {
        Intent videoIntent = new Intent();
        videoIntent.setClass(IndexActivity.this, DcPlayerActivity.class);
        videoIntent.putExtra("video_type",videoType);
        videoIntent.putExtra("video_url",videoUrl);
        startActivity(videoIntent);
    }
}
