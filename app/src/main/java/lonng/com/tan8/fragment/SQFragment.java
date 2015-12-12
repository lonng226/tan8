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


public class SQFragment extends BaseFragment {

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
        mainActivity.titlename.setText("社区");
        testText.setText("(1）你问我答\n" +
                "（2）视频秀场\n" +
                "（3）成人学琴\n" +
                "（4）少儿考级\n" +
                "（5）艺术高考\n" +
                "（6）求谱制谱\n" +
                "（7）买琴晒琴\n" +
                "（8）流行歌曲");
    }

    @Override
    protected void processClick(View v) {

    }
}
