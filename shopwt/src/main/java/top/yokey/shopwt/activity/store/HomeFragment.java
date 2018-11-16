package top.yokey.shopwt.activity.store;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.GoodsListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.UBLImageLoader;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsBean;
import top.yokey.base.bean.StoreInfoBean;
import top.yokey.base.event.StoreBeanEvent;
import top.yokey.base.model.MemberCartModel;
import top.yokey.base.model.MemberFavoritesStoreModel;
import top.yokey.base.model.StoreModel;
import top.yokey.base.util.JsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_store_home)
public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.backgroundImageView)
    private AppCompatImageView backgroundImageView;
    @ViewInject(R.id.avatarImageView)
    private AppCompatImageView avatarImageView;
    @ViewInject(R.id.nameTextView)
    private AppCompatTextView nameTextView;
    @ViewInject(R.id.favoritesTextView)
    private AppCompatTextView favoritesTextView;
    @ViewInject(R.id.numberTextView)
    private AppCompatTextView numberTextView;

    @ViewInject(R.id.mainBanner)
    private Banner mainBanner;
    @ViewInject(R.id.mainRecyclerView)
    private RecyclerView mainRecyclerView;
    @ViewInject(R.id.favoritesRankTextView)
    private AppCompatTextView favoritesRankTextView;
    @ViewInject(R.id.saleRankTextView)
    private AppCompatTextView saleRankTextView;
    @ViewInject(R.id.favoritesRankLinearLayout)
    private LinearLayoutCompat favoritesRankLinearLayout;
    @ViewInject(R.id.favoritesRankOneImageView)
    private AppCompatImageView favoritesRankOneImageView;
    @ViewInject(R.id.favoritesRankTwoImageView)
    private AppCompatImageView favoritesRankTwoImageView;
    @ViewInject(R.id.favoritesRankThrImageView)
    private AppCompatImageView favoritesRankThrImageView;
    private AppCompatImageView[] favoritesRankImageView;
    @ViewInject(R.id.favoritesRankOneTextView)
    private AppCompatTextView favoritesRankOneTextView;
    @ViewInject(R.id.favoritesRankTwoTextView)
    private AppCompatTextView favoritesRankTwoTextView;
    @ViewInject(R.id.favoritesRankThrTextView)
    private AppCompatTextView favoritesRankThrTextView;
    private AppCompatTextView[] favoritesRankNameTextView;
    @ViewInject(R.id.saleRankLinearLayout)
    private LinearLayoutCompat saleRankLinearLayout;
    @ViewInject(R.id.saleRankOneImageView)
    private AppCompatImageView saleRankOneImageView;
    @ViewInject(R.id.saleRankTwoImageView)
    private AppCompatImageView saleRankTwoImageView;
    @ViewInject(R.id.saleRankThrImageView)
    private AppCompatImageView saleRankThrImageView;
    private AppCompatImageView[] saleRankImageView;
    @ViewInject(R.id.saleRankOneTextView)
    private AppCompatTextView saleRankOneTextView;
    @ViewInject(R.id.saleRankTwoTextView)
    private AppCompatTextView saleRankTwoTextView;
    @ViewInject(R.id.saleRankThrTextView)
    private AppCompatTextView saleRankThrTextView;
    private AppCompatTextView[] saleRankNameTextView;

    private StoreInfoBean storeInfoBean;
    private GoodsListAdapter mainAdapter;
    private ArrayList<GoodsBean> mainArrayList;
    private ArrayList<GoodsBean> saleArrayList;
    private ArrayList<GoodsBean> favoritesArrayList;

    @Override
    public void initData() {

        storeInfoBean = new StoreInfoBean();
        mainBanner.setImageLoader(new UBLImageLoader());
        mainBanner.setDelayTime(BaseConstant.TIME_DELAY);
        mainBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        mainArrayList = new ArrayList<>();
        saleArrayList = new ArrayList<>();
        favoritesArrayList = new ArrayList<>();
        mainAdapter = new GoodsListAdapter(mainArrayList, true);
        BaseApplication.get().setRecyclerView(getActivity(), mainRecyclerView, mainAdapter);

        mainRecyclerView.setPadding(BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2), BaseApplication.get().dipToPx(2));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        //noinspection deprecation
        gridLayoutManager.setAutoMeasureEnabled(true);
        mainRecyclerView.setLayoutManager(gridLayoutManager);

        favoritesRankImageView = new AppCompatImageView[3];
        favoritesRankImageView[0] = favoritesRankOneImageView;
        favoritesRankImageView[1] = favoritesRankTwoImageView;
        favoritesRankImageView[2] = favoritesRankThrImageView;
        favoritesRankNameTextView = new AppCompatTextView[3];
        favoritesRankNameTextView[0] = favoritesRankOneTextView;
        favoritesRankNameTextView[1] = favoritesRankTwoTextView;
        favoritesRankNameTextView[2] = favoritesRankThrTextView;

        saleRankImageView = new AppCompatImageView[3];
        saleRankImageView[0] = saleRankOneImageView;
        saleRankImageView[1] = saleRankTwoImageView;
        saleRankImageView[2] = saleRankThrImageView;
        saleRankNameTextView = new AppCompatTextView[3];
        saleRankNameTextView[0] = saleRankOneTextView;
        saleRankNameTextView[1] = saleRankTwoTextView;
        saleRankNameTextView[2] = saleRankThrTextView;

    }

    @Override
    public void initEven() {

        favoritesTextView.setOnClickListener(view -> {
            if (storeInfoBean.isIsFavorate()) {
                favoritesDel();
            } else {
                favoritesAdd();
            }
        });

        favoritesRankTextView.setOnClickListener(view -> {
            favoritesRankTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
            favoritesRankLinearLayout.setVisibility(View.VISIBLE);
            saleRankTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            saleRankLinearLayout.setVisibility(View.GONE);
        });

        saleRankTextView.setOnClickListener(view -> {
            favoritesRankTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            favoritesRankLinearLayout.setVisibility(View.GONE);
            saleRankTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
            saleRankLinearLayout.setVisibility(View.VISIBLE);
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

    private void favoritesAdd() {

        MemberFavoritesStoreModel.get().favoritesAdd(storeInfoBean.getStoreId(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    favoritesTextView.setText("已收藏");
                    favoritesTextView.setBackgroundResource(R.color.blackSub);
                    storeInfoBean.setStoreCollect(storeInfoBean.getStoreCollect() + 1);
                    String temp = "粉丝：" + storeInfoBean.getStoreCollect();
                    storeInfoBean.setIsFavorate(true);
                    numberTextView.setText(temp);
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

    private void favoritesDel() {

        MemberFavoritesStoreModel.get().favoritesDel(storeInfoBean.getStoreId(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    favoritesTextView.setText("收藏");
                    favoritesTextView.setBackgroundResource(R.color.primary);
                    storeInfoBean.setStoreCollect((Integer.parseInt(storeInfoBean.getStoreCollect()) - 1) + "");
                    String temp = "粉丝：" + storeInfoBean.getStoreCollect();
                    storeInfoBean.setIsFavorate(false);
                    numberTextView.setText(temp);
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

    private void getGoodsSale() {

        StoreModel.get().storeGoodsRank(storeInfoBean.getStoreId(), "salenumdesc", "3", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String temp;
                saleArrayList.clear();
                GoodsBean goodsStoreBean;
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_list");
                saleArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsBean.class));
                for (int i = 0; i < saleArrayList.size(); i++) {
                    goodsStoreBean = saleArrayList.get(i);
                    final String goodsId = goodsStoreBean.getGoodsId();
                    BaseImageLoader.get().display(goodsStoreBean.getGoodsImageUrl(), saleRankImageView[i]);
                    temp = "已售：" + goodsStoreBean.getGoodsSalenum() + "   ￥" + goodsStoreBean.getGoodsPrice();
                    saleRankNameTextView[i].setText(temp);
                    saleRankImageView[i].setOnClickListener(view -> BaseApplication.get().startGoods(getActivity(), goodsId));
                }
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getGoodsSale();
                    }
                }.start();
            }
        });

    }

    private void getGoodsFavorites() {

        StoreModel.get().storeGoodsRank(storeInfoBean.getStoreId(), "collectdesc", "3", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String temp;
                favoritesArrayList.clear();
                GoodsBean goodsStoreBean;
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "goods_list");
                favoritesArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsBean.class));
                for (int i = 0; i < favoritesArrayList.size(); i++) {
                    goodsStoreBean = favoritesArrayList.get(i);
                    final String goodsId = goodsStoreBean.getGoodsId();
                    BaseImageLoader.get().display(goodsStoreBean.getGoodsImageUrl(), favoritesRankImageView[i]);
                    temp = "已售：" + goodsStoreBean.getGoodsSalenum() + "   ￥" + goodsStoreBean.getGoodsPrice();
                    favoritesRankNameTextView[i].setText(temp);
                    favoritesRankImageView[i].setOnClickListener(view -> BaseApplication.get().startGoods(getActivity(), goodsId));
                }
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getGoodsFavorites();
                    }
                }.start();
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
        BaseImageLoader.get().display(storeInfoBean.getStoreAvatar(), avatarImageView);
        BaseImageLoader.get().display(storeInfoBean.getMbTitleImg(), backgroundImageView);
        nameTextView.setText(storeInfoBean.getStoreName());
        String temp = "粉丝：" + storeInfoBean.getStoreCollect();
        numberTextView.setText(temp);
        if (storeInfoBean.isIsFavorate()) {
            favoritesTextView.setText("已收藏");
            favoritesTextView.setBackgroundResource(R.color.blackSub);
        } else {
            favoritesTextView.setText("收藏");
            favoritesTextView.setBackgroundResource(R.color.primary);
        }
        //广告轮播
        if (storeInfoBean.getMbSliders().size() == 0) {
            mainBanner.setVisibility(View.GONE);
        } else {
            final List<String> type = new ArrayList<>();
            final List<String> link = new ArrayList<>();
            List<String> image = new ArrayList<>();
            for (int i = 0; i < storeInfoBean.getMbSliders().size(); i++) {
                type.add(storeInfoBean.getMbSliders().get(i).getType());
                link.add(storeInfoBean.getMbSliders().get(i).getLink());
                image.add(storeInfoBean.getMbSliders().get(i).getImgUrl());
            }
            mainBanner.setOnBannerListener(position -> BaseApplication.get().startTypeValue(getActivity(), type.get(position), link.get(position)));
            mainBanner.update(image);
            mainBanner.start();
        }
        //推荐商品
        data = JsonUtil.getDatasString(event.getBaseBean().getDatas(), "rec_goods_list");
        mainArrayList.addAll(JsonUtil.json2ArrayList(data, GoodsBean.class));
        mainAdapter.notifyDataSetChanged();
        getGoodsFavorites();
        getGoodsSale();

    }

}
