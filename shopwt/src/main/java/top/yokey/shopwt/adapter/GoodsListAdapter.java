package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseAnimClient;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.GoodsBean;
import top.yokey.base.model.MemberFavoritesModel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.ViewHolder> {

    private final boolean isGridModel;
    private final ArrayList<GoodsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public GoodsListAdapter(ArrayList<GoodsBean> arrayList, boolean isGridModel) {
        this.arrayList = arrayList;
        this.isGridModel = isGridModel;
        this.onItemClickListener = null;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int positionInt = position;
        final GoodsBean bean = arrayList.get(position);

        int width = BaseApplication.get().getWidth() / 2 - 16;
        BaseImageLoader.get().displayRadius(bean.getGoodsImageUrl(), holder.mainImageView);
        ViewGroup.LayoutParams layoutParams = holder.mainImageView.getLayoutParams();
        if (isGridModel) {
            layoutParams.width = width;
            //noinspection SuspiciousNameCombination
            layoutParams.height = width;
        }
        holder.mainImageView.setLayoutParams(layoutParams);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.descTextView.setText(bean.getGoodsJingle());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsPrice());
        holder.mobileTextView.setVisibility(bean.isSoleFlag() ? View.VISIBLE : View.GONE);
        holder.descTextView.setVisibility(TextUtils.isEmpty(bean.getGoodsJingle()) ? View.GONE : View.VISIBLE);
        holder.saleTextView.setText("销量：");
        holder.saleTextView.append(bean.getGoodsSalenum());

        if (bean.getIsOwnShop() == null) {
            holder.ownShopTextView.setVisibility(View.GONE);
        } else {
            holder.ownShopTextView.setVisibility(bean.getIsOwnShop().equals("1") ? View.VISIBLE : View.GONE);
        }

        if (isGridModel) {
            holder.descTextView.setVisibility(View.GONE);
        }

        holder.cartImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onCart(positionInt, bean);
            }
        });

        holder.moreImageView.setOnClickListener(view -> MemberFavoritesModel.get().favoritesInfo(bean.getGoodsId(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (holder.favoritesTextView.getVisibility() == View.VISIBLE) {
                    holder.favoritesTextView.setVisibility(View.GONE);
                    BaseAnimClient.get().goneAlpha(holder.favoritesTextView);
                } else {
                    holder.favoritesTextView.setVisibility(View.VISIBLE);
                    BaseAnimClient.get().showAlpha(holder.favoritesTextView);
                }
                if (baseBean.getDatas().equals("null") || baseBean.getDatas().equals("[]")) {
                    holder.favoritesTextView.setText("收藏");
                    holder.favoritesTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_white, 0, 0);
                } else {
                    holder.favoritesTextView.setText("已收藏");
                    holder.favoritesTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_press, 0, 0);
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        }));

        holder.favoritesTextView.setOnClickListener(view -> {
            if (holder.favoritesTextView.getText().toString().equals("收藏")) {
                MemberFavoritesModel.get().favoritesAdd(bean.getGoodsId(), new BaseHttpListener() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        holder.favoritesTextView.setText("已收藏");
                        BaseToast.get().show("收藏成功");
                        holder.favoritesTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_press, 0, 0);
                        if (holder.favoritesTextView.getVisibility() == View.VISIBLE) {
                            holder.favoritesTextView.setVisibility(View.GONE);
                            BaseAnimClient.get().goneAlpha(holder.favoritesTextView);
                        }
                    }

                    @Override
                    public void onFailure(String reason) {
                        BaseToast.get().show(reason);
                    }
                });
            } else {
                MemberFavoritesModel.get().favoritesDel(bean.getGoodsId(), new BaseHttpListener() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        holder.favoritesTextView.setText("收藏");
                        BaseToast.get().show("取消收藏成功");
                        holder.favoritesTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_white, 0, 0);
                        if (holder.favoritesTextView.getVisibility() == View.VISIBLE) {
                            holder.favoritesTextView.setVisibility(View.GONE);
                            BaseAnimClient.get().goneAlpha(holder.favoritesTextView);
                        }
                    }

                    @Override
                    public void onFailure(String reason) {
                        BaseToast.get().show(reason);
                    }
                });
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
        View view;
        if (isGridModel) {
            view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods, group, false);
        } else {
            view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_ver, group, false);
        }
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onCart(int position, GoodsBean bean);

        void onClick(int position, GoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.descTextView)
        private AppCompatTextView descTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.mobileTextView)
        private AppCompatTextView mobileTextView;
        @ViewInject(R.id.cartImageView)
        private AppCompatImageView cartImageView;
        @ViewInject(R.id.saleTextView)
        private AppCompatTextView saleTextView;
        @ViewInject(R.id.ownShopTextView)
        private AppCompatTextView ownShopTextView;
        @ViewInject(R.id.moreImageView)
        private AppCompatImageView moreImageView;
        @ViewInject(R.id.favoritesTextView)
        private AppCompatTextView favoritesTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
