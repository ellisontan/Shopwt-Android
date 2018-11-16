package top.yokey.shopwt.activity.mine;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import top.yokey.base.base.MemberHttpClient;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.main.MainActivity;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseShared;
import top.yokey.base.base.BaseToast;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class CenterActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private RelativeLayout passwordRelativeLayout;
    private RelativeLayout mobileRelativeLayout;
    private RelativeLayout payPassRelativeLayout;
    private AppCompatTextView logoutTextView;

    private long logoutTimeLong;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_center);
        mainToolbar = findViewById(R.id.mainToolbar);
        passwordRelativeLayout = findViewById(R.id.passwordRelativeLayout);
        mobileRelativeLayout = findViewById(R.id.mobileRelativeLayout);
        payPassRelativeLayout = findViewById(R.id.payPassRelativeLayout);
        logoutTextView = findViewById(R.id.logoutTextView);

    }

    @Override
    public void initData() {

        logoutTimeLong = 0L;
        setToolbar(mainToolbar, "个人设置");

    }

    @Override
    public void initEven() {

        passwordRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckMobile(getActivity(), PasswordActivity.class));

        mobileRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckMobile(getActivity(), ModifyMobileActivity.class));

        payPassRelativeLayout.setOnClickListener(view -> BaseApplication.get().startCheckMobile(getActivity(), PayPassActivity.class));

        logoutTextView.setOnClickListener(view -> logout());

    }

    //自定义方法

    private void logout() {

        if (System.currentTimeMillis() - logoutTimeLong > BaseConstant.TIME_EXIT) {
            BaseToast.get().show("再按一次注销登录...");
            logoutTimeLong = System.currentTimeMillis();
        } else {
            MemberHttpClient.get().updateKey("");
            BaseToast.get().show("注销成功！");
            BaseShared.get().putString(BaseConstant.SHARED_KEY, "");
            BaseApplication.get().start(getActivity(), MainActivity.class);
            BaseApplication.get().finish(getActivity());
        }

    }

}
