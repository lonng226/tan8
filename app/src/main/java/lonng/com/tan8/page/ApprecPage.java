package lonng.com.tan8.page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.ApprecAdapter;
import lonng.com.tan8.Entity.ClassEnjoy;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.TeachVideoActivity;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by lonng on 15/12/8.
 */
public class ApprecPage extends BasePage implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    @Bind(R.id.apprec_list)
    ListView apprec_list;
    @Bind(R.id.apprecRefreshLayout)
    SwipeRefreshLayout apprecRefreshLayout;
    private ArrayList<ClassEnjoy> list;

    private View footerview;
    private TextView footer_tv;
    private ProgressBar progressBar;
    private int lastitemIndex;
    private int startIndex;
    private int requestCount = 5;
    private ApprecAdapter apprecAdapter;

    public ApprecPage(Context context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_apprec, null);
        ButterKnife.bind(this, view);
        apprecRefreshLayout.setOnRefreshListener(this);
        apprecRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        footerview = inflater.inflate(R.layout.footer_layout, null);
        footer_tv = (TextView) footerview.findViewById(R.id.footer_tv);
        progressBar = (ProgressBar) footerview.findViewById(R.id.footer_progressbar);
//        apprec_list.addFooterView(footerview);

        apprec_list.setOnScrollListener(new SwpipeListViewOnScrollListener(apprecRefreshLayout, this));

        if (list == null) {
            list = new ArrayList<ClassEnjoy>();
        }

//        readyData();
        apprecAdapter = new ApprecAdapter(ct, list);
        apprec_list.setAdapter(apprecAdapter);

        apprecRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                apprecRefreshLayout.setRefreshing(true);
                updataPage(0, 20, true);

            }
        });

        apprec_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ct, TeachVideoActivity.class);
                intent.putExtra("type",list.get(position).getcEnjoyId());
                intent.putExtra("album",list.get(position).getcEnjoyName());
                ct.startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void initData() {


    }

//    private void readyData() {
//        for (int i = 0; i <= 20; i++) {
//            ClassEnjoy c = new ClassEnjoy();
//            c.setcEnjoyId(0);
//            c.setcEnjoyDiscription("今天天气很好，为什么不出去玩");
//            c.setcEnjoyName("今天的天气");
//            c.setcEnjoyPreviewImageUrl("");
//            list.add(c);
//        }
//
//
//    }

    @Override
    public void onRefresh() {
        apprecRefreshLayout.setRefreshing(true);
        updataPage(0, 20, true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

//        Log.i("tan8","czAdapterCount:"+czxAdapter.getCount());
        //czxAdapter.getCount() 不包括头尾，这个listview还有headview 所以=lastitemIndex的时候加载更多的item显示在最底端
        if (lastitemIndex == apprecAdapter.getCount() - 1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            Log.i("tan8", "加载更多");
            startIndex += requestCount;
            footer_tv.setText("正在加载");
            progressBar.setVisibility(View.VISIBLE);
            updataPage(startIndex, startIndex + requestCount, false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 这是最后一个 item 即 footeritem的index
        lastitemIndex = firstVisibleItem + visibleItemCount - 1 - 1;
//              Log.i("tan8","lastitemIndex:"+lastitemIndex);
    }

    private boolean isPull = true;

    public void updataPage(int fromIndex, int toIndex, boolean isPull_) {

        new SendHttpThreadGet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if (result == null || result.equals("")) {
                    Log.i("tan8", "get class failed");
                    apprecRefreshLayout.setRefreshing(false);
                    return;
                }

                Log.i("tan8", "invatationslistjson:" + result);
                parseJson(result);
                startIndex = list.size();
                apprecRefreshLayout.setRefreshing(false);
                if (!isPull) {
                    progressBar.setVisibility(View.GONE);
                    if (result.equals("[]")) {
                        footer_tv.setText("已全部加载");
                    } else {
                        footer_tv.setText("加载更多");
                    }
                    apprecAdapter.notifyDataSetChanged();
                } else {
                    apprec_list.setAdapter(apprecAdapter);
                }

            }
        }, CommonUtils.KtShangxi+"?type=赏析", 0).start();

    }

    private void parseJson(String result) {

        if (isPull) {
            list.clear();
        }
        try {
            JSONArray jsa = new JSONArray(result);
            if (jsa.length() >0) {
                for (int i = 0; i < jsa.length(); i++) {
                    JSONObject js = (JSONObject)jsa.get(i);
                    ClassEnjoy cEnjoy = new ClassEnjoy();
                    if (js.has("type")){
                        cEnjoy.setcEnjoyId(js.getString("type")+"");
                    }
                    if (js.has("name")){
                        cEnjoy.setcEnjoyName(js.getString("name")+"");
                    }
                    if (js.has("description")){
                        cEnjoy.setcEnjoyDiscription(js.getString("description")+"");
                    }
                    if (js.has("preview")){
                        String purl = js.getString("preview");
                         cEnjoy.setcEnjoyPreviewImageUrl(purl.replace("//","")+"");
                    }
                    list.add(cEnjoy);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
