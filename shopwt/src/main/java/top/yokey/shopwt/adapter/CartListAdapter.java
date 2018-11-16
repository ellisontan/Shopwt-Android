package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
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

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private final ArrayList<CartBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public CartListAdapter(ArrayList<CartBean> arrayList) {
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
        final CartBean bean = arrayList.get(position);
        final GoodsCartListAdapter goodsCartListAdapter;

        holder.storeNameTextView.setText(bean.getStoreName());
        holder.mainCheckBox.setChecked(bean.isCheck());

        goodsCartListAdapter = new GoodsCartListAdapter(bean.getGoods());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, goodsCartListAdapter);

        if (bean.getMansong() == null) {
            holder.mansongLineView.setVisibility(View.GONE);
            holder.manSongLinearLayout.setVisibility(View.GONE);
        } else {
            holder.mansongLineView.setVisibility(View.VISIBLE);
            holder.manSongLinearLayout.setVisibility(View.VISIBLE);
            holder.manSongDescTextView.setText(bean.getMansong().get(0).getDesc());
            BaseImageLoader.get().display(bean.getMansong().get(0).getUrl(), holder.manSongGoodsImageView);
        }

        goodsCartListAdapter.setOnItemClickListener(new GoodsCartListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, CartBean.GoodsBean goodsBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onGoods(positionInt, position, goodsBean);
                }
            }

            @Override
            public void onDelete(int position, CartBean.GoodsBean goodsBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onGoodsDelete(positionInt, position, goodsBean);
                }
            }

            @Override
            public void onAdd(int position, CartBean.GoodsBean goodsBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onGoodsAdd(positionInt, position, goodsBean);
                }
            }

            @Override
            public void onSub(int position, CartBean.GoodsBean goodsBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onGoodsSub(positionInt, position, goodsBean);
                }
            }

            @Override
            public void onCheck(int position, boolean isCheck, CartBean.GoodsBean goodsBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onGoodsCheck(positionInt, position, isCheck, goodsBean);
                }
            }
        });

        holder.mainCheckBox.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onCheck(positionInt, holder.mainCheckBox.isChecked(), bean);
            }
        });

        holder.storeLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onStore(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_cart, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, CartBean bean);

        void onStore(int position, CartBean bean);

        void onCheck(int position, boolean isCheck, CartBean bean);

        void onGoods(int position, int positionGoods, CartBean.GoodsBean bean);

        void onGoodsDelete(int position, int positionGoods, CartBean.GoodsBean bean);

        void onGoodsAdd(int position, int positionGoods, CartBean.GoodsBean bean);

        void onGoodsSub(int position, int positionGoods, CartBean.GoodsBean bean);

        void onGoodsCheck(int position, int positionGoods, boolean isCheck, CartBean.GoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.storeLinearLayout)
        private LinearLayoutCompat storeLinearLayout;
        @ViewInject(R.id.mainCheckBox)
        private AppCompatCheckBox mainCheckBox;
        @ViewInject(R.id.storeNameTextView)
        private AppCompatTextView storeNameTextView;

        @ViewInject(R.id.mansongLineView)
        private View mansongLineView;
        @ViewInject(R.id.manSongLinearLayout)
        private LinearLayoutCompat manSongLinearLayout;
        @ViewInject(R.id.manSongDescTextView)
        private AppCompatTextView manSongDescTextView;
        @ViewInject(R.id.manSongGoodsImageView)
        private AppCompatImageView manSongGoodsImageView;

        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
