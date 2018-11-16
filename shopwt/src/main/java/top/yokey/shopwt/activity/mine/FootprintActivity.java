package top.yokey.shopwt.activity.mine;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.GoodsBrowseListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsBrowseBean;
import top.yokey.base.model.MemberGoodsBrowseModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class FootprintActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView toolbarImageView;
    private PullRefreshView mainPullRefreshView;

    private int pageInt;
    private GoodsBrowseListAdapter mainAdapter;
    private ArrayList<GoodsBrowseBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "我的足迹");
        toolbarImageView.setImageResource(R.drawable.ic_action_delete);

        pageInt = 1;
        mainArrayList = new ArrayList<>();
        mainAdapter = new GoodsBrowseListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        getGoodsBrowse();

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> clearAll());

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                pageInt = 1;
                getGoodsBrowse();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageInt = 1;
                getGoodsBrowse();
            }

            @Override
            public void onLoadMore() {
                getGoodsBrowse();
            }
        });

        mainAdapter.setOnItemClickListener((position, goodsBrowseBean) -> BaseApplication.get().startGoods(getActivity(), goodsBrowseBean.getGoodsId()));

    }

    //自定义方法

    private void clearAll() {

        toolbarImageView.setEnabled(false);

        MemberGoodsBrowseModel.get().browseClearAll(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                toolbarImageView.setEnabled(true);
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    pageInt = 1;
                    getGoodsBrowse();
                } else {
                    BaseToast.get().showFailure();
                }
            }

            @Override
            public void onFailure(String reason) {
                toolbarImageView.setEnabled(true);
                BaseToast.get().show(reason);
            }
        });

    }

    private void getGoodsBrowse() {

        mainPullRefreshView.setLoading();

        MemberGoodsBrowseModel.get().browseList(pageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt == 1) {
                    mainArrayList.clear();
                }
                if (pageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goodsbrowse_list");
                    mainArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsBrowseBean.class));
                    pageInt++;
                }
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

}
