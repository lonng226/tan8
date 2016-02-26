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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/2/24.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{

    @Bind(R.id.register_account)
    EditText ed_account;
    @Bind(R.id.register_pwd)
    EditText ed_pwd;
    @Bind(R.id.register_register)
    Button register;
    @Bind(R.id.register_email)
    EditText ed_email;

    private String account;
    private String pwd;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeractivity);
        ButterKnife.bind(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_register:
                register();
                break;
        }
    }

    private void register(){

        account = ed_account.getEditableText().toString();
        pwd = ed_pwd.getEditableText().toString();
        email = ed_email.getEditableText().toString();

        if (account == null || account.equals("")){

            Toast.makeText(RegisterActivity.this,"请输入账号",Toast.LENGTH_LONG).show();
            return;
        }
        if (email == null || email.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd == null || pwd.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String,String> map = new HashMap<String,String>();

        new SendHttpThreadMime(CommonUtils.HTTPHOST, RegisterActivity.this,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result =(String) msg.obj;
                if (result == null || result.equals("")){
                    return;
                }

                Toast.makeText(RegisterActivity.this,"注册成功请登录",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                RegisterActivity.this.finish();
            }
        },map,0,null).start();
    }
}
