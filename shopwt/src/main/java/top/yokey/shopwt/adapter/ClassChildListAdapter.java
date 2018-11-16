package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
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

public class ClassChildListAdapter extends RecyclerView.Adapter<ClassChildListAdapter.ViewHolder> {

    private final ArrayList<ClassChildBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ClassChildListAdapter(ArrayList<ClassChildBean> arrayList) {
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
        final ClassChildBean bean = arrayList.get(position);
        final ClassItemListAdapter classItemListAdapter;

        holder.nameTextView.setText(bean.getGcName());

        classItemListAdapter = new ClassItemListAdapter(bean.getChild());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, classItemListAdapter);
        holder.mainRecyclerView.setLayoutManager(new GridLayoutManager(BaseApplication.get(), 3));

        classItemListAdapter.setOnItemClickListener((position1, childBean) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(positionInt, bean, childBean);
            }
        });

        holder.nameTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_class_child, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ClassChildBean bean);

        void onItemClick(int position, ClassChildBean classChildBean, ClassChildBean.ChildBean childBean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
