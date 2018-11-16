package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.OrderInfoBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsOrderInfoListAdapter extends RecyclerView.Adapter<GoodsOrderInfoListAdapter.ViewHolder> {

    private final ArrayList<OrderInfoBean.GoodsListBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public GoodsOrderInfoListAdapter(ArrayList<OrderInfoBean.GoodsListBean> arrayList) {
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
        final OrderInfoBean.GoodsListBean bean = arrayList.get(position);

        BaseImageLoader.get().displayRadius(bean.getImageUrl(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.specTextView.setText(bean.getGoodsSpec());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsPrice());
        holder.numberTextView.setText(bean.getGoodsNum());
        holder.numberTextView.append(" 件");

        if (Integer.parseInt(bean.getRefund()) == 0) {
            holder.refundTextView.setVisibility(View.GONE);
            holder.returnTextView.setVisibility(View.GONE);
        } else {
            holder.refundTextView.setVisibility(View.VISIBLE);
            holder.returnTextView.setVisibility(View.VISIBLE);
        }

        holder.refundTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onRefund(positionInt, bean);
            }
        });

        holder.returnTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onReturn(positionInt, bean);
            }
        });

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_order_info, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, OrderInfoBean.GoodsListBean bean);

        void onRefund(int position, OrderInfoBean.GoodsListBean bean);

        void onReturn(int position, OrderInfoBean.GoodsListBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.specTextView)
        private AppCompatTextView specTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.numberTextView)
        private AppCompatTextView numberTextView;
        @ViewInject(R.id.refundTextView)
        private AppCompatTextView refundTextView;
        @ViewInject(R.id.returnTextView)
        private AppCompatTextView returnTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
