package top.yokey.shopwt.activity.goods;

import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import com.squareup.otto.Subscribe;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import cn.sharesdk.onekeyshare.OnekeyShare;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.base.LoginActivity;
import top.yokey.shopwt.activity.choose.AreaActivity;
import top.yokey.shopwt.activity.main.CartActivity;
import top.yokey.shopwt.adapter.EvaluateGoodsSimpleListAdapter;
import top.yokey.shopwt.adapter.GoodsCommendListAdapter;
import top.yokey.shopwt.adapter.SpecListAdapter;
import top.yokey.shopwt.adapter.VoucherGoodsListAdapter;
import top.yokey.base.base.BaseAnimClient;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseBusClient;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.base.UBLImageLoader;
import top.yokey.base.bean.EvaluateGoodsBean;
import top.yokey.base.bean.GoodsCommendBean;
import top.yokey.base.bean.VoucherGoodsBean;
import top.yokey.base.event.GoodsAreaEvent;
import top.yokey.base.event.GoodsBeanEvent;
import top.yokey.base.event.GoodsEvaluateEvent;
import top.yokey.base.event.GoodsGoneEvent;
import top.yokey.base.event.GoodsShowEvent;
import top.yokey.base.event.GoodsIdEvent;
import top.yokey.base.model.GoodsModel;
import top.yokey.base.model.MemberCartModel;
import top.yokey.base.model.MemberFavoritesModel;
import top.yokey.base.model.MemberVoucherModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.view.CenterTextView;
import top.yokey.shopwt.view.CountdownTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_goods_goods)
public class GoodsFragment extends BaseFragment {

