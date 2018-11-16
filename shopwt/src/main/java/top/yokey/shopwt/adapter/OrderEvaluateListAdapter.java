package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.OrderEvaluateBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class OrderEvaluateListAdapter extends RecyclerView.Adapter<OrderEvaluateListAdapter.ViewHolder> {

    private final ArrayList<OrderEvaluateBean.OrderGoodsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public OrderEvaluateListAdapter(ArrayList<OrderEvaluateBean.OrderGoodsBean> arrayList) {
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
        final OrderEvaluateBean.OrderGoodsBean bean = arrayList.get(position);

        BaseImageLoader.get().displayRadius(bean.getGoodsImageUrl(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGoodsName());

        if (TextUtils.isEmpty(bean.getEvaluateImage0())) {
            holder.zeroImageView.setBackgroundResource(R.mipmap.ic_add_img);
        } else {
            BaseImageLoader.get().displayRadius(bean.getEvaluateImage0(), holder.zeroImageView);
        }
        if (TextUtils.isEmpty(bean.getEvaluateImage1())) {
            holder.oneImageView.setBackgroundResource(R.mipmap.ic_add_img);
        } else {
            BaseImageLoader.get().displayRadius(bean.getEvaluateImage1(), holder.oneImageView);
        }
        if (TextUtils.isEmpty(bean.getEvaluateImage2())) {
            holder.twoImageView.setBackgroundResource(R.mipmap.ic_add_img);
        } else {
            BaseImageLoader.get().displayRadius(bean.getEvaluateImage2(), holder.twoImageView);
        }
        if (TextUtils.isEmpty(bean.getEvaluateImage3())) {
            holder.thrImageView.setBackgroundResource(R.mipmap.ic_add_img);
        } else {
            BaseImageLoader.get().displayRadius(bean.getEvaluateImage3(), holder.thrImageView);
        }
        if (TextUtils.isEmpty(bean.getEvaluateImage4())) {
            holder.fouImageView.setBackgroundResource(R.mipmap.ic_add_img);
        } else {
            BaseImageLoader.get().displayRadius(bean.getEvaluateImage4(), holder.fouImageView);
        }

        holder.mainRatingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> arrayList.get(positionInt).setEvaluateRating(v + ""));

        holder.evaluateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayList.get(positionInt).setEvaluateContent(holder.evaluateEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.zeroImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickImage(positionInt, 0, bean);
            }
        });

        holder.oneImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickImage(positionInt, 1, bean);
            }
        });

        holder.twoImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickImage(positionInt, 2, bean);
            }
        });

        holder.thrImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickImage(positionInt, 3, bean);
            }
        });

        holder.fouImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickImage(positionInt, 4, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_order_evaluate, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, OrderEvaluateBean.OrderGoodsBean bean);

        void onClickImage(int position, int positionImage, OrderEvaluateBean.OrderGoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.mainRatingBar)
        private AppCompatRatingBar mainRatingBar;
        @ViewInject(R.id.evaluateEditText)
        private AppCompatEditText evaluateEditText;
        @ViewInject(R.id.zeroImageView)
        private AppCompatImageView zeroImageView;
        @ViewInject(R.id.oneImageView)
        private AppCompatImageView oneImageView;
        @ViewInject(R.id.twoImageView)
        private AppCompatImageView twoImageView;
        @ViewInject(R.id.thrImageView)
        private AppCompatImageView thrImageView;
        @ViewInject(R.id.fouImageView)
        private AppCompatImageView fouImageView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
