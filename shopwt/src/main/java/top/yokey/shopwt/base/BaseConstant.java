package top.yokey.shopwt.base;

/**
 * 系统常量
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class BaseConstant {

    public static final int CODE_AREA = 1000;
    public static final int CODE_ALBUM = 1001;
    public static final int CODE_CAMERA = 1002;
    public static final int CODE_QRCODE = 1003;

    public static final int CODE_LOGIN = 2000;
    public static final int CODE_CLASS = 2001;
    public static final int CODE_ADDRESS = 2002;
    public static final int CODE_INVOICE = 2003;

    public static final int TIME_EXIT = 1000;
    public static final int TIME_ANIM = 1000;
    public static final int TIME_DELAY = 10000;
    public static final long TIME_TICK = 1000L;
    public static final long TIME_COUNT = 2000L;
    public static final long TIME_MESSAGE = 5000L;
    public static final long TIME_REFRESH = 2000;
    public static final float DEFAULT_ANIM = 600f;

    public static final String DATA_ID = "id";
    public static final String DATA_SN = "sn";
    public static final String DATA_KEY = "key";
    public static final String DATA_URL = "url";
    public static final String DATA_BID = "bid";
    public static final String DATA_GCID = "gcid";
    public static final String DATA_BEAN = "bean";
    public static final String DATA_STCID = "stcid";
    public static final String DATA_IFCART = "ifcart";
    public static final String DATA_GOODSID = "goodsid";
    public static final String DATA_KEYWORD = "keyword";
    public static final String DATA_CONTENT = "content";
    public static final String DATA_MEMBERID = "memberid";
    public static final String DATA_POSITION = "position";

    public static final String PACK_NAME = "top.yokey.shopwt";

    public static final String TAG_LOG = "yokey_tag";
    public static final String SHARED_NAME = "yokey_shopnc";
    public static final String SHARED_KEY = "shared_key";
    public static final String SHARED_SELLER_KEY = "shared_seller_key";
    public static final String SHARED_SETTING_PUSH = "shared_settingd_push";
    public static final String SHARED_SETTING_IMAGE = "shared_setting_image";

    public static final String URL = "http://demo.shopwt.com/";
    public static final String URL_API = URL + "api/mobile/index.php";
    public static final String URL_LOGIN_WB = URL_API + "?w=connect&t=get_sina_oauth2";
    public static final String URL_STORE_INFO = URL + "mobile/html/store_intro.html?store_id=";
    public static final String URL_GOODS_DETAILED = URL + "mobile/html/product_detail.html?goods_id=";
    public static final String URL_LOGISTICS = "http://m.kuaidi100.com/result.jsp?nu=";

    //第三方

    public static final String WX_APP_ID = "wx67bc79b681fee1b3";
    public static final String BUGLY_APP_ID = "7efc96d9c8";

}
