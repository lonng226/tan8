package lonng.com.tan8.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import android.widget.Button;
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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.FansAdapter;
import lonng.com.tan8.Adapter.MineTieAdapter;
import lonng.com.tan8.EditActivity;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.LoginActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.UserCenterActivity;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.base.BaseFragment;
import lonng.com.tan8.control.CirclePublicCommentContralMine;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.dialog.CustomDialog;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.http.SendHttpThreadGetImage;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.utils.CommonUtils;


public class MineFragment extends BaseFragment implements View.OnClickListener, AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener{

    private MainActivity mainActivity;
    @Bind(R.id.me_login)
    Button loginbtn;
    @Bind(R.id.login_nologinview)
    RelativeLayout nologinview;
    @Bind(R.id.login_loginview)
    RelativeLayout loginview;

    @Bind(R.id.loginview_goin)
    TextView gointv;
    @Bind(R.id.loginview_fans)
    LinearLayout fansl;
    @Bind(R.id.loginview_guanzhu)
    LinearLayout guanzhul;
    @Bind(R.id.loginview_tiezi)
    LinearLayout tiezil;
    @Bind(R.id.loginview_shoucang)
    LinearLayout shoucangl;
    @Bind(R.id.loginview_yuepu)
    LinearLayout yuepul;
    @Bind(R.id.loginview_headicon)
    ImageView loginview_headicon;
    @Bind(R.id.loginview_nickname)
    TextView loginview_nickname;
    @Bind(R.id.loginview_address)
    TextView loginview_address;

    private DisplayImageOptions options;
    @Bind(R.id.progress_layout)
    RelativeLayout progress_layout;
    @Bind(R.id.progress_text)
    TextView progress_text;
    @Bind(R.id.mine_listview)
    ListView mineListview;

    @Bind(R.id.mineRefreshLayout)
    SwipeRefreshLayout mineRefreshLayout;
    @Bind(R.id.mineHead)
    RelativeLayout mineHead;

    @Bind(R.id.fanscount)
    TextView fansCount;
    @Bind(R.id.tiezicount)
    TextView tieziCount;
    @Bind(R.id.guanzhucount)
    TextView guanzhuCount;
    @Bind(R.id.center_listviewf)
    ListView listviewf;
    @Bind(R.id.center_listviewg)
    ListView listviewg;

    List<Invitation> invitations;
    List<User> gusers;
    List<User> fusers;

    FansAdapter fansAdapter;
    FansAdapter guanAdapter;
    private int curpage = 1;

    private View footerview;
    private TextView footer_tv;
    private ProgressBar progressBar;
    private MineTieAdapter mineTieAdapter;
    private int lastitemIndex;
    private int startIndex;
    private int requestCount = 5;


    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private CirclePublicCommentContralMine mCirclePublicCommentContralMine;

    @Bind(R.id.editTextBodyLl)
    LinearLayout mEditTextBody;
    @Bind(R.id.circleEt)
    EditText mEditText;
    @Bind(R.id.sendTv)
    TextView sendTv;

    List<LinearLayout> tabs ;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_me, null);
        ButterKnife.bind(this, view);


        footerview = inflater.inflate(R.layout.footer_layout,null);
        footer_tv = (TextView)footerview.findViewById(R.id.footer_tv);
        progressBar = (ProgressBar)footerview.findViewById(R.id.footer_progressbar);
        mineListview.addFooterView(footerview);



        mineListview.setOnScrollListener(new SwpipeListViewOnScrollListener(mineRefreshLayout,this));
        mineListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mEditTextBody.getVisibility()==View.VISIBLE){
                    mEditTextBody.setVisibility(View.GONE);
                    CommonUtils.hideSoftInput(ct, mEditText,0);
                    return true;
                }
                return false;
            }
        });
        mineRefreshLayout.setOnRefreshListener(this);
        mineRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        mCirclePublicCommentContralMine = new CirclePublicCommentContralMine(this, mEditTextBody, mEditText, sendTv);
        mCirclePublicCommentContralMine.setmListView(mineListview);



        if (invitations == null)
            invitations = new ArrayList<Invitation>();
        mineTieAdapter = new MineTieAdapter(invitations,ct,((MainActivity)ct).getProgressLayout());
        mineTieAdapter.setmCirclePublicCommentContral(mCirclePublicCommentContralMine);
        mineListview.setAdapter(mineTieAdapter);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(false).cacheInMemory(false).cacheOnDisk(false)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)).build();

        loginbtn.setOnClickListener(this);

        gointv.setOnClickListener(this);
        tiezil.setOnClickListener(this);
        yuepul.setOnClickListener(this);
        shoucangl.setOnClickListener(this);
        guanzhul.setOnClickListener(this);
        fansl.setOnClickListener(this);
        loginview_headicon.setOnClickListener(this);
