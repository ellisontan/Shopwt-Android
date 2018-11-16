package top.yokey.shopwt.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import top.yokey.base.base.BaseAnimClient;
import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseFileClient;
import top.yokey.base.base.BaseLogger;
import top.yokey.base.base.BaseShared;
import top.yokey.base.base.BaseToast;
import top.yokey.base.base.LocalAesClient;
import top.yokey.base.base.MemberHttpClient;
import top.yokey.base.base.SellerHttpClient;
import top.yokey.base.bean.ArticleBean;
import top.yokey.base.bean.MemberAssetBean;
import top.yokey.base.bean.MemberBean;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.base.BrowserActivity;
import top.yokey.shopwt.activity.base.LoginActivity;
import top.yokey.shopwt.activity.goods.BuyActivity;
import top.yokey.shopwt.activity.home.RedPacketActivity;
import top.yokey.shopwt.activity.home.RobBuyActivity;
import top.yokey.shopwt.activity.home.SpecialActivity;
import top.yokey.shopwt.activity.home.VoucherActivity;
import top.yokey.shopwt.activity.mine.SignActivity;
import top.yokey.shopwt.activity.order.PayActivity;
import top.yokey.shopwt.activity.goods.GoodsActivity;
import top.yokey.shopwt.activity.goods.ListActivity;
import top.yokey.shopwt.activity.home.ChatOnlyActivity;
import top.yokey.shopwt.activity.home.NoticeShowActivity;
import top.yokey.shopwt.activity.mine.BindMobileActivity;
import top.yokey.shopwt.activity.mine.FavoritesActivity;
import top.yokey.shopwt.activity.order.OrderActivity;
import top.yokey.shopwt.activity.store.GoodsListActivity;
import top.yokey.shopwt.activity.store.StoreActivity;
import top.yokey.shopwt.activity.store.StreetActivity;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;

