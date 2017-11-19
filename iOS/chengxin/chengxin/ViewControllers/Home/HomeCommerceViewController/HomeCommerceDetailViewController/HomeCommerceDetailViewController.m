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

#import "Global.h"
#import "UIImageView+WebCache.h"

@interface HomeCommerceDetailViewController ()
{
    NSDictionary *productDictionary;
    NSString *accountID;
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
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getProductDetail" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].selectedProductID forKey:@"productId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPRODUCTDETAIL Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.noNetworkView setHidden:YES];
                productDictionary = (NSDictionary *)(dicRes[@"product"]);
                [self setData];
            }else{
                [self.noNetworkView setHidden:NO];
            }
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
            [imgView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, imagePaths[i]]]];
        }
        [slideCommerceScrollView addSubview:imgView];
    }
    self.lblName.text = productDictionary[@"name"];
    self.lblInfo.text = productDictionary[@"comment"];
    self.lblPrice.text = [NSString stringWithFormat:@"￥%ld", (long)([productDictionary[@"price"] longValue])];
    NSInteger isFavorite = (NSInteger)[productDictionary[@"isFavourite"] integerValue];
    if(isFavorite == 0)
        self.btnFavorite.selected = NO;
    else
        self.btnFavorite.selected = YES;
    self.lblNetwork.text = productDictionary[@"weburl"];
    self.lblAddress.text = productDictionary[@"saleAddr"];
    self.lblEntername.text = productDictionary[@"enterName"];
    self.lblEnterKindName.text = productDictionary[@"enterKindName"];
    self.lblEntercode.text = [NSString stringWithFormat:@"诚信代码：%@", productDictionary[@"accountCode"]];
    self.lblViewcnt.text = [NSString stringWithFormat:@"诚信度：%ld%%", (long)([productDictionary[@"accountCredit"] longValue])];
    NSString *accountLogo = productDictionary[@"accountLogo"];
    if(accountLogo)
        [self.imgEnterLogo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, accountLogo]]];
    accountID = productDictionary[@"accountId"];
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
    
    [slideCommerceScrollView removeFromSuperview];
    slideCommerceScrollView = nil;
}

- (IBAction)onFavoriteAction:(id)sender {
    self.btnFavorite.selected = !self.btnFavorite.selected;
    
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onClickCommerceDetailPage:(id)sender {
    
    HomeFamiliarDetailViewController *homeEnterDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
    [CommonData sharedInstance].selectedFriendAccountID = accountID;
    [self.navigationController pushViewController:homeEnterDetailVC animated:YES];
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
@end
