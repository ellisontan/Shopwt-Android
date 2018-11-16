package top.yokey.shopwt.adapter;

import android.graphics.Color;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.base.base.BaseViewHolder;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class SpecListAdapter extends RecyclerView.Adapter<SpecListAdapter.ViewHolder> {

    private final ArrayList<HashMap<String, String>> arrayList;
    private onItemClickListener onItemClickListener;

    public SpecListAdapter(ArrayList<HashMap<String, String>> arrayList) {
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
        final HashMap<String, String> hashMap = arrayList.get(position);

        holder.mainTextView.setText(hashMap.get("value"));

        if (hashMap.get("default").equals("1")) {
            holder.mainTextView.setTextColor(Color.WHITE);
            holder.mainTextView.setBackgroundResource(R.drawable.selector_border_primary);
        } else {
            holder.mainTextView.setTextColor(Color.GRAY);
            holder.mainTextView.setBackgroundResource(R.drawable.selector_border_selector);
        }

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, hashMap.get("id"), hashMap.get("value"));
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_spec, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(onItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface onItemClickListener {

        void onClick(int position, String id, String value);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.mainTextView)
        private AppCompatTextView mainTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
