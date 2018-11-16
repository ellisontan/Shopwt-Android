package top.yokey.shopwt.activity.mine;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.PreDepositCashLogListAdapter;
import top.yokey.shopwt.adapter.PreDepositRechargeLogListAdapter;
import top.yokey.shopwt.adapter.PreDepositLogListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.PreDepositCashLogBean;
import top.yokey.base.bean.PreDepositRechargeLogBean;
import top.yokey.base.bean.PreDepositLogBean;
import top.yokey.base.model.MemberFundModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class PreDepositActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private AppCompatTextView preDepositValueTextView;

    private int preDepositPageInt;
    private PullRefreshView preDepositPullRefreshView;
    private PreDepositLogListAdapter preDepositAdapter;
    private ArrayList<PreDepositLogBean> preDepositArrayList;
    private int pdRechargePageInt;
    private PullRefreshView pdRechargePullRefreshView;
    private PreDepositRechargeLogListAdapter pdRechargeAdapter;
    private ArrayList<PreDepositRechargeLogBean> pdRechargeArrayList;
    private int pdCashPageInt;
    private PullRefreshView pdCashPullRefreshView;
    private PreDepositCashLogListAdapter pdCashAdapter;
    private ArrayList<PreDepositCashLogBean> pdCashArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_pre_deposit);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);
        preDepositValueTextView = findViewById(R.id.preDepositValueTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "预存款账户");

        preDepositValueTextView.setText(BaseApplication.get().getMemberAssetBean().getPredepoit());
        preDepositValueTextView.append("元");

        List<String> titleList = new ArrayList<>();
        titleList.add("账户余额");
        titleList.add("充值明细");
        titleList.add("余额提现");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));

        //账户余额
        preDepositPageInt = 1;
        preDepositArrayList = new ArrayList<>();
        preDepositAdapter = new PreDepositLogListAdapter(preDepositArrayList);
        preDepositPullRefreshView = viewList.get(0).findViewById(R.id.mainPullRefreshView);
        preDepositPullRefreshView.getRecyclerView().setAdapter(preDepositAdapter);

        //余额提现
        pdRechargePageInt = 1;
        pdRechargeArrayList = new ArrayList<>();
        pdRechargeAdapter = new PreDepositRechargeLogListAdapter(pdRechargeArrayList);
        pdRechargePullRefreshView = viewList.get(1).findViewById(R.id.mainPullRefreshView);
        pdRechargePullRefreshView.getRecyclerView().setAdapter(pdRechargeAdapter);

        //充值明细
        pdCashPageInt = 1;
        pdCashArrayList = new ArrayList<>();
        pdCashAdapter = new PreDepositCashLogListAdapter(pdCashArrayList);
        pdCashPullRefreshView = viewList.get(2).findViewById(R.id.mainPullRefreshView);
        pdCashPullRefreshView.getRecyclerView().setAdapter(pdCashAdapter);

        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);

        getPreDepositLog();
        getPdRechargeLog();
        getPdCashLog();

    }

    @Override
    public void initEven() {

        preDepositPullRefreshView.setOnClickListener(view -> {
            if (preDepositPullRefreshView.isFailure()) {
                preDepositPageInt = 1;
                getPreDepositLog();
            }
        });

        preDepositPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                preDepositPageInt = 1;
                getPreDepositLog();
            }

            @Override
            public void onLoadMore() {
                getPreDepositLog();
            }
        });

        preDepositAdapter.setOnItemClickListener((position, preDepositLogBean) -> {

        });

        pdRechargePullRefreshView.setOnClickListener(view -> {
            if (pdRechargePullRefreshView.isFailure()) {
                pdRechargePageInt = 1;
                getPdRechargeLog();
            }
        });

        pdRechargePullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pdRechargePageInt = 1;
                getPdRechargeLog();
            }

            @Override
            public void onLoadMore() {
                getPdRechargeLog();
            }
        });

        pdRechargeAdapter.setOnItemClickListener((position, pdRechargeLogBean) -> {

        });

        pdCashPullRefreshView.setOnClickListener(view -> {
            if (pdCashPullRefreshView.isFailure()) {
                pdCashPageInt = 1;
                getPdCashLog();
            }
        });

        pdCashPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pdCashPageInt = 1;
                getPdCashLog();
            }

            @Override
            public void onLoadMore() {
                getPdCashLog();
            }
        });

        pdCashAdapter.setOnItemClickListener((position, pdCashLogBean) -> {
            Intent intent = new Intent(getActivity(), PreDepositCashActivity.class);
            intent.putExtra(BaseConstant.DATA_BEAN, pdCashLogBean);
            BaseApplication.get().start(getActivity(), intent);
        });

    }

    //自定义方法

    private void getPreDepositLog() {

        preDepositPullRefreshView.setLoading();

        MemberFundModel.get().preDepositLog(preDepositPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (preDepositPageInt == 1) {
                    preDepositArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    preDepositPageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                preDepositArrayList.addAll(JsonUtil.json2ArrayList(data, PreDepositLogBean.class));
                preDepositPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                preDepositPullRefreshView.setFailure();
            }
        });

    }

    private void getPdRechargeLog() {

        pdRechargePullRefreshView.setLoading();

        MemberFundModel.get().pdRechargeList(pdRechargePageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pdRechargePageInt == 1) {
                    pdRechargeArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    pdRechargePageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                pdRechargeArrayList.addAll(JsonUtil.json2ArrayList(data, PreDepositRechargeLogBean.class));
                pdRechargePullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                pdRechargePullRefreshView.setFailure();
            }
        });

    }

    private void getPdCashLog() {

        pdCashPullRefreshView.setLoading();

        MemberFundModel.get().pdCashList(pdCashPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pdCashPageInt == 1) {
                    pdCashArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    pdCashPageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                pdCashArrayList.addAll(JsonUtil.json2ArrayList(data, PreDepositCashLogBean.class));
                pdCashPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                pdCashPullRefreshView.setFailure();
            }
        });

    }

}
