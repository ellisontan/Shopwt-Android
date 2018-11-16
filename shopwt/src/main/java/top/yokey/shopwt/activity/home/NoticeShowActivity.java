package top.yokey.shopwt.activity.home;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.ArticleBean;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class NoticeShowActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView contentTextView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_home_notice_show);
        mainToolbar = findViewById(R.id.mainToolbar);
        contentTextView = findViewById(R.id.contentTextView);

    }

    @Override
    public void initData() {

        ArticleBean articleBean = (ArticleBean) getIntent().getSerializableExtra(BaseConstant.DATA_BEAN);

        if (articleBean == null) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        assert articleBean != null;
        contentTextView.setText(articleBean.getArticleContent());
        setToolbar(mainToolbar, articleBean.getArticleTitle());

    }

    @Override
    public void initEven() {

    }

}
