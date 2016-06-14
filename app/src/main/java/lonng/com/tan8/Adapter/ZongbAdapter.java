package lonng.com.tan8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.UserCenterActivity;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.http.SendHttpThreadGetImage;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2015/12/25.
 */
public class ZongbAdapter extends BaseAdapter{


    private User userSelf;
    private Context context;
    private List<User> userList;
    private DisplayImageOptions options;


    public ZongbAdapter(User userSelf,Context context,List<User> userList){
        this.userSelf = userSelf;
        this.context = context;
        this.userList = userList;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();
    }


    @Override
    public int getItemViewType(int position) {

        return position == 0 ? 0:1;
    }

    @Override
    public int getCount() {
        if (userList == null){
            return 0;
        }
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

        ViewHolder viewHolder;
        if (convertView ==  null){
          convertView = LayoutInflater.from(context).inflate(R.layout.zongbitemlayout,null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView)convertView.findViewById(R.id.zongbitme_headicon);
            viewHolder.name = (TextView)convertView.findViewById(R.id.zongbitem_username);
            viewHolder.sumpost = (TextView)convertView.findViewById(R.id.zongbitem_sumpost);
            viewHolder.sumup = (TextView)convertView.findViewById(R.id.zongbitem_sumup);

            convertView.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) convertView.getTag();
        }

//        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS+userList.get(position).getHeadiconUrl(),viewHolder.iv, options);



        final ImageView iv_ = viewHolder.iv;


        if(userList.get(position).getUserId().equals(TanApplication.curUser.getUserId())){
            try {

                new SendHttpThreadGetImage(context,new Handler(){
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
            ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS +userList.get(position).getHeadiconUrl(), viewHolder.iv, options);
        }






        viewHolder.sumpost.setText("发帖总数："+userList.get(position).getSendInvatationCount()+"");
        viewHolder.name.setText(userList.get(position).getUserNickname());
        viewHolder.sumup.setText("获赞数："+userList.get(position).getUpCount()+"");

        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserCenterActivity.class);
                intent.putExtra("uid", userList.get(position).getUserId() + "");
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView name,sumpost,sumup;
    }
}
