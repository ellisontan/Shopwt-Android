package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.GoodsFavoritesBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsFavoritesListAdapter extends RecyclerView.Adapter<GoodsFavoritesListAdapter.ViewHolder> {

    private final ArrayList<GoodsFavoritesBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public GoodsFavoritesListAdapter(ArrayList<GoodsFavoritesBean> arrayList) {
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
        final GoodsFavoritesBean bean = arrayList.get(position);

        int width = BaseApplication.get().getWidth() / 2 - 16;
        BaseImageLoader.get().displayRadius(bean.getGoodsImageUrl(), holder.mainImageView);
        @SuppressWarnings("SuspiciousNameCombination")
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(width, width);
        holder.mainImageView.setLayoutParams(layoutParams);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsPrice());

        holder.deleteImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDelete(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_favorites, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, GoodsFavoritesBean bean);

        void onDelete(int position, GoodsFavoritesBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.deleteImageView)
        private AppCompatImageView deleteImageView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
