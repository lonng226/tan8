package lonng.com.tan8;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
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
        bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
}
