package top.yokey.shopwt.activity.seller;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.BaseViewPagerAdapter;
import top.yokey.shopwt.adapter.GoodsSellerListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.bean.GoodsSellerBean;
import top.yokey.base.model.SellerGoodsModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText searchEditText;
    private AppCompatImageView toolbarImageView;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private GoodsSellerListAdapter[] mainAdapter;
    private PullRefreshView[] mainPullRefreshView;
    private ArrayList<GoodsSellerBean>[] mainArrayList;

    private int[] pageInt;
    private int positionInt;
    private String keywordString;
    private String[] stateString;
    private boolean refreshBoolean;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_goods);
        mainToolbar = findViewById(R.id.mainToolbar);
        searchEditText = findViewById(R.id.searchEditText);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainTabLayout = findViewById(R.id.mainTabLayout);
        mainViewPager = findViewById(R.id.mainViewPager);

    }

    @Override
    public void initData() {

        positionInt = getIntent().getIntExtra(BaseConstant.DATA_POSITION, 0);

        setToolbar(mainToolbar, "");
        toolbarImageView.setImageResource(R.drawable.ic_action_search);

        keywordString = "";
        stateString = new String[3];
        stateString[0] = "";
        stateString[1] = "offline";
        stateString[2] = "lockup";

        List<String> titleList = new ArrayList<>();
        titleList.add("出售中");
        titleList.add("仓库中");
        titleList.add("违规商品");

        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));
        viewList.add(getLayoutInflater().inflate(R.layout.include_recycler_view, null));

        refreshBoolean = false;
        pageInt = new int[viewList.size()];
        //noinspection unchecked
        mainArrayList = new ArrayList[viewList.size()];
        mainAdapter = new GoodsSellerListAdapter[viewList.size()];
        mainPullRefreshView = new PullRefreshView[viewList.size()];

        for (int i = 0; i < viewList.size(); i++) {
            pageInt[i] = 1;
            mainArrayList[i] = new ArrayList<>();
            mainAdapter[i] = new GoodsSellerListAdapter(mainArrayList[i]);
            mainPullRefreshView[i] = viewList.get(i).findViewById(R.id.mainPullRefreshView);
            mainTabLayout.addTab(mainTabLayout.newTab().setText(titleList.get(i)));
            mainPullRefreshView[i].getRecyclerView().setAdapter(mainAdapter[i]);
        }

        BaseApplication.get().setTabLayout(mainTabLayout, new BaseViewPagerAdapter(viewList, titleList), mainViewPager);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mainViewPager.setCurrentItem(positionInt);
        getGoods();

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> {
            BaseApplication.get().hideKeyboard(getActivity());
            keywordString = searchEditText.getText().toString();
            pageInt[positionInt] = 1;
            getGoods();
        });

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionInt = position;
                if (mainArrayList[positionInt].size() == 0) {
                    getGoods();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (PullRefreshView pullRefreshView : mainPullRefreshView) {
            pullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pageInt[positionInt] = 1;
                    getGoods();
                }

                @Override
                public void onLoadMore() {
                    getGoods();
                }
            });
        }

        for (GoodsSellerListAdapter adapter : mainAdapter) {
            adapter.setOnItemClickListener(new GoodsSellerListAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position, GoodsSellerBean bean) {
                    Intent intent = new Intent(getActivity(), GoodsEditActivity.class);
                    intent.putExtra(BaseConstant.DATA_ID, bean.getGoodsCommonid());
                    BaseApplication.get().startCheckSellerLogin(getActivity(), intent);
                    refreshBoolean = true;
                }

                @Override
                public void onEditor(int position, GoodsSellerBean bean) {
                    Intent intent = new Intent(getActivity(), GoodsEditActivity.class);
                    intent.putExtra(BaseConstant.DATA_ID, bean.getGoodsCommonid());
                    BaseApplication.get().startCheckSellerLogin(getActivity(), intent);
                    refreshBoolean = true;
                }

                @Override
                public void onOption(int position, GoodsSellerBean bean) {
                    switch (bean.getGoodsState()) {
                        case "1":
                            unshowGoods(bean.getGoodsCommonid());
                            break;
                        case "0":
                            showGoods(bean.getGoodsCommonid());
                            break;
                        case "10":
                            BaseToast.get().show("商品禁售中，请重新编辑商品！");
                            break;
                    }
                }

                @Override
                public void onDelete(int position, GoodsSellerBean bean) {
                    deleteGoods(bean.getGoodsCommonid());
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshBoolean) {
            refreshBoolean = false;
            pageInt[positionInt] = 1;
            getGoods();
        }
    }

    //自定义方法

    private void getGoods() {

        mainPullRefreshView[positionInt].setLoading();

        SellerGoodsModel.get().goodsList(stateString[positionInt], keywordString, pageInt[positionInt] + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt[positionInt] == 1) {
                    mainArrayList[positionInt].clear();
                }
                if (pageInt[positionInt] <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_list");
                    mainArrayList[positionInt].addAll(JsonUtil.json2ArrayList(data, GoodsSellerBean.class));
                    pageInt[positionInt]++;
                }
                mainPullRefreshView[positionInt].setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView[positionInt].setFailure();
            }
        });

    }

    private void showGoods(String id) {

        SellerGoodsModel.get().goodsShow(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("上架成功！");
                pageInt[positionInt] = 1;
                getGoods();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void deleteGoods(String id) {

        SellerGoodsModel.get().goodsDrop(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("删除成功！");
                pageInt[positionInt] = 1;
                getGoods();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void unshowGoods(String id) {

        SellerGoodsModel.get().goodsUnshow(id, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("下架成功！");
                pageInt[positionInt] = 1;
                getGoods();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