import com.jiagu.sdk.BaseProtected;
import com.mob.MobSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 全局Applicaiton
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class BaseApplication extends Application {

    public IWXAPI iwxapi;
    private boolean isPush;
    private boolean isImage;
    private boolean isWxPay;
    private boolean isSuccess;

    private MemberBean memberBean;
    private CookieManager cookieManager;
    private MemberAssetBean memberAssetBean;
    private static BaseApplication instance;

    public static BaseApplication get() {

        return instance;

    }

    //原生方法

    private native String getLocalAesKey();

    private native String getServerAesKey();

    //重写方法

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        System.loadLibrary("native-lib");
        cookieManager = CookieManager.getInstance();
        BaseProtected.install(this);

        //加载框架
        x.Ext.init(this);
        MobSDK.init(this);
        JPushInterface.init(this);
        BaseToast.get().init(this);
        BaseDialog.get().init(this);
        BaseImageLoader.get().init(this);
        LocalAesClient.get().init(getLocalAesKey());
        BaseFileClient.get().init(getPackageName());
        BaseAnimClient.get().init(BaseConstant.TIME_ANIM);
        BaseLogger.get().init(this, BaseConstant.TAG_LOG);
        BaseShared.get().init(getSharedPreferences(BaseConstant.SHARED_NAME, MODE_PRIVATE));
        MemberHttpClient.get().init(this, BaseConstant.URL_API, BaseShared.get().getString(BaseConstant.SHARED_KEY), true);
        SellerHttpClient.get().init(this, BaseConstant.URL_API, BaseShared.get().getString(BaseConstant.SHARED_SELLER_KEY), true);

        //微信支付
        iwxapi = WXAPIFactory.createWXAPI(this, BaseConstant.WX_APP_ID);
        iwxapi.registerApp(BaseConstant.WX_APP_ID);

        //系统设置
        isPush = BaseShared.get().getBoolean(BaseConstant.SHARED_SETTING_PUSH, true);
        isImage = BaseShared.get().getBoolean(BaseConstant.SHARED_SETTING_IMAGE, true);

    }

    //账号相关

    public boolean isPush() {

        return isPush;

    }

    public boolean isImage() {

        return isImage;

    }

    public boolean isLogin() {

        return !TextUtils.isEmpty(BaseShared.get().getString(BaseConstant.SHARED_KEY));

    }

    public boolean isSellerLogin() {

        return !TextUtils.isEmpty(BaseShared.get().getString(BaseConstant.SHARED_SELLER_KEY));

    }

    public IWXAPI getIwxapi() {

        return iwxapi;

    }

    public MemberBean getMemberBean() {

        return memberBean;

    }

    public void setPush(boolean push) {

        isPush = push;

    }

    public void setImage(boolean image) {

        isImage = image;

    }

    public CookieManager getCookieManager() {

        return cookieManager;

    }

    public MemberAssetBean getMemberAssetBean() {

        return memberAssetBean;

    }

    public void setMemberBean(MemberBean memberBean) {

        this.memberBean = memberBean;

    }

    public void setMemberAssetBean(MemberAssetBean memberAssetBean) {

        this.memberAssetBean = memberAssetBean;

    }

    public void setIwxapi(IWXAPI iwxapi) {

        this.iwxapi = iwxapi;

    }

    public boolean isWxPay() {

        return isWxPay;

    }

    public void setWxPay(boolean wxPay) {

        isWxPay = wxPay;

    }

    public boolean isSuccess() {

        return isSuccess;

    }

    public void setSuccess(boolean success) {

        isSuccess = success;

    }

    //公共方法

    public int getWidth() {

        return getResources().getDisplayMetrics().widthPixels;

    }

    public int getHeight() {

        return getResources().getDisplayMetrics().heightPixels;

    }

    public int dipToSp(int dip) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dip, getResources().getDisplayMetrics());

    }

    public int dipToPx(int dip) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());

    }

    public int getColors(int id) {

        return ContextCompat.getColor(this, id);

    }

    public void setFocus(View view) {

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();

    }

    public void setWebView(WebView webView) {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(false);

    }

    public void hideKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getWindow().peekDecorView().getWindowToken(), 0);
        }

    }

    public void setFullScreen(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    public void setText2Clipboard(String content) {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", content);
        clipboardManager.setPrimaryClip(clipData);

    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {

        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.boy, R.color.girl, R.color.primaryAdd);

    }

    public void setTabLayout(TabLayout tabLayout, BaseViewPagerAdapter adapter, ViewPager viewPager) {

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setSelectedTabIndicatorColor(getColors(R.color.primary));
        tabLayout.setTabTextColors(getColors(R.color.greyAdd), getColors(R.color.primary));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    public void setRecyclerView(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {

        recyclerView.setFocusableInTouchMode(false);
        recyclerView.requestFocus();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    public void setRecyclerView(Activity activity, RecyclerView recyclerView, RecyclerView.Adapter adapter) {

        recyclerView.setFocusableInTouchMode(false);
        recyclerView.requestFocus();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0";
        }
    }

    public Drawable getMipmap(int id) {

        return ContextCompat.getDrawable(this, id);

    }

    public Drawable getMipmap(int id, int color) {

        Drawable mDrawable = ContextCompat.getDrawable(this, id);
        mDrawable = DrawableCompat.wrap(mDrawable);
        DrawableCompat.setTint(mDrawable, ContextCompat.getColor(this, color));

        return mDrawable;

    }

    public Drawable getMipmap(Drawable drawable, int color) {

        Drawable mDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(mDrawable, ContextCompat.getColor(this, color));

        return mDrawable;

    }

    public GradientDrawable getGradientDrawable(float radius, int color) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);

        return gradientDrawable;

    }

    public boolean isLaunchedActivity(@NonNull Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public boolean inActivityStackTop() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = manager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public boolean inActivityStack(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (componentName != null) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(componentName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public boolean inActivityStackTop(Class<?> cls) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    //App内置跳转

    public void startOrder(Activity activity, int position) {

        Intent intent = new Intent(activity, OrderActivity.class);
        intent.putExtra(BaseConstant.DATA_POSITION, position);
        startCheckLogin(activity, intent);

    }

    public void startBrowser(Activity activity, String url) {

        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra(BaseConstant.DATA_URL, url);
        start(activity, intent);

    }

    public void startGoods(Activity activity, String goodsId) {

        Intent intent = new Intent(activity, GoodsActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, goodsId);
        start(activity, intent);

    }

    public void startStore(Activity activity, String storeId) {

        Intent intent = new Intent(activity, StoreActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, storeId);
        start(activity, intent);

    }

    public void startCheckLogin(Activity activity, Class cls) {

        if (isLogin()) {
            start(activity, cls);
        } else {
            start(activity, LoginActivity.class);
        }

    }

    public void startOrderPay(Activity activity, String paySn) {

        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, paySn);
        start(activity, intent);

    }

    public void startCheckMobile(Activity activity, Class cls) {

        if (memberBean.isMobielState()) {
            start(activity, cls);
        } else {
            start(activity, BindMobileActivity.class);
        }

    }

    public void startFavorites(Activity activity, int position) {

        Intent intent = new Intent(activity, FavoritesActivity.class);
        intent.putExtra(BaseConstant.DATA_POSITION, position);
        startCheckLogin(activity, intent);

    }

    public void startCheckLogin(Activity activity, Intent intent) {

        if (isLogin()) {
            start(activity, intent);
        } else {
            start(activity, LoginActivity.class);
        }

    }

    public void startOrderSeller(Activity activity, int position) {

        Intent intent = new Intent(activity, top.yokey.shopwt.activity.seller.OrderActivity.class);
        intent.putExtra(BaseConstant.DATA_POSITION, position);
        startCheckSellerLogin(activity, intent);

    }

    public void startGoodsSeller(Activity activity, int position) {

        Intent intent = new Intent(activity, top.yokey.shopwt.activity.seller.GoodsActivity.class);
        intent.putExtra(BaseConstant.DATA_POSITION, position);
        startCheckSellerLogin(activity, intent);

    }

    public void startCheckSellerLogin(Activity activity, Class cls) {

        if (isSellerLogin()) {
            start(activity, cls);
        } else {
            start(activity, top.yokey.shopwt.activity.seller.LoginActivity.class);
        }

    }

    public void startCheckSellerLogin(Activity activity, Intent intent) {

        if (isSellerLogin()) {
            start(activity, intent);
        } else {
            start(activity, top.yokey.shopwt.activity.seller.LoginActivity.class);
        }

    }

    public void startNoticeShow(Activity activity, ArticleBean articleBean) {

        Intent intent = new Intent(activity, NoticeShowActivity.class);
        intent.putExtra(BaseConstant.DATA_BEAN, articleBean);
        start(activity, intent);

    }

    public void startTypeValue(Activity activity, String type, String value) {

        Intent intent = null;

        switch (type) {
            case "2":
                startGoods(activity, value);
                break;
            case "url":
                switch (value) {
                    case "html/member/signin.html":
                        startCheckLogin(activity, SignActivity.class);
                        break;
                    case "html/product_robbuy.html":
                        intent = new Intent(activity, RobBuyActivity.class);
                        intent.putExtra(BaseConstant.DATA_POSITION, 1);
                        start(activity, intent);
                        break;
                    case "html/product_dazhe.html":
                        intent = new Intent(activity, RobBuyActivity.class);
                        intent.putExtra(BaseConstant.DATA_POSITION, 0);
                        start(activity, intent);
                        break;
                    case "html/coupon_list.html":
                        intent = new Intent(activity, VoucherActivity.class);
                        intent.putExtra(BaseConstant.DATA_POSITION, 0);
                        start(activity, intent);
                        break;
                    case "html/voucher_list.html":
                        intent = new Intent(activity, VoucherActivity.class);
                        intent.putExtra(BaseConstant.DATA_POSITION, 1);
                        start(activity, intent);
                        break;
                    case "html/pointspro_list.html":
                        start(activity, top.yokey.shopwt.activity.points.ListActivity.class);
                        break;
                    case "shop.html":
                        start(activity, StreetActivity.class);
                        break;
                    case "html/product_list.html":
                        start(activity, ListActivity.class);
                        break;
                    case "html/red_packet.html?id=1":
                        intent = new Intent(activity, RedPacketActivity.class);
                        intent.putExtra(BaseConstant.DATA_ID, value.substring(value.indexOf("=") + 1, value.length()));
                        start(activity, intent);
                        break;
                    default:
                        startBrowser(activity, value);
                        break;
                }
                break;
            case "goods":
                startGoods(activity, value);
                break;
            case "special":
                intent = new Intent(activity, SpecialActivity.class);
                intent.putExtra(BaseConstant.DATA_ID, value);
                start(activity, intent);
                break;
            case "keyword":
                startGoodsList(activity, value, "", "");
                break;
        }

    }

    public void startGoodsBuy(Activity activity, String cartId, String ifCart) {

        Intent intent = new Intent(activity, BuyActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, cartId);
        intent.putExtra(BaseConstant.DATA_IFCART, ifCart);
        start(activity, intent);

    }

    public void startChatOnly(Activity activity, String memberId, String goodsId) {

        Intent intent = new Intent(activity, ChatOnlyActivity.class);
        intent.putExtra(BaseConstant.DATA_MEMBERID, memberId);
        intent.putExtra(BaseConstant.DATA_GOODSID, goodsId);
        startCheckLogin(activity, intent);

    }

    public void startGoodsList(Activity activity, String keyword, String bId, String gcId) {

        Intent intent = new Intent(activity, ListActivity.class);
        intent.putExtra(BaseConstant.DATA_KEYWORD, keyword);
        intent.putExtra(BaseConstant.DATA_GCID, gcId);
        intent.putExtra(BaseConstant.DATA_BID, bId);
        start(activity, intent);

    }

    public void startStoreGoodsList(Activity activity, String storeId, String keyword, String stcId) {

        Intent intent = new Intent(activity, GoodsListActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, storeId);
        intent.putExtra(BaseConstant.DATA_KEYWORD, keyword);
        intent.putExtra(BaseConstant.DATA_STCID, stcId);
        start(activity, intent);

    }

    //Activity跳转

    public void startHome(Activity activity) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        start(activity, intent);

    }

    public void startLocation(Activity activity) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        start(activity, intent);

    }

    public void start(Activity activity, Class cls) {

        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);

    }

    public void start(Activity activity, Intent intent) {

        activity.startActivity(intent);

    }

    public void startCall(Activity activity, String mobile) {

        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            start(activity, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void start(Activity activity, Class cls, int code) {

        Intent intent = new Intent(activity, cls);
        activity.startActivityForResult(intent, code);

    }

    public void startInstallApk(Activity activity, File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, getPackageName() + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        activity.startActivity(intent);

    }

    public void startMessage(Activity activity, String mobile) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobile));
        start(activity, intent);

    }

    public void start(Activity activity, Intent intent, int code) {

        activity.startActivityForResult(intent, code);

    }

    public void startMatisse(Activity activity, int number, int code) {

        Matisse.from(activity)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "top.yokey.shopwt.fileprovider"))
                .maxSelectable(number)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(code);

    }

    public void startApplicationSetting(Activity activity, String packageName) {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", packageName, null));
        start(activity, intent);

    }

    //Activity销毁

    public void finish(Activity activity) {

        activity.finish();

    }

    public void finishOk(Activity activity) {

        activity.setResult(Activity.RESULT_OK);
        finish(activity);

    }

    public void finishOk(Activity activity, Intent intent) {

        activity.setResult(Activity.RESULT_OK, intent);
        finish(activity);

    }

}
