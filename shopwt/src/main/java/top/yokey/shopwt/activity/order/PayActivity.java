package top.yokey.shopwt.activity.order;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseLogger;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.MemberBuyModel;
import top.yokey.base.model.MemberPaymentModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.payment.PayResult;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class PayActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView moneyTextView;
    private AppCompatTextView snTextView;
    private LinearLayoutCompat ownLinearLayout;
    private RelativeLayout preDepositRelativeLayout;
    private AppCompatTextView preDepositTextView;
    private AppCompatRadioButton preDepositRadioButton;
    private RelativeLayout rechargeCardRelativeLayout;
    private AppCompatTextView rechargeCardTextView;
    private AppCompatRadioButton rechargeCardRadioButton;
    private RelativeLayout passwordRelativeLayout;
    private AppCompatEditText passwordEditText;
    private LinearLayoutCompat thrLinearLayout;
    private RelativeLayout aliPayRelativeLayout;
    private AppCompatRadioButton aliPayRadioButton;
    private RelativeLayout wxPayRelativeLayout;
    private AppCompatRadioButton wxPayRadioButton;
    private AppCompatTextView payTextView;

    private String paySnString;
    private String passwordString;
    private String rcbPayString;
    private String pdPayString;
    private String paymentCodeString;

    @SuppressWarnings("CanBeFinal")
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    @SuppressWarnings("UnusedAssignment")
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        payTextView.setText("订单支付");
                        BaseToast.get().show("支付成功");
                        BaseApplication.get().finish(getActivity());
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            BaseToast.get().show("支付结果确认中");
                        } else {
                            BaseToast.get().show("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void initView() {

        setContentView(R.layout.activity_order_pay);
        mainToolbar = findViewById(R.id.mainToolbar);
        moneyTextView = findViewById(R.id.moneyTextView);
        snTextView = findViewById(R.id.snTextView);
        ownLinearLayout = findViewById(R.id.ownLinearLayout);
        preDepositRelativeLayout = findViewById(R.id.preDepositRelativeLayout);
        preDepositTextView = findViewById(R.id.preDepositTextView);
        preDepositRadioButton = findViewById(R.id.preDepositRadioButton);
        rechargeCardRelativeLayout = findViewById(R.id.rechargeCardRelativeLayout);
        rechargeCardTextView = findViewById(R.id.rechargeCardTextView);
        rechargeCardRadioButton = findViewById(R.id.rechargeCardRadioButton);
        passwordRelativeLayout = findViewById(R.id.passwordRelativeLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        thrLinearLayout = findViewById(R.id.thrLinearLayout);
        aliPayRelativeLayout = findViewById(R.id.aliPayRelativeLayout);
        aliPayRadioButton = findViewById(R.id.aliPayRadioButton);
        wxPayRelativeLayout = findViewById(R.id.wxPayRelativeLayout);
        wxPayRadioButton = findViewById(R.id.wxPayRadioButton);
        payTextView = findViewById(R.id.payTextView);

    }

    @Override
    public void initData() {

        paySnString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(paySnString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "订单支付");

        snTextView.setText("订单：");
        snTextView.append(paySnString);

        passwordString = "";
        rcbPayString = "";
        pdPayString = "";
        paymentCodeString = "";

        getData();

    }

    @Override
    public void initEven() {

        preDepositRelativeLayout.setOnClickListener(view -> preDepositRadioButton.setChecked(true));

        preDepositRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rechargeCardRadioButton.setChecked(false);
                aliPayRadioButton.setChecked(false);
                wxPayRadioButton.setChecked(false);
                passwordRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        rechargeCardRelativeLayout.setOnClickListener(view -> rechargeCardRadioButton.setChecked(true));

        rechargeCardRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                preDepositRadioButton.setChecked(false);
                aliPayRadioButton.setChecked(false);
                wxPayRadioButton.setChecked(false);
                passwordRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        aliPayRelativeLayout.setOnClickListener(view -> aliPayRadioButton.setChecked(true));

        aliPayRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                paymentCodeString = "alipay";
                preDepositRadioButton.setChecked(false);
                rechargeCardRadioButton.setChecked(false);
                wxPayRadioButton.setChecked(false);
                passwordRelativeLayout.setVisibility(View.GONE);
            }
        });

        wxPayRelativeLayout.setOnClickListener(view -> wxPayRadioButton.setChecked(true));

        wxPayRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                paymentCodeString = "wxpay";
                preDepositRadioButton.setChecked(false);
                rechargeCardRadioButton.setChecked(false);
                aliPayRadioButton.setChecked(false);
                passwordRelativeLayout.setVisibility(View.GONE);
            }
        });

        payTextView.setOnClickListener(view -> {
            boolean check = false;
            if (preDepositRadioButton.isChecked()) {
                check = true;
            }
            if (rechargeCardRadioButton.isChecked()) {
                check = true;
            }
            if (aliPayRadioButton.isChecked()) {
                check = true;
            }
            if (wxPayRadioButton.isChecked()) {
                check = true;
            }
            if (!check) {
                BaseToast.get().show("请选择支付方式！");
                return;
            }
            pay();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (BaseApplication.get().isWxPay()) {
            BaseApplication.get().setWxPay(false);
            if (BaseApplication.get().isSuccess()) {
                BaseToast.get().show("微信支付成功");
            } else {
                BaseToast.get().show("微信支付失败");
            }
            BaseApplication.get().finish(getActivity());
        }
    }

    //自定义方法

    private void pay() {

        pdPayString = preDepositRadioButton.isChecked() ? "1" : "0";
        rcbPayString = rechargeCardRadioButton.isChecked() ? "1" : "0";

        if (pdPayString.equals("1") || rcbPayString.equals("1")) {

            passwordString = passwordEditText.getText().toString();
            if (TextUtils.isEmpty(passwordString)) {
                BaseToast.get().show("请输入支付密码！");
                return;
            }

            payTextView.setEnabled(false);
            payTextView.setText("支付中...");

            MemberBuyModel.get().checkPdPwd(passwordString, new BaseHttpListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    if (JsonUtil.isSuccess(baseBean.getDatas())) {
                        ownPay();
                    } else {
                        payTextView.setEnabled(true);
                        payTextView.setText("订单支付");
                        BaseToast.get().showFailure();
                    }
                }

                @Override
                public void onFailure(String reason) {
                    payTextView.setEnabled(true);
                    payTextView.setText("订单支付");
                    BaseToast.get().show(reason);
                }
            });

            return;

        }

        if (paymentCodeString.equals("alipay")) {

            payTextView.setEnabled(false);
            payTextView.setText("支付中...");

            MemberPaymentModel.get().alipayNativePay(paySnString, new BaseHttpListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    payTextView.setEnabled(true);
                    payTextView.setText("订单支付");
                    try {
                        JSONObject jsonObject = new JSONObject(baseBean.getDatas());
                        BaseLogger.get().show(jsonObject.toString());
                        final String signStr = jsonObject.getString("signStr");
                        Runnable payRunnable = () -> {
                            PayTask alipay = new PayTask(getActivity());
                            String result = alipay.pay(signStr, true);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } catch (JSONException e) {
                        BaseToast.get().show("服务器返回数据错误！");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String reason) {
                    payTextView.setEnabled(true);
                    payTextView.setText("订单支付");
                    BaseToast.get().show(reason);
                }
            });

            return;

        }

        if (paymentCodeString.equals("wxpay")) {

            payTextView.setEnabled(false);
            payTextView.setText("支付中...");

            MemberPaymentModel.get().wxAppPay3(paySnString, new BaseHttpListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    payTextView.setEnabled(true);
                    payTextView.setText("订单支付");
                    try {
                        JSONObject json = new JSONObject(baseBean.getDatas());
                        PayReq req = new PayReq();
                        req.appId = json.getString("appid");
                        req.partnerId = json.getString("partnerid");
                        req.prepayId = json.getString("prepayid");
                        req.nonceStr = json.getString("noncestr");
                        req.timeStamp = json.getString("timestamp");
                        req.packageValue = json.getString("package");
                        req.sign = json.getString("sign");
                        req.extData = "app data";
                        BaseApplication.get().setWxPay(true);
                        BaseApplication.get().setSuccess(false);
                        BaseApplication.get().getIwxapi().sendReq(req);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String reason) {
                    payTextView.setEnabled(true);
                    payTextView.setText("订单支付");
                    BaseToast.get().show(reason);
                }
            });

        }

    }

    private void ownPay() {

        MemberPaymentModel.get().payNew(paySnString, passwordString, rcbPayString, pdPayString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                payTextView.setEnabled(true);
                payTextView.setText("订单支付");
                BaseToast.get().show("支付成功");
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                payTextView.setEnabled(true);
                payTextView.setText("订单支付");
                BaseToast.get().show("支付成功");
                BaseApplication.get().finish(getActivity());
            }
        });

    }

    private void getData() {

        MemberBuyModel.get().pay(paySnString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "pay_info");
                    JSONObject jsonObject = new JSONObject(data);
                    //站内支付
                    double payAmount = Double.parseDouble(jsonObject.getString("pay_amount"));
                    double preDeposit = Double.parseDouble(jsonObject.getString("member_available_pd"));
                    double rechargeCard = Double.parseDouble(jsonObject.getString("member_available_rcb"));
                    if (preDeposit > payAmount) {
                        ownLinearLayout.setVisibility(View.VISIBLE);
                        preDepositRelativeLayout.setVisibility(View.VISIBLE);
                        preDepositTextView.append("（可用金额：￥" + jsonObject.getString("member_available_pd") + "）");
                    }
                    if (rechargeCard > payAmount) {
                        ownLinearLayout.setVisibility(View.VISIBLE);
                        rechargeCardRelativeLayout.setVisibility(View.VISIBLE);
                        rechargeCardTextView.append("（可用金额：￥" + jsonObject.getString("member_available_rcb") + "）");
                    }
                    //第三方支付
                    if (data.contains("wxpay")) {
                        thrLinearLayout.setVisibility(View.VISIBLE);
                        wxPayRelativeLayout.setVisibility(View.VISIBLE);
                    }
                    if (data.contains("alipay")) {
                        thrLinearLayout.setVisibility(View.VISIBLE);
                        aliPayRelativeLayout.setVisibility(View.VISIBLE);
                    }
                    if (ownLinearLayout.getVisibility() == View.GONE) {
                        aliPayRadioButton.setChecked(true);
                    }
                    moneyTextView.setText("￥");
                    moneyTextView.append(payAmount + "");
                    payTextView.setText("订单支付：￥");
                    payTextView.append(payAmount + "");
                } catch (JSONException e) {
                    e.printStackTrace();
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
