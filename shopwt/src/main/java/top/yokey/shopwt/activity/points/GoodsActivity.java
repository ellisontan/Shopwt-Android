package top.yokey.shopwt.activity.points;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsPointsBean;
import top.yokey.base.model.PointcartModel;
import top.yokey.base.model.PointprodModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.base.util.TextUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.base.LoginActivity;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.base.base.BaseAnimClient;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.base.BaseImageLoader;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView mainImageView;
    private AppCompatTextView nameTextView;
    private AppCompatTextView moneyTextView;
    private AppCompatTextView priceTextView;
    private AppCompatTextView saleTextView;
    private WebView mainWebView;
    private AppCompatTextView exchangeTextView;
    private AppCompatTextView nightTextView;
    private RelativeLayout chooseRelativeLayout;
    private AppCompatImageView chooseGoodsImageView;
    private AppCompatTextView chooseNameTextView;
    private AppCompatTextView chooseMoneyTextView;
    private AppCompatTextView chooseStorageTextView;
    private AppCompatTextView chooseAddTextView;
    private AppCompatEditText chooseNumberEditText;
    private AppCompatTextView chooseSubTextView;

    private String limit;
    private String idString;
    private boolean haveGoods;
    private GoodsPointsBean goodsPointsBean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_points_goods);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainImageView = findViewById(R.id.mainImageView);
        nameTextView = findViewById(R.id.nameTextView);
        moneyTextView = findViewById(R.id.moneyTextView);
        priceTextView = findViewById(R.id.priceTextView);
        saleTextView = findViewById(R.id.saleTextView);
        mainWebView = findViewById(R.id.mainWebView);
        exchangeTextView = findViewById(R.id.exchangeTextView);
        nightTextView = findViewById(R.id.nightTextView);
        chooseRelativeLayout = findViewById(R.id.chooseRelativeLayout);
        chooseGoodsImageView = findViewById(R.id.chooseGoodsImageView);
        chooseNameTextView = findViewById(R.id.chooseNameTextView);
        chooseMoneyTextView = findViewById(R.id.chooseMoneyTextView);
        chooseStorageTextView = findViewById(R.id.chooseStorageTextView);
        chooseAddTextView = findViewById(R.id.chooseAddTextView);
        chooseNumberEditText = findViewById(R.id.chooseNumberEditText);
        chooseSubTextView = findViewById(R.id.chooseSubTextView);

    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void initData() {

        idString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(idString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        limit = "";
        haveGoods = true;

        int width = BaseApplication.get().getWidth();
        @SuppressWarnings("SuspiciousNameCombination")
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(width, width);
        mainImageView.setLayoutParams(layoutParams);
        BaseApplication.get().setWebView(mainWebView);
        goodsPointsBean = new GoodsPointsBean();
        setToolbar(mainToolbar, "积分兑换详细");

        getData();

    }

    @Override
    public void initEven() {

        nightTextView.setOnClickListener(view -> goneChooseLayout());

        chooseRelativeLayout.setOnClickListener(view -> {
            //仅仅只是为了防止点到暗色标签
        });

        chooseAddTextView.setOnClickListener(view -> {
            String number = (Integer.parseInt(chooseNumberEditText.getText().toString()) + 1) + "";
            chooseNumberEditText.setText(number);
            changeNumber();
        });

        chooseNumberEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                changeNumber();
            }
            return false;
        });

        chooseSubTextView.setOnClickListener(view -> {
            String number = (Integer.parseInt(chooseNumberEditText.getText().toString()) - 1) + "";
            chooseNumberEditText.setText(number);
            changeNumber();
        });

        exchangeTextView.setOnClickListener(view -> {
            if (!haveGoods) {
                BaseToast.get().show("没货啦！");
                return;
            }
            if (chooseRelativeLayout.getVisibility() == View.GONE) {
                showChooseLayout();
            } else {
                exchange();
            }
        });

    }

    //自定义方法

    private void getData() {

        BaseDialog.get().progress(getActivity());

        PointprodModel.get().pinfo(idString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseDialog.get().cancel();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_content");
                goodsPointsBean = JsonUtil.json2Bean(data, GoodsPointsBean.class);
                BaseImageLoader.get().display(goodsPointsBean.getPgoodsImage(), mainImageView);
                BaseImageLoader.get().display(goodsPointsBean.getPgoodsImage(), chooseGoodsImageView);
                nameTextView.setText(goodsPointsBean.getPgoodsName());
                chooseNameTextView.setText(goodsPointsBean.getPgoodsName());
                moneyTextView.setText("兑换积分：");
                moneyTextView.append(goodsPointsBean.getPgoodsPoints());
                priceTextView.setText("原价：");
                priceTextView.append(goodsPointsBean.getPgoodsPrice());
                priceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                chooseMoneyTextView.setText(moneyTextView.getText());
                saleTextView.setText("库存：");
                saleTextView.append(goodsPointsBean.getPgoodsStorage());
                chooseStorageTextView.setText(saleTextView.getText());
                mainWebView.loadDataWithBaseURL(
                        null,
                        TextUtil.encodeHtml(getActivity(), goodsPointsBean.getPgoodsBody()),
                        "text/html",
                        "UTF-8",
                        null
                );
                haveGoods = !goodsPointsBean.getPgoodsStorage().equals("0");
                if (goodsPointsBean.getPgoodsIslimit().equals("1")) {
                    limit = goodsPointsBean.getPgoodsLimitnum();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
                BaseDialog.get().queryLoadingFailure(getActivity(), reason, (dialog, which) -> getData(), (dialog, which) -> BaseApplication.get().finish(getActivity()));
            }
        });

    }

    private void exchange() {

        if (!BaseApplication.get().isLogin()) {
            BaseApplication.get().start(getActivity(), LoginActivity.class);
            return;
        }

        goneChooseLayout();
        BaseDialog.get().progress(getActivity());
        PointcartModel.get().add(idString, chooseNumberEditText.getText().toString(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseDialog.get().cancel();
                BaseApplication.get().startCheckLogin(getActivity(), BuyActivity.class);
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
                BaseToast.get().show(reason);
            }
        });

    }

    private void changeNumber() {

        if (TextUtils.isEmpty(chooseNumberEditText.getText().toString())) {
            BaseToast.get().show("数量不能为空！");
            chooseNumberEditText.setText("1");
            chooseNumberEditText.setSelection(1);
            return;
        }

        int number = Integer.parseInt(chooseNumberEditText.getText().toString());

        if (number <= 0) {
            BaseToast.get().show("最低要买 1 件");
            number = 1;
        }

        if (!TextUtils.isEmpty(limit)) {
            int upper = Integer.parseInt(limit);
            if (number > upper) {
                number = upper;
                BaseToast.get().show("每人最高限购：" + number + " 件");
            }
        }

        int storage = Integer.parseInt(goodsPointsBean.getPgoodsStorage());

        if (number > storage) {
            number = storage;
            BaseToast.get().show("库存不足！");
        }

        String temp = number + "";
        chooseNumberEditText.setText(temp);
        chooseNumberEditText.setSelection(temp.length());

    }

    private void goneChooseLayout() {

        if (nightTextView.getVisibility() == View.VISIBLE) {
            nightTextView.setVisibility(View.GONE);
            BaseAnimClient.get().goneAlpha(nightTextView);
        }

        if (chooseRelativeLayout.getVisibility() == View.VISIBLE) {
            chooseRelativeLayout.setVisibility(View.GONE);
            //BaseAnimClient.get().downTranslate(chooseRelativeLayout, chooseRelativeLayout.getHeight());
        }

    }

    private void showChooseLayout() {

        if (nightTextView.getVisibility() == View.GONE) {
            nightTextView.setVisibility(View.VISIBLE);
            BaseAnimClient.get().showAlpha(nightTextView);
        }

        if (chooseRelativeLayout.getVisibility() == View.GONE) {
            chooseRelativeLayout.setVisibility(View.VISIBLE);
            //BaseAnimClient.get().upTranslate(chooseRelativeLayout, chooseRelativeLayout.getHeight());
        }

    }

}
