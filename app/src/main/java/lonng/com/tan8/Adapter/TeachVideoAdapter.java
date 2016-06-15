package lonng.com.tan8.Adapter;

import android.content.Context;
import android.util.Log;
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

import lonng.com.tan8.Entity.ClassVideo;
import lonng.com.tan8.R;

/**
 * Created by Administrator on 2016/3/1.
 */
public class TeachVideoAdapter extends BaseAdapter{

    private List<ClassVideo> apprecList;
    private Context mct;
    private DisplayImageOptions options;


    public TeachVideoAdapter(Context context, List<ClassVideo> objects) {
        this.apprecList = objects;
        this.mct = context;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();
    }

    @Override
    public int getCount() {
        return apprecList.size();
    }

    @Override
    public Object getItem(int position) {
        return apprecList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mct).inflate(R.layout.item_teachvideo, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.video_item_name);
            holder.iv = (ImageView)convertView.findViewById(R.id.video_item_image);
            holder.video_item_dec = (TextView)convertView.findViewById(R.id.video_item_dec);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (apprecList.get(position).getDescription().equals("null")){
            holder.video_item_dec.setText(apprecList.get(position).getVideoName()+"");
        }else {

            holder.video_item_dec.setText(apprecList.get(position).getDescription()+"");
        }

        holder.name.setText(apprecList.get(position).getVideoName()+"");
        ImageLoader.getInstance().displayImage(apprecList.get(position).getPreviewimageUrl(),holder.iv,options);

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView name,video_item_dec;

    }
}
