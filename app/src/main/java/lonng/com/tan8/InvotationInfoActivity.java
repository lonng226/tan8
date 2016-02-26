package lonng.com.tan8;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.base.BaseActivity;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.view.RefreshLinearLayout;

/**
 * Created by Administrator on 2015/12/28.
 */
public class InvotationInfoActivity extends BaseActivity implements RefreshLinearLayout.OnRefreshListener,AbsListView.OnScrollListener{

    @Bind(R.id.invatationinfo_headicon)
    ImageView iv_headicon;
    @Bind(R.id.invatationinfo_nickname)
    TextView tv_nikename;
    @Bind(R.id.invatationinfo_content)
    TextView tv_content;
    @Bind(R.id.invatationinfo_pic)
    ImageView iv_pic;
    @Bind(R.id.invatationinfo_bank)
    TextView tv_bank;
    @Bind(R.id.invatationinfo_time)
    TextView tv_time;
    @Bind(R.id.invatationinfo_dzcount)
    TextView tv_dzcount;
    @Bind(R.id.invatationinfo_plcount)
    TextView tv_plcount;
    @Bind(R.id.invatationinfo_grideview)
    GridView gridView;
    @Bind(R.id.invatationinfo_listview)
    ListView listview;
    @Bind(R.id.invatationinfo_refreshLinearLayout)
    RefreshLinearLayout refreshLinearLayout;
    private DisplayImageOptions options;
    private List<User> upUserList;
    private List<Comment> commentList;

