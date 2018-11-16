package top.yokey.shopwt.activity.refund;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.RefundListAdapter;
import top.yokey.shopwt.adapter.ReturnListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.RefundBean;
import top.yokey.base.bean.ReturnBean;
import top.yokey.base.model.MemberRefundModel;
import top.yokey.base.model.MemberReturnModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RefundActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private int refundPageInt;
    private PullRefreshView refundPullRefreshView;
    private RefundListAdapter refundAdapter;
    private ArrayList<RefundBean> refundArrayList;

    private int returnPageInt;
    private PullRefreshView returnPullRefreshView;
    private ReturnListAdapter returnAdapter;
    private ArrayList<ReturnBean> returnArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_refund_refund);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "退款/退货");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        List<String> titleList = new ArrayList<>();
        titleList.add("退款");
        titleList.add("退货");
        for (int i = 0; i < viewList.size(); i++) {
            mainTabLayout.addTab(mainTabLayout.newTab().setText(titleList.get(i)));
        }
        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);

        refundPageInt = 1;
        refundArrayList = new ArrayList<>();
        refundPullRefreshView = viewList.get(0).findViewById(R.id.mainPullRefreshView);
        refundAdapter = new RefundListAdapter(refundArrayList);
        refundPullRefreshView.getRecyclerView().setAdapter(refundAdapter);

        returnPageInt = 1;
        returnArrayList = new ArrayList<>();
        returnPullRefreshView = viewList.get(1).findViewById(R.id.mainPullRefreshView);
        returnAdapter = new ReturnListAdapter(returnArrayList);
        returnPullRefreshView.getRecyclerView().setAdapter(returnAdapter);

        getRefund();

    }

    @Override
    public void initEven() {

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1 && returnArrayList.size() == 0) {
                    getReturn();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        refundPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refundPageInt = 1;
                getRefund();
            }

            @Override
            public void onLoadMore() {
                getRefund();
            }
        });

        returnPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                returnPageInt = 1;
                getReturn();
            }

            @Override
            public void onLoadMore() {
                getReturn();
            }
        });

        refundAdapter.setOnItemClickListener(new RefundListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, RefundBean refundBean) {
                refundDetailed(refundBean.getRefundId());
            }

            @Override
            public void onClickGoods(int position, int itemPosition, RefundBean.GoodsListBean goodsListBean) {
                BaseApplication.get().startGoods(getActivity(), goodsListBean.getGoodsId());
            }
        });

        returnAdapter.setOnItemClickListener(new ReturnListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, ReturnBean returnBean) {
                refundDetailed(returnBean.getRefundId());
            }

            @Override
            public void onOpera(int position, ReturnBean returnBean) {
                if (returnBean.getShipState().equals("1")) {
                    returnDelivery(returnBean.getRefundId());
                } else {
                    refundDetailed(returnBean.getRefundId());
                }
            }

            @Override
            public void onClickGoods(int position, ReturnBean returnBean) {
                BaseApplication.get().startGoods(getActivity(), returnBean.getGoodsId());
            }
        });

    }

    //自定义方法

    private void getRefund() {

        MemberRefundModel.get().getRefundList(refundPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (refundPageInt == 1) {
                    refundArrayList.clear();
                }
                if (refundPageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "refund_list");
                    refundArrayList.addAll(JsonUtil.json2ArrayList(data, RefundBean.class));
                    refundPageInt++;
                }
                refundPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void getReturn() {

        MemberReturnModel.get().getReturnList(returnPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (returnPageInt == 1) {
                    returnArrayList.clear();
                }
                if (returnPageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "return_list");
                    returnArrayList.addAll(JsonUtil.json2ArrayList(data, ReturnBean.class));
                    returnPageInt++;
                }
                returnPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void returnDelivery(String returnId) {

        Intent intent = new Intent(getActivity(), ReturnDeliveryActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, returnId);
        BaseApplication.get().start(getActivity(), intent);

    }

    private void refundDetailed(String refundId) {

        Intent intent = new Intent(getActivity(), RefundDetailedActivity.class);
        intent.putExtra(BaseConstant.DATA_ID, refundId);
        BaseApplication.get().start(getActivity(), intent);

    }

}
