//
//  HomeCommerceDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeCommerceDetailViewController.h"
#import "HomeEnterpriseDetailViewController.h"
#import "HomeFamiliarDetailViewController.h"
#import "WebViewController.h"

#import "Global.h"
#import "UIImageView+WebCache.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"

@interface HomeCommerceDetailViewController ()
{
    NSDictionary *productDictionary;
    NSString *accountID;
    NSString *productID;
    NSString *callString;
}
@end

@implementation HomeCommerceDetailViewController
@synthesize viewDetail, lblInfo;
@synthesize slideCommerceScrollView, slideCommercePageCtrl;
@synthesize btnBuy;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self getProductDataFromServer];
    btnBuy.layer.cornerRadius = 3.f;
}

- (void)getProductDataFromServer {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getProductDetail" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].selectedProductID forKey:@"productId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPRODUCTDETAIL Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.noNetworkView setHidden:YES];
                productDictionary = (NSDictionary *)(dicRes[@"product"]);
                [self setData];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                                                 duration:3.0
                                                 position:CSToastPositionCenter
                                                    style:nil];
                [self.noNetworkView setHidden:NO];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                             duration:3.0
                                             position:CSToastPositionCenter
                                                style:nil];
        }
    }];

}

- (void)setData {
    NSMutableArray *imagePaths = (NSMutableArray *)(productDictionary[@"imgPaths"]);
    [slideCommerceScrollView setContentSize:CGSizeMake(SCREEN_WIDTH * imagePaths.count, SCREEN_WIDTH)];
    for ( int i = 0; i < imagePaths.count; i++) {
        UIImageView *imgView = [[UIImageView alloc] init];
        [imgView setFrame:CGRectMake(i * SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_WIDTH)];
        imgView.contentMode = UIViewContentModeScaleToFill;
        if(imagePaths[i]) {
            [imgView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, imagePaths[i]]] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        }
        [slideCommerceScrollView addSubview:imgView];
    }
    if([productDictionary[@"isMain"] intValue] == 0)
    {
        self.mainProductWidth.constant = 0;
        self.mainProductRightMargin.constant = 0;
    }else
    {
        self.mainProductWidth.constant = 54;
        self.mainProductRightMargin.constant = 7;
    }
    
    productID = productDictionary[@"id"];
    self.lblName.text = productDictionary[@"name"];
    self.lblInfo.text = productDictionary[@"comment"];
    self.lblPrice.text = [NSString stringWithFormat:@"￥%ld", (long)([productDictionary[@"price"] longValue])];
    NSInteger isFavorite = (NSInteger)[productDictionary[@"isFavourite"] integerValue];
    if(isFavorite == 0)
        self.btnFavorite.selected = NO;
    else
        self.btnFavorite.selected = YES;
    self.lblCode.text = [NSString stringWithFormat:@"编号：%@", productDictionary[@"code"]];
    self.lblNetwork.text = productDictionary[@"weburl"];
    if(self.lblNetwork.text.length == 0)
    {
        self.btnBuy.hidden = YES;
        self.detailViewConstraint.constant = 190;
    } else {
        self.btnBuy.hidden = NO;
        self.detailViewConstraint.constant = 250;
//        NSURL* url = [NSURL URLWithString:productDictionary[@"weburl"]];
//        if ([[UIApplication sharedApplication] canOpenURL:url]) {
//            self.btnBuy.hidden = NO;
//            self.detailViewConstraint.constant = 250;
//        }else{
//            self.btnBuy.hidden = YES;
//            self.detailViewConstraint.constant = 190;
//        }
    }
    self.lblAddress.text = productDictionary[@"saleAddr"];
    self.lblEntername.text = productDictionary[@"enterName"];
    self.lblEnterKindName.text = @"企业";//productDictionary[@"enterKindName"];
    self.lblEntercode.text = [NSString stringWithFormat:@"诚信代码：%@", productDictionary[@"accountCode"]];
    self.lblViewcnt.text = [NSString stringWithFormat:@"诚信度：%ld%%", (long)([productDictionary[@"accountCredit"] longValue])];
    NSString *accountLogo = productDictionary[@"accountLogo"];
    if(accountLogo)
        [self.imgEnterLogo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, accountLogo]] placeholderImage:[UIImage imageNamed:@"no_image_enter.png"]];
    accountID = [NSString stringWithFormat:@"%ld", [productDictionary[@"accountId"] longValue]];
    callString = productDictionary[@"accountMobile"];
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_COMMERCE_DATA_NOTIFICATION object:nil];

    [self.navigationController popViewControllerAnimated:YES];
    
    [slideCommerceScrollView removeFromSuperview];
    slideCommerceScrollView = nil;
}

