package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ExpressSellerSendBean;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ExpressSellerSendListAdapter extends RecyclerView.Adapter<ExpressSellerSendListAdapter.ViewHolder> {

    private final ArrayList<ExpressSellerSendBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ExpressSellerSendListAdapter(ArrayList<ExpressSellerSendBean> arrayList) {
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
        final ExpressSellerSendBean bean = arrayList.get(position);

        holder.nameTextView.setText(bean.geteName());

        holder.confirmTextView.setOnClickListener(view -> {
            bean.setCode(holder.codeEditText.getText().toString());
            arrayList.get(positionInt).setCode(holder.codeEditText.getText().toString());
            if (onItemClickListener != null) {
                onItemClickListener.onConfirm(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_express_seller_send, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ExpressSellerSendBean bean);

        void onConfirm(int position, ExpressSellerSendBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.codeEditText)
        private AppCompatEditText codeEditText;
        @ViewInject(R.id.confirmTextView)
        private AppCompatTextView confirmTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
