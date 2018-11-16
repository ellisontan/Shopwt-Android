package top.yokey.shopwt.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ClassBean;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    private final ArrayList<ClassBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ClassListAdapter(ArrayList<ClassBean> arrayList) {
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
        final ClassBean bean = arrayList.get(position);

        holder.nameTextView.setText(bean.getGcName());

        if (bean.isClick()) {
            holder.nameTextView.setTextColor(Color.parseColor("#EE5D64"));
        } else {
            holder.nameTextView.setTextColor(BaseApplication.get().getColors(R.color.greySub));
        }

        if (TextUtils.isEmpty(bean.getImage())) {
            if (bean.isClick()) {
                holder.mainImageView.setImageResource(R.mipmap.ic_goods_class_default_press);
            } else {
                holder.mainImageView.setImageResource(R.mipmap.ic_goods_class_default);
            }
        } else {
            x.image().bind(holder.mainImageView, bean.getImage(), new Callback.CacheCallback<Drawable>() {
                @Override
                public boolean onCache(Drawable result) {
                    if (bean.isClick()) {
                        holder.mainImageView.setImageDrawable(result);
                    } else {
                        holder.mainImageView.setImageDrawable(BaseApplication.get().getMipmap(result, R.color.greySub));
                    }
                    return false;
                }

                @Override
                public void onSuccess(Drawable result) {
                    if (bean.isClick()) {
                        holder.mainImageView.setImageDrawable(result);
                    } else {
                        holder.mainImageView.setImageDrawable(BaseApplication.get().getMipmap(result, R.color.greySub));
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_class, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ClassBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
