package top.yokey.shopwt.activity.store;

import android.support.v7.widget.GridLayoutManager;

import com.squareup.otto.Subscribe;

import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.GoodsListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsBean;
import top.yokey.base.bean.StoreInfoBean;
import top.yokey.base.event.StoreBeanEvent;
import top.yokey.base.model.MemberCartModel;
import top.yokey.base.model.StoreModel;
import top.yokey.base.util.JsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_store_new)
public class NewFragment extends BaseFragment {

    @ViewInject(R.id.mainPullRefreshView)
    private PullRefreshView mainPullRefreshView;

    private int pageInt;
    private StoreInfoBean storeInfoBean;
    private GoodsListAdapter mainAdapter;
    private ArrayList<GoodsBean> mainArrayList;

    @Override
    public void initData() {

        storeInfoBean = new StoreInfoBean();

        mainArrayList = new ArrayList<>();
        mainAdapter = new GoodsListAdapter(mainArrayList, true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        //noinspection deprecation
        gridLayoutManager.setAutoMeasureEnabled(true);

        pageInt = 1;
        mainPullRefreshView.getRecyclerView().setHasFixedSize(true);
        mainPullRefreshView.getRecyclerView().setNestedScrollingEnabled(false);
        mainPullRefreshView.getRecyclerView().setLayoutManager(gridLayoutManager);
        mainPullRefreshView.getRecyclerView().setPadding(BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2));
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.clearItemDecoration();

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                getGoods();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageInt = 1;
                getGoods();
            }

            @Override
            public void onLoadMore() {
                getGoods();
            }
        });

        mainAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onCart(int position, GoodsBean goodsBean) {
                addCart(goodsBean.getGoodsId());
            }

            @Override
            public void onClick(int position, GoodsBean goodsBean) {
                BaseApplication.get().startGoods(getActivity(), goodsBean.getGoodsId());
            }
        });

    }

    //自定义方法

    private void getGoods() {

        mainPullRefreshView.setLoading();

        StoreModel.get().storeNewGoods(storeInfoBean.getStoreId(), pageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt == 1) {
                    mainArrayList.clear();
                }
                if (pageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_list");
                    mainArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsBean.class));
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

    private void addCart(String goodsId) {

        MemberCartModel.get().cartAdd(goodsId, "1", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().showSuccess();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onStoreBeanEvent(StoreBeanEvent event) {

        String data = JsonUtil.getDatasString(event.getBaseBean().getDatas(), "store_info");
        storeInfoBean = JsonUtil.json2Bean(data, StoreInfoBean.class);
        getGoods();

    }

}