    private View footerview;
    private ProgressBar progressBar;
    private TextView tv_load;
    private int startIndex;
    private int reqestCount = 20;
    private int lastIndex;
    private MyListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invatationinfo);
        ButterKnife.bind(this);
        refreshLinearLayout.setOnRefreshListener(this);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();


        if (upUserList == null)
            upUserList = new ArrayList<User>();
        if (commentList == null)
            commentList = new ArrayList<Comment>();
        //test
        ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=3710103736,733712610&fm=21&gp=0.jpg?a=1",iv_headicon,options);

        ImageLoader.getInstance().displayImage("http://images.99pet.com/InfoImages/wm600_450/1d770941f8d44c6e85ba4c0eb736ef69.jpg?a=2",iv_pic,options);

        for (int i = 0; i <20 ; i++) {
            User u = new User();
            u.setHeadiconUrl("http://img0.imgtn.bdimg.com/it/u=3710103736,733712610&fm=21&gp=0.jpg?a="+i);
            upUserList.add(u);
        }

        //test
        for (int i = 0; i <30 ; i++) {
            Comment c = new Comment();
            User p = new User();
            p.setHeadiconUrl("http://img0.imgtn.bdimg.com/it/u=3710103736,733712610&fm=21&gp=0.jpg?a="+(i+100));
            p.setUserNickname("白居易");
            c.setPlUser(p);
            c.setMessage("元旦了，好快啊");
            c.setPltime("12-29 9:00");
            if (i%5 == 0){
                p = null;
                p = new User();
                p.setUserNickname("王维");
                c.setReplyUser(p);
            }
            commentList.add(c);
        }
        gridView.setAdapter(new MyGridViewAdapter(upUserList,ct));
        gridView.setOnItemClickListener(gridviewItemClikListener);


        footerview = LayoutInflater.from(this).inflate(R.layout.footer_layout,null);
        progressBar = (ProgressBar)footerview.findViewById(R.id.footer_progressbar);
        tv_load = (TextView)footerview.findViewById(R.id.footer_tv);
        listview.addFooterView(footerview);
        listview.setOnScrollListener(this);
        mAdapter = new MyListViewAdapter(commentList,ct);
        listview.setAdapter(mAdapter);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (lastIndex == mAdapter.getCount() -1&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
               Log.i("tan8","loadmore");
            tv_load.setText("正在加载");
            progressBar.setVisibility(View.VISIBLE);
            startIndex += reqestCount;
            loadMoreComments(startIndex,startIndex+reqestCount);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
              lastIndex = firstVisibleItem + visibleItemCount -1-1;
    }

    private void loadMoreComments(int start,int end){
        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String result = (String)msg.obj;
                progressBar.setVisibility(View.GONE);
                if (result == null || result.isEmpty()){
                    tv_load.setText("加载失败");
                    return;
                }
                tv_load.setText("加载更多");
                Log.i("tan8","加载评论："+result);

                mAdapter.notifyDataSetChanged();


            }
        },"",1).start();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh(RefreshLinearLayout view) {
        refreshLinearLayout.finishRefresh();
    }


    class MyGridViewAdapter extends BaseAdapter{

        private List<User> upUsers;
        private Context context;

        public MyGridViewAdapter(List<User> upUsers,Context context){

            this.upUsers = upUsers;
            this.context = context;
        }


        @Override
        public int getCount() {
            if (upUsers == null){
                return 0;
            }
            return upUsers.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return upUsers.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder;
//            if (convertView == null){
//                convertView = LayoutInflater.from(context).inflate(R.layout.item_info_gridview,null);
//                viewHolder = new ViewHolder();
//                viewHolder.iv = (ImageView)convertView.findViewById(R.id.item_info_gridview_iv);
//                convertView.setTag(viewHolder);
//            }else {
//                viewHolder =(ViewHolder) convertView.getTag();
//            }
//            DisplayImageOptions options_grideview = new DisplayImageOptions.Builder()
//                    .showStubImage(R.mipmap.ic_launcher)
//                    .showImageForEmptyUri(R.mipmap.ic_launcher)
//                    .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
//                    .cacheOnDisc().displayer(new RoundedBitmapDisplayer(10)).build();
//            ImageLoader.getInstance().displayImage(upUsers.get(position).getHeadiconUrl(),viewHolder.iv,options_grideview);


            ImageView imageView;
            if(convertView==null){
                imageView=new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else{
                imageView = (ImageView) convertView;
            }
            ImageLoader.getInstance().displayImage(upUsers.get(position).getHeadiconUrl(),imageView,options);
            return imageView;

//            return convertView;
        }


        class ViewHolder{
            ImageView iv;
        }

    }

    private AdapterView.OnItemClickListener  gridviewItemClikListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Log.i("tan8","gridviewitem");
        }
    };

    class MyListViewAdapter extends BaseAdapter{

        private List<Comment> comments;
        private Context context;

        public MyListViewAdapter(List<Comment> comments,Context context){
            this.comments = comments;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (comments == null){
                return 0;
            }
            return comments.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_info_listview,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_headIcon = (ImageView)convertView.findViewById(R.id.item_info_listview_headicon);
                viewHolder.tv_pluserNikename = (TextView)convertView.findViewById(R.id.item_info_listview_plusernikename);
                viewHolder.tv_replayString = (TextView)convertView.findViewById(R.id.item_info_listview_replaystring);
                viewHolder.tv_replayedNikename = (TextView)convertView.findViewById(R.id.item_info_listview_replayedusernikename);
                viewHolder.tv_content = (TextView)convertView.findViewById(R.id.item_info_listview_content);
                viewHolder.tv_pltime = (TextView)convertView.findViewById(R.id.item_info_listview_time);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(comments.get(position).getPlUser().getHeadiconUrl(),viewHolder.iv_headIcon,options);
            viewHolder.tv_pluserNikename.setText(comments.get(position).getPlUser().getUserNickname());
            if (comments.get(position).getReplyUser() != null){
                viewHolder.tv_replayString.setVisibility(View.VISIBLE);
                viewHolder.tv_replayedNikename.setText(comments.get(position).getReplyUser().getUserNickname());
                viewHolder.tv_replayedNikename.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tv_replayString.setVisibility(View.GONE);
                viewHolder.tv_replayedNikename.setVisibility(View.GONE);
            }
            viewHolder.tv_content.setText(":"+comments.get(position).getMessage());
            viewHolder.tv_pltime.setText(comments.get(position).getPltime());

            return convertView;
        }

        class ViewHolder{
            ImageView iv_headIcon;
            TextView  tv_pluserNikename,tv_replayString,tv_replayedNikename,tv_content,tv_pltime;
        }
    }

}
