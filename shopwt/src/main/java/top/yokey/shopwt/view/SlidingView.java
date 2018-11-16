package top.yokey.shopwt.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import top.yokey.shopwt.R;

/**
 * 滑动删除控件
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class SlidingView extends HorizontalScrollView {

    private int scrollWidth;
    private Boolean oneBoolean = false;
    private Boolean openBoolean = false;
    private AppCompatTextView deleteTextView;
    private OnSlidingListener onSlidingListener;

    public SlidingView(Context context) {
        this(context, null);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!oneBoolean) {
            deleteTextView = (AppCompatTextView) findViewById(R.id.mainTextView);
            oneBoolean = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
            scrollWidth = deleteTextView.getWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                onSlidingListener.onMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollX();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldL, int oldT) {
        super.onScrollChanged(l, t, oldL, oldT);
        deleteTextView.setTranslationX(l - scrollWidth);
    }

    public void changeScrollX() {
        if (getScrollX() >= (scrollWidth / 2)) {
            this.smoothScrollTo(scrollWidth, 0);
            openBoolean = true;
            onSlidingListener.onOpen(this);
        } else {
            this.smoothScrollTo(0, 0);
            openBoolean = false;
        }
    }

    public void closeMenu() {
        if (openBoolean) {
            this.smoothScrollTo(0, 0);
            openBoolean = false;
        }
    }

    public void setSlidingListener(OnSlidingListener listener) {

        onSlidingListener = listener;

    }

    public interface OnSlidingListener {

        void onOpen(View view);

        void onMove(SlidingView slidingView);

    }

}
