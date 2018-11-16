package top.yokey.shopwt.activity.mine;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.PreDepositCashLogBean;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class PreDepositCashActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView snTextView;
    private AppCompatTextView moneyTextView;
    private AppCompatTextView bankTextView;
    private AppCompatTextView noTextView;
    private AppCompatTextView nameTextView;
    private AppCompatTextView timeTextView;
    private AppCompatTextView stateTextView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_pre_deposit_cash);
        mainToolbar = findViewById(R.id.mainToolbar);
        snTextView = findViewById(R.id.snTextView);
        moneyTextView = findViewById(R.id.moneyTextView);
        bankTextView = findViewById(R.id.bankTextView);
        noTextView = findViewById(R.id.noTextView);
        nameTextView = findViewById(R.id.nameTextView);
        timeTextView = findViewById(R.id.timeTextView);
        stateTextView = findViewById(R.id.stateTextView);

    }

    @Override
    public void initData() {

        PreDepositCashLogBean pdCashLogBean = (PreDepositCashLogBean) getIntent().getSerializableExtra(BaseConstant.DATA_BEAN);

        if (pdCashLogBean == null) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "提现详情");

        assert pdCashLogBean != null;
        snTextView.setText(pdCashLogBean.getPdcSn());
        moneyTextView.setText(pdCashLogBean.getPdcAmount());
        bankTextView.setText(pdCashLogBean.getPdcBankName());
        noTextView.setText(pdCashLogBean.getPdcBankNo());
        nameTextView.setText(pdCashLogBean.getPdcBankUser());
        timeTextView.setText(pdCashLogBean.getPdcAddTimeText());
        stateTextView.setText(pdCashLogBean.getPdcPaymentStateText());

    }

    @Override
    public void initEven() {

    }

}
