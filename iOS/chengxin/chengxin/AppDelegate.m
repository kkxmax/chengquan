//
//  AppDelegate.m
//  chengxin
//
//  Created by seniorcoder on 10/23/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "AppDelegate.h"
#import "MainViewController.h"
#import "LoginViewController.h"
#import "ForgotPasswordViewController.h"
#import "SignupSuccessViewController.h"
#import "FavouriteViewController.h"
#import "FavouritesTableViewController.h"
#import "MineViewController.h"
#import "LoginViewController.h"
#import "Global.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKConnector/ShareSDKConnector.h>
#import <TencentOpenAPI/TencentOAuth.h>
#import <TencentOpenAPI/QQApiInterface.h>
#import "WXApi.h"
#import "WeiboSDK.h"
#import <Fabric/Fabric.h>
#import <Crashlytics/Crashlytics.h>

@interface AppDelegate ()
{
    NSTimer *notificationTimer;
}
@end

@implementation AppDelegate
@synthesize notificationDelegate;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [Fabric with:@[[Crashlytics class]]];
    // Keyboard show under textfield.
    self.isAccountDuplicated = false;
    [IQKeyboardManager sharedManager].enable = YES;

    UINavigationController *navVC = [[UINavigationController alloc] init];
    self.navController = navVC;
    // Override point for customization after application launch.
    LoginViewController *loginVC;
    if(IS_IPHONE_5_OR_LESS)
    {
        loginVC = [[LoginViewController alloc] initWithNibName:@"LoginViewController_iphone4" bundle:nil];
    }else
    {
        loginVC = [[LoginViewController alloc] initWithNibName:@"LoginViewController" bundle:nil];
    }
    
    notificationTimer = [NSTimer scheduledTimerWithTimeInterval:NOTIFICATION_SECOND target:self selector:@selector(GetNotificationFromServer) userInfo:nil repeats:YES];
    //MainViewController *mainVC = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
    [navVC pushViewController:loginVC animated:NO];
    self.window.rootViewController = navVC;
    [self.window addSubview:navVC.view];
    [self.window makeKeyAndVisible];


//    [ShareSDK registerActivePlatforms:@[
//                                        @(SSDKPlatformTypeSinaWeibo),
//                                        @(SSDKPlatformTypeMail),
//                                        @(SSDKPlatformTypeSMS),
//                                        @(SSDKPlatformTypeCopy),
//                                        @(SSDKPlatformTypeWechat),
//                                        @(SSDKPlatformTypeQQ)]
//                             onImport:^(SSDKPlatformType platformType) {
//                                 switch (platformType) {
//                                     case SSDKPlatformTypeWechat:
//                                         [ShareSDKConnector connectWeChat:[WXApi class]];
//                                         break;
//                                     case SSDKPlatformTypeQQ:
//                                         [ShareSDKConnector connectQQ:[QQApiInterface class] tencentOAuthClass:[TencentOAuth class]];
//                                     case SSDKPlatformTypeSinaWeibo:
//                                         [ShareSDKConnector connectWeibo:[WeiboSDK class]];
//                                     default:
//                                         break;
//                                 }
//                             } onConfiguration:^(SSDKPlatformType platformType, NSMutableDictionary *appInfo) {
//                                 switch (platformType) {
//                                     case SSDKPlatformTypeSinaWeibo:
//                                         [appInfo SSDKSetupSinaWeiboByAppKey:@"" appSecret:@"" redirectUri:@"" authType:SSDKAuthTypeBoth];
//                                         break;
//                                     case SSDKPlatformTypeWechat:
//                                         [appInfo SSDKSetupWeChatByAppId:@"" appSecret:@""];
//                                         break;
//                                     case SSDKPlatformTypeQQ:
//                                         [appInfo SSDKSetupQQByAppId:@"" appKey:@"" authType:SSDKAuthTypeBoth];
//                                         break;
//                                     default:
//                                         break;
//                                 }
//                             }];
    
    return YES;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)GetNotificationFromServer {
    if([[CommonData sharedInstance].tokenName isEqualToString:@""])
        return;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0),^{
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"getNoticeCount" forKey:@"pAct"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        dispatch_async(dispatch_get_main_queue(), ^{
            [[WebAPI sharedInstance] sendPostRequest:ACTION_GETNOTICECOUNT Parameters:dicParams :^(NSObject *resObj) {
                NSDictionary *dicRes = (NSDictionary *)resObj;
                if (dicRes != nil ) {
                    if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                        [CommonData sharedInstance].notificationCount = [dicRes[@"totalCnt"] longValue];
                        [CommonData sharedInstance].myEstimateCnt = [dicRes[@"myEstimateCnt"] longValue];
                        [CommonData sharedInstance].estimateToMeCnt = [dicRes[@"estimateToMeCnt"] longValue];
                        [notificationDelegate updateNotification];
                    }
                }
            }];
        });
    });
}

@end
