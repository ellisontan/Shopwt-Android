package top.yokey.shopwt.activity.mine;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.PointsLogListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.PointsLogBean;
import top.yokey.base.model.MemberPointsModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class PointsActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView pointsValueTextView;
    private PullRefreshView mainPullRefreshView;

    private int pageInt;
    private PointsLogListAdapter mainAdapter;
    private ArrayList<PointsLogBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_points);
        mainToolbar = findViewById(R.id.mainToolbar);
        pointsValueTextView = findViewById(R.id.pointsValueTextView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "积分明细");

        pageInt = 1;
        mainArrayList = new ArrayList<>();
        mainAdapter = new PointsLogListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        pointsValueTextView.setText(BaseApplication.get().getMemberAssetBean().getPoint());
        pointsValueTextView.append("分");

        getPoints();

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                pageInt = 1;
                getPoints();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageInt = 1;
                getPoints();
            }

            @Override
            public void onLoadMore() {
                getPoints();
            }
        });

        mainAdapter.setOnItemClickListener((position, pointsLogBean) -> {

        });

    }

    //自定义方法

    private void getPoints() {

        mainPullRefreshView.setLoading();

        MemberPointsModel.get().pointsLog(pageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt == 1) {
                    mainArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    pageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "log_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, PointsLogBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

}
