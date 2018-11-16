package top.yokey.shopwt.activity.order;

import android.annotation.SuppressLint;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseToast;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class LogisticsActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private WebView mainWebView;
    private ContentLoadingProgressBar mainProgressBar;

    private String idString;

    @Override
    public void initView() {

        setContentView(R.layout.activity_base_browser);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainWebView = findViewById(R.id.mainWebView);
        mainProgressBar = findViewById(R.id.mainProgressBar);

    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void initData() {

        idString = getIntent().getStringExtra(BaseConstant.DATA_ID);

        if (TextUtils.isEmpty(idString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "物流信息");
        BaseApplication.get().setWebView(mainWebView);
        mainWebView.loadUrl(BaseConstant.URL_LOGISTICS + idString);

    }

    @Override
    public void initEven() {

        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mainWebView.loadUrl(url.contains(BaseConstant.URL_LOGISTICS) ? url : BaseConstant.URL_LOGISTICS + idString);
                return false;
            }
        });

        mainWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mainProgressBar.setVisibility(View.GONE);
                } else {
                    if (mainProgressBar.getVisibility() == View.GONE) {
                        mainProgressBar.setVisibility(View.VISIBLE);
                    }
                    mainProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

    }

}
