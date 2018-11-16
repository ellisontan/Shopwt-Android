package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
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

public class EvaluateGoodsListAdapter extends RecyclerView.Adapter<EvaluateGoodsListAdapter.ViewHolder> {

    private final ArrayList<EvaluateGoodsBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public EvaluateGoodsListAdapter(ArrayList<EvaluateGoodsBean> arrayList) {
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

        BaseImageLoader.get().display(bean.getMemberAvatar(), holder.mainImageView);
        holder.nameTextView.setText(bean.getGevalFrommembername());
        holder.timeTextView.setText(bean.getGevalAddtimeDate());
        holder.scoreRatingBar.setRating(Float.parseFloat(bean.getGevalScores()));
        holder.contentTextView.setText(bean.getGevalContent());

        if (bean.getGevalImage240().size() == 0) {
            holder.imageLinearLayout.setVisibility(View.GONE);
        } else {
            holder.imageLinearLayout.setVisibility(View.VISIBLE);
            holder.evaImageView = new AppCompatImageView[5];
            holder.evaImageView[0] = holder.oneImageView;
            holder.evaImageView[1] = holder.twoImageView;
            holder.evaImageView[2] = holder.thrImageView;
            holder.evaImageView[3] = holder.fouImageView;
            holder.evaImageView[4] = holder.fivImageView;
            for (AppCompatImageView appCompatImageView : holder.evaImageView) {
                appCompatImageView.setVisibility(View.GONE);
            }
            for (int i = 0; i < bean.getGevalImage240().size(); i++) {
                if (i < 5) {
                    holder.evaImageView[i].setVisibility(View.VISIBLE);
                    BaseImageLoader.get().display(bean.getGevalImage240().get(i), holder.evaImageView[i]);
                }
            }
        }

        if (TextUtils.isEmpty(bean.getGevalExplain())) {
            holder.explainTextView.setVisibility(View.GONE);
        } else {
            holder.explainTextView.setVisibility(View.VISIBLE);
            holder.explainTextView.setText("掌柜回复：");
            holder.explainTextView.append(bean.getGevalExplain());
        }

        holder.appendTimeTextView.setVisibility(View.GONE);
        holder.appendTextView.setVisibility(View.GONE);
        holder.appendLinearLayout.setVisibility(View.GONE);
        holder.appendExplainTextView.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(bean.getGevalContentAgain())) {
            holder.appendTimeTextView.setVisibility(View.VISIBLE);
            holder.appendTextView.setVisibility(View.VISIBLE);
            holder.appendTimeTextView.setText(bean.getGevalAddtimeAgainDate());
            holder.appendTimeTextView.append(" 追加评价");
            holder.appendTextView.setText(bean.getGevalContentAgain());
            if (bean.getGevalImageAgain240().size() != 0) {
                holder.appendLinearLayout.setVisibility(View.VISIBLE);
                holder.appendImageView = new AppCompatImageView[5];
                holder.appendImageView[0] = holder.appendOneImageView;
                holder.appendImageView[1] = holder.appendTwoImageView;
                holder.appendImageView[2] = holder.appendThrImageView;
                holder.appendImageView[3] = holder.appendFouImageView;
                holder.appendImageView[4] = holder.appendFivImageView;
                for (AppCompatImageView appCompatImageView : holder.appendImageView) {
                    appCompatImageView.setVisibility(View.GONE);
                }
                for (int i = 0; i < bean.getGevalImageAgain240().size(); i++) {
                    if (i < 5) {
                        holder.appendImageView[i].setVisibility(View.VISIBLE);
                        BaseImageLoader.get().display(bean.getGevalImageAgain240().get(i), holder.appendImageView[i]);
                    }
                }
            }
            if (!TextUtils.isEmpty(bean.getGevalExplainAgain())) {
                holder.appendExplainTextView.setVisibility(View.VISIBLE);
                holder.appendExplainTextView.setText("掌柜回复：");
                holder.appendExplainTextView.append(bean.getGevalExplainAgain());
            }
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_evaluate_goods, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, EvaluateGoodsBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.scoreRatingBar)
        private AppCompatRatingBar scoreRatingBar;
        @ViewInject(R.id.contentTextView)
        private AppCompatTextView contentTextView;
        @ViewInject(R.id.imageLinearLayout)
        private LinearLayoutCompat imageLinearLayout;
        @ViewInject(R.id.oneImageView)
        private AppCompatImageView oneImageView;
        @ViewInject(R.id.twoImageView)
        private AppCompatImageView twoImageView;
        @ViewInject(R.id.thrImageView)
        private AppCompatImageView thrImageView;
        @ViewInject(R.id.fouImageView)
        private AppCompatImageView fouImageView;
        @ViewInject(R.id.fivImageView)
        private AppCompatImageView fivImageView;
        private AppCompatImageView[] evaImageView;
        @ViewInject(R.id.explainTextView)
        private AppCompatTextView explainTextView;

        @ViewInject(R.id.appendTimeTextView)
        private AppCompatTextView appendTimeTextView;
        @ViewInject(R.id.appendTextView)
        private AppCompatTextView appendTextView;
        @ViewInject(R.id.appendLinearLayout)
        private LinearLayoutCompat appendLinearLayout;
        @ViewInject(R.id.appendOneImageView)
        private AppCompatImageView appendOneImageView;
        @ViewInject(R.id.appendTwoImageView)
        private AppCompatImageView appendTwoImageView;
        @ViewInject(R.id.appendThrImageView)
        private AppCompatImageView appendThrImageView;
        @ViewInject(R.id.appendFouImageView)
        private AppCompatImageView appendFouImageView;
        @ViewInject(R.id.appendFivImageView)
        private AppCompatImageView appendFivImageView;
        private AppCompatImageView[] appendImageView;
        @ViewInject(R.id.appendExplainTextView)
        private AppCompatTextView appendExplainTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
