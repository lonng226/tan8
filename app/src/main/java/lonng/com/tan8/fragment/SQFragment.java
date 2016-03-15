package lonng.com.tan8.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.base.BaseFragment;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.dialog.CustomDialog;
import lonng.com.tan8.page.BankPage;
import lonng.com.tan8.page.Czxpage;
import lonng.com.tan8.page.ZongbPage;


public class SQFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.community_sendt)
    TextView sendInvitation;
    @Bind(R.id.community_viewpager)
    ViewPager viewpager;
    @Bind(R.id.czx)
    RelativeLayout czx;
    @Bind(R.id.zongb)
    RelativeLayout zongb;
    @Bind(R.id.bank)
    RelativeLayout bank;
    private MainActivity mainActivity;
    private List<BasePage> pagelist = new ArrayList<BasePage>();
    private Czxpage czxPage;
    private ZongbPage zongbPage;
    private BankPage bankPage;


    @Bind(R.id.sqtitle)
    LinearLayout title2Layout;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_community, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
            mainActivity = (MainActivity) ct;
            mainActivity.titlename.setText("社区");
            if (pagelist .size()==0){
                czxPage = new Czxpage(ct);
                bankPage = new BankPage(ct);
                zongbPage = new ZongbPage(ct);
                pagelist.clear();
                pagelist.add(czxPage);
                pagelist.add(bankPage);
                pagelist.add(zongbPage);
        }

        viewpager.setAdapter(new SqAdapter(pagelist,ct));
        viewpager.setOffscreenPageLimit(0);
        czx.setOnClickListener(this);
        bank.setOnClickListener(this);
        zongb.setOnClickListener(this);

        mainActivity.setTitle2Layout(title2Layout);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePage page = pagelist.get(position);
                page.initData();
                switch (position) {
                    case 0:
                        czx.setBackgroundColor(Color.parseColor("#00ff00"));
                        bank.setBackgroundColor(Color.parseColor("#70005500"));
                        zongb.setBackgroundColor(Color.parseColor("#70005500"));
                        break;
                    case 1:
                        czx.setBackgroundColor(Color.parseColor("#70005500"));
                        bank.setBackgroundColor(Color.parseColor("#00ff00"));
                        zongb.setBackgroundColor(Color.parseColor("#70005500"));
                        break;
                    case 2:
                        czx.setBackgroundColor(Color.parseColor("#70005500"));
                        bank.setBackgroundColor(Color.parseColor("#70005500"));
                        zongb.setBackgroundColor(Color.parseColor("#00ff00"));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        czx.setBackgroundColor(Color.parseColor("#00ff00"));
        Log.i("tan8","3");
        viewpager.setCurrentItem(0);
        czxPage.initData();
        sendInvitation.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.community_sendt:
                new CustomDialog(mainActivity,0,null,czxPage.getSl()).show();
                break;
            case R.id.czx:
                viewpager.setCurrentItem(0);
                break;
            case R.id.bank:
                viewpager.setCurrentItem(1);
                break;
            case R.id.zongb:
                viewpager.setCurrentItem(2);
                break;
            default:
                break;

        }
    }

    class SqAdapter extends PagerAdapter{

        private List<BasePage> pages;
        private Context context;
        public SqAdapter(List<BasePage> pages ,Context context){
            this.pages = pages;
            this.context = context;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pages.get(position).getContentView(),0);
            return pages.get(position).getContentView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pages.get(position).getContentView());
        }
    }
}
