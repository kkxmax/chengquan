//
//  HomeCommerceViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeCommerceViewController.h"
#import "RealNameAuthenticationViewController.h"
#import "HomeCommerceCollectionViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "AAPullToRefresh.h"

@interface HomeCommerceViewController ()
{
    NSMutableArray *productArray;
    NSString *strCityName;
    NSString *strCommerceIds;
    NSString *strStart;
    NSString *strKeyword;
    NSString *strLength;
    
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;

}
@end

@implementation HomeCommerceViewController
@synthesize commerceCollectionView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    productArray = [NSMutableArray array];
    [self.commerceCollectionView registerNib:[UINib nibWithNibName:@"HomeCommerceCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HomeCommerceCellIdentifier"];
    
    // NSNotification for Reload Changed Data
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadChangedData:) name:RELOAD_COMMERCE_DATA_NOTIFICATION object:nil];

    currentSortOrderIndex = 1;
//    strCityName = @"";
//    strCommerceIds = @"";
//    strStart = @"";
//    strKeyword = @"";
//    strLength = @"";
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.commerceCollectionView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.commerceCollectionView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;
    
    [self.commerceCollectionView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];

}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(baseDelegate && [keyPath isEqualToString:@"contentSize"]) {
        self.commerceCollectionView.frame = CGRectMake(self.commerceCollectionView.frame.origin.x, self.commerceCollectionView.frame.origin.y, self.commerceCollectionView.frame.size.width, self.commerceCollectionView.contentSize.height);
        self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.commerceCollectionView.frame.size.height);
        [baseDelegate finishedLoadingData:2];
    }

}

- (void)dealloc
{
    [self.commerceCollectionView removeObserver:self forKeyPath:@"contentSize" context:NULL];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self refreshTopItems];
    topRefreshView.showPullToRefresh = YES;
    bottomRefreshView.showPullToRefresh = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    topRefreshView.showPullToRefresh = NO;
    bottomRefreshView.showPullToRefresh = NO;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(productArray)
        [productArray removeAllObjects];
    [self getProductFromServer:[CommonData sharedInstance].choiceCommerceCity
                             PleixingIds:[CommonData sharedInstance].choiceCommerceIds
                                   Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                                  Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                                 Keyword:[CommonData sharedInstance].searchProductText];
}

- (void)refreshBottomItems {
    refreshStartIndex = productArray.count;
    [self getProductFromServer:[CommonData sharedInstance].choiceCommerceCity
                   PleixingIds:[CommonData sharedInstance].choiceCommerceIds
                         Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                        Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                       Keyword:[CommonData sharedInstance].searchProductText];
}


- (void)getProductFromServer:cityName PleixingIds:(NSString*)pleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword  {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getProductList" forKey:@"pAct"];
    [dicParams setObject:start forKey:@"start"];
    [dicParams setObject:length forKey:@"length"];
    [dicParams setObject:pleixingIds forKey:@"pleixingIds"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:keyword forKey:@"keyword"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPRODUCTLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *productList = (NSArray *)(dicRes[@"data"]);
//                [productArray removeAllObjects];
                for(int i = 0; i < productList.count; i++) {
                    NSDictionary *productDic = (NSDictionary *)(productList[i]);
                    [productArray addObject:productDic];
                }
                [self.commerceCollectionView reloadData];
                if(baseDelegate) {
                    [baseDelegate stopLoadingIndicator];
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
        [GeneralUtil hideProgress];
    }];
}

#pragma mark - UICollectionView Delegate, DataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HomeCommerceCellIdentifier" forIndexPath:indexPath];
    if(productArray.count <= indexPath.row)
        return homeCommerceCollectionCell;
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    NSString *productImageName = productDic[@"imgPath1"];
    [homeCommerceCollectionCell.produceImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, productImageName]] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
    homeCommerceCollectionCell.produceNameLabel.text = productDic[@"name"];
    homeCommerceCollectionCell.producePriceLabel.text = [NSString stringWithFormat:@"¥%.2f", (float)[productDic[@"price"] floatValue]];
    return homeCommerceCollectionCell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return productArray.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGSize result = [[UIScreen mainScreen] bounds].size;
        if(result.height == 568)
        {
            return CGSizeMake(156.f, 200);
            
        }
        if(result.height == 667)
        {
            return CGSizeMake(184.f, 244);
            
        }
        if(result.height == 736)
        {
            return CGSizeMake(202.f, 244);
            
            
        }
    }
    return CGSizeMake(184.f, 244);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    [CommonData sharedInstance].selectedProductID = [NSString stringWithFormat:@"%ld", [productDic[@"id"] longValue]];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMECOMMERCEDETAIL_VIEW_NOTIFICATION object:indexPath];
}

- (void)addAction {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
    } else {
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEPRODUCTADD_VIEW_NOTIFICATION object:nil];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
}

- (void)reloadChangedData:(NSNotification *)notification {
//    [self getProductFromServer:[CommonData sharedInstance].choiceCommerceCity
//                             PleixingIds:[CommonData sharedInstance].choiceCommerceIds
//                                   Start:@""
//                                  Length:@""
//                                 Keyword:[CommonData sharedInstance].searchProductText];
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
