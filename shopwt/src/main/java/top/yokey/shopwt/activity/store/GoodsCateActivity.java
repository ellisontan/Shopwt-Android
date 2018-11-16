package top.yokey.shopwt.activity.store;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.ClassStoreListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.ClassStoreBean;
import top.yokey.base.model.StoreModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsCateActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private PullRefreshView mainPullRefreshView;

    private String storeIdString;
    private ClassStoreListAdapter mainAdapter;
    private ArrayList<ClassStoreBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        storeIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(storeIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        mainArrayList = new ArrayList<>();
        mainAdapter = new ClassStoreListAdapter(mainArrayList);
        mainPullRefreshView.clearItemDecoration();
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        setToolbar(mainToolbar, "店铺分类");
        getGoodsClass();

    }

    @Override
    public void initEven() {

        mainAdapter.setOnItemClickListener(new ClassStoreListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, ClassStoreBean classStoreBean) {
                Intent intent = new Intent();
                intent.putExtra(BaseConstant.DATA_STCID, classStoreBean.getId());
                BaseApplication.get().finishOk(getActivity(), intent);
            }

            @Override
            public void onChildClick(int position, ClassStoreBean classStoreBean) {
                Intent intent = new Intent();
                intent.putExtra(BaseConstant.DATA_STCID, classStoreBean.getId());
                BaseApplication.get().finishOk(getActivity(), intent);
            }
        });

    }

    //自定义方法

    private void getGoodsClass() {

        StoreModel.get().storeGoodsClass(storeIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                ArrayList<ClassStoreBean> level2ArrayList = new ArrayList<>();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "store_goods_class");
                //noinspection ConstantConditions
                ArrayList<ClassStoreBean> arrayList = new ArrayList<>(JsonUtil.json2ArrayList(data, ClassStoreBean.class));
                //遍历第一级
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getLevel().equals("1")) {
                        mainArrayList.add(arrayList.get(i));
                    }
                    if (arrayList.get(i).getLevel().equals("2")) {
                        level2ArrayList.add(arrayList.get(i));
                    }
                }
                //遍历第二级
                for (int i = 0; i < mainArrayList.size(); i++) {
                    for (int j = 0; j < level2ArrayList.size(); j++) {
                        if (level2ArrayList.get(j).getPid().equals(mainArrayList.get(i).getId())) {
                            arrayList.clear();
                            arrayList.addAll(mainArrayList.get(i).getChild());
                            arrayList.add(level2ArrayList.get(j));
                            mainArrayList.get(i).setChild(arrayList);
                        }
                    }
                }
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getGoodsClass();
                    }
                }.start();
            }
        });

    }

}
