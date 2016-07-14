package lonng.com.tan8;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/7.
 */
public class VideoViewPlayAcitivity extends Activity{

    private VideoView mVideoView;
    private TextView tvcache;
    private String remoteUrl;
    private String localUrl;
    private ProgressDialog progressDialog = null;


    //1MB(兆字节)=1024KB(千字节)
    //	1KB(千字节)=1024B(字节)

    private static final int READY_BUFF = 1000 * 1024;
    private static final int CACHE_BUFF = 500 * 1024;

    private boolean isready = false;
    private boolean iserror = false;
    private int errorCnt = 0;
    private int curPosition = 0;
    private long mediaLength = 0;
    private long readSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoviewplay);

        findViews();
        init();
        playvideo();
//		test();
    }

    private void findViews() {
        this.mVideoView = (VideoView) findViewById(R.id.bbvideoview);
        this.tvcache = (TextView) findViewById(R.id.tvcache);
    }

    private void test(){
//        MediaController mediaController = new MediaController(this);
//		this.mVideoView.setMediaController(mediaController);
        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        this.mVideoView.setLayoutParams(layoutParams);
//        this.mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.a));
        this.mVideoView.start();
    }

    private void init() {
        Intent intent = getIntent();

        this.remoteUrl = intent.getStringExtra("filepath");
        System.out.println("remoteUrl: " + remoteUrl);

        if (this.remoteUrl == null) {
            finish();
            return;
        }

//        this.localUrl = intent.getStringExtra("cache");

        mVideoView.setMediaController(new MediaController(this));

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                System.out.print("onPrepared");
                dismissProgressDialog();
                mVideoView.seekTo(curPosition);
                mp.start();
            }
        });


        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                curPosition = 0;
                mVideoView.pause();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                iserror = true;
                errorCnt++;
                mVideoView.pause();
                showProgressDialog();
                return true;
            }
        });
    }

    private void showProgressDialog() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = ProgressDialog.show(VideoViewPlayAcitivity.this,
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

    private void playvideo() {
        if (!URLUtil.isNetworkUrl(this.remoteUrl)) {
            mVideoView.setVideoPath(this.remoteUrl);
            mVideoView.start();
            return;
        }

        showProgressDialog();

        new Thread(new Runnable() {

            @Override
            public void run() {
                FileOutputStream out = null;
                InputStream is = null;

                try {
                    URL url = new URL(remoteUrl);
                    Log.i("test", "remoteUrl:" + remoteUrl);
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

                    if (localUrl == null) {
                        localUrl = Environment.getExternalStorageDirectory()
                                .getAbsolutePath()
                                + "/VideoCache/"
                                + System.currentTimeMillis() + ".mp4";
                    }

                    System.out.println("localUrl: " + localUrl);

                    File cacheFile = new File(localUrl);

                    if (!cacheFile.exists()) {
                        cacheFile.getParentFile().mkdirs();
                        cacheFile.createNewFile();
                    }

                    readSize = cacheFile.length();
                    out = new FileOutputStream(cacheFile, true);

                    httpConnection.setRequestProperty("User-Agent", "NetFox");
                    httpConnection.setRequestProperty("RANGE", "bytes="+ readSize + "-");

                    is = httpConnection.getInputStream();

                    mediaLength = httpConnection.getContentLength();
                    Log.i("test", "mediaLength:"+mediaLength);
                    if (mediaLength == -1) {
                        return;
                    }

                    mediaLength += readSize;

                    byte buf[] = new byte[4 * 1024];
                    int size = 0;
                    long lastReadSize = 0;

                    mHandler.sendEmptyMessage(VIDEO_STATE_UPDATE);

                    while ((size = is.read(buf)) != -1) {
                        try {
                            out.write(buf, 0, size);
                            readSize += size;
                            Log.i("test", "readSize:"+readSize+"lastReadSize:"+lastReadSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!isready) {
                            if ((readSize - lastReadSize) > READY_BUFF) {
                                lastReadSize = readSize;
                                mHandler.sendEmptyMessage(CACHE_VIDEO_READY);
                            }
                            Log.i("test", "!isready");
                        } else {
                            if ((readSize - lastReadSize) > CACHE_BUFF* (errorCnt + 1)) {
                                lastReadSize = readSize;
                                mHandler.sendEmptyMessage(CACHE_VIDEO_UPDATE);
                            }

                            Log.i("test", "isready");
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

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_STATE_UPDATE:
                    double cachepercent = readSize * 100.00 / mediaLength * 1.0;
                    String s = String.format("已缓存: [%.2f%%]", cachepercent);

                    if (mVideoView.isPlaying()) {
                        curPosition = mVideoView.getCurrentPosition();
                        int duration = mVideoView.getDuration();
                        duration = duration == 0 ? 1 : duration;

                        double playpercent = curPosition * 100.00 / duration * 1.0;

                        int i = curPosition / 1000;
                        int hour = i / (60 * 60);
                        int minute = i / 60 % 60;
                        int second = i % 60;

                        s += String.format(" 播放: %02d:%02d:%02d [%.2f%%]", hour,
                                minute, second, playpercent);
                    }

                    tvcache.setText(s);

                    mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
                    break;

                case CACHE_VIDEO_READY:
                    isready = true;
                    dismissProgressDialog();
                    mVideoView.setVideoPath(localUrl);
                    mVideoView.start();
                    break;

                case CACHE_VIDEO_UPDATE:
                    if (iserror) {
                        mVideoView.setVideoPath(localUrl);
                        dismissProgressDialog();
                        mVideoView.start();
                        iserror = false;
                    }
                    break;

                case CACHE_VIDEO_END:
                    if (iserror) {
                        mVideoView.setVideoPath(localUrl);
                        dismissProgressDialog();
                        mVideoView.start();
                        iserror = false;
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };
}
