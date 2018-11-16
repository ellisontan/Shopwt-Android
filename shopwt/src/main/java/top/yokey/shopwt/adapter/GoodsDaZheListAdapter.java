package top.yokey.shopwt.adapter;

import android.graphics.Paint;
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

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.GoodsDaZheBean;
import top.yokey.shopwt.view.CountdownTextView;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsDaZheListAdapter extends RecyclerView.Adapter<GoodsDaZheListAdapter.ViewHolder> {

    private final ArrayList<GoodsDaZheBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public GoodsDaZheListAdapter(ArrayList<GoodsDaZheBean> arrayList) {
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
        final GoodsDaZheBean bean = arrayList.get(position);

        BaseImageLoader.get().displayRadius(bean.getImageUrl(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getXianshiPrice());
        holder.priceTextView.setText("￥");
        holder.priceTextView.append(bean.getGoodsPrice());
        holder.priceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.timeTextView.init("", Long.parseLong(bean.getEndtime()), "剩余", "");
        holder.timeTextView.start(0);
        holder.numberTextView.setText(bean.getXianshiTitle());

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_rob_buy, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, GoodsDaZheBean bean);

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
        @ViewInject(R.id.priceTextView)
        private AppCompatTextView priceTextView;
        @ViewInject(R.id.timeTextView)
        private CountdownTextView timeTextView;
        @ViewInject(R.id.numberTextView)
        private AppCompatTextView numberTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
