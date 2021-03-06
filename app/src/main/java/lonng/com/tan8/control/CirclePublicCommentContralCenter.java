package lonng.com.tan8.control;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import lonng.com.tan8.BankActivity;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;
import lonng.com.tan8.UserCenterActivity;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.view.AppNoScrollerListView;

/**
 * 
* @ClassName: CirclePublicCommentContral 
* @Description: 控制EdittextView的显示和隐藏，以及发布动作，根据回复的位置调整listview的位置
* @author yiw
* @date 2015-12-28 下午3:45:21 
*
 */
public class CirclePublicCommentContralCenter {
	private static final String TAG = CirclePublicCommentContralCenter.class.getSimpleName();
	private View mEditTextBody;
	private EditText mEditText;
	private View mSendBt;
	private CirclePresenter mCirclePresenter;
	private int mCirclePosition;
	private int mCommentType;
	private User mReplyUser;
	private int mCommentPosition;
	private ListView mListView;
	private Context mContext;

	private int Tid;
	/**
	 * 选择动态条目的高
	 */
	private int mSelectCircleItemH;
	/**
	 * 选择的commentItem距选择的CircleItem底部的距离
	 */
	private int mSelectCommentItemBottom;

	public ListView getmListView() {
		return mListView;
	}

	public void setmListView(ListView mListView) {
		this.mListView = mListView;
	}

	public CirclePublicCommentContralCenter(Context context, View editTextBody, EditText editText, View sendBt){
		mContext = context;
		mEditTextBody = editTextBody;
		mEditText = editText;
		mSendBt = sendBt;
		mSendBt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mCirclePresenter!=null){
					//发布评论
					mCirclePresenter.addComment(mCirclePosition, mCommentType, mReplyUser);
				}
				editTextBodyVisible(View.GONE);
			}
		});
	}

	/**
	 * 
	* @Title: editTextBodyVisible 
	* @Description: 评论时显示发布布局，评论完隐藏，根据不同位置调节listview的滑动
	* @param  visibility
	* @param  mCirclePresenter
	* @param  mCirclePosition
	* @param  commentType  0:发布评论   1：回复评论
	* @param  replyUser    
	* @return void    返回类型 
	* @throws
	 */
	public void editTextBodyVisible(int visibility, CirclePresenter mCirclePresenter, int mCirclePosition, int commentType, User replyUser, int commentPosition,int Tid) {
		this.mCirclePosition = mCirclePosition;
		this.mCirclePresenter = mCirclePresenter;
		this.mCommentType = commentType;
		this.mReplyUser = replyUser;
		this.mCommentPosition = commentPosition;
		this.Tid = Tid;
		editTextBodyVisible(visibility);

		measure(mCirclePosition, commentType);
	}

	private void measure(int mCirclePosition, int commentType) {
		if(mListView != null){
			int firstPosition = mListView.getFirstVisiblePosition();
			//此方法只能取到当前可见的item，就是当前可见的第一个item的index是0，但是这个item在listview中postion不一定是0，所以如下处理
			View selectCircleItem = mListView.getChildAt(mCirclePosition-firstPosition);
			mSelectCircleItemH = selectCircleItem.getHeight();

			if(commentType == ICircleViewUpdate.TYPE_REPLY_COMMENT){//回复评论的情况
				AppNoScrollerListView commentLv = (AppNoScrollerListView) selectCircleItem.findViewById(R.id.item_czx_listview);
				if(commentLv!=null){
					int firstCommentPosition = commentLv.getFirstVisiblePosition();
					//找到要回复的评论view,计算出该view距离所属动态底部的距离
					View selectCommentItem = commentLv.getChildAt(mCommentPosition - firstCommentPosition);
					if(selectCommentItem!=null){
						mSelectCommentItemBottom = 0;
						View parentView = selectCommentItem;
						do {
							int subItemBottom = parentView.getBottom();
							parentView = (View) parentView.getParent();
							if(parentView != null){
								mSelectCommentItemBottom += (parentView.getHeight() - subItemBottom);
							}
						} while (parentView != null && parentView != selectCircleItem);
					}
				}
			}
		}
	}

	public void handleListViewScroll() {
		int headHeight =  ((UserCenterActivity)mContext).getHeadHeight();
		int keyH = TanApplication.mKeyBoardH;//键盘的高度

		int editTextBodyH = ((UserCenterActivity)mContext).getEditTextBodyHeight();//整个EditTextBody的高度
		int screenlH = ((UserCenterActivity)mContext).getScreenHeight();//整个应用屏幕的高度

		int listviewOffset = screenlH - mSelectCircleItemH - keyH - editTextBodyH;
//		Log.d(TAG, "offset=" + listviewOffset + " &mSelectCircleItemH=" + mSelectCircleItemH + " &keyH=" + keyH + " &editTextBodyH=" + editTextBodyH);
		if(mCommentType == ICircleViewUpdate.TYPE_REPLY_COMMENT){
			listviewOffset = listviewOffset + mSelectCommentItemBottom;
		}
		if(mListView!=null){
			//将指定的item放到顶部，并且有个偏移量
			mListView.setSelectionFromTop(mCirclePosition, listviewOffset - headHeight);
		}

	}
	
	public void editTextBodyVisible(int visibility) {
		if(mEditTextBody!=null){
			mEditTextBody.setVisibility(visibility);
			if(View.VISIBLE==visibility){
				mEditText.requestFocus();
				//弹出键盘
				CommonUtils.showSoftInput(mEditText.getContext(), mEditText,2);
				
			}else if(View.GONE==visibility){
				//隐藏键盘
				CommonUtils.hideSoftInput(mEditText.getContext(), mEditText,2);
			}
		}
	}

	public String getEditTextString() {
		String result = "";
		if(mEditText!=null){
			result =  mEditText.getText().toString();
		}
		return result;
	}
	
	public void clearEditText(){
		if(mEditText!=null){
			mEditText.setText("");
		}
	}

}
