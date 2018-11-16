package top.yokey.shopwt.activity.seller;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.zhihu.matisse.Matisse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import top.yokey.shopwt.base.BaseActivity;
import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseFileClient;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.shopwt.base.BaseImageLoader;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.choose.CateActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.base.model.SellerAlbumModel;
import top.yokey.base.model.SellerGoodsModel;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsEditActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView cateTextView;
    private AppCompatEditText nameEditText;
    private AppCompatEditText moneyEditText;
    private AppCompatEditText priceEditText;
    private AppCompatTextView discountTextView;
    private AppCompatImageView oneImageView;
    private AppCompatImageView twoImageView;
    private AppCompatImageView thrImageView;
    private AppCompatImageView fouImageView;
    private AppCompatImageView fivImageView;
    private AppCompatImageView[] imageImageView;
    private AppCompatEditText stockEditText;
    private AppCompatEditText numberEditText;
    private AppCompatEditText logisticsEditText;
    private AppCompatEditText descEditText;
    private AppCompatRadioButton nowRadioButton;
    private AppCompatRadioButton wareRadioButton;
    private AppCompatTextView saveTextView;

    private String goodsId;
    private int positionInt;
    private String gcIdString;
    private String[] imageString;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_goods_edit);
        mainToolbar = findViewById(R.id.mainToolbar);
        cateTextView = findViewById(R.id.cateTextView);
        nameEditText = findViewById(R.id.nameEditText);
        moneyEditText = findViewById(R.id.moneyEditText);
        priceEditText = findViewById(R.id.priceEditText);
        discountTextView = findViewById(R.id.discountTextView);
        oneImageView = findViewById(R.id.oneImageView);
        twoImageView = findViewById(R.id.twoImageView);
        thrImageView = findViewById(R.id.thrImageView);
        fouImageView = findViewById(R.id.fouImageView);
        fivImageView = findViewById(R.id.fivImageView);
        stockEditText = findViewById(R.id.stockEditText);
        numberEditText = findViewById(R.id.numberEditText);
        logisticsEditText = findViewById(R.id.logisticsEditText);
        descEditText = findViewById(R.id.descEditText);
        nowRadioButton = findViewById(R.id.nowRadioButton);
        wareRadioButton = findViewById(R.id.wareRadioButton);
        saveTextView = findViewById(R.id.saveTextView);

    }

    @Override
    public void initData() {

        goodsId = getIntent().getStringExtra(BaseConstant.DATA_ID);
        if (TextUtils.isEmpty(goodsId)) {
            BaseToast.get().showDataError();
            BaseApplication.get().finish(getActivity());
        }

        setToolbar(mainToolbar, "编辑商品");

        imageImageView = new AppCompatImageView[5];
        imageImageView[0] = oneImageView;
        imageImageView[1] = twoImageView;
        imageImageView[2] = thrImageView;
        imageImageView[3] = fouImageView;
        imageImageView[4] = fivImageView;

        imageString = new String[5];
        imageString[0] = "";
        imageString[1] = "";
        imageString[2] = "";
        imageString[3] = "";
        imageString[4] = "";

        gcIdString = "";
        getInfo();

    }

    @Override
    public void initEven() {

        cateTextView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), CateActivity.class, BaseConstant.CODE_CLASS));

        for (int i = 0; i < 5; i++) {
            final int pos = i;
            imageImageView[i].setOnClickListener(view -> {
                positionInt = pos;
                BaseApplication.get().startMatisse(getActivity(), 1, BaseConstant.CODE_ALBUM);
            });
        }

        nowRadioButton.setOnClickListener(view -> {
            nowRadioButton.setChecked(true);
            wareRadioButton.setChecked(false);
        });

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String money = moneyEditText.getText().toString();
                String price = priceEditText.getText().toString();
                if (!TextUtils.isEmpty(money) && !TextUtils.isEmpty(price)) {
                    float m = Float.parseFloat(money);
                    float p = Float.parseFloat(price);
                    String dis = ((int) (m / p * 100)) + "";
                    discountTextView.setText(dis);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wareRadioButton.setOnClickListener(view -> {
            nowRadioButton.setChecked(false);
            wareRadioButton.setChecked(true);
        });

        saveTextView.setOnClickListener(view -> save());

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK) {
            switch (req) {
                case BaseConstant.CODE_CLASS:
                    gcIdString = intent.getStringExtra(BaseConstant.DATA_GCID);
                    cateTextView.setText(intent.getStringExtra(BaseConstant.DATA_CONTENT));
                    break;
                case BaseConstant.CODE_ALBUM:
                    updateImage(Matisse.obtainPathResult(intent).get(0));
                    break;
                default:
                    break;
            }
        }
    }

    //自定义方法

    private void save() {

        String name = nameEditText.getText().toString();
        String money = moneyEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String storage = stockEditText.getText().toString();
        String serial = numberEditText.getText().toString();
        String freight = logisticsEditText.getText().toString();
        String desc = descEditText.getText().toString();
        String state = nowRadioButton.isChecked() ? "1" : "0";
        String discount = discountTextView.getText().toString();
        StringBuilder imageAllBuilder = new StringBuilder();
        for (String string : imageString) {
            if (!TextUtils.isEmpty(string)) {
                imageAllBuilder.append(string).append(",");
            }
        }
        String imageAll = imageAllBuilder.toString();
        if (TextUtils.isEmpty(imageAll)) {
            BaseToast.get().show("最少上传一张图片！");
            return;
        }
        imageAll = imageAll.substring(0, imageAll.length() - 1);
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(money) || TextUtils.isEmpty(price) || TextUtils.isEmpty(storage) || TextUtils.isEmpty(serial) || TextUtils.isEmpty(freight) || TextUtils.isEmpty(desc)) {
            BaseToast.get().show("请输入完整信息！");
            return;
        }

        saveTextView.setEnabled(false);
        saveTextView.setText("保存中...");

        SellerGoodsModel.get().goodsEdit(goodsId, gcIdString, cateTextView.getText().toString(), name, money, price, discount, imageAll, storage, serial, freight, desc, state, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("保存成功！");
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                saveTextView.setText("保存信息");
                saveTextView.setEnabled(true);
            }
        });

    }

    private void getInfo() {

        SellerGoodsModel.get().goodsInfo(goodsId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "goodscommon_info");
                    JSONObject jsonObject = new JSONObject(data);
                    gcIdString = jsonObject.getString("gc_id");
                    cateTextView.setText(Html.fromHtml(jsonObject.getString("gc_name")));
                    nameEditText.setText(jsonObject.getString("goods_name"));
                    moneyEditText.setText(jsonObject.getString("goods_price"));
                    priceEditText.setText(jsonObject.getString("goods_marketprice"));
                    discountTextView.setText(jsonObject.getString("goods_discount"));
                    stockEditText.setText(jsonObject.getString("g_storage"));
                    numberEditText.setText(jsonObject.getString("goods_serial"));
                    logisticsEditText.setText(jsonObject.getString("goods_freight"));
                    descEditText.setText(jsonObject.getString("goods_body"));
                    nameEditText.setSelection(nameEditText.getText().length());
                    if (jsonObject.getString("goods_state").equals("1")) {
                        nowRadioButton.setChecked(true);
                        wareRadioButton.setChecked(false);
                    } else {
                        nowRadioButton.setChecked(false);
                        wareRadioButton.setChecked(true);
                    }
                    getImageInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getInfo();
                    }
                }.start();
            }
        });

    }

    private void getImageInfo() {

        SellerGoodsModel.get().goodsImageInfo(goodsId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "image_list");
                    JSONArray jsonArray = new JSONArray(data);
                    data = jsonArray.getString(0);
                    JSONObject jsonObject = new JSONObject(data);
                    jsonArray = new JSONArray(jsonObject.getString("images"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (i < 5) {
                            imageString[i] = jsonArray.getJSONObject(i).getString("goods_image");
                            BaseImageLoader.get().display(jsonArray.getJSONObject(i).getString("goods_image_url"), imageImageView[i]);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getImageInfo();
                    }
                }.start();
            }
        });

    }

    private void updateImage(String path) {

        SellerAlbumModel.get().imageUpload(BaseFileClient.get().createImage("seller_add_goods_" + positionInt, BaseImageLoader.get().getLocal(path)), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    JSONObject jsonObject = new JSONObject(baseBean.getDatas());
                    imageString[positionInt] = jsonObject.getString("name");
                    BaseImageLoader.get().display(jsonObject.getString("thumb_name"), imageImageView[positionInt]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
