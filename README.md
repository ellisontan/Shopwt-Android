# Shopwt Android

**警告：禁止任何未授权商用！**  

**警告：禁止任何未授权商用！** 

**警告：禁止任何未授权商用！** 

#基本信息

**本开源程序作者：MapStory，联系QQ：1002285057(如需要商业授权，定制开发，请联系。PS:闲聊勿扰。)**


Shopwt 系统简介：[ShopWT官网-专业开发B2B2C多用户商城系统_微信商城_手机商城系统_网店源码](https://shopai.yokey.top/)

Shopwt PC演示站：[ShopWT多用户商城系统 - Powered by shopwt.com](https://shopai.yokey.top/)

Shopwt Wap演示站：[ShopWT多用户商城系统 - Powered by shopwt.com](https://shopai.yokey.top/mobile/)

基于好商城Shopwt的原生Android客户端，简单修改两行代码即可适配到自己的商城系统。目前实现的功能：二维码扫描，支付宝微信支付，商家后台，热更新等（一个完整的购物APP均已实现）

特别感谢以下开源项目：[xUtils](https://github.com/wyouflf/xUtils3),[Otto](https://github.com/square/otto),[Banner](https://github.com/youth5201314/banner),[Marqueeview](https://github.com/sfsheng0322/MarqueeView),[Glide](https://github.com/bumptech/glide),[QRCode-Android](https://github.com/XuDaojie/QRCode-Android),[Pulltorefresh](https://github.com/823546371/PullToRefresh),[ScrollableLayout](https://github.com/w446108264/ScrollableLayout),[SlideBack](https://github.com/leehong2005/SlideBack)

#快速体验

安装包位于：shopwt\release\shopwt-release.apk

#快速开始

1.修改：shopwt\src\main\java\top\yokey\shopwt\base\BaseConstant.java
```
public static final String SHARED_NAME = "yokey_shopwt"; //修改成为你的ShareName
public static final String URL = "http://33hao.yokey.top/"; //修改成为你的网站
```

2.如果修改了包名，为了兼容Android7.0,需要修改：shopwt\src\main\AndroidManifest.xml，shopwt\src\main\res\xml\path.xml，
```
将代码：top.yokey.shopwt 替换成自己的包名
```

3.支付宝支付

申请地址：https://www.alipay.com/

修改：APP客户端无需任何修改！

4.微信支付

申请地址：https://open.weixin.qq.com/

5.极光推送

申请地址：https://www.jiguang.cn/

修改：shopwt\build.gradle
```
JPUSH_APPKEY : 'a2fab7a5205a67d388889645' //为自己的极光推送ID
```
ShareSdk

申请地址：http://www.mob.com/

修改：shopwt\build.gradle
```
MobSDK 节点
```

#V1.0更新日记

1.完成5.X版本Android客户端所有功能

2.Shopwt Wap首页适配，首页图标随时可变

3.打折，促销商品功能

4.店铺优惠券，平台优惠券

5.专题页适配

6.首页红包领取

...

#一些截图

![1](https://images.gitee.com/uploads/images/2018/0728/141645_630191bc_941277.png "Screenshot_2018-07-28-14-09-23-245_top.yokey.shopwt.png")
![2](https://images.gitee.com/uploads/images/2018/0728/141731_732acc2b_941277.png "Screenshot_2018-07-28-14-09-29-220_top.yokey.shopwt.png")
![3](https://images.gitee.com/uploads/images/2018/0728/141742_debb6896_941277.png "Screenshot_2018-07-28-14-09-31-331_top.yokey.shopwt.png")
![4](https://images.gitee.com/uploads/images/2018/0728/141754_ac492d55_941277.png "Screenshot_2018-07-28-14-09-34-194_top.yokey.shopwt.png")
![5](https://images.gitee.com/uploads/images/2018/0728/141813_faaa0484_941277.png "Screenshot_2018-07-28-14-09-39-335_top.yokey.shopwt.png")
![6](https://images.gitee.com/uploads/images/2018/0728/142003_d189860b_941277.jpeg "Screenshot_2018-07-28-14-10-17-174_top.jpg")
![7](https://images.gitee.com/uploads/images/2018/0728/142017_ed3a0f1f_941277.jpeg "Screenshot_2018-07-28-14-12-52-082_top.yokey.jpg")
![8](https://images.gitee.com/uploads/images/2018/0728/142036_0788919d_941277.png "Screenshot_2018-07-28-14-10-22-211_top.yokey.shopwt.png")
![9](https://images.gitee.com/uploads/images/2018/0728/142049_62c507bf_941277.png "Screenshot_2018-07-28-14-10-27-369_top.yokey.shopwt.png")
![10](https://images.gitee.com/uploads/images/2018/0728/142109_73d04b16_941277.png "Screenshot_2018-07-28-14-10-33-814_top.yokey.shopwt.png")
![11](https://images.gitee.com/uploads/images/2018/0728/142744_1ae24561_941277.png "Screenshot_2018-07-28-14-10-42-249_top.yokey.shopwt.png")
![12](https://images.gitee.com/uploads/images/2018/0728/142800_e904fb95_941277.png "Screenshot_2018-07-28-14-12-58-616_top.yokey.shopwt.png")
![13](https://images.gitee.com/uploads/images/2018/0728/142812_3a4aa1fb_941277.png "Screenshot_2018-07-28-14-13-01-628_top.yokey.shopwt.png")