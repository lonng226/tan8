package lonng.com.tan8;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.base.BaseActivity;
import lonng.com.tan8.fragment.SQFragment;
import lonng.com.tan8.fragment.KTFragment;
import lonng.com.tan8.fragment.QPFragment;
import lonng.com.tan8.fragment.ZYFragment;
import lonng.com.tan8.fragment.ShowFragment;
import lonng.com.tan8.fragment.MineFragment;
//
public class MainActivity extends BaseActivity {


    @Bind(R.id.bottomBar)
    public RadioGroup bottomBar;
    @Bind(R.id.titlename)
    public TextView titlename;
    @Bind(R.id.menuBar)
    RelativeLayout menuBarLayout;
    @Bind(R.id.title)
    RelativeLayout titleLayout;
    @Bind(R.id.shequ)
    RadioButton shequ;
    @Bind(R.id.ketang)
    RadioButton ketang;
    @Bind(R.id.qupu)
    RadioButton qupu;
    @Bind(R.id.mine)
    RadioButton mine;
    @Bind(R.id.progress_layout)
    RelativeLayout progress_layout;


    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;

    private SQFragment SQFragment;
    private KTFragment KTFragment;
    private QPFragment QPFragment;
    private ZYFragment ZYFragment;
    private ShowFragment ShowFragment;
    private MineFragment MineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            SQFragment = new SQFragment();
            KTFragment = new KTFragment();
            QPFragment = new QPFragment();
            ZYFragment = new ZYFragment();
            ShowFragment = new ShowFragment();
            MineFragment = new MineFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, SQFragment, "MAIN").commit();
        }

        shequ.setBackgroundColor(this.getResources().getColor(R.color.top_bar_normal_bg));

        bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                setbtnbg(checkedId);
                switch (checkedId) {
                    case R.id.shequ:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, SQFragment, "MAIN").commit();
                        break;
                    case R.id.ketang:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, KTFragment, "MAIN").commit();
                        break;
                    case R.id.qupu:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, QPFragment, "MAIN").commit();
                        break;
                    case R.id.zhengyue:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, ZYFragment, "MAIN").commit();
                        break;
                    case R.id.show:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, ShowFragment, "MAIN").commit();
                        break;
                    case R.id.mine:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, MineFragment, "MAIN").commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public RelativeLayout getMenuBarView(){
        return menuBarLayout;
    }

    public RelativeLayout getTitleLayout(){
        return titleLayout;
    }

    LinearLayout title2Layout;
    public void setTitle2Layout(LinearLayout title2Layout){
        this.title2Layout = title2Layout;
    }

    public LinearLayout getTitle2Layout(){
        return title2Layout;
    }

    public RelativeLayout getProgressLayout(){
        return progress_layout;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onClick(View v) {

    }

    private List<RadioButton> radioButtons ;
    private void setbtnbg(int id){

        if (radioButtons == null) {
            radioButtons = new ArrayList<RadioButton>();
            radioButtons.add(shequ);
            radioButtons.add(ketang);
            radioButtons.add(qupu);
            radioButtons.add(mine);
        }

        for (int i=0;i<radioButtons.size();i++){
              RadioButton r = radioButtons.get(i);
            if (r.getId() == id){
                r.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.top_bar_normal_bg));
            }else {
                r.setBackgroundColor(0);
            }
        }

    }

}
