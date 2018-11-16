package top.yokey.shopwt.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;

/**
 * 微信支付处理
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.get().setIwxapi(WXAPIFactory.createWXAPI(this, BaseConstant.WX_APP_ID));
        BaseApplication.get().getIwxapi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BaseApplication.get().getIwxapi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    BaseApplication.get().setSuccess(true);
                    break;
                case -1:
                    BaseApplication.get().setSuccess(false);
                    break;
                case -2:
                    BaseApplication.get().setSuccess(false);
                    break;
            }
            finish();
        }
    }

}
