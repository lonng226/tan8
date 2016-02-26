package lonng.com.tan8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.utils.SharePrefUtil;

/**
 * Created by Administrator on 2016/2/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

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
           switch (v.getId()){
               case R.id.login_btn:
                   account = ed_account.getEditableText().toString();
                   pwd = ed_pwd.getEditableText().toString();
                   if (account == null || account.equals("")){
                       Toast.makeText(LoginActivity.this,"请输入账号",Toast.LENGTH_SHORT).show();
                       return;
                   }
                   if (pwd == null || pwd.equals("")){
                       Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                       return;
                    }
                   login();

                   break;
               case R.id.login_register:
                   Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                   LoginActivity.this.startActivity(intent);
                   LoginActivity.this.finish();

                   break;
           }
    }

    private void login(){

        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result =(String) msg.obj;
                if (result == null || result.equals("")){
                    return;
                }


                //存入首选项
                SharePrefUtil.saveString(LoginActivity.this,CommonUtils.ACCOUNT,account);
                SharePrefUtil.saveString(LoginActivity.this,CommonUtils.PWD,pwd);
                TanApplication.isLogin = true;
                TanApplication.curUser = null;

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();

            }
        }, CommonUtils.HTTPHOST,0).start();

    }


}
