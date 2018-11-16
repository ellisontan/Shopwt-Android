package top.yokey.shopwt.activity.mine;

import android.app.ProgressDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.IndexModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseFileClient;
import top.yokey.base.base.BaseShared;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class SettingActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private RelativeLayout pushRelativeLayout;
    private SwitchCompat pushSwitch;
    private RelativeLayout imageRelativeLayout;
    private SwitchCompat imageSwitch;
    private RelativeLayout updateRelativeLayout;
    private AppCompatTextView updateTextView;
    private RelativeLayout aboutRelativeLayout;

    private String url;
    private ProgressDialog progressDialog;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_setting);
        mainToolbar = findViewById(R.id.mainToolbar);
        pushRelativeLayout = findViewById(R.id.pushRelativeLayout);
        pushSwitch = findViewById(R.id.pushSwitch);
        imageRelativeLayout = findViewById(R.id.imageRelativeLayout);
        imageSwitch = findViewById(R.id.imageSwitch);
        updateRelativeLayout = findViewById(R.id.updateRelativeLayout);
        updateTextView = findViewById(R.id.updateTextView);
        aboutRelativeLayout = findViewById(R.id.aboutRelativeLayout);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "系统设置");

        url = "";
        pushSwitch.setChecked(BaseApplication.get().isPush());
        imageSwitch.setChecked(BaseApplication.get().isImage());
        checkVersion();

    }

    @Override
    public void initEven() {

        pushRelativeLayout.setOnClickListener(view -> pushSwitch.setChecked(!pushSwitch.isChecked()));

        imageRelativeLayout.setOnClickListener(view -> imageSwitch.setChecked(!imageSwitch.isChecked()));

        pushSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            BaseApplication.get().setPush(isChecked);
            BaseShared.get().putBoolean(BaseConstant.SHARED_SETTING_PUSH, isChecked);
        });

        imageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            BaseApplication.get().setImage(isChecked);
            BaseShared.get().putBoolean(BaseConstant.SHARED_SETTING_IMAGE, isChecked);
        });

        updateRelativeLayout.setOnClickListener(view -> {
            if (updateTextView.getText().equals("已是最新版本")) {
                BaseToast.get().show("已是最新版本");
                return;
            }
            BaseToast.get().show("正在下载更新文件...");
            downloadApk(url);
        });

        aboutRelativeLayout.setOnClickListener(view -> BaseDialog.get().query(getActivity(), "关于我们", "本程序作者：MapStory，联系QQ：1002285057，交流群：492184679，禁止未授权商用，源码免费下载，请勿上当受骗！", null, null));

    }

    //自定义方法

    private void checkVersion() {

        IndexModel.get().apkVersion(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                url = JsonUtil.getDatasString(baseBean.getDatas(), "url");
                String version = JsonUtil.getDatasString(baseBean.getDatas(), "version");
                if (!version.equals(BaseApplication.get().getVersion())) {
                    updateTextView.setText("有新版本");
                    updateTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                } else {
                    updateTextView.setText("已是最新版本");
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void downloadApk(String url) {

        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(BaseFileClient.get().getDownPath());
        requestParams.setAutoRename(true);
        requestParams.setAutoRename(true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        x.http().get(requestParams, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File file) {
                BaseApplication.get().startInstallApk(getActivity(), file);
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                progressDialog.dismiss();
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                BaseToast.get().show("准备开始下载...");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("正在下载中...");
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
                progressDialog.setProgressNumberFormat(" ");
            }
        });

    }

}
