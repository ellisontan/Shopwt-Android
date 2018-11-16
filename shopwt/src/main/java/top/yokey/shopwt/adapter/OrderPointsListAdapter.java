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

import top.yokey.base.bean.OrderPointsBean;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class OrderPointsListAdapter extends RecyclerView.Adapter<OrderPointsListAdapter.ViewHolder> {

    private final ArrayList<OrderPointsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public OrderPointsListAdapter(ArrayList<OrderPointsBean> arrayList) {
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
        final OrderPointsBean bean = arrayList.get(position);
        final GoodsOrderListAdapter goodsOrderListAdapter;

        holder.storeNameTextView.setText("兑换单号：");
        holder.storeNameTextView.append(bean.getPointOrdersn());
        holder.storeDescTextView.setText(bean.getStateDesc());

        BaseImageLoader.get().display(bean.getProdlist().get(0).getPointGoodsimage(), holder.mainImageView);
        holder.nameTextView.setText(bean.getProdlist().get(0).getPointGoodsname());
        holder.pointsTextView.setText("积分兑换：");
        holder.pointsTextView.append(bean.getProdlist().get(0).getPointGoodspoints());
        holder.numberTextView.setText(bean.getProdlist().get(0).getPointGoodsnum());
        holder.numberTextView.append(" 件");

        String temp = "共 <font color='#FF0000'>" + bean.getProdlist().size() + " </font>件商品，合计<font color='#FF0000'>" + bean.getPointAllpoint() + "积分</font>";
        holder.totalTextView.setText(Html.fromHtml(temp));

        holder.optionTextView.setVisibility(View.VISIBLE);
        holder.operaTextView.setVisibility(View.VISIBLE);
        holder.operaTextView.setText("查看详细");

        switch (bean.getPointOrderstate()) {
            case "2":
                holder.optionTextView.setVisibility(View.GONE);
                holder.optionTextView.setText("");
                break;
            case "20":
                holder.optionTextView.setText("取消兑换");
                break;
            case "30":
                holder.optionTextView.setText("确认收货");
                break;
            case "40":
                holder.optionTextView.setVisibility(View.GONE);
                holder.optionTextView.setText("");
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

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_order_points, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, OrderPointsBean bean);

        void onOption(int position, OrderPointsBean bean);

        void onOpera(int position, OrderPointsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.storeNameTextView)
        private AppCompatTextView storeNameTextView;
        @ViewInject(R.id.storeDescTextView)
        private AppCompatTextView storeDescTextView;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.pointsTextView)
        private AppCompatTextView pointsTextView;
        @ViewInject(R.id.numberTextView)
        private AppCompatTextView numberTextView;
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
