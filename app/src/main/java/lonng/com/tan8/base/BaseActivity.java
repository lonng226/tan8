package lonng.com.tan8.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import lonng.com.tan8.utils.AppManager;


public abstract class BaseActivity extends FragmentActivity implements
        OnClickListener {

    protected Context ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getAppManager().addActivity(this);
        ct = this;
        initView();
        initData();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }


    protected void showToast(String msg) {
        showToast(msg, 0);
    }

    protected void showToast(String msg, int time) {
        Toast.makeText(ct, msg, time).show();
//        CustomToast customToast = new CustomToast(ct, msg, time);
//        customToast.show();
    }


    protected void showProgressDialog(String content) {
//        if (dialog == null && ct != null) {
//            dialog = (CustomProgressDialog) DialogUtil.createProgressDialog(ct,
//                    content);
//        }
//        dialog.show();
    }

    protected void closeProgressDialog() {
//        if (dialog != null)
//            dialog.dismiss();
    }

    public void showLoadingView() {
//        if (loadingView != null)
//            loadingView.setVisibility(View.VISIBLE);
    }

    public void dismissLoadingView() {
//        if (loadingView != null)
//            loadingView.setVisibility(View.INVISIBLE);
    }

    public void showLoadFailView() {
//        if (loadingView != null) {
//            loadingView.setVisibility(View.VISIBLE);
//            loadfailView.setVisibility(View.VISIBLE);
//        }

    }

    public void dismissLoadFailView() {
//        if (loadingView != null)
//            loadfailView.setVisibility(View.INVISIBLE);
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void processClick(View v);

//    protected void loadData(HttpRequest.HttpMethod method, String url,
//                            RequestParams params, RequestCallBack<String> callback) {
//        HttpUtils http = new HttpUtils();
//        http.configCurrentHttpCacheExpiry(0);
//
//        LogUtils.allowD = true;
//        if (params != null) {
//            if (params.getQueryStringParams() != null)
//                LogUtils.d(url + "?" + params.getQueryStringParams().toString());
//        } else {
//            params = new RequestParams();
//
//        }
////		params.addHeader("x-deviceid", app.deviceId);
////		params.addHeader("x-channel", app.channel);
//        if (0 == CommonUtil.isNetworkAvailable(ct)) {
//            showToast("加载失败，请检查网络！");
//        } else {
//            LogUtils.d(url);
//            http.send(method, url, params, callback);
//        }
//    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }


}
