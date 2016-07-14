package lonng.com.tan8.page;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.Adapter.BankAdapter;
import lonng.com.tan8.Adapter.CzxAdapter;
import lonng.com.tan8.Adapter.QupuAdapter;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.Qupu;
import lonng.com.tan8.ImagePagerActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.control.CirclePublicCommentContral;
import lonng.com.tan8.control.CirclePublicCommentContralBank;
import lonng.com.tan8.control.SwpipeListViewOnScrollListener;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.utils.CommonUtils;

/**
 * Created by lonng on 15/12/8.
 */
public class ExamPage extends BasePage implements SwipeRefreshLayout.OnRefreshListener{


    @Bind(R.id.qupuRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.qupu_circleLv)
    ListView qupuLv;

    List<Qupu> qupus ;
    QupuAdapter qupuAdapter;

    public ExamPage(Context context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_exam, null);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);



        qupuLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImagePagerActivity.imageSize = new ImageSize(400, 600);
                ImagePagerActivity.startImagePagerActivity(ct, qupus.get(position).getPics(), position);
            }
        });


        return view;
    }


    @Override
    public void initData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getDatas();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onRefresh() {
         getDatas();
         mSwipeRefreshLayout.setRefreshing(true);
    }

    private void getDatas(){
        new SendHttpThreadGet(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result =(String) msg.obj;
                mSwipeRefreshLayout.setRefreshing(false);
                if (result == null || result.equals("")){
                    return;
                }
                Log.i("tan8","qupu:"+result);
                Parse(result);
                if (qupuAdapter == null) {
                    qupuAdapter = new QupuAdapter(ct, qupus);
                }else {
                    qupuAdapter.setQupuList(qupus);
                }

                qupuLv.setAdapter(qupuAdapter);


            }
        },CommonUtils.QUPU,0).start();
    }

    private void Parse(String result){


        if (qupus == null){
            qupus = new ArrayList<Qupu>();
        }
        qupus.clear();

        try {
            JSONArray jsa = new JSONArray(result);
            for (int i = 0;i<jsa.length();i++){
                JSONObject json = (JSONObject)jsa.get(i);
                Qupu qupu = new Qupu();
                if (json.has("id")){
                    qupu.setId(json.getString("id"));
                }
                if (json.has("type")){
                    qupu.setType(json.getString("type"));
                }
                if (json.has("name")){
                    qupu.setName(json.getString("name"));
                }
                if (json.has("description")){
                    qupu.setDescription(json.getString("description"));
                }
                if (json.has("path")){
                    qupu.setPath(json.getString("path"));
                }

                List<String> pics = new ArrayList<String>();
                pics.add(qupu.getPath());
                qupu.setPics(pics);

                qupus.add(qupu);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
