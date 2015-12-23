package lonng.com.tan8.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import lonng.com.tan8.EditActivity;
import lonng.com.tan8.EditActivity.OnclickOfButton;
import lonng.com.tan8.R;

public class CustomDialog extends Dialog implements View.OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel, btn_text,btn_video;
	private RelativeLayout layout;
	private Context context;
	private int where;
	private OnclickOfButton onclickbtn;

	public CustomDialog(Context context,int where,OnclickOfButton onclickbtn) {
		super(context, R.style.MyDialogStyleBottom);
		this.context = context;
		this.where = where;
		this.onclickbtn = onclickbtn;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitydialog_layout);
		btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_text = (Button) findViewById(R.id.btn_take_text);
		btn_video = (Button) findViewById(R.id.btn_video);
		btn_take_photo.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_video.setOnClickListener(this);
		
		layout = (RelativeLayout) findViewById(R.id.pop_layout);
		layout.setOnClickListener(this);
		
		if (where == 1) {
			btn_text.setVisibility(View.INVISIBLE);
		}else{
			btn_text.setOnClickListener(this);
		}

	}
	
	


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_take_photo:
			StartToEditActivity(1); 
			break;
		case R.id.btn_pick_photo:
			StartToEditActivity(2); 
			break;
		case R.id.btn_take_text:
			StartToEditActivity(0); 
			break;
		case R.id.btn_video:
			StartToEditActivity(3); 
			break;
		case R.id.btn_cancel:
			this.dismiss();
			break;
		case R.id.pop_layout:
			this.dismiss();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	private void StartToEditActivity(int type){
		if (onclickbtn != null) {
			onclickbtn.onclick(type);
		}else{
			Intent intent = new Intent(context, EditActivity.class);
			intent.putExtra("type", type);
			context.startActivity(intent);
		}
		this.dismiss();
	}

}