//        getUserInfo();
        setViewTreeObserver();
        return view;

    }


    private void showTab(int type){
        if (tabs != null){
            tabs.clear();
            tabs = null;
        }
        tabs = new ArrayList<LinearLayout>();
        tabs.add(tiezil);
        tabs.add(guanzhul);
        tabs.add(fansl);

        for (int i = 0;i <tabs.size();i++){
            LinearLayout l = tabs.get(i);
            if (i == type){
                l.setBackgroundColor(ct.getResources().getColor(R.color.top_bar_normal_bg));
            }else {
                l.setBackgroundColor(0);
            }
        }

    }

    private void setViewTreeObserver() {

        final ViewTreeObserver swipeRefreshLayoutVTO = mineListview.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mineRefreshLayout.getWindowVisibleDisplayFrame(r);
                int screenH = mineRefreshLayout.getRootView().getHeight() - getTitleLayoutHeight();
                int keyH = screenH - (r.bottom - r.top);
                if(keyH == TanApplication.mKeyBoardH2){//有变化时才处理，否则会陷入死循环
                    return;
                }
                Log.d("tan8", "keyH = " + keyH + " &r.bottom=" + r.bottom + " &top=" + r.top);
                TanApplication.mKeyBoardH2 = keyH;
                mScreenHeight = screenH;//应用屏幕的高度
                mEditTextBodyHeight = mEditTextBody.getHeight();
                if(mCirclePublicCommentContralMine != null){
                    mCirclePublicCommentContralMine.handleListViewScroll();
                }
            }
        });
    }


    private void getUserInfo(){
        //http://120.24.16.24/tanqin/user.php?uid=1000&type=self
        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                Log.i("tan8","userinfo:"+result);
                if (result == null || result.equals("")){
//                    Toast.makeText(ct,"获取用户信息失败",Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    JSONObject json = new JSONObject(result.substring(result.indexOf("{")));
                    Log.i("tan8","r:"+result.substring(result.indexOf("{")));

                    if (json.has("userpic")){
                        String upic = json.getString("userpic");
//                        ImageLoader.getInstance().getDiskCache().get(CommonUtils.GET_FILS + upic.replace("\\","")).delete();
//                        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + upic.replace("\\",""), loginview_headicon, options);
                        try {

                            new SendHttpThreadGetImage(new Handler(){
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

                    }
                    if (json.has("username")){
                        String uname = json.getString("username");
                        loginview_nickname.setText(TanApplication.curUser.getUserNickname());
                    }
                    if (json.has("sumpost")){
                        tieziCount.setText("("+json.getString("sumpost")+")");
                    }
                    if (json.has("sumfollowers")){
                        fansCount.setText("("+json.getString("sumfollowers")+")");
                    }
                    if(json.has("sumattentions")){
                        guanzhuCount.setText("("+json.getString("sumattentions")+")");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },CommonUtils.GETGUANZHU+"?uid="+TanApplication.curUser.getUserId()+"&type=self",0).start();

    }
    @Override
    public void onRefresh() {

        mineRefreshLayout.setRefreshing(true);

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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

//        Log.i("tan8","czAdapterCount:"+czxAdapter.getCount());
        //czxAdapter.getCount() 不包括头尾，这个listview还有headview 所以=lastitemIndex的时候加载更多的item显示在最底端
        if (lastitemIndex == mineTieAdapter.getCount()-1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            Log.i("tan8","加载更多");
            startIndex += requestCount;
            footer_tv.setText("正在加载");
            progressBar.setVisibility(View.VISIBLE);
            updataPage(startIndex,startIndex+requestCount,false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 这是最后一个 item 即 footeritem的index
        lastitemIndex = firstVisibleItem + visibleItemCount - 1 -1;
//              Log.i("tan8","lastitemIndex:"+lastitemIndex);
    }

    public int getScreenHeight(){
        return mScreenHeight;
    }

    public int getEditTextBodyHeight(){
        return mEditTextBodyHeight;
    }

    public Context getCt(){
        return  ct;
    }

    public int getTitleLayoutHeight(){
        return ((MainActivity)ct).getTitleLayout().getHeight()
                +mineHead.getHeight();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mainActivity = (MainActivity) ct;
        mainActivity.titlename.setText("我的");
//        testText.setText("（1）关注\n" +
//                "（2）粉丝\n" +
//                "（3）发布的帖子\n" +
//                "（4）收藏");


        if (TanApplication.isLogin){
//            mineRefreshLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    mineRefreshLayout.setRefreshing(true);
//                    updataPage(0, 20, true);
//                }
//            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_login:
                Log.i("tan8","loginbtn");
                Intent intent = new Intent(ct, LoginActivity.class);
                ct.startActivity(intent);
                break;
            case R.id.loginview_goin:
                Intent in = new Intent(ct, UserCenterActivity.class);
                in.putExtra("uid",TanApplication.curUser.getUserId());
                ct.startActivity(in);
                break;
            case R.id.loginview_tiezi:

                showTab(0);
                showPage(1);
//                mineRefreshLayout.setRefreshing(true);
//                updataPage(0,20,true);
                break;
            case R.id.loginview_yuepu:
                break;
            case R.id.loginview_shoucang:
                break;
            case R.id.loginview_guanzhu:
//                getData("attentions");
                showTab(1);
                showPage(2);
                break;
            case R.id.loginview_fans:
//                getData("followers");
                showTab(2);
                showPage(3);
                break;
            case R.id.loginview_headicon:
                startActivityToDialog();
                break;
        }
    }



    private void initGuanZhu(){

    }









    public void updataPage(int fromIndex,int toIndex,boolean isPull_){

        isPull = isPull_;
        if (TanApplication.isLogin){
            new SendHttpThreadGet(handler,CommonUtils.GET_INVATATIONLIST+"?uid="+TanApplication.curUser.getUserId()+"&from="+fromIndex+"&to="+toIndex  ,0).start();
        }else{
//            Toast.makeText(ct,"请登录",Toast.LENGTH_SHORT).show();
        }
    }


    private void getData(String type){
             mineRefreshLayout.setRefreshing(true);
            //http://120.24.16.24/tanqin/user.php?uid=15&type=attentions
            new SendHttpThreadGet(handler,CommonUtils.GETGUANZHU+"?uid="+TanApplication.curUser.getUserId()+"&type="+type,0).start();
    }


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String result = (String)msg.obj;
            if (result == null || result.equals("")){
                Log.i("tan8","get invatations failed");
                mineRefreshLayout.setRefreshing(false);
                return ;
            }

            Log.i("tan8","mine:"+result);
            if (msg.what == 0){
            parseJson(result);
            tieziCount.setText("("+invitations.size()+")");
            startIndex = invitations.size();

            if(!isPull) {
                progressBar.setVisibility(View.GONE);
                if (result.equals("[]")){
                    footer_tv.setText("已全部加载");
                }else {
                    footer_tv.setText("加载更多");
                }
                mineTieAdapter.notifyDataSetChanged();
            }else {
                mineListview.setAdapter(mineTieAdapter);
            }

            mineRefreshLayout.setRefreshing(false);
            }else if (msg.what == 1){
                mineRefreshLayout.setRefreshing(false);
                parseG(result);
                if (guanAdapter != null)
                    guanAdapter = null;
                guanAdapter = new FansAdapter(ct,gusers);
                listviewg.setAdapter(guanAdapter);

            }else if (msg.what ==2){
                mineRefreshLayout.setRefreshing(false);
                parseF(result);
                if (fansAdapter != null)
                    fansAdapter = null;
                fansAdapter = new FansAdapter(ct,fusers);
                listviewf.setAdapter(fansAdapter);

            }
        }
    };


    boolean isPull;
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















    boolean  setHeadicon;
    @Override
    public void onResume() {
        super.onResume();
        Log.i("tan8","mefragment onresume");

        if (TanApplication.isLogin){

            if (invitations == null || invitations.size() ==0){
                Log.i("tan8","onresume--------------------------");
                mineRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mineRefreshLayout.setRefreshing(true);
                        updataPage(0, 20, true);
                    }
                });
            }

            getUserInfo();
            loginview.setVisibility(View.VISIBLE);
            mineRefreshLayout.setVisibility(View.VISIBLE);

            nologinview.setVisibility(View.GONE);
//            if (TanApplication.curUser.getHeadiconBitmap() != null){
//                Log.i("tan8","curUser.getHeadiconBitmap()");
//                  loginview_headicon.setImageBitmap(TanApplication.curUser.getHeadiconBitmap());
//            }else {
//                Log.i("tan8","curUser.getHeadiconUrl()");
//                ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + TanApplication.curUser.getHeadiconUrl(), loginview_headicon, options);
//            }
//            loginview_headicon.setImageResource(R.mipmap.ic_launcher);
//            loginview_nickname.setText(TanApplication.curUser.getUserNickname());
//            loginview_address.setText("北京");
        }else {
            loginview.setVisibility(View.GONE);
            mineRefreshLayout.setVisibility(View.GONE);
            nologinview.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void processClick(View v) {

    }

    /**
     *
     */
    private void startActivityToDialog(){
        new CustomDialog(getActivity(), 12, new EditActivity.OnclickOfButton() {
            @Override
            public void onclick(int type) {

                show(type);
            }
        }, null).show();

    }

    /**
     *
     */
    private void show(int type) {

        switch (type) {
            case 1:
                // 照相
                selectPicFromCamera();
                break;
            case 2:
                // 相册
                selectPicFromLocal(); // 图库选择图片
                break;

            default:
                break;
        }
    }


    /**
     * 照相获取图片
     */
    protected File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    protected void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_LONG).show();
            return;
        }

        cameraFile = new File(CommonUtils.getPath(), "tan8"+System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        //打开照相机 拍照 并设置图片存储路径
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    Map<String,File> filesMap = new HashMap<String,File>();


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("tan8","onActivityResult");

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()){
                    Log.i("tan8", "getAbsolutePath():" + cameraFile.getAbsolutePath());
                    setImage(null, cameraFile);
//                    setFiles(cameraFile);
                }
            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }
        }
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(mainActivity, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            Log.i("tan8", "picturePath:"+picturePath);
            File file = new File(picturePath);
            setImage(null, file);
//            setFiles(file);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(mainActivity, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            Log.i("tan8", "picturePath2:"+file.getAbsolutePath());
            setImage(null, file);
//            setFiles(file);
        }

    }

    private void setImage(Bitmap bitmap, final File file) {

        Bitmap bp = null;
        if (bitmap != null) {
            bp = bitmap;
        } else {
            Bitmap bp_ = fileToBitmap(file);
            if (bp_ != null) {
                bp = bp_;
            } else {
                return;
            }
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024>200){
            progress_layout.setVisibility(View.VISIBLE);
            MyBitmapThread mbt = new MyBitmapThread(bp,new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    progress_layout.setVisibility(View.GONE);
                    loginview_headicon.setImageBitmap(lastb_);
                    TanApplication.curUser.setHeadiconBitmap(lastb_);
                    setFiles(file);
                }
            });
            mbt.start();
            return;
        }

        lastb_ = bp;
        loginview_headicon.setImageBitmap(lastb_);
        TanApplication.curUser.setHeadiconBitmap(lastb_);
        setFiles(file);
    }


    Bitmap lastb_;
    class MyBitmapThread extends Thread{

        private Bitmap b;
        private Handler hanlder;

        public MyBitmapThread(Bitmap b,Handler handler){
            this.b = b;
            this.hanlder = handler;
        }

        @Override
        public void run() {
            super.run();
            try{

//                b_ = compressImage(b);
                lastb_ = ThumbnailUtils.extractThumbnail(b, 100, 100);

                if(hanlder != null){
                    Message m = new Message();
                    m.obj = "";
                    m.what = 0;
                    hanlder.sendMessage(m);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param file
     */
    private Bitmap fileToBitmap(File file){
        if (file == null) {
            return null;
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }



    public void saveBitmapFile(Bitmap bitmap,File file){
        //将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            Log.i("tan8","options:"+options);

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if (options <= 10){
                options -= 1;
            }else if(options < 1){
                break;
            }else {
                options -= 10;//每次都减少10
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void setFiles(File file){


        File filetemp = null;

        if (lastb_ != null){
            filetemp = new File(TanApplication.DEFAULT_SAVE_IMAGE_PATH+"temp");
            Log.i("tan8","lastb_  != null");
            saveBitmapFile(lastb_, filetemp);
        }

        if (filetemp != null){
            filesMap.put("userpic",filetemp);
        }else {
            filesMap.put("userpic",file);
        }


        Map<String,String> map = new HashMap<String,String>();
        map.put("userid",TanApplication.curUser.getUserId());

        new SendHttpThreadMime(CommonUtils.HEADICON, mainActivity, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                Log.i("tan8", "result:"+result+"");
//				sendComplete.sendOk();
//                progress_layout.setVisibility(View.GONE);
                if (result.contains("success")){
//                    ImageLoader.getInstance().getDiskCache().get(CommonUtils.GET_FILS +"/data/user/"+TanApplication.curUser.getUserId()+"/"+TanApplication.curUser.getUserId()+".jpg").delete();
//                    ImageLoader.getInstance().clearMemoryCache();
//                    ImageLoader.getInstance().clearDiskCache();

                    Toast.makeText(mainActivity, "设置成功", Toast.LENGTH_SHORT).show();
                    loginview_headicon.setImageBitmap(lastb_);
                    TanApplication.curUser.setHeadiconBitmap(lastb_);
                }

            }
        }, map, 0,filesMap).start();

    }



    private void showPage(int type){

        this.curpage = type;

        if (type == 2){
            //关注
            mineListview.setVisibility(View.INVISIBLE);
            listviewf.setVisibility(View.INVISIBLE);
            listviewg.setVisibility(View.VISIBLE);
            if (gusers != null){
                gusers.clear();
                gusers = null;
            }
            gusers = new ArrayList<User>();

            getG();
        }else if (type == 3){
            //粉丝
            mineListview.setVisibility(View.INVISIBLE);
            listviewf.setVisibility(View.VISIBLE);
            listviewg.setVisibility(View.INVISIBLE);

            if (fusers != null){
                fusers.clear();;
                fusers = null;
            }
            fusers = new ArrayList<User>();

            getF();
        }else if (type == 1){
            //帖子
            mineListview.setVisibility(View.VISIBLE);
            listviewf.setVisibility(View.INVISIBLE);
            listviewg.setVisibility(View.INVISIBLE);
        }

    }


    //
    private void getG(){
        //http://120.24.16.24/tanqin/user.php?uid=15&type=attentions
        mineRefreshLayout.setRefreshing(true);
        new SendHttpThreadGet(handler,CommonUtils.GuanZhuList+"?uid="+TanApplication.curUser.getUserId()+"&type=attentions",1).start();
    }

    private void getF(){
        //http://120.24.16.24/tanqin/user.php?uid=1&type=followers
        mineRefreshLayout.setRefreshing(true);
        new SendHttpThreadGet(handler,CommonUtils.GuanZhuList+"?uid="+TanApplication.curUser.getUserId()+"&type=followers",2).start();
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
                guanzhuCount.setText("("+gusers.size()+")");

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

            fansCount.setText("("+fusers.size()+")");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
