package top.yokey.shopwt.activity.seller;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import top.yokey.base.base.BaseToast;
import top.yokey.base.base.SellerHttpClient;
import top.yokey.base.util.JsonUtil;
import top.yokey.base.util.TextUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseShared;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.SellerLoginModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class LoginActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText usernameEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatTextView loginTextView;

    private long exitTimeLong;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_login);
        mainToolbar = findViewById(R.id.mainToolbar);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginTextView = findViewById(R.id.loginTextView);

    }

    @Override
    public void initData() {

        exitTimeLong = 0L;
        setToolbar(mainToolbar, "商家登录");

    }

    @Override
    public void initEven() {

        loginTextView.setOnClickListener(view -> login());

    }

    @Override
    public void onReturn() {

        if (System.currentTimeMillis() - exitTimeLong > BaseConstant.TIME_EXIT) {
            BaseToast.get().showReturnOneMoreTime();
            exitTimeLong = System.currentTimeMillis();
        } else {
            super.onReturn();
        }

    }

    //自定义方法

    private void login() {

        BaseApplication.get().hideKeyboard(getActivity());

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtil.isEmail(password)) {
            BaseToast.get().show("请输入所有的信息！");
            return;
        }

        loginTextView.setEnabled(false);
        loginTextView.setText("登录中...");

        SellerLoginModel.get().index(username, password, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                loginTextView.setEnabled(true);
                loginTextView.setText("登 录");
                BaseToast.get().show("登录成功！");
                SellerHttpClient.get().updateKey(JsonUtil.getDatasString(baseBean.getDatas(), "key"));
                BaseShared.get().putString(BaseConstant.SHARED_SELLER_KEY, JsonUtil.getDatasString(baseBean.getDatas(), "key"));
                BaseApplication.get().start(getActivity(), SellerActivity.class);
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                loginTextView.setEnabled(true);
                loginTextView.setText("登 录");
                BaseToast.get().show(reason);
            }
        });

    }

}
