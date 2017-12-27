//
//  ProductDetailsViewController.m
//  chengxin
//
//  Created by common on 7/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ProductDetailsViewController.h"
#import "Global.h"
#import "WebAPI.h"
#import "UIImageView+WebCache.h"
#import "WebViewController.h"
#import "HomeCommerceAddViewController.h"
#import "HomeItemAddViewController.h"

@interface ProductDetailsViewController ()

@end

@implementation ProductDetailsViewController
{
    NSDictionary *productDictionary;
    NSString *accountID;
    NSString *productID;
    NSString *callString;
    BOOL isUp;
    UIAlertView* deleteAlert, *upDownAlert;
    NSMutableArray *imagePaths;
}
@synthesize viewDetail, lblInfo;
@synthesize slideCommerceScrollView, slideCommercePageCtrl;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.btnEdit.layer.cornerRadius = 4;
    self.btnDelete.layer.cornerRadius = 4;
    self.btnUpDown.layer.cornerRadius = 4;

}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self getProductDataFromServer];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
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
    imagePaths = (NSMutableArray *)(productDictionary[@"imgPaths"]);
    [slideCommerceScrollView setContentSize:CGSizeMake(SCREEN_WIDTH * imagePaths.count, SCREEN_WIDTH)];
    for ( int i = 0; i < imagePaths.count; i++) {
        UIImageView *imgView = [[UIImageView alloc] init];
        [imgView setFrame:CGRectMake(i * SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_WIDTH)];
        imgView.contentMode = UIViewContentModeScaleToFill;
//        [imgView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, imagePaths[i]]] placeholderImage:[UIImage imageNamed:@"bg_pic.png"]];
        [imgView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, imagePaths[i]]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            if(!image) {
                [imgView setImage:[UIImage imageNamed:@"bg_pic.png"]];
            }
        }];

        [slideCommerceScrollView addSubview:imgView];
    }
    
    [slideCommercePageCtrl setNumberOfPages:imagePaths.count];
    CGRect pcFrame = slideCommercePageCtrl.frame;
    CGFloat width = imagePaths.count * 16;
    [slideCommercePageCtrl setFrame:CGRectMake(pcFrame.origin.x + pcFrame.size.width - width, pcFrame.origin.y, width, pcFrame.size.height)];
    
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
    self.lblPrice.text = [NSString stringWithFormat:@"￥%.2f", (float)([productDictionary[@"price"] floatValue])];
    NSInteger isFavorite = (NSInteger)[productDictionary[@"isFavourite"] integerValue];
    if(isFavorite == 0)
        self.btnFavorite.selected = NO;
    else
        self.btnFavorite.selected = YES;
    self.lblNetwork.text = productDictionary[@"weburl"];
    self.lblAddress.text = productDictionary[@"saleAddr"];
    self.lblEntername.text = productDictionary[@"enterName"];
    //self.lblEnterKindName.text = productDictionary[@"enterKindName"];
    self.lblEntercode.text = [NSString stringWithFormat:@"编号：%@", productDictionary[@"code"]];
    self.lblViewcnt.text = [NSString stringWithFormat:@"诚信度：%ld%%", (long)([productDictionary[@"accountCredit"] longValue])];
    isUp = [productDictionary[@"status"] boolValue];
    if(isUp)
    {
        self.lblStatus.text = @"已上架";
        [self.lblStatus setTextColor:ORANGE_COLOR];
        //[self.btnUpDown setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        //[self.btnUpDown setBackgroundColor:[UIColor whiteColor]];
        [self.btnUpDown setTitle:@"下架" forState:UIControlStateNormal];
        self.btnUpDown.frame = CGRectMake(7, self.btnUpDown.frame.origin.y, self.view.frame.size.width - 14, self.btnEdit.frame.size.height);
        self.btnEdit.hidden = YES;
        self.btnDelete.hidden = YES;
    }else
    {
        self.lblStatus.text = @"未上架";
        [self.lblStatus setTextColor:[UIColor darkGrayColor]];
        //[self.btnUpDown setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        //[self.btnUpDown setBackgroundColor:[UIColor orangeColor]];
        self.btnUpDown.frame = CGRectMake(self.view.frame.size.width - self.btnDelete.frame.origin.x - self.btnDelete.frame.size.width, self.btnUpDown.frame.origin.y, self.btnDelete.frame.size.width, self.btnDelete.frame.size.height);
        [self.btnUpDown setTitle:@"上架" forState:UIControlStateNormal];
        self.btnEdit.hidden = NO;
        self.btnDelete.hidden = NO;
    }
    NSString *accountLogo = productDictionary[@"accountLogo"];
    if(accountLogo)
        [self.imgEnterLogo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, accountLogo]]];
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
                [appDelegate.window makeToast:@"成功立即购买"
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
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
#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == slideCommerceScrollView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        [slideCommercePageCtrl setCurrentPage:page];
    }
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - Actions
-(IBAction)onUpDown:(id)sender
{
    if(isUp)
    {
        upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要下架该产品吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    }else
    {
        upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要上架该产品吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    }
    
    [upDownAlert show];
    
    

}
-(IBAction)onDelete:(id)sender
{
    deleteAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要删除该产品吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    [deleteAlert show];
}
-(IBAction)onEdit:(id)sender
{
    HomeCommerceAddViewController* vc = [[HomeCommerceAddViewController alloc] initWithNibName:@"HomeCommerceAddViewController" bundle:nil];
    vc.changedProductID = productDictionary[@"id"];
    vc.product = productDictionary;
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        if(alertView == deleteAlert)
        {
            NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
            [dicParams setObject:ACTION_DELETEPRODUCT forKey:@"pAct"];
            [dicParams setObject:productDictionary[@"id"] forKey:@"productId"];
            [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
            
            [[WebAPI sharedInstance] sendPostRequest:ACTION_DELETEPRODUCT Parameters:dicParams :^(NSObject *resObj){
                
                [GeneralUtil hideProgress];
                
                NSDictionary *dicRes = (NSDictionary *)resObj;
                
                if (dicRes != nil ) {
                    if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                        [appDelegate.window makeToast:@"删除！"
                                    duration:3.0
                                    position:CSToastPositionCenter
                                       style:nil];
                        [self.navigationController popViewControllerAnimated:YES];
                    }
                    else {
                        [appDelegate.window makeToast:dicRes[@"msg"]
                                    duration:3.0
                                    position:CSToastPositionCenter
                                       style:nil];
                    }
                }
            }];
        }else if(alertView == upDownAlert)
        {
            NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
            [dicParams setObject:isUp ? ACTION_DOWNPRODUCT : ACTION_UPPRODUCT forKey:@"pAct"];
            [dicParams setObject:productDictionary[@"id"] forKey:@"productId"];
            [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
            
            [[WebAPI sharedInstance] sendPostRequest:isUp ? ACTION_DOWNPRODUCT : ACTION_UPPRODUCT Parameters:dicParams :^(NSObject *resObj){
                
                [GeneralUtil hideProgress];
                
                NSDictionary *dicRes = (NSDictionary *)resObj;
                
                if (dicRes != nil ) {
                    if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                        isUp = !isUp;
                        if(isUp)
                        {
                            self.lblStatus.text = @"已上架";
                            
                            //[self.btnUpDown.titleLabel setTextColor:[UIColor blackColor]];
                            //[self.btnUpDown setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                            //[self.btnUpDown setBackgroundColor:[UIColor whiteColor]];
                            [self.btnUpDown setTitle:@"下架" forState:UIControlStateNormal];
                            self.btnUpDown.frame = CGRectMake(7, self.btnUpDown.frame.origin.y, self.view.frame.size.width - 14, self.btnDelete.frame.size.height);
                            self.btnEdit.hidden = YES;
                            self.btnDelete.hidden = YES;
                        }else
                        {
                            self.lblStatus.text = @"未上架";
                            //[self.lblShangJia setTextColor:[UIColor blackColor]];
                            //[self.btnUpDown setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
                            //[self.btnUpDown setBackgroundColor:[UIColor orangeColor]];
                            [self.btnUpDown setTitle:@"上架" forState:UIControlStateNormal];
                            self.btnUpDown.frame = CGRectMake(self.view.frame.size.width - self.btnDelete.frame.origin.x - self.btnDelete.frame.size.width, self.btnUpDown.frame.origin.y, self.btnEdit.frame.size.width, self.btnEdit.frame.size.height);
                            self.btnEdit.hidden = NO;
                            self.btnDelete.hidden = NO;
                        }

                        
                    }
                    else {
                        [appDelegate.window makeToast:dicRes[@"msg"]
                                    duration:3.0
                                    position:CSToastPositionCenter
                                       style:nil];
                    }
                }
            }];
        }
        
    }
    
}

