package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.StoreStreetBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class StoreStreetListAdapter extends RecyclerView.Adapter<StoreStreetListAdapter.ViewHolder> {

    private final ArrayList<StoreStreetBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public StoreStreetListAdapter(ArrayList<StoreStreetBean> arrayList) {
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
        final StoreStreetBean bean = arrayList.get(position);

        BaseImageLoader.get().displayRadius(bean.getStoreAvatar(), holder.mainImageView);
        holder.nameTextView.setText(bean.getStoreName());
        holder.gradeTextView.setText(bean.getGradeId());
        String temp = "好评率： " + bean.getStoreCreditPercent() + "%";
        holder.creditTextView.setText(temp);
        temp = "共" + bean.getGoodsCount() + "件商品,最近成交" + bean.getNumSalesJq() + "件";
        holder.goodsTextView.setText(temp);
        holder.creditDescRatingBar.setRating(Float.parseFloat(bean.getStoreCredit().getStoreDesccredit().getCredit()));
        holder.creditServerRatingBar.setRating(Float.parseFloat(bean.getStoreCredit().getStoreServicecredit().getCredit()));
        holder.creditDeliveryRatingBar.setRating(Float.parseFloat(bean.getStoreCredit().getStoreDeliverycredit().getCredit()));
        GoodsStoreStreetListAdapter goodsStoreStreetListAdapter = new GoodsStoreStreetListAdapter(bean.getSearchListGoods());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.goodsRecyclerView, goodsStoreStreetListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(BaseApplication.get(), 4);
        holder.goodsRecyclerView.setLayoutManager(gridLayoutManager);

        goodsStoreStreetListAdapter.setOnItemClickListener((position1, bean1) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickGoods(positionInt, position1, bean1);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_store_street, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, StoreStreetBean bean);

        void onClickGoods(int position, int itemPosition, StoreStreetBean.SearchListGoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.gradeTextView)
        private AppCompatTextView gradeTextView;
        @ViewInject(R.id.creditTextView)
        private AppCompatTextView creditTextView;
        @ViewInject(R.id.goodsTextView)
        private AppCompatTextView goodsTextView;
        @ViewInject(R.id.creditDescRatingBar)
        private AppCompatRatingBar creditDescRatingBar;
        @ViewInject(R.id.creditServerRatingBar)
        private AppCompatRatingBar creditServerRatingBar;
        @ViewInject(R.id.creditDeliveryRatingBar)
        private AppCompatRatingBar creditDeliveryRatingBar;
        @ViewInject(R.id.goodsRecyclerView)
        private RecyclerView goodsRecyclerView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
