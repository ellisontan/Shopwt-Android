package top.yokey.shopwt.activity.seller;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.SellerHttpClient;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.base.base.BaseCountTime;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseShared;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.SellerIndexBean;
import top.yokey.base.model.SellerIndexModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class SellerActivity extends BaseActivity {

    private AppCompatImageView avatarImageView;
    private AppCompatTextView nicknameTextView;
    private AppCompatTextView saleDayTextView;
    private AppCompatTextView saleMonthTextView;
    private AppCompatTextView saleIngTextView;
    private RelativeLayout orderRelativeLayout;
    private RelativeLayout orderNewRelativeLayout;
    private RelativeLayout orderPayRelativeLayout;
    private RelativeLayout orderSendRelativeLayout;
    private RelativeLayout orderSuccessRelativeLayout;
    private AppCompatTextView orderNewDotTextView;
    private AppCompatTextView orderPayDotTextView;
    private RelativeLayout goodsRelativeLayout;
    private AppCompatTextView goodsIngTextView;
    private AppCompatTextView goodsWareTextView;
    private AppCompatTextView goodsIllegalTextView;
    private AppCompatTextView goodsAddTextView;
    private RelativeLayout addressRelativeLayout;
    private RelativeLayout logisticsRelativeLayout;
    private RelativeLayout settingRelativeLayout;
    private AppCompatTextView logoutTextView;

    private SellerIndexBean sellerIndexBean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_seller);
        avatarImageView = findViewById(R.id.avatarImageView);
        nicknameTextView = findViewById(R.id.nicknameTextView);
        saleDayTextView = findViewById(R.id.saleDayTextView);
        saleMonthTextView = findViewById(R.id.saleMonthTextView);
        saleIngTextView = findViewById(R.id.saleIngTextView);
        orderRelativeLayout = findViewById(R.id.orderRelativeLayout);
        orderNewRelativeLayout = findViewById(R.id.orderNewRelativeLayout);
        orderPayRelativeLayout = findViewById(R.id.orderPayRelativeLayout);
        orderSendRelativeLayout = findViewById(R.id.orderSendRelativeLayout);
        orderSuccessRelativeLayout = findViewById(R.id.orderSuccessRelativeLayout);
        orderNewDotTextView = findViewById(R.id.orderNewDotTextView);
        orderPayDotTextView = findViewById(R.id.orderPayDotTextView);
        goodsRelativeLayout = findViewById(R.id.goodsRelativeLayout);
        goodsIngTextView = findViewById(R.id.goodsIngTextView);
        goodsWareTextView = findViewById(R.id.goodsWareTextView);
        goodsIllegalTextView = findViewById(R.id.goodsIllegalTextView);
        goodsAddTextView = findViewById(R.id.goodsAddTextView);
        addressRelativeLayout = findViewById(R.id.addressRelativeLayout);
        logisticsRelativeLayout = findViewById(R.id.logisticsRelativeLayout);
        settingRelativeLayout = findViewById(R.id.settingRelativeLayout);
        logoutTextView = findViewById(R.id.logoutTextView);

    }

    @Override
    public void initData() {

        getData();

    }

    @Override
    public void initEven() {

        orderRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrderSeller(getActivity(), 0));

        orderNewRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrderSeller(getActivity(), 0));

        orderPayRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrderSeller(getActivity(), 1));

        orderSendRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrderSeller(getActivity(), 2));

        orderSuccessRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrderSeller(getActivity(), 3));

        goodsRelativeLayout.setOnClickListener(view -> BaseApplication.get().startGoodsSeller(getActivity(), 0));

        goodsIngTextView.setOnClickListener(view -> BaseApplication.get().startGoodsSeller(getActivity(), 0));

        goodsWareTextView.setOnClickListener(view -> BaseApplication.get().startGoodsSeller(getActivity(), 1));

        goodsIllegalTextView.setOnClickListener(view -> BaseApplication.get().startGoodsSeller(getActivity(), 2));

        goodsAddTextView.setOnClickListener(view -> BaseApplication.get().startCheckSellerLogin(getActivity(), GoodsAddActivity.class));

        addressRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckSellerLogin(getActivity(), AddressActivity.class));

        logisticsRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckSellerLogin(getActivity(), ExpressActivity.class));

        settingRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckSellerLogin(getActivity(), SettingActivity.class));

        logoutTextView.setOnClickListener(view -> logout());

    }

    //自定义方法

    private void logout() {

        BaseDialog.get().queryConfirmYourChoice(getActivity(), "注销登录？", (dialog, which) -> {
            BaseToast.get().show("注销成功！");
            SellerHttpClient.get().updateKey("");
            BaseShared.get().putString(BaseConstant.SHARED_SELLER_KEY, "");
            BaseApplication.get().finish(getActivity());
        }, null);

    }

    private void getData() {

        SellerIndexModel.get().index(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = baseBean.getDatas().replace("null", "\"\"");
                sellerIndexBean = JsonUtil.json2Bean(data, SellerIndexBean.class);
                BaseImageLoader.get().displayCircle(sellerIndexBean.getStoreInfo().getStoreAvatar(), avatarImageView);
                String temp = sellerIndexBean.getSellerInfo().getSellerName() + "（" + sellerIndexBean.getStoreInfo().getGradeName() + "）";
                nicknameTextView.setText(temp);
                temp = sellerIndexBean.getStoreInfo().getDailySales().getOrdernum() + "\n昨日销量";
                saleDayTextView.setText(temp);
                temp = sellerIndexBean.getStoreInfo().getMonthlySales().getOrdernum() + "\n当月销量";
                saleMonthTextView.setText(temp);
                temp = sellerIndexBean.getStatics().getOnline() + "\n出售中";
                saleIngTextView.setText(temp);
                orderNewDotTextView.setVisibility(sellerIndexBean.getStatics().getPayment().equals("0") ? View.GONE : View.VISIBLE);
                orderPayDotTextView.setVisibility(sellerIndexBean.getStatics().getDelivery().equals("0") ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(String reason) {
                if (reason.equals("请登录")) {
                    BaseToast.get().show("身份信息已失效，请重新登录！");
                    SellerHttpClient.get().updateKey("");
                    BaseShared.get().putString(BaseConstant.SHARED_SELLER_KEY, "");
                    BaseApplication.get().start(getActivity(), LoginActivity.class);
                    BaseApplication.get().finish(getActivity());
                    return;
                }
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getData();
                    }
                }.start();
            }
        });

    }

}
