package top.yokey.shopwt.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.jwenfeng.library.pulltorefresh.ViewStatus;

import top.yokey.base.base.BaseDecoration;

/**
 * 下拉刷新控件
 *
 * @author MapStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public class PullRefreshView extends PullToRefreshLayout {

    private boolean isFailure = false;
    private boolean isLoadMore = false;
    private Context context = getContext();
    private OnRefreshListener onRefreshListener = null;
    private RecyclerView recyclerView = new RecyclerView(context);
    private BaseDecoration baseDecoration = new BaseDecoration(context, BaseDecoration.VERTICAL);

    public PullRefreshView(Context context) {
        super(context);
        initialization();
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialization();
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialization();
    }

    //初始化

    private void initialization() {

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(baseDecoration);
        recyclerView.setLayoutParams(layoutParams);
        addView(recyclerView);

        setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                isLoadMore = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onRefreshListener != null) {
                            onRefreshListener.onRefresh();
                        }
                        finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                isLoadMore = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onRefreshListener != null) {
                            onRefreshListener.onLoadMore();
                        }
                        finishLoadMore();
                    }
                }, 2000);
            }
        });

    }

    //公共方法

    public void setFailure() {

        isFailure = true;
        recyclerView.getAdapter().notifyDataSetChanged();
        if (recyclerView.getAdapter().getItemCount() == 0) {
            showView(ViewStatus.ERROR_STATUS);
        }

    }

    public void setLoading() {

        if (recyclerView.getAdapter().getItemCount() == 0) {
            isFailure = false;
            recyclerView.getAdapter().notifyDataSetChanged();
            showView(ViewStatus.LOADING_STATUS);
        }

    }

    public void setComplete() {

        isFailure = false;
        recyclerView.getAdapter().notifyDataSetChanged();
        if (recyclerView.getAdapter().getItemCount() == 0) {
            showView(ViewStatus.EMPTY_STATUS);
        } else {
            showView(ViewStatus.CONTENT_STATUS);
            if (isLoadMore) {
                recyclerView.smoothScrollBy(0, 255);
            }
        }

    }

    public boolean isFailure() {

        return isFailure;

    }

    public void clearItemDecoration() {

        recyclerView.removeItemDecoration(baseDecoration);

    }

    public void setItemDecoration() {

        recyclerView.addItemDecoration(baseDecoration);

    }

    public void removeItemDecoration() {

        recyclerView.removeItemDecoration(baseDecoration);

    }

    public RecyclerView getRecyclerView() {

        return recyclerView;

    }

    //外部接口

    public interface OnRefreshListener {

        void onRefresh();

        void onLoadMore();

    }

    public void setOnRefreshListener(OnRefreshListener listener) {

        this.onRefreshListener = listener;

    }

}
