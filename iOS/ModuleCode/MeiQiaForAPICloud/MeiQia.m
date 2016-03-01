//
//  MeiQia.m
//  UZApp
//
//  Created by Injoy on 16/1/6.
//  Copyright © 2016年 APICloud. All rights reserved.
//

#import "MeiQia.h"

@implementation MeiQia
{
    NSInteger receiveMessageCbId;
    
    NSString *loginCustomizedId;
    NSString *loginMQClientId;
    UIColor *setTitleBarColor;
    UIColor *titleColor;
    
    NSDictionary* clientInfo;
}

- (id)initWithUZWebView:(UZWebView *)webView_ {
    if (self = [super initWithUZWebView:webView_]) {
        [theApp addAppHandle:self];
    }
    return self;
}

- (void)dispose {
    [theApp removeAppHandle:self];
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    [MQManager closeMeiqiaService];
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    [MQManager openMeiqiaService];
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    [MQManager registerDeviceToken:deviceToken];
}

/**
 *  初始化美洽
 */
- (void)initMeiQia:(NSDictionary *)paramDict {
    NSInteger cbId = [paramDict integerValueForKey:@"cbId" defaultValue:-1];
    NSString *appkey = paramDict[@"appkey"];
 
    //初始化SDK
    [MQManager initWithAppkey:appkey completion:^(NSString *clientId, NSError *error) {
        if (error) {
            [self sendResultEventWithCallbackId:cbId dataDict:nil errDict:@{@"info": error.domain} doDelete:YES];
            return;
        }
        //初始化成功，返回clientId
        [self sendResultEventWithCallbackId:cbId dataDict:@{@"clientId": clientId} errDict:nil doDelete:YES];
    }];
}

/**
 *  设置用户信息
 */
- (void)setClientInfo:(NSDictionary *)paramDict {
    clientInfo = paramDict;
}

/**
 *  指定分配客服
 */
- (void)setScheduledAgentOrAgentGroup:(NSDictionary *)paramDict
{
    NSString *agentId = paramDict[@"agentId"];
    NSString *AgentGroup = paramDict[@"agentGroup"];
    NSString *scheduleRule = paramDict[@"scheduleRule"];
    
    //转接逻辑
    MQScheduleRules rules = MQScheduleRulesRedirectEnterprise;
    if (scheduleRule) {
        if ([scheduleRule isEqualToString:@"none"]) {
            rules = MQScheduleRulesRedirectNone;
        }else if ([scheduleRule isEqualToString:@"group"]) {
            rules = MQScheduleRulesRedirectGroup;
        }else if ([scheduleRule isEqualToString:@"enterprise"]) {
            rules = MQScheduleRulesRedirectEnterprise;
        }
    }
    
    //指定分配
    [MQManager setScheduledAgentWithAgentId:agentId agentGroupId:AgentGroup scheduleRule:rules];
}

/**
 *  设置用户的在美洽的ClientId
 */
- (void)setLoginMQClientId:(NSDictionary *)paramDict
{
    NSString *_id = paramDict[@"id"];
    if (_id) {
        loginMQClientId = _id;
    }
}

/**
 *  设置自定义的用户ID
 */
- (void)setLoginCustomizedId:(NSDictionary *)paramDict
{
    NSString *_id = paramDict[@"id"];
    if (_id) {
        loginCustomizedId = _id;
    }
}

/**
 *  设置NavigationBar的TintColor
 */
- (void)setTitleColor:(NSDictionary *)paramDict
{
    NSString *colorStr = paramDict[@"color"];
    titleColor = [UZAppUtils colorFromNSString:colorStr];
}

/**
 *  设置NavigationBar的backgroundColor
 */
- (void)setTitleBarColor:(NSDictionary *)paramDict
{
    NSString *colorStr = paramDict[@"color"];
    setTitleBarColor = [UZAppUtils colorFromNSString:colorStr];
}


- (void)show:(NSDictionary *)paramDict {
    NSString *type = paramDict[@"type"];
    if (!type) type = @"";
    
    //监听用户上线的通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(clientOnlineSuccess) name:MQ_CLIENT_ONLINE_SUCCESS_NOTIFICATION object:nil];
    
    MQChatViewManager *chatViewManager = [MQChatViewManager new];
    //设置配置
    [chatViewManager setNavigationBarColor:setTitleBarColor];
    [chatViewManager setNavigationBarTintColor:titleColor];
    [chatViewManager setLoginMQClientId:loginMQClientId];
    [chatViewManager setLoginCustomizedId:loginCustomizedId];
    [chatViewManager enableOutgoingAvatar:NO];
    
    
    //默认使用push
    if ([type.lowercaseString isEqualToString:@"present"]) {
        [chatViewManager presentMQChatViewControllerInViewController:self.viewController];
    }else{
        MQChatViewController *viewController = [chatViewManager pushMQChatViewControllerInViewController:self.viewController];
        viewController.slidBackEnabled = YES;
        [viewController.navigationController setNavigationBarHidden:NO];
    }
}

- (void)clientOnlineSuccess
{
    //上线成功后，上传自定义信息
    if (clientInfo) {
        [MQManager setClientInfo:clientInfo completion:^(BOOL success, NSError *error) {
            clientInfo = nil;
        }];
    }
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:MQAudioPlayerDidInterruptNotification object:nil];
}

@end
