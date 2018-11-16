package top.yokey.shopwt.activity.points;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.OrderPointsDetailedBean;
import top.yokey.base.model.MemberPointOrderModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.shopwt.base.BaseImageLoader;

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
    private RelativeLayout messageRelativeLayout;
    private AppCompatTextView messageContentTextView;
    private AppCompatImageView mainImageView;
    private AppCompatTextView nameTextView;
    private AppCompatTextView pointsTextView;
    private AppCompatTextView numberTextView;
    private AppCompatTextView snTextView;
    private AppCompatTextView createTimeTextView;
    private RelativeLayout goodsRelativeLayout;

    private String orderIdString;
    private OrderPointsDetailedBean orderPointsDetailedBean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_points_detailed);
        mainToolbar = findViewById(R.id.mainToolbar);
        stateTextView = findViewById(R.id.stateTextView);
        addressNameTextView = findViewById(R.id.addressNameTextView);
        addressMobileTextView = findViewById(R.id.addressMobileTextView);
        addressAreaTextView = findViewById(R.id.addressAreaTextView);
        messageRelativeLayout = findViewById(R.id.messageRelativeLayout);
        messageContentTextView = findViewById(R.id.messageContentTextView);
        mainImageView = findViewById(R.id.mainImageView);
        nameTextView = findViewById(R.id.nameTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        numberTextView = findViewById(R.id.numberTextView);
        snTextView = findViewById(R.id.snTextView);
        createTimeTextView = findViewById(R.id.createTimeTextView);
        goodsRelativeLayout = findViewById(R.id.goodsRelativeLayout);

    }

    @Override
    public void initData() {

        orderIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(orderIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "订单详细");
        orderPointsDetailedBean = null;
        getData();

    }

    @Override
    public void initEven() {

        goodsRelativeLayout.setOnClickListener(view -> {
            if (orderPointsDetailedBean != null) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                intent.putExtra(BaseConstant.DATA_ID, orderPointsDetailedBean.getProdList().get(0).getPointGoodsid());
                BaseApplication.get().start(getActivity(), intent);
            }
        });

    }

    //自定义方法

    private void getData() {

        BaseDialog.get().progress(getActivity());

        MemberPointOrderModel.get().orderInfo(orderIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseDialog.get().cancel();
                orderPointsDetailedBean = JsonUtil.json2Bean(baseBean.getDatas(), OrderPointsDetailedBean.class);
                stateTextView.setText(orderPointsDetailedBean.getOrderInfo().getPointOrderstatetext());
                addressNameTextView.setText(orderPointsDetailedBean.getOrderaddressInfo().getPointTruename());
                addressMobileTextView.setText(orderPointsDetailedBean.getOrderaddressInfo().getPointMobphone());
                addressAreaTextView.setText(orderPointsDetailedBean.getOrderaddressInfo().getPointAreainfo());
                addressAreaTextView.append(orderPointsDetailedBean.getOrderaddressInfo().getPointAddress());
                BaseImageLoader.get().display(orderPointsDetailedBean.getProdList().get(0).getPointGoodsimage(), mainImageView);
                nameTextView.setText(orderPointsDetailedBean.getProdList().get(0).getPointGoodsname());
                pointsTextView.setText(orderPointsDetailedBean.getProdList().get(0).getPointGoodspoints());
                pointsTextView.append("积分");
                numberTextView.setText(orderPointsDetailedBean.getProdList().get(0).getPointGoodsnum());
                numberTextView.append("件");
                snTextView.setText("订单编号：");
                snTextView.append(orderPointsDetailedBean.getOrderInfo().getPointOrdersn());
                createTimeTextView.setText("创建时间：");
                createTimeTextView.append(orderPointsDetailedBean.getOrderInfo().getPointAddtime());
                if (TextUtils.isEmpty(orderPointsDetailedBean.getOrderInfo().getPointOrdermessage()) ||
                        orderPointsDetailedBean.getOrderInfo().getPointOrdermessage().equals("null")) {
                    messageRelativeLayout.setVisibility(View.GONE);
                } else {
                    messageRelativeLayout.setVisibility(View.VISIBLE);
                    messageContentTextView.setText(orderPointsDetailedBean.getOrderInfo().getPointOrdermessage());
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
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

}
