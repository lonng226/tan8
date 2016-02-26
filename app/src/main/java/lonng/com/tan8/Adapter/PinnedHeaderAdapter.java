// @author Bhavya Mehta
package lonng.com.tan8.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import lonng.com.tan8.R;
import lonng.com.tan8.view.IPinnedHeader;
import lonng.com.tan8.view.PinnedHeaderListView;

// Customized adaptor to populate data in PinnedHeaderListView
public class PinnedHeaderAdapter extends BaseAdapter implements OnScrollListener, IPinnedHeader{

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SECTION = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SECTION + 1;

	LayoutInflater mLayoutInflater;
	int mCurrentSectionPosition = 0, mNextSectionPostion = 0;

	// array list to store section positions
	ArrayList<Integer> mListSectionPos;

	// array list to store list view data
	ArrayList<String> mListItems;

	ArrayList<String> mListContent;

	// context object
	Context mContext;
	Map<String,String> map;

	public PinnedHeaderAdapter(Context context, ArrayList<String> listItems,ArrayList<Integer> listSectionPos
			,Map<String,String> map,ArrayList<String> mListContent) {
		this.mContext = context;
		this.mListItems = listItems;
		this.mListSectionPos = listSectionPos;
		this.map = map;
		this.mListContent = mListContent;

		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mListItems.size();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return !mListSectionPos.contains(position);
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return mListSectionPos.contains(position) ? TYPE_SECTION : TYPE_ITEM;
	}

	public Object getItem(int position) {
		return mListItems.get(position);
	}

	public long getItemId(int position) {
		return mListItems.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			int type = getItemViewType(position);

			switch (type) {
			case TYPE_ITEM:
				convertView = mLayoutInflater.inflate(R.layout.row_view, null);
				holder.rowContent = (TextView)convertView.findViewById(R.id.row_content);
				break;
			case TYPE_SECTION:
				convertView = mLayoutInflater.inflate(R.layout.section_row_view, null);
				break;
			}
			holder.textView = (TextView) convertView.findViewById(R.id.row_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(mListItems.get(position).toString());
		if (getItemViewType(position) == TYPE_ITEM){
			holder.rowContent.setText(mListContent.get(position));
		}
		return convertView;
	}

	public int getPinnedHeaderState(int position) {
		// hide pinned header when items count is zero OR position is less than
		// zero OR
		// there is already a header in list view
		if (getCount() == 0 || position < 0 || mListSectionPos.indexOf(position) != -1) {
			return PINNED_HEADER_GONE;
		}

		// the header should get pushed up if the top item shown
		// is the last item in a section for a particular letter.
		mCurrentSectionPosition = getCurrentSectionPosition(position);
		mNextSectionPostion = getNextSectionPosition(mCurrentSectionPosition);
		if (mNextSectionPostion != -1 && position == mNextSectionPostion - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}

		return PINNED_HEADER_VISIBLE;
	}

	//获取当前分类名称的item的postion
	public int getCurrentSectionPosition(int position) {
		String listChar = mListItems.get(position).toString();
		String kind = map.get(listChar);
//				.substring(0, 1).toUpperCase(Locale.getDefault());
		return mListItems.indexOf(kind);
	}

	//获取当前分类的下一个分类的postion
	public int getNextSectionPosition(int currentSectionPosition) {
		int index = mListSectionPos.indexOf(currentSectionPosition);
		if ((index + 1) < mListSectionPos.size()) {
			return mListSectionPos.get(index + 1);
		}
		return mListSectionPos.get(index);
	}

	public void configurePinnedHeader(View v, int position) {
		// set text in pinned header
		TextView header = (TextView) v;
		mCurrentSectionPosition = getCurrentSectionPosition(position);
		header.setText(mListItems.get(mCurrentSectionPosition));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		Log.i("tan8", "scroll");
		if (view instanceof PinnedHeaderListView) {
			Log.i("tan8","scroll");
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	public static class ViewHolder {
		public TextView textView,rowContent;
	}
}