    @ViewInject(R.id.headerRelativeLayout)
    private RelativeLayout headerRelativeLayout;
    @ViewInject(R.id.mainBanner)
    private Banner mainBanner;
    @ViewInject(R.id.favoritesImageView)
    private AppCompatImageView favoritesImageView;
    @ViewInject(R.id.shareImageView)
    private AppCompatImageView shareImageView;
    @ViewInject(R.id.saleRelativeLayout)
    private RelativeLayout saleRelativeLayout;
    @ViewInject(R.id.saleTypeTextView)
    private AppCompatTextView saleTypeTextView;
    @ViewInject(R.id.saleTimeTextView)
    private CountdownTextView saleTimeTextView;
    @ViewInject(R.id.nameTextView)
    private AppCompatTextView nameTextView;
    @ViewInject(R.id.descTextView)
    private AppCompatTextView descTextView;
    @ViewInject(R.id.moneyTextView)
    private AppCompatTextView moneyTextView;
    @ViewInject(R.id.marketPriceTextView)
    private AppCompatTextView marketPriceTextView;
    @ViewInject(R.id.mobileTextView)
    private AppCompatTextView mobileTextView;
    @ViewInject(R.id.saleTextView)
    private AppCompatTextView saleTextView;
    @ViewInject(R.id.activityLinearLayout)
    private LinearLayoutCompat activityLinearLayout;
    @ViewInject(R.id.activityTitleTextView)
    private AppCompatTextView activityTitleTextView;
    @ViewInject(R.id.activityDescTextView)
    private AppCompatTextView activityDescTextView;
    @ViewInject(R.id.manSongLinearLayout)
    private LinearLayoutCompat manSongLinearLayout;
    @ViewInject(R.id.manSongDescTextView)
    private AppCompatTextView manSongDescTextView;
    @ViewInject(R.id.manSongGoodsImageView)
    private AppCompatImageView manSongGoodsImageView;
    @ViewInject(R.id.voucherTextView)
    private AppCompatTextView voucherTextView;
    @ViewInject(R.id.areaRelativeLayout)
    private RelativeLayout areaRelativeLayout;
    @ViewInject(R.id.areaAddressTextView)
    private AppCompatTextView areaAddressTextView;
    @ViewInject(R.id.areaHaveTextView)
    private AppCompatTextView areaHaveTextView;
    @ViewInject(R.id.areaChooseTextView)
    private AppCompatTextView areaChooseTextView;
    @ViewInject(R.id.specRelativeLayout)
    private RelativeLayout specRelativeLayout;
    @ViewInject(R.id.specOneTextView)
    private AppCompatTextView specOneTextView;
    @ViewInject(R.id.specTwoTextView)
    private AppCompatTextView specTwoTextView;
    private AppCompatTextView[] specTextView;
    @ViewInject(R.id.serviceDescTextView)
    private AppCompatTextView serviceDescTextView;
    @ViewInject(R.id.serviceSevDayTextView)
    private AppCompatTextView serviceSevDayTextView;
    @ViewInject(R.id.serviceQualityTextView)
    private AppCompatTextView serviceQualityTextView;
    @ViewInject(R.id.serviceReissueTextView)
    private AppCompatTextView serviceReissueTextView;
    @ViewInject(R.id.serviceLogisticsTextView)
    private AppCompatTextView serviceLogisticsTextView;
    @ViewInject(R.id.evaluateRelativeLayout)
    private RelativeLayout evaluateRelativeLayout;
    @ViewInject(R.id.evaluateDescTextView)
    private AppCompatTextView evaluateDescTextView;
    @ViewInject(R.id.evaluateNumberTextView)
    private AppCompatTextView evaluateNumberTextView;
    @ViewInject(R.id.evaluateRecyclerView)
    private RecyclerView evaluateRecyclerView;
    @ViewInject(R.id.storeRelativeLayout)
    private RelativeLayout storeRelativeLayout;
    @ViewInject(R.id.storeNameTextView)
    private AppCompatTextView storeNameTextView;
    @ViewInject(R.id.storeOwnTextView)
    private AppCompatTextView storeOwnTextView;
    @ViewInject(R.id.storeDescTextView)
    private AppCompatTextView storeDescTextView;
    @ViewInject(R.id.storeDescPercentTextView)
    private AppCompatTextView storeDescPercentTextView;
    @ViewInject(R.id.storeServiceTextView)
    private AppCompatTextView storeServiceTextView;
    @ViewInject(R.id.storeServicePercentTextView)
    private AppCompatTextView storeServicePercentTextView;
    @ViewInject(R.id.storeDeliveryTextView)
    private AppCompatTextView storeDeliveryTextView;
    @ViewInject(R.id.storeDeliveryPercentTextView)
    private AppCompatTextView storeDeliveryPercentTextView;
    @ViewInject(R.id.commendRecyclerView)
    private RecyclerView commendRecyclerView;
    @ViewInject(R.id.customerTextView)
    private CenterTextView customerTextView;
    @ViewInject(R.id.cartTextView)
    private CenterTextView cartTextView;
    @ViewInject(R.id.addCartTextView)
    private AppCompatTextView addCartTextView;
    @ViewInject(R.id.buyTextView)
    private AppCompatTextView buyTextView;
    @ViewInject(R.id.chooseRelativeLayout)
    private RelativeLayout chooseRelativeLayout;
    @ViewInject(R.id.chooseGoodsImageView)
    private AppCompatImageView chooseGoodsImageView;
    @ViewInject(R.id.chooseNameTextView)
    private AppCompatTextView chooseNameTextView;
    @ViewInject(R.id.chooseMoneyTextView)
    private AppCompatTextView chooseMoneyTextView;
    @ViewInject(R.id.chooseStorageTextView)
    private AppCompatTextView chooseStorageTextView;
    @ViewInject(R.id.chooseLineOneView)
    private View chooseLineOneView;
    @ViewInject(R.id.chooseLineTwoView)
    private View chooseLineTwoView;
    @ViewInject(R.id.chooseValueOneTextView)
    private AppCompatTextView chooseValueOneTextView;
    @ViewInject(R.id.chooseValueTwoTextView)
    private AppCompatTextView chooseValueTwoTextView;
    @ViewInject(R.id.chooseValueOneRecyclerView)
    private RecyclerView chooseValueOneRecyclerView;
    @ViewInject(R.id.chooseValueTwoRecyclerView)
    private RecyclerView chooseValueTwoRecyclerView;
    private View[] chooseLineView;
    private AppCompatTextView[] chooseValueTextView;
    private RecyclerView[] chooseValueRecyclerView;
    @ViewInject(R.id.chooseAddTextView)
    private AppCompatTextView chooseAddTextView;
    @ViewInject(R.id.chooseNumberEditText)
    private AppCompatEditText chooseNumberEditText;
    @ViewInject(R.id.chooseSubTextView)
    private AppCompatTextView chooseSubTextView;
    @ViewInject(R.id.voucherLinearLayout)
    private LinearLayoutCompat voucherLinearLayout;
    @ViewInject(R.id.voucherStoreNameTextView)
    private AppCompatTextView voucherStoreNameTextView;
    @ViewInject(R.id.voucherRecyclerView)
    private RecyclerView voucherRecyclerView;
    @ViewInject(R.id.nightTextView)
    private AppCompatTextView nightTextView;

    private String goodsId;
    private String storeId;
    private String memberId;
    private String jsonString;
    private boolean haveGoods;
    private boolean isFavorites;
    private boolean isBackBoolean;
    private String manSongGoodsId;
    private String lowerLimitString;
    private String upperLimitString;
    private String goodsStorageString;

    private String shareUrl;
    private String shareText;
    private String shareTitle;
    private String shareTitleUrl;
    private String shareImageUrl;

