package top.yokey.shopwt.activity.order;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.refund.RefundApplyActivity;
import top.yokey.shopwt.activity.refund.ReturnApplyActivity;
import top.yokey.shopwt.adapter.GoodsOrderInfoListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.OrderInfoBean;
import top.yokey.base.model.MemberOrderModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class DetailedActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView stateTextView;
    private AppCompatTextView addressNameTextView;
    private AppCompatTextView addressMobileTextView;
    private AppCompatTextView addressAreaTextView;
    private RelativeLayout invoiceRelativeLayout;
    private AppCompatTextView invoiceContentTextView;
    private RelativeLayout messageRelativeLayout;
    private AppCompatTextView messageContentTextView;
    private AppCompatTextView paymentContentTextView;
    private AppCompatTextView storeNameTextView;
    private RecyclerView mainRecyclerView;
    private LinearLayoutCompat zengPinLinearLayout;
    private AppCompatTextView zengPinDescTextView;
    private RelativeLayout promotionRelativeLayout;
    private AppCompatTextView promotionContentTextView;
    private AppCompatTextView logisticsMoneyTextView;
    private AppCompatTextView totalMoneyTextView;
    private AppCompatTextView snTextView;
    private AppCompatTextView createTimeTextView;
    private AppCompatTextView payTimeTextView;
    private AppCompatTextView customerTextView;
    private AppCompatTextView callTextView;
    private AppCompatTextView operaTextView;

    private String orderIdString;
    private OrderInfoBean orderInfoBean;

    private GoodsOrderInfoListAdapter mainAdapter;
    private ArrayList<OrderInfoBean.GoodsListBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_order_detailed);
        mainToolbar = findViewById(R.id.mainToolbar);
        stateTextView = findViewById(R.id.stateTextView);
        addressNameTextView = findViewById(R.id.addressNameTextView);
        addressMobileTextView = findViewById(R.id.addressMobileTextView);
        addressAreaTextView = findViewById(R.id.addressAreaTextView);
        invoiceRelativeLayout = findViewById(R.id.invoiceRelativeLayout);
        invoiceContentTextView = findViewById(R.id.invoiceContentTextView);
        messageRelativeLayout = findViewById(R.id.messageRelativeLayout);
        messageContentTextView = findViewById(R.id.messageContentTextView);
        paymentContentTextView = findViewById(R.id.paymentContentTextView);
        storeNameTextView = findViewById(R.id.storeNameTextView);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        zengPinLinearLayout = findViewById(R.id.zengPinLinearLayout);
        zengPinDescTextView = findViewById(R.id.zengPinDescTextView);
        promotionRelativeLayout = findViewById(R.id.promotionRelativeLayout);
        promotionContentTextView = findViewById(R.id.promotionContentTextView);
        logisticsMoneyTextView = findViewById(R.id.logisticsMoneyTextView);
        totalMoneyTextView = findViewById(R.id.totalMoneyTextView);
        snTextView = findViewById(R.id.snTextView);
        createTimeTextView = findViewById(R.id.createTimeTextView);
        payTimeTextView = findViewById(R.id.payTimeTextView);
        customerTextView = findViewById(R.id.customerTextView);
        callTextView = findViewById(R.id.callTextView);
        operaTextView = findViewById(R.id.operaTextView);

    }

    @Override
    public void initData() {

        orderIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(orderIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "订单详细");
        orderInfoBean = new OrderInfoBean();

        mainArrayList = new ArrayList<>();
        mainAdapter = new GoodsOrderInfoListAdapter(mainArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), mainRecyclerView, mainAdapter);

    }

    @Override
    public void initEven() {

        storeNameTextView.setOnClickListener(view -> BaseApplication.get().startStore(getActivity(), orderInfoBean.getStoreId()));

        mainAdapter.setOnItemClickListener(new GoodsOrderInfoListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, OrderInfoBean.GoodsListBean goodsListBean) {
                BaseApplication.get().startGoods(getActivity(), goodsListBean.getGoodsId());
            }

            @Override
            public void onRefund(int position, OrderInfoBean.GoodsListBean goodsListBean) {
                orderRefund(orderIdString, goodsListBean.getRecId());
            }

            @Override
            public void onReturn(int position, OrderInfoBean.GoodsListBean goodsListBean) {
                orderReturn(orderIdString, goodsListBean.getRecId());
            }
        });

        customerTextView.setOnClickListener(view -> BaseApplication.get().startChatOnly(getActivity(), orderInfoBean.getStoreMemberId(), ""));

        callTextView.setOnClickListener(view -> BaseApplication.get().startCall(getActivity(), orderInfoBean.getStorePhone()));

        operaTextView.setOnClickListener(view -> {
            switch (orderInfoBean.getStateDesc()) {
                case "已取消":
                    orderDelete(orderIdString);
                    break;
                case "待付款":
                    orderCancel(orderIdString);
                    break;
                case "待发货":
                    if (!orderInfoBean.isIfLock()) {
                        orderRefund(orderIdString, "");
                    }
                    break;
                case "待收货":
                    if (!orderInfoBean.isIfLock()) {
                        orderReceive(orderIdString);
                    }
                    break;
                case "交易完成":
                    if (orderInfoBean.isIfEvaluation()) {
                        orderEvaluate(orderIdString);
                    } else {
                        orderEvaluateAgain(orderIdString);
                    }
                    break;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    //自定义方法

    private void getData() {

        MemberOrderModel.get().orderInfo(orderIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "order_info");
                orderInfoBean = JsonUtil.json2Bean(data, OrderInfoBean.class);
                stateTextView.setText(orderInfoBean.getStateDesc());
                addressNameTextView.setText(orderInfoBean.getReciverName());
                addressMobileTextView.setText(orderInfoBean.getReciverPhone());
                addressAreaTextView.setText(orderInfoBean.getReciverAddr());
                paymentContentTextView.setText(orderInfoBean.getPaymentName());
                storeNameTextView.setText(orderInfoBean.getStoreName());
                logisticsMoneyTextView.setText("￥");
                logisticsMoneyTextView.append(orderInfoBean.getShippingFee());
                totalMoneyTextView.setText("￥");
                totalMoneyTextView.append(orderInfoBean.getRealPayAmount());
                snTextView.setText("订单编号：");
                snTextView.append(orderInfoBean.getOrderSn());
                createTimeTextView.setText("创建时间：");
                createTimeTextView.append(orderInfoBean.getAddTime());
                payTimeTextView.setText("支付时间：");
                payTimeTextView.append(orderInfoBean.getPaymentTime());
                mainArrayList.clear();
                mainArrayList.addAll(orderInfoBean.getGoodsList());
                mainAdapter.notifyDataSetChanged();
                if (TextUtils.isEmpty(orderInfoBean.getOrderMessage()) || orderInfoBean.getOrderMessage().equals("null")) {
                    messageRelativeLayout.setVisibility(View.GONE);
                } else {
                    messageRelativeLayout.setVisibility(View.VISIBLE);
                    messageContentTextView.setText(orderInfoBean.getOrderMessage());
                }
                if (TextUtils.isEmpty(orderInfoBean.getInvoice()) || orderInfoBean.getInvoice().equals("null")) {
                    invoiceRelativeLayout.setVisibility(View.GONE);
                } else {
                    invoiceRelativeLayout.setVisibility(View.VISIBLE);
                    invoiceContentTextView.setText(orderInfoBean.getInvoice());
                }
                if (orderInfoBean.getZengpinList().size() == 0) {
                    zengPinLinearLayout.setVisibility(View.GONE);
                } else {
                    zengPinLinearLayout.setVisibility(View.VISIBLE);
                    zengPinDescTextView.setText(orderInfoBean.getZengpinList().get(0).getGoodsName());
                    zengPinDescTextView.append(" x" + orderInfoBean.getZengpinList().get(0).getGoodsNum());
                }
                if (orderInfoBean.getPromotion().size() == 0) {
                    promotionRelativeLayout.setVisibility(View.GONE);
                } else {
                    promotionContentTextView.setText("");
                    for (int i = 0; i < orderInfoBean.getPromotion().get(0).size(); i++) {
                        promotionContentTextView.append(orderInfoBean.getPromotion().get(0).get(i));
                    }
                }
                switch (orderInfoBean.getStateDesc()) {
                    case "已取消":
                        operaTextView.setText("删除订单");
                        break;
                    case "待付款":
                        operaTextView.setText("取消订单");
                        break;
                    case "待发货":
                        if (!orderInfoBean.isIfLock()) {
                            operaTextView.setText("申请退款");
                        } else {
                            operaTextView.setText("退货/款中...");
                        }
                        break;
                    case "待收货":
                        if (!orderInfoBean.isIfLock()) {
                            operaTextView.setText("确认收货");
                        } else {
                            operaTextView.setText("退货/款中...");
                        }
                        break;
                    case "交易完成":
                        if (orderInfoBean.isIfEvaluation()) {
                            operaTextView.setText("评价订单");
                        } else {
                            operaTextView.setText("追加评价");
                        }
                        break;
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

    private void orderDelete(String orderId) {

        MemberOrderModel.get().orderDelete(orderId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("订单已删除！");
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void orderCancel(String orderId) {

        MemberOrderModel.get().orderCancel(orderId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                getData();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void orderReceive(String orderId) {

        MemberOrderModel.get().orderReceive(orderId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                getData();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void orderEvaluate(String orderId) {

        Intent intent = new Intent(getActivity(), EvaluateActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        BaseApplication.get().start(getActivity(), intent);

    }

    private void orderEvaluateAgain(String orderId) {

        Intent intent = new Intent(getActivity(), EvaluateAgainActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        BaseApplication.get().start(getActivity(), intent);

    }

    private void orderRefund(String orderId, String goodsId) {

        Intent intent = new Intent(getActivity(), RefundApplyActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        intent.putExtra(BaseConstant.DATA_GOODSID, goodsId);
        BaseApplication.get().start(getActivity(), intent);

    }

    private void orderReturn(String orderId, String goodsId) {

        Intent intent = new Intent(getActivity(), ReturnApplyActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        intent.putExtra(BaseConstant.DATA_GOODSID, goodsId);
        BaseApplication.get().start(getActivity(), intent);

    }

}
