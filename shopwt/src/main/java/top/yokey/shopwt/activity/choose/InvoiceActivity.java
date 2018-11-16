package top.yokey.shopwt.activity.choose;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;

import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.mine.InvoiceAddActivity;
import top.yokey.shopwt.adapter.InvoiceListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.InvoiceBean;
import top.yokey.base.model.MemberInvoiceModel;
import top.yokey.base.util.JsonUtil;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.activity_recycler_view)
public class InvoiceActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView toolbarImageView;
    private PullRefreshView mainPullRefreshView;

    private long exitTimeLong;
    private InvoiceListAdapter mainAdapter;
    private ArrayList<InvoiceBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "发票信息");
        toolbarImageView.setImageResource(R.drawable.ic_action_add);

        exitTimeLong = 0L;
        mainArrayList = new ArrayList<>();
        mainAdapter = new InvoiceListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), InvoiceAddActivity.class));

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInvoice();
            }

            @Override
            public void onLoadMore() {
                getInvoice();
            }
        });

        mainAdapter.setOnItemClickListener(new InvoiceListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, InvoiceBean invoiceBean) {
                Intent intent = new Intent();
                intent.putExtra(BaseConstant.DATA_ID, invoiceBean.getInvId());
                intent.putExtra(BaseConstant.DATA_CONTENT, invoiceBean.getInvTitle() + " " + invoiceBean.getInvContent());
                BaseApplication.get().finishOk(getActivity(), intent);
            }

            @Override
            public void onDelete(int position, InvoiceBean invoiceBean) {
                delInvoice(invoiceBean.getInvId());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getInvoice();
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

    private void getInvoice() {

        mainPullRefreshView.setLoading();

        MemberInvoiceModel.get().invoiceList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "invoice_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, InvoiceBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void delInvoice(String invId) {

        MemberInvoiceModel.get().invoiceDel(invId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                getInvoice();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
