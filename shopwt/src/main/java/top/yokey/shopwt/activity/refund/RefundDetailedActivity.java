package top.yokey.shopwt.activity.refund;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.RefundDetailedBean;
import top.yokey.base.model.MemberRefundModel;
import top.yokey.base.util.JsonUtil;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RefundDetailedActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView snTextView;
    private AppCompatTextView reasonTextView;
    private AppCompatTextView moneyTextView;
    private AppCompatTextView remarkTextView;
    private AppCompatImageView zeroImageView;
    private AppCompatImageView oneImageView;
    private AppCompatImageView twoImageView;
    private AppCompatImageView[] mainImageView;
    private AppCompatTextView storeStateTextView;
    private AppCompatTextView storeRemarkTextView;
    private AppCompatTextView adminStateTextView;
    private AppCompatTextView adminRemarkTextView;
    private AppCompatTextView paymentTextView;
    private AppCompatTextView onlineMoneyTextView;
    private AppCompatTextView preDepositMoneyTextView;
    private AppCompatTextView rechargeCardMoneyTextView;

    private String refundIdString;
    private RefundDetailedBean orderRefundDetailedBean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_refund_detailed);
        mainToolbar = findViewById(R.id.mainToolbar);
        snTextView = findViewById(R.id.snTextView);
        reasonTextView = findViewById(R.id.reasonTextView);
        moneyTextView = findViewById(R.id.moneyTextView);
        remarkTextView = findViewById(R.id.remarkTextView);
        zeroImageView = findViewById(R.id.zeroImageView);
        oneImageView = findViewById(R.id.oneImageView);
        twoImageView = findViewById(R.id.twoImageView);
        storeStateTextView = findViewById(R.id.storeStateTextView);
        storeRemarkTextView = findViewById(R.id.storeRemarkTextView);
        adminStateTextView = findViewById(R.id.adminStateTextView);
        adminRemarkTextView = findViewById(R.id.adminRemarkTextView);
        paymentTextView = findViewById(R.id.paymentTextView);
        onlineMoneyTextView = findViewById(R.id.onlineMoneyTextView);
        preDepositMoneyTextView = findViewById(R.id.preDepositMoneyTextView);
        rechargeCardMoneyTextView = findViewById(R.id.rechargeCardMoneyTextView);

    }

    @Override
    public void initData() {

        refundIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(refundIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        mainImageView = new AppCompatImageView[3];
        mainImageView[2] = zeroImageView;
        mainImageView[1] = oneImageView;
        mainImageView[0] = twoImageView;
        mainImageView[0].setVisibility(View.GONE);
        mainImageView[1].setVisibility(View.GONE);
        mainImageView[2].setVisibility(View.GONE);

        setToolbar(mainToolbar, "退款详细");
        getData();

    }

    @Override
    public void initEven() {

    }

    //自定义方法

    private void getData() {

        MemberRefundModel.get().getRefundInfo(refundIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = baseBean.getDatas().replace("[]", "null");
                orderRefundDetailedBean = JsonUtil.json2Bean(data, RefundDetailedBean.class);
                snTextView.setText(orderRefundDetailedBean.getRefund().getRefundSn());
                reasonTextView.setText(orderRefundDetailedBean.getRefund().getReasonInfo());
                moneyTextView.setText(orderRefundDetailedBean.getRefund().getRefundAmount());
                remarkTextView.setText(orderRefundDetailedBean.getRefund().getBuyerMessage());
                storeStateTextView.setText(orderRefundDetailedBean.getRefund().getSellerState());
                storeRemarkTextView.setText(orderRefundDetailedBean.getRefund().getSellerMessage());
                adminStateTextView.setText(orderRefundDetailedBean.getRefund().getAdminState());
                adminRemarkTextView.setText(orderRefundDetailedBean.getRefund().getAdminMessage());
                if (orderRefundDetailedBean.getDetailArray() != null) {
                    paymentTextView.setText(orderRefundDetailedBean.getDetailArray().getRefundCode());
                    onlineMoneyTextView.setText("￥");
                    onlineMoneyTextView.append(orderRefundDetailedBean.getDetailArray().getPayAmount());
                    preDepositMoneyTextView.setText("￥");
                    preDepositMoneyTextView.append(orderRefundDetailedBean.getDetailArray().getPdAmount());
                    rechargeCardMoneyTextView.setText("￥");
                    rechargeCardMoneyTextView.append(orderRefundDetailedBean.getDetailArray().getRcbAmount());
                }
                if (orderRefundDetailedBean.getPicList() != null) {
                    for (int i = 0; i < orderRefundDetailedBean.getPicList().size(); i++) {
                        mainImageView[i].setVisibility(View.VISIBLE);
                        BaseImageLoader.get().display(orderRefundDetailedBean.getPicList().get(i), mainImageView[i]);
                    }
                }
            }

            @Override
            public void onFailure(String reason) {
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
