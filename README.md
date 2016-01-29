/*
Title: meChat
Description: meChat
*/

<ul id="tab" class="clearfix">
	<li class="active"><a href="#method-content">Method</a></li>
</ul>
<div id="method-content">

<div class="outline">
[initMeChat](#1)
[setTitleColor](#2)
[setTitleBarColor](#3)
[show](#4)
[setScheduledAgentOrAgentGroup](#5)
[setClientInfo](#6)
</div>

#**概述**

美洽是一款实现手机用户与企业保持随时随刻沟通的客服工具。本模块封装了美洽的相关接口。使用此模块之前需要先注册美洽获取appkey。

![图片说明](https://s3.cn-north-1.amazonaws.com.cn/pics.meiqia.bucket/4f2e9f86df2fd5f8)

**注册方法如下:**

使用**管理员权限**账号登陆[美洽](https://app.meiqia.com/login)，在 **设置 --> SDK** 页面中，选择 **添加 App 配置** ，根据提示配置App信息，然后添加 APP 即可得到 `appkey` 用于配置。

注意：本模块在ios上支持最低版本为6.0

#开源地址

模块源代码：[https://github.com/Meiqia/MeiQia_For_APICloud](https://github.com/Meiqia/MeiQia_For_APICloud)

模块中的美洽UI源代码：[https://github.com/Meiqia/MeiqiaSDK-iOS](https://github.com/Meiqia/MeiqiaSDK-iOS)

#**initMeiQia**<div id="1"></div>
	
初始化美洽

initMeiQia(params, callback)

##params

appkey：
- 类型：字符串
- 默认值：无
- 描述：注册美洽后，从美洽后台获得的appkey，不可为空

callback:
- 类型：方法
- 默认值：无
- 描述：初始化成功将返回顾客ID（ClientId），初始化失败将返回错误描述

##示例代码

```js
//创建美洽
var mq = api.require('meiQia');

//配置初始化美洽需要的appkey
var param = {
    appkey:"33c19c19bd858746b878fcc93166f2e5"
};
        
//初始化美洽
mq.initMeiQia(param, function (ret, err){
    if (ret) {
	    //初始化成功
        alert(JSON.stringify(ret));
    }else{
	    //初始化失败
        alert(JSON.stringify(err));
    }
})
```

##补充说明

必须在初始化后才能正常使用此模块的其他方法，所以建议将初始化放在App启动时执行。美洽模块只需要初始化一次。

##可用性

iOS系统，Android系统

可提供的3.0.0及更高版本

#**setTitleColor**<div id="2"></div>

设置美洽聊天界面的标题栏中文字的颜色
setTitleColor(params)

##示例代码
```js
var mq = api.require('meiQia');
//设置title以及按钮颜色
var titleColor = {
    color:"#ffffff"
};
mq.setTitleColor(titleColor);
```

##补充说明
参数需要是`#FFFFFF`格式的HTML颜色。

#**setTitleBarColor**<div id="3"></div>

设置美洽聊天界面的标题栏背景颜色
setTitleBarColor(params)

##示例代码
```js
var mq = api.require('meiQia');
//设置标题栏背景颜色
var titleBarColor = {
    color:"#00ff00"
};
mq.setTitleBarColor(titleBarColor);
```

##补充说明
参数需要是`#FFFFFF`格式的HTML颜色。

#**show**<div id="4"></div>

弹出美洽聊天界面

show()

##示例代码
```js
var mq = api.require('meiQia');
mq.show();
```

##补充说明
如果需要指定客服`setScheduledAgentOrAgentGroup()`或添加自定义信息`setClientInfo()`，需要执行`show()`前执行。

##可用性

iOS系统，Android系统

可提供的3.0.0及更高版本


#**setScheduledAgentOrAgentGroup**<div id="5"></div>

指定分配客服与客服组

setScheduledAgentOrAgentGroup(params)

##params

agentId：

- 类型：字符串
- 默认值：无
- 描述：在美洽系统中客服对应的ID

agentGroup：

- 类型：字符串
- 默认值：无
- 描述：在美洽系统中客服组对应的ID

scheduleRule:

 - 类型：字符串 
 - 默认值：enterprise 
 - 描述：
	 - none：不转接给任何人，让用户留言
	 - group: 转接给组内的人
	 - enterprise: 转接给企业其他随机一个人

##示例代码

```js
var mq = api.require('meiQia');
//设置指定分配给某客服,并且如果客服不在线，则留言而不转接给其他客服
var scheduleParam = {
    agentId: "ed55383a0fa82bbe8242ee16477c9ac3",
    scheduleRule: "none"
};
mq.setScheduledAgentOrAgentGroup(scheduleParam);

```

##补充说明

agentId和agentGroup可只传其中一个，也可同时都传。美洽系统将优先分配指定客服，如果客服不在线，则分配到指定的客服组，如果客服组也无人在线，则分配到全部客服。如果使用该接口，那么需要在show之前调用

##可用性

iOS系统，Android系统

可提供的3.0.0及更高版本


#**setClientInfo**<div id="6"></div>

效果图：

![效果图](https://s3.cn-north-1.amazonaws.com.cn/pics.meiqia.bucket/7d60384c6f4663d0)

添加规范化用户信息

setClientInfo(params)

##params

|默认参数|描述|
|----|----|
|avatar|头像 URL|
|address|地址|
|age|年龄|
|comment|备注|
|email|邮箱|
|gender|性别|
|name|名字|
|qq|QQ|
|tel|电话|
|weibo|微博|
|weixin|微信|
|source|顾客来源|
|tags|标签，数组形式。且必须是企业中**已经存在的标签**|

##示例代码

```JS
var mq = api.require('meiQia');
//设置用户信息
var infoParam = {
    email: "dev@meiqia.com",
    comment: "这是备注",
    avatar: "https://app.meiqia.com/images/logo.png",
    tags: ["付费用户","使用疑问"]
};
mq.setClientInfo(infoParam);
```

##补充说明

自定义用户信息将会被传送到美洽服务端，用于对话时显示给客服人员一作参考。这些参数都是可选的，可以选择其中的一个或者多个传递。此接口必须在show之前执行。

可用性

iOS系统，Android系统

可提供的3.0.0及更高版本