    private String[] specString;
    private ArrayList<String> goodsImageArrayList;
    private ArrayList<HashMap<String, String>> specNameArrayList;
    private ArrayList<HashMap<String, String>> specValueArrayList;
    private ArrayList<HashMap<String, String>> goodsSpecArrayList;
    private ArrayList<HashMap<String, String>> specListArrayList;

    private EvaluateGoodsSimpleListAdapter evaluateGoodsAdapter;
    private ArrayList<EvaluateGoodsBean> evaluateGoodsArrayList;

    private GoodsCommendListAdapter commendAdapter;
    private ArrayList<GoodsCommendBean> commendArrayList;

    private VoucherGoodsListAdapter voucherAdapter;
    private ArrayList<VoucherGoodsBean> voucherArrayList;

    @Override
    public void initData() {

        storeId = "";
        memberId = "";
        haveGoods = true;
        isFavorites = false;
        lowerLimitString = "";
        upperLimitString = "";
        specString = new String[]{"", ""};

        shareUrl = "";
        shareText = "";
        shareTitle = "";
        shareTitleUrl = "";
        shareImageUrl = "";

        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(BaseApplication.get().getWidth(), BaseApplication.get().getWidth());
        headerRelativeLayout.setLayoutParams(layoutParams);

        mainBanner.setImageLoader(new UBLImageLoader());
        mainBanner.setDelayTime(BaseConstant.TIME_DELAY);
        mainBanner.setIndicatorGravity(BannerConfig.CENTER);
        mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        specNameArrayList = new ArrayList<>();
        specValueArrayList = new ArrayList<>();
        goodsSpecArrayList = new ArrayList<>();
        goodsImageArrayList = new ArrayList<>();

        evaluateGoodsArrayList = new ArrayList<>();
        evaluateGoodsAdapter = new EvaluateGoodsSimpleListAdapter(evaluateGoodsArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), evaluateRecyclerView, evaluateGoodsAdapter);

