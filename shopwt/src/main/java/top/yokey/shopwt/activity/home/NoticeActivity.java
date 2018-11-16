package top.yokey.shopwt.activity.home;

import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.ArticleListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.ArticleBean;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.ArticleModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class NoticeActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private PullRefreshView mainPullRefreshView;

    private ArticleListAdapter mainAdapter;
    private ArrayList<ArticleBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "商城公告");
        mainArrayList = new ArrayList<>();
        mainAdapter = new ArticleListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.setCanLoadMore(false);

        getNotice();

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                getNotice();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotice();
            }

            @Override
            public void onLoadMore() {

            }
        });

        mainAdapter.setOnItemClickListener((position, articleBean) -> BaseApplication.get().startNoticeShow(getActivity(), articleBean));

    }

    //自定义方法

    private void getNotice() {

        mainPullRefreshView.setLoading();

        ArticleModel.get().articleList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "article_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, ArticleBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

}
