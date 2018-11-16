package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.PreDepositRechargeLogBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class PreDepositRechargeLogListAdapter extends RecyclerView.Adapter<PreDepositRechargeLogListAdapter.ViewHolder> {

    private final ArrayList<PreDepositRechargeLogBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public PreDepositRechargeLogListAdapter(ArrayList<PreDepositRechargeLogBean> arrayList) {
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
        final PreDepositRechargeLogBean bean = arrayList.get(position);

        holder.mainTextView.setText(bean.getPdrPaymentName());
        holder.mainTextView.append("：" + bean.getPdrPaymentStateText());
        holder.moneyTextView.setText(bean.getPdrAmount());
        holder.descTextView.setText("编号：");
        holder.descTextView.append(bean.getPdrSn());
        holder.timeTextView.setText(bean.getPdrAddTimeText());

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_pre_deposit_recharge_log, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, PreDepositRechargeLogBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainTextView)
        private AppCompatTextView mainTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.descTextView)
        private AppCompatTextView descTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
