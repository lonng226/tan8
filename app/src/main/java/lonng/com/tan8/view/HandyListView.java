package lonng.com.tan8.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 *
 * @author Administrator
 *
 */
public abstract class HandyListView extends ListView implements OnScrollListener{
	
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected int mFirstVisbleItem;
	protected boolean mIsTop;
	protected boolean mIsBottom;
	
	protected Point mDownPoint;
	protected Point mUpPoint;
	protected Point mMovePoint;
	
	

	public HandyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HandyListView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HandyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HandyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(context);
		setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mFirstVisbleItem = firstVisibleItem;
		if (arg0.getFirstVisiblePosition() == 1) {
			mIsTop = true;
		}else if (arg0.getLastVisiblePosition() == arg0.getCount() - 1) {
			mIsBottom = true;
		}else{
			mIsTop = false;
			mIsBottom = false;
		}		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub;o
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		int x = 0;
		int y = 0;
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			x = (int) ev.getX();
			y = (int) ev.getY();
			mDownPoint = new Point(x,y);
			onDown(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			x = (int) ev.getX();
			y = (int) ev.getY();
			mMovePoint = new Point(x, y);
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			x = (int) ev.getX();
			y = (int) ev.getY();
			mUpPoint = new Point(x, y);
			onUp(ev);
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	

	public abstract void onDown(MotionEvent ev);
	
	public abstract void onMove(MotionEvent ev);

	public abstract void onUp(MotionEvent ev);
}
