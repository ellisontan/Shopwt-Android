package top.yokey.shopwt.activity.mine;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseToast;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.InviteIndexBean;
import top.yokey.base.model.MemberInviteModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class InviteActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView qrCodeImageView;
    private AppCompatEditText linkEditText;
    private AppCompatTextView copyTextView;

    private InviteIndexBean inviteIndexBean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_invite);
        mainToolbar = findViewById(R.id.mainToolbar);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        linkEditText = findViewById(R.id.linkEditText);
        copyTextView = findViewById(R.id.copyTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "邀请返利");

        getData();

    }

    @Override
    public void initEven() {

        copyTextView.setOnClickListener(view -> {
            BaseApplication.get().setText2Clipboard(linkEditText.getText().toString());
            BaseToast.get().show("链接已复制到剪切板~");
        });

    }

    //自定义方法

    private void getData() {

        MemberInviteModel.get().index(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "member_info");
                inviteIndexBean = JsonUtil.json2Bean(data, InviteIndexBean.class);
                BaseImageLoader.get().display(inviteIndexBean.getMyurlSrc(), qrCodeImageView);
                linkEditText.setText(inviteIndexBean.getMyurl());
                linkEditText.setSelection(linkEditText.getText().length());
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
