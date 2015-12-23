package lonng.com.tan8.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class HandyTextView extends TextView {

	public HandyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public HandyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HandyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		if (text == null) {
			text = "";
		}
		super.setText(text, type);
	}
}
