package top.yokey.shopwt.activity.main;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import io.github.xudaojie.qrcodelib.CaptureActivity;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.home.ChatListActivity;
import top.yokey.shopwt.adapter.BrandRecommendListAdapter;
import top.yokey.shopwt.adapter.ClassChildListAdapter;
import top.yokey.shopwt.adapter.ClassListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseBusClient;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.BrandRecommendBean;
import top.yokey.base.bean.ClassBean;
import top.yokey.base.bean.ClassChildBean;
import top.yokey.base.event.MainPositionEvent;
import top.yokey.base.model.BrandModel;
import top.yokey.base.model.ClassModel;
import top.yokey.base.util.JsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_main_cate)
public class CateFragment extends BaseFragment {

    @ViewInject(R.id.searchEditText)
    private AppCompatEditText searchEditText;
    @ViewInject(R.id.messageImageView)
    private AppCompatImageView messageImageView;
    @ViewInject(R.id.scanImageView)
    private AppCompatImageView scanImageView;

    @ViewInject(R.id.classPullRefreshView)
    private PullRefreshView classPullRefreshView;
    @ViewInject(R.id.brandRecommendPullRefreshView)
    private PullRefreshView brandRecommendPullRefreshView;
    @ViewInject(R.id.classChildPullRefreshView)
    private PullRefreshView classChildPullRefreshView;

    private String gcIdString;

    private ClassListAdapter classAdapter;
    private ArrayList<ClassBean> classArrayList;
    private ClassChildListAdapter classChildAdapter;
    private ArrayList<ClassChildBean> classChildArrayList;
    private BrandRecommendListAdapter brandRecommendAdapter;
    private ArrayList<BrandRecommendBean> brandRecommendArrayList;

    @Override
    public void initData() {

        gcIdString = "";

        classArrayList = new ArrayList<>();
        classAdapter = new ClassListAdapter(classArrayList);
        classPullRefreshView.getRecyclerView().setAdapter(classAdapter);
        classPullRefreshView.setCanLoadMore(false);
        classPullRefreshView.setCanRefresh(false);

        brandRecommendArrayList = new ArrayList<>();
        brandRecommendAdapter = new BrandRecommendListAdapter(brandRecommendArrayList);
        brandRecommendPullRefreshView.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 3));
        brandRecommendPullRefreshView.getRecyclerView().setAdapter(brandRecommendAdapter);
        brandRecommendPullRefreshView.clearItemDecoration();
        brandRecommendPullRefreshView.setCanLoadMore(false);
        brandRecommendPullRefreshView.setCanRefresh(false);

        classChildArrayList = new ArrayList<>();
        classChildAdapter = new ClassChildListAdapter(classChildArrayList);
        classChildPullRefreshView.getRecyclerView().setAdapter(classChildAdapter);
        classChildPullRefreshView.setCanLoadMore(false);
        classChildPullRefreshView.setCanRefresh(false);
        getBrandRecommend();
        getCate();

    }

    @Override
    public void initEven() {

        scanImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), CaptureActivity.class, BaseConstant.CODE_QRCODE));

        searchEditText.setOnClickListener(view -> BaseBusClient.get().post(new MainPositionEvent(2)));

        messageImageView.setOnClickListener(view -> BaseApplication.get().startCheckLogin(getActivity(), ChatListActivity.class));

        classPullRefreshView.setOnClickListener(view -> {
            if (classPullRefreshView.isFailure()) {
                getCate();
            }
        });

        classAdapter.setOnItemClickListener((position, classBean) -> {
            brandRecommendPullRefreshView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            classChildPullRefreshView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            for (int i = 0; i < classArrayList.size(); i++) {
                classArrayList.get(i).setClick(false);
                classAdapter.notifyItemChanged(i);
            }
            classArrayList.get(position).setClick(true);
            classAdapter.notifyItemChanged(position);
            gcIdString = classBean.getGcId();
            getChildAll();
        });

        brandRecommendPullRefreshView.setOnClickListener(view -> {
            if (brandRecommendPullRefreshView.isFailure()) {
                getBrandRecommend();
            }
        });

        brandRecommendAdapter.setOnItemClickListener((position, brandRecommendBean) -> BaseApplication.get().startGoodsList(getActivity(), "", brandRecommendBean.getBrandId(), ""));

        classChildPullRefreshView.setOnClickListener(view -> {
            if (classChildPullRefreshView.isFailure()) {
                getChildAll();
            }
        });

        classChildAdapter.setOnItemClickListener(new ClassChildListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, ClassChildBean classChildBean) {
                BaseApplication.get().startGoodsList(getActivity(), "", "", classChildBean.getGcId());
            }

            @Override
            public void onItemClick(int position, ClassChildBean classChildBean, ClassChildBean.ChildBean childBean) {
                BaseApplication.get().startGoodsList(getActivity(), "", "", childBean.getGcId());
            }
        });

    }

    //自定义方法

    private void getCate() {

        classPullRefreshView.setLoading();

        ClassModel.get().index(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                classArrayList.clear();
                //品牌推荐
                ClassBean classBean = new ClassBean();
                classBean.setGcName("品牌推荐");
                classBean.setClick(true);
                classArrayList.add(classBean);
                //商品分类
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "class_list");
                classArrayList.addAll(JsonUtil.json2ArrayList(data, ClassBean.class));
                classPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                classPullRefreshView.setFailure();
            }
        });

    }

    private void getBrandRecommend() {

        brandRecommendPullRefreshView.setLoading();

        BrandModel.get().recommendList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                brandRecommendArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "brand_list");
                brandRecommendArrayList.addAll(JsonUtil.json2ArrayList(data, BrandRecommendBean.class));
                brandRecommendPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                brandRecommendPullRefreshView.setFailure();
            }
        });

    }

    private void getChildAll() {

        classChildPullRefreshView.setLoading();

        ClassModel.get().getChildAll(gcIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                classChildArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "class_list");
                classChildArrayList.addAll(JsonUtil.json2ArrayList(data, ClassChildBean.class));
                classChildPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                classChildPullRefreshView.setFailure();
            }
        });

    }

}
