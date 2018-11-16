package top.yokey.shopwt.adapter;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.HomeBean;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class Home3ListAdapter extends RecyclerView.Adapter<Home3ListAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<HomeBean.Home3Bean.ItemBean> arrayList;

    Home3ListAdapter(Activity activity, ArrayList<HomeBean.Home3Bean.ItemBean> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final HomeBean.Home3Bean.ItemBean bean = arrayList.get(position);

        BaseImageLoader.get().display(bean.getImage(), holder.mainImageView);

        holder.mainLinearLayout.setOnClickListener(view -> BaseApplication.get().startTypeValue(activity, bean.getType(), bean.getData()));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_home_3, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
