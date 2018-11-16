package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.OrderSellerBean;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class OrderSellerListAdapter extends RecyclerView.Adapter<OrderSellerListAdapter.ViewHolder> {

    private final ArrayList<OrderSellerBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public OrderSellerListAdapter(ArrayList<OrderSellerBean> arrayList) {
        this.arrayList = arrayList;
        this.onItemClickListener = null;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int positionInt = position;
        final OrderSellerBean bean = arrayList.get(position);
        final GoodsOrderSellerListAdapter goodsOrderSellerListAdapter;

        goodsOrderSellerListAdapter = new GoodsOrderSellerListAdapter(bean.getGoodsList());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, goodsOrderSellerListAdapter);

        if (bean.getZengpinList().size() == 0) {
            holder.zenPinView.setVisibility(View.GONE);
            holder.zengPinLinearLayout.setVisibility(View.GONE);
        } else {
            holder.zenPinView.setVisibility(View.VISIBLE);
            holder.zengPinLinearLayout.setVisibility(View.VISIBLE);
            holder.zengPinDescTextView.setText(bean.getZengpinList().get(0).getGoodsName());
            BaseImageLoader.get().display(bean.getZengpinList().get(0).getImage240Url(), holder.zengPinGoodsImageView);
        }

        holder.snTextView.setText("单号：");
        holder.snTextView.append(bean.getOrderSn());
        holder.timeTextView.setText(bean.getBuyerName());

        String temp = "共 <font color='#FF0000'>" + bean.getGoodsCount() + " </font>件商品，合计<font color='#FF0000'>￥" + bean.getOrderAmount() + "</font>（含运费：<font color='#FF0000'>￥" + bean.getShippingFee() + "</font>）";
        holder.totalTextView.setText(Html.fromHtml(temp));

        holder.optionTextView.setVisibility(View.VISIBLE);
        holder.operaTextView.setVisibility(View.VISIBLE);

        switch (bean.getOrderState()) {
            case "0":
                holder.optionTextView.setVisibility(View.GONE);
                holder.operaTextView.setText("已取消");
                break;
            case "10":
                holder.optionTextView.setText("取消订单");
                holder.operaTextView.setText("修改价格");
                break;
            case "20":
                holder.optionTextView.setText("待发货");
                holder.operaTextView.setText("去发货");
                break;
            case "30":
                holder.optionTextView.setVisibility(View.GONE);
                holder.operaTextView.setText("待收货");
                break;
            case "40":
                holder.optionTextView.setVisibility(View.GONE);
                holder.operaTextView.setText("已完成");
                break;
        }

        holder.optionTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onOption(positionInt, bean);
            }
        });

        holder.operaTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onOpera(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_order_seller, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onOption(int position, OrderSellerBean bean);

        void onOpera(int position, OrderSellerBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.snTextView)
        private AppCompatTextView snTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;
        @ViewInject(R.id.zengPinView)
        private View zenPinView;
        @ViewInject(R.id.zengPinLinearLayout)
        private LinearLayoutCompat zengPinLinearLayout;
        @ViewInject(R.id.zengPinDescTextView)
        private AppCompatTextView zengPinDescTextView;
        @ViewInject(R.id.zengPinGoodsImageView)
        private AppCompatImageView zengPinGoodsImageView;
        @ViewInject(R.id.totalTextView)
        private AppCompatTextView totalTextView;
        @ViewInject(R.id.optionTextView)
        private AppCompatTextView optionTextView;
        @ViewInject(R.id.operaTextView)
        private AppCompatTextView operaTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
