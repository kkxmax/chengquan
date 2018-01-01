//
//  HomeItemDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemDetailViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "HomeFamiliarDetailViewController.h"
#import "WebViewController.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"

@interface HomeItemDetailViewController ()
{
    NSDictionary *itemDic;
    NSString *accountID;
    NSString *callString;
}
@end

@implementation HomeItemDetailViewController
@synthesize detailScrollView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    itemDic = [[NSDictionary alloc] init];
    itemDic = [CommonData sharedInstance].selectedItemServiceDic;
    [self setData];
    
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE) {
        self.navTitleLabel.text = @"服务详情";
        self.commentTitleLabel.text = @"服务介绍";
        self.needTitleLabel.text = @"地址：";
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setData {
    NSString *logoImageName = itemDic[@"logo"];
    if(logoImageName) {
        self.logoImageLabel.hidden = YES;
        [self.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_item.png"]];
    }else{
        self.logoImageLabel.hidden = NO;
    }
    self.nameLabel.text = itemDic[@"name"];
    [self.nameLabel sizeToFit];
    [self.fenleiNameButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGSize stringSize = [self.fenleiNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.0]}];
        if(self.nameLabel.frame.size.width > (self.view.frame.size.width - 75 - stringSize.width - self.nameLabel.frame.origin.x)) {
            [self.nameLabel setFrame:CGRectMake(self.nameLabel.frame.origin.x, self.nameLabel.frame.origin.y, self.view.frame.size.width - 75 - stringSize.width - self.nameLabel.frame.origin.x, self.nameLabel.frame.size.height)];
        }
        CGRect nameLabelFrame = self.nameLabel.frame;
        [self.fenleiNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
    });
    self.addressLabel.text = [NSString stringWithFormat:@"所在城市 : %@", itemDic[@"cityName"]];
    self.codeLabel.text = [NSString stringWithFormat:@"编号：%@", itemDic[@"code"]];
    self.commentTextView.text = itemDic[@"comment"];
    self.networkAddrLabel.text = itemDic[@"weburl"];
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE) {
        self.needLabel.text = itemDic[@"addr"];
    }else{
        self.needLabel.text = itemDic[@"need"];
    }
    self.contactNameLabel.text = itemDic[@"contactName"];
    self.contactMobileLabel.text = itemDic[@"contactMobile"];
    self.contactWeixinLabel.text = itemDic[@"contactWeixin"];
    
    NSInteger aKind = [itemDic[@"akind"] integerValue];
    
    NSString *accountLogoImageName = itemDic[@"accountLogo"];
    if(accountLogoImageName) {
        self.accountLogoImageLabel.hidden = YES;
        [self.accountLogoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, accountLogoImageName]] placeholderImage:[UIImage imageNamed: aKind == 1 ? @"no_image_person.png" : @"no_image_enter.png"]];
    }else{
        self.accountLogoImageLabel.hidden = NO;
    }
    
    if (aKind == PERSONAL_KIND) {
        self.officeMarkLabel.text = @"个人";
        self.enterNameLabel.text = itemDic[@"realname"];
    }else{
        self.enterNameLabel.text = itemDic[@"enterName"];
        self.officeMarkLabel.text = @"企业";//itemDic[@"enterKindName"];
    }
    self.enterCodeLabel.text = itemDic[@"accountCode"];
    self.creditLabel.text = [NSString stringWithFormat:@"%ld%%", (long)([itemDic[@"accountCredit"] longValue])];
    self.writeTimeLabel.text = [GeneralUtil getDateHourMinFrom:itemDic[@"writeTimeString"]];
    accountID = [NSString stringWithFormat:@"%ld", (long)[itemDic[@"accountId"] integerValue]];
    callString = itemDic[@"accountMobile"];
}
#pragma mark - IBAction

