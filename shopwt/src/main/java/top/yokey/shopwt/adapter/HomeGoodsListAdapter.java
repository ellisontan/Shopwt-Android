package top.yokey.shopwt.adapter;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

public class HomeGoodsListAdapter extends RecyclerView.Adapter<HomeGoodsListAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<HomeBean.GoodsBean.ItemBean> arrayList;

    HomeGoodsListAdapter(Activity activity, ArrayList<HomeBean.GoodsBean.ItemBean> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final HomeBean.GoodsBean.ItemBean bean = arrayList.get(position);

        int width = BaseApplication.get().getWidth() / 2 - 16;
        BaseImageLoader.get().displayRadius(bean.getGoodsImage(), holder.mainImageView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
        holder.mainImageView.setLayoutParams(layoutParams);
        holder.nameTextView.setText(bean.getGoodsName());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getGoodsSalePrice());

        holder.mainRelativeLayout.setOnClickListener(view -> BaseApplication.get().startGoods(activity, bean.getGoodsId()));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_home_goods, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
