package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
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
import top.yokey.base.bean.CartBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

class GoodsCartListAdapter extends RecyclerView.Adapter<GoodsCartListAdapter.ViewHolder> {

    private final ArrayList<CartBean.GoodsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    GoodsCartListAdapter(ArrayList<CartBean.GoodsBean> arrayList) {
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
        final CartBean.GoodsBean bean = arrayList.get(position);

        holder.mainCheckBox.setChecked(bean.isCheck());
        BaseImageLoader.get().displayRadius(bean.getGoodsImageUrl(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsPrice());
        holder.numberEditText.setText(bean.getGoodsNum());

        holder.mobileTextView.setVisibility(View.GONE);
        holder.activityTextView.setVisibility(View.GONE);

        if (bean.isIfsole()) {
            holder.mobileTextView.setVisibility(View.VISIBLE);
        }

        if (bean.isIfxianshi()) {
            holder.activityTextView.setText("限时折扣");
            holder.activityTextView.setVisibility(View.VISIBLE);
        }

        if (bean.isIfgroupbuy()) {
            holder.activityTextView.setText("抢购");
            holder.activityTextView.setVisibility(View.VISIBLE);
        }

        holder.mainCheckBox.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onCheck(positionInt, holder.mainCheckBox.isChecked(), bean);
            }
        });

        holder.deleteImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDelete(positionInt, bean);
            }
        });

        holder.subTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onSub(positionInt, bean);
            }
        });

        holder.addTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onAdd(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_cart, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, CartBean.GoodsBean bean);

        void onDelete(int position, CartBean.GoodsBean bean);

        void onAdd(int position, CartBean.GoodsBean bean);

        void onSub(int position, CartBean.GoodsBean bean);

        void onCheck(int position, boolean isCheck, CartBean.GoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainCheckBox)
        private AppCompatCheckBox mainCheckBox;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.mobileTextView)
        private AppCompatTextView mobileTextView;
        @ViewInject(R.id.activityTextView)
        private AppCompatTextView activityTextView;
        @ViewInject(R.id.deleteImageView)
        private AppCompatImageView deleteImageView;
        @ViewInject(R.id.addTextView)
        private AppCompatTextView addTextView;
        @ViewInject(R.id.numberEditText)
        private AppCompatEditText numberEditText;
        @ViewInject(R.id.subTextView)
        private AppCompatTextView subTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
