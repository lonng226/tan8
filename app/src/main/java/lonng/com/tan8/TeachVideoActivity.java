package lonng.com.tan8;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.TeachVideoAdapter;
import lonng.com.tan8.Entity.ClassVideo;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by Administrator on 2016/3/1.
 */
public class TeachVideoActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,AbsListView.OnScrollListener{

    @Bind(R.id.teachaRefreshLayout)
    SwipeRefreshLayout teachSwipeRefreshLayout;
    @Bind(R.id.teacha_circleLv)
    ListView teachacircleLv;
    @Bind(R.id.teacha_titlename)
    TextView teachatitle;
    @Bind(R.id.teacha_back)
    TextView back;

    private View footerview;
    private TextView footer_tv;
    private ProgressBar progressBar;
    private int lastitemIndex;
    private int startIndex;
    private int requestCount = 5;
    private TeachVideoAdapter teachVideoAdapter;

    private List<ClassVideo> list;

    private int classid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachvideoactivity);

        ButterKnife.bind(this);

        back.setOnClickListener(this);

        footerview = LayoutInflater.from(this).inflate(R.layout.footer_layout, null);
        footer_tv = (TextView)footerview.findViewById(R.id.footer_tv);
        progressBar = (ProgressBar)footerview.findViewById(R.id.footer_progressbar);
        teachacircleLv.addFooterView(footerview);
        teachacircleLv.setOnScrollListener(new SwpipeListViewOnScrollListener(teachSwipeRefreshLayout,this));

        teachSwipeRefreshLayout.setOnRefreshListener(this);
        teachSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        list = new ArrayList<ClassVideo>();
        teachVideoAdapter = new TeachVideoAdapter(TeachVideoActivity.this,list);
        teachacircleLv.setAdapter(teachVideoAdapter);

        classid = getIntent().getIntExtra("classid",0);
        teachSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

//                teachSwipeRefreshLayout.setRefreshing(true);
//                updataPage(0,20,true);
            }
        });

        teachacircleLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bank_back:
                TeachVideoActivity.this.finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
            teachSwipeRefreshLayout.setRefreshing(true);
//        updataPage(0,20,true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        Log.i("tan8","bankAdapterCount:"+bankAdapter.getCount());
        //czxAdapter.getCount() 不包括头尾，这个listview还有headview 所以=lastitemIndex的时候加载更多的item显示在最底端
        if (lastitemIndex == teachVideoAdapter.getCount()-1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            Log.i("tan8", "加载更多");
            startIndex += requestCount;
            footer_tv.setText("正在加载");
            progressBar.setVisibility(View.VISIBLE);
            updataPage(startIndex,startIndex+requestCount,false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastitemIndex = firstVisibleItem + visibleItemCount - 1 -1;
//        Log.i("tan8","lastitemIndex:"+lastitemIndex);
    }

    private boolean isPull;

    public void updataPage(int fromIndex, int toIndex, boolean isPull_) {

        new SendHttpThreadGet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if (result == null || result.equals("")) {
                    Log.i("tan8", "get class failed");
                    teachSwipeRefreshLayout.setRefreshing(false);
                    return;
                }

                Log.i("tan8", "invatationslistjson:" + result);

                if (!isPull) {
                    progressBar.setVisibility(View.GONE);
                    if (result.equals("[]")) {
                        footer_tv.setText("已全部加载");
                    } else {
                        footer_tv.setText("加载更多");
                    }
                    teachVideoAdapter.notifyDataSetChanged();
                } else {
                    teachacircleLv.setAdapter(teachVideoAdapter);
                }
                parseJson(result);
                startIndex = list.size();
                teachSwipeRefreshLayout.setRefreshing(false);
            }
        }, CommonUtils.GET_INVATATIONLIST+"?classid="+classid, 0).start();

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
                    ClassVideo cVideo = new ClassVideo();
                    if (js.has("classenjoyvideoname")){
                        cVideo.setVideoName(js.getString("classenjoyvideoname"));
                    }
                    if (js.has("classenjoyvideopreviewimage")){
                        cVideo.setPreviewimageUrl(js.getString("classenjoyvideopreviewimage"));
                    }
                    if (js.has("classenjoyvideopath")){
                        String purl = js.getString("classenjoyvideopath");
                        cVideo.setVideoUrl(purl.replace("//", ""));
                    }
                    list.add(cVideo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
