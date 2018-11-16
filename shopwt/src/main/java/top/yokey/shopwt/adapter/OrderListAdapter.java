package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private final ArrayList<OrderBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public OrderListAdapter(ArrayList<OrderBean> arrayList) {
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
        final OrderBean bean = arrayList.get(position);
        final StoreOrderListAdapter storeOrderListAdapter;

        boolean showPay = false;
        for (int i = 0; i < bean.getOrderList().size(); i++) {
            if (bean.getOrderList().get(i).getOrderState().equals("10")) {
                showPay = true;
            }
        }

        if (bean.getPayAmount() == null) {
            holder.payTextView.setVisibility(View.GONE);
        } else {
            holder.payTextView.setText("订单支付：￥");
            holder.payTextView.setVisibility(View.VISIBLE);
            holder.payTextView.append(bean.getPayAmount());
        }

        storeOrderListAdapter = new StoreOrderListAdapter(bean.getOrderList());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, storeOrderListAdapter);
        holder.payTextView.setVisibility(showPay ? View.VISIBLE : View.GONE);

        storeOrderListAdapter.setOnItemClickListener(new StoreOrderListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, OrderBean.OrderListBean orderListBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(positionInt, position, orderListBean);
                }
            }

            @Override
            public void onOption(int position, OrderBean.OrderListBean orderListBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onOption(positionInt, position, orderListBean);
                }
            }

            @Override
            public void onOpera(int position, OrderBean.OrderListBean orderListBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onOpera(positionInt, position, orderListBean);
                }
            }

            @Override
            public void onClickGoods(int position, int itemPosition, OrderBean.OrderListBean.ExtendOrderGoodsBean extendOrderGoodsBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemGoodsClick(position, itemPosition, bean.getOrderList().get(position));
                }
            }
        });

        holder.payTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onPay(positionInt, bean);
            }
        });

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_order, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onPay(int position, OrderBean bean);

        void onClick(int position, OrderBean bean);

        void onItemClick(int position, int itemPosition, OrderBean.OrderListBean bean);

        void onItemGoodsClick(int position, int itemPosition, OrderBean.OrderListBean bean);

        void onOption(int position, int itemPosition, OrderBean.OrderListBean bean);

        void onOpera(int position, int itemPosition, OrderBean.OrderListBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.payTextView)
        private AppCompatTextView payTextView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
