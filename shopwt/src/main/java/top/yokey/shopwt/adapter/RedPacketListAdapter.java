package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
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
import top.yokey.base.bean.RedPacketBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RedPacketListAdapter extends RecyclerView.Adapter<RedPacketListAdapter.ViewHolder> {

    private final ArrayList<RedPacketBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public RedPacketListAdapter(ArrayList<RedPacketBean> arrayList) {
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
        final RedPacketBean bean = arrayList.get(position);

        BaseImageLoader.get().display(bean.getRpacketCustomimgUrl(), holder.mainImageView);
        holder.titleTextView.setText(bean.getRpacketTitle());
        holder.timeTextView.setText("有效期至：");
        holder.timeTextView.append(bean.getRpacketEndDateText());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getRpacketPrice());
        holder.limitTextView.setText("满￥");
        holder.limitTextView.append(bean.getRpacketLimit());
        holder.limitTextView.append("可用");

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_red_packet, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, RedPacketBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.titleTextView)
        private AppCompatTextView titleTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.limitTextView)
        private AppCompatTextView limitTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