- (IBAction)onFavoriteAction:(id)sender {
    self.btnFavorite.selected = !self.btnFavorite.selected;
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setFavourite" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:@"1" forKey:@"kind"];
    [dicParams setObject:productID forKey:@"id"];
    [dicParams setObject:self.btnFavorite.selected? @"1" : @"0" forKey:@"val"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETFAVOURITE Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
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

- (IBAction)onNetworkAction:(id)sender {
    if(![productDictionary[@"weburl"] isEqualToString:@""]) {
        NSURL* url = [NSURL URLWithString:productDictionary[@"weburl"]];
        if([[url absoluteString] containsString:@"http"] == false)
        {
            url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
        }
        [[UIApplication sharedApplication] openURL:url];
        /*
        WebViewController *webVC = [[WebViewController alloc] initWithNibName:@"WebViewController" bundle:nil];
        webVC.webUrl = productDictionary[@"weburl"];
        [self.navigationController pushViewController:webVC animated:YES];
         */
    }
}
- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onClickCommerceDetailPage:(id)sender {
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

- (IBAction)onPurchase:(id)sender {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"onPurchase" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:productID forKey:@"productId"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_ONPURCHASE Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                if(![productDictionary[@"weburl"] isEqualToString:@""]) {
                    NSURL* url = [NSURL URLWithString:productDictionary[@"weburl"]];
                    if([[url absoluteString] containsString:@"http"] == false)
                    {
                        url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
                    }
                    [[UIApplication sharedApplication] openURL:url];
                    /*
                    WebViewController *webVC = [[WebViewController alloc] initWithNibName:@"WebViewController" bundle:nil];
                    webVC.webUrl = productDictionary[@"weburl"];
                    [self.navigationController pushViewController:webVC animated:YES];
                     */
                }
            }else{
            }
        }else{
        }
    }];

}
#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == slideCommerceScrollView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        [slideCommercePageCtrl setCurrentPage:page];
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
                [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                     duration:3.0
                                     position:CSToastPositionCenter
                                        style:nil];
            }
        }];
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

- (IBAction)onClickBuyButton:(id)sender {
}

- (IBAction)onShareAction:(id)sender {
    [self shareMenu];
}

#pragma mark ShareSDK
- (void)shareMenu
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    NSArray* imageArray = @[[[NSBundle mainBundle] pathForResource:@"no_image@2x" ofType:@"png"]];
    NSString *imgPath1 = productDictionary[@"imgPath1"];
    if (imgPath1) {
        NSURL *imgUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, imgPath1]];
        imageArray = @[imgUrl];
    }
    NSString *url = [NSString stringWithFormat:@"%@%@%@", BASE_WEB_URL, @"/chanpin.html?productId=",productDictionary[@"id"]];
    
    [shareParams SSDKSetupShareParamsByText:productDictionary[@"comment"]
                                     images:imageArray
                                        url:[NSURL URLWithString:url]
                                      title:@"【产品】您的好友给您分享了一个产品，立即查看！"
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
    [dicParams setObject:[NSNumber numberWithInteger:1] forKey:@"kind"];
    [dicParams setObject:productDictionary[@"id"] forKey:@"id"];
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
@end
