package top.yokey.shopwt.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;

/**
 * 基础Receiver
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class BaseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (BaseApplication.get().isPush()) {
            String alert = intent.getStringExtra(JPushInterface.EXTRA_ALERT);
            String extra = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
            String message = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
        }

    }

}
