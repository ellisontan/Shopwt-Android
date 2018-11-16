package top.yokey.shopwt.activity.choose;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import top.yokey.shopwt.R;
import top.yokey.shopwt.adapter.AreaListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.AreaBean;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.AreaModel;
import top.yokey.base.util.JsonUtil;

import java.util.ArrayList;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class AreaActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView areaTextView;
    private AppCompatTextView cityTextView;
    private AppCompatTextView provinceTextView;
    private RecyclerView areaRecyclerView;
    private RecyclerView cityRecyclerView;
    private RecyclerView provinceRecyclerView;

    private long exitTimeLong;
    private String areaIdString;
    private String cityIdString;
    private String provinceIdString;
    private String areaNameString;
    private String cityNameString;
    private String provinceNameString;

    private AreaListAdapter areaAdapter;
    private ArrayList<AreaBean> areaArrayList;
    private AreaListAdapter cityAdapter;
    private ArrayList<AreaBean> cityArrayList;
    private AreaListAdapter provinceAdapter;
    private ArrayList<AreaBean> provinceArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_choose_area);
        mainToolbar = findViewById(R.id.mainToolbar);
        areaTextView = findViewById(R.id.areaTextView);
        cityTextView = findViewById(R.id.cityTextView);
        provinceTextView = findViewById(R.id.provinceTextView);
        areaRecyclerView = findViewById(R.id.areaRecyclerView);
        cityRecyclerView = findViewById(R.id.cityRecyclerView);
        provinceRecyclerView = findViewById(R.id.provinceRecyclerView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "选择地区");

        exitTimeLong = 0L;
        areaIdString = "0";
        cityIdString = "0";
        provinceIdString = "0";

        areaArrayList = new ArrayList<>();
        areaAdapter = new AreaListAdapter(areaArrayList);
        BaseApplication.get().setRecyclerView(BaseApplication.get(), areaRecyclerView, areaAdapter);
        areaRecyclerView.setItemAnimator(new DefaultItemAnimator());

        cityArrayList = new ArrayList<>();
        cityAdapter = new AreaListAdapter(cityArrayList);
        BaseApplication.get().setRecyclerView(BaseApplication.get(), cityRecyclerView, cityAdapter);
        cityRecyclerView.setItemAnimator(new DefaultItemAnimator());

        provinceArrayList = new ArrayList<>();
        provinceAdapter = new AreaListAdapter(provinceArrayList);
        BaseApplication.get().setRecyclerView(BaseApplication.get(), provinceRecyclerView, provinceAdapter);
        provinceRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getProvince();

    }

    @Override
    public void initEven() {

        provinceAdapter.setOnItemClickListener((position, areaBean) -> {
            areaIdString = "";
            cityIdString = "";
            areaNameString = "";
            cityNameString = "";
            provinceIdString = areaBean.getAreaId();
            provinceNameString = areaBean.getAreaName();
            areaTextView.setText("地区");
            cityTextView.setText("城市");
            provinceTextView.setText(provinceNameString);
            areaTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            cityTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            provinceTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
            getCity();
        });

        cityAdapter.setOnItemClickListener((position, areaBean) -> {
            areaIdString = "";
            areaNameString = "";
            cityIdString = areaBean.getAreaId();
            cityNameString = areaBean.getAreaName();
            areaTextView.setText("地区");
            cityTextView.setText(cityNameString);
            areaTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            cityTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
            getArea();
        });

        areaAdapter.setOnItemClickListener((position, areaBean) -> {
            areaIdString = areaBean.getAreaId();
            areaNameString = areaBean.getAreaName();
            Intent intent = new Intent();
            intent.putExtra("area_id", areaIdString);
            intent.putExtra("city_id", cityIdString);
            intent.putExtra("province_id", provinceIdString);
            intent.putExtra("area_name", areaNameString);
            intent.putExtra("city_name", cityNameString);
            intent.putExtra("province_name", provinceNameString);
            intent.putExtra("area_info", provinceNameString + " " + cityNameString + " " + areaNameString);
            BaseApplication.get().finishOk(getActivity(), intent);
        });

    }

    @Override
    public void onReturn() {

        if (System.currentTimeMillis() - exitTimeLong > BaseConstant.TIME_EXIT) {
            BaseToast.get().showReturnOneMoreTime();
            exitTimeLong = System.currentTimeMillis();
        } else {
            super.onReturn();
        }

    }

    //自定义方法

    private void getArea() {

        AreaModel.get().areaList(cityIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                areaArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "area_list");
                areaArrayList.addAll(JsonUtil.json2ArrayList(data, AreaBean.class));
                areaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getArea();
                    }
                }.start();
            }
        });

    }

    private void getCity() {

        AreaModel.get().areaList(provinceIdString, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                cityArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "area_list");
                cityArrayList.addAll(JsonUtil.json2ArrayList(data, AreaBean.class));
                cityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getCity();
                    }
                }.start();
            }
        });

    }

    private void getProvince() {

        AreaModel.get().areaList("", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                provinceArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "area_list");
                provinceArrayList.addAll(JsonUtil.json2ArrayList(data, AreaBean.class));
                provinceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getProvince();
                    }
                }.start();
            }
        });

    }

}
