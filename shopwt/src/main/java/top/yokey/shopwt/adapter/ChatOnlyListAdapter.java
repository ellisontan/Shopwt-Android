package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ChatBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ChatOnlyListAdapter extends RecyclerView.Adapter<ChatOnlyListAdapter.ViewHolder> {

    private final String avatar;
    private final ArrayList<ChatBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ChatOnlyListAdapter(ArrayList<ChatBean> arrayList, String avatar) {
        this.avatar = avatar;
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
        final ChatBean bean = arrayList.get(position);

        holder.friendRelativeLayout.setVisibility(View.GONE);
        holder.mineRelativeLayout.setVisibility(View.GONE);

        if (bean.getFName().equals(BaseApplication.get().getMemberBean().getUserName())) {
            holder.mineRelativeLayout.setVisibility(View.VISIBLE);
            holder.mineContentTextView.setVisibility(View.GONE);
            holder.mineContentImageView.setVisibility(View.GONE);
            BaseImageLoader.get().displayCircle(BaseApplication.get().getMemberBean().getAvatar(), holder.mineImageView);
            if (bean.getTMsg().contains("SIMG")) {
                String link = bean.getTMsg().replace("]", "");
                link = link.replace("[SIMG:", "");
                holder.mineContentImageView.setVisibility(View.VISIBLE);
                BaseImageLoader.get().display(link, holder.mineContentImageView);
            } else {
                holder.mineContentTextView.setVisibility(View.VISIBLE);
                holder.mineContentTextView.setText(Html.fromHtml(bean.getTMsg()));
            }
        } else {
            holder.friendRelativeLayout.setVisibility(View.VISIBLE);
            holder.friendContentTextView.setVisibility(View.GONE);
            holder.friendContentImageView.setVisibility(View.GONE);
            BaseImageLoader.get().displayCircle(avatar, holder.friendImageView);
            if (bean.getTMsg().contains("SIMG")) {
                String link = bean.getTMsg().replace("]", "");
                link = link.replace("[SIMG:", "");
                holder.friendContentImageView.setVisibility(View.VISIBLE);
                BaseImageLoader.get().display(link, holder.friendContentImageView);
            } else {
                holder.friendContentTextView.setVisibility(View.VISIBLE);
                holder.friendContentTextView.setText(Html.fromHtml(bean.getTMsg()));
            }
        }

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_chat_only, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ChatBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;

        @ViewInject(R.id.friendRelativeLayout)
        private RelativeLayout friendRelativeLayout;
        @ViewInject(R.id.friendImageView)
        private AppCompatImageView friendImageView;
        @ViewInject(R.id.friendContentTextView)
        private AppCompatTextView friendContentTextView;
        @ViewInject(R.id.friendContentImageView)
        private AppCompatImageView friendContentImageView;

        @ViewInject(R.id.mineRelativeLayout)
        private RelativeLayout mineRelativeLayout;
        @ViewInject(R.id.mineImageView)
        private AppCompatImageView mineImageView;
        @ViewInject(R.id.mineContentTextView)
        private AppCompatTextView mineContentTextView;
        @ViewInject(R.id.mineContentImageView)
        private AppCompatImageView mineContentImageView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
