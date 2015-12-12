package lonng.com.tan8.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BasePage {
    protected Context ct;
    protected View contentView;

    public BasePage(Context context) {
        ct = context;
        contentView = initView((LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }


    public View getContentView() {
        return contentView;
    }

    protected abstract View initView(LayoutInflater inflater);

    public abstract void initData();

    public void onResume() {

    }

    public void getData() {

    }


}
