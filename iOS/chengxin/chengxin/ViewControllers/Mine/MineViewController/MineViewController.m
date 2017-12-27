//
//  MineViewController.m
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "MineViewController.h"
#import "RequestFriendViewController.h"
#import "RealNameAuthenticationViewController.h"
#import "ChengXinRecordViewController.h"
#import "ChengXinReportViewController.h"
#import "SearchProductsViewController.h"
#import "SettingViewController.h"
#import "EvaluateViewController.h"
#import "EvalAwakenViewController.h"
#import "MyJiuCuoViewController.h"
#import "Global.h"
#import "ChengXinEnterpriseReportViewController.h"
#import "UIImageView+WebCache.h"
#import "OpinionViewController.h"
#import "FavouriteListViewController.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"

@interface MineViewController ()

@end

@implementation MineViewController
@synthesize messageNumberLabel;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.automaticallyAdjustsScrollViewInsets = NO;
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    int akind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
    if(akind == 1)
    {
        self.lblName.text = [CommonData sharedInstance].userInfo[@"realname"];
        if(self.lblName.text.length == 0)
        {
            self.lblName.text = [CommonData sharedInstance].userInfo[@"mobile"];
        }
    }else
    {
        self.lblName.text = [CommonData sharedInstance].userInfo[@"enterName"];
        if(self.lblName.text.length == 0)
        {
            self.lblName.text = [CommonData sharedInstance].userInfo[@"mobile"];
        }
    }
    
    self.lblCodeNumber.text = [CommonData sharedInstance].userInfo[@"code"];
    if(self.lblCodeNumber.text.length == 0) {
        self.lblCodeNumber.text = @"请认证";
    }
    if([[CommonData sharedInstance].userInfo[@"credit"] longValue] == 0) {
        self.lblChengHuDu.text = @"诚信度: 暂无";
    }else{
        self.lblChengHuDu.text = [NSString stringWithFormat:@"诚信度: %ld%%", (long)[[CommonData sharedInstance].userInfo[@"credit"] integerValue]];
    }
    self.lblPingJia.text = [NSString stringWithFormat:@"评价: %@", [[CommonData sharedInstance].userInfo[@"feedbackCnt"] stringValue] ];
    self.lblDianZan.text = [NSString stringWithFormat:@"点赞: %@", [[CommonData sharedInstance].userInfo[@"electCnt"] stringValue]];
    
    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]]];
    [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"add_touxiang1.png"]];
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction
- (IBAction)onShowRequestFriendAction:(id)sender {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_INVITEFRIEND forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_INVITEFRIEND Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [CommonData sharedInstance].requestCode = dicRes[@"reqCode"];
                [self shareMenu];
            }
            else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else
        {
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
    //RequestFriendViewController *requestFriendVC = [[RequestFriendViewController alloc] initWithNibName:@"RequestFriendViewController" bundle:nil];
    //[self.navigationController pushViewController:requestFriendVC animated:YES];
}

- (IBAction)showNotificationView:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
}

