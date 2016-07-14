package lonng.com.tan8;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    String path;

    boolean isCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        ButterKnife.bind(this);

        Log.i("tan8","onCreate");
        initView();

    }


    String filepath = null;
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

        filepath = getIntent().getStringExtra("filepath");
        File f = null;
        String localpath="";
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
             localpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/TanVideoCache/" + filepath.substring(filepath.lastIndexOf("/")+1,filepath.length());
            Log.i("tan8","localpath:"+localpath);
             f = new File(localpath);
            if (!f.exists()) {
                path = filepath;
                isCache = false;
            } else {
                path = localpath;
                isCache = true;
            }
        }else{
            path = filepath;
            isCache = false;
            Log.i("tan8","path = filepath");
        }



        try{
//            Cache cache = new FileCache(new File(getExternalCacheDir(), VIDEO_CACHE_NAME));
//              filepath = "http:120.24.16.24/tanqin/uploads/%E6%95%99%E5%AD%A6/test/VID_20160415_202230.mp4";
            Log.i("tan8","initView--path==="+path);
//            if(filepath.contains("http")){
//                Log.i("tan8","filepath.contains(\"http\")");
////                filepath=URLEncoder.encode(filepath, "utf-8");
//                mediaPlayer.setDataSource(path);
//            }else {
//                if(isCache){
//                     // 本地存在
//                    mediaPlayer.setDataSource(path);
//                }else {
//                    mediaPlayer.setDataSource(CommonUtils.GET_FILS+path);
//                }
//
//            }
//              mediaPlayer.setDataSource(localpath);
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
//            mediaPlayer.prepareAsync();
            try {
                writeMedia();
            }catch (Exception e){
                e.printStackTrace();
            }

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
        Log.i("tan8","what:"+what);

//        showProgressDialog();
//        mediaPlayer.pause();

        // 当一些特定信息出现或者警告时触发
        switch(what){
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Log.i("tan8","MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                Log.i("tan8","MEDIA_INFO_METADATA_UPDATE");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Log.i("tan8","MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.i("tan8","MEDIA_INFO_NOT_SEEKABLE");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                Log.i("tan8","MEDIA_INFO_BUFFERING_END");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
               // MediaPlayer暂停播放等待缓冲更多数据。
                Log.i("tan8","MEDIA_INFO_BUFFERING_START");
//                showProgressDialog();
//                mediaPlayer.pause();

                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                mediaPlayer.pause();
//
//                mediaPlayer.reset();

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
        int hour = time/1000/60/60;
        int mins = time/1000/60;
        int secs = time/1000-mins*60;
        timetv.setText(String.format("%02d:%02d:%02d", hour,mins, secs));

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

        Log.i("tan8","vWidth："+vWidth+",vHeight:"+vHeight);
        //设置surfaceView的布局参数
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(vWidth, vHeight);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        surfaceView.setLayoutParams(lp);

        //然后开始播放视频

        seekBar.setMax(mediaPlayer.getDuration());

        isPlaying = true;

        seekBar.setProgress(curPosition);
        mediaPlayer.seekTo(curPosition);

        dismissProgressDialog();
        mediaPlayer.start();

    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        isPlaying = false;
        //mediaplayer 播放完成后出发
        Log.i("tan8","onCompletion");
        int time = mediaPlayer.getDuration();
        int hour = time/1000/60/60;
        int mins = time/1000/60;
        int secs = time/1000-mins*60;
        curtimetv.setText(String.format("%02d:%02d:%02d", hour, mins, secs));

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
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

        Log.i("tan8","onError--------------------------------------------------------------------------"+what+","+extra);

        iserror = true;
        errCount ++;
        mediaPlayer.pause();
        mediaPlayer.reset();
        showProgressDialog();

        if (isloadfinish){
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     try{
                         mediaPlayer.setDataSource(localUrl);
                         mediaPlayer.prepareAsync();
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
             },1500);
        }


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
        return true;
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
        if (mHandler != null){
            mHandler.removeMessages(VIDEO_STATE_UPDATE);
        }


    }




    private String localUrl;

    private long mediaLength = 0;
    private long readSize = 0;

    private void writeMedia() {

        showProgressDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream out = null;
                InputStream is = null;

                try {
                    URL url = null;


                    if(filepath.contains("http")){
                        url = new URL(filepath);
                    }else {
                        url = new URL(CommonUtils.GET_FILS+filepath);
                    }

                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

                    if (localUrl == null) {
                        localUrl = Environment.getExternalStorageDirectory()
                                .getAbsolutePath()
                                + "/TanVideoCache/"
                                + path.substring(path.lastIndexOf("/")+1,path.length());
                    }
                    Log.i("tan8","localUrl:"+localUrl);

                    File cacheFile = new File(localUrl);

                    if (!cacheFile.exists()) {
                        cacheFile.getParentFile().mkdirs();
                        cacheFile.createNewFile();
                    }

                    readSize = cacheFile.length();
                    out = new FileOutputStream(cacheFile, true);


                    httpConnection.setRequestProperty("User-Agent", "NetFox");
                    httpConnection.setRequestProperty("RANGE", "bytes="+ readSize + "-");


                    mHandler.sendEmptyMessage(VIDEO_STATE_UPDATE);
                    mediaLength = httpConnection.getContentLength();
                    Log.i("tan8","mediaLength:"+mediaLength+",readSize:"+readSize);
                    if (mediaLength == -1 && readSize>0) {
                        isloadfinish = true;
                        mHandler.sendEmptyMessage(CACHE_VIDEO_END);
                        return;
                    }

                    if (mediaLength == -1){
                        mHandler.sendEmptyMessage(VIDEO_SIZE0);
                        return;
                    }

                    is = httpConnection.getInputStream();

                    mediaLength += readSize;

                    byte buf[] = new byte[4 * 1024];
                    int size = 0;
                    long lastReadSize = 0;


                    while ((size = is.read(buf)) != -1) {
                        try {
                            out.write(buf, 0, size);
                            readSize += size;
//                        Log.i("tan8","readSize:"+readSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!isready) {
                            if (mediaLength<READY_BUFF ||(readSize - lastReadSize) > READY_BUFF) {
                                isready = true;
                                lastReadSize = readSize;
                                Log.i("tan8", "CACHE_VIDEO_READY");
                                mHandler.sendEmptyMessage(CACHE_VIDEO_READY);
                            }
                        } else {
                            if ((readSize - lastReadSize) > CACHE_BUFF* (0 + 1)) {
                                lastReadSize = readSize;
                                Log.i("tan8","CACHE_VIDEO_UPDATE");
                                mHandler.sendEmptyMessage(CACHE_VIDEO_UPDATE);
                            }

                        }
                    }
                           mHandler.sendEmptyMessage(CACHE_VIDEO_END);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            //
                        }
                    }

                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            //
                        }
                    }
                }

            }
        }).start();


    }


    private final static int VIDEO_STATE_UPDATE = 0;
    private final static int CACHE_VIDEO_READY = 1;
    private final static int CACHE_VIDEO_UPDATE = 2;
    private final static int CACHE_VIDEO_END = 3;
    private final static int VIDEO_SIZE0 = 4;

    //1MB(兆字节)=1024KB(千字节)
    //	1KB(千字节)=1024B(字节)

    private static final int READY_BUFF = 1000 * 1024;
    private static final int CACHE_BUFF = 1000 * 1024;
    private int errCount = 0;

    int curPosition;
    boolean isready = false;
    boolean iserror = false;
    boolean isloadfinish = false;

    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_STATE_UPDATE:
                    double cachepercent = readSize * 100.00 / mediaLength * 1.0;
                    if (mediaLength == -1){
                        cachepercent = 0;
                    }
                    String s = String.format("已缓存: [%.2f%%]", cachepercent);

                    if (mediaPlayer!= null &&mediaPlayer.isPlaying() ) {
                        curPosition = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();
                        duration = duration == 0 ? 1 : duration;

                        double playpercent = curPosition * 100.00 / duration * 1.0;

                        int i = curPosition / 1000;
                        int hour = i / (60 * 60);
                        int minute = i / 60 % 60;
                        int second = i % 60;

                        curtimetv.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                        seekBar.setProgress(curPosition);
                        s += String.format(" 播放: %02d:%02d:%02d [%.2f%%]", hour, minute, second, playpercent);
                    }

                    Log.i("tan8","s:"+s);

                    if (mHandler != null){
                        mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
                    }
                    break;

                case CACHE_VIDEO_READY:
                    isready = true;
                    try{
                        Log.i("tan8","CACHE_VIDEO_READY1");
                        mediaPlayer.setDataSource(localUrl);
                        mediaPlayer.prepareAsync();
                        dismissProgressDialog();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                case CACHE_VIDEO_UPDATE:
                    try {
                        Log.i("tan8","CACHE_VIDEO_UPDATE1");
                        if (iserror){

                            mediaPlayer.setDataSource(localUrl);
                            mediaPlayer.prepareAsync();
                            dismissProgressDialog();
                            iserror = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                case CACHE_VIDEO_END:
                    isloadfinish = true;
                     try{
                         Log.i("tan8","CACHE_VIDEO_END1");
                         if (iserror || !mediaPlayer.isPlaying()){
                             Log.i("tan8","CACHE_VIDEO_END2");
                             mediaPlayer.setDataSource(localUrl);
                             mediaPlayer.prepareAsync();
                             dismissProgressDialog();
                             iserror = false;
                         }

                     }catch (Exception e){
                         e.printStackTrace();
                     }
                    break;
                case VIDEO_SIZE0:
                    VideoPlayActivity.this.finish();
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private ProgressDialog progressDialog = null;

    private void showProgressDialog() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = ProgressDialog.show(VideoPlayActivity.this,
                            "视频缓存", "正在努力加载中 ...", true, false);
                }
            }
        });
    }

    private void dismissProgressDialog() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            Log.i("tan8","KEYCODE_BACK");
            mediaPlayer.pause();

            VideoPlayActivity.this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
