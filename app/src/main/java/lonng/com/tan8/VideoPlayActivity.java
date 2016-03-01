package lonng.com.tan8;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.base.BaseActivity;
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
    MediaPlayer mediaPlayer;
    int postion;
    Display currDisplay;
    int vWidth,vHeight;
    int currPosition;
    boolean isPlaying;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        ButterKnife.bind(this);

        initView();
    }


    protected void initView() {
        surfaceView.getHolder().setFixedSize(200, 200);
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
            mediaPlayer.setDataSource(CommonUtils.GET_FILS+filepath);
        } catch (Exception e){
            e.printStackTrace();
        }

        currDisplay = this.getWindowManager().getDefaultDisplay();


        start_stop.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(change);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_stop){
            startOrStop();
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

            int progress = seekBar.getProgress();
            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(progress);
            }
        }
    };

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
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

            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                currPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }
    }


    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
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

        //首先取得video的宽和高
        vWidth = mediaPlayer.getVideoWidth();
        vHeight = mediaPlayer.getVideoHeight();

        if (vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()) {
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            float wRatio = (float) vWidth / (float) currDisplay.getWidth();
            float hRatio = (float) vHeight / (float) currDisplay.getHeight();

            //选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);

            vWidth = (int) Math.ceil((float) vWidth / ratio);
            vHeight = (int) Math.ceil((float) vHeight / ratio);

            //设置surfaceView的布局参数
            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));

            //然后开始播放视频
        }

        seekBar.setMax(mediaPlayer.getDuration());
        isPlaying = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isPlaying){
                    int progress = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(progress);
                    try{
                        Thread.sleep(100);
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

        //mediaplayer 播放完成后出发
        this.finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.v("mysample", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.v("mysample", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return false;
    }

}
