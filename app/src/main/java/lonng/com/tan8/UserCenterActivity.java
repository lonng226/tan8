package lonng.com.tan8;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/2/24.
 */
public class UserCenterActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

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

    private String Uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercenteractivity);
        ButterKnife.bind(this);

        ucenterRefreshLayout.setOnRefreshListener(this);
        ucenterRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        guanzhuTv.setOnClickListener(this);
        tiezil.setOnClickListener(this);
        yuepul.setOnClickListener(this);
        shoucangl.setOnClickListener(this);
        guanzhul.setOnClickListener(this);
        fansl.setOnClickListener(this);

        Uid = getIntent().getStringExtra("uid");
        if (TanApplication.isLogin) {
            if (Uid.equals(TanApplication.curUser.getUserId())) {
                titlename.setText("我的主页");
            }
        }
    }


    private void getUserInfo(String uid){
        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        }, CommonUtils.HTTPHOST+"",0).start();
    }


    @Override
    public void onRefresh() {
//        ucenterRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.center_guanzhubtn:
                //关注

                break;
            case R.id.center_guanzhu:
                break;
            case R.id.center_tiezi:
                break;
            case R.id.center_yuepu:
                break;
            case R.id.center_shoucang:
                break;
            case R.id.center_fans:
                break;
        }

    }
}
