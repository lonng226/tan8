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


public class ZYFragment extends BaseFragment {

    @Bind(R.id.testText)
   TextView testText;
    private MainActivity mainActivity;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_test, null);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mainActivity = (MainActivity) ct;
        mainActivity.titlename.setText("筝乐");
        testText.setText("（1）考级\n" +
                "（2）流行\n" +
                "（3）传统\n" +
                "（4）儿歌");
    }

    @Override
    protected void processClick(View v) {

    }
}
