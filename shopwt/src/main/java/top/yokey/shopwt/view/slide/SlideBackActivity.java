package top.yokey.shopwt.view.slide;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;

/**
 * 滑动返回
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public abstract class SlideBackActivity extends ActivityInterfaceImpl implements SlideFrameLayout.SlidingListener {

    private float mBackPreviewViewInitOffset;
    private boolean mSlideable = true;
    private boolean mPreviousActivitySlideFollow = true;
    private boolean mInterceptFinish = false;
    private Activity mPreviousActivity;
    private boolean mNeedFindActivityFlag = true;
    private boolean mNeedFinishActivityFlag = false;
    private SlideFrameLayout mSlideFrameLayout;

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl() {
        @Override
        public void onActivityDestroyed(Activity activity) {
            super.onActivityDestroyed(activity);
            onPreviousActivityDestroyed(activity);
        }
    };

    private Runnable mFinishTask = new Runnable() {
        @Override
        public void run() {
            doRealFinishForSlide();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        if (mSlideable) {
            View previewView = getPreviousActivityContentView();
            if (previewView == null) {
                mSlideable = false;
            }
        }
        if (!mSlideable) {
            super.setContentView(view);
            return;
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mBackPreviewViewInitOffset = -(1.f / 3) * metrics.widthPixels;
        mSlideFrameLayout = new SlideFrameLayout(this);
        int size = ViewGroup.LayoutParams.MATCH_PARENT;
        SlideFrameLayout.LayoutParams params = new SlideFrameLayout.LayoutParams(size, size);
        mSlideFrameLayout.addView(view, params);
        mSlideFrameLayout.setShadowResource(R.drawable.ic_slide_shadow);
        mSlideFrameLayout.setSlideable(mSlideable);
        mSlideFrameLayout.setSlidingListener(this);
        super.setContentView(mSlideFrameLayout);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (slideOffset <= 0) {
            mInterceptFinish = false;
            offsetPreviousSnapshot(0);
        } else if (slideOffset < 1) {
            mInterceptFinish = true;
            offsetPreviousSnapshot(mBackPreviewViewInitOffset * (1 - slideOffset));
        } else {
            mInterceptFinish = false;
            offsetPreviousSnapshot(0);
            mNeedFinishActivityFlag = true;
            mSlideFrameLayout.postDelayed(mFinishTask, 500);
        }
    }

    @Override
    public void continueSettling(View panel, boolean settling) {
        if (mNeedFinishActivityFlag && !settling) {
            mSlideFrameLayout.removeCallbacks(mFinishTask);
            doRealFinishForSlide();
        }
    }

    @Override
    public void finish() {
        if (!mInterceptFinish) {
            super.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }

    public void setShadowResource(int resId) {
        if (mSlideFrameLayout != null) {
            mSlideFrameLayout.setShadowResource(resId);
        }
    }

    public void onSlideBack() {

    }

    public boolean isSlideable() {

        return mSlideable;

    }

    public void setSlideable(boolean slideable) {
        mSlideable = slideable;
        if (mSlideFrameLayout != null) {
            mSlideFrameLayout.setSlideable(slideable);
        }
    }

    public void setPreviousActivitySlideFollow(boolean flag) {

        mPreviousActivitySlideFollow = flag;

    }

    private void doRealFinishForSlide() {
        finish();
        overridePendingTransition(0, 0);
        onSlideBack();
    }

    private View getPreviousActivityContentView() {
        Activity previousActivity = getPreviousPreviewActivity();
        if (null != previousActivity) {
            return previousActivity.findViewById(android.R.id.content);
        }
        return null;
    }

    private Activity getPreviousPreviewActivity() {
        Activity previousActivity = mPreviousActivity;
        if (previousActivity != null && previousActivity.isFinishing()) {
            previousActivity = null;
            mPreviousActivity = null;
        }
        if (previousActivity == null && mNeedFindActivityFlag) {
            previousActivity = ActivityStackManager.getPreviousActivity(this);
            mPreviousActivity = previousActivity;
            if (null == previousActivity) {
                mNeedFindActivityFlag = false;
            }
            if (previousActivity instanceof ActivityInterface) {
                ((ActivityInterface) previousActivity).setActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            }
        }
        return previousActivity;
    }

    private void offsetPreviousSnapshot(float translateX) {
        View view = getPreviousActivityContentView();
        if (view == null) {
            throw new NullPointerException("NullPointerException");
        }
        if (view != null && mSlideFrameLayout != null) {
            if (!mPreviousActivitySlideFollow) {
                translateX = 0;
            }
            mSlideFrameLayout.offsetPreviousSnapshot(view, translateX);
        }
    }

    private void onPreviousActivityDestroyed(Activity activity) {
        if (activity == mPreviousActivity) {
            release();
            mPreviousActivity = getPreviousPreviewActivity();
            if (null == mPreviousActivity) {
                mNeedFindActivityFlag = false;
                setSlideable(false);
            }
        }
    }

    private void release() {
        if (mPreviousActivity != null) {
            if (mPreviousActivity instanceof ActivityInterface) {
                ((ActivityInterface) mPreviousActivity).setActivityLifecycleCallbacks(null);
            }
        }
        mPreviousActivity = null;
    }

}
