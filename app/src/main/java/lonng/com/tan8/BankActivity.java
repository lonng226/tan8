package lonng.com.tan8;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lonng.com.tan8.Adapter.BankAdapter;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.control.CirclePublicCommentContralBank;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class BankActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener,View.OnClickListener{

    @Bind(R.id.bankRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.bank_circleLv)
    ListView bankcircleLv;
    @Bind(R.id.bank_titlename)
    TextView banktitle;

    @Bind(R.id.editTextBodyLl)
    LinearLayout mEditTextBody;
    @Bind(R.id.circleEt)
    EditText mEditText;
    @Bind(R.id.sendTv)
    TextView sendTv;
    @Bind(R.id.bank_back)
    TextView back;
    @Bind(R.id.bank_backlayout)
    RelativeLayout bank_backlayout;
    @Bind(R.id.progress_layout)
    RelativeLayout progressLayout;

    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private CirclePublicCommentContralBank mCirclePublicCommentContral;
    private int headHeight;


    private View footerview;
    private TextView footer_tv;
    private ProgressBar progressBar;
    private BankAdapter bankAdapter;
    private int lastitemIndex;
    private int startIndex;
    private int requestCount = 5;

    List<Invitation> invitations;
    private  int bankID;

    @Bind(R.id.banktitle)
    RelativeLayout r_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankactivity);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        bankID = getIntent().getIntExtra("bankId",0);

        switch (bankID){
            case 0:
                banktitle.setText("show");
                break;
            case 1:
                banktitle.setText("成人学琴");
                break;
            case 2:
                banktitle.setText("求谱");
                break;
            case 3:
                banktitle.setText("初学问答");
                break;
        }


        footerview = LayoutInflater.from(this).inflate(R.layout.footer_layout, null);
        footer_tv = (TextView)footerview.findViewById(R.id.footer_tv);
        progressBar = (ProgressBar)footerview.findViewById(R.id.footer_progressbar);
        bankcircleLv.addFooterView(footerview);


        bankcircleLv.setOnScrollListener(new SwpipeListViewOnScrollListener(mSwipeRefreshLayout,this));
        bankcircleLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mEditTextBody.getVisibility()==View.VISIBLE){
                    mEditTextBody.setVisibility(View.GONE);
                    CommonUtils.hideSoftInput(BankActivity.this, mEditText,1);
                    return true;
                }
                return false;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        mCirclePublicCommentContral = new CirclePublicCommentContralBank(this, mEditTextBody, mEditText, sendTv);
        mCirclePublicCommentContral.setmListView(bankcircleLv);

        invitations = new ArrayList<Invitation>();
        bankAdapter = new BankAdapter(invitations,BankActivity.this,progressLayout);
        bankAdapter.setmCirclePublicCommentContral(mCirclePublicCommentContral);
        bankcircleLv.setAdapter(bankAdapter);


        bankcircleLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(ct,InvotationInfoActivity.class);
//                ct.startActivity(intent);
            }
        });

        setViewTreeObserver();


        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                updataPage(0,20,true);
            }
        });


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

        final ViewTreeObserver swipeRefreshLayoutVTO = bankcircleLv.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mSwipeRefreshLayout.getWindowVisibleDisplayFrame(r);
                headHeight = r_title.getHeight();
                int screenH = mSwipeRefreshLayout.getRootView().getHeight() - headHeight;
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
        if (lastitemIndex == bankAdapter.getCount()-1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
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
        mSwipeRefreshLayout.setRefreshing(true);
        updataPage(0,20,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bank_back:
                BankActivity.this.finish();
                break;
        }
    }

    public void updataPage(int fromIndex,int toIndex,boolean isPull_){

        isPull = isPull_;
        new SendHttpThreadGet(handler,CommonUtils.HTTPHOST+"?from="+fromIndex+"&to="+toIndex+"&fid="+bankID ,0).start();

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

            parseJson(result);
            startIndex = invitations.size();
            Log.i("tan8","invatationslistjson:"+result);
            if(!isPull) {
                progressBar.setVisibility(View.GONE);
                if (result.equals("[]")){
                    footer_tv.setText("已全部加载");
                }else {
                    footer_tv.setText("加载更多");
                }
                bankAdapter.notifyDataSetChanged();
            }else {
                bankcircleLv.setAdapter(bankAdapter);
            }
            mSwipeRefreshLayout.setRefreshing(false);
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

}
