package top.yokey.shopwt.activity.mine;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.DistributionListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.bean.MemberDistributionBean;
import top.yokey.base.model.MemberInviteModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class DistributionActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private int onePageInt;
    private PullRefreshView onePullRefreshView;
    private DistributionListAdapter oneAdapter;
    private ArrayList<MemberDistributionBean> oneArrayList;

    private int twoPageInt;
    private PullRefreshView twoPullRefreshView;
    private DistributionListAdapter twoAdapter;
    private ArrayList<MemberDistributionBean> twoArrayList;

    private int thrPageInt;
    private PullRefreshView thrPullRefreshView;
    private DistributionListAdapter thrAdapter;
    private ArrayList<MemberDistributionBean> thrArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_tab_view_pager);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "分销收入");

        List<String> titleList = new ArrayList<>();
        titleList.add("一级下线");
        titleList.add("二级下线");
        titleList.add("三级下线");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));

        onePageInt = 1;
        oneArrayList = new ArrayList<>();
        oneAdapter = new DistributionListAdapter(oneArrayList);
        onePullRefreshView = viewList.get(0).findViewById(R.id.mainPullRefreshView);
        onePullRefreshView.getRecyclerView().setAdapter(oneAdapter);

        twoPageInt = 1;
        twoArrayList = new ArrayList<>();
        twoAdapter = new DistributionListAdapter(twoArrayList);
        twoPullRefreshView = viewList.get(1).findViewById(R.id.mainPullRefreshView);
        twoPullRefreshView.getRecyclerView().setAdapter(twoAdapter);

        thrPageInt = 1;
        thrArrayList = new ArrayList<>();
        thrAdapter = new DistributionListAdapter(thrArrayList);
        thrPullRefreshView = viewList.get(2).findViewById(R.id.mainPullRefreshView);
        thrPullRefreshView.getRecyclerView().setAdapter(thrAdapter);

        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);
        getOne();
        getTwo();
        getThr();

    }

    @Override
    public void initEven() {

        onePullRefreshView.setOnClickListener(view -> {
            if (onePullRefreshView.isFailure()) {
                getOne();
            }
        });

        onePullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onePageInt = 1;
                getOne();
            }

            @Override
            public void onLoadMore() {
                getOne();
            }
        });

        oneAdapter.setOnItemClickListener((position, bean) -> {

        });

        twoPullRefreshView.setOnClickListener(view -> {
            if (twoPullRefreshView.isFailure()) {
                getTwo();
            }
        });

        twoPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                twoPageInt = 1;
                getTwo();
            }

            @Override
            public void onLoadMore() {
                getTwo();
            }
        });

        twoAdapter.setOnItemClickListener((position, bean) -> {

        });

        thrPullRefreshView.setOnClickListener(view -> {
            if (thrPullRefreshView.isFailure()) {
                getThr();
            }
        });

        thrPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                thrPageInt = 1;
                getThr();
            }

            @Override
            public void onLoadMore() {
                getThr();
            }
        });

        thrAdapter.setOnItemClickListener((position, bean) -> {

        });

    }

    //自定义方法

    private void getOne() {

        onePullRefreshView.setLoading();

        MemberInviteModel.get().inviteone(onePageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (onePageInt == 1) {
                    oneArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    onePageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                oneArrayList.addAll(JsonUtil.json2ArrayList(data, MemberDistributionBean.class));
                onePullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                onePullRefreshView.setFailure();
            }
        });

    }

    private void getTwo() {

        twoPullRefreshView.setLoading();

        MemberInviteModel.get().invitetwo(twoPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (twoPageInt == 1) {
                    twoArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    twoPageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                twoArrayList.addAll(JsonUtil.json2ArrayList(data, MemberDistributionBean.class));
                twoPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                twoPullRefreshView.setFailure();
            }
        });

    }

    private void getThr() {

        thrPullRefreshView.setLoading();

        MemberInviteModel.get().invitethir(thrPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (thrPageInt == 1) {
                    thrArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    thrPageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                thrArrayList.addAll(JsonUtil.json2ArrayList(data, MemberDistributionBean.class));
                thrPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                thrPullRefreshView.setFailure();
            }
        });

    }

}
