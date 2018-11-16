package top.yokey.shopwt.base;

import com.squareup.otto.Bus;

/**
 * OTTO
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class BaseBusClient extends Bus {

    private static volatile BaseBusClient instance;

    public static BaseBusClient get() {
        if (instance == null) {
            synchronized (BaseBusClient.class) {
                if (instance == null) {
                    instance = new BaseBusClient();
                }
            }
        }
        return instance;
    }

}
