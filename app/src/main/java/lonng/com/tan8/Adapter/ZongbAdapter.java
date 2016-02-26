package lonng.com.tan8.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import lonng.com.tan8.Entity.User;

/**
 * Created by Administrator on 2015/12/25.
 */
public class ZongbAdapter extends BaseAdapter{


    private User userSelf;
    private Context context;
    private List<User> userList;

    public ZongbAdapter(User userSelf,Context context,List<User> userList){
        this.userSelf = userSelf;
        this.context = context;
        this.userList = userList;
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0:1;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