- (IBAction)onShowEnterAction:(id)sender {
    if([accountID isEqualToString:@""])
        return;
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getAccountDetail" forKey:@"pAct"];
    [dicParams setObject:accountID forKey:@"accountId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETACCOUNTDETAIL Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSDictionary *friendDictionary = (NSDictionary *)(dicRes[@"account"]);
                int nTestStatus = [friendDictionary[@"testStatus"] intValue];
                if(nTestStatus != 2) {
                    [appDelegate.window makeToast:@"未认证的熟人／企业"
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }else{
                    HomeFamiliarDetailViewController *homeEnterDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
                    [CommonData sharedInstance].selectedFriendAccountID = accountID;
                    [self.navigationController pushViewController:homeEnterDetailVC animated:YES];
                }
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

- (IBAction)onBackAction:(id)sender {
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
       [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_SERVICE_DATA_NOTIFICATION object:nil];
    else
        [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_ITEM_DATA_NOTIFICATION object:nil];
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onShareAction:(id)sender {
    [self shareMenu];
}
- (IBAction)onNetworkAction:(id)sender {
    if(![itemDic[@"weburl"] isEqualToString:@""]) {
        NSURL* url = [NSURL URLWithString:itemDic[@"weburl"]];
        if([[url absoluteString] containsString:@"http"] == false)
        {
            url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
        }
        [[UIApplication sharedApplication] openURL:url];
        /*
        WebViewController *webVC = [[WebViewController alloc] initWithNibName:@"WebViewController" bundle:nil];
        webVC.webUrl = itemDic[@"weburl"];
        [self.navigationController pushViewController:webVC animated:YES];
         */
    }
}

- (IBAction)onCallingAction:(id)sender {
    if(![callString isEqualToString:@""]) {
        NSString *URLString = [@"tel:" stringByAppendingString:callString];
        NSURL *URL = [NSURL URLWithString:URLString];
//        if([[URL absoluteString] containsString:@"http"] == false)
//        {
//            URL = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [URL absoluteString] ]];
//        }
        [[UIApplication sharedApplication] openURL:URL];
        
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"onContact" forKey:@"pAct"];
        [dicParams setObject:accountID forKey:@"accountId"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        
        [[WebAPI sharedInstance] sendPostRequest:@"onContact" Parameters:dicParams :^(NSObject *resObj) {
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            //[GeneralUtil hideProgress];
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    
                }else{
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                         duration:3.0
                                         position:CSToastPositionCenter
                                            style:nil];
                }
            }else{
                [appDelegate.window makeToast:@"网络不连接"
                                     duration:3.0
                                     position:CSToastPositionCenter
                                        style:nil];
            }
        }];

    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if(scrollView == self.detailScrollView)
        self.detailScrollView.bounces = (self.detailScrollView.contentOffset.y > 100);
}

#pragma mark ShareSDK
- (void)shareMenu
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    NSArray* imageArray = @[[[NSBundle mainBundle] pathForResource:@"no_image@2x" ofType:@"png"]];
    NSString *logoImageName = itemDic[@"logo"];
    if (logoImageName) {
        NSURL *imgUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]];
        imageArray = @[imgUrl];
    }
    NSString *shareTitle = @"【项目】您的好友给您分享了一个项目，立即查看！";
    NSString *url = [NSString stringWithFormat:@"%@%@%@", BASE_WEB_URL, @"/xiangmu.html?itemId=",itemDic[@"id"]];
    
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE) {
        url = [NSString stringWithFormat:@"%@%@%@", BASE_WEB_URL, @"/fuwu.html?serviceId=",itemDic[@"id"]];
        shareTitle = @"【服务】您的好友给您分享了一项服务，立即查看！";
    }
    [shareParams SSDKSetupShareParamsByText:itemDic[@"comment"]
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
    [dicParams setObject:[NSNumber numberWithInteger:2] forKey:@"kind"];
    if([CommonData sharedInstance].detailItemServiceIndex == SUB_HOME_SERVICE)
        [dicParams setObject:[NSNumber numberWithInteger:3] forKey:@"kind"];
    
    [dicParams setObject:itemDic[@"id"] forKey:@"id"];
//    [dicParams setObject:@"1" forKey:@"share"];
    
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


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
