package top.yokey.shopwt.activity.order;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.refund.RefundApplyActivity;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.OrderListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.OrderBean;
import top.yokey.base.model.MemberOrderModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class OrderActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText searchEditText;
    private AppCompatImageView toolbarImageView;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private OrderListAdapter[] mainAdapter;
    private PullRefreshView[] mainPullRefreshView;
    private ArrayList<OrderBean>[] mainArrayList;

    private int[] pageInt;
    private String keyword;
    private int positionInt;
    private boolean refreshBoolean;
    private String[] stateTypeString;

    @Override
    public void initView() {

        setContentView(R.layout.activity_order_order);
        mainToolbar = findViewById(R.id.mainToolbar);
        searchEditText = findViewById(R.id.searchEditText);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);

    }

    @Override
    public void initData() {

        positionInt = getIntent().getIntExtra(BaseConstant.DATA_POSITION, 0);

        setToolbar(mainToolbar, "");
        toolbarImageView.setImageResource(R.drawable.ic_action_search);

        stateTypeString = new String[5];
        stateTypeString[0] = "";
        stateTypeString[1] = "state_new";
        stateTypeString[2] = "state_send";
        stateTypeString[3] = "state_notakes";
        stateTypeString[4] = "state_noeval";

        List<String> titleList = new ArrayList<>();
        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待收货");
        titleList.add("待自提");
        titleList.add("待评价");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));

        keyword = "";
        refreshBoolean = false;
        pageInt = new int[viewList.size()];
        //noinspection unchecked
        mainArrayList = new ArrayList[viewList.size()];
        mainAdapter = new OrderListAdapter[viewList.size()];
        mainPullRefreshView = new PullRefreshView[viewList.size()];

        for (int i = 0; i < viewList.size(); i++) {
            pageInt[i] = 1;
            mainArrayList[i] = new ArrayList<>();
            mainAdapter[i] = new OrderListAdapter(mainArrayList[i]);
            mainPullRefreshView[i] = viewList.get(i).findViewById(R.id.mainPullRefreshView);
            mainTabLayout.addTab(mainTabLayout.newTab().setText(titleList.get(i)));
            mainPullRefreshView[i].getRecyclerView().setAdapter(mainAdapter[i]);
        }

        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mainViewPager.setCurrentItem(positionInt);
        getOrder();

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> {
            BaseApplication.get().hideKeyboard(getActivity());
            keyword = searchEditText.getText().toString();
            pageInt[positionInt] = 1;
            getOrder();
        });

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionInt = position;
                if (mainArrayList[positionInt].size() == 0) {
                    getOrder();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (PullRefreshView pullRefreshView : mainPullRefreshView) {
            pullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pageInt[positionInt] = 1;
                    getOrder();
                }

                @Override
                public void onLoadMore() {
                    getOrder();
                }
            });
        }

        for (OrderListAdapter orderListAdapter : mainAdapter) {
            orderListAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
                @Override
                public void onPay(int position, OrderBean orderBean) {
                    BaseApplication.get().startOrderPay(getActivity(), orderBean.getPaySn());
                    refreshBoolean = true;
                }

                @Override
                public void onClick(int position, OrderBean orderBean) {

                }

                @Override
                public void onItemClick(int position, int itemPosition, OrderBean.OrderListBean orderListBean) {
                    orderDetailed(orderListBean.getOrderId());
                }

                @Override
                public void onItemGoodsClick(int position, int itemPosition, OrderBean.OrderListBean orderListBean) {
                    orderDetailed(orderListBean.getOrderId());
                }

                @Override
                public void onOption(int position, int itemPosition, OrderBean.OrderListBean orderListBean) {
                    switch (orderListBean.getOrderState()) {
                        case "0":
                            //订单详细
                            orderDetailed(orderListBean.getOrderId());
                            break;
                        case "10":
                            //订单详细
                            orderDetailed(orderListBean.getOrderId());
                            break;
                        case "20":
                            if (orderListBean.getLockState().equals("0")) {
                                //订单详细
                                orderDetailed(orderListBean.getOrderId());
                            }
                        case "30":
                            if (orderListBean.getLockState().equals("0")) {
                                //查看物流
                                orderLogistics(orderListBean.getShippingCode());
                            } else {
                                //查看物流
                                orderLogistics(orderListBean.getShippingCode());
                            }
                            break;
                        case "40":
                            if (orderListBean.getEvaluationState().equals("1")) {
                                if (orderListBean.getEvaluationAgainState().equals("1")) {
                                    //删除订单
                                    orderDelete(orderListBean.getOrderId());
                                } else {
                                    //删除订单
                                    orderDelete(orderListBean.getOrderId());
                                }
                            } else {
                                //订单详细
                                orderDetailed(orderListBean.getOrderId());
                            }
                            break;
                    }
                }

                @Override
                public void onOpera(int position, int itemPosition, OrderBean.OrderListBean orderListBean) {
                    switch (orderListBean.getOrderState()) {
                        case "0":
                            //删除订单
                            orderDelete(orderListBean.getOrderId());
                            break;
                        case "10":
                            //取消订单
                            orderCancel(orderListBean.getOrderId());
                            break;
                        case "20":
                            if (orderListBean.getLockState().equals("0")) {
                                //申请退款
                                orderRefund(orderListBean.getOrderId());
                            } else {
                                //订单详细
                                orderDetailed(orderListBean.getOrderId());
                            }
                            break;
                        case "30":
                            if (orderListBean.getLockState().equals("0")) {
                                //确认收货
                                orderReceive(orderListBean.getOrderId());
                            } else {
                                //订单详细
                                orderDetailed(orderListBean.getOrderId());
                            }
                            break;
                        case "40":
                            if (orderListBean.getEvaluationState().equals("1")) {
                                if (orderListBean.getEvaluationAgainState().equals("1")) {
                                    //订单详细
                                    orderDetailed(orderListBean.getOrderId());
                                } else {
                                    //追加评价
                                    orderEvaluateAgain(orderListBean.getOrderId());
                                }
                            } else {
                                //订单评价
                                orderEvaluate(orderListBean.getOrderId());
                            }
                            break;
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshBoolean) {
            refreshBoolean = false;
            pageInt[positionInt] = 1;
            getOrder();
        }
    }

    //自定义方法

    private void getOrder() {

        mainPullRefreshView[positionInt].setLoading();

        MemberOrderModel.get().orderList(stateTypeString[positionInt], keyword, pageInt[positionInt] + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt[positionInt] == 1) {
                    mainArrayList[positionInt].clear();
                }
                if (pageInt[positionInt] <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "order_group_list");
                    mainArrayList[positionInt].addAll(JsonUtil.json2ArrayList(data, OrderBean.class));
                    pageInt[positionInt]++;
                }
                for (int i = 0; i < mainArrayList[positionInt].size(); i++) {
                    for (int j = 0; j < mainArrayList[positionInt].get(i).getOrderList().size(); i++) {
                        if (mainArrayList[positionInt].get(i).getOrderList().get(j).getExtendOrderGoods() == null) {
                            mainArrayList[positionInt].remove(i);
                            i--;
                            break;
                        }
                    }
                }
                mainPullRefreshView[positionInt].setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView[positionInt].setFailure();
            }
        });

    }

    private void orderDelete(String orderId) {

        MemberOrderModel.get().orderDelete(orderId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                pageInt[positionInt] = 1;
                getOrder();
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
                pageInt[positionInt] = 1;
                getOrder();
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
                pageInt[positionInt] = 1;
                getOrder();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void orderLogistics(String number) {

        Intent intent = new Intent(getActivity(), LogisticsActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, number);
        BaseApplication.get().start(getActivity(), intent);

    }

    private void orderRefund(String orderId) {

        Intent intent = new Intent(getActivity(), RefundApplyActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        intent.putExtra(BaseConstant.DATA_GOODSID, "");
        BaseApplication.get().start(getActivity(), intent);
        refreshBoolean = true;

    }

    private void orderDetailed(String orderId) {

        Intent intent = new Intent(getActivity(), DetailedActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        BaseApplication.get().start(getActivity(), intent);
        refreshBoolean = true;

    }

    private void orderEvaluate(String orderId) {

        Intent intent = new Intent(getActivity(), EvaluateActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        BaseApplication.get().start(getActivity(), intent);
        refreshBoolean = true;

    }

    private void orderEvaluateAgain(String orderId) {

        Intent intent = new Intent(getActivity(), EvaluateAgainActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, orderId);
        BaseApplication.get().start(getActivity(), intent);
        refreshBoolean = true;

    }

}
