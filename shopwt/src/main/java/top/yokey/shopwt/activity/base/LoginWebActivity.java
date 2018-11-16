package top.yokey.shopwt.activity.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.base.base.BaseAnimClient;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseToast;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class LoginWebActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private ContentLoadingProgressBar mainProgressBar;
    private WebView mainWebView;

    private long exitTimeLong;

    @Override
    public void initView() {

        setContentView(R.layout.activity_base_browser);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainProgressBar = findViewById(R.id.mainProgressBar);
        mainWebView = findViewById(R.id.mainWebView);

    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void initData() {

        String url = getIntent().getStringExtra(BaseConstant.DATA_URL);
        if (TextUtils.isEmpty(url)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        BaseApplication.get().setWebView(mainWebView);
        BaseApplication.get().getCookieManager().removeAllCookie();
        BaseApplication.get().getCookieManager().removeSessionCookie();
        BaseApplication.get().getCookieManager().removeExpiredCookie();

        exitTimeLong = 0L;
        mainWebView.loadUrl(url);
        setToolbar(mainToolbar, "加载中...");

    }

    @Override
    public void initEven() {

        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String cookie = BaseApplication.get().getCookieManager().getCookie(url);
                if (cookie != null) {
                    if (cookie.contains("key=")) {
                        cookie = cookie.substring(cookie.indexOf("key=") + 4, cookie.length());
                        String key = cookie.substring(0, cookie.indexOf(";"));
                        Intent intent = new Intent();
                        intent.putExtra(BaseConstant.DATA_KEY, key);
                        setResult(RESULT_OK, intent);
                        BaseApplication.get().finish(getActivity());
                        return;
                    }
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            @SuppressWarnings("EmptyMethod")
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        mainWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                mainProgressBar.setProgress(progress);
                if (progress == 100) {
                    if (mainProgressBar.getVisibility() == View.VISIBLE) {
                        BaseAnimClient.get().goneAlpha(mainProgressBar);
                    }
                    mainProgressBar.setVisibility(View.GONE);
                } else {
                    if (mainProgressBar.getVisibility() == View.GONE) {
                        BaseAnimClient.get().showAlpha(mainProgressBar);
                    }
                    mainProgressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, progress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setToolbar(mainToolbar, title);
            }
        });

    }

    @Override
    public void onReturn() {

        if (System.currentTimeMillis() - exitTimeLong > BaseConstant.TIME_EXIT) {
            BaseToast.get().showReturnOneMoreTime();
            exitTimeLong = System.currentTimeMillis();
        } else {
            super.onReturn();
        }

    }

}
