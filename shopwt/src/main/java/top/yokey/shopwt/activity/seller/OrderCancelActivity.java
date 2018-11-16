package top.yokey.shopwt.activity.seller;

import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.model.SellerOrderModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class OrderCancelActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView moneyTextView;
    private AppCompatTextView snTextView;
    private AppCompatRadioButton oneRadioButton;
    private AppCompatRadioButton twoRadioButton;
    private AppCompatRadioButton thrRadioButton;
    private AppCompatRadioButton fouRadioButton;
    private AppCompatTextView submitTextView;

    private String orderId;
    private String reason;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_order_cancel);
        mainToolbar = findViewById(R.id.mainToolbar);
        moneyTextView = findViewById(R.id.moneyTextView);
        snTextView = findViewById(R.id.snTextView);
        oneRadioButton = findViewById(R.id.oneRadioButton);
        twoRadioButton = findViewById(R.id.twoRadioButton);
        thrRadioButton = findViewById(R.id.thrRadioButton);
        fouRadioButton = findViewById(R.id.fouRadioButton);
        submitTextView = findViewById(R.id.submitTextView);

    }

    @Override
    public void initData() {

        orderId = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(orderId)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "取消订单");

        reason = "无法备齐货物";
        moneyTextView.setText("￥");
        moneyTextView.append(getIntent().getStringExtra(BaseConstant.DATA_CONTENT));
        snTextView.setText("订单编号：");
        snTextView.append(getIntent().getStringExtra(BaseConstant.DATA_SN));

    }

    @Override
    public void initEven() {

        oneRadioButton.setOnClickListener(view -> {
            reason = "无法备齐货物";
            oneRadioButton.setChecked(true);
            twoRadioButton.setChecked(false);
            thrRadioButton.setChecked(false);
            fouRadioButton.setChecked(false);
        });

        twoRadioButton.setOnClickListener(view -> {
            reason = "不是有效的订单";
            oneRadioButton.setChecked(false);
            twoRadioButton.setChecked(true);
            thrRadioButton.setChecked(false);
            fouRadioButton.setChecked(false);
        });

        thrRadioButton.setOnClickListener(view -> {
            reason = "卖家主动要求";
            oneRadioButton.setChecked(false);
            twoRadioButton.setChecked(false);
            thrRadioButton.setChecked(true);
            fouRadioButton.setChecked(false);
        });

        fouRadioButton.setOnClickListener(view -> {
            reason = "其他原因";
            oneRadioButton.setChecked(false);
            twoRadioButton.setChecked(false);
            thrRadioButton.setChecked(false);
            fouRadioButton.setChecked(true);
        });

        submitTextView.setOnClickListener(view -> cancel());

    }

    //自定义方法

    private void cancel() {

        submitTextView.setEnabled(false);
        submitTextView.setText("取消中...");

        SellerOrderModel.get().orderCancel(orderId, reason, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("取消成功");
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                submitTextView.setEnabled(true);
                submitTextView.setText("提交");
            }
        });

    }

}
