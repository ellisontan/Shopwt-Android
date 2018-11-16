package top.yokey.shopwt.activity.seller;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.AddressSellerListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.bean.AddressSellerBean;
import top.yokey.base.model.SellerAddressModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class AddressActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView toolbarImageView;
    private PullRefreshView mainPullRefreshView;

    private AddressSellerListAdapter mainAdapter;
    private ArrayList<AddressSellerBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "发货地址管理");
        toolbarImageView.setImageResource(R.drawable.ic_action_add);

        mainArrayList = new ArrayList<>();
        mainAdapter = new AddressSellerListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> BaseApplication.get().startCheckSellerLogin(getActivity(), AddressAddActivity.class));

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddress();
            }

            @Override
            public void onLoadMore() {
                getAddress();
            }
        });

        mainAdapter.setOnItemClickListener(new AddressSellerListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, AddressSellerBean bean) {
                Intent intent = new Intent(getActivity(), AddressEditActivity.class);
                intent.putExtra(BaseConstant.DATA_BEAN, bean);
                BaseApplication.get().startCheckSellerLogin(getActivity(), intent);
            }

            @Override
            public void onEdit(int position, AddressSellerBean bean) {
                Intent intent = new Intent(getActivity(), AddressEditActivity.class);
                intent.putExtra(BaseConstant.DATA_BEAN, bean);
                BaseApplication.get().startCheckSellerLogin(getActivity(), intent);
            }

            @Override
            public void onDelete(int position, AddressSellerBean bean) {
                deleteAddress(bean.getAddressId());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    //自定义方法

    private void getAddress() {

        mainPullRefreshView.setLoading();

        SellerAddressModel.get().addressList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "address_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, AddressSellerBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void deleteAddress(String addressId) {

        SellerAddressModel.get().addressDel(addressId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                getAddress();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
