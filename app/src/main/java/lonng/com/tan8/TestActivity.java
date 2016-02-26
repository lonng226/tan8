package lonng.com.tan8;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.view.HandyRefreshListView;
import lonng.com.tan8.view.RefreshLinearLayout;

/**
 * Created by Administrator on 2015/12/24.
 */
public class TestActivity extends Activity{

    @Bind(R.id.ed)
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);


    }

}
