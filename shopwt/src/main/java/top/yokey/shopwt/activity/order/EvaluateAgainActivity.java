package top.yokey.shopwt.activity.order;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.OrderEvaluateAgainListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseFileClient;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.FileUploadBean;
import top.yokey.base.bean.OrderEvaluateAgainBean;
import top.yokey.base.model.MemberEvaluateModel;
import top.yokey.base.model.SnsAlbumModel;
import top.yokey.base.util.JsonUtil;

import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class EvaluateAgainActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private RecyclerView mainRecyclerView;
    private AppCompatTextView saveTextView;

    private int positionInt;
    private int positionImageInt;

    private String orderIdString;
    private OrderEvaluateAgainBean orderEvaluateAgainBean;

    private OrderEvaluateAgainListAdapter mainAdapter;
    private ArrayList<OrderEvaluateAgainBean.EvaluateGoodsBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_order_evaluate_again);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        saveTextView = findViewById(R.id.saveTextView);

    }

    @Override
    public void initData() {

        orderIdString = getIntent().getStringExtra(BaseConstant.DATA_ID);

        if (TextUtils.isEmpty(orderIdString)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "追加评价");

        orderEvaluateAgainBean = new OrderEvaluateAgainBean();

        positionInt = 0;
        mainArrayList = new ArrayList<>();
        mainAdapter = new OrderEvaluateAgainListAdapter(mainArrayList);
        BaseApplication.get().setRecyclerView(getActivity(), mainRecyclerView, mainAdapter);

        again();

    }

    @Override
    public void initEven() {

        mainAdapter.setOnItemClickListener(new OrderEvaluateAgainListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, OrderEvaluateAgainBean.EvaluateGoodsBean evaluateGoodsBean) {

            }

            @Override
            public void onClickImage(int position, int positionImage, OrderEvaluateAgainBean.EvaluateGoodsBean evaluateGoodsBean) {
                positionInt = position;
                positionImageInt = positionImage;
                BaseApplication.get().startMatisse(getActivity(), 1, BaseConstant.CODE_ALBUM);
            }
        });

        saveTextView.setOnClickListener(view -> save());

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK && req == BaseConstant.CODE_ALBUM) {
            updateImage(Matisse.obtainPathResult(intent).get(0));
        }
    }

    //自定义方法

    private void save() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("order_id", orderIdString);

        OrderEvaluateAgainBean.EvaluateGoodsBean bean;
        for (int i = 0; i < mainArrayList.size(); i++) {
            bean = mainArrayList.get(i);
            hashMap.put("goods[" + bean.getGevalId() + "][comment]", bean.getEvaluateContent());
            hashMap.put("goods[" + bean.getGevalId() + "][evaluate_image][0]", bean.getEvaluateImage0Name());
            hashMap.put("goods[" + bean.getGevalId() + "][evaluate_image][1]", bean.getEvaluateImage1Name());
            hashMap.put("goods[" + bean.getGevalId() + "][evaluate_image][2]", bean.getEvaluateImage2Name());
            hashMap.put("goods[" + bean.getGevalId() + "][evaluate_image][3]", bean.getEvaluateImage3Name());
            hashMap.put("goods[" + bean.getGevalId() + "][evaluate_image][4]", bean.getEvaluateImage4Name());
        }

        saveTextView.setEnabled(false);
        saveTextView.setText("追加评价中...");

        MemberEvaluateModel.get().saveAgain(hashMap, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("追加评价成功");
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                saveTextView.setText("追加评价");
                saveTextView.setEnabled(true);
            }
        });

    }

    private void again() {

        MemberEvaluateModel.get().again(orderIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                orderEvaluateAgainBean = JsonUtil.json2Bean(baseBean.getDatas(), OrderEvaluateAgainBean.class);
                mainArrayList.addAll(orderEvaluateAgainBean.getEvaluateGoods());
                mainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        again();
                    }
                }.start();
            }
        });

    }

    private void updateImage(String path) {

        SnsAlbumModel.get().fileUpload(BaseFileClient.get().createImage("evaluate" + positionInt, BaseImageLoader.get().getLocal(path)), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                FileUploadBean fileUploadBean = JsonUtil.json2Bean(baseBean.getDatas(), FileUploadBean.class);
                switch (positionImageInt) {
                    case 0:
                        mainArrayList.get(positionInt).setEvaluateImage0(fileUploadBean.getFileUrl());
                        mainArrayList.get(positionInt).setEvaluateImage0Name(fileUploadBean.getFileName());
                        break;
                    case 1:
                        mainArrayList.get(positionInt).setEvaluateImage1(fileUploadBean.getFileUrl());
                        mainArrayList.get(positionInt).setEvaluateImage1Name(fileUploadBean.getFileName());
                        break;
                    case 2:
                        mainArrayList.get(positionInt).setEvaluateImage2(fileUploadBean.getFileUrl());
                        mainArrayList.get(positionInt).setEvaluateImage2Name(fileUploadBean.getFileName());
                        break;
                    case 3:
                        mainArrayList.get(positionInt).setEvaluateImage3(fileUploadBean.getFileUrl());
                        mainArrayList.get(positionInt).setEvaluateImage3Name(fileUploadBean.getFileName());
                        break;
                    case 4:
                        mainArrayList.get(positionInt).setEvaluateImage4(fileUploadBean.getFileUrl());
                        mainArrayList.get(positionInt).setEvaluateImage4Name(fileUploadBean.getFileName());
                        break;
                }
                mainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