        commendArrayList = new ArrayList<>();
        commendAdapter = new GoodsCommendListAdapter(commendArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), commendRecyclerView, commendAdapter);
        commendRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        voucherArrayList = new ArrayList<>();
        voucherAdapter = new VoucherGoodsListAdapter(voucherArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), voucherRecyclerView, voucherAdapter);

        specTextView = new AppCompatTextView[2];
        specTextView[0] = specOneTextView;
        specTextView[1] = specTwoTextView;
        chooseLineView = new View[2];
        chooseLineView[0] = chooseLineOneView;
        chooseLineView[1] = chooseLineTwoView;
        chooseValueTextView = new AppCompatTextView[2];
        chooseValueTextView[0] = chooseValueOneTextView;
        chooseValueTextView[1] = chooseValueTwoTextView;
        chooseValueRecyclerView = new RecyclerView[2];
        chooseValueRecyclerView[0] = chooseValueOneRecyclerView;
        chooseValueRecyclerView[1] = chooseValueTwoRecyclerView;

        chooseValueRecyclerView[0].setVisibility(View.GONE);
        chooseValueRecyclerView[1].setVisibility(View.GONE);
        chooseValueTextView[0].setVisibility(View.GONE);
        chooseValueTextView[1].setVisibility(View.GONE);
        chooseLineView[0].setVisibility(View.GONE);
        chooseLineView[1].setVisibility(View.GONE);
        specTextView[0].setVisibility(View.GONE);
        specTextView[1].setVisibility(View.GONE);

    }

    @Override
    public void initEven() {

        favoritesImageView.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
            } else {
                if (isFavorites) {
                    favoritesDel();
                } else {
                    favoritesAdd();
                }
            }
        });

        shareImageView.setOnClickListener(view -> share());

        manSongGoodsImageView.setOnClickListener(view -> BaseBusClient.get().post(new GoodsIdEvent(manSongGoodsId)));

        voucherTextView.setOnClickListener(view -> showVoucherLayout());

        voucherAdapter.setOnItemClickListener((position, voucherGoodsBean) -> voucherFreeex(voucherGoodsBean.getVoucherTId()));

        areaRelativeLayout.setOnClickListener(view -> BaseApplication.get().start(getActivity(), AreaActivity.class, BaseConstant.CODE_AREA));

        specRelativeLayout.setOnClickListener(view -> showChooseLayout());

        storeRelativeLayout.setOnClickListener(view -> BaseApplication.get().startStore(getActivity(), storeId));

        evaluateRelativeLayout.setOnClickListener(view -> BaseBusClient.get().post(new GoodsEvaluateEvent("")));

        commendAdapter.setOnItemClickListener((position, goodsCommendBean) -> BaseBusClient.get().post(new GoodsIdEvent(goodsCommendBean.getGoodsId())));

        chooseAddTextView.setOnClickListener(view -> {
            String number = (Integer.parseInt(chooseNumberEditText.getText().toString()) + 1) + "";
            chooseNumberEditText.setText(number);
            changeNumber();
        });

        chooseNumberEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                changeNumber();
            }
            return false;
        });

        chooseSubTextView.setOnClickListener(view -> {
            String number = (Integer.parseInt(chooseNumberEditText.getText().toString()) - 1) + "";
            chooseNumberEditText.setText(number);
            changeNumber();
        });

        nightTextView.setOnClickListener(view -> {
            goneChooseLayout();
            goneVoucherLayout();
        });

        chooseRelativeLayout.setOnClickListener(view -> {
            //仅仅只是为了防止点到暗色标签
        });

        customerTextView.setOnClickListener(view -> BaseApplication.get().startChatOnly(getActivity(), memberId, goodsId));

        cartTextView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), CartActivity.class));

        addCartTextView.setOnClickListener(view -> {
            if (!haveGoods) {
                BaseToast.get().show("没货啦！");
                return;
            }
            if (chooseRelativeLayout.getVisibility() == View.GONE) {
                showChooseLayout();
            } else {
                addCart();
            }
        });

        buyTextView.setOnClickListener(view -> {
            if (!haveGoods) {
                BaseToast.get().show("没货啦！");
                return;
            }
            if (chooseRelativeLayout.getVisibility() == View.GONE) {
                showChooseLayout();
            } else {
                buy();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mainBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainBanner.stopAutoPlay();
    }

    //自定义方法

    private void buy() {

        if (!BaseApplication.get().isLogin()) {
            BaseApplication.get().start(getActivity(), LoginActivity.class);
            return;
        }

        goneChooseLayout();
        String cartId = goodsId + "|" + chooseNumberEditText.getText().toString();
        BaseApplication.get().startGoodsBuy(getActivity(), cartId, "");

    }

    private void share() {

        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitleUrl(shareTitleUrl);
        oks.setImageUrl(shareImageUrl);
        oks.setTitle(shareTitle);
        oks.setText(shareText);
        oks.setUrl(shareUrl);
        //noinspection ConstantConditions
        oks.show(getActivity());

    }

    private void addCart() {

        if (!BaseApplication.get().isLogin()) {
            BaseApplication.get().start(getActivity(), LoginActivity.class);
            return;
        }

        MemberCartModel.get().cartAdd(goodsId, chooseNumberEditText.getText().toString(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                goneChooseLayout();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    @SuppressWarnings("ConstantConditions")
    private void handlerData() {

        try {
            String temp = "";
            JSONObject jsonObject;
            JSONObject mainJSONObject = new JSONObject(jsonString);
            JSONObject goodsInfoJSONObject = new JSONObject(mainJSONObject.getString("goods_content"));
            String[] goodsImages = mainJSONObject.getString("goods_image").split(",");
            goodsId = goodsInfoJSONObject.getString("goods_id");
            shareUrl = BaseConstant.URL_GOODS_DETAILED + goodsId;
            shareTitleUrl = shareUrl;
            //轮播图
            goodsImageArrayList.clear();
            Collections.addAll(goodsImageArrayList, goodsImages);
            shareImageUrl = goodsImageArrayList.get(0);
            mainBanner.update(goodsImageArrayList);
            mainBanner.start();
            //是否收藏
            isFavorites = BaseApplication.get().isLogin() && mainJSONObject.getBoolean("is_favorate");
            favoritesImageView.setImageResource(isFavorites ? R.drawable.ic_favorite_press : R.drawable.ic_favorite_primary);
            //商品信息
            nameTextView.setText(goodsInfoJSONObject.getString("goods_name"));
            descTextView.setText(goodsInfoJSONObject.getString("goods_jingle"));
            descTextView.setVisibility(TextUtils.isEmpty(goodsInfoJSONObject.getString("goods_jingle")) ? View.GONE : View.VISIBLE);
            goodsStorageString = goodsInfoJSONObject.getString("goods_storage");
            shareTitle = nameTextView.getText().toString();
            shareText = descTextView.getText().toString();
            moneyTextView.setText("￥");
            marketPriceTextView.setText("￥");
            mobileTextView.setVisibility(View.GONE);
            saleRelativeLayout.setVisibility(View.GONE);
            if (goodsInfoJSONObject.has("goods_sale_type") && !goodsInfoJSONObject.getString("goods_sale_type").equals("0")) {
                activityLinearLayout.setVisibility(View.VISIBLE);
                activityTitleTextView.setText(goodsInfoJSONObject.getString("title"));
                switch (goodsInfoJSONObject.getString("sale_type")) {
                    case "sole":
                        mobileTextView.setVisibility(View.VISIBLE);
                        temp = "手机专享价格￥" + goodsInfoJSONObject.getString("sale_price");
                        break;
                    case "xianshi":
                        saleRelativeLayout.setVisibility(View.VISIBLE);
                        saleTypeTextView.setText("限时打折");
                        lowerLimitString = goodsInfoJSONObject.getString("lower_limit");
                        temp = "直降￥" + goodsInfoJSONObject.getString("down_price") + "，最低 " + lowerLimitString + " 件起";
                        saleTimeTextView.init("", Long.parseLong(goodsInfoJSONObject.getString("xs_time")), "距离结束：", "");
                        saleTimeTextView.start(0);
                        break;
                    case "groupbuy":
                        upperLimitString = goodsInfoJSONObject.getString("upper_limit");
                        temp = "直降￥" + goodsInfoJSONObject.getString("down_price") + "，限购 " + upperLimitString + " 件";
                        break;
                    case "robbuy":
                        saleRelativeLayout.setVisibility(View.VISIBLE);
                        saleTypeTextView.setText("限时抢购");
                        upperLimitString = goodsInfoJSONObject.getString("upper_limit");
                        temp = "限购 " + upperLimitString + " 件，" + goodsInfoJSONObject.getString("remark");
                        saleTimeTextView.init("", Long.parseLong(goodsInfoJSONObject.getString("end_time")), "距离结束：", "");
                        saleTimeTextView.start(0);
                        break;
                }
                activityDescTextView.setText(temp);
                moneyTextView.append(goodsInfoJSONObject.getString("sale_price"));
                marketPriceTextView.append(goodsInfoJSONObject.getString("goods_price"));
            } else {
                activityLinearLayout.setVisibility(View.GONE);
                moneyTextView.append(goodsInfoJSONObject.getString("goods_price"));
                marketPriceTextView.append(goodsInfoJSONObject.getString("goods_marketprice"));
            }
            marketPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            saleTextView.setText("销量：");
            saleTextView.append(goodsInfoJSONObject.getString("goods_salenum"));
            //满送
            temp = mainJSONObject.getString("mansong_info");
            if (temp.equals("null") || temp.equals("[]")) {
                manSongLinearLayout.setVisibility(View.GONE);
            } else {
                manSongLinearLayout.setVisibility(View.VISIBLE);
                JSONObject manSongJSONObject = new JSONObject(temp);
                JSONArray jsonArray = new JSONArray(manSongJSONObject.getString("rules"));
                manSongJSONObject = new JSONObject(jsonArray.getString(0));
                temp = "单笔订单满￥" + manSongJSONObject.getString("price") + "，立减￥" + manSongJSONObject.getString("discount") + "，送礼品";
                manSongGoodsId = manSongJSONObject.getString("goods_id");
                manSongDescTextView.setText(temp);
                if (manSongJSONObject.has("goods_image_url")) {
                    BaseImageLoader.get().display(manSongJSONObject.getString("goods_image_url"), manSongGoodsImageView);
                }
            }
            //代金券
            if (mainJSONObject.has("voucher")) {
                voucherArrayList.clear();
                voucherTextView.setVisibility(View.VISIBLE);
                temp = mainJSONObject.getString("voucher");
                voucherArrayList.addAll(JsonUtil.json2ArrayList(temp, VoucherGoodsBean.class));
                voucherAdapter.notifyDataSetChanged();
            } else {
                voucherTextView.setVisibility(View.GONE);
            }
            //SpecName
            temp = goodsInfoJSONObject.getString("spec_name");
            specNameArrayList = new ArrayList<>();
            if (!TextUtils.isEmpty(temp) && !temp.equals("false") && !temp.equals("null")) {
                jsonObject = new JSONObject(temp);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    HashMap<String, String> hashMap1 = new HashMap<>();
                    String key = iterator.next().toString();
                    String value = jsonObject.getString(key);
                    hashMap1.put("id", key);
                    hashMap1.put("value", value);
                    specNameArrayList.add(hashMap1);
                }
                for (int i = 0; i < specNameArrayList.size(); i++) {
                    if (i < 2) {
                        chooseLineView[i].setVisibility(View.VISIBLE);
                        chooseValueRecyclerView[i].setVisibility(View.VISIBLE);
                        chooseValueTextView[i].setVisibility(View.VISIBLE);
                        chooseValueTextView[i].setText(specNameArrayList.get(i).get("value"));
                    }
                }
            } else {
                specTextView[0].setText("默认");
                specTextView[0].setVisibility(View.VISIBLE);
            }
            //specValue
            temp = goodsInfoJSONObject.getString("spec_value");
            specValueArrayList = new ArrayList<>();
            if (!TextUtils.isEmpty(temp) && !temp.equals("false") && !temp.equals("null")) {
                jsonObject = new JSONObject(temp);
                if (specNameArrayList.size() != 0) {
                    for (int i = 0; i < specNameArrayList.size(); i++) {
                        String id = specNameArrayList.get(i).get("id");
                        String value = specNameArrayList.get(i).get("value");
                        JSONObject object = new JSONObject(jsonObject.getString(id));
                        Iterator iterator = object.keys();
                        while (iterator.hasNext()) {
                            HashMap<String, String> hashMap1 = new HashMap<>();
                            String key = iterator.next().toString();
                            hashMap1.put("value", object.getString(key));
                            hashMap1.put("parent_value", value);
                            hashMap1.put("parent_id", id);
                            hashMap1.put("id", key);
                            specValueArrayList.add(hashMap1);
                        }
                    }
                }
            }
            //goodsSpec
            temp = goodsInfoJSONObject.getString("goods_spec");
            goodsSpecArrayList = new ArrayList<>();
            if (!TextUtils.isEmpty(temp) && !temp.equals("false") && !temp.equals("null")) {
                jsonObject = new JSONObject(temp);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    HashMap<String, String> hashMap1 = new HashMap<>();
                    String key = iterator.next().toString();
                    String value = jsonObject.getString(key);
                    for (int i = 0; i < specValueArrayList.size(); i++) {
                        String id = specValueArrayList.get(i).get("id");
                        if (key.equals(id)) {
                            String parent_value = specValueArrayList.get(i).get("parent_value");
                            hashMap1.put("key", key);
                            hashMap1.put("value", value);
                            hashMap1.put("content", parent_value + "：" + value);
                        }
                    }
                    goodsSpecArrayList.add(hashMap1);
                }
                for (int i = 0; i < goodsSpecArrayList.size(); i++) {
                    if (i < 2) {
                        specTextView[i].setVisibility(View.VISIBLE);
                        specTextView[i].setText(goodsSpecArrayList.get(i).get("content"));
                        specString[i] = goodsSpecArrayList.get(i).get("key");
                    }
                }
            }
            //specList
            //noinspection unchecked
            ArrayList<HashMap<String, String>>[] specArrayList = new ArrayList[2];
            SpecListAdapter[] specAdapter = new SpecListAdapter[2];
            temp = mainJSONObject.getString("spec_list");
            specListArrayList = new ArrayList<>(JsonUtil.json2ArrayList(temp));
            for (int i = 0; i < specNameArrayList.size(); i++) {
                if (i < 2) {
                    specArrayList[i] = new ArrayList<>();
                    String value = specNameArrayList.get(i).get("value");
                    for (int j = 0; j < specValueArrayList.size(); j++) {
                        if (value.equals(specValueArrayList.get(j).get("parent_value"))) {
                            HashMap<String, String> hashMap = new HashMap<>(specValueArrayList.get(j));
                            hashMap.put("default", "0");
                            for (int k = 0; k < goodsSpecArrayList.size(); k++) {
                                if (goodsSpecArrayList.get(k).get("value").equals(hashMap.get("value"))) {
                                    hashMap.put("default", "1");
                                    break;
                                }
                            }
                            specArrayList[i].add(hashMap);
                        }
                    }
                }
            }
            isBackBoolean = true;
            if (specArrayList[0] != null) {
                for (int i = 0; i < specArrayList[0].size(); i++) {
                    String id = specArrayList[0].get(i).get("id");
                    if (id.equals(specString[0])) {
                        isBackBoolean = false;
                        break;
                    }
                }
            }
            for (int i = 0; i < specArrayList.length; i++) {
                if (i < 2) {
                    if (specArrayList[i] != null) {
                        final int positionInt = i;
                        specAdapter[i] = new SpecListAdapter(specArrayList[i]);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        chooseValueRecyclerView[i].setLayoutManager(linearLayoutManager);
                        chooseValueRecyclerView[i].setAdapter(specAdapter[i]);
                        specAdapter[i].setOnItemClickListener((position, id, value) -> {
                            if (isBackBoolean) {
                                if (positionInt == 1) {
                                    specString[positionInt - 1] = id;
                                } else {
                                    specString[positionInt + 1] = id;
                                }
                            } else {
                                specString[positionInt] = id;
                            }
                            refreshSpecData();
                        });
                    }
                }
            }
            //虚拟物品
            if (goodsInfoJSONObject.getString("is_virtual").equals("0")) {
                areaRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                areaRelativeLayout.setVisibility(View.GONE);
            }
            //店铺信息
            JSONObject storeInfoJSONObject = new JSONObject(mainJSONObject.getString("store_info"));
            storeId = storeInfoJSONObject.getString("store_id");
            memberId = storeInfoJSONObject.getString("member_id");
            temp = "由 “" + storeInfoJSONObject.getString("store_name") + "” 销售和发货，并享受售后服务";
            storeNameTextView.setText(storeInfoJSONObject.getString("store_name"));
            voucherStoreNameTextView.setText(storeNameTextView.getText().toString());
            storeOwnTextView.setText(storeInfoJSONObject.getString("is_own_shop").equals("1") ? "自营店" : "");
            jsonObject = new JSONObject(storeInfoJSONObject.getString("store_credit"));
            jsonObject = new JSONObject(jsonObject.getString("store_desccredit"));
            storeDescTextView.setText(jsonObject.getString("credit"));
            storeDescPercentTextView.setText(storeInfoJSONObject.getString("is_own_shop").equals("1") ? "平" : jsonObject.getString("percent_text"));
            jsonObject = new JSONObject(storeInfoJSONObject.getString("store_credit"));
            jsonObject = new JSONObject(jsonObject.getString("store_servicecredit"));
            storeServiceTextView.setText(jsonObject.getString("credit"));
            storeServicePercentTextView.setText(storeInfoJSONObject.getString("is_own_shop").equals("1") ? "平" : jsonObject.getString("percent_text"));
            jsonObject = new JSONObject(storeInfoJSONObject.getString("store_credit"));
            jsonObject = new JSONObject(jsonObject.getString("store_deliverycredit"));
            storeDeliveryTextView.setText(jsonObject.getString("credit"));
            storeDeliveryPercentTextView.setText(storeInfoJSONObject.getString("is_own_shop").equals("1") ? "平" : jsonObject.getString("percent_text"));
            serviceDescTextView.setText(temp);
            //服务信息
            if (goodsInfoJSONObject.has("contractlist") && goodsInfoJSONObject.getString("contractlist").contains("{")) {
                serviceSevDayTextView.setVisibility(View.VISIBLE);
                serviceQualityTextView.setVisibility(View.VISIBLE);
                serviceReissueTextView.setVisibility(View.VISIBLE);
                serviceLogisticsTextView.setVisibility(View.VISIBLE);
            } else {
                serviceSevDayTextView.setVisibility(View.GONE);
                serviceQualityTextView.setVisibility(View.GONE);
                serviceReissueTextView.setVisibility(View.GONE);
                serviceLogisticsTextView.setVisibility(View.GONE);
            }
            //评价信息
            jsonObject = new JSONObject(mainJSONObject.getString("goods_evaluate_info"));
            evaluateDescTextView.setText("好评率 ");
            evaluateDescTextView.append(jsonObject.getString("good_percent") + "%");
            evaluateNumberTextView.setText("(");
            evaluateNumberTextView.append(jsonObject.getString("all") + "人评价)");
            if (mainJSONObject.getString("goods_eval_list").equals("null")) {
                evaluateRecyclerView.setVisibility(View.GONE);
            } else {
                evaluateRecyclerView.setVisibility(View.VISIBLE);
                evaluateGoodsArrayList.clear();
                evaluateGoodsArrayList.addAll(JsonUtil.json2ArrayList(mainJSONObject.getString("goods_eval_list"), EvaluateGoodsBean.class));
                evaluateGoodsAdapter.notifyDataSetChanged();
            }
            //商品推荐
            commendArrayList.clear();
            commendArrayList.addAll(JsonUtil.json2ArrayList(mainJSONObject.getString("goods_commend_list"), GoodsCommendBean.class));
            commendAdapter.notifyDataSetChanged();
            //选择页面
            BaseImageLoader.get().display(goodsImageArrayList.get(0), chooseGoodsImageView);
            chooseNameTextView.setText(nameTextView.getText().toString());
            chooseMoneyTextView.setText(moneyTextView.getText().toString());
            chooseStorageTextView.setText("库存：");
            chooseStorageTextView.append(goodsStorageString);
            if (!TextUtils.isEmpty(lowerLimitString)) {
                chooseNumberEditText.setText(lowerLimitString);
            }
            chooseNumberEditText.setSelection(chooseNumberEditText.getText().length());
            haveGoods = !goodsStorageString.equals("0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void changeNumber() {

        if (TextUtils.isEmpty(chooseNumberEditText.getText().toString())) {
            BaseToast.get().show("数量不能为空！");
            chooseNumberEditText.setText("1");
            chooseNumberEditText.setSelection(1);
            return;
        }

        int number = Integer.parseInt(chooseNumberEditText.getText().toString());

        if (number <= 0) {
            BaseToast.get().show("最低要买 1 件");
            number = 1;
        }

        if (!TextUtils.isEmpty(upperLimitString)) {
            int upper = Integer.parseInt(upperLimitString);
            if (number > upper) {
                number = upper;
                BaseToast.get().show("每人最高限购：" + number + " 件");
            }
        }

        if (!TextUtils.isEmpty(lowerLimitString)) {
            int lower = Integer.parseInt(lowerLimitString);
            if (number < lower) {
                number = lower;
                BaseToast.get().show("最低要购买：" + number + " 件");
            }
        }

        int storage = Integer.parseInt(goodsStorageString);

        if (number > storage) {
            number = storage;
            BaseToast.get().show("库存不足！");
        }

        String temp = number + "";
        chooseNumberEditText.setText(temp);
        chooseNumberEditText.setSelection(temp.length());

    }

    private void favoritesAdd() {

        BaseToast.get().show("收藏中...");

        MemberFavoritesModel.get().favoritesAdd(goodsId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                isFavorites = true;
                favoritesImageView.setImageResource(R.drawable.ic_favorite_press);
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void favoritesDel() {

        BaseToast.get().show("取消收藏中...");

        MemberFavoritesModel.get().favoritesDel(goodsId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                isFavorites = false;
                favoritesImageView.setImageResource(R.drawable.ic_favorite_primary);
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void refreshSpecData() {

        for (int i = 0; i < specListArrayList.size(); i++) {
            String key = specListArrayList.get(i).get("key");
            if (key.contains(specString[0]) && key.contains(specString[1])) {
                goodsId = specListArrayList.get(i).get("value");
                break;
            }
        }

        BaseBusClient.get().post(new GoodsIdEvent(goodsId));

    }

    private void goneChooseLayout() {

        BaseBusClient.get().post(new GoodsShowEvent(false));

        if (nightTextView.getVisibility() == View.VISIBLE) {
            nightTextView.setVisibility(View.GONE);
            BaseAnimClient.get().goneAlpha(nightTextView);
        }

        if (chooseRelativeLayout.getVisibility() == View.VISIBLE) {
            chooseRelativeLayout.setVisibility(View.GONE);
            //BaseAnimClient.get().downTranslate(chooseRelativeLayout, chooseRelativeLayout.getHeight());
        }

    }

    private void showChooseLayout() {

        BaseBusClient.get().post(new GoodsShowEvent(true));

        if (nightTextView.getVisibility() == View.GONE) {
            nightTextView.setVisibility(View.VISIBLE);
            BaseAnimClient.get().showAlpha(nightTextView);
        }

        if (chooseRelativeLayout.getVisibility() == View.GONE) {
            chooseRelativeLayout.setVisibility(View.VISIBLE);
            //BaseAnimClient.get().upTranslate(chooseRelativeLayout, chooseRelativeLayout.getHeight());
        }

    }

    private void goneVoucherLayout() {

        BaseBusClient.get().post(new GoodsShowEvent(false));

        if (nightTextView.getVisibility() == View.VISIBLE) {
            nightTextView.setVisibility(View.GONE);
            BaseAnimClient.get().goneAlpha(nightTextView);
        }

        if (voucherLinearLayout.getVisibility() == View.VISIBLE) {
            voucherLinearLayout.setVisibility(View.GONE);
            //BaseAnimClient.get().downTranslate(voucherLinearLayout, voucherLinearLayout.getHeight());
        }

    }

    private void showVoucherLayout() {

        BaseBusClient.get().post(new GoodsShowEvent(true));

        if (nightTextView.getVisibility() == View.GONE) {
            nightTextView.setVisibility(View.VISIBLE);
            BaseAnimClient.get().showAlpha(nightTextView);
        }

        if (voucherLinearLayout.getVisibility() == View.GONE) {
            voucherLinearLayout.setVisibility(View.VISIBLE);
            //BaseAnimClient.get().upTranslate(voucherLinearLayout, voucherLinearLayout.getHeight());
        }

    }

    private void voucherFreeex(String tid) {

        MemberVoucherModel.get().voucherFreeex(tid, new BaseHttpListener() {
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
    public void onGoodsBeanEvent(GoodsBeanEvent event) {

        jsonString = event.getBaseBean().getDatas();

        initData();
        initEven();
        handlerData();

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGoodsAreaEvent(GoodsAreaEvent event) {

        areaAddressTextView.setText(event.getAreaInfo());

        GoodsModel.get().calc(goodsId, event.getAreaId(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    JSONObject jsonObject = new JSONObject(baseBean.getDatas());
                    areaHaveTextView.setText(jsonObject.getString("if_store_cn"));
                    areaChooseTextView.setText(jsonObject.getString("content"));
                    haveGoods = jsonObject.getBoolean("if_store");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGoodsGoneEvent(GoodsGoneEvent event) {

        if (event.isGone()) {
            goneChooseLayout();
            goneVoucherLayout();
        }

    }

}
