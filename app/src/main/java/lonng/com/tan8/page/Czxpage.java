package lonng.com.tan8.page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.CzxAdapter;
import lonng.com.tan8.EditActivity;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.InvotationInfoActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.control.CirclePublicCommentContral;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;
/**
 * Created by Administrator on 2015/12/16.
 */
public class Czxpage extends BasePage implements AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.circleLv)
     ListView mCircleLv;
    private List<Invitation> invitations ;
    private View footerview;
    private TextView footer_tv;
    private ProgressBar progressBar;
    private CzxAdapter czxAdapter;
    private int lastitemIndex;
    private int startIndex;
    private int requestCount = 5;

    @Bind(R.id.editTextBodyLl)
     LinearLayout mEditTextBody;
    @Bind(R.id.circleEt)
     EditText mEditText;
    @Bind(R.id.sendTv)
     TextView sendTv;
    @Bind(R.id.mRefreshLayout)
     SwipeRefreshLayout mSwipeRefreshLayout;

    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private CirclePublicCommentContral mCirclePublicCommentContral;
    public  Czxpage(Context context){
        super(context);
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_czx,null);
        ButterKnife.bind(this, view);
        footerview = inflater.inflate(R.layout.footer_layout,null);
        footer_tv = (TextView)footerview.findViewById(R.id.footer_tv);
        progressBar = (ProgressBar)footerview.findViewById(R.id.footer_progressbar);
        mCircleLv.addFooterView(footerview);


        mCircleLv.setOnScrollListener(new SwpipeListViewOnScrollListener(mSwipeRefreshLayout,this));
        mCircleLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mEditTextBody.getVisibility()==View.VISIBLE){
                    mEditTextBody.setVisibility(View.GONE);
                    CommonUtils.hideSoftInput(ct, mEditText,true);
                    return true;
                }
                return false;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        mCirclePublicCommentContral = new CirclePublicCommentContral(this, mEditTextBody, mEditText, sendTv);
        mCirclePublicCommentContral.setmListView(mCircleLv);

        if (invitations == null)
            invitations = new ArrayList<Invitation>();
        czxAdapter = new CzxAdapter(invitations,ct);
        czxAdapter.setmCirclePublicCommentContral(mCirclePublicCommentContral);
        mCircleLv.setAdapter(czxAdapter);

        setViewTreeObserver();
        return view;
    }
    private void setViewTreeObserver() {

        final ViewTreeObserver swipeRefreshLayoutVTO = mCircleLv.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mSwipeRefreshLayout.getWindowVisibleDisplayFrame(r);
                int screenH = mSwipeRefreshLayout.getRootView().getHeight() - getTitleLayoutHeight();
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
        return ((MainActivity)ct).getTitleLayout().getHeight()+((MainActivity)ct).getTitle2Layout().getHeight();
    }

    @Override
    public void initData() {

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                updataPage(0,20,true);
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

    }



    public EditActivity.SendCompleteListener getSl(){
        return new EditActivity.SendCompleteListener() {
            @Override
            public void sendOk() {
                updataPage(0,20,true);
            }
        };
    }



    @Override
    public void onRefresh() {
        Log.i("tan8","onRefresh");
          updataPage(0,20,true);
    }

    public void updataPage(int fromIndex,int toIndex,boolean isPull_){

        isPull = isPull_;
        new SendHttpThreadGet(handler,CommonUtils.GET_INVATATIONLIST+"?from="+fromIndex+"&to="+toIndex ,0).start();

    }

    private boolean isPull;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String)msg.obj;
            if (result == null || result.equals("")){
                Log.i("tan8","get invatations failed");
                mSwipeRefreshLayout.setRefreshing(false);
                return ;
            }

            Log.i("tan8","invatationslistjson:"+result);

            if(!isPull) {
                progressBar.setVisibility(View.GONE);
                if (result.equals("[]")){
                    footer_tv.setText("已全部加载");
                }else {
                    footer_tv.setText("加载更多");
                }
                czxAdapter.notifyDataSetChanged();
            }else {
                mCircleLv.setAdapter(czxAdapter);
            }
            parseJson(result);
            startIndex = invitations.size();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

//        Log.i("tan8","czAdapterCount:"+czxAdapter.getCount());
        //czxAdapter.getCount() 不包括头尾，这个listview还有headview 所以=lastitemIndex的时候加载更多的item显示在最底端
        if (lastitemIndex == czxAdapter.getCount()-1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
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
                    invitation.setSendUser(user);
                }
                if (js.has("message")){
                    invitation.setContent(js.getString("message"));
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

}
