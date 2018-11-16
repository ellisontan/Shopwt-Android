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

import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.GoodsSellerBean;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsSellerListAdapter extends RecyclerView.Adapter<GoodsSellerListAdapter.ViewHolder> {

    private final ArrayList<GoodsSellerBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public GoodsSellerListAdapter(ArrayList<GoodsSellerBean> arrayList) {
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
        final GoodsSellerBean bean = arrayList.get(position);

        BaseImageLoader.get().display(bean.getGoodsImage(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsPrice());
        holder.timeTextView.setText("日期：");
        holder.timeTextView.append(bean.getGoodsAddtime());
        holder.timeTextView.append(" 库存：");
        holder.timeTextView.append(bean.getGoodsStorageSum());

        switch (bean.getGoodsState()) {
            case "1":
                holder.optionTextView.setText(" 下架 ");
                break;
            case "0":
                holder.optionTextView.setText(" 上架 ");
                break;
            case "10":
                holder.optionTextView.setText(" 禁售 ");
                break;
        }

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

        holder.editTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEditor(positionInt, bean);
            }
        });

        holder.optionTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onOption(positionInt, bean);
            }
        });

        holder.deleteTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDelete(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_seller, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, GoodsSellerBean bean);

        void onEditor(int position, GoodsSellerBean bean);

        void onOption(int position, GoodsSellerBean bean);

        void onDelete(int position, GoodsSellerBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.editTextView)
        private AppCompatTextView editTextView;
        @ViewInject(R.id.optionTextView)
        private AppCompatTextView optionTextView;
        @ViewInject(R.id.deleteTextView)
        private AppCompatTextView deleteTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
