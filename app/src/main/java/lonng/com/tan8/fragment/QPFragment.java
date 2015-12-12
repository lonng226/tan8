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
import lonng.com.tan8.page.ExamPage;
import lonng.com.tan8.page.PopuPage;
import lonng.com.tan8.page.TradPage;


public class QPFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.exam)
    RelativeLayout exam;
    @Bind(R.id.popu)
    RelativeLayout popu;
    @Bind(R.id.trad)
    RelativeLayout trad;
    @Bind(R.id.ktDetail)
    ViewPager qpDetail;
    private MainActivity mainActivity;
    private ExamPage examPage;
    private PopuPage popuPage;
    private TradPage tradPage;
    private ArrayList<BasePage> pages = new ArrayList<BasePage>();

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_qp, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mainActivity = (MainActivity) ct;
        mainActivity.titlename.setText("曲谱");
        examPage = new ExamPage(ct);
        popuPage = new PopuPage(ct);
        tradPage = new TradPage(ct);
        pages.clear();
        pages.add(examPage);
        pages.add(popuPage);
        pages.add(tradPage);
        qpDetail.setAdapter(new QPAdapter(ct, pages));
        qpDetail.setOffscreenPageLimit(0);
        exam.setOnClickListener(this);
        popu.setOnClickListener(this);
        trad.setOnClickListener(this);
        qpDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePage page = pages.get(position);
                page.initData();
                switch (position) {
                    case 0:
                        exam.setBackgroundColor(Color.parseColor("#00ff00"));
                        popu.setBackgroundColor(Color.parseColor("#70005500"));
                        trad.setBackgroundColor(Color.parseColor("#70005500"));
                        break;
                    case 1:
                        exam.setBackgroundColor(Color.parseColor("#70005500"));
                        popu.setBackgroundColor(Color.parseColor("#00ff00"));
                        trad.setBackgroundColor(Color.parseColor("#70005500"));
                        break;
                    case 2:
                        exam.setBackgroundColor(Color.parseColor("#70005500"));
                        popu.setBackgroundColor(Color.parseColor("#70005500"));
                        trad.setBackgroundColor(Color.parseColor("#00ff00"));
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
        exam.setBackgroundColor(Color.parseColor("#00ff00"));
        qpDetail.setCurrentItem(0);
    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exam:
                qpDetail.setCurrentItem(0);
                break;
            case R.id.popu:
                qpDetail.setCurrentItem(1);
                break;
            case R.id.trad:
                qpDetail.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    class QPAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<BasePage> pages;

        public QPAdapter(Context ct, ArrayList<BasePage> pages) {
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
