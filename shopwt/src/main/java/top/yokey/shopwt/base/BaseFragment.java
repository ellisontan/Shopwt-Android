package top.yokey.shopwt.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * 基础Fragment
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@SuppressWarnings("ALL")
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = x.view().inject(this, inflater, viewGroup);
        initData();
        initEven();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseBusClient.get().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseBusClient.get().unregister(this);
    }

    //必须重载

    public abstract void initData();

    public abstract void initEven();

}
