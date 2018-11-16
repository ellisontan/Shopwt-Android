package top.yokey.shopwt.activity.main;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import io.github.xudaojie.qrcodelib.CaptureActivity;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.home.ChatListActivity;
import top.yokey.shopwt.activity.home.NoticeActivity;
import top.yokey.shopwt.adapter.HomeListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseBusClient;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.UBLImageLoader;
import top.yokey.base.bean.ArticleBean;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.HomeBean;
import top.yokey.base.event.MainPositionEvent;
import top.yokey.base.model.IndexModel;
import top.yokey.base.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_main_home)
public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.mainSwipeRefreshLayout)
    private SwipeRefreshLayout mainSwipeRefreshLayout;
    @ViewInject(R.id.searchEditText)
    private AppCompatEditText searchEditText;
    @ViewInject(R.id.messageImageView)
    private AppCompatImageView messageImageView;
    @ViewInject(R.id.scanImageView)
    private AppCompatImageView scanImageView;
    @ViewInject(R.id.mainBanner)
    private Banner mainBanner;
    @ViewInject(R.id.navigationLinearLayout)
    private LinearLayoutCompat navigationLinearLayout;
    @ViewInject(R.id.oneLinearLayout)
    private LinearLayoutCompat oneLinearLayout;
    @ViewInject(R.id.oneImageView)
    private AppCompatImageView oneImageView;
    @ViewInject(R.id.oneTextView)
    private AppCompatTextView oneTextView;
    @ViewInject(R.id.twoLinearLayout)
    private LinearLayoutCompat twoLinearLayout;
    @ViewInject(R.id.twoImageView)
    private AppCompatImageView twoImageView;
    @ViewInject(R.id.twoTextView)
    private AppCompatTextView twoTextView;
    @ViewInject(R.id.thrLinearLayout)
    private LinearLayoutCompat thrLinearLayout;
    @ViewInject(R.id.thrImageView)
    private AppCompatImageView thrImageView;
    @ViewInject(R.id.thrTextView)
    private AppCompatTextView thrTextView;
    @ViewInject(R.id.fouLinearLayout)
    private LinearLayoutCompat fouLinearLayout;
    @ViewInject(R.id.fouImageView)
    private AppCompatImageView fouImageView;
    @ViewInject(R.id.fouTextView)
    private AppCompatTextView fouTextView;
    @ViewInject(R.id.fivLinearLayout)
    private LinearLayoutCompat fivLinearLayout;
    @ViewInject(R.id.fivImageView)
    private AppCompatImageView fivImageView;
    @ViewInject(R.id.fivTextView)
    private AppCompatTextView fivTextView;
    @ViewInject(R.id.sixLinearLayout)
    private LinearLayoutCompat sixLinearLayout;
    @ViewInject(R.id.sixImageView)
    private AppCompatImageView sixImageView;
    @ViewInject(R.id.sixTextView)
    private AppCompatTextView sixTextView;
    @ViewInject(R.id.sevLinearLayout)
    private LinearLayoutCompat sevLinearLayout;
    @ViewInject(R.id.sevImageView)
    private AppCompatImageView sevImageView;
    @ViewInject(R.id.sevTextView)
    private AppCompatTextView sevTextView;
    @ViewInject(R.id.eigLinearLayout)
    private LinearLayoutCompat eigLinearLayout;
    @ViewInject(R.id.eigImageView)
    private AppCompatImageView eigImageView;
    @ViewInject(R.id.eigTextView)
    private AppCompatTextView eigTextView;
    @ViewInject(R.id.nigLinearLayout)
    private LinearLayoutCompat nigLinearLayout;
    @ViewInject(R.id.nigImageView)
    private AppCompatImageView nigImageView;
    @ViewInject(R.id.nigTextView)
    private AppCompatTextView nigTextView;
    @ViewInject(R.id.tenLinearLayout)
    private LinearLayoutCompat tenLinearLayout;
    @ViewInject(R.id.tenImageView)
    private AppCompatImageView tenImageView;
    @ViewInject(R.id.tenTextView)
    private AppCompatTextView tenTextView;
    @ViewInject(R.id.noticeMarqueeView)
    private MarqueeView noticeMarqueeView;
    @ViewInject(R.id.noticeTextView)
    private AppCompatTextView noticeTextView;
    @ViewInject(R.id.mainRecyclerView)
    private RecyclerView mainRecyclerView;

    private HomeListAdapter mainAdapter;
    private ArrayList<HomeBean> mainArrayList;
    private ArrayList<ArticleBean> articleArrayList;

    @Override
    public void initData() {

        articleArrayList = new ArrayList<>();
        mainBanner.setImageLoader(new UBLImageLoader());
        mainBanner.setDelayTime(BaseConstant.TIME_DELAY);
        mainBanner.setIndicatorGravity(BannerConfig.CENTER);
        mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        mainArrayList = new ArrayList<>();
        mainAdapter = new HomeListAdapter(getActivity(), mainArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), mainRecyclerView, mainAdapter);
        BaseApplication.get().setSwipeRefreshLayout(mainSwipeRefreshLayout);

        getIndex();
        getGG();

    }

    @Override
    public void initEven() {

        scanImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), CaptureActivity.class, BaseConstant.CODE_QRCODE));

        searchEditText.setOnClickListener(view -> BaseBusClient.get().post(new MainPositionEvent(2)));

        messageImageView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), ChatListActivity.class));

        mainSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            mainSwipeRefreshLayout.setRefreshing(false);
            getIndex();
            getGG();
        }, BaseConstant.TIME_REFRESH));

        noticeMarqueeView.setOnItemClickListener((position, textView) -> BaseApplication.get().startNoticeShow(getActivity(), articleArrayList.get(position)));

        noticeTextView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), NoticeActivity.class));

    }

    @Override
    public void onStart() {
        super.onStart();
        mainBanner.startAutoPlay();
        noticeMarqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainBanner.stopAutoPlay();
        noticeMarqueeView.stopFlipping();
    }

    //自定义方法

    private void getIndex() {

        IndexModel.get().index(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    String name;
                    HomeBean indexBean;
                    JSONObject jsonObject;
                    mainArrayList.clear();
                    navigationLinearLayout.setVisibility(View.GONE);
                    String index = JsonUtil.getDatasString(baseBean.getDatas(), "index");
                    JSONArray jsonArray = new JSONArray(index);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = new JSONObject(jsonArray.getString(i));
                        //广告图
                        name = "show_list";
                        if (jsonObject.has(name)) {
                            handlerAdvList(jsonObject.getString(name));
                        }
                        //Home1
                        name = "home1";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setHome1Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Home1Bean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Home2
                        name = "home2";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setHome2Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Home2Bean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Home3
                        name = "home3";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setHome3Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Home3Bean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Home4
                        name = "home4";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setHome4Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Home4Bean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Home5
                        name = "home5";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setHome5Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Home5Bean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Home7
                        name = "home7";
                        if (jsonObject.has(name)) {
                            handlerHome7(jsonObject.getString(name));
                        }
                        //Goods
                        name = "goods";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setGoodsBean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.GoodsBean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Goods1
                        name = "goods1";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setGoods1Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Goods1Bean.class));
                            mainArrayList.add(indexBean);
                        }
                        //Goods2
                        name = "goods2";
                        if (jsonObject.has(name)) {
                            indexBean = new HomeBean();
                            indexBean.setType(name);
                            indexBean.setGoods2Bean(JsonUtil.json2Bean(jsonObject.getString(name), HomeBean.Goods2Bean.class));
                            mainArrayList.add(indexBean);
                        }
                    }
                    mainAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getIndex();
                    }
                }.start();
            }
        });

    }

    private void getGG() {

        IndexModel.get().getGG(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                articleArrayList.clear();
                List<String> list = new ArrayList<>();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "article_list");
                articleArrayList.addAll(JsonUtil.json2ArrayList(data, ArticleBean.class));
                for (int i = 0; i < articleArrayList.size(); i++) {
                    list.add(articleArrayList.get(i).getArticleTitle());
                }
                noticeMarqueeView.startWithList(list);
            }

            @Override
            public void onFailure(String reason) {

            }
        });

    }

    private void handlerHome7(String json) {

        try {
            final JSONObject jsonObject = new JSONObject(json);
            //第一个
            final String squareType = jsonObject.getString("square_type");
            final String squareData = jsonObject.getString("square_data");
            oneTextView.setText(jsonObject.getString("square_ico_name"));
            oneImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("square_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("square_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), oneImageView);
            oneLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), squareType, squareData));
            //第二个
            final String rectangle1Type = jsonObject.getString("rectangle1_type");
            final String rectangle1Data = jsonObject.getString("rectangle1_data");
            twoTextView.setText(jsonObject.getString("rectangle1_ico_name"));
            twoImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle1_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle1_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), twoImageView);
            twoLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle1Type, rectangle1Data));
            //第三个
            final String rectangle2Type = jsonObject.getString("rectangle2_type");
            final String rectangle2Data = jsonObject.getString("rectangle2_data");
            thrTextView.setText(jsonObject.getString("rectangle2_ico_name"));
            thrImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle2_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle2_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), thrImageView);
            thrLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle2Type, rectangle2Data));
            //第四个
            final String rectangle3Type = jsonObject.getString("rectangle3_type");
            final String rectangle3Data = jsonObject.getString("rectangle3_data");
            fouTextView.setText(jsonObject.getString("rectangle3_ico_name"));
            fouImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle3_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle3_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), fouImageView);
            fouLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle3Type, rectangle3Data));
            //第五个
            final String rectangle4Type = jsonObject.getString("rectangle4_type");
            final String rectangle4Data = jsonObject.getString("rectangle4_data");
            fivTextView.setText(jsonObject.getString("rectangle4_ico_name"));
            fivImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle4_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle4_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), fivImageView);
            fivLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle4Type, rectangle4Data));
            //第六个
            final String rectangle5Type = jsonObject.getString("rectangle5_type");
            final String rectangle5Data = jsonObject.getString("rectangle5_data");
            sixTextView.setText(jsonObject.getString("rectangle5_ico_name"));
            sixImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle5_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle5_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), sixImageView);
            sixLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle5Type, rectangle5Data));
            //第七个
            final String rectangle6Type = jsonObject.getString("rectangle6_type");
            final String rectangle6Data = jsonObject.getString("rectangle6_data");
            sevTextView.setText(jsonObject.getString("rectangle6_ico_name"));
            sevImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle6_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle6_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), sevImageView);
            sevLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle6Type, rectangle6Data));
            //第八个
            final String rectangle7Type = jsonObject.getString("rectangle7_type");
            final String rectangle7Data = jsonObject.getString("rectangle7_data");
            eigTextView.setText(jsonObject.getString("rectangle7_ico_name"));
            eigImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle7_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle7_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), eigImageView);
            eigLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle7Type, rectangle7Data));
            //第九个
            final String rectangle8Type = jsonObject.getString("rectangle8_type");
            final String rectangle8Data = jsonObject.getString("rectangle8_data");
            nigTextView.setText(jsonObject.getString("rectangle8_ico_name"));
            nigImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle8_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle8_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), nigImageView);
            nigLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle8Type, rectangle8Data));
            //第十个
            final String rectangle9Type = jsonObject.getString("rectangle9_type");
            final String rectangle9Data = jsonObject.getString("rectangle9_data");
            tenTextView.setText(jsonObject.getString("rectangle9_ico_name"));
            tenImageView.setBackgroundDrawable(BaseApplication.get().getGradientDrawable(BaseApplication.get().dipToPx(28), Color.parseColor(jsonObject.getString("rectangle9_ico_color"))));
            BaseImageLoader.get().display(jsonObject.getString("rectangle9_image"), BaseApplication.get().dipToPx(28), BaseApplication.get().dipToPx(28), tenImageView);
            tenLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(getActivity(), rectangle9Type, rectangle9Data));
            navigationLinearLayout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            navigationLinearLayout.setVisibility(View.GONE);
            e.printStackTrace();
        }

    }

    private void handlerAdvList(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<HomeBean.AdvListBean> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonObject.getString("item"));
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(JsonUtil.json2Bean(jsonArray.getString(i), HomeBean.AdvListBean.class));
            }
            if (arrayList.size() == 0) {
                mainBanner.setVisibility(View.GONE);
            } else {
                mainBanner.setVisibility(View.VISIBLE);
                List<String> image = new ArrayList<>();
                final List<String> type = new ArrayList<>();
                final List<String> data = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    image.add(arrayList.get(i).getImage());
                    type.add(arrayList.get(i).getType());
                    data.add(arrayList.get(i).getData());
                }
                mainBanner.setOnBannerListener(position -> BaseApplication.get().startTypeValue(getActivity(), type.get(position), data.get(position)));
                mainBanner.update(image);
                mainBanner.start();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