#pragma mark - Notification Delegate
- (void)updateNotification {
    if([CommonData sharedInstance].notificationCount > 0) {
        NSString *strNotificationCount = [NSString stringWithFormat:@"%ld", [CommonData sharedInstance].notificationCount];
        messageNumberLabel.text = strNotificationCount;
        messageNumberLabel.hidden = NO;
    }else{
        messageNumberLabel.hidden = YES;
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
-(IBAction)onRealNameAuthentication:(id)sender
{
    RealNameAuthenticationViewController* realnameVC = [[RealNameAuthenticationViewController alloc] initWithNibName:@"RealNameAuthenticationViewController" bundle:nil];
    [self.navigationController pushViewController:realnameVC animated:YES];
}
-(IBAction)onChengXinReport:(id)sender
{
    if([[CommonData sharedInstance].userInfo[@"akind"] intValue] == 1)
    {
        ChengXinReportViewController *vc = [[ChengXinReportViewController alloc] initWithNibName:@"ChengXinReportViewController" bundle:nil];
        [self.navigationController pushViewController:vc animated:YES];
    }else if([[CommonData sharedInstance].userInfo[@"akind"] intValue] == 2)
    {
        ChengXinEnterpriseReportViewController *vc = [[ChengXinEnterpriseReportViewController alloc] initWithNibName:@"ChengXinEnterpriseReportViewController" bundle:nil];
        [self.navigationController pushViewController:vc animated:YES];
    }
}
-(IBAction)onChengXinRecord:(id)sender
{
    ChengXinRecordViewController *vc = [[ChengXinRecordViewController alloc] initWithNibName:@"ChengXinRecordViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

-(IBAction)onMyRating:(id)sender
{
    EvaluateViewController *vc = [[EvaluateViewController alloc] initWithNibName:@"EvaluateViewController" bundle:nil];
    vc.bIsDetail = YES;
    vc.estimationType = em_MyEstimation;
    [self.navigationController pushViewController:vc animated:YES];
}

-(IBAction)onRatingToMe:(id)sender
{
    EvaluateViewController *vc = [[EvaluateViewController alloc] initWithNibName:@"EvaluateViewController" bundle:nil];
    vc.bIsDetail = YES;
    vc.estimationType = em_EstimationToMe;
    [self.navigationController pushViewController:vc animated:YES];
    //EvalAwakenViewController *vc = [[EvalAwakenViewController alloc] initWithNibName:@"EvalAwakenViewController" bundle:nil];
    //[self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onFavouriteList:(id)sender
{
    FavouriteListViewController* vc = [[FavouriteListViewController alloc] initWithNibName:@"FavouriteListViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onMyPublication:(id)sender
{
    SearchProductsViewController* spVC = [[SearchProductsViewController alloc] initWithNibName:@"SearchProductsViewController" bundle:nil];
    [self.navigationController pushViewController:spVC animated:YES];
}
-(IBAction)onOpinion:(id)sender
{
    OpinionViewController* vc = [[OpinionViewController alloc] initWithNibName:@"OpinionViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onSetting:(id)sender
{
    SettingViewController* svVC = [[SettingViewController alloc] initWithNibName:@"SettingViewController" bundle:nil];
    [self.navigationController pushViewController:svVC animated:YES];
}
-(IBAction)onJiuCuo:(id)sender
{
    MyJiuCuoViewController* vc = [[MyJiuCuoViewController alloc] initWithNibName:@"MyJiuCuoViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onPhoto:(id)sender
{
    RealNameAuthenticationViewController* vc = [[RealNameAuthenticationViewController alloc] initWithNibName:@"RealNameAuthenticationViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark ShareSDK
- (void)shareMenu
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    NSArray* imageArray = @[[[NSBundle mainBundle] pathForResource:@"logo_new@2x" ofType:@"png"]];
    NSString *url = [NSString stringWithFormat:@"%@%@%@%@%@", BASE_WEB_URL, @"/invite.html?reqcode=", [CommonData sharedInstance].requestCode,@"&shareUserId=",[CommonData sharedInstance].userInfo[@"id"]];
    [shareParams SSDKSetupShareParamsByText:@"准备好了吗？诚信实时评价，你的话语权你做主！评价有回报，共建社会诚信生态系统，加入我们吧！"
                                     images:imageArray
                                        url:[NSURL URLWithString:url]
                                      title:@"沟通太难？产品不靠谱？用“诚乎”，找诚信人，拒绝忽悠！"
                                       type:SSDKContentTypeWebPage];
    //优先使用平台客户端分享
    //    [shareParams SSDKEnableUseClientShare];
    //设置微博使用高级接口
    //2017年6月30日后需申请高级权限
    //    [shareParams SSDKEnableAdvancedInterfaceShare];
    //    设置显示平台 只能分享视频的YouTube MeiPai 不显示
    //    NSArray *items = @[
    //                       @(SSDKPlatformTypeFacebook),
    //                       @(SSDKPlatformTypeFacebookMessenger),
    //                       @(SSDKPlatformTypeInstagram),
    //                       @(SSDKPlatformTypeTwitter),
    //                       @(SSDKPlatformTypeLine),
    //                       @(SSDKPlatformTypeQQ),
    //                       @(SSDKPlatformTypeWechat),
    //                       @(SSDKPlatformTypeSinaWeibo)
    //                       ];
    
    //设置简介版UI 需要  #import <ShareSDKUI/SSUIShareActionSheetStyle.h>
    //    [SSUIShareActionSheetStyle setShareActionSheetStyle:ShareActionSheetStyleSimple];
    //    [ShareSDK setWeiboURL:@"http://www.mob.com"];
    [ShareSDK showShareActionSheet:self.view
                             items:[MOBShareSDKHelper shareInstance].platforems
                       shareParams:shareParams
               onShareStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
                   
                   switch (state) {
                           
                       case SSDKResponseStateBegin:
                       {
                           //设置UI等操作
                           break;
                       }
                       case SSDKResponseStateSuccess:
                       {
                           //Instagram、Line等平台捕获不到分享成功或失败的状态，最合适的方式就是对这些平台区别对待
                           if (platformType == SSDKPlatformTypeInstagram)
                           {
                               break;
                           }
                           
                           UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享成功"
                                                                               message:nil
                                                                              delegate:nil
                                                                     cancelButtonTitle:@"确定"
                                                                     otherButtonTitles:nil];
                           [alertView show];
                           break;
                       }
                       case SSDKResponseStateFail:
                       {
                           NSLog(@"%@",error);
                           UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"分享失败"
                                                                           message:[NSString stringWithFormat:@"%@",error]
                                                                          delegate:nil
                                                                 cancelButtonTitle:@"OK"
                                                                 otherButtonTitles:nil, nil];
                           [alert show];
                           break;
                       }
                       case SSDKResponseStateCancel:
                       {
//                           UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享已取消"
//                                                                               message:nil
//                                                                              delegate:nil
//                                                                     cancelButtonTitle:@"确定"
//                                                                     otherButtonTitles:nil];
//                           [alertView show];
                           break;
                       }
                       default:
                           break;
                   }
               }];
}

@end
