package top.yokey.shopwt.activity.points;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.PointcartModel;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.choose.AddressActivity;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.base.BaseImageLoader;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class BuyActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private RelativeLayout addressRelativeLayout;
    private AppCompatTextView addressNameTextView;
    private AppCompatTextView addressMobileTextView;
    private AppCompatTextView addressAreaTextView;
    private AppCompatImageView mainImageView;
    private AppCompatTextView nameTextView;
    private AppCompatTextView pointsTextView;
    private AppCompatTextView numberTextView;
    private AppCompatEditText messageEditText;
    private AppCompatTextView moneyTextView;
    private AppCompatTextView balanceTextView;

    private String message;
    private String addressId;

    @Override
    public void initView() {

        setContentView(R.layout.activity_points_buy);
        mainToolbar = findViewById(R.id.mainToolbar);
        addressRelativeLayout = findViewById(R.id.addressRelativeLayout);
        addressNameTextView = findViewById(R.id.addressNameTextView);
        addressMobileTextView = findViewById(R.id.addressMobileTextView);
        addressAreaTextView = findViewById(R.id.addressAreaTextView);
        mainImageView = findViewById(R.id.mainImageView);
        nameTextView = findViewById(R.id.nameTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        numberTextView = findViewById(R.id.numberTextView);
        messageEditText = findViewById(R.id.messageEditText);
        moneyTextView = findViewById(R.id.moneyTextView);
        balanceTextView = findViewById(R.id.balanceTextView);

    }

    @Override
    public void initData() {

        message = "";
        addressId = "";

        setToolbar(mainToolbar, "确认兑换");
        getData();

    }

    @Override
    public void initEven() {

        addressRelativeLayout.setOnClickListener(view -> BaseApplication.get().start(getActivity(), AddressActivity.class, BaseConstant.CODE_ADDRESS));

        balanceTextView.setOnClickListener(view -> balance());

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK) {
            switch (req) {
                case BaseConstant.CODE_ADDRESS:
                    addressId = intent.getStringExtra(BaseConstant.DATA_ID);
                    getData();
                    break;
                default:
                    break;
            }
        } else {
            switch (req) {
                case BaseConstant.CODE_ADDRESS:
                    if (TextUtils.isEmpty(addressId)) {
                        tipsAddress();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    //获取数据

    private void getData() {

        BaseDialog.get().progress(getActivity());

        PointcartModel.get().step1(addressId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseDialog.get().cancel();
                try {
                    JSONObject jsonObject = new JSONObject(baseBean.getDatas());
                    JSONObject addressJsonObject = new JSONObject(jsonObject.getString("address_info"));
                    jsonObject = new JSONObject(jsonObject.getString("pointprod_arr"));
                    String temp = "合计积分：" + "<font color='#FF0000'>" + jsonObject.getString("pgoods_pointall") + "</font>";
                    moneyTextView.setText(Html.fromHtml(temp));
                    if (addressJsonObject.toString().equals("[]") || addressJsonObject.toString().equals("null")) {
                        tipsAddress();
                        return;
                    }
                    addressNameTextView.setText(addressJsonObject.getString("true_name"));
                    addressMobileTextView.setText(addressJsonObject.getString("mob_phone"));
                    addressAreaTextView.setText(addressJsonObject.getString("area_info"));
                    addressAreaTextView.append(" " + addressJsonObject.getString("address"));
                    addressId = addressJsonObject.getString("address_id");
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("pointprod_list"));
                    jsonObject = new JSONObject(jsonArray.getString(0));
                    BaseImageLoader.get().display(jsonObject.getString("pgoods_image"), mainImageView);
                    nameTextView.setText(jsonObject.getString("pgoods_name"));
                    pointsTextView.setText("积分：");
                    pointsTextView.append(jsonObject.getString("pgoods_points"));
                    numberTextView.setText(jsonObject.getString("quantity"));
                    numberTextView.append("件");
                } catch (JSONException e) {
                    getDataFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
            }
        });

    }

    private void getDataFailure(String reason) {

        BaseDialog.get().queryLoadingFailure(getActivity(), reason, (dialog, which) -> getData(), (dialog, which) -> BaseApplication.get().finish(getActivity()));

    }

    private void tipsAddress() {

        new AlertDialog.Builder(getActivity()).setTitle("请添加地址").setMessage("尚未添加收货地址")
                .setNegativeButton("取消", (dialogInterface, i) -> BaseApplication.get().finish(getActivity()))
                .setPositiveButton("确认", (dialogInterface, i) -> BaseApplication.get().start(getActivity(), AddressActivity.class, BaseConstant.CODE_ADDRESS))
                .show();

    }

    private void balance() {

        BaseDialog.get().progress(getActivity());

        message = messageEditText.getText().toString();

        PointcartModel.get().step2(addressId, message, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseDialog.get().cancel();
                BaseToast.get().show("兑换成功，等待发货！");
                BaseApplication.get().startCheckLogin(getActivity(), ExchangeActivity.class);
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
                BaseToast.get().show(reason);
            }
        });

    }

}
