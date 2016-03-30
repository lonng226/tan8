package lonng.com.tan8.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;

/**
 * Created by Administrator on 2015/12/21.
 */
public class PlAdapter extends BaseAdapter{


    private List<Comment> list;
    private Context context;
    private ICommentItemClickListener commentItemClickListener;// 评论点击事件

    public PlAdapter(List<Comment> list ,Context context){
        this.list = list;
        this.context = context;
    }
    public void setCommentClickListener(
            ICommentItemClickListener commentItemClickListener) {
        this.commentItemClickListener = commentItemClickListener;
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
            viewHolder.pluser = (TextView)convertView.findViewById(R.id.item_pl_username);
            viewHolder.restring = (TextView)convertView.findViewById(R.id.item_pl_restring);
            viewHolder.replayuser = (TextView)convertView.findViewById(R.id.item_pl_replayedusername);
            viewHolder.plcontent = (TextView)convertView.findViewById(R.id.item_pl_content);
            convertView.setTag(viewHolder);
        }else{
          viewHolder = (ViewHolder)convertView.getTag();
        }

        if(list.get(position).getPlUser().getUserNickname() != null){
            viewHolder.pluser.setText(list.get(position).getPlUser().getUserNickname());
        }else{

        }

        User replayu = list.get(position).getReplyUser();
//        Log.i("tan8","replayuName:"+replayu.getUserNickname());
        if (replayu != null && replayu.getUserNickname() != null&&!replayu.getUserNickname().equals("")){
          viewHolder.restring.setVisibility(View.VISIBLE);
            viewHolder.replayuser.setVisibility(View.VISIBLE);
            viewHolder.replayuser.setText(replayu.getUserNickname());
        }else {
            viewHolder.restring.setVisibility(View.GONE);
            viewHolder.replayuser.setVisibility(View.GONE);
        }

        viewHolder.plcontent.setText(":"+list.get(position).getMessage());


        return convertView;
    }

    class ViewHolder{
        TextView pluser,restring,replayuser,plcontent;
    }

    public List<Comment> getDatasource() {
        return list;
    }

    public interface ICommentItemClickListener {

        public void onItemClick(int position);
    }
}

