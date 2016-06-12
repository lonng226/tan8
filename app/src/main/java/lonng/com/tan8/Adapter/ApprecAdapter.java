package lonng.com.tan8.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import lonng.com.tan8.Entity.ClassEnjoy;
import lonng.com.tan8.R;

/**
 * Created by Administrator on 2016/3/1.
 */
public class ApprecAdapter extends BaseAdapter{

    private ArrayList<ClassEnjoy> apprecList;
    private Context mct;
    private DisplayImageOptions options;


    public ApprecAdapter(Context context, ArrayList<ClassEnjoy> objects) {
        super();
        apprecList = objects;
        mct = context;
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
            convertView = View.inflate(mct, R.layout.item_apperc, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.apprec_item_name);
            holder.iv = (ImageView)convertView.findViewById(R.id.apprec_item_image);
            holder.des = (TextView)convertView.findViewById(R.id.apprec_item_dec);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(apprecList.get(position).getcEnjoyName()+"");
        holder.des.setText(apprecList.get(position).getcEnjoyDiscription()+"");
        ImageLoader.getInstance().displayImage(apprecList.get(position).getcEnjoyPreviewImageUrl(),holder.iv,options);

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView name,des;

    }
}
