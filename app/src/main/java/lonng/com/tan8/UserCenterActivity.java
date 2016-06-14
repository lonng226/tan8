package lonng.com.tan8;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.BankAdapter;
import lonng.com.tan8.Adapter.CenterTieAdapter;
import lonng.com.tan8.Adapter.FansAdapter;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.control.CirclePublicCommentContralBank;
import lonng.com.tan8.control.CirclePublicCommentContralCenter;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.dialog.CustomDialog;
import lonng.com.tan8.http.SendHttpDelete;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.http.SendHttpThreadGetImage;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.invitation.ImageGridActivity;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/2/24.
 */
public class UserCenterActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,AbsListView.OnScrollListener{

    @Bind(R.id.ucenterRefreshLayout)
    SwipeRefreshLayout ucenterRefreshLayout;

    @Bind(R.id.center_guanzhubtn)
    TextView guanzhuTv;

    @Bind(R.id.center_tiezi)
    LinearLayout tiezil;
    @Bind(R.id.center_yuepu)
    LinearLayout yuepul;
    @Bind(R.id.center_shoucang)
    LinearLayout shoucangl;
    @Bind(R.id.center_guanzhu)
    LinearLayout guanzhul;
    @Bind(R.id.center_fans)
    LinearLayout fansl;

    @Bind(R.id.center_guanzhuCount)
    TextView gCountTv;
    @Bind(R.id.center_tieziCount)
    TextView tCountTv;
    @Bind(R.id.center_yuepuCount)
    TextView yCountTv;
    @Bind(R.id.center_shoucangCount)
    TextView sCountTv;
    @Bind(R.id.center_fansCount)
    TextView fCountTv;

    @Bind(R.id.center_titlename)
    TextView titlename;

    @Bind(R.id.center_listview)
    ListView listview;
    @Bind(R.id.center_null)
    TextView tvnull;
    @Bind(R.id.loginview_headicon)
    ImageView loginview_headicon;
    @Bind(R.id.editTextBodyLl)
    LinearLayout mEditTextBody;
    @Bind(R.id.circleEt)
    EditText mEditText;
    @Bind(R.id.sendTv)
    TextView sendTv;
    @Bind(R.id.loginview_nickname)
    TextView loginview_nickname;


    private View footerview;
    private TextView footer_tv;
    private ProgressBar progressBar;
    private CenterTieAdapter centerTieAdapter;
    private int lastitemIndex;
    private int startIndex;
    private int requestCount = 5;

    @Bind(R.id.bank_back)
    TextView back;
    @Bind(R.id.bank_backlayout)
    RelativeLayout bank_backlayout;
    @Bind(R.id.progress_layout)
    RelativeLayout progressLayout;
    @Bind(R.id.centertitle)
    RelativeLayout centerTitle;
    @Bind(R.id.center_head)
    RelativeLayout centerHead;
    @Bind(R.id.center_listviewf)
    ListView listviewf;
    @Bind(R.id.center_listviewg)
    ListView listviewg;

    List<Invitation> invitations;
    List<User> gusers;
    List<User> fusers;

    FansAdapter fansAdapter;
    FansAdapter guanAdapter;

    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private CirclePublicCommentContralCenter mCirclePublicCommentContral;
    private int headHeight;
    private int curpage = 1;




    private String Uid;
    private DisplayImageOptions options;

    private  boolean attention;

    private List<LinearLayout> tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercenteractivity);
        ButterKnife.bind(this);


        registerReceiver(receiver,getIntentFilter());

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();

        ucenterRefreshLayout.setOnRefreshListener(this);
        ucenterRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        guanzhuTv.setOnClickListener(this);
        tiezil.setOnClickListener(this);
        yuepul.setOnClickListener(this);
        shoucangl.setOnClickListener(this);
        guanzhul.setOnClickListener(this);
        fansl.setOnClickListener(this);
        back.setOnClickListener(this);

