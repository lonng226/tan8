package lonng.com.tan8;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2015/12/21.
 */
public class VideoPlayActivity  extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener
        ,MediaPlayer.OnInfoListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener,View.OnClickListener{

    @Bind(R.id.seekbar)
    SeekBar seekBar;
    @Bind(R.id.start_stop)
    Button start_stop;
    @Bind(R.id.surfaceview)
    SurfaceView surfaceView;
    @Bind(R.id.videoplay_back)
    TextView videoplay_back;
    @Bind(R.id.timetv)
    TextView timetv;
    MediaPlayer mediaPlayer;
    @Bind(R.id.curtimetv)
    TextView curtimetv;
    int postion;
    Display currDisplay;
    int vWidth,vHeight,mSurfaceViewHeight,mSurfaceViewWidth;
    int currPosition;
    boolean isPlaying;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        ButterKnife.bind(this);

        Log.i("tan8","onCreate");
        initView();

    }


    protected void initView() {

        videoplay_back.setOnClickListener(this);

//        surfaceView.getHolder().setFixedSize(200, 200);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new SurfaceCallback());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);

        String filepath = getIntent().getStringExtra("filepath");
        try{
//            Cache cache = new FileCache(new File(getExternalCacheDir(), VIDEO_CACHE_NAME));
//              filepath = "http:120.24.16.24/tanqin/uploads/%E6%95%99%E5%AD%A6/test/VID_20160415_202230.mp4";
            Log.i("tan8","initView--filepath==="+filepath);
            if(filepath.contains("http")){
                Log.i("tan8","filepath.contains(\"http\")");
//                filepath=URLEncoder.encode(filepath, "utf-8");
                mediaPlayer.setDataSource(filepath);
            }else {
                mediaPlayer.setDataSource(CommonUtils.GET_FILS+filepath);
            }

//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.getDuration();

        } catch (Exception e){
            e.printStackTrace();
        }

        currDisplay = this.getWindowManager().getDefaultDisplay();
        mSurfaceViewHeight = currDisplay.getHeight();
        mSurfaceViewWidth = currDisplay.getWidth();




        start_stop.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(change);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_stop){
            startOrStop();
        }else if(v.getId() ==R.id.videoplay_back){
            VideoPlayActivity.this.finish();
        }
    }

    private void startOrStop(){
        if(start_stop.getText().equals("start")){
//             mediaPlayer.start();
            onPrepared(mediaPlayer);
            start_stop.setText("pause");
        }else if (start_stop.getText().equals("pause")){
            isPlaying = false;
            mediaPlayer.pause();
            start_stop.setText("start");
        }
    }

    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            Log.i("tan8","onStopTrackingTouch");
            int progress = seekBar.getProgress();
            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(progress);
            }
        }
    };

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i("tan8","surfaceCreated");
            // 当SurfaceView中的Surface被创建的时候被调用
            //在这里我们指定MediaPlayer在当前的Surface中进行播放
            mediaPlayer.setDisplay(holder);
            //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
            mediaPlayer.prepareAsync();

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            Log.i("tan8","surfaceDestroyed");
//            if (mediaPlayer != null && mediaPlayer.isPlaying()){
//                currPosition = mediaPlayer.getCurrentPosition();
//                mediaPlayer.stop();
                isPlaying = false;
//            }
        }
    }


    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (width == 0 || height == 0) {
            Log.e("tan8", "invalid video width(" + width + ") or height(" + height
                    + ")");
            return;
        }

        Log.i("tan8","width:"+width);
//        mIsVideoSizeKnown = true;
//        mVideoHeight = height;
//        mVideoWidth = width;

//        int w = mSurfaceViewHeight * width / height;
//        int margin = (mSurfaceViewWidth - w) / 2;
//        Log.i("tan8", "margin:" + margin);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//        lp.setMargins(margin, 0, margin, 0);
//        surfaceView.setLayoutParams(lp);

//        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//            startVideoPlayback();
//        }

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.i("tan8","onInfo");
        // 当一些特定信息出现或者警告时触发
        switch(what){
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //当prepare完成后，该方法触发，在这里我们播放视频
        Log.i("tan8","onPrepared");
        //首先取得video的宽和高
        vWidth = mediaPlayer.getVideoWidth();
        vHeight = mediaPlayer.getVideoHeight();
        int time = mediaPlayer.getDuration();
        int mins = time/1000/60;
        int secs = time/1000-mins*60;
        timetv.setText(String.format("%02d",mins)+":"+String.format("%02d",secs));

        Log.i("tan8", "总时长：" + mediaPlayer.getDuration() /1000/60);
        Log.i("tan8","vWidth:"+vWidth+",vHeight:"+vHeight);
        Log.i("tan8","currDisplay.getWidth():"+currDisplay.getWidth()+",currDisplay.getHeight():"+currDisplay.getHeight());

//        if (vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()) {
        //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
        float wRatio = (float) vWidth / (float) currDisplay.getWidth();
        float hRatio = (float) vHeight / (float) currDisplay.getHeight();

        //选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);

            vWidth = (int) Math.ceil((float) vWidth / ratio);
            vHeight = (int) Math.ceil((float) vHeight / ratio);

//        }else{
//
//        }


        Log.i("tan8","vWidth："+vWidth+",vHeight:"+vHeight);
        //设置surfaceView的布局参数
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(vWidth, vHeight);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        surfaceView.setLayoutParams(lp);

        //然后开始播放视频

        seekBar.setMax(mediaPlayer.getDuration());

        isPlaying = true;


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isPlaying){

//                    if (mediaPlayer == null ||!mediaPlayer.isPlaying()){
//
//                        Log.i("tan8","mediaPlayer break");
//                            break;
//                    }

                    try{
                        Thread.sleep(1000);
                        if(mediaPlayer != null){
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = "";
                            hprogress.sendMessage(msg);

//                            Log.i("tan8","progress:"+progress);
                        }else{
                            break;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        mediaPlayer.start();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        isPlaying = false;
        //mediaplayer 播放完成后出发
        Log.i("tan8","onCompletion");
        int time = mediaPlayer.getDuration();
        int mins = time/1000/60;
        int secs = time/1000-mins*60;
        curtimetv.setText(String.format("%02d",mins)+":"+String.format("%02d",secs));
        mediaPlayer.stop();
        mediaPlayer.release();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                VideoPlayActivity.this.finish();
            }
        },1000);

    }

    //videopath":"\/data\/attachments\/0AEE7BBB-A5ED-918F-5247-6EB32E41456E\/video\/1448924547466.mp4"
    //"videopath":"\/data\/attachments\/24DFD7FB-4508-E073-2F8B-E6B39A4139BE\/video\/VID_20160312_104056.mp4
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.v("tan8", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.v("tan8", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return false;
    }



    private Handler hprogress = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaPlayer != null && isPlaying){

                Log.i("tan8","CurrentPosition:"+mediaPlayer.getCurrentPosition());
                int progress = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(progress);
                int mins = progress/1000/60;
                int secs = progress/1000-mins*60;
                curtimetv.setText(String.format("%02d",mins)+":"+String.format("%02d",secs));
            }


        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("tan8","onStop");
//        isPlaying = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("tan8","onResume");
//        isPlaying = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("tan8","onDestroy");
        isPlaying = false;
    }
}
