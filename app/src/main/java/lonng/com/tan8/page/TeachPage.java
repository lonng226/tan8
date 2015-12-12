package lonng.com.tan8.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BasePage;

/**
 * Created by lonng on 15/12/8.
 */
public class TeachPage extends BasePage {


    public TeachPage(Context context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_teach, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {

    }
}
