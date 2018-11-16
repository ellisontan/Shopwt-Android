package top.yokey.shopwt.activity.goods;

import android.support.v7.widget.AppCompatTextView;

import com.squareup.otto.Subscribe;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.EvaluateGoodsListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.EvaluateGoodsBean;
import top.yokey.base.event.GoodsBeanEvent;
import top.yokey.base.model.GoodsModel;
import top.yokey.base.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_goods_evaluate)
public class EvaluateFragment extends BaseFragment {

    @ViewInject(R.id.allTextView)
    private AppCompatTextView allTextView;
    @ViewInject(R.id.goodTextView)
    private AppCompatTextView goodTextView;
    @ViewInject(R.id.inTextView)
    private AppCompatTextView inTextView;
    @ViewInject(R.id.badTextView)
    private AppCompatTextView badTextView;
    @ViewInject(R.id.imageTextView)
    private AppCompatTextView imageTextView;
    @ViewInject(R.id.additionTextView)
    private AppCompatTextView additionTextView;
    private AppCompatTextView[] navigationTextView;

    @ViewInject(R.id.mainPullRefreshView)
    private PullRefreshView mainPullRefreshView;

    private int pageInt;
    private String typeString;
    private String goodsIdString;
    private EvaluateGoodsListAdapter mainAdapter;
    private ArrayList<EvaluateGoodsBean> mainArrayList;

    @Override
    public void initData() {

        typeString = "";
        goodsIdString = "";

        pageInt = 1;
        mainArrayList = new ArrayList<>();
        mainAdapter = new EvaluateGoodsListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        navigationTextView = new AppCompatTextView[6];
        navigationTextView[0] = allTextView;
        navigationTextView[1] = goodTextView;
        navigationTextView[2] = inTextView;
        navigationTextView[3] = badTextView;
        navigationTextView[4] = imageTextView;
        navigationTextView[5] = additionTextView;

    }

    @Override
    public void initEven() {

        allTextView.setOnClickListener(view -> updateNavigation(0));

        goodTextView.setOnClickListener(view -> updateNavigation(1));

        inTextView.setOnClickListener(view -> updateNavigation(2));

        badTextView.setOnClickListener(view -> updateNavigation(3));

        imageTextView.setOnClickListener(view -> updateNavigation(4));

        additionTextView.setOnClickListener(view -> updateNavigation(5));

        mainAdapter.setOnItemClickListener((position, evaluateGoodsBean) -> {

        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageInt = 1;
                getEvaluate();
            }

            @Override
            public void onLoadMore() {
                getEvaluate();
            }
        });

    }

    //自定义方法

    private void getEvaluate() {

        mainPullRefreshView.setLoading();

        GoodsModel.get().goodsEvaluate(goodsIdString, typeString, pageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt == 1) {
                    mainArrayList.clear();
                }
                if (pageInt <= baseBean.getPageTotal()) {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_eval_list");
                    mainArrayList.addAll(JsonUtil.json2ArrayList(data, EvaluateGoodsBean.class));
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

    private void updateNavigation(int position) {

        for (AppCompatTextView appCompatTextView : navigationTextView) {
            appCompatTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            appCompatTextView.setBackgroundResource(R.drawable.border_evaluate);
        }

        navigationTextView[position].setTextColor(BaseApplication.get().getColors(R.color.white));
        navigationTextView[position].setBackgroundResource(R.drawable.border_evaluate_press);

        pageInt = 1;
        typeString = position == 0 ? "" : position + "";
        getEvaluate();

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGoodsBeanEvent(GoodsBeanEvent event) {
        try {
            String temp = "";
            JSONObject jsonObject = new JSONObject(event.getBaseBean().getDatas());
            jsonObject = new JSONObject(jsonObject.getString("goods_content"));
            goodsIdString = jsonObject.getString("goods_id");
            getEvaluate();
        } catch (JSONException e) {
            BaseToast.get().show("数据解析错误！");
            e.printStackTrace();
        }
    }

}
