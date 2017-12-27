//
//  ChengXinEnterpriseReportViewController.m
//  chengxin
//
//  Created by common on 4/15/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChengXinEnterpriseReportViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "WebViewController.h"
#import "HomeFamiliarDetailViewController.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"

@interface ChengXinEnterpriseReportViewController ()

@end

@implementation ChengXinEnterpriseReportViewController
{
    NSDictionary* inviterInfo;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    [GeneralUtil showProgress];
    self.lblManagerName.text = [CommonData sharedInstance].userInfo[@"bossName"];
    self.lblMainBusiness.text = [CommonData sharedInstance].userInfo[@"bossJob"];
    self.lblManagerPosition.text = [CommonData sharedInstance].userInfo[@"mainJob"];
    self.lblManagerPhoneNumber.text = [CommonData sharedInstance].userInfo[@"bossMobile"];
    self.lblManagerWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
    self.lblOfficeAddress.text = [NSString stringWithFormat:@"%@ %@ %@", [CommonData sharedInstance].userInfo[@"provinceName"], [CommonData sharedInstance].userInfo[@"cityName"], [CommonData sharedInstance].userInfo[@"addr"]];

    self.lblCompanyInfo.text = [CommonData sharedInstance].userInfo[@"comment"];
    self.lblRecommend.text = [CommonData sharedInstance].userInfo[@"recommend"];
    self.lblCompanyURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
    
    self.lblName.text = [CommonData sharedInstance].userInfo[@"enterName"];

    [self.btnBusiness setTitle:[CommonData sharedInstance].userInfo[@"xyName"] forState:UIControlStateNormal];
    self.lblCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
    self.lblCodeNumber.text = [CommonData sharedInstance].userInfo[@"code"];
    if([[CommonData sharedInstance].userInfo[@"credit"] longValue] == 0) {
        self.lblChengHuDu.text = @"诚信度: 暂无";
    }else{
        self.lblChengHuDu.text = [NSString stringWithFormat:@"诚信度: %@%@", [[CommonData sharedInstance].userInfo[@"credit"] stringValue], @"%"];
    }
    
    self.lblFeedback.text = [NSString stringWithFormat:@"评价: %@", [[CommonData sharedInstance].userInfo[@"feedbackCnt"] stringValue]];
    self.lblDianZan.text = [NSString stringWithFormat:@"点赞: %@", [[CommonData sharedInstance].userInfo[@"electCnt"] stringValue]];
    //self.lblAddress.text = [CommonData sharedInstance].userInfo[@"addr"];
    //self.lblCompanyWebURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
    //self.lblWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
    //self.lblPosition.text = [CommonData sharedInstance].userInfo[@"job"];
    //self.lblWorkExperience.text = [CommonData sharedInstance].userInfo[@"experience"];

    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]]];
    
    [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
    
    url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"enterCertImage"]]];
    [self.businessPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"bg_pic.png"]];
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_GETINVITERINFO forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETINVITERINFO Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                inviterInfo = dicRes[@"inviterInfo"];
                
                if(inviterInfo == nil)
                {
                    self.recommendView.hidden = YES;
                    [self.scrollView setContentSize:CGSizeMake(SCREEN_WIDTH, 750)];
                    return;
                }else
                {
                    self.recommendView.hidden = NO;
                    [self.scrollView setContentSize:CGSizeMake(SCREEN_WIDTH, 919)];
                }
                
                [[CommonData sharedInstance] setSelectedFriendAccountID:inviterInfo[@"id"]];
                
                if([inviterInfo[@"akind"] intValue] == 1)
                {
                    [self.markImage setImage:[UIImage imageNamed:@"personal"]];
                    self.lblRecommenderName.text = [inviterInfo objectForKey:@"realname"];
                    if(self.lblRecommenderName.text.length == 0)
                        self.lblRecommenderName.text = inviterInfo[@"mobile"];
                }else if([inviterInfo[@"akind"] intValue] == 2)
                {
                    [self.markImage setImage:[UIImage imageNamed:@"office"]];
                    self.lblRecommenderName.text = inviterInfo[@"enterName"];
                }

                self.lblRecommenderCode.text = [inviterInfo objectForKey:@"code"];
                self.lblRecommenderDianZan.text = [[inviterInfo objectForKey:@"electCnt"] stringValue];
                self.lblRecommenderChengHuDu.text = self.lblRecommenderChengHuDu1.text = [NSString stringWithFormat:@"%@%@", [inviterInfo[@"credit"] stringValue], @"%"];
                if([inviterInfo[@"credit"] longValue] == 0) {
                    self.lblRecommenderChengHuDu.text = self.lblRecommenderChengHuDu1.text = @"暂无";
                }
                [self.btnRecommenderBusiness setTitle:inviterInfo[@"xyName"] forState:UIControlStateNormal];
                self.lblRecommenderPingJia.text = [[inviterInfo objectForKey:@"feedbackCnt"] stringValue];
                
                NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]]];
                [self.recommenderPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
            }
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)onURL:(id)sender
{
    if(self.lblCompanyURL.text.length != 0)
    {
        NSURL* url = [NSURL URLWithString:self.lblCompanyURL.text];
        if([[url absoluteString] containsString:@"http"] == false)
        {
            url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
        }
        [[UIApplication sharedApplication] openURL:url];
        /*
        WebViewController* vc = [[WebViewController alloc] initWithNibName:@"WebViewController" bundle:nil];
        vc.webUrl = self.lblCompanyURL.text;
        [self.navigationController pushViewController:vc animated:YES];
         */
    }
}

