package lonng.com.tan8.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.view.HandyRefreshListView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ZongbPage extends BasePage implements HandyRefreshListView.OnRefreshListener{

    @Bind(R.id.zongb_refreshlistview)
    HandyRefreshListView refreshListView;

    public ZongbPage(Context context){
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_zongb,null);
        ButterKnife.bind(this, view);
        refreshListView.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnRefresh() {
       refreshListView.OnRefreshComplete();
    }
}
