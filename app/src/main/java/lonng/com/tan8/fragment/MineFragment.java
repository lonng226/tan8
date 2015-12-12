package lonng.com.tan8.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BaseFragment;


public class MineFragment extends BaseFragment {

    @Bind(R.id.testText)
    TextView testText;
    private MainActivity mainActivity;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_test, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mainActivity = (MainActivity) ct;
        mainActivity.titlename.setText("我的");
        testText.setText("（1）关注\n" +
                "（2）粉丝\n" +
                "（3）发布的帖子\n" +
                "（4）收藏");
    }

    @Override
    protected void processClick(View v) {

    }
}
