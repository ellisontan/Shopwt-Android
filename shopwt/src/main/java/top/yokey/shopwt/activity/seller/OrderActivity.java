package top.yokey.shopwt.activity.seller;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.OrderSellerListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.bean.OrderSellerBean;
import top.yokey.base.model.SellerOrderModel;

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

    private OrderSellerListAdapter[] mainAdapter;
    private PullRefreshView[] mainPullRefreshView;
    private ArrayList<OrderSellerBean>[] mainArrayList;

    private int[] pageInt;
    private String keyword;
    private int positionInt;
    private boolean refreshBoolean;
    private String[] stateTypeString;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_order);
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
        stateTypeString[0] = "state_new";
        stateTypeString[1] = "state_pay";
        stateTypeString[2] = "state_send";
        stateTypeString[3] = "state_success";
        stateTypeString[4] = "state_cancel";

        List<String> titleList = new ArrayList<>();
        titleList.add("待付款");
        titleList.add("待发货");
        titleList.add("已发货");
        titleList.add("已完成");
        titleList.add("已取消");

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
        mainAdapter = new OrderSellerListAdapter[viewList.size()];
        mainPullRefreshView = new PullRefreshView[viewList.size()];

        for (int i = 0; i < viewList.size(); i++) {
            pageInt[i] = 1;
            mainArrayList[i] = new ArrayList<>();
            mainAdapter[i] = new OrderSellerListAdapter(mainArrayList[i]);
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

        for (OrderSellerListAdapter orderSellerListAdapter : mainAdapter) {
            orderSellerListAdapter.setOnItemClickListener(new OrderSellerListAdapter.OnItemClickListener() {
                @Override
                public void onOption(final int position, final OrderSellerBean bean) {
                    switch (bean.getOrderState()) {
                        case "10":
                            Intent intent = new Intent(getActivity(), OrderCancelActivity.class);
                            intent.putExtra(BaseConstant.DATA_ID, bean.getOrderId());
                            intent.putExtra(BaseConstant.DATA_SN, bean.getOrderSn());
                            intent.putExtra(BaseConstant.DATA_CONTENT, bean.getOrderAmount());
                            BaseApplication.get().startCheckSellerLogin(getActivity(), intent);
                            refreshBoolean = true;
                            break;
                    }
                }

                @Override
                public void onOpera(final int position, final OrderSellerBean bean) {
                    switch (bean.getOrderState()) {
                        case "10":
                            int paddingTop = BaseApplication.get().dipToPx(16);
                            int paddingLeft = BaseApplication.get().dipToPx(28);
                            final AppCompatEditText appCompatEditText = new AppCompatEditText(getActivity());
                            appCompatEditText.setTextColor(BaseApplication.get().getColors(R.color.primary));
                            appCompatEditText.setPadding(paddingLeft, paddingTop, paddingLeft, 0);
                            appCompatEditText.setHint("原始价格：" + bean.getOrderAmount());
                            appCompatEditText.setBackgroundColor(Color.TRANSPARENT);
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("请输入金额~")
                                    .setView(appCompatEditText)
                                    .setPositiveButton("确认", (dialog, which) -> orderSpayPrice(position, bean.getOrderId(), appCompatEditText.getText().toString()))
                                    .setNegativeButton("取消", null)
                                    .show();
                            break;
                        case "20":
                            Intent intent = new Intent(getActivity(), OrderSendActivity.class);
                            intent.putExtra(BaseConstant.DATA_ID, bean.getOrderId());
                            BaseApplication.get().startCheckSellerLogin(getActivity(), intent);
                            refreshBoolean = true;
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

        SellerOrderModel.get().orderList(stateTypeString[positionInt], keyword, pageInt[positionInt] + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt[positionInt] == 1) {
                    mainArrayList[positionInt].clear();
                }
                if (pageInt[positionInt] <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "order_list");
                    mainArrayList[positionInt].addAll(JsonUtil.json2ArrayList(data, OrderSellerBean.class));
                    pageInt[positionInt]++;
                }
                mainPullRefreshView[positionInt].setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView[positionInt].setFailure();
            }
        });

    }

    private void orderSpayPrice(final int position, final String orderId, final String orderFee) {

        SellerOrderModel.get().orderSpayPrice(orderId, orderFee, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show(baseBean.getDatas());
                mainArrayList[positionInt].get(position).setOrderAmount(orderFee);
                mainAdapter[positionInt].notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
