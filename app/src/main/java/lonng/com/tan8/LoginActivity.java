package lonng.com.tan8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.utils.SharePrefUtil;

/**
 * Created by Administrator on 2016/2/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.login_btn)
    Button login_btn;
    @Bind(R.id.login_register)
    Button login_register;
    @Bind(R.id.login_account)
    EditText ed_account;
    @Bind(R.id.login_pwd)
    EditText ed_pwd;

    private String account;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        ButterKnife.bind(this);
        login_btn.setOnClickListener(this);
        login_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                account = ed_account.getEditableText().toString();
                pwd = ed_pwd.getEditableText().toString();
                if (account == null || account.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd == null || pwd.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();

                break;
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();

                break;
        }
    }

    private void login() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("uname",account);
        map.put("userpassword",pwd);

        new SendHttpThreadMime(CommonUtils.LOGINURL, LoginActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                Log.i("tan8","login:"+result);
                if (result == null || result.equals("")) {
                    return;
                }


                String uid = "",uname = "",headiconUrl= "";
                try {
                    JSONObject json = new JSONObject(result);
                    // login:{"uid":"8","uname":"555","uprofile":""}
                    if (json.has("uid")){
                        uid = json.getString("uid");
                        if (uid.equals("-1")){
                            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (json.has("uname")){
                        uname = json.getString("uname");
                    }
                    if (json.has("uprofile")){
                        if(!json.getString("uprofile").equals("")){
                            headiconUrl = json.getString("uprofile");
                        }
                    }
                    //存入首选项
                    SharePrefUtil.saveString(LoginActivity.this,CommonUtils.UID,uid);
                    SharePrefUtil.saveString(LoginActivity.this, CommonUtils.ACCOUNT, account);
                    SharePrefUtil.saveString(LoginActivity.this, CommonUtils.PWD, pwd);
                    TanApplication.isLogin = true;
                    TanApplication.curUser.setUserId(uid);
                    TanApplication.curUser.setUserNickname(uname);
                    TanApplication.curUser.setHeadiconUrl("");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, map, 0, null).start();

    }


}
