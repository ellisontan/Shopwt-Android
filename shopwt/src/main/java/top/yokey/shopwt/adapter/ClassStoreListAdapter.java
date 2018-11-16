package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ClassStoreBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ClassStoreListAdapter extends RecyclerView.Adapter<ClassStoreListAdapter.ViewHolder> {

    private final ArrayList<ClassStoreBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ClassStoreListAdapter(ArrayList<ClassStoreBean> arrayList) {
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
        final ClassStoreBean bean = arrayList.get(position);
        final ClassStoreListAdapter classStoreListAdapter;

        if (bean.getLevel().equals("1")) {
            holder.nameTextView.setText(bean.getName());
        } else {
            holder.nameTextView.setText("  -  ");
            holder.nameTextView.append(bean.getName());
        }

        if (bean.getChild().size() != 0) {
            holder.mainRecyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.mainRecyclerView.setVisibility(View.GONE);
        }

        classStoreListAdapter = new ClassStoreListAdapter(bean.getChild());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, classStoreListAdapter);

        classStoreListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, ClassStoreBean classStoreBean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onChildClick(position, classStoreBean);
                }
            }

            @Override
            public void onChildClick(int position, ClassStoreBean classStoreBean) {

            }
        });

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_class_store, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ClassStoreBean bean);

        void onChildClick(int position, ClassStoreBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
