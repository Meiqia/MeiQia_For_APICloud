# MeiQia_For_APICloud
#类介绍介绍
- meiQia 业务实现
- meiQiaUI 美洽客服页面 

#快速使用流程

#函数介绍
## meiQia
### initMeiQia
说明：初始化美洽。只有成功初始化后才能正常使用美洽模块的其他功能
参数：
- appkey:appkey
- 初始化结果的回调函数

回调参数：
- 初始化成功:顾客ID（ClientID）
- 初始化失败:异常原因

### setClientInfo
说明：设置顾客信息，这些信息将会提供给客服查看
参数：
- 键和值为字符串的json

### setScheduledAgentOrAgentGroup
说明：制定分配客服或客服分组
参数：
- agentId:客服ID。可为空。
- agentGroup:客服分组ID。可为空。
- scheduleRule:该客服/客服组不在线时，转接顾客的规则。可为空。
 - none:不转接给任何人
 - group:转接给组内的人
 - enterprise:转接给企业其他随机一个人

### setLoginCustomizedId
说明：设置登录客服的开发者自定义id，设置该id后，聊天将会以该自定义id的顾客上线
参数：
- id:字符串

### setLoginMQClientId
说明：设置登录美洽客服的顾客的id，设置该id后，聊天将会以该顾客id的顾客上线
参数：
- clientId:美洽顾客的ID

### setTitleColor
说明：设置标题文字颜色
参数：
- color

### setTitleBarColor
说明：设置标题栏背景颜色
参数：
- color

### show
说明：显示美洽客服界面
参数：
- type：iOS平台上，弹出客服界面的方式。默认为push
 - push
 - present
- 关闭界面时的回调函数