-(IBAction)onInviter:(id)sender
{
    if(inviterInfo == nil)
        return;
    
    int nTestStatus = [inviterInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [appDelegate.window makeToast:@"未认证的熟人／企业"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    
    [CommonData sharedInstance].selectedFriendAccountID = [inviterInfo objectForKey:@"id"];
    HomeFamiliarDetailViewController* vc = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)onShareAction:(id)sender {
    [self shareMenu];
}

#pragma mark ShareSDK
- (void)shareMenu
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    NSArray* imageArray = @[[[NSBundle mainBundle] pathForResource:@"logo_new@2x" ofType:@"png"]];
    long accountID = [[CommonData sharedInstance].userInfo[@"id"] longValue];
    
    NSString *url = @"";
    NSString *shareText = @"";
    NSString *realName = @"";
    NSString *shareTitle = @"";
    NSString *credit = [[CommonData sharedInstance].userInfo[@"credit"] stringValue];
    NSString *positiveFeedbackCnt = [[CommonData sharedInstance].userInfo[@"positiveFeedbackCnt"] stringValue];
    NSString *negativeFeedbackCnt = [[CommonData sharedInstance].userInfo[@"negativeFeedbackCnt"] stringValue];
    
    NSInteger akind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
    if (akind == PERSONAL_KIND) {
        url = [NSString stringWithFormat:@"%@%@%ld%s%ld", BASE_WEB_URL, @"/report_geren.html?accountId=",accountID,"&shareUserId=",accountID];
        realName = [CommonData sharedInstance].userInfo[@"realname"];
        shareTitle = @"【诚信报告】您的好友给您分享了一个诚信报告，立即查看！";
        shareText = [NSString stringWithFormat:@"%@，诚信度%@%%, %@个正面评价, %@个负面评价，查看完整诚信报告！", realName,credit,positiveFeedbackCnt,negativeFeedbackCnt];
    }
    else {
        url = [NSString stringWithFormat:@"%@%@%ld%s%ld", BASE_WEB_URL, @"/report_qiye.html?accountId=",accountID,"&shareUserId=",accountID];
        realName = [CommonData sharedInstance].userInfo[@"enterName"];
        shareTitle = @"【诚信报告】您的好友给您分享了一份企业诚信报告，立即查看！";
        shareText = [NSString stringWithFormat:@"%@, 诚信度%@%%, %@个正面评价, %@个负面评价，查看完整诚信报告！", realName,credit,positiveFeedbackCnt,negativeFeedbackCnt];
    }
    [shareParams SSDKSetupShareParamsByText:shareText
                                     images:imageArray
                                        url:[NSURL URLWithString:url]
                                      title:shareTitle
                                       type:SSDKContentTypeWebPage];
    
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
                           UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享成功"
                                                                               message:nil
                                                                              delegate:nil
                                                                     cancelButtonTitle:@"确定"
                                                                     otherButtonTitles:nil];
                           [alertView show];
                           [self onCallForStatics];
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
                           break;
                       }
                       default:
                           break;
                   }
               }];
}

- (void)onCallForStatics {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"onShare" forKey:@"pAct"];
    [dicParams setObject:@"6" forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].userInfo[@"id"] forKey:@"id"];
    [dicParams setObject:@"1" forKey:@"share"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_ONSHARE Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
}
@end
