package lonng.com.tan8.page;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.CzxAdapter;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.view.HandyRefreshListView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Czxpage extends BasePage implements HandyRefreshListView.OnRefreshListener{

    @Bind(R.id.czx_listview)
    HandyRefreshListView refreshListView;

    private List<Invitation> invitations ;

    public  Czxpage(Context context){
        super(context);
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_czx,null);
        ButterKnife.bind(this, view);
        refreshListView.setOnRefreshListener(this);
        invitations = new ArrayList<Invitation>();
//        updataPage();
        return view;
    }

    @Override
    public void initData() {
        //test
        for (int i = 0; i < 20 ; i++) {
          Invitation itation = new Invitation();
            itation.setBank("唐诗宋词 ");
            itation.setContent("今天天气不错，有点雾霾，不算大。今天天气不错，有点雾霾，不算大。今天天气不错，有点雾霾，不算大。今天天气不错，有点雾霾，不算大。");
            itation.setDzCount("100");
            itation.setHeadiconUrl("http://img0.imgtn.bdimg.com/it/u=3710103736,733712610&fm=21&gp=0.jpg");
            itation.setNickName("李白");
            List<String> picurls = new ArrayList<String>();
            picurls.add("http://images.99pet.com/InfoImages/wm600_450/1d770941f8d44c6e85ba4c0eb736ef69.jpg");
            itation.setPicUrls(picurls);
            itation.setPlCount("12");
            List<Comment> list = new ArrayList<Comment>();
            for (int j = 0; j <5 ; j++) {
               Comment c = new Comment();
                c.setMessage("杜甫：今天是12月21日了，明天是星期二。");
                list.add(c);
            }
            itation.setComments(list);
            List<User> users = new ArrayList<>();
            for (int j = 0; j < 10 ; j++) {
                User u = new User();
                u.setUserId(""+i);
                u.setUserNickname("白居易,");
                users.add(u);

            }
            itation.setUsers(users);

            invitations.add(itation);
        }
        refreshListView.setAdapter(new CzxAdapter(invitations,ct));
    }

    @Override
    public void OnRefresh() {
//        updataPage();
        refreshListView.OnRefreshComplete();
    }

    public void updataPage(){

        String url = CommonUtils.GETLIST_INVATATION+"?";
        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                if (result == null || result.equals("")){
                    Log.i("tan8","get invatations failed");
                    refreshListView.OnRefreshComplete();
                    return ;
                }

                Log.i("tan8","invatationslistjson:"+result);
                parseJson(result);
                refreshListView.OnRefreshComplete();


            }
        },url ,0).start();

    }

    public void parseJson(String result){
        if (invitations == null){
            invitations = new ArrayList<Invitation>();
        }else{
            invitations.clear();
        }

        try{
            JSONArray jsa = new JSONArray(result);
            for (int i = 0; i <jsa.length() ; i++) {
                JSONObject js = (JSONObject)jsa.get(i);
                Invitation invitation = new Invitation();
                //Tid
                if (js.has("Tid")){
                  invitation.setTid(js.getInt("Tid"));
                }
                if (js.has("Authorid") && js.has("Authorname")){
                    User user = new User();
                    user.setUserId(js.getString("Authorid"));
                    user.setUserNickname(js.getString("Authorname"));
                    invitation.setSendUser(user);
                }
                if (js.has("Message")){
                    invitation.setContent(js.getString("Message"));
                }
                    JSONArray comments = js.getJSONArray("Comments");
                    if (comments != null){
                        List<Comment> commentList = new ArrayList<Comment>();
                        for (int j = 0; j <comments.length() ; j++) {
                            JSONObject comment = (JSONObject)comments.get(j);
                            Comment c = new Comment();

                            if (comment.has("Authorname")){
                                User pluser = new User();
                                pluser.setUserNickname(comment.getString("Authorname"));
                                if (comment.has("Authorid")){
                                    pluser.setUserId(comment.getString("Authorid"));
                                }
                                c.setPlUser(pluser);
                            }

                            if (comment.has("Message")){
                                c.setMessage(comment.getString("Message"));
                            }

                            if (comment.has("replyAuthorname")){
                                User replayuser = new User();
                                replayuser.setUserNickname(comment.getString("replyAuthorname"));
                                if (comment.has("replyAuthorid")){
                                    replayuser.setUserId(comment.getString("replyAuthorid"));
                                }

                            }
                            commentList.add(c);
                        }
                        invitation.setComments(commentList);
                    }

                    JSONArray jsaup = js.getJSONArray("Up");
                    if (jsaup != null){
                        List<User> upers = new ArrayList<User>();
                        for (int j = 0; j < jsaup.length() ; j++) {
                            JSONObject jsup =(JSONObject) jsaup.get(j);
                            User user = new User();
                            if (jsup.has("Authorid")){
                                user.setUserId(jsup.getString("Authorid"));
                            }
                            if (jsup.has("Authorname")){
                               user.setUserNickname(jsup.getString("Authorname"));
                            }
                            upers.add(user);
                        }
                        invitation.setUsers(upers);
                    }

                JSONArray pics = js.getJSONArray("Pics");

                if (pics != null){
                    List<String> picurls = new ArrayList<String>();
                    for (int j = 0; j <pics.length() ; j++) {
                        String pic = pics.getString(j);
                        picurls.add(pic);
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