//        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + TanApplication.curUser.getHeadiconUrl(), loginview_headicon, options);

        footerview = LayoutInflater.from(this).inflate(R.layout.footer_layout, null);
        footer_tv = (TextView)footerview.findViewById(R.id.footer_tv);
        progressBar = (ProgressBar)footerview.findViewById(R.id.footer_progressbar);
        listview.addFooterView(footerview);


        listview.setOnScrollListener(new SwpipeListViewOnScrollListener(ucenterRefreshLayout,this));
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mEditTextBody.getVisibility()==View.VISIBLE){
                    mEditTextBody.setVisibility(View.GONE);
                    CommonUtils.hideSoftInput(UserCenterActivity.this, mEditText,2);
                    return true;
                }
                return false;
            }
        });
        ucenterRefreshLayout.setOnRefreshListener(this);
        ucenterRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        mCirclePublicCommentContral = new CirclePublicCommentContralCenter(this, mEditTextBody, mEditText, sendTv);
        mCirclePublicCommentContral.setmListView(listview);

        invitations = new ArrayList<Invitation>();
        centerTieAdapter = new CenterTieAdapter(invitations,UserCenterActivity.this,progressLayout);
        centerTieAdapter.setmCirclePublicCommentContral(mCirclePublicCommentContral);
        listview.setAdapter(centerTieAdapter);



        setViewTreeObserver();


        Uid = getIntent().getStringExtra("uid");
        if (TanApplication.isLogin){
            if(Uid.equals(TanApplication.curUser.getUserId())){
                guanzhuTv.setVisibility(View.GONE);
            }
        }
        getUserInfo();
