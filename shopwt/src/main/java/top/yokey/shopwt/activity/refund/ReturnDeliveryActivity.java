package top.yokey.shopwt.activity.refund;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.ExpressBean;
import top.yokey.base.model.MemberReturnModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ReturnDeliveryActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatSpinner expressSpinner;
    private AppCompatEditText noEditText;
    private AppCompatTextView submitTextView;

    private String returnIdString;
    private String expressIdString;
    private Vector<String> idVector;
    private Vector<String> nameVector;

    @Override
    public void initView() {

        setContentView(R.layout.activity_return_delivery);
        mainToolbar = findViewById(R.id.mainToolbar);
        expressSpinner = findViewById(R.id.expressSpinner);
        noEditText = findViewById(R.id.noEditText);
        submitTextView = findViewById(R.id.submitTextView);

    }

    @Override
    public void initData() {

        returnIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(returnIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        expressIdString = "";
        idVector = new Vector<>();
        nameVector = new Vector<>();
        setToolbar(mainToolbar, "退货发货");
        getData();

    }

    @Override
    public void initEven() {

        expressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expressIdString = idVector.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitTextView.setOnClickListener(view -> submit());

    }

    //自定义方法

    private void submit() {

        String no = noEditText.getText().toString();
        if (TextUtils.isEmpty(no)) {
            BaseToast.get().show("请输入运单号码");
            return;
        }

        submitTextView.setEnabled(false);
        submitTextView.setText("提交中...");

        MemberReturnModel.get().shipPost(returnIdString, expressIdString, no, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("发货成功！");
                BaseApplication.get().finish(getActivity());
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

        MemberReturnModel.get().shipForm(returnIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                idVector.clear();
                nameVector.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "express_list");
                //noinspection ConstantConditions
                ArrayList<ExpressBean> arrayList = new ArrayList<>(JsonUtil.json2ArrayList(data, ExpressBean.class));
                for (int i = 0; i < arrayList.size(); i++) {
                    idVector.add(arrayList.get(i).getExpressId());
                    nameVector.add(arrayList.get(i).getExpressName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, nameVector);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                expressSpinner.setAdapter(arrayAdapter);
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

}
