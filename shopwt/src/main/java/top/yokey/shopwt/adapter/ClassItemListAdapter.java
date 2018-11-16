package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ClassChildBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

class ClassItemListAdapter extends RecyclerView.Adapter<ClassItemListAdapter.ViewHolder> {

    private final ArrayList<ClassChildBean.ChildBean> arrayList;
    private OnItemClickListener onItemClickListener;

    ClassItemListAdapter(ArrayList<ClassChildBean.ChildBean> arrayList) {
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
        final ClassChildBean.ChildBean bean = arrayList.get(position);

        holder.nameTextView.setText(bean.getGcName());

        holder.nameTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_class_item, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ClassChildBean.ChildBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
