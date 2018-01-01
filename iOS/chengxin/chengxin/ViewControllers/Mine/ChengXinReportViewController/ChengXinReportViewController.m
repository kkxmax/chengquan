//
//  ChengXinReportViewController.m
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChengXinReportViewController.h"
#import "WebAPI.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "HomeFamiliarDetailViewController.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"


@interface ChengXinReportViewController ()

@end

@implementation ChengXinReportViewController
{
    NSDictionary* inviterInfo;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_GETINVITERINFO forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [GeneralUtil showProgress];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETINVITERINFO Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                inviterInfo = dicRes[@"inviterInfo"];
                if(inviterInfo == nil)
                {
                    self.lblRecommender.text = @"";
                    self.lblRecommenderCode.text = @"";
                    self.lblRecommenderChengHuDu.text = @"";
                    self.lblRecommenderChengHuDu1.text = @"";
                    self.lblRecommenderPingJia.text = @"";
                    self.lblRecommenderDianZan.text = @"";
                    [self.btnRecommenderBusiness setTitle:@"" forState:UIControlStateNormal];
                    self.recommendView.hidden = YES;
                    [self.scrollView setContentSize:CGSizeMake(SCREEN_WIDTH, 782)];
                    return;
                }else
                {
                    self.recommendView.hidden = NO;
                    [self.scrollView setContentSize:CGSizeMake(SCREEN_WIDTH, 951)];
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
                self.lblRecommender.text = [self.lblRecommenderName.text copy];//[NSString stringWithFormat:@"%@ - ", [inviterInfo objectForKey:@"inviterFriendLevel"]];
                
                
                self.lblRecommenderCode.text = [inviterInfo objectForKey:@"code"];
                self.lblRecommenderDianZan.text = [[inviterInfo objectForKey:@"electCnt"] stringValue];
                self.lblRecommenderChengHuDu.text = self.lblRecommenderChengHuDu1.text = [NSString stringWithFormat:@"%@%@", [inviterInfo[@"credit"] stringValue], @"%"];
                if([inviterInfo[@"credit"] longValue] == 0) {
                    self.lblRecommenderChengHuDu.text = self.lblRecommenderChengHuDu1.text = @"暂无";
                }
                [self.btnRecommenderBusiness setTitle:inviterInfo[@"xyName"] forState:UIControlStateNormal];
                self.lblRecommenderPingJia.text = [[inviterInfo objectForKey:@"feedbackCnt"] stringValue];
            }
        }
    }];
    
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    int akind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
    if(akind == 1)
    {
        self.lblName.text = [CommonData sharedInstance].userInfo[@"realname"];
        if(self.lblName.text.length == 0)
        {
            self.lblName.text = [CommonData sharedInstance].userInfo[@"mobile"];
        }
    }else if(akind == 2)
    {
        self.lblName.text = [CommonData sharedInstance].userInfo[@"enterName"];
    }
    [self.lblName sizeToFit];
    
    NSString *strXYName = [CommonData sharedInstance].userInfo[@"xyName"];
    if(strXYName.length == 0) {
        self.btnBusiness.hidden = YES;
    }else{
        self.btnBusiness.hidden = NO;
        [self.btnBusiness setTitle:strXYName forState:UIControlStateNormal];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGRect nameLabelFrame = self.lblName.frame;
            
            CGSize titleSize = [self.lblName.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.0]}];
            int nNextLine = 0;
            while (titleSize.width >= self.lblName.frame.size.width) {
                titleSize.width -= self.lblName.frame.size.width;
                nNextLine ++;
            }
            CGSize stringSize = [strXYName sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.0]}];
            
            [self.btnBusiness setFrame:CGRectMake(nameLabelFrame.origin.x + titleSize.width + 3, nameLabelFrame.origin.y + 21 * nNextLine, stringSize.width, 16)];
        });
    }

   
    self.lblCompanyName.text = [CommonData sharedInstance].userInfo[@"enterName"];
    self.lblCodeNumber.text = [CommonData sharedInstance].userInfo[@"code"];
    if(self.lblCodeNumber.text.length == 0) {
        self.lblCodeNumber.text = @"请认证";
    }
    
    if([[CommonData sharedInstance].userInfo[@"credit"] longValue] == 0) {
        self.lblChengHuDu.text = @"诚信度: 暂无";
    }else{
        self.lblChengHuDu.text = [NSString stringWithFormat:@"诚信度: %@%@", [[CommonData sharedInstance].userInfo[@"credit"] stringValue], @"%"];
    }
    
    self.lblFeedback.text = [NSString stringWithFormat:@"评价: %@", [[CommonData sharedInstance].userInfo[@"feedbackCnt"] stringValue]];
    self.lblDianZan.text = [NSString stringWithFormat:@"点赞: %@", [[CommonData sharedInstance].userInfo[@"electCnt"] stringValue]];
    self.lblAddress.text = [NSString stringWithFormat:@"%@ %@ %@", [CommonData sharedInstance].userInfo[@"provinceName"], [CommonData sharedInstance].userInfo[@"cityName"], [CommonData sharedInstance].userInfo[@"addr"]];
    self.lblCompanyWebURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
    self.lblWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
    self.lblPosition.text = [CommonData sharedInstance].userInfo[@"job"];
    self.lblWorkExperience.text = [CommonData sharedInstance].userInfo[@"experience"];
    self.lblPersonalHistory.text = [CommonData sharedInstance].userInfo[@"history"];
    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]]];
    
    [self.logoImageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:akind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    
}
- (void)didReceiveMemoryWarning
{
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
#pragma mark - IBActions
-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
- (IBAction)onURL:(id)sender
{
    if(self.lblCompanyWebURL.text.length == 0) {
        return;
    }
    NSURL* url = [NSURL URLWithString:self.lblCompanyWebURL.text];
    if([[url absoluteString] containsString:@"http"] == false)
    {
        url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
    }
    [[UIApplication sharedApplication] openURL:url];

}
- (IBAction)onCompanyDetail:(id)sender
{
    if(self.lblCompanyName.text.length == 0) {
        return;
    }
    [CommonData sharedInstance].selectedFriendAccountID = [CommonData sharedInstance].userInfo[@"enterId"];
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
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:@"6" forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].userInfo[@"id"] forKey:@"id"];
    
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
                           style:nil];;
        }
    }];
}
@end
