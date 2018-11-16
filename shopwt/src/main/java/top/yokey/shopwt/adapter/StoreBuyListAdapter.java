package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.StoreBuyBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class StoreBuyListAdapter extends RecyclerView.Adapter<StoreBuyListAdapter.ViewHolder> {

    private final ArrayList<StoreBuyBean> arrayList;

    public StoreBuyListAdapter(ArrayList<StoreBuyBean> arrayList) {

        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int positionInt = position;
        final StoreBuyBean bean = arrayList.get(position);
        final GoodsBuyListAdapter goodsBuyListAdapter;

        goodsBuyListAdapter = new GoodsBuyListAdapter(bean.getGoodsList());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, goodsBuyListAdapter);

        holder.storeNameTextView.setText(bean.getStoreName());
        holder.logisticsMoneyTextView.setText("运费：￥");
        holder.logisticsMoneyTextView.append(bean.getLogisticsMoney());
        holder.totalMoneyTextView.setText("￥");
        holder.totalMoneyTextView.append(bean.getTotalMoney());

        holder.voucherLineView.setVisibility(View.GONE);
        holder.voucherRelativeLayout.setVisibility(View.GONE);

        if (bean.getStoreVoucherInfo() != null) {
            holder.voucherLineView.setVisibility(View.VISIBLE);
            holder.voucherRelativeLayout.setVisibility(View.VISIBLE);
            holder.voucherMoneyTextView.setText("节省￥");
            holder.voucherMoneyTextView.append(bean.getStoreVoucherInfo().getVoucherPrice());
        }

        if (bean.getStoreMansongRuleList() == null) {
            holder.mansongLineView.setVisibility(View.GONE);
            holder.manSongLinearLayout.setVisibility(View.GONE);
        } else {
            holder.mansongLineView.setVisibility(View.VISIBLE);
            holder.manSongLinearLayout.setVisibility(View.VISIBLE);
            holder.manSongDescTextView.setText(bean.getStoreMansongRuleList().getDesc().getDesc());
            BaseImageLoader.get().display(bean.getStoreMansongRuleList().getDesc().getUrl(), holder.manSongGoodsImageView);
        }

        holder.messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayList.get(positionInt).setMessage(holder.messageEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_store_buy, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.storeNameTextView)
        private AppCompatTextView storeNameTextView;
        @ViewInject(R.id.mansongLineView)
        private View mansongLineView;
        @ViewInject(R.id.manSongLinearLayout)
        private LinearLayoutCompat manSongLinearLayout;
        @ViewInject(R.id.manSongDescTextView)
        private AppCompatTextView manSongDescTextView;
        @ViewInject(R.id.manSongGoodsImageView)
        private AppCompatImageView manSongGoodsImageView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;
        @ViewInject(R.id.voucherLineView)
        private View voucherLineView;
        @ViewInject(R.id.voucherRelativeLayout)
        private RelativeLayout voucherRelativeLayout;
        @ViewInject(R.id.voucherMoneyTextView)
        private AppCompatTextView voucherMoneyTextView;
        @ViewInject(R.id.logisticsMoneyTextView)
        private AppCompatTextView logisticsMoneyTextView;
        @ViewInject(R.id.totalMoneyTextView)
        private AppCompatTextView totalMoneyTextView;
        @ViewInject(R.id.messageEditText)
        private AppCompatEditText messageEditText;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
