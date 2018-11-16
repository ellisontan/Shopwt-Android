package top.yokey.shopwt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.ChatListBean;
import top.yokey.shopwt.view.SlidingView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final ArrayList<ChatListBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ChatListAdapter(ArrayList<ChatListBean> arrayList) {
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
        final ChatListBean bean = arrayList.get(position);

        holder.contentRelativeLayout.getLayoutParams().width = BaseApplication.get().getWidth();
        BaseImageLoader.get().displayCircle(bean.getAvatar(), holder.mainImageView);
        holder.nameTextView.setText(bean.getUName());
        holder.timeTextView.setText(bean.getTime());
        if (bean.getTMsg().contains("SIMG")) {
            holder.contentTextView.setText("[图片]");
        } else {
            holder.contentTextView.setText(bean.getTMsg());
        }

        holder.deleteTextView.setOnClickListener(view -> {
            if (holder.mainSlidingView != null) {
                holder.mainSlidingView.closeMenu();
            }
            if (onItemClickListener != null) {
                onItemClickListener.onDelete(positionInt, bean);
            }
        });

        holder.contentRelativeLayout.setOnClickListener(view -> {
            if (holder.mainSlidingView != null) {
                holder.mainSlidingView.closeMenu();
            }
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (holder.mainSlidingView != null) {
                holder.mainSlidingView.closeMenu();
            }
        });

        holder.mainSlidingView.setSlidingListener(new SlidingView.OnSlidingListener() {
            @Override
            public void onOpen(View view) {
                holder.mainSlidingView = (SlidingView) view;
            }

            @Override
            public void onMove(SlidingView slidingView) {
                if (holder.mainSlidingView != null) {
                    if (holder.mainSlidingView != slidingView) {
                        holder.mainSlidingView = null;
                        slidingView.closeMenu();
                    }
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_chat, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ChatListBean bean);

        void onDelete(int position, ChatListBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainSlidingView)
        private SlidingView mainSlidingView;
        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.contentRelativeLayout)
        private RelativeLayout contentRelativeLayout;
        @ViewInject(R.id.deleteTextView)
        private AppCompatTextView deleteTextView;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.contentTextView)
        private AppCompatTextView contentTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
