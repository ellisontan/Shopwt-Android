package top.yokey.shopwt.activity.refund;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.GoodsRefundApplyListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseFileClient;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.FileUploadBean;
import top.yokey.base.bean.RefundApplyBean;
import top.yokey.base.model.MemberRefundModel;
import top.yokey.base.util.JsonUtil;

import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RefundApplyActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView storeNameTextView;
    private RecyclerView mainRecyclerView;
    private LinearLayoutCompat zengPinLinearLayout;
    private AppCompatTextView zengPinDescTextView;
    private AppCompatImageView zengPinGoodsImageView;
    private RelativeLayout goodsRelativeLayout;
    private AppCompatImageView goodsImageView;
    private AppCompatTextView goodsNameTextView;
    private AppCompatTextView goodsMoneyTextView;
    private AppCompatTextView goodsSpecTextView;
    private AppCompatTextView goodsNumberTextView;
    private AppCompatTextView reasonTextView;
    private AppCompatSpinner reasonSpinner;
    private AppCompatTextView moneyTextView;
    private AppCompatEditText remarkEditText;
    private AppCompatImageView zeroImageView;
    private AppCompatImageView oneImageView;
    private AppCompatImageView twoImageView;
    private AppCompatTextView submitTextView;

    private int positionInt;
    private String orderIdString;
    private String goodsIdString;
    private String reasonIdString;
    private String refundPic0String;
    private String refundPic1String;
    private String refundPic2String;
    private Vector<String> reasonIdVector;
    private RefundApplyBean orderRefundBean;
    private Vector<String> reasonContentVector;

    private GoodsRefundApplyListAdapter mainAdapter;
    private ArrayList<RefundApplyBean.GoodsListBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_refund_apply);
        mainToolbar = findViewById(R.id.mainToolbar);
        storeNameTextView = findViewById(R.id.storeNameTextView);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        zengPinLinearLayout = findViewById(R.id.zengPinLinearLayout);
        zengPinDescTextView = findViewById(R.id.zengPinDescTextView);
        zengPinGoodsImageView = findViewById(R.id.zengPinGoodsImageView);
        goodsRelativeLayout = findViewById(R.id.goodsRelativeLayout);
        goodsImageView = findViewById(R.id.goodsImageView);
        goodsNameTextView = findViewById(R.id.goodsNameTextView);
        goodsMoneyTextView = findViewById(R.id.goodsMoneyTextView);
        goodsSpecTextView = findViewById(R.id.goodsSpecTextView);
        goodsNumberTextView = findViewById(R.id.goodsNumberTextView);
        reasonTextView = findViewById(R.id.reasonTextView);
        reasonSpinner = findViewById(R.id.reasonSpinner);
        moneyTextView = findViewById(R.id.moneyTextView);
        remarkEditText = findViewById(R.id.remarkEditText);
        zeroImageView = findViewById(R.id.zeroImageView);
        oneImageView = findViewById(R.id.oneImageView);
        twoImageView = findViewById(R.id.twoImageView);
        submitTextView = findViewById(R.id.submitTextView);

    }

    @Override
    public void initData() {

        orderIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        goodsIdString = getIntent().getStringExtra(BaseConstant.DATA_GOODSID);
        if (TextUtils.isEmpty(orderIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        orderRefundBean = new RefundApplyBean();
        reasonIdVector = new Vector<>();
        reasonContentVector = new Vector<>();

        positionInt = 0;
        reasonIdString = "0";
        refundPic0String = "";
        refundPic1String = "";
        refundPic2String = "";
        mainArrayList = new ArrayList<>();
        mainAdapter = new GoodsRefundApplyListAdapter(mainArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), mainRecyclerView, mainAdapter);

        if (TextUtils.isEmpty(goodsIdString)) {
            setToolbar(mainToolbar, "订单退款");
            getDataAll();
        } else {
            setToolbar(mainToolbar, "商品退款");
            getData();
        }

    }

    @Override
    public void initEven() {

        storeNameTextView.setOnClickListener(view -> BaseApplication.get().startStore(getActivity(), orderRefundBean.getOrder().getStoreId()));

        mainAdapter.setOnItemClickListener((position, goodsListBean) -> BaseApplication.get().startGoods(getActivity(), goodsListBean.getGoodsId()));

        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonIdString = reasonIdVector.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        zeroImageView.setOnClickListener(view -> {
            positionInt = 0;
            BaseApplication.get().startMatisse(getActivity(), 1, BaseConstant.CODE_ALBUM);
        });

        oneImageView.setOnClickListener(view -> {
            positionInt = 1;
            BaseApplication.get().startMatisse(getActivity(), 1, BaseConstant.CODE_ALBUM);
        });

        twoImageView.setOnClickListener(view -> {
            positionInt = 2;
            BaseApplication.get().startMatisse(getActivity(), 1, BaseConstant.CODE_ALBUM);
        });

        submitTextView.setOnClickListener(view -> {
            if (TextUtils.isEmpty(goodsIdString)) {
                submitAll();
            } else {
                submit();
            }
        });

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK && req == BaseConstant.CODE_ALBUM) {
            updateImage(Matisse.obtainPathResult(intent).get(0));
        }
    }

    //自定义方法

    private void submit() {

        String remark = remarkEditText.getText().toString();

        if (TextUtils.isEmpty(remark)) {
            BaseToast.get().show("请输入原因！");
            return;
        }

        submitTextView.setEnabled(false);
        submitTextView.setText("提交中...");

        MemberRefundModel.get().refundPost(orderIdString, goodsIdString, "1", reasonIdString, orderRefundBean.getOrder().getOrderAmount(), remark, refundPic0String, refundPic1String, refundPic2String, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    BaseToast.get().show("申请成功！");
                    BaseApplication.get().finish(getActivity());
                } else {
                    BaseToast.get().showFailure();
                    submitTextView.setEnabled(true);
                    submitTextView.setText("提 交");
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                submitTextView.setEnabled(true);
                submitTextView.setText("提 交");
            }
        });

    }

    private void getData() {

        MemberRefundModel.get().refundForm(orderIdString, goodsIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                orderRefundBean = JsonUtil.json2Bean(baseBean.getDatas(), RefundApplyBean.class);
                storeNameTextView.setText(orderRefundBean.getOrder().getStoreName());
                moneyTextView.setText("￥");
                moneyTextView.append(orderRefundBean.getGoods().getGoodsPayPrice());
                zengPinLinearLayout.setVisibility(View.GONE);
                goodsRelativeLayout.setVisibility(View.VISIBLE);
                BaseImageLoader.get().display(orderRefundBean.getGoods().getGoodsImg360(), goodsImageView);
                goodsNameTextView.setText(orderRefundBean.getGoods().getGoodsName());
                goodsMoneyTextView.setText("￥");
                goodsMoneyTextView.append(orderRefundBean.getGoods().getGoodsPrice());
                goodsSpecTextView.setText(orderRefundBean.getGoods().getGoodsSpec());
                goodsNumberTextView.setText("x");
                goodsNumberTextView.append(orderRefundBean.getGoods().getGoodsNum());
                reasonSpinner.setVisibility(View.VISIBLE);
                reasonTextView.setVisibility(View.GONE);
                reasonIdVector.clear();
                reasonContentVector.clear();
                for (int i = 0; i < orderRefundBean.getReasonList().size(); i++) {
                    if (i == 0) {
                        reasonIdString = orderRefundBean.getReasonList().get(i).getReasonId();
                    }
                    reasonIdVector.add(orderRefundBean.getReasonList().get(i).getReasonId());
                    reasonContentVector.add(orderRefundBean.getReasonList().get(i).getReasonInfo());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, reasonContentVector);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reasonSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(String reason) {
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

    private void submitAll() {

        submitTextView.setEnabled(false);
        submitTextView.setText("提交中...");

        MemberRefundModel.get().refundAllPost(orderIdString, remarkEditText.getText().toString(), refundPic0String, refundPic1String, refundPic2String, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    BaseToast.get().show("申请成功！");
                    BaseApplication.get().finish(getActivity());
                } else {
                    BaseToast.get().showFailure();
                    submitTextView.setEnabled(true);
                    submitTextView.setText("提 交");
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                submitTextView.setEnabled(true);
                submitTextView.setText("提 交");
            }
        });

    }

    private void getDataAll() {

        MemberRefundModel.get().refundAllFrom(orderIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                orderRefundBean = JsonUtil.json2Bean(baseBean.getDatas(), RefundApplyBean.class);
                storeNameTextView.setText(orderRefundBean.getOrder().getStoreName());
                mainArrayList.clear();
                mainArrayList.addAll(orderRefundBean.getGoodsList());
                mainAdapter.notifyDataSetChanged();
                moneyTextView.setText("￥");
                moneyTextView.append(orderRefundBean.getOrder().getAllowRefundAmount());
                goodsRelativeLayout.setVisibility(View.GONE);
                reasonSpinner.setVisibility(View.GONE);
                reasonTextView.setVisibility(View.VISIBLE);
                if (orderRefundBean.getGiftList().size() == 0) {
                    zengPinLinearLayout.setVisibility(View.GONE);
                } else {
                    zengPinLinearLayout.setVisibility(View.VISIBLE);
                    zengPinDescTextView.setText(orderRefundBean.getGiftList().get(0).getGoodsName());
                    zengPinDescTextView.append(" x" + orderRefundBean.getGiftList().get(0).getGoodsNum());
                    BaseImageLoader.get().display(orderRefundBean.getGiftList().get(0).getGoodsImg360(), zengPinGoodsImageView);
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getDataAll();
                    }
                }.start();
            }
        });

    }

    private void updateImage(String path) {

        MemberRefundModel.get().uploadPic(BaseFileClient.get().createImage("evaluate" + positionInt, BaseImageLoader.get().getLocal(path)), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                FileUploadBean fileUploadBean = JsonUtil.json2Bean(baseBean.getDatas(), FileUploadBean.class);
                switch (positionInt) {
                    case 0:
                        refundPic0String = fileUploadBean.getFileName();
                        BaseImageLoader.get().display(fileUploadBean.getPic(), zeroImageView);
                        break;
                    case 1:
                        refundPic1String = fileUploadBean.getFileName();
                        BaseImageLoader.get().display(fileUploadBean.getPic(), oneImageView);
                        break;
                    case 2:
                        refundPic2String = fileUploadBean.getFileName();
                        BaseImageLoader.get().display(fileUploadBean.getPic(), twoImageView);
                        break;
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
