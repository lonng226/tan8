package lonng.com.tan8.page;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.ZongbAdapter;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.view.HandyRefreshListView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ZongbPage extends BasePage implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.zongbListview)
    ListView listView;
    @Bind(R.id.zongbRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    List<User> users;

    public ZongbPage(Context context){
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_zongb,null);
        ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        return view;
    }

    @Override
    public void initData() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getUserDatas();
             refreshLayout.setRefreshing(true);
            }
        });
    }


    private void getUserDatas(){


        if (users == null){
            users = new ArrayList<User>();
        }else {
            users.clear();
        }

        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                refreshLayout.setRefreshing(false);
                String result = (String)msg.obj;
                if (result == null || result.equals("")){
                    Toast.makeText(ct,"获取数据失败",Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    JSONArray jsa = new JSONArray(result);
                    for (int i = 0;i<jsa.length();i++){
                        JSONObject json = (JSONObject)jsa.get(i);
                        User u = new User();
                        if (json.has("sumpost")){
                            if (json.getString("sumpost").equals("null")){
                                u.setSendInvatationCount(0);
                            }else {
                                u.setSendInvatationCount(Integer.parseInt(json.getString("sumpost")));

                            }
                        }
                        if (json.has("userid")){
                           u.setUserId(json.getString("userid"));
                        }
                        if (json.has("userpic")){
                           u.setHeadiconUrl(json.getString("userpic").replace("\\",""));
                        }
                        if (json.has("username")){
                            u.setUserNickname(json.getString("username"));
                        }
                        if (json.has("sumup")){
                            if (json.getString("sumup").equals("null")){
                                u.setUpCount(0);
                            }else {

                                u.setUpCount(Integer.parseInt(json.getString("sumup")));
                            }
                        }

                        users.add(u);
                    }

                    listView.setAdapter(new ZongbAdapter(null,ct,users));

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, CommonUtils.GuanZhuList+"?type=sumpost",0).start();
    }






    @Override
    public void onRefresh() {
        getUserDatas();
        refreshLayout.setRefreshing(true);
    }
}
