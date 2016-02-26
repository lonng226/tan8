package lonng.com.tan8.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lonng.com.tan8.EditActivity;
import lonng.com.tan8.EditActivity.OnclickOfButton;
import lonng.com.tan8.R;

public class BankDialog extends Dialog implements View.OnClickListener {

	private TextView b1,b2,b3,b4;
	private Context context;

	private EditActivity.OnclikBank clikbank;


	public BankDialog(Context context, EditActivity.OnclikBank clikbank) {
		super(context);
		this.context = context;
		this.clikbank = clikbank;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bankdialog);
		b1 = (TextView)findViewById(R.id.bankdialog_b1);
		b2 = (TextView)findViewById(R.id.bankdialog_b2);
		b3 = (TextView)findViewById(R.id.bankdialog_b3);
		b4 = (TextView)findViewById(R.id.bankdialog_b4);

		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);



	}
	
	


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bankdialog_b1:
			clikbank.clikBank(0);
			this.dismiss();
			break;
		case R.id.bankdialog_b2:
			clikbank.clikBank(1);
			this.dismiss();
			break;
		case R.id.bankdialog_b3:
			clikbank.clikBank(2);
			this.dismiss();
			break;
		case R.id.bankdialog_b4:
			clikbank.clikBank(3);
			this.dismiss();
			break;
		default:
			break;
		}
	}
	
}
