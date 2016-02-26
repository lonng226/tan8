package lonng.com.tan8.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.EditActivity;
import lonng.com.tan8.LoginActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.UserCenterActivity;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.base.BaseFragment;


public class MineFragment extends BaseFragment implements View.OnClickListener{

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


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_me, null);
        ButterKnife.bind(this, view);
        loginbtn.setOnClickListener(this);

        gointv.setOnClickListener(this);
        tiezil.setOnClickListener(this);
        yuepul.setOnClickListener(this);
        shoucangl.setOnClickListener(this);
        guanzhul.setOnClickListener(this);
        fansl.setOnClickListener(this);
        return view;

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mainActivity = (MainActivity) ct;
        mainActivity.titlename.setText("我的");
//        testText.setText("（1）关注\n" +
//                "（2）粉丝\n" +
//                "（3）发布的帖子\n" +
//                "（4）收藏");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_login:
                Intent intent = new Intent(ct, LoginActivity.class);
                ct.startActivity(intent);
                break;
            case R.id.loginview_goin:
                Intent in = new Intent(ct, UserCenterActivity.class);
                ct.startActivity(in);
                break;
            case R.id.loginview_tiezi:
                break;
            case R.id.loginview_yuepu:
                break;
            case R.id.loginview_shoucang:
                break;
            case R.id.loginview_guanzhu:
                break;
            case R.id.loginview_fans:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("tan8","mefragment onresume");
        if (TanApplication.isLogin){
            loginview.setVisibility(View.VISIBLE);
            nologinview.setVisibility(View.GONE);
        }else {
            loginview.setVisibility(View.GONE);
            nologinview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void processClick(View v) {

    }
}
