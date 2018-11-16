package top.yokey.shopwt.view.slide;

import android.app.Activity;

import java.util.LinkedList;

/**
 * 滑动返回
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
final class ActivityStackManager {

    private static LinkedList<Activity> mActivityStack = new LinkedList<>();

    public static Activity getPreviousActivity(Activity curActivity) {
        final LinkedList<Activity> activities = mActivityStack;
        Activity preActivity = null;
        for (int cur = activities.size() - 1; cur >= 0; cur--) {
            Activity activity = activities.get(cur);
            if (activity == curActivity) {
                int pre = cur - 1;
                if (pre >= 0) {
                    preActivity = activities.get(pre);
                }
            }
        }
        return preActivity;
    }

    public static synchronized void removeFromStack(Activity activity) {

        mActivityStack.remove(activity);

    }

    public static synchronized void addToStack(Activity activity) {
        mActivityStack.remove(activity);
        mActivityStack.add(activity);
    }

    public static synchronized void clearTask() {
        int size = mActivityStack.size();
        if (size > 0) {
            Activity[] activities = new Activity[size];
            mActivityStack.toArray(activities);
            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }

    public static synchronized Activity[] getActivityStack() {
        Activity[] activities = new Activity[mActivityStack.size()];
        return mActivityStack.toArray(activities);
    }

}
