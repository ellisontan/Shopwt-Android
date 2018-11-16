package top.yokey.shopwt.activity.goods;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.otto.Subscribe;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.event.GoodsBeanEvent;
import top.yokey.base.model.GoodsModel;
import top.yokey.base.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_goods_detailed)
public class DetailedFragment extends BaseFragment {

    @ViewInject(R.id.mainProgressBar)
    private ContentLoadingProgressBar mainProgressBar;
    @ViewInject(R.id.mainWebView)
    private WebView mainWebView;

    @Override
    public void initData() {

        BaseApplication.get().setWebView(mainWebView);

    }

    @Override
    public void initEven() {

        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mainWebView.loadUrl(url);
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

    //自定义方法

    @Subscribe
    @SuppressWarnings("unused")
    public void onGoodsBeanEvent(GoodsBeanEvent event) {
        try {
            String temp = "";
            JSONObject jsonObject = new JSONObject(event.getBaseBean().getDatas());
            jsonObject = new JSONObject(jsonObject.getString("goods_content"));
            String goodsId = jsonObject.getString("goods_id");
            GoodsModel.get().goodsBody(goodsId, new BaseHttpListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    BaseToast.get().show("数据解析错误！");
                }

                @Override
                public void onFailure(String reason) {
                    mainWebView.loadDataWithBaseURL(
                            null,
                            TextUtil.encodeHtml(getActivity(), reason),
                            "text/html",
                            "UTF-8",
                            null
                    );
                }
            });
        } catch (JSONException e) {
            BaseToast.get().show("数据解析错误！");
            e.printStackTrace();
        }
    }

}
