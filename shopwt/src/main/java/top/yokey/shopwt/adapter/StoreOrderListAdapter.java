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

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.OrderBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

class StoreOrderListAdapter extends RecyclerView.Adapter<StoreOrderListAdapter.ViewHolder> {

    private final ArrayList<OrderBean.OrderListBean> arrayList;
    private OnItemClickListener onItemClickListener;

    StoreOrderListAdapter(ArrayList<OrderBean.OrderListBean> arrayList) {
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
        final OrderBean.OrderListBean bean = arrayList.get(position);
        final GoodsOrderListAdapter goodsOrderListAdapter;

        goodsOrderListAdapter = new GoodsOrderListAdapter(bean.getExtendOrderGoods());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, goodsOrderListAdapter);

        if (bean.getZengpinList().size() == 0) {
            holder.zenPinView.setVisibility(View.GONE);
            holder.zengPinLinearLayout.setVisibility(View.GONE);
        } else {
            holder.zenPinView.setVisibility(View.VISIBLE);
            holder.zengPinLinearLayout.setVisibility(View.VISIBLE);
            holder.zengPinDescTextView.setText(bean.getZengpinList().get(0).getGoodsName());
            BaseImageLoader.get().display(bean.getZengpinList().get(0).getGoodsImageUrl(), holder.zengPinGoodsImageView);
        }

        holder.storeNameTextView.setText(bean.getStoreName());
        holder.storeDescTextView.setText(bean.getStateDesc());

        int count = 0;
        for (int i = 0; i < bean.getExtendOrderGoods().size(); i++) {
            count += Integer.parseInt(bean.getExtendOrderGoods().get(i).getGoodsNum());
        }
        String temp = "共 <font color='#FF0000'>" + count + " </font>件商品，合计<font color='#FF0000'>￥" + bean.getOrderAmount() + "</font>（含运费：<font color='#FF0000'>￥" + bean.getShippingFee() + "</font>";
        if (bean.getPointsNumber() != null) {
            temp = temp + " | <font color='#FF0000'>" + bean.getPointsNumber() + "</font>积分抵扣<font color='#FF0000'>￥" + bean.getPointsMoney() + "</font>）";
        } else {
            temp = temp + "）";
        }
        holder.totalTextView.setText(Html.fromHtml(temp));

        holder.optionTextView.setVisibility(View.VISIBLE);
        holder.operaTextView.setVisibility(View.VISIBLE);

        switch (bean.getOrderState()) {
            case "0":
                holder.optionTextView.setText("订单详细");
                holder.operaTextView.setText("删除订单");
                break;
            case "10":
                holder.optionTextView.setText("订单详细");
                holder.operaTextView.setText("取消订单");
                break;
            case "20":
                if (bean.getLockState().equals("0")) {
                    holder.optionTextView.setText("订单详细");
                    holder.operaTextView.setText("申请退款");
                } else {
                    holder.storeDescTextView.append("（退货/款中）");
                    holder.optionTextView.setVisibility(View.GONE);
                    holder.operaTextView.setText("订单详细");
                }
                break;
            case "30":
                if (bean.getLockState().equals("0")) {
                    holder.optionTextView.setText("查看物流");
                    holder.operaTextView.setText("确认收货");
                } else {
                    holder.storeDescTextView.append("（退货/款中）");
                    holder.optionTextView.setText("查看物流");
                    holder.operaTextView.setText("订单详细");
                }
                break;
            case "40":
                if (bean.getEvaluationState().equals("1")) {
                    if (bean.getEvaluationAgainState().equals("1")) {
                        holder.optionTextView.setText("删除订单");
                        holder.operaTextView.setText("订单详细");
                    } else {
                        holder.optionTextView.setText("删除订单");
                        holder.operaTextView.setText("追加评价");
                    }
                } else {
                    holder.optionTextView.setText("订单详细");
                    holder.operaTextView.setText("订单评价");
                }
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

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

        goodsOrderListAdapter.setOnItemClickListener((position1, extendOrderGoodsBean) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickGoods(positionInt, position1, extendOrderGoodsBean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_store_order, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, OrderBean.OrderListBean bean);

        void onOption(int position, OrderBean.OrderListBean bean);

        void onOpera(int position, OrderBean.OrderListBean bean);

        void onClickGoods(int position, int itemPosition, OrderBean.OrderListBean.ExtendOrderGoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.storeNameTextView)
        private AppCompatTextView storeNameTextView;
        @ViewInject(R.id.storeDescTextView)
        private AppCompatTextView storeDescTextView;
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
