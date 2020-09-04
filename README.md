## OptionBarView

[![](https://jitpack.io/v/DMingOu/OptionBarView.svg)](https://jitpack.io/#DMingOu/OptionBarView)   ![](http://picbed-dmingou.oss-cn-shenzhen.aliyuncs.com/img/platform-Android-lightgrey.svg)  ![](http://picbed-dmingou.oss-cn-shenzhen.aliyuncs.com/img/license-MIT-000000.svg)   ![](https://img.shields.io/badge/author-DMingOu-blue.svg)

## 项目简介
**一个简单方便高度可定制的条目设置选项类型的View**

### 为什么想要封装一个这样的View？

在做项目的过程中发现经常地要写各种各样的点击选项的条目，常见的"设置页"的条目，一般的做法是每写一个条目选项就要写一个布局然后里面配置一堆的View，虽然也能完成效果，但是如果数量很多或者设计图效果各异就会容易出错，同时一个页面如果有过多的布局嵌套也会影响效率。

于是，我开始找一些定制性高且内部不需要布局就能完成的框架。最后，我找到了由[GitLqr](GitLqr)作者开发的[LQROptionItemView](https://github.com/GitLqr/LQROptionItemView)，大体满足需求，在此非常感谢作者[GitLqr](GitLqr)，但是在使用过程中发现几个小问题：

- **图片均不能设置宽度和高度** 
- **图片不支持直接设置Vector矢量资源**
- **不支持顶部/底部绘制分割线**

由于原作者的项目近几年好像都没有继续维护了，于是我打算自己动手改进以上的问题，并开源出来。

## 主要功能

- 绘制左、中、右侧的文字
- 绘制左、右侧的图片
- 设置顶部或底部的分割线
- 定制图片与文字的大小和距离

## 效果演示

下图列举了几种常见的条目效果，项目还支持更多不同的效果搭配。


<img src="http://picbed-dmingou.oss-cn-shenzhen.aliyuncs.com/img/image-20200904131126978.png" width="40%" height="40%">


## Gradle集成方式

[![](https://jitpack.io/v/DMingOu/OptionBarView.svg)](https://jitpack.io/#DMingOu/OptionBarView)

在Project 的 build.gradle

```
allprojects {
    repositories {
		...
        maven { url 'https://jitpack.io' }
    }
}
```

在Module 的 build.gradle

```
	dependencies {
	    implementation 'com.github.DMingOu:OptionBarView:1.0.0'
	}
```



## **快速上手**

### 1、在XML布局中使用

属性均可选，不设置的属性则不显示，⭐图片与文字的距离若不设置会有一个默认的距离，可设置任意类型的图片资源。

```xml
<com.dmingo.optionbarview.OptionBarView
	 android:id="@+id/opv_1"
	 android:layout_width="match_parent"
	 android:layout_height="60dp"
	 android:layout_marginTop="30dp"
	 android:background="@android:color/white"
	 app:left_image_margin_left="20dp"
	 app:left_src="@mipmap/ic_launcher"
	 app:left_src_height="24dp"
	 app:left_src_width="24dp"
	 app:left_text="左标题1"
	 app:left_text_margin_left="5dp"
	 app:left_text_size="16sp"
	 app:title="中间标题1"
	 app:title_size="20sp"
	 app:title_color="@android:color/holo_red_light"
	 app:right_image_margin_right="20dp"
	 app:right_src="@mipmap/ic_launcher"
	 app:right_src_height="20dp"
	 app:right_src_width="20dp"
	 app:right_text="右方标题1"
	 app:right_text_size="16sp"
	 app:show_divide_line="true"
	 app:divide_line_color="@android:color/black"
	 app:divide_line_left_margin="20dp"
	 app:divide_line_right_margin="20dp"/>
```

### 2、在Java代码里动态添加

方式与其他View相同，也是确定布局参数，通过api设置OptionBarView的属性，这里就不阐述了

### 3、条目点击事件

#### 整体点击模式

默认开启的是整体点击模式，可以通过`setSplitMode(false)`手动开启

```
opv2.setOnClickListener(new View.OnClickListener() {
  @Override
   public void onClick(View view) {
       Toast.makeText(MainActivity.this,"OptionBarView Click",Toast.LENGTH_LONG).show();
   }
});
```

#### 分区域点击模式

默认不会开启分区域点击模式，可以通过`setSplitMode(true)`开启，通过设置接口回调进行监听事件

```java
opv1.setSplitMode(true);
opv1.setOnOptionItemClickListener(new OptionBarView.OnOptionItemClickListener() {
   @Override
    public void leftOnClick() {
        Toast.makeText(MainActivity.this,"Left Click",Toast.LENGTH_SHORT).show();
    }
   @Override
   public void centerOnClick() {
        Toast.makeText(MainActivity.this,"Center Click",Toast.LENGTH_SHORT).show();
   }
   @Override
   public void rightOnClick() {
        Toast.makeText(MainActivity.this,"Right Click",Toast.LENGTH_SHORT).show();
   }
 });
```

### 4、API

```java
//中间标题
getTitleText()
setTitleText(String text)
setTitleText(int stringId)
setTitleColor(int color)
setTitleSize(int sp)

//左侧
getLeftText()
setLeftText(String text)
setLeftText(int stringId)
setLeftTextSize(int sp)
setLeftTextColor(int color)
setLeftTextMarginLeft(int dp)
setLeftImageMarginLeft(int dp)
setLeftImageMarginRight(int dp)
setLeftImage(Bitmap bitmap)
showLeftImg(boolean flag)
showLeftText(boolean flag)
setLeftImageWidthHeight(int width, int Height)

//右侧
getRightText()
setRightImage(Bitmap bitmap)
setRightText(String text)
setRightText(int stringId)
setRightTextColor(int color)
setRightTextSize(int sp)
setRightTextMarginRight(int dp)
setRightImageMarginLeft(int dp)
setRightImageMarginRight(int dp)
showRightImg(boolean flag)
showRightText(boolean flag)
setRightImageWidthHeight(int width, int Height)

//点击模式
setSplitMode(boolean splitMode)
getSplitMode()

//分割线
getIsShowDivideLine()
setShowDivideLine(Boolean showDivideLine)
setDivideLineColor(int divideLineColor)
```

### 5、特殊属性说明

主要是对一些图片文字的距离属性的说明。看图就能明白了。
<img src="http://picbed-dmingou.oss-cn-shenzhen.aliyuncs.com/img/snipaste_20200904_134534.png" width="40%" height="40%">

## 混淆

```
-dontwarn com.dmingo.optionbarview.*
-keep class com.dmingo.optionbarview.*{*;}
```

## 联系方式

- Gmail: [dmingou0529@gmail.com](mailto:dmingou0529@gmail.com)

- QQ Email: [758502274@qq.com](mailto:758502274@qq.com)

- QQ: 758502274


## License

```
MIT License

Copyright (c) 2020 DMingOu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


