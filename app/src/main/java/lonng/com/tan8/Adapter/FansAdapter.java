package lonng.com.tan8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.List;

import lonng.com.tan8.Entity.ClassVideo;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.UserCenterActivity;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.http.SendHttpThreadGetImage;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/3/1.
 */
public class FansAdapter extends BaseAdapter{

    private List<User> userList;
    private Context mct;
    private DisplayImageOptions options;


    public FansAdapter(Context context, List<User> objects) {
        super();
        userList = objects;
        mct = context;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mct, R.layout.fansitemlayout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.fansitem_username);
            holder.iv = (ImageView)convertView.findViewById(R.id.fansitme_headicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(userList.get(position).getUserNickname());
        Log.i("tan8",userList.get(position).getHeadiconUrl());


//        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS+userList.get(position).getHeadiconUrl(),holder.iv, options);

        final ImageView iv_ = holder.iv;

        if(userList.get(position).getUserId().equals(TanApplication.curUser.getUserId())){
            try {

                new SendHttpThreadGetImage(new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        Bitmap bitmap = (Bitmap)msg.obj;

                        if (bitmap != null){

                            iv_.setImageBitmap(bitmap);   //显示图片
                        }
                    }
                },CommonUtils.GET_FILS+userList.get(position).getHeadiconUrl(),0).start();


            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS+userList.get(position).getHeadiconUrl(), holder.iv, options);
        }


        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mct, UserCenterActivity.class);
                intent.putExtra("uid", userList.get(position).getUserId() + "");
                mct.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView name;

    }
}
