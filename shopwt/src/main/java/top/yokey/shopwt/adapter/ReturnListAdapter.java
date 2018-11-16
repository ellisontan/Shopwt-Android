package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ReturnBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ReturnListAdapter extends RecyclerView.Adapter<ReturnListAdapter.ViewHolder> {

    private final ArrayList<ReturnBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ReturnListAdapter(ArrayList<ReturnBean> arrayList) {
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
        final ReturnBean bean = arrayList.get(position);

        holder.storeNameTextView.setText(bean.getStoreName());
        BaseImageLoader.get().display(bean.getGoodsImg360(), holder.goodsImageView);
        holder.goodsNameTextView.setText(bean.getGoodsName());
        holder.timeTextView.setText(bean.getAddTime());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getRefundAmount());
        holder.numberTextView.setText(bean.getGoodsNum());
        holder.numberTextView.append("件");
        holder.operaTextView.setText("退货详细");

        if (bean.getShipState().equals("1")) {
            holder.operaTextView.setText("退货发货");
        }

        if (bean.getAdminState().equals("无")) {
            holder.storeDescTextView.setText(bean.getSellerState());
        } else {
            holder.storeDescTextView.setText(bean.getAdminState());
        }

        holder.goodsRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickGoods(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_return, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ReturnBean bean);

        void onOpera(int position, ReturnBean bean);

        void onClickGoods(int position, ReturnBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.storeNameTextView)
        private AppCompatTextView storeNameTextView;
        @ViewInject(R.id.storeDescTextView)
        private AppCompatTextView storeDescTextView;
        @ViewInject(R.id.goodsRelativeLayout)
        private RelativeLayout goodsRelativeLayout;
        @ViewInject(R.id.goodsImageView)
        private AppCompatImageView goodsImageView;
        @ViewInject(R.id.goodsNameTextView)
        private AppCompatTextView goodsNameTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.numberTextView)
        private AppCompatTextView numberTextView;
        @ViewInject(R.id.operaTextView)
        private AppCompatTextView operaTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
