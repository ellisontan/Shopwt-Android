package top.yokey.shopwt.activity.mine;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.MemberAccountModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.base.util.TextUtil;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class BindMobileActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText mobileEditText;
    private AppCompatTextView getTextView;
    private AppCompatEditText codeEditText;
    private AppCompatTextView bindTextView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_bind_mobile);
        mainToolbar = findViewById(R.id.mainToolbar);
        mobileEditText = findViewById(R.id.mobileEditText);
        getTextView = findViewById(R.id.getTextView);
        codeEditText = findViewById(R.id.codeEditText);
        bindTextView = findViewById(R.id.bindTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "手机验证");

    }

    @Override
    public void initEven() {

        getTextView.setOnClickListener(view -> bindMobileSetup1());

        bindTextView.setOnClickListener(view -> bindMobileSetup2());

    }

    //自定义方法

    private void bindMobileSetup1() {

        BaseApplication.get().hideKeyboard(getActivity());

        String mobile = mobileEditText.getText().toString();

        if (!TextUtil.isMobile(mobile)) {
            BaseToast.get().show("手机号码格式不正确！");
            return;
        }

        getTextView.setEnabled(false);
        getTextView.setText("获取中...");

        MemberAccountModel.get().bindMobileSetup1(mobile, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                final String smsTime = JsonUtil.getDatasString(baseBean.getDatas(), "sms_time");
                final int time = Integer.parseInt(smsTime);
                //倒计时
                new BaseCountTime(time * 1000, BaseConstant.TIME_TICK) {

                    int totalTime = time;

                    @Override
                    public void onTick(long millis) {
                        super.onTick(millis);
                        String temp = "再次获取（" + totalTime-- + " S ）";
                        getTextView.setText(temp);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getTextView.setEnabled(true);
                        getTextView.setText("获取验证码");
                    }

                }.start();

            }

            @Override
            public void onFailure(String reason) {
                getTextView.setEnabled(true);
                getTextView.setText("获取验证码");
                BaseToast.get().show(reason);
            }
        });

    }

    private void bindMobileSetup2() {

        BaseApplication.get().hideKeyboard(getActivity());

        String code = codeEditText.getText().toString();

        if (TextUtils.isEmpty(code)) {
            BaseToast.get().show("请输入验证码！");
            return;
        }

        bindTextView.setEnabled(false);
        bindTextView.setText("处理中...");

        MemberAccountModel.get().bindMobileSetup2(code, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().showSuccess();
                BaseApplication.get().getMemberBean().setMobielState(true);
                BaseApplication.get().getMemberBean().setUserMobile(mobileEditText.getText().toString());
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                bindTextView.setEnabled(true);
                bindTextView.setText("绑定手机");
                BaseToast.get().show(reason);
            }
        });

    }

}
