package top.yokey.shopwt.activity.home;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.VoucherPlatformListAdapter;
import top.yokey.shopwt.adapter.VoucherStoreListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseLogger;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.VoucherPlatformBean;
import top.yokey.base.bean.VoucherStoreBean;
import top.yokey.base.model.MemberVoucherModel;
import top.yokey.base.model.VoucherModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.view.PullRefreshView;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class VoucherActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView platformTextView;
    private AppCompatTextView storeTextView;
    private PullRefreshView mainPullRefreshView;

    private int position;

    private int platformPage;
    private VoucherPlatformListAdapter platformAdapter;
    private ArrayList<VoucherPlatformBean> platformArrayList;

    private int storePage;
    private VoucherStoreListAdapter storeAdapter;
    private ArrayList<VoucherStoreBean> storeArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_home_voucher);
        mainToolbar = findViewById(R.id.mainToolbar);
        platformTextView = findViewById(R.id.platformTextView);
        storeTextView = findViewById(R.id.storeTextView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar);

        position = getIntent().getIntExtra(BaseConstant.DATA_POSITION, 0);

        platformPage = 1;
        platformArrayList = new ArrayList<>();
        platformAdapter = new VoucherPlatformListAdapter(platformArrayList);

        storePage = 1;
        storeArrayList = new ArrayList<>();
        storeAdapter = new VoucherStoreListAdapter(storeArrayList);

        if (position == 0) {
            platformTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            platformTextView.setBackgroundResource(R.drawable.border_toolbar_left_press);
            storeTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            storeTextView.setBackgroundResource(R.drawable.border_toolbar_right);
            mainPullRefreshView.getRecyclerView().setAdapter(platformAdapter);
            getPlatform();
        } else {
            mainPullRefreshView.getRecyclerView().setAdapter(storeAdapter);
            platformTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            platformTextView.setBackgroundResource(R.drawable.border_toolbar_left);
            storeTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            storeTextView.setBackgroundResource(R.drawable.border_toolbar_right_press);
            mainPullRefreshView.getRecyclerView().setAdapter(storeAdapter);
            getStore();
        }

    }

    @Override
    public void initEven() {

        platformTextView.setOnClickListener(view -> {
            platformTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            platformTextView.setBackgroundResource(R.drawable.border_toolbar_left_press);
            storeTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            storeTextView.setBackgroundResource(R.drawable.border_toolbar_right);
            mainPullRefreshView.getRecyclerView().setAdapter(platformAdapter);
            position = 0;
            if (platformPage == 1) {
                getPlatform();
            } else {
                platformAdapter.notifyDataSetChanged();
            }
        });

        storeTextView.setOnClickListener(view -> {
            platformTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            platformTextView.setBackgroundResource(R.drawable.border_toolbar_left);
            storeTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            storeTextView.setBackgroundResource(R.drawable.border_toolbar_right_press);
            mainPullRefreshView.getRecyclerView().setAdapter(storeAdapter);
            position = 1;
            if (storePage == 1) {
                getStore();
            } else {
                storeAdapter.notifyDataSetChanged();
            }
        });

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                if (position == 0) {
                    getPlatform();
                } else {
                    getStore();
                }
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (position == 0) {
                    platformPage = 1;
                    getPlatform();
                } else {
                    storePage = 1;
                    getStore();
                }
            }

            @Override
            public void onLoadMore() {
                if (position == 0) {
                    getPlatform();
                } else {
                    getStore();
                }
            }
        });

        platformAdapter.setOnItemClickListener((position, bean) -> {
            BaseToast.get().show("领取中...");
            MemberVoucherModel.get().getCoupon(bean.getCouponTId(), new BaseHttpListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    if (JsonUtil.isSuccess(baseBean.getDatas())) {
                        BaseToast.get().show("领取成功！");
                    } else {
                        BaseToast.get().show(baseBean.getDatas());
                    }
                }

                @Override
                public void onFailure(String reason) {
                    BaseToast.get().show(reason);
                }
            });
        });

        storeAdapter.setOnItemClickListener((position, bean) -> {
            BaseToast.get().show("领取中...");
            MemberVoucherModel.get().voucherFreeex(bean.getVoucherTId(), new BaseHttpListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    if (JsonUtil.isSuccess(baseBean.getDatas())) {
                        BaseToast.get().show("领取成功！");
                    } else {
                        BaseToast.get().show(baseBean.getDatas());
                    }
                }

                @Override
                public void onFailure(String reason) {
                    BaseToast.get().show(reason);
                }
            });
        });

    }

    //自定义方法

    private void getPlatform() {

        mainPullRefreshView.setLoading();

        VoucherModel.get().couponList(platformPage + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (platformPage == 1) {
                    platformArrayList.clear();
                }
                if (platformPage <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "coupon_list");
                    platformArrayList.addAll(JsonUtil.json2ArrayList(data, VoucherPlatformBean.class));
                    platformPage++;
                }
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

    private void getStore() {

        mainPullRefreshView.setLoading();

        VoucherModel.get().voucherList(storePage + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (storePage == 1) {
                    storeArrayList.clear();
                }
                if (storePage <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "voucher_list");
                    storeArrayList.addAll(JsonUtil.json2ArrayList(data, VoucherStoreBean.class));
                    storePage++;
                }
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseLogger.get().show(reason);
                mainPullRefreshView.setFailure();
            }
        });

    }

}
