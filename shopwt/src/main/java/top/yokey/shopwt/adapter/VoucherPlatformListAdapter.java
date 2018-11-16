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

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.VoucherPlatformBean;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class VoucherPlatformListAdapter extends RecyclerView.Adapter<VoucherPlatformListAdapter.ViewHolder> {

    private final ArrayList<VoucherPlatformBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public VoucherPlatformListAdapter(ArrayList<VoucherPlatformBean> arrayList) {
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
        final VoucherPlatformBean bean = arrayList.get(position);

        BaseImageLoader.get().display(bean.getCouponTCustomimgUrl(), holder.mainImageView);
        holder.titleTextView.setText(bean.getCouponTTitle());
        holder.timeTextView.setText(bean.getEndDate());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getCouponTPrice());
        holder.progressTextView.setText("已领取：");
        holder.progressTextView.append(bean.getCouponTProgress() + "%");

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_voucher_platform, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, VoucherPlatformBean bean);

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
        @ViewInject(R.id.progressTextView)
        private AppCompatTextView progressTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