- (IBAction)swipeLeftHomeScrollView:(UISwipeGestureRecognizer *)gestureRecognizer {
    CGFloat pageWidth = slideCommerceScrollView.frame.size.width;
    float fractionalPage = slideCommerceScrollView.contentOffset.x / pageWidth;
    NSInteger nCurrentPage = lround(fractionalPage);
    if(nCurrentPage < imagePaths.count - 1) {
        [slideCommerceScrollView setContentOffset:CGPointMake(pageWidth * (nCurrentPage + 1), slideCommerceScrollView.contentOffset.y) animated:YES];
        [slideCommercePageCtrl setCurrentPage:nCurrentPage +1];
        
    }

}

- (IBAction)swipeRightHomeScrollView:(UISwipeGestureRecognizer *)gestureRecognizer {

    CGFloat pageWidth = slideCommerceScrollView.frame.size.width;
    float fractionalPage = slideCommerceScrollView.contentOffset.x / pageWidth;
    NSInteger nCurrentPage = lround(fractionalPage);
    if(nCurrentPage > 0) {
        [slideCommerceScrollView setContentOffset:CGPointMake(pageWidth * (nCurrentPage - 1), slideCommerceScrollView.contentOffset.y) animated:YES];
        [slideCommercePageCtrl setCurrentPage:nCurrentPage - 1];
        
    }

}
@end
