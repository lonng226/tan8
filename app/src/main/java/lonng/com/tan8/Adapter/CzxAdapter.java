package lonng.com.tan8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.VideoPlayActivity;

/**
 * Created by Administrator on 2015/12/16.
 */
public class CzxAdapter extends BaseAdapter{

    private List<Invitation> invitations ;
    private Context ct;
    private DisplayImageOptions options;
    public CzxAdapter(List<Invitation> invitations,Context ct){
        this.invitations = invitations;
        this.ct = ct;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();
    }

    @Override
    public int getCount() {
        return invitations.size();
    }

    @Override
    public Object getItem(int position) {
        return invitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(ct).inflate(R.layout.item_czx,null);
            viewHolder = new ViewHolder();
            viewHolder.headicom = (ImageView)convertView.findViewById(R.id.item_czx_headicon);
            viewHolder.nickname =(TextView)convertView.findViewById(R.id.item_czx_nickname);
            viewHolder.content = (TextView)convertView.findViewById(R.id.item_czx_content);
            viewHolder.pic = (ImageView)convertView.findViewById(R.id.item_czx_pic);
            viewHolder.dzpersons = (TextView)convertView.findViewById(R.id.item_dzpersons);
            viewHolder.bank = (TextView)convertView.findViewById(R.id.item_czx_bank);
            viewHolder.dz = (TextView)convertView.findViewById(R.id.item_czx_dz);
            viewHolder.pl = (TextView)convertView.findViewById(R.id.item_czx_pl);
            viewHolder.pllistview = (ListView)convertView.findViewById(R.id.item_czx_listview);
            convertView.setTag(viewHolder);

        }else {
            viewHolder =(ViewHolder)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(invitations.get(position).getHeadiconUrl(),viewHolder.headicom,options);
        viewHolder.nickname.setText(invitations.get(position).getNickName());
        viewHolder.content.setText(invitations.get(position).getContent());
        List<String> picurls  = invitations.get(position).getPicUrls();
        if (picurls != null) {
            for (int i = 0; i < picurls.size(); i++) {
                ImageLoader.getInstance().displayImage(picurls.get(i),viewHolder.pic,options);
            }
        }

        viewHolder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, VideoPlayActivity.class);
                intent.putExtra("filepath",invitations.get(position).getVideoUrl());
                ct.startActivity(intent);
            }
        });

        List<User> users = invitations.get(position).getUsers();
        if (users != null){
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < users.size(); i++) {
                String nickName = users.get(i).getUserNickname();
                if(nickName != null && !nickName.equals("")){
                    sb.append(nickName);
                }
            }

           viewHolder.dzpersons.setText(sb.toString());

        }
        viewHolder.bank.setText(invitations.get(position).getBank());
        viewHolder.dz.setText(invitations.get(position).getDzCount());
        viewHolder.pl.setText(invitations.get(position).getPlCount());

        List<Comment> pls = invitations.get(position).getComments();
        if (pls != null && pls.size()>0){
            viewHolder.pllistview.setAdapter(new PlAdapter(pls,ct));
            setListViewHeightBasedOnChildren(viewHolder.pllistview);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView headicom,pic;
        TextView content,bank,dz,pl,nickname,dzpersons;
        ListView pllistview;
    }


    //setadapter之后调用
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height += 5;
        // if without this statement,the listview will be a
        // little short
        listView.setLayoutParams(params);
    }

}
