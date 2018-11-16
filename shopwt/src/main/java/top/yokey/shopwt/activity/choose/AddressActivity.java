package top.yokey.shopwt.activity.choose;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.mine.AddressAddActivity;
import top.yokey.shopwt.activity.mine.AddressEditActivity;
import top.yokey.shopwt.adapter.AddressListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.AddressBean;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.MemberAddressModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class AddressActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView toolbarImageView;
    private PullRefreshView mainPullRefreshView;

    private long exitTimeLong;
    private AddressListAdapter mainAdapter;
    private ArrayList<AddressBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "选择地址");
        toolbarImageView.setImageResource(R.drawable.ic_action_add);

        exitTimeLong = 0L;
        mainArrayList = new ArrayList<>();
        mainAdapter = new AddressListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), AddressAddActivity.class));

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

        mainAdapter.setOnItemClickListener(new AddressListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, AddressBean addressBean) {
                Intent intent = new Intent();
                intent.putExtra(BaseConstant.DATA_ID, addressBean.getAddressId());
                BaseApplication.get().finishOk(getActivity(), intent);
            }

            @Override
            public void onEdit(int position, AddressBean addressBean) {
                Intent intent = new Intent(getActivity(), AddressEditActivity.class);
                intent.putExtra(BaseConstant.DATA_BEAN, addressBean);
                BaseApplication.get().start(getActivity(), intent);
            }

            @Override
            public void onDelete(int position, AddressBean addressBean) {
                delAddress(addressBean.getAddressId());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getAddress();
    }

    @Override
    public void onReturn() {

        if (System.currentTimeMillis() - exitTimeLong > BaseConstant.TIME_EXIT) {
            BaseToast.get().showReturnOneMoreTime();
            exitTimeLong = System.currentTimeMillis();
        } else {
            super.onReturn();
        }

    }

    //自定义方法

    private void getAddress() {

        mainPullRefreshView.setLoading();

        MemberAddressModel.get().addressList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "address_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, AddressBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void delAddress(String addressId) {

        MemberAddressModel.get().addressDel(addressId, new BaseHttpListener() {
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
