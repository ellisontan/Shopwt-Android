package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.OrderSellerBean;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsOrderSellerListAdapter extends RecyclerView.Adapter<GoodsOrderSellerListAdapter.ViewHolder> {

    private final ArrayList<OrderSellerBean.GoodsListBean> arrayList;

    public GoodsOrderSellerListAdapter(ArrayList<OrderSellerBean.GoodsListBean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final OrderSellerBean.GoodsListBean bean = arrayList.get(position);
        BaseImageLoader.get().displayRadius(bean.getImage240Url(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.specTextView.setText(bean.getGoodsSpec());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsPrice());
        holder.numberTextView.setText(bean.getGoodsNum());
        holder.numberTextView.append(" 件");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_order, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder {

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

        private ViewHolder(View view) {
            super(view);
        }

    }

}
