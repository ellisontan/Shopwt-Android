package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.EvaluateGoodsBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class EvaluateGoodsSimpleListAdapter extends RecyclerView.Adapter<EvaluateGoodsSimpleListAdapter.ViewHolder> {

    private final ArrayList<EvaluateGoodsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public EvaluateGoodsSimpleListAdapter(ArrayList<EvaluateGoodsBean> arrayList) {
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
        final EvaluateGoodsBean bean = arrayList.get(position);

        holder.mainRatingBar.setRating(Float.parseFloat(bean.getGevalScores()));
        holder.contentTextView.setText(bean.getGevalFrommembername());
        holder.contentTextView.append("：");
        holder.contentTextView.append(bean.getGevalContent());
        holder.timeTextView.setText(bean.getGevalAddtimeDate());

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_evaluate_goods_simple, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, EvaluateGoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainRatingBar)
        private AppCompatRatingBar mainRatingBar;
        @ViewInject(R.id.contentTextView)
        private AppCompatTextView contentTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
