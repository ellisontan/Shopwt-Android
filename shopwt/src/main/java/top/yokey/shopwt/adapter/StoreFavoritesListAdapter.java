package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.StoreFavoritesBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class StoreFavoritesListAdapter extends RecyclerView.Adapter<StoreFavoritesListAdapter.ViewHolder> {

    private final ArrayList<StoreFavoritesBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public StoreFavoritesListAdapter(ArrayList<StoreFavoritesBean> arrayList) {
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
        final StoreFavoritesBean bean = arrayList.get(position);

        BaseImageLoader.get().displayRadius(bean.getStoreAvatarUrl(), holder.mainImageView);
        holder.nameTextView.setText(bean.getStoreName());
        String temp = "粉丝： " + "<font color='#FF0000'>" + bean.getStoreCollect() + "</font> 人";
        holder.favoritesTextView.setText(Html.fromHtml(temp));
        temp = "商品： " + "<font color='#FF0000'>" + bean.getGoodsCount() + "</font> 件";
        holder.goodsTextView.setText(Html.fromHtml(temp));

        holder.deleteImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDelete(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_store_favorites, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {

        void onClick(int position, StoreFavoritesBean bean);

        void onDelete(int position, StoreFavoritesBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.favoritesTextView)
        private AppCompatTextView favoritesTextView;
        @ViewInject(R.id.goodsTextView)
        private AppCompatTextView goodsTextView;
        @ViewInject(R.id.deleteImageView)
        private AppCompatImageView deleteImageView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
