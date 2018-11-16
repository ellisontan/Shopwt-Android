package top.yokey.shopwt.activity.main;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import top.yokey.base.base.MemberHttpClient;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.base.LoginActivity;
import top.yokey.shopwt.activity.mine.AddressActivity;
import top.yokey.shopwt.activity.mine.DistributionActivity;
import top.yokey.shopwt.activity.mine.FeedbackActivity;
import top.yokey.shopwt.activity.mine.FootprintActivity;
import top.yokey.shopwt.activity.mine.InviteActivity;
import top.yokey.shopwt.activity.mine.PointsActivity;
import top.yokey.shopwt.activity.mine.PreDepositActivity;
import top.yokey.shopwt.activity.mine.PropertyActivity;
import top.yokey.shopwt.activity.mine.RechargeCardActivity;
import top.yokey.shopwt.activity.mine.RedPacketActivity;
import top.yokey.shopwt.activity.mine.CenterActivity;
import top.yokey.shopwt.activity.mine.SettingActivity;
import top.yokey.shopwt.activity.mine.SignActivity;
import top.yokey.shopwt.activity.mine.VoucherActivity;
import top.yokey.shopwt.activity.points.ExchangeActivity;
import top.yokey.shopwt.activity.refund.RefundActivity;
import top.yokey.shopwt.activity.seller.SellerActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseBusClient;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseShared;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.MemberAssetBean;
import top.yokey.base.bean.MemberBean;
import top.yokey.base.event.MessageCountEvent;
import top.yokey.base.model.MemberAccountModel;
import top.yokey.base.model.MemberChatModel;
import top.yokey.base.model.MemberIndexModel;
import top.yokey.base.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_main_mine)
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.mineLinearLayout)
    private LinearLayoutCompat mineLinearLayout;
    @ViewInject(R.id.avatarImageView)
    private AppCompatImageView avatarImageView;
    @ViewInject(R.id.nicknameTextView)
    private AppCompatTextView nicknameTextView;

    @ViewInject(R.id.goodsTextView)
    private AppCompatTextView goodsTextView;
    @ViewInject(R.id.storeTextView)
    private AppCompatTextView storeTextView;
    @ViewInject(R.id.footprintTextView)
    private AppCompatTextView footprintTextView;
    @ViewInject(R.id.signTextView)
    private AppCompatTextView signTextView;

    @ViewInject(R.id.orderRelativeLayout)
    private RelativeLayout orderRelativeLayout;
    @ViewInject(R.id.waitPaymentRelativeLayout)
    private RelativeLayout waitPaymentRelativeLayout;
    @ViewInject(R.id.waitReceiptRelativeLayout)
    private RelativeLayout waitReceiptRelativeLayout;
    @ViewInject(R.id.waitTakesRelativeLayout)
    private RelativeLayout waitTakesRelativeLayout;
    @ViewInject(R.id.waitEvaluateRelativeLayout)
    private RelativeLayout waitEvaluateRelativeLayout;
    @ViewInject(R.id.waitReturnRelativeLayout)
    private RelativeLayout waitReturnRelativeLayout;
    @ViewInject(R.id.waitPaymentDotTextView)
    private AppCompatTextView waitPaymentDotTextView;
    @ViewInject(R.id.waitReceiptDotTextView)
    private AppCompatTextView waitReceiptDotTextView;
    @ViewInject(R.id.waitTakesDotTextView)
    private AppCompatTextView waitTakesDotTextView;
    @ViewInject(R.id.waitEvaluateDotTextView)
    private AppCompatTextView waitEvaluateDotTextView;
    @ViewInject(R.id.waitReturnDotTextView)
    private AppCompatTextView waitReturnDotTextView;

    @ViewInject(R.id.propertyRelativeLayout)
    private RelativeLayout propertyRelativeLayout;
    @ViewInject(R.id.preDepositTextView)
    private AppCompatTextView preDepositTextView;
    @ViewInject(R.id.rechargeCardTextView)
    private AppCompatTextView rechargeCardTextView;
    @ViewInject(R.id.voucherTextView)
    private AppCompatTextView voucherTextView;
    @ViewInject(R.id.redPacketTextView)
    private AppCompatTextView redPacketTextView;
    @ViewInject(R.id.pointsTextView)
    private AppCompatTextView pointsTextView;

    @ViewInject(R.id.inviteTextView)
    private AppCompatTextView inviteTextView;
    @ViewInject(R.id.distributionTextView)
    private AppCompatTextView distributionTextView;
    @ViewInject(R.id.addressTextView)
    private AppCompatTextView addressTextView;
    @ViewInject(R.id.sellerTextView)
    private AppCompatTextView sellerTextView;
    @ViewInject(R.id.exchangeTextView)
    private AppCompatTextView exchangeTextView;
    @ViewInject(R.id.feedbackTextView)
    private AppCompatTextView feedbackTextView;
    @ViewInject(R.id.settingTextView)
    private AppCompatTextView settingTextView;

    @Override
    public void initData() {

        if (BaseApplication.get().isLogin()) {
            getData();
            getMessageCount();
        }

    }

    @Override
    public void initEven() {

        mineLinearLayout.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), CenterActivity.class));

        goodsTextView.setOnClickListener(view -> BaseApplication.get().startFavorites(getActivity(), 0));

        storeTextView.setOnClickListener(view -> BaseApplication.get().startFavorites(getActivity(), 1));

        footprintTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), FootprintActivity.class));

        signTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), SignActivity.class));

        orderRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrder(getActivity(), 0));

        waitPaymentRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrder(getActivity(), 1));

        waitReceiptRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrder(getActivity(), 2));

        waitTakesRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrder(getActivity(), 3));

        waitEvaluateRelativeLayout.setOnClickListener(view -> BaseApplication.get().startOrder(getActivity(), 4));

        waitReturnRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), RefundActivity.class));

        propertyRelativeLayout.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
                return;
            }
            if (BaseApplication.get().getMemberAssetBean() == null) {
                return;
            }
            BaseApplication.get().start(getActivity(), PropertyActivity.class);
        });

        preDepositTextView.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
                return;
            }
            if (BaseApplication.get().getMemberAssetBean() == null) {
                return;
            }
            BaseApplication.get().start(getActivity(), PreDepositActivity.class);
        });

        rechargeCardTextView.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
                return;
            }
            if (BaseApplication.get().getMemberAssetBean() == null) {
                return;
            }
            BaseApplication.get().start(getActivity(), RechargeCardActivity.class);
        });

        voucherTextView.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
                return;
            }
            if (BaseApplication.get().getMemberAssetBean() == null) {
                return;
            }
            BaseApplication.get().start(getActivity(), VoucherActivity.class);
        });

        redPacketTextView.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
                return;
            }
            if (BaseApplication.get().getMemberAssetBean() == null) {
                return;
            }
            BaseApplication.get().start(getActivity(), RedPacketActivity.class);
        });

        pointsTextView.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
                return;
            }
            if (BaseApplication.get().getMemberAssetBean() == null) {
                return;
            }
            BaseApplication.get().start(getActivity(), PointsActivity.class);
        });

        inviteTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), InviteActivity.class));

        distributionTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), DistributionActivity.class));

        addressTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), AddressActivity.class));

        sellerTextView.setOnClickListener(view -> BaseApplication.get().startCheckSellerLogin(getActivity(), SellerActivity.class));

        exchangeTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), ExchangeActivity.class));

        feedbackTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), FeedbackActivity.class));

        settingTextView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), SettingActivity.class));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (BaseApplication.get().isLogin()) {
            getData();
        } else {
            nicknameTextView.setText("请登录");
        }
    }

    //自定义方法

    private void getData() {

        MemberIndexModel.get().index(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "member_info");
                BaseApplication.get().setMemberBean(JsonUtil.json2Bean(data, MemberBean.class));
                BaseImageLoader.get().displayCircle(BaseApplication.get().getMemberBean().getAvatar(), avatarImageView);
                nicknameTextView.setText(BaseApplication.get().getMemberBean().getUserName());
                goodsTextView.setText("商品");
                if (!TextUtils.isEmpty(BaseApplication.get().getMemberBean().getFavoritesGoods())) {
                    goodsTextView.append("：" + BaseApplication.get().getMemberBean().getFavoritesGoods());
                }
                storeTextView.setText("店铺");
                if (!TextUtils.isEmpty(BaseApplication.get().getMemberBean().getFavoritesStore())) {
                    storeTextView.append("：" + BaseApplication.get().getMemberBean().getFavoritesStore());
                }
                waitPaymentDotTextView.setVisibility(BaseApplication.get().getMemberBean().getOrderNopayCount().equals("0") ? View.GONE : View.VISIBLE);
                waitReceiptDotTextView.setVisibility(BaseApplication.get().getMemberBean().getOrderNoreceiptCount().equals("0") ? View.GONE : View.VISIBLE);
                waitTakesDotTextView.setVisibility(BaseApplication.get().getMemberBean().getOrderNotakesCount().equals("0") ? View.GONE : View.VISIBLE);
                waitEvaluateDotTextView.setVisibility(BaseApplication.get().getMemberBean().getOrderNoevalCount().equals("0") ? View.GONE : View.VISIBLE);
                waitReturnDotTextView.setVisibility(BaseApplication.get().getMemberBean().getReturnX().equals("0") ? View.GONE : View.VISIBLE);
                getMobileInfo();
            }

            @Override
            public void onFailure(String reason) {
                if (reason.equals("请登录")) {
                    MemberHttpClient.get().updateKey("");
                    BaseToast.get().show(reason);
                    BaseShared.get().putString(BaseConstant.SHARED_KEY, "");
                    BaseApplication.get().start(getActivity(), LoginActivity.class);
                } else {
                    new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                        @Override
                        public void onFinish() {
                            super.onFinish();
                            getData();
                        }
                    }.start();
                }
            }
        });

    }

    private void getMobileInfo() {

        MemberAccountModel.get().getMobileInfo(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    JSONObject jsonObject = new JSONObject(baseBean.getDatas());
                    BaseApplication.get().getMemberBean().setMobielState(jsonObject.getBoolean("state"));
                    BaseApplication.get().getMemberBean().setUserMobile(jsonObject.getString("mobile"));
                    getMemberAsset();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getMobileInfo();
                    }
                }.start();
            }
        });

    }

    private void getMemberAsset() {

        MemberIndexModel.get().myAsset(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseApplication.get().setMemberAssetBean(JsonUtil.json2Bean(baseBean.getDatas(), MemberAssetBean.class));
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_MESSAGE, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getMemberAsset();
                    }
                }.start();
            }
        });

    }

    private void getMessageCount() {

        MemberChatModel.get().getMsgCount(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (!baseBean.getDatas().equals("0")) {
                    BaseBusClient.get().post(new MessageCountEvent(true));
                }
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getMessageCount();
                    }
                }.start();
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getMessageCount();
                    }
                }.start();
            }
        });

    }

}
