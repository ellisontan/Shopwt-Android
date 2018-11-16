package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ExpressSellerBean;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ExpressSellerListAdapter extends RecyclerView.Adapter<ExpressSellerListAdapter.ViewHolder> {

    private final ArrayList<ExpressSellerBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ExpressSellerListAdapter(ArrayList<ExpressSellerBean> arrayList) {
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
        final ExpressSellerBean bean = arrayList.get(position);

        holder.mainCheckBox.setChecked(bean.getIsCheck().equals("1"));
        holder.mainCheckBox.setText(bean.getEName());

        holder.mainCheckBox.setOnClickListener(view -> arrayList.get(positionInt).setIsCheck(holder.mainCheckBox.isChecked() ? "1" : "0"));

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_express_seller, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ExpressSellerBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.mainCheckBox)
        private AppCompatCheckBox mainCheckBox;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
