package lonng.com.tan8.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    protected Context ct;
    public View rootView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("tan8","onActivityCreated");
        initData(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("tan8","onCreate");
        ct = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.i("tan8","onCreateView");
        rootView = initView(inflater);
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    protected abstract View initView(LayoutInflater inflater);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void processClick(View v);
}
