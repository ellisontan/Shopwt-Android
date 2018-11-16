package top.yokey.shopwt.activity.home;

import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.view.PullRefreshView;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.ChatListAdapter;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.ChatListBean;
import top.yokey.base.model.MemberChatModel;
import top.yokey.base.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class ChatListActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private PullRefreshView mainPullRefreshView;

    private ChatListAdapter mainAdapter;
    private ArrayList<ChatListBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "消息列表");
        mainArrayList = new ArrayList<>();
        mainAdapter = new ChatListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.setCanLoadMore(false);

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                getChatList();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatList();
            }

            @Override
            public void onLoadMore() {

            }
        });

        mainAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, ChatListBean chatListBean) {
                BaseApplication.get().startChatOnly(getActivity(), chatListBean.getUId(), "");
            }

            @Override
            public void onDelete(int position, ChatListBean chatListBean) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getChatList();
    }

    //自定义方法

    private void getChatList() {

        mainPullRefreshView.setLoading();

        MemberChatModel.get().getUserList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    mainArrayList.clear();
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                    JSONObject jsonObject = new JSONObject(data);
                    Iterator iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        String value = jsonObject.getString(key);
                        mainArrayList.add(JsonUtil.json2Bean(value, ChatListBean.class));
                    }
                    mainPullRefreshView.setComplete();
                } catch (JSONException e) {
                    mainPullRefreshView.setComplete();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

}
