package lonng.com.tan8.page;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BasePage;

/**
 * Created by lonng on 15/12/8.
 */
public class ApprecPage extends BasePage {

    @Bind(R.id.apprec_list)
    ListView apprec_list;
    private ArrayList<String> list;

    public ApprecPage(Context context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_apprec, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        readyData();
        apprec_list.setAdapter(new ApprecAdapter(ct, list));

    }

    private void readyData() {
        list = new ArrayList<String>();
        for (int i = 0; i <= 20; i++) {
            list.add("第" + i + "个Item");
        }
    }

    class ApprecAdapter extends BaseAdapter {

        private ArrayList<String> apprecList;
        private Context mct;


        public ApprecAdapter(Context context, ArrayList<String> objects) {
            super();
            apprecList = objects;
            mct = context;
        }

        @Override
        public int getCount() {
            return apprecList.size();
        }

        @Override
        public Object getItem(int position) {
            return apprecList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mct, R.layout.item_apperc, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView
                        .findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(apprecList.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }
}
