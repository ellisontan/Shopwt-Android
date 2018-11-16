package top.yokey.shopwt.activity.home;

import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.RedPacketDetailedBean;
import top.yokey.base.model.MemberRedPackerModel;
import top.yokey.base.model.RedPacketModel;
import top.yokey.base.util.JsonUtil;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RedPacketActivity extends BaseActivity {

    private LinearLayoutCompat toolbarLinearLayout;
    private RelativeLayout redPacketRelativeLayout;

    private String id;
    private RedPacketDetailedBean redPacketDetailedBean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_home_red_packet);
        toolbarLinearLayout = findViewById(R.id.toolbarLinearLayout);
        redPacketRelativeLayout = findViewById(R.id.redPacketRelativeLayout);

    }

    @Override
    public void initData() {

        id = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(id)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        BaseApplication.get().setFullScreen(getActivity());
        redPacketDetailedBean = null;
        getData();

    }

    @Override
    public void initEven() {

        toolbarLinearLayout.setOnClickListener(view -> BaseApplication.get().finish(getActivity()));

        redPacketRelativeLayout.setOnClickListener(view -> getRedPack());

    }

    //自定义方法

    private void getData() {

        RedPacketModel.get().index(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "packet_detail");
                redPacketDetailedBean = JsonUtil.json2Bean(data, RedPacketDetailedBean.class);
                if (!redPacketDetailedBean.getState().equals("1")) {
                    BaseToast.get().show("此次红包活动已结束~");
                    BaseApplication.get().finish(getActivity());
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void getRedPack() {

        BaseToast.get().show("领取中...");

        MemberRedPackerModel.get().getPack(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseDialog.get().query(getActivity(), "提示", baseBean.getDatas(), null, null);
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().query(getActivity(), "提示", reason, null, null);
            }
        });

    }

}
