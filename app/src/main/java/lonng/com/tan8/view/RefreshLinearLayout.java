package lonng.com.tan8.view;

import android.content.ContentResolver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import lonng.com.tan8.R;

/**
 * Created by Administrator on 2015/12/25.
 */
public class RefreshLinearLayout extends LinearLayout{



    private final static int RELEASE_TO_REFRESH = 0;
    private final static int PULL_TO_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;
    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;
    private Animation mLoadingAnimation;

    private Scroller scroller;
    private LinearLayout headView;
    private int headContentWidth;
    private int headContentHeight;

    private ImageView refreshIndicatorView;
    private int refreshTargetTop = -140;
    private ImageView bar;
    private TextView downTextView;
    private TextView timeTextView;

    private OnRefreshListener refreshListener;

    private Long refreshTime = null;
    private int lastY;
    // 拉动标记
    private boolean isDragging = false;
    // 是否可刷新标记
    private boolean isRefreshEnabled = true;
    // 在刷新中标记
    public boolean isRefreshing = false;
    private Context mContext;




    public RefreshLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public RefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        // 滑动对象，
        scroller = new Scroller(mContext);

        // 刷新视图顶端的的view
        headView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pull_refresh_header, null);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        // 指示器view
        refreshIndicatorView = (ImageView) headView.findViewById(R.id.refreshing_header_iv_arrow);
        // 刷新bar
        bar = (ImageView) headView.findViewById(R.id.refreshing_header_iv_loading);
        // 下拉显示text
        downTextView = (TextView) headView.findViewById(R.id.refreshing_header_htv_title);
        // 下来显示时间
        timeTextView = (TextView) headView.findViewById(R.id.refreshing_header_htv_time);

        LayoutParams lp = new LayoutParams( LayoutParams.FILL_PARENT, -refreshTargetTop);
        lp.topMargin = refreshTargetTop;
        lp.gravity = Gravity.TOP;
        addView(headView, lp);


        downTextView.setText("下拉刷新");
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String date = format.format(new Date());
        timeTextView.setText("最后刷新："+date);


        mLoadingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading);

        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录下y坐标
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
//                Logger.d("ScrollView  ACTION_MOVE");
                // y移动坐标
                int m = y - lastY;
                if (((m < 6) && (m > -1)) || (!isDragging)) {
                    doMovement(m);
                }
                // 记录下此刻y坐标
                this.lastY = y;
                break;

            case MotionEvent.ACTION_UP:
//                Logger.d("ScrollView  ACTION_UP");
                fling();
                break;
        }
        return true;
    }

    /**
     * up事件处理
     */
    private void fling() {
        LayoutParams lp = (LayoutParams) headView.getLayoutParams();
//        Logger.d("fling()" + lp.topMargin);
        if (lp.topMargin > 0) {// 拉到了触发可刷新事件
            refresh();
        } else {
            returnInitState();
        }
    }

    private void returnInitState() {
        LayoutParams lp = (LayoutParams) this.headView.getLayoutParams();
        int i = lp.topMargin;
        scroller.startScroll(0, i, 0, refreshTargetTop);
        invalidate();
    }

    private void refresh() {
        LayoutParams lp = (LayoutParams) this.headView .getLayoutParams();
        int i = lp.topMargin;
        refreshIndicatorView.clearAnimation();
        refreshIndicatorView.setVisibility(View.GONE);
        bar.clearAnimation();
        bar.startAnimation(mLoadingAnimation);
        bar.setVisibility(View.VISIBLE);
        timeTextView.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH：mm");
        String date = format.format(new Date());
        timeTextView.setText("最后刷新："+date);


        downTextView.setVisibility(View.VISIBLE);
        downTextView.setText("正在刷新...");
        scroller.startScroll(0, i, 0, 0 - i);
        invalidate();
        if (refreshListener != null) {
            refreshListener.onRefresh(this);
            isRefreshing = true;
        }
    }

    /**
     *
     */
    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (scroller.computeScrollOffset()) {
            int i = this.scroller.getCurrY();
            LayoutParams lp = (LayoutParams) this.headView.getLayoutParams();
            int k = Math.max(i, refreshTargetTop);
            lp.topMargin = k;
            this.headView.setLayoutParams(lp);
            this.headView.invalidate();
            invalidate();
        }
    }

    /**
     * 下拉move事件处理
     *
     * @param moveY
     */
    private void doMovement(int moveY) {
        // TODO Auto-generated method stub
        LayoutParams lp = (LayoutParams) headView.getLayoutParams();
        if (moveY > 0) {
            // 获取view的上边距
            float f1 = lp.topMargin;
            float f2 = moveY * 0.3F;
            int i = (int) (f1 + f2);
            // 修改上边距
            lp.topMargin = i;
            // 修改后刷新
            headView.setLayoutParams(lp);
            headView.invalidate();
            invalidate();
        }
        timeTextView.setVisibility(View.VISIBLE);
        downTextView.setVisibility(View.VISIBLE);

        refreshIndicatorView.setVisibility(View.VISIBLE);




        bar.setVisibility(View.GONE);
        if (lp.topMargin > 0) {
            downTextView.setText("松开刷新");
            refreshIndicatorView.setVisibility(View.VISIBLE);
            refreshIndicatorView.clearAnimation();
            refreshIndicatorView.startAnimation(animation);
            refreshIndicatorView.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
            refreshIndicatorView.clearAnimation();
            refreshIndicatorView.startAnimation(animation);
            bar.clearAnimation();



        } else {
            downTextView.setText("下来刷新");
            bar.setVisibility(View.GONE);
            refreshIndicatorView.clearAnimation();
            bar.clearAnimation();
            refreshIndicatorView.setImageResource(R.mipmap.ic_common_droparrow);
        }

    }

    public void setRefreshEnabled(boolean b) {
        this.isRefreshEnabled = b;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.refreshListener = listener;
    }

    /**
     * 结束刷新事件
     */
    public void finishRefresh() {
//        Logger.d("执行了=====finishRefresh");
        LayoutParams lp = (LayoutParams) this.headView.getLayoutParams();
        int i = lp.topMargin;
        timeTextView.setVisibility(View.VISIBLE);

        scroller.startScroll(0, 0, 0, refreshTargetTop);

        invalidate();
        isRefreshing = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH：mm");
        String date = format.format(new Date());
        timeTextView.setText("最后刷新："+date);

    }

    /*
     * 该方法一般和ontouchEvent 一起用 (non-Javadoc)
     *
     * @see
     * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        // TODO Auto-generated method stub
        int action = e.getAction();
        int y = (int) e.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                // y移动坐标
                int m = y - lastY;

                // 记录下此刻y坐标
                this.lastY = y;
                if (m > 6 && canScroll()) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return false;
    }

    private boolean canScroll() {
        View childView;
        if (getChildCount() > 1) {
            childView = this.getChildAt(1);
            if (childView instanceof ListView) {
                int top = ((ListView) childView).getChildAt(0).getTop();
                int pad = ((ListView) childView).getListPaddingTop();
                if ((Math.abs(top - pad)) < 3
                        && ((ListView) childView).getFirstVisiblePosition() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (childView instanceof ScrollView) {
                if (((ScrollView) childView).getScrollY() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 刷新监听接口
     *
     * @author Nono
     *
     */
    public interface OnRefreshListener {
        public void onRefresh(RefreshLinearLayout view);
    }
}
