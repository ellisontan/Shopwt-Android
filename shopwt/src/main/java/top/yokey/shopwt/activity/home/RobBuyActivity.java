package top.yokey.shopwt.activity.home;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.GoodsDaZheListAdapter;
import top.yokey.shopwt.adapter.GoodsRobBuyListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsDaZheBean;
import top.yokey.base.bean.GoodsRobBuyBean;
import top.yokey.base.model.GoodsModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.view.PullRefreshView;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RobBuyActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView daZheTextView;
    private AppCompatTextView robBuyTextView;
    private PullRefreshView mainPullRefreshView;

    private int position;

    private int daZhePage;
    private GoodsDaZheListAdapter daZheAdapter;
    private ArrayList<GoodsDaZheBean> daZheArrayList;

    private int robBuyPage;
    private GoodsRobBuyListAdapter robBuyAdapter;
    private ArrayList<GoodsRobBuyBean> robBuyArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_home_rob_buy);
        mainToolbar = findViewById(R.id.mainToolbar);
        daZheTextView = findViewById(R.id.daZheTextView);
        robBuyTextView = findViewById(R.id.robBuyTextView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar);

        position = getIntent().getIntExtra(BaseConstant.DATA_POSITION, 0);

        daZhePage = 1;
        daZheArrayList = new ArrayList<>();
        daZheAdapter = new GoodsDaZheListAdapter(daZheArrayList);

        robBuyPage = 1;
        robBuyArrayList = new ArrayList<>();
        robBuyAdapter = new GoodsRobBuyListAdapter(robBuyArrayList);

        if (position == 0) {
            daZheTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            daZheTextView.setBackgroundResource(R.drawable.border_toolbar_left_press);
            robBuyTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            robBuyTextView.setBackgroundResource(R.drawable.border_toolbar_right);
            mainPullRefreshView.getRecyclerView().setAdapter(daZheAdapter);
            getDaZhe();
        } else {
            mainPullRefreshView.getRecyclerView().setAdapter(robBuyAdapter);
            daZheTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            daZheTextView.setBackgroundResource(R.drawable.border_toolbar_left);
            robBuyTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            robBuyTextView.setBackgroundResource(R.drawable.border_toolbar_right_press);
            mainPullRefreshView.getRecyclerView().setAdapter(robBuyAdapter);
            getRobBuy();
        }

    }

    @Override
    public void initEven() {

        daZheTextView.setOnClickListener(view -> {
            daZheTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            daZheTextView.setBackgroundResource(R.drawable.border_toolbar_left_press);
            robBuyTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            robBuyTextView.setBackgroundResource(R.drawable.border_toolbar_right);
            mainPullRefreshView.getRecyclerView().setAdapter(daZheAdapter);
            position = 0;
            if (daZhePage == 1) {
                getDaZhe();
            } else {
                daZheAdapter.notifyDataSetChanged();
            }
        });

        robBuyTextView.setOnClickListener(view -> {
            daZheTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            daZheTextView.setBackgroundResource(R.drawable.border_toolbar_left);
            robBuyTextView.setTextColor(BaseApplication.get().getColors(R.color.white));
            robBuyTextView.setBackgroundResource(R.drawable.border_toolbar_right_press);
            mainPullRefreshView.getRecyclerView().setAdapter(robBuyAdapter);
            position = 1;
            if (robBuyPage == 1) {
                getRobBuy();
            } else {
                robBuyAdapter.notifyDataSetChanged();
            }
        });

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                if (position == 0) {
                    getDaZhe();
                } else {
                    getRobBuy();
                }
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (position == 0) {
                    daZhePage = 1;
                    getDaZhe();
                } else {
                    robBuyPage = 1;
                    getRobBuy();
                }
            }

            @Override
            public void onLoadMore() {
                if (position == 0) {
                    getDaZhe();
                } else {
                    getRobBuy();
                }
            }
        });

        daZheAdapter.setOnItemClickListener((position, bean) -> BaseApplication.get().startGoods(getActivity(), bean.getGoodsId()));

        robBuyAdapter.setOnItemClickListener((position, bean) -> BaseApplication.get().startGoods(getActivity(), bean.getGoodsId()));

    }

    //自定义方法

    private void getDaZhe() {

        mainPullRefreshView.setLoading();

        GoodsModel.get().daZhe(daZhePage + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (daZhePage == 1) {
                    daZheArrayList.clear();
                }
                if (daZhePage <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_list");
                    daZheArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsDaZheBean.class));
                    daZhePage++;
                }
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

    private void getRobBuy() {

        mainPullRefreshView.setLoading();

        GoodsModel.get().robBuy(robBuyPage + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (robBuyPage == 1) {
                    robBuyArrayList.clear();
                }
                if (robBuyPage <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_list");
                    robBuyArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsRobBuyBean.class));
                    robBuyPage++;
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
