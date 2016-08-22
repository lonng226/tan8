package lonng.com.tan8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends Activity implements View.OnClickListener{
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    public static ImageSize imageSize;
    private TextView imagepager_back,imagepager_save;
    ArrayList<String> imgUrls = new ArrayList<String>();



    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position){
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);
        imagepager_back = (TextView)findViewById(R.id.imagepager_back);
        imagepager_back.setOnClickListener(this);

        imagepager_save = (TextView)findViewById(R.id.imagepager_save);
        imagepager_save.setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);

        int startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);

        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.i("tan8","postion："+position);
                curImageUrl = imgUrls.get(position);
                if(curImageUrl.contains("http")){
                }else{
                    curImageUrl=CommonUtils.GET_FILS+curImageUrl;
                }

                for(int i=0; i<guideViewList.size(); i++){
                    guideViewList.get(i).setSelected(i==position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);

        addGuideView(guideGroup, startPos, imgUrls);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagepager_back:
                ImagePagerActivity.this.finish();
                break;
            case R.id.imagepager_save:
                Log.i("tan8","curImageUrl"+curImageUrl);
                if (curImageUrl == null){
                    curImageUrl = imgUrls.get(0);
                    if(curImageUrl.contains("http")){
                    }else{
                        curImageUrl=CommonUtils.GET_FILS+curImageUrl;
                    }
                }
               Bitmap bitmap =  ImageLoader.getInstance().loadImageSync(curImageUrl);
               saveImageToGallery(ImagePagerActivity.this,bitmap);
                break;
        }
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if(imgUrls!=null && imgUrls.size()>0){
            guideViewList.clear();
            for (int i=0; i<imgUrls.size(); i++){
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i==startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.gudieview_width),
                        getResources().getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }


    private  static String curImageUrl = null;
    private static class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private DisplayImageOptions options;
        private Context context;

        public void setDatas(List<String> datas) {
            if(datas != null )
                this.datas = datas;
        }

        public ImageAdapter(Context context){
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }

        @Override
        public int getCount() {
            if(datas == null) return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if(view != null){
                final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                //预览imageView
                final ImageView smallImageView = new ImageView(context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                layoutParams.gravity = Gravity.CENTER;
                smallImageView.setLayoutParams(layoutParams);
                smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((FrameLayout)view).addView(smallImageView);
                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout)view).addView(loading);

                final String imgurl = datas.get(position);
                final String imgurl_;
                if(imgurl.contains("http")){
                    imgurl_ = imgurl;
                }else{
                    imgurl_=CommonUtils.GET_FILS+imgurl;
                }


                ImageLoader.getInstance().displayImage(imgurl_, imageView, options, new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //获取内存中的缩略图
                        String memoryCacheKey = MemoryCacheUtils.generateKey(imageUri, imageSize);
                        Bitmap bmp = ImageLoader.getInstance().getMemoryCache().get(memoryCacheKey);
                        if(bmp != null && !bmp.isRecycled()){
                            smallImageView.setVisibility(View.VISIBLE);
                            smallImageView.setImageBitmap(bmp);
                        }
                        loading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        loading.setVisibility(View.GONE);
                        smallImageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                    }
                });

                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "tan8picture");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        Log.i("tan8","path=="+file.getAbsolutePath());
    }
}
