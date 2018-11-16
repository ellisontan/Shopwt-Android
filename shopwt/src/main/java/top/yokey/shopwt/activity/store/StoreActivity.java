package top.yokey.shopwt.activity.store;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.main.MainActivity;
import top.yokey.shopwt.adapter.BaseFragmentAdapter;
import top.yokey.shopwt.adapter.VoucherGoodsListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.base.base.BaseAnimClient;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseBusClient;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.StoreInfoBean;
import top.yokey.base.bean.VoucherGoodsBean;
import top.yokey.base.event.GoodsShowEvent;
import top.yokey.base.event.StoreBeanEvent;
import top.yokey.base.model.MemberVoucherModel;
import top.yokey.base.model.StoreModel;
import top.yokey.base.model.VoucherModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class StoreActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText searchEditText;
    private AppCompatImageView toolbarImageView;
    private AppCompatTextView homeTextView;
    private AppCompatTextView goodsTextView;
    private AppCompatTextView newTextView;
    private AppCompatTextView activityTextView;
    private ViewPager mainViewPager;
    private AppCompatTextView detailedTextView;
    private AppCompatTextView voucherTextView;
    private AppCompatTextView customerTextView;
    private LinearLayoutCompat voucherLinearLayout;
    private RecyclerView voucherRecyclerView;
    private AppCompatTextView nightTextView;

    private AppCompatTextView[] navigationTextView;
    private Drawable[] navigationPressDrawable;
    private Drawable[] navigationNormalDrawable;

    private String storeIdString;
    private StoreInfoBean storeInfoBean;
    private VoucherGoodsListAdapter voucherAdapter;
    private ArrayList<VoucherGoodsBean> voucherArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_store_store);
        mainToolbar = findViewById(R.id.mainToolbar);
        searchEditText = findViewById(R.id.searchEditText);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        homeTextView = findViewById(R.id.homeTextView);
        goodsTextView = findViewById(R.id.goodsTextView);
        newTextView = findViewById(R.id.newTextView);
        activityTextView = findViewById(R.id.activityTextView);
        mainViewPager = findViewById(R.id.mainViewPager);
        detailedTextView = findViewById(R.id.detailedTextView);
        voucherTextView = findViewById(R.id.voucherTextView);
        customerTextView = findViewById(R.id.customerTextView);
        voucherLinearLayout = findViewById(R.id.voucherLinearLayout);
        voucherRecyclerView = findViewById(R.id.voucherRecyclerView);
        nightTextView = findViewById(R.id.nightTextView);

    }

    @Override
    public void initData() {

        storeIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(storeIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        navigationTextView = new AppCompatTextView[4];
        navigationTextView[0] = homeTextView;
        navigationTextView[1] = goodsTextView;
        navigationTextView[2] = newTextView;
        navigationTextView[3] = activityTextView;

        navigationNormalDrawable = new Drawable[navigationTextView.length];
        navigationNormalDrawable[0] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_home, R.color.grey);
        navigationNormalDrawable[1] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_goods, R.color.grey);
        navigationNormalDrawable[2] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_new, R.color.grey);
        navigationNormalDrawable[3] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_activity, R.color.grey);

        navigationPressDrawable = new Drawable[navigationTextView.length];
        navigationPressDrawable[0] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_home_press);
        navigationPressDrawable[1] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_goods_press);
        navigationPressDrawable[2] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_new_press);
        navigationPressDrawable[3] = BaseApplication.get().getMipmap(R.mipmap.ic_navigation_store_activity_press);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new GoodsFragment());
        fragmentList.add(new NewFragment());
        fragmentList.add(new ActivityFragment());

        mainViewPager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), fragmentList));
        mainViewPager.setOffscreenPageLimit(navigationTextView.length);

        setToolbar(mainToolbar, "");
        toolbarImageView.setImageResource(R.drawable.ic_action_search);

        storeInfoBean = new StoreInfoBean();
        voucherArrayList = new ArrayList<>();
        voucherAdapter = new VoucherGoodsListAdapter(voucherArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), voucherRecyclerView, voucherAdapter);


        updateNavigation(0);
        storeInfo();

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> BaseApplication.get().startStoreGoodsList(getActivity(), storeIdString, searchEditText.getText().toString(), ""));

        for (int i = 0; i < navigationTextView.length; i++) {
            final int position = i;
            navigationTextView[i].setOnClickListener(view -> updateNavigation(position));
        }

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateNavigation(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        detailedTextView.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            intent.putExtra(BaseConstant.DATA_ID, storeIdString);
            BaseApplication.get().start(getActivity(), intent);
        });

        voucherTextView.setOnClickListener(view -> showVoucherLayout());

        customerTextView.setOnClickListener(view -> BaseApplication.get().startChatOnly(getActivity(), storeInfoBean.getMemberId(), ""));

        nightTextView.setOnClickListener(view -> goneVoucherLayout());

        voucherAdapter.setOnItemClickListener((position, voucherGoodsBean) -> voucherFreeex(voucherGoodsBean.getVoucherTId()));

    }

    @Override
    public void onReturn() {

        if (mainViewPager.getCurrentItem() != 0) {
            mainViewPager.setCurrentItem(0);
            return;
        }

        if (voucherLinearLayout.getVisibility() == View.VISIBLE) {
            goneVoucherLayout();
            return;
        }

        if (BaseApplication.get().inActivityStackTop() && !BaseApplication.get().inActivityStack(MainActivity.class)) {
            BaseApplication.get().start(getActivity(), MainActivity.class);
        }

        super.onReturn();

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            onReturn();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    //自定义方法

    private void storeInfo() {

        StoreModel.get().storeInfo(storeIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "store_info");
                storeInfoBean = JsonUtil.json2Bean(data, StoreInfoBean.class);
                BaseBusClient.get().post(new StoreBeanEvent(baseBean));
                getVoucher();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        storeInfo();
                    }
                }.start();
            }
        });

    }

    private void getVoucher() {

        VoucherModel.get().voucherTplList(storeIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                voucherArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "voucher_list");
                voucherArrayList.addAll(JsonUtil.json2ArrayList(data, VoucherGoodsBean.class));
                voucherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getVoucher();
                    }
                }.start();
            }
        });

    }

    private void goneVoucherLayout() {

        BaseBusClient.get().post(new GoodsShowEvent(false));

        if (nightTextView.getVisibility() == View.VISIBLE) {
            nightTextView.setVisibility(View.GONE);
            BaseAnimClient.get().goneAlpha(nightTextView);
        }

        if (voucherLinearLayout.getVisibility() == View.VISIBLE) {
            voucherLinearLayout.setVisibility(View.GONE);
            //BaseAnimClient.get().downTranslate(voucherLinearLayout, voucherLinearLayout.getHeight());
        }

    }

    private void showVoucherLayout() {

        BaseBusClient.get().post(new GoodsShowEvent(true));

        if (nightTextView.getVisibility() == View.GONE) {
            nightTextView.setVisibility(View.VISIBLE);
            BaseAnimClient.get().showAlpha(nightTextView);
        }

        if (voucherLinearLayout.getVisibility() == View.GONE) {
            voucherLinearLayout.setVisibility(View.VISIBLE);
            //BaseAnimClient.get().upTranslate(voucherLinearLayout, voucherLinearLayout.getHeight());
        }

    }

    private void voucherFreeex(String tid) {

        MemberVoucherModel.get().voucherFreeex(tid, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().showSuccess();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void updateNavigation(int position) {

        for (int i = 0; i < navigationTextView.length; i++) {
            navigationTextView[i].setTextColor(BaseApplication.get().getColors(R.color.grey));
            navigationTextView[i].setCompoundDrawablesWithIntrinsicBounds(null, navigationNormalDrawable[i], null, null);
        }

        navigationTextView[position].setTextColor(BaseApplication.get().getColors(R.color.primary));
        navigationTextView[position].setCompoundDrawablesWithIntrinsicBounds(null, navigationPressDrawable[position], null, null);
        mainViewPager.setCurrentItem(position);

    }

}
