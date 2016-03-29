package lonng.com.tan8;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.dialog.CustomDialog;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.invitation.ImageGridActivity;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/2/24.
 */
public class UserCenterActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    @Bind(R.id.ucenterRefreshLayout)
    SwipeRefreshLayout ucenterRefreshLayout;

    @Bind(R.id.center_guanzhubtn)
    TextView guanzhuTv;

    @Bind(R.id.center_tiezi)
    LinearLayout tiezil;
    @Bind(R.id.center_yuepu)
    LinearLayout yuepul;
    @Bind(R.id.center_shoucang)
    LinearLayout shoucangl;
    @Bind(R.id.center_guanzhu)
    LinearLayout guanzhul;
    @Bind(R.id.center_fans)
    LinearLayout fansl;

    @Bind(R.id.center_guanzhuCount)
    TextView gCountTv;
    @Bind(R.id.center_tieziCount)
    TextView tCountTv;
    @Bind(R.id.center_yuepuCount)
    TextView yCountTv;
    @Bind(R.id.center_shoucangCount)
    TextView sCountTv;
    @Bind(R.id.center_fansCount)
    TextView fCountTv;

    @Bind(R.id.center_titlename)
    TextView titlename;

    @Bind(R.id.center_listview)
    ListView listview;
    @Bind(R.id.center_null)
    TextView tvnull;
    @Bind(R.id.loginview_headicon)
    ImageView loginview_headicon;

    @Bind(R.id.progress_layout)
    RelativeLayout progress_layout;
    @Bind(R.id.progress_text)
    TextView progress_text;

    private String Uid;
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercenteractivity);
        ButterKnife.bind(this);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();

        ucenterRefreshLayout.setOnRefreshListener(this);
        ucenterRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        guanzhuTv.setOnClickListener(this);
        tiezil.setOnClickListener(this);
        yuepul.setOnClickListener(this);
        shoucangl.setOnClickListener(this);
        guanzhul.setOnClickListener(this);
        fansl.setOnClickListener(this);
        loginview_headicon.setOnClickListener(this);

        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + TanApplication.curUser.getHeadiconUrl(), loginview_headicon, options);

        Uid = getIntent().getStringExtra("uid");
        if (TanApplication.isLogin) {
            if (Uid.equals(TanApplication.curUser.getUserId())) {
                titlename.setText("我的主页");
            }
        }
    }


    private void getUserInfo(String uid){
        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {super.handleMessage(msg);
            }
        }, CommonUtils.HTTPHOST+"",0).start();
    }


    @Override
    public void onRefresh() {
//        ucenterRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.center_guanzhubtn:
                //关注

                break;
            case R.id.center_guanzhu:
                break;
            case R.id.center_tiezi:
                break;
            case R.id.center_yuepu:
                break;
            case R.id.center_shoucang:
                break;
            case R.id.center_fans:
                break;
            case R.id.loginview_headicon:
                //更改头像
                startActivityToDialog();
                break;
        }

    }

    /**
     *
     */
    private void startActivityToDialog(){
        new CustomDialog(UserCenterActivity.this, 12, new EditActivity.OnclickOfButton() {
            @Override
            public void onclick(int type) {

                show(type);
            }
        }, null).show();

    }

    /**
     *
     */
    private void show(int type) {

        switch (type) {
            case 1:
                // 照相
                selectPicFromCamera();
                break;
            case 2:
                // 相册
                selectPicFromLocal(); // 图库选择图片
                break;

            default:
                break;
        }
    }


    /**
     * 照相获取图片
     */
    protected File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    protected void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            Toast.makeText(this, R.string.sd_card_does_not_exist, Toast.LENGTH_LONG).show();
            return;
        }

        cameraFile = new File(CommonUtils.getPath(), "tan8"+System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        //打开照相机 拍照 并设置图片存储路径
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    Map<String,File> filesMap = new HashMap<String,File>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()){
                    Log.i("tan8", "getAbsolutePath():" + cameraFile.getAbsolutePath());
                    setImage(null, cameraFile);
                    setFiles(cameraFile);
                }
            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }
        }
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            Log.i("tan8", "picturePath:"+picturePath);
            File file = new File(picturePath);
            setImage(null, file);
            setFiles(file);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            Log.i("tan8", "picturePath2:"+file.getAbsolutePath());
            setImage(null, file);
            setFiles(file);
        }

    }

    private void setImage(Bitmap bitmap, File file) {

        Bitmap bp = null;
        if (bitmap != null) {
            bp = bitmap;
        } else {
            Bitmap bp_ = fileToBitmap(file);
            if (bp_ != null) {
                bp = bp_;
            } else {
                return;
            }
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024>200){
            progress_layout.setVisibility(View.VISIBLE);
            MyBitmapThread mbt = new MyBitmapThread(bp,new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    progress_layout.setVisibility(View.GONE);
                    loginview_headicon.setImageBitmap(b_);
                }
            });
            mbt.start();
            return;
        }


        loginview_headicon.setImageBitmap(bp);
    }


    Bitmap b_;
    class MyBitmapThread extends Thread{

        private Bitmap b;
        private Handler hanlder;

        public MyBitmapThread(Bitmap b,Handler handler){
            this.b = b;
            this.hanlder = handler;
        }

        @Override
        public void run() {
            super.run();
            try{

                b_ = compressImage(b);

                if(hanlder != null){
                    Message m = new Message();
                    m.obj = "";
                    m.what = 0;
                    hanlder.sendMessage(m);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param file
     */
    private Bitmap fileToBitmap(File file){
        if (file == null) {
            return null;
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            Log.i("tan8","options:"+options);

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if (options <= 10){
                options -= 1;
            }else if(options < 1){
                break;
            }else {
                options -= 10;//每次都减少10
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void setFiles(File file){
        filesMap.put("userpic",file);

        Map<String,String> map = new HashMap<String,String>();
        map.put("userid",Uid);

        new SendHttpThreadMime(CommonUtils.HEADICON, UserCenterActivity.this, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                Log.i("tan8", "result:"+result+"");
//				sendComplete.sendOk();
//                progress_layout.setVisibility(View.GONE);
                if (result.contains("success")){

                    Toast.makeText(UserCenterActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                }

            }
        }, map, 0,filesMap).start();

    }



}
