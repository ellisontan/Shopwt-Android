package top.yokey.shopwt.activity.main;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;

import io.github.xudaojie.qrcodelib.CaptureActivity;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.SearchHistoryListAdapter;
import top.yokey.shopwt.adapter.SearchKeyListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.base.BaseFragment;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.IndexModel;
import top.yokey.base.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_main_search)
public class SearchFragment extends BaseFragment {

    @ViewInject(R.id.scanImageView)
    private AppCompatImageView scanImageView;
    @ViewInject(R.id.searchEditText)
    private AppCompatEditText searchEditText;
    @ViewInject(R.id.searchImageView)
    private AppCompatImageView searchImageView;

    @ViewInject(R.id.keyRecyclerView)
    private RecyclerView keyRecyclerView;
    @ViewInject(R.id.historyRecyclerView)
    private RecyclerView historyRecyclerView;

    private ArrayList<String> keyArrayList;
    private SearchKeyListAdapter keyAdapter;

    private ArrayList<String> historyArrayList;
    private SearchHistoryListAdapter historyAdapter;

    @Override
    public void initData() {

        keyArrayList = new ArrayList<>();
        keyAdapter = new SearchKeyListAdapter(keyArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), keyRecyclerView, keyAdapter);
        keyRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        historyArrayList = new ArrayList<>();
        historyAdapter = new SearchHistoryListAdapter(historyArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), historyRecyclerView, historyAdapter);
        historyRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        getSearchKey();

    }

    @Override
    public void initEven() {

        scanImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), CaptureActivity.class, BaseConstant.CODE_QRCODE));

        searchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                BaseApplication.get().startGoodsList(getActivity(), searchEditText.getText().toString(), "", "");
            }
            return false;
        });

        searchImageView.setOnClickListener(view -> BaseApplication.get().startGoodsList(getActivity(), searchEditText.getText().toString(), "", ""));

        keyAdapter.setOnItemClickListener((position, key) -> BaseApplication.get().startGoodsList(getActivity(), key, "", ""));

        historyAdapter.setOnItemClickListener((position, key) -> BaseApplication.get().startGoodsList(getActivity(), key, "", ""));

    }

    @Override
    public void onResume() {
        super.onResume();
        getSearchKey();
    }

    //自定义方法

    private void getSearchKey() {

        IndexModel.get().searchKeyList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    //热门搜索
                    keyArrayList.clear();
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        keyArrayList.add(jsonArray.getString(i));
                    }
                    keyAdapter.notifyDataSetChanged();
                    //历史记录
                    historyArrayList.clear();
                    data = JsonUtil.getDatasString(baseBean.getDatas(), "his_list");
                    jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        historyArrayList.add(jsonArray.getString(i));
                    }
                    historyAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {

            }
        });

    }

}
