package lonng.com.tan8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.R;

/**
 * Created by Administrator on 2015/12/21.
 */
public class PlAdapter extends BaseAdapter{


    private List<Comment> list;
    private Context context;

    public PlAdapter(List<Comment> list ,Context context){
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pl,null);
            viewHolder = new ViewHolder();
            viewHolder.plcontent = (TextView)convertView.findViewById(R.id.item_pl_content);
            convertView.setTag(viewHolder);
        }else{
          viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.plcontent.setText(list.get(position).getMessage());


        return convertView;
    }

    class ViewHolder{
        TextView plcontent;
    }
}

