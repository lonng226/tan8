package lonng.com.tan8.page;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lonng.com.tan8.BankActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.base.BasePage;

/**
 * Created by Administrator on 2015/12/16.
 */
public class BankPage extends BasePage{

    @Bind(R.id.banklistview)
    ListView blistview;
    private List<String> bankNames_;

    public BankPage(Context context){
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_bank,null);
        ButterKnife.bind(this, view);

        if(bankNames_ == null){
            bankNames_ = new ArrayList<String>();
        }

        bankNames_.add("show");
        bankNames_.add("成人学琴");
        bankNames_.add("求谱");
        bankNames_.add("初学问答");

        blistview.setAdapter(new BankAdapter(bankNames_,ct));
        blistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ct, BankActivity.class);
                intent.putExtra("bankId", position);
                ct.startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void initData() {

    }



    class BankAdapter extends BaseAdapter{


        private List<String> bankNames;
        private Context context;

        public BankAdapter(List<String> bankNames,Context context){

            this.bankNames = bankNames;
            this.context = context;
        }

        @Override
        public int getCount() {
            return bankNames.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return bankNames.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.banklist_item,null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
                viewHolder.bankNameTv = (TextView)convertView.findViewById(R.id.banklist_bankname);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.bankNameTv.setText(bankNames.get(position));
            return convertView;
        }

        class ViewHolder{
              TextView bankNameTv;
        }
    }



}
