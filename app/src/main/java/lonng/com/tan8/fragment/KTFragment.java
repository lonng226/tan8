package lonng.com.tan8.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BaseFragment;
import lonng.com.tan8.base.BasePage;
import lonng.com.tan8.page.ApprecPage;
import lonng.com.tan8.page.TeachPage;


public class KTFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.appreciation)
    RelativeLayout appreciation;
    @Bind(R.id.teaching)
    RelativeLayout teaching;
    @Bind(R.id.ktDetail)
    ViewPager ktDetail;
    private MainActivity mainActivity;
    private ApprecPage apprecPage;
    private TeachPage teachPage;
    private ArrayList<BasePage> pages = new ArrayList<BasePage>();

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_kt, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mainActivity = (MainActivity) ct;
        mainActivity.titlename.setText("课堂");

        if (pages == null || pages.size()==0){
            apprecPage = new ApprecPage(ct);
            teachPage = new TeachPage(ct);
            pages.clear();
            pages.add(apprecPage);
            pages.add(teachPage);
        }

        ktDetail.setAdapter(new KTAdapter(ct, pages));
        ktDetail.setOffscreenPageLimit(0);
        appreciation.setOnClickListener(this);
        teaching.setOnClickListener(this);
        ktDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePage page = pages.get(position);
                page.initData();
                switch (position) {
                    case 0:
                        appreciation.setBackgroundColor(Color.parseColor("#00ff00"));
                        teaching.setBackgroundColor(Color.parseColor("#70005500"));
                        break;
                    case 1:
                        appreciation.setBackgroundColor(Color.parseColor("#70005500"));
                        teaching.setBackgroundColor(Color.parseColor("#00ff00"));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pages.get(0).initData();
        appreciation.setBackgroundColor(Color.parseColor("#00ff00"));
        ktDetail.setCurrentItem(0);

    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.appreciation:
                ktDetail.setCurrentItem(0);
                break;
            case R.id.teaching:
                ktDetail.setCurrentItem(1);
                break;
            default:
                break;
        }

    }

    class KTAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<BasePage> pages;

        public KTAdapter(Context ct, ArrayList<BasePage> pages) {
            this.mContext = ct;
            this.pages = pages;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pages.get(position).getContentView(), 0);
            return pages.get(position).getContentView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pages.get(position).getContentView());
        }
    }

}