//        if (TanApplication.isLogin) {
//            if (Uid.equals(TanApplication.curUser.getUserId())) {
//                titlename.setText("我的主页");
//            }
//        }
        ucenterRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                ucenterRefreshLayout.setRefreshing(true);
                updataPage(0,20,true);
            }
        });
    }

    private void showTab(int type) {
        if (tabs == null) {
            tabs = new ArrayList<LinearLayout>();
            tabs.add(tiezil);
            tabs.add(guanzhul);
            tabs.add(fansl);
        }

        for (int i = 0; i < tabs.size(); i++) {
            LinearLayout l = tabs.get(i);
            if (i == type) {
                l.setBackgroundColor(UserCenterActivity.this.getResources().getColor(R.color.top_bar_normal_bg));
            } else {
                l.setBackgroundColor(0);
            }
        }

    }

    private void getUserInfo(){
        //http://120.24.16.24/tanqin/user.php?uid=1000&type=self
        //http://120.24.16.24/tanqin/user.php?type=another&uid=20&opuid=19

        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                Log.i("tan8","userinfo:"+result);
                if (result == null || result.equals("")){
                    Toast.makeText(UserCenterActivity.this,"获取用户信息失败",Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                     JSONObject json = new JSONObject(result.substring(result.indexOf("{")));
                    Log.i("tan8","r:"+result.substring(result.indexOf("{")));

                    if (json.has("userpic")){
                         String upic = json.getString("userpic");
//                        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + upic.replace("\\",""), loginview_headicon, options);

                        if (TanApplication.isLogin){

                            if (Uid.equals(TanApplication.curUser.getUserId())){
                                try {

                                    new SendHttpThreadGetImage(UserCenterActivity.this,new Handler(){
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);

                                            Bitmap bitmap = (Bitmap)msg.obj;

                                            if (bitmap != null){

                                                loginview_headicon.setImageBitmap(bitmap);   //显示图片
                                            }
                                        }
                                    },CommonUtils.GET_FILS + upic.replace("\\", ""),0).start();


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else {
                                ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + upic.replace("\\",""), loginview_headicon, options);
                            }
                        }else {
                            ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + upic.replace("\\",""), loginview_headicon, options);
                        }



                    }
                    if (json.has("username")){
                         String uname = json.getString("username");
                        titlename.setText(uname+"的主页");
                        loginview_nickname.setText(uname+"");

                    }
                    if (json.has("sumpost")){
                      tCountTv.setText("("+json.getString("sumpost")+")");
                    }
                    if (json.has("sumfollowers")){
                      fCountTv.setText("("+json.getString("sumfollowers")+")");
                    }
                    if(json.has("sumattentions")){
                       gCountTv.setText("("+json.getString("sumattentions")+")");
                    }
                    if(json.has("attentioned")){
                         attention = json.getBoolean("attentioned");
                        if (attention){
                            guanzhuTv.setText("取消关注");
                        }else {
                            guanzhuTv.setText("关注");
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },CommonUtils.GETGUANZHU+"?opuid="+Uid+"&type=another"+"&uid="+TanApplication.curUser.getUserId(),0).start();

    }

    private void doGuanZhu(){
        //http://120.24.16.24/tanqin/user.php?action=attention

        if (!TanApplication.isLogin){
             Toast.makeText(UserCenterActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
            return;
        }
        progressLayout.setVisibility(View.VISIBLE);
        Map<String,String> map = new HashMap<String,String>();
        map.put("uid",TanApplication.curUser.getUserId());
        map.put("attentionid",Uid);
        new SendHttpThreadMime(CommonUtils.GuanZhu, UserCenterActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressLayout.setVisibility(View.GONE);
                String result = (String)msg.obj;
                Log.i("tan8","result:"+result );
                if (result == null || result.equals("")){
                    return;
                }
                try{
                    String resultjson = "";
                    JSONObject json = new JSONObject(result);
                    if (json.has("result")){
                        resultjson = json.getString("result");
                        if (resultjson.equals("success")){
                            guanzhuTv.setText("取消关注");
                            attention = true;
                            Toast.makeText(UserCenterActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, map, 0, null).start();

    }

    private void cancelGuanzhu(){

        progressLayout.setVisibility(View.VISIBLE);

        new SendHttpDelete(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressLayout.setVisibility(View.GONE);
                String result = (String)msg.obj;
                Log.i("tan8","result:"+result );
                if (result == null || result.equals("")){
                    return;
                }
                try{
                    String resultjson = "";
                    JSONObject json = new JSONObject(result);
                    if (json.has("result")){
                        resultjson = json.getString("result");
                        if (resultjson.equals("success")){
                            guanzhuTv.setText("关注");
                            attention = false;
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },CommonUtils.GETGUANZHU+"?attentionid="+Uid+"&uid="+TanApplication.curUser.getUserId()+"&type=attention",0).start();
        Log.i("tan8","取消关注"+CommonUtils.GETGUANZHU+"?attentionid="+Uid+"&uid="+TanApplication.curUser.getUserId()+"&type=attention");
    }


    public RelativeLayout getRe(){
        return bank_backlayout;
    }
    public int getScreenHeight(){
        return mScreenHeight;
    }

    public int getEditTextBodyHeight(){
        return mEditTextBodyHeight;
    }

    public int getHeadHeight(){
        return headHeight;
    }


    private void setViewTreeObserver() {

        final ViewTreeObserver swipeRefreshLayoutVTO = listview.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                ucenterRefreshLayout.getWindowVisibleDisplayFrame(r);
                headHeight = centerTitle.getHeight()+centerHead.getHeight();
                int screenH = ucenterRefreshLayout.getRootView().getHeight() - headHeight;
                int keyH = screenH - (r.bottom - r.top);
                if(keyH == TanApplication.mKeyBoardH){//有变化时才处理，否则会陷入死循环
                    return;
                }
//                Log.d("tan8", "keyH = " + keyH + " &r.bottom=" + r.bottom + " &top=" + r.top);
                TanApplication.mKeyBoardH = keyH;
                mScreenHeight = screenH;//应用屏幕的高度
                mEditTextBodyHeight = mEditTextBody.getHeight();
                if(mCirclePublicCommentContral != null){
                    mCirclePublicCommentContral.handleListViewScroll();
                }
            }
        });
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        Log.i("tan8","bankAdapterCount:"+bankAdapter.getCount());
        //czxAdapter.getCount() 不包括头尾，这个listview还有headview 所以=lastitemIndex的时候加载更多的item显示在最底端
        if (lastitemIndex == centerTieAdapter.getCount()-1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            Log.i("tan8","加载更多");
            startIndex += requestCount;
            footer_tv.setText("正在加载");
            progressBar.setVisibility(View.VISIBLE);
            updataPage(startIndex,startIndex+requestCount,false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastitemIndex = firstVisibleItem + visibleItemCount - 1 -1;
//        Log.i("tan8","lastitemIndex:"+lastitemIndex);
    }



    @Override
    public void onRefresh() {
        ucenterRefreshLayout.setRefreshing(true);

        if (curpage == 1){
            updataPage(0,20,true);
        }else if (curpage == 2){
            if (gusers != null){
                gusers.clear();
            }
            getG();
        }else if (curpage == 3){
            if (fusers != null){
                fusers.clear();
            }
            getF();
        }

    }


    boolean isPull;
    public void updataPage(int fromIndex,int toIndex,boolean isPull_){

        isPull = isPull_;
        new SendHttpThreadGet(handler,CommonUtils.GET_INVATATIONLIST+"?uid="+Uid+"&from="+fromIndex+"&to="+toIndex  ,0).start();

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String)msg.obj;
            if (result == null || result.equals("")){
                Log.i("tan8","get  failed");
                ucenterRefreshLayout.setRefreshing(false);
                return ;
            }

            Log.i("tan8","center:"+result);
            if (msg.what == 0){
                parseJson(result);
                tCountTv.setText("(" + invitations.size()+")");
                startIndex = invitations.size();

                if(!isPull) {
                    progressBar.setVisibility(View.GONE);
                    if (result.equals("[]")){
                        footer_tv.setText("已全部加载");
                    }else {
                        footer_tv.setText("加载更多");
                    }
                    centerTieAdapter.notifyDataSetChanged();
                }else {
                    listview.setAdapter(centerTieAdapter);
                }

                ucenterRefreshLayout.setRefreshing(false);
            }else if (msg.what == 1){
                ucenterRefreshLayout.setRefreshing(false);
                parseG(result);
                if (guanAdapter != null)
                    guanAdapter = null;
                guanAdapter = new FansAdapter(UserCenterActivity.this,gusers);
                listviewg.setAdapter(guanAdapter);

            }else if (msg.what ==2){
                ucenterRefreshLayout.setRefreshing(false);
                parseF(result);
                if (fansAdapter != null)
                    fansAdapter = null;
                fansAdapter = new FansAdapter(UserCenterActivity.this,fusers);
                listviewf.setAdapter(fansAdapter);
            }
        }
    };

    private void parseJson(String result){
        if (invitations == null){
            invitations = new ArrayList<Invitation>();
        }
        if (isPull){
            invitations.clear();
        }

        try{
            JSONArray jsa = new JSONArray(result);
            for (int i = 0; i <jsa.length() ; i++) {
                JSONObject js = (JSONObject)jsa.get(i);
                Invitation invitation = new Invitation();
                //Tid
                if (js.has("tid")){
                    invitation.setTid(js.getInt("tid"));
                }
                if (js.has("fid")){
                    int fid = js.getInt("fid");
                    invitation.setBank(fid);
                }
                if (js.has("authorid") && js.has("authorname")){
                    User user = new User();
                    user.setUserId(js.getString("authorid"));
                    user.setUserNickname(js.getString("authorname"));
                    if (js.has("authorpic")){
                        user.setHeadiconUrl(js.getString("authorpic"));
                    }
                    invitation.setSendUser(user);
                }
                if (js.has("message")){
                    invitation.setContent(js.getString("message"));
                }
                if(js.has("datetime")){
                    invitation.setDatetime(js.getString("datetime"));
                }
                JSONArray comments = js.getJSONArray("comments");
                if (comments != null && comments.length()>0){
                    List<Comment> commentList = new ArrayList<Comment>();
                    for (int j = 0; j <comments.length() ; j++) {
                        JSONObject comment = (JSONObject)comments.get(j);
                        Comment c = new Comment();

                        if (comment.has("authorname")){
                            User pluser = new User();
                            pluser.setUserNickname(comment.getString("authorname"));
                            if (comment.has("authorid")){
                                pluser.setUserId(comment.getString("authorid"));
                            }
                            c.setPlUser(pluser);
                        }

                        if (comment.has("message")){
                            c.setMessage(comment.getString("message"));
                        }

                        if (comment.has("replyauthorname")){
                            User replayuser = new User();
                            replayuser.setUserNickname(comment.getString("replyauthorname"));
                            if (comment.has("replyauthorid")){
                                replayuser.setUserId(comment.getString("replyauthorid"));
                            }
                            c.setReplyUser(replayuser);
                        }
                        if (comment.has("commentid")){
                            c.setPlID(comment.getInt("commentid"));
                        }
                        commentList.add(c);
                    }
                    invitation.setComments(commentList);
                }

                JSONArray jsaup = js.getJSONArray("up");
                if (jsaup != null && jsaup.length()>0){
                    List<User> upers = new ArrayList<User>();
                    for (int j = 0; j < jsaup.length() ; j++) {
                        JSONObject jsup =(JSONObject) jsaup.get(j);
                        User user = new User();
                        if (jsup.has("authorid")){
                            user.setUserId(jsup.getString("authorid"));
                        }
                        if (jsup.has("authorname")){
                            user.setUserNickname(jsup.getString("authorname"));
                        }

                        upers.add(user);
                    }
                    invitation.setUpUsers(upers);
                }

                JSONArray pics = js.getJSONArray("pics");

                if (pics != null && pics.length()>0){
                    List<String> picurls = new ArrayList<String>();
                    for (int j = 0; j <pics.length() ; j++) {
                        String pic = pics.getString(j);
                        picurls.add(pic.replace("\\",""));
                        Log.i("tan8",pic.replace("\\",""));
                    }
                    invitation.setPicUrls(picurls);
                }
                if (js.has("videopath")){
                    invitation.setVideoUrl(js.getString("videopath"));
                }
                if (js.has("previewimage")){
                    invitation.setPreviewimage(js.getString("previewimage"));
                }

                invitations.add(invitation);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.center_guanzhubtn:
                //关注
                if (!attention){
                    doGuanZhu();
                }else {
                    cancelGuanzhu();
                }
                break;
            case R.id.center_guanzhu:
                showTab(1);
                showPage(2);
                break;
            case R.id.center_tiezi:
//                updataPage();
                showTab(0);
                showPage(1);
                break;
            case R.id.center_yuepu:
                break;
            case R.id.center_shoucang:
                break;
            case R.id.center_fans:
                showTab(2);
                showPage(3);
                break;
            case R.id.loginview_headicon:
                //更改头像
                break;
            case R.id.bank_back:

                Intent intent = new Intent("finish");
                UserCenterActivity.this.sendBroadcast(intent);

//                UserCenterActivity.this.finish();
                break;
        }

    }


    private void showPage(int type){

        this.curpage = type;

          if (type == 2){
              //关注
              listview.setVisibility(View.INVISIBLE);
              listviewf.setVisibility(View.INVISIBLE);
              listviewg.setVisibility(View.VISIBLE);
              if (gusers == null){
                  gusers = new ArrayList<User>();
                  gusers.clear();

                  getG();

              }


          }else if (type == 3){
             //粉丝
              listview.setVisibility(View.INVISIBLE);
              listviewf.setVisibility(View.VISIBLE);
              listviewg.setVisibility(View.INVISIBLE);

              if (fusers == null){

                  fusers = new ArrayList<User>();
                  fusers.clear();

                  getF();
              }
          }else if (type == 1){
              //帖子
              listview.setVisibility(View.VISIBLE);
              listviewf.setVisibility(View.INVISIBLE);
              listviewg.setVisibility(View.INVISIBLE);
          }

    }


    //
    private void getG(){
        //http://120.24.16.24/tanqin/user.php?uid=15&type=attentions
        ucenterRefreshLayout.setRefreshing(true);
        new SendHttpThreadGet(handler,CommonUtils.GuanZhuList+"?uid="+Uid+"&type=attentions",1).start();
    }

    private void getF(){
        //http://120.24.16.24/tanqin/user.php?uid=1&type=followers
        ucenterRefreshLayout.setRefreshing(true);
        new SendHttpThreadGet(handler,CommonUtils.GuanZhuList+"?uid="+Uid+"&type=followers",2).start();
    }

    private void parseG(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0;i<jsonArray.length();i++){
                User u = new User();
                JSONObject json = (JSONObject)jsonArray.get(i);
                if (json.has("attentionid")){
                   u.setUserId(json.getString("attentionid"));
                }
                if (json.has("userpic")){
                    u.setHeadiconUrl(json.getString("userpic").replace("\\",""));
                }
                if (json.has("username")){
                    u.setUserNickname(json.getString("username"));
                }

                gusers.add(u);
                gCountTv.setText("("+gusers.size()+")");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseF(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0;i<jsonArray.length();i++){
                User u = new User();
                JSONObject json = (JSONObject)jsonArray.get(i);
                if (json.has("followerid")){
                    u.setUserId(json.getString("followerid"));
                }
                if (json.has("userpic")){
                    u.setHeadiconUrl(json.getString("userpic").replace("\\",""));
                }
                if (json.has("username")){
                    u.setUserNickname(json.getString("username"));
                }

                fusers.add(u);
            }

            fCountTv.setText("("+fusers.size()+")");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private IntentFilter getIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        return intentFilter;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
                if (action!=null && action.equals("finish")){
                    UserCenterActivity.this.finish();
                }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
