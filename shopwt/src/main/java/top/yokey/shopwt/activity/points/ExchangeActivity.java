package top.yokey.shopwt.activity.points;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import top.yokey.base.base.BaseDialog;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.OrderPointsBean;
import top.yokey.base.model.MemberPointOrderModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.OrderPointsListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.view.PullRefreshView;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ExchangeActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private PullRefreshView mainPullRefreshView;

    private int page;
    private OrderPointsListAdapter mainAdapter;
    private ArrayList<OrderPointsBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_points_exchange);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        page = 1;

        mainArrayList = new ArrayList<>();
        mainAdapter = new OrderPointsListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        setToolbar(mainToolbar, "兑换记录");
        getData();

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });

        mainAdapter.setOnItemClickListener(new OrderPointsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, OrderPointsBean bean) {
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra(BaseConstant.DATA_ID, bean.getPointOrderid());
                BaseApplication.get().startCheckLogin(getActivity(), intent);
            }

            @Override
            public void onOption(int position, OrderPointsBean bean) {
                switch (bean.getPointOrderstate()) {
                    case "20":
                        cancelOrder(bean.getPointOrderid());
                        break;
                    case "30":
                        receiveOrder(bean.getPointOrderid());
                        break;
                }
            }

            @Override
            public void onOpera(int position, OrderPointsBean bean) {
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra(BaseConstant.DATA_ID, bean.getPointOrderid());
                BaseApplication.get().startCheckLogin(getActivity(), intent);
            }
        });

    }

    //获取数据

    private void getData() {

        mainPullRefreshView.setLoading();

        MemberPointOrderModel.get().orderList(page + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (page == 1) {
                    mainArrayList.clear();
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "order_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, OrderPointsBean.class));
                mainPullRefreshView.setComplete();
                page++;
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

    private void cancelOrder(String id) {

        BaseDialog.get().progress(getActivity());

        MemberPointOrderModel.get().cancelOrder(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                page = 1;
                BaseDialog.get().cancel();
                BaseToast.get().show("取消成功");
                getData();
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
                BaseToast.get().show(reason);
            }
        });

    }

    private void receiveOrder(String id) {

        BaseDialog.get().progress(getActivity());

        MemberPointOrderModel.get().receiveOrder(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                page = 1;
                BaseDialog.get().cancel();
                BaseToast.get().show("确认收货成功");
                getData();
            }

            @Override
            public void onFailure(String reason) {
                BaseDialog.get().cancel();
                BaseToast.get().show(reason);
            }
        });

    }

}
