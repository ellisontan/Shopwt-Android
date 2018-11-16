package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.StoreStreetBean;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseImageLoader;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

class GoodsStoreStreetListAdapter extends RecyclerView.Adapter<GoodsStoreStreetListAdapter.ViewHolder> {

    private final ArrayList<StoreStreetBean.SearchListGoodsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    GoodsStoreStreetListAdapter(ArrayList<StoreStreetBean.SearchListGoodsBean> arrayList) {
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
        final StoreStreetBean.SearchListGoodsBean bean = arrayList.get(position);

        BaseImageLoader.get().displayRadius(bean.getGoodsImage(), holder.mainImageView);
        holder.nameTextView.setText("￥");
        holder.nameTextView.append(bean.getGoodsPrice());

        int height = (BaseApplication.get().getWidth() - BaseApplication.get().dipToPx(56)) / 4;
        ViewGroup.LayoutParams layoutParams = holder.mainImageView.getLayoutParams();
        layoutParams.height = height;
        holder.mainImageView.setLayoutParams(layoutParams);

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_store_street, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, StoreStreetBean.SearchListGoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
