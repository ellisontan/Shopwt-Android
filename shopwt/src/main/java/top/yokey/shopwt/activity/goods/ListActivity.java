package top.yokey.shopwt.activity.goods;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.choose.CateActivity;
import top.yokey.shopwt.adapter.GoodsListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsBean;
import top.yokey.base.model.GoodsModel;
import top.yokey.base.model.MemberCartModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ListActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText searchEditText;
    private AppCompatImageView toolbarImageView;
    private AppCompatTextView orderTextView;
    private AppCompatTextView saleTextView;
    private AppCompatTextView screenTextView;
    private AppCompatImageView orientationImageView;
    private LinearLayoutCompat orderLinearLayout;
    private RelativeLayout screenRelativeLayout;
    private AppCompatTextView orderCompTextView;
    private AppCompatTextView orderHighTextView;
    private AppCompatTextView orderLowTextView;
    private AppCompatTextView orderHotTextView;
    private AppCompatEditText priceFromEditText;
    private AppCompatEditText priceToEditText;
    private AppCompatTextView confirmTextView;
    private PullRefreshView mainPullRefreshView;

    private String bIdString;
    private String gcIdString;
    private String keywordString;

    private String keyString;
    private String orderString;
    private boolean isGridModel;
    private String priceToString;
    private String priceFromString;

    private int pageInt;
    private GoodsListAdapter mainAdapter;
    private ArrayList<GoodsBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_goods_list);
        mainToolbar = findViewById(R.id.mainToolbar);
        searchEditText = findViewById(R.id.searchEditText);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        orderTextView = findViewById(R.id.orderTextView);
        saleTextView = findViewById(R.id.saleTextView);
        screenTextView = findViewById(R.id.screenTextView);
        orientationImageView = findViewById(R.id.orientationImageView);
        orderLinearLayout = findViewById(R.id.orderLinearLayout);
        screenRelativeLayout = findViewById(R.id.screenRelativeLayout);
        orderCompTextView = findViewById(R.id.orderCompTextView);
        orderHighTextView = findViewById(R.id.orderHighTextView);
        orderLowTextView = findViewById(R.id.orderLowTextView);
        orderHotTextView = findViewById(R.id.orderHotTextView);
        priceFromEditText = findViewById(R.id.priceFromEditText);
        priceToEditText = findViewById(R.id.priceToEditText);
        confirmTextView = findViewById(R.id.confirmTextView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        bIdString = getIntent().getStringExtra(BaseConstant.DATA_BID);
        gcIdString = getIntent().getStringExtra(BaseConstant.DATA_GCID);
        keywordString = getIntent().getStringExtra(BaseConstant.DATA_KEYWORD);

        setToolbar(mainToolbar, "");
        toolbarImageView.setImageResource(R.drawable.ic_navigation_cate);

        keyString = "";
        orderString = "";
        isGridModel = true;
        priceToString = "";
        priceFromString = "";
        if (!TextUtils.isEmpty(keywordString)) {
            searchEditText.setText(keywordString);
            searchEditText.setSelection(keywordString.length());
        }
        orientationImageView.setImageDrawable(BaseApplication.get().getMipmap(R.mipmap.ic_orientation_grid, R.color.grey));

        pageInt = 1;
        mainArrayList = new ArrayList<>();
        mainAdapter = new GoodsListAdapter(mainArrayList, isGridModel);
        setGirdModel();
        getGoods();

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), CateActivity.class, BaseConstant.CODE_CLASS));

        searchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                pageInt = 1;
                BaseApplication.get().hideKeyboard(getActivity());
                keywordString = searchEditText.getText().toString();
                getGoods();
            }
            return false;
        });

        orderTextView.setOnClickListener(view -> {
            screenRelativeLayout.setVisibility(View.GONE);
            screenTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            if (orderLinearLayout.getVisibility() == View.VISIBLE) {
                orderLinearLayout.setVisibility(View.GONE);
                orderTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            } else {
                orderLinearLayout.setVisibility(View.VISIBLE);
                orderTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
            }
        });

        saleTextView.setOnClickListener(view -> order(2, "1", "2"));

        screenTextView.setOnClickListener(view -> {
            orderLinearLayout.setVisibility(View.GONE);
            orderTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            if (screenRelativeLayout.getVisibility() == View.VISIBLE) {
                screenRelativeLayout.setVisibility(View.GONE);
                screenTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
            } else {
                screenRelativeLayout.setVisibility(View.VISIBLE);
                screenTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
            }
        });

        orientationImageView.setOnClickListener(view -> {
            isGridModel = !isGridModel;
            if (isGridModel) {
                setGirdModel();
                orientationImageView.setImageDrawable(BaseApplication.get().getMipmap(R.mipmap.ic_orientation_grid, R.color.grey));
            } else {
                setVerModel();
                orientationImageView.setImageDrawable(BaseApplication.get().getMipmap(R.mipmap.ic_orientation_ver, R.color.grey));
            }
        });

        orderCompTextView.setOnClickListener(view -> {
            orderTextView.setText("综合排序");
            order(1, "", "");
        });

        orderHighTextView.setOnClickListener(view -> {
            orderTextView.setText("价格从高到低");
            order(1, "3", "2");
        });

        orderLowTextView.setOnClickListener(view -> {
            orderTextView.setText("价格从低到高");
            order(1, "3", "1");
        });

        orderHotTextView.setOnClickListener(view -> {
            orderTextView.setText("人气排序");
            order(1, "2", "2");
        });

        confirmTextView.setOnClickListener(view -> order(3, keyString, orderString));

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

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK) {
            switch (req) {
                case BaseConstant.CODE_CLASS:
                    pageInt = 1;
                    mainArrayList.clear();
                    mainAdapter.notifyDataSetChanged();
                    bIdString = intent.getStringExtra(BaseConstant.DATA_BID);
                    gcIdString = intent.getStringExtra(BaseConstant.DATA_GCID);
                    getGoods();
                    break;
                default:
                    break;
            }
        }
    }

    //自定义方法

    private void getGoods() {

        mainPullRefreshView.setLoading();

        GoodsModel.get().goodsList(keywordString, orderString, keyString, priceFromString, priceToString, bIdString, gcIdString, pageInt + "", new BaseHttpListener() {
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

    private void setVerModel() {

        mainAdapter = new GoodsListAdapter(mainArrayList, isGridModel);
        mainPullRefreshView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        mainPullRefreshView.getRecyclerView().setPadding(BaseApplication.get().dipToPx(0), BaseApplication.get().dipToPx(0), BaseApplication.get().dipToPx(0), BaseApplication.get().dipToPx(0));
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.setItemDecoration();
        mainPullRefreshView.setComplete();

        mainAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, GoodsBean goodsBean) {
                BaseApplication.get().startGoods(getActivity(), goodsBean.getGoodsId());
            }

            @Override
            public void onCart(int position, GoodsBean goodsBean) {
                addCart(goodsBean.getGoodsId());
            }
        });

    }

    private void setGirdModel() {

        mainAdapter = new GoodsListAdapter(mainArrayList, isGridModel);
        mainPullRefreshView.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mainPullRefreshView.getRecyclerView().setPadding(BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2));
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.clearItemDecoration();
        mainPullRefreshView.setComplete();

        mainAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, GoodsBean goodsBean) {
                BaseApplication.get().startGoods(getActivity(), goodsBean.getGoodsId());
            }

            @Override
            public void onCart(int position, GoodsBean goodsBean) {
                addCart(goodsBean.getGoodsId());
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

    private void order(int type, String key, String order) {

        pageInt = 1;
        keyString = key;
        orderString = order;
        priceToString = priceToEditText.getText().toString();
        priceFromString = priceFromEditText.getText().toString();

        orderLinearLayout.setVisibility(View.GONE);
        screenRelativeLayout.setVisibility(View.GONE);

        orderTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
        screenTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);

        switch (type) {
            case 1:
                orderTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
                saleTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
                break;
            case 2:
                orderTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
                saleTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
                break;
        }

        if (TextUtils.isEmpty(priceFromString) && TextUtils.isEmpty(priceToString)) {
            screenTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
        } else {
            screenTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
        }


        mainArrayList.clear();
        mainAdapter.notifyDataSetChanged();

        getGoods();

    }

}
