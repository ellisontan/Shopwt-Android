package top.yokey.shopwt.activity.mine;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.GoodsFavoritesListAdapter;
import top.yokey.shopwt.adapter.StoreFavoritesListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsFavoritesBean;
import top.yokey.base.bean.StoreFavoritesBean;
import top.yokey.base.model.MemberFavoritesModel;
import top.yokey.base.model.MemberFavoritesStoreModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class FavoritesActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private int positionInt;
    private int goodsPageInt;
    private PullRefreshView goodsPullRefreshView;
    private GoodsFavoritesListAdapter goodsAdapter;
    private ArrayList<GoodsFavoritesBean> goodsArrayList;
    private int storePageInt;
    private PullRefreshView storePullRefreshView;
    private StoreFavoritesListAdapter storeAdapter;
    private ArrayList<StoreFavoritesBean> storeArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_favorites);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);

    }

    @Override
    public void initData() {

        positionInt = getIntent().getIntExtra(BaseConstant.DATA_POSITION, 0);
        setToolbar(mainToolbar, "我的收藏");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        List<String> titleList = new ArrayList<>();
        titleList.add("商品收藏");
        titleList.add("店铺收藏");
        for (int i = 0; i < viewList.size(); i++) {
            mainTabLayout.addTab(mainTabLayout.newTab().setText(titleList.get(i)));
        }
        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);

        goodsPageInt = 1;
        goodsArrayList = new ArrayList<>();
        goodsPullRefreshView = viewList.get(0).findViewById(R.id.mainPullRefreshView);
        goodsPullRefreshView.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 2));
        goodsPullRefreshView.getRecyclerView().setPadding(BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2));
        goodsAdapter = new GoodsFavoritesListAdapter(goodsArrayList);
        goodsPullRefreshView.getRecyclerView().setAdapter(goodsAdapter);
        goodsPullRefreshView.clearItemDecoration();

        storePageInt = 1;
        storeArrayList = new ArrayList<>();
        storePullRefreshView = viewList.get(1).findViewById(R.id.mainPullRefreshView);
        storeAdapter = new StoreFavoritesListAdapter(storeArrayList);
        storePullRefreshView.getRecyclerView().setAdapter(storeAdapter);
        mainViewPager.setCurrentItem(positionInt);

        getGoods();
        getStore();

    }

    @Override
    public void initEven() {

        goodsPullRefreshView.setOnClickListener(view -> {
            if (goodsPullRefreshView.isFailure()) {
                goodsPageInt = 1;
                getGoods();
            }
        });

        goodsPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goodsPageInt = 1;
                getGoods();
            }

            @Override
            public void onLoadMore() {
                getGoods();
            }
        });

        goodsAdapter.setOnItemClickListener(new GoodsFavoritesListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, GoodsFavoritesBean goodsFavoritesBean) {
                BaseApplication.get().startGoods(getActivity(), goodsFavoritesBean.getGoodsId());
            }

            @Override
            public void onDelete(int position, GoodsFavoritesBean goodsFavoritesBean) {
                positionInt = position;
                delGoods(goodsFavoritesBean.getFavId());
            }
        });

        storePullRefreshView.setOnClickListener(view -> {
            if (storePullRefreshView.isFailure()) {
                storePageInt = 1;
                getStore();
            }
        });

        storePullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storePageInt = 1;
                getStore();
            }

            @Override
            public void onLoadMore() {
                getStore();
            }
        });

        storeAdapter.setOnItemClickListener(new StoreFavoritesListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, StoreFavoritesBean storeFavoritesBean) {
                BaseApplication.get().startStore(getActivity(), storeFavoritesBean.getStoreId());
            }

            @Override
            public void onDelete(int position, StoreFavoritesBean storeFavoritesBean) {
                positionInt = position;
                delStore(storeFavoritesBean.getStoreId());
            }
        });

    }

    //自定义方法

    private void getGoods() {

        goodsPullRefreshView.setLoading();

        MemberFavoritesModel.get().favoritesList(goodsPageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (goodsPageInt == 1) {
                    goodsArrayList.clear();
                }
                if (goodsPageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "favorites_list");
                    goodsArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsFavoritesBean.class));
                    goodsPageInt++;
                }
                goodsPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                goodsPullRefreshView.setFailure();
            }
        });

    }

    private void getStore() {

        storePullRefreshView.setLoading();

        MemberFavoritesStoreModel.get().favoritesList(storePageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (storePageInt == 1) {
                    storeArrayList.clear();
                }
                if (storePageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "favorites_list");
                    storeArrayList.addAll(JsonUtil.json2ArrayList(data, StoreFavoritesBean.class));
                    storePageInt++;
                }
                storePullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                storePullRefreshView.setFailure();
            }
        });

    }

    private void delGoods(String favId) {

        MemberFavoritesModel.get().favoritesDel(favId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    goodsArrayList.remove(positionInt);
                    goodsAdapter.notifyItemRemoved(positionInt);
                    storePullRefreshView.setComplete();
                } else {
                    BaseToast.get().showFailure();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void delStore(String storeId) {

        MemberFavoritesStoreModel.get().favoritesDel(storeId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    storeArrayList.remove(positionInt);
                    storeAdapter.notifyItemRemoved(positionInt);
                    storePullRefreshView.setComplete();
                } else {
                    BaseToast.get().showFailure();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
