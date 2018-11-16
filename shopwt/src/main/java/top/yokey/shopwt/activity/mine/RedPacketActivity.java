package top.yokey.shopwt.activity.mine;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.RedPacketListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.RedPacketBean;
import top.yokey.base.model.MemberRedPackerModel;
import top.yokey.base.model.SeccodeModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RedPacketActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private int pageInt;
    private PullRefreshView mainPullRefreshView;
    private RedPacketListAdapter mainAdapter;
    private ArrayList<RedPacketBean> mainArrayList;

    private String codeKeyString;
    private AppCompatEditText codeEditText;
    private AppCompatEditText captchaEditText;
    private AppCompatImageView captchaImageView;
    private AppCompatTextView submitTextView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_red_packet);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "平台红包");

        List<String> titleList = new ArrayList<>();
        titleList.add("我的红包");
        titleList.add("领取红包");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_mine_red_packet, null));

        //充值卡余额
        pageInt = 1;
        mainArrayList = new ArrayList<>();
        mainAdapter = new RedPacketListAdapter(mainArrayList);
        mainPullRefreshView = viewList.get(0).findViewById(R.id.mainPullRefreshView);
        mainPullRefreshView.getRecyclerView().setPadding(0, BaseApplication.get().dipToPx(8), 0, BaseApplication.get().dipToPx(8));
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        //充值卡充值
        codeKeyString = "";
        codeEditText = viewList.get(1).findViewById(R.id.codeEditText);
        captchaEditText = viewList.get(1).findViewById(R.id.captchaEditText);
        captchaImageView = viewList.get(1).findViewById(R.id.captchaImageView);
        submitTextView = viewList.get(1).findViewById(R.id.submitTextView);

        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);

        getRedPack();
        makeCodeKey();

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                pageInt = 1;
                getRedPack();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageInt = 1;
                getRedPack();
            }

            @Override
            public void onLoadMore() {
                getRedPack();
            }
        });

        mainAdapter.setOnItemClickListener((position, redPacketBean) -> {

        });

        captchaImageView.setOnClickListener(view -> makeCodeKey());

        submitTextView.setOnClickListener(view -> submit());

    }

    //自定义方法

    private void getRedPack() {

        mainPullRefreshView.setLoading();

        MemberRedPackerModel.get().redPacketList(pageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt == 1) {
                    mainArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    pageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "redpacket_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, RedPacketBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

    private void makeCodeKey() {

        SeccodeModel.get().makeCodeKey(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                codeKeyString = JsonUtil.getDatasString(baseBean.getDatas(), "codekey");
                BaseImageLoader.get().display(SeccodeModel.get().makeCode(codeKeyString), captchaImageView);
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        makeCodeKey();
                    }
                }.start();
            }
        });

    }

    private void submit() {

        String sn = codeEditText.getText().toString();
        String captcha = captchaEditText.getText().toString();

        if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(captcha)) {
            BaseToast.get().show("请输入所有的内容！");
            return;
        }

        submitTextView.setEnabled(false);
        submitTextView.setText("提交中...");

        MemberRedPackerModel.get().redPacketPwex(sn, captcha, codeKeyString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                pageInt = 1;
                getRedPack();
                makeCodeKey();
                codeEditText.setText("");
                captchaEditText.setText("");
                submitTextView.setEnabled(true);
                submitTextView.setText("确认提交");
                BaseToast.get().show("红包兑换成功！");
            }

            @Override
            public void onFailure(String reason) {
                makeCodeKey();
                captchaEditText.setText("");
                submitTextView.setEnabled(true);
                submitTextView.setText("确认提交");
                BaseToast.get().show(reason);
            }
        });

    }

}
