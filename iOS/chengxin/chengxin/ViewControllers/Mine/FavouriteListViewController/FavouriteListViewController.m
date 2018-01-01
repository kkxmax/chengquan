//
//  FavouriteListViewController.m
//  chengxin
//
//  Created by common on 4/17/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "FavouriteListViewController.h"
#import "Global.h"
#import "HomeCommerceCollectionViewCell.h"
#import "HotTableViewCell.h"
#import "UIImageView+WebCache.h"
#import "HotObject.h"
#import "HotDetailViewController.h"
#import "AAPullToRefresh.h"

@interface FavouriteListViewController ()

@end

@implementation FavouriteListViewController
{
    NSMutableArray *productArray;
    NSMutableArray *reDianArray;

    AAPullToRefresh *topProductRefreshView;
    AAPullToRefresh *bottomProductRefreshView;
    NSInteger refreshProductStartIndex;

    AAPullToRefresh *topReDianRefreshView;
    AAPullToRefresh *bottomReDianRefreshView;
    NSInteger refreshReDianStartIndex;
    NSMutableArray* aryHeights;

    float imageWidth;
    float imageHeight;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    aryHeights = [[NSMutableArray alloc] init];
    productArray = [[NSMutableArray alloc] init];
    reDianArray = [[NSMutableArray alloc] init];
    [self setTopTab:em_FavouriteProduct];
    [self.productCollectionView registerNib:[UINib nibWithNibName:@"HomeCommerceCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HomeCommerceCellIdentifier"];
    [self.tblRedian registerNib:[UINib nibWithNibName:@"HotTableViewCell" bundle:nil] forCellReuseIdentifier:@"HotTableCellIdentifier"];
    __weak typeof(self) weakSelf = self;
    topProductRefreshView = [self.productCollectionView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopProductItems];
    }];
    bottomProductRefreshView = [self.productCollectionView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomProductItems];
    }];
    
    refreshProductStartIndex = 0;

    topReDianRefreshView = [self.tblRedian addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopReDianItems];
    }];
    bottomReDianRefreshView = [self.tblRedian addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomReDianItems];
    }];
    
    refreshReDianStartIndex = 0;
    
    imageWidth = (SCREEN_WIDTH - 20 - 14) / 3;
    imageHeight = imageWidth * 80 / 113;

}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self refreshTopProductItems];
    [self refreshTopReDianItems];
    topProductRefreshView.showPullToRefresh = YES;
    bottomProductRefreshView.showPullToRefresh = YES;
    topReDianRefreshView.showPullToRefresh = YES;
    bottomReDianRefreshView.showPullToRefresh = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    topProductRefreshView.showPullToRefresh = NO;
    bottomProductRefreshView.showPullToRefresh = NO;
    topReDianRefreshView.showPullToRefresh = NO;
    bottomReDianRefreshView.showPullToRefresh = NO;
}

- (void)refreshTopProductItems {
    refreshProductStartIndex = 0;
    if(productArray)
        [productArray removeAllObjects];
    [self getMyFavouriteProductDataFromServer:refreshProductStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshBottomProductItems {
    refreshProductStartIndex = productArray.count;
    [self getMyFavouriteProductDataFromServer:refreshProductStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshTopReDianItems {
    refreshReDianStartIndex = 0;
    if(reDianArray)
        [reDianArray removeAllObjects];
    [self getMyFavouriteHotDataFromServer:refreshReDianStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshBottomReDianItems {
    refreshReDianStartIndex = reDianArray.count;
    [self getMyFavouriteHotDataFromServer:refreshReDianStartIndex Length:REFRESH_GET_DATA_COUNT];
}


- (void) getMyFavouriteProductDataFromServer:(NSInteger)start Length:(NSInteger)length{
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_GETMYFAVOURITELIST forKey:@"pAct"];
    [dicParams setObject:[NSNumber numberWithInt:1] forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:start] forKey:@"start"];
    [dicParams setObject:[NSNumber numberWithInt:length] forKey:@"length"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETMYFAVOURITELIST Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topProductRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomProductRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *productList = (NSArray *)(dicRes[@"data"]);
                if(productList == nil)
                    self.productCollectionView.hidden = YES;
//                [productArray removeAllObjects];
                for(int i = 0; i < productList.count; i++) {
                    NSDictionary *productDic = (NSDictionary *)(productList[i]);
                    [productArray addObject:productDic];
                }
                if(productArray.count == 0) {
                    self.blankView.hidden = NO;
                }else{
                    self.blankView.hidden = YES;
                }

                [self.productCollectionView reloadData];
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

- (void) getMyFavouriteHotDataFromServer:(NSInteger)start Length:(NSInteger)length{
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_GETMYFAVOURITELIST forKey:@"pAct"];
    [dicParams setObject:[NSNumber numberWithInt:2] forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:start] forKey:@"start"];
    [dicParams setObject:[NSNumber numberWithInt:length] forKey:@"length"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETMYFAVOURITELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        [topReDianRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomReDianRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *list = (NSArray *)(dicRes[@"data"]);
//                [reDianArray removeAllObjects];
                if(list == nil)
                    self.tblRedian.hidden = YES;
                for(int i = 0; i < list.count; i++) {
                    NSDictionary *dic = (NSDictionary *)(list[i]);
                    [reDianArray addObject:[self parseHotObjFromData:dic]];
                }
                for(int i = 0; i < list.count; i++)
                {
                    [aryHeights addObject:[NSNumber numberWithFloat:60.0f]];
                }
                

                [self.tblRedian reloadData];
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

- (void)setTopTab:(FavouriteList_Tab)selection
{
    if(selection == em_FavouriteProduct)
    {
        self.productCollectionView.hidden = NO;
        self.tblRedian.hidden = YES;
        self.lblProductUnderline.hidden = NO;
        self.lblReDianUnderline.hidden = YES;
        [self.btnReDian setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnProduct setTitleColor:[UIColor colorWithRed:246/255.0f green:184/255.0f blue:17/255.0f alpha:1]  forState:UIControlStateNormal];
        if(productArray.count == 0) {
            self.blankView.hidden = NO;
        }else{
            self.blankView.hidden = YES;
        }
    }else if(selection == em_ReDian)
    {
        self.productCollectionView.hidden = YES;
        self.tblRedian.hidden = NO;
        self.lblProductUnderline.hidden = YES;
        self.lblReDianUnderline.hidden = NO;
        [self.btnProduct setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnReDian setTitleColor:[UIColor colorWithRed:246/255.0f green:184/255.0f blue:17/255.0f alpha:1]  forState:UIControlStateNormal]  ;
        if(reDianArray.count == 0) {
            self.blankView.hidden = NO;
        }else{
            self.blankView.hidden = YES;
        }
    }
    tab_selection = selection;
}
-(IBAction)onProduct:(id)sender
{
    [self setTopTab:em_FavouriteProduct];
}
-(IBAction)onReDian:(id)sender
{
    [self setTopTab:em_ReDian];
}
-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
- (HotObject*) parseHotObjFromData:(NSDictionary*) data {
    HotObject *hot = [[HotObject alloc] init];
    hot.mCommentCnt = [[data valueForKey:@"hotCommentCnt"] integerValue];
    hot.strContent = [data valueForKey:@"hotContent"];
    hot.strSummary = [data valueForKey:@"hotSummary"];
    hot.strDownTime = [data valueForKey:@"downTime"];
    hot.mElectCnt = [data[@"electCnt"] integerValue];
    hot.isElectedByMe = [[data valueForKey:@"isElectedByMe"] integerValue];
    hot.mId = [data[@"hotId"] integerValue];
    hot.aryImgPath = data[@"hotImgPaths"];
    hot.bIsFavourite = YES; //always YES.
    hot.mSerial = [data[@"serial"] integerValue];
    hot.mShareCnt = [data[@"shareCnt"] integerValue];
    hot.mStatus = [data[@"status"] integerValue];
    hot.strStatusName = data[@"statusName"];
    hot.strTitle = data[@"hotTitle"];
    hot.strUpTime = data[@"upTime"];
    hot.mVisitCnt = [data[@"hotVisitCnt"] integerValue];
    hot.dicWriteTime = data[@"writeTime"];
    hot.strWriteTimeString = data[@"hotWriteTimeString"];
    hot.mXyleixingId = [data[@"xyleixingId"] integerValue];
    hot.mXyleixingLevel1Id = [data[@"xyleixingLevel1Id"] integerValue];
    hot.mXyleixingLevel1Name = data[@"xyleixingLevel1Name"];
    hot.mXyleixingName = data[@"xyleixingName"];
    hot.mHotStatus = [data[@"hotStatus"] integerValue];
    hot.mHotId = [data[@"id"] longValue];
    return hot;
}

#pragma mark TableView
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return reDianArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if(aryHeights.count > indexPath.row)
        return [aryHeights[indexPath.row] floatValue];
    else
        return 174.f;
//    if(reDianArray.count <= indexPath.row)
//        return 169.0f;
//    HotObject *hot = reDianArray[indexPath.row];
//    if ( hot.aryImgPath == nil || hot.aryImgPath.count == 0 )
//        return 169.0f;
//    
//    return 249.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"HotTableCellIdentifier";
    HotTableViewCell *cell = (HotTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    if(reDianArray.count <= indexPath.row)
        return cell;
    HotObject *hot = reDianArray[indexPath.row];
    
    cell.lblTitle.text = hot.strTitle;
    cell.lblContent.text = hot.strSummary;
    cell.lblRead.text = [NSString stringWithFormat:@"%ld", (long)hot.mVisitCnt];
    cell.lblEval.text = [NSString stringWithFormat:@"%ld", (long)hot.mCommentCnt];
    cell.lblDate.text = [GeneralUtil getDateHourMinFrom:hot.strWriteTimeString];
    
    for( UIView* subV in [cell.imageContentView subviews])
    {
        [subV removeFromSuperview];
    }
    
    NSMutableArray *aryPath = hot.aryImgPath;
    if ( aryPath == nil || aryPath.count == 0  ) {
        cell.imageContentView.hidden = YES;
    }
    else
    {
        cell.imageContentView.hidden = NO;
        cell.imageContentView.frame = CGRectMake(cell.imageContentView.frame.origin.x, cell.imageContentView.frame.origin.y, cell.imageContentView.frame.size.width, imageHeight);
        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * (imageWidth + 7), 0, imageWidth, imageHeight)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
            
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
            [cell.imageContentView addSubview:imgView];
        }

//        for (int i = 0; i < aryPath.count; i++)
//        {
//            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 120, 0, 113, 80)];
//            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
//            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
//            [cell.scrollThumb addSubview:imgView];
//        }
//        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 120 - 7, 80)];
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.tblRedian beginUpdates];
        [cell.lblTitle sizeToFit];
        [cell.lblContent sizeToFit];
        NSNumber *itemHeight =  [NSNumber numberWithFloat:174 + cell.lblTitle.frame.size.height + cell.lblContent.frame.size.height - (aryPath.count == 0 ? imageHeight : 0)];
        [aryHeights replaceObjectAtIndex:indexPath.row  withObject:itemHeight];
        
        [self.tblRedian endUpdates];
    });

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    HotDetailViewController *detailViewController = [[HotDetailViewController alloc] initWithNibName:@"HotDetailViewController" bundle:nil];
    HotObject *hot = reDianArray[indexPath.row];
    if(hot.mHotStatus == 0) {
        [appDelegate.window makeToast:@"该热点已下架"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        [self setFavouriteHot:hot];
        return;
    }
    detailViewController.hotData = reDianArray[indexPath.row];
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
    
}


#pragma mark - UICollectionView Delegate, DataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HomeCommerceCellIdentifier" forIndexPath:indexPath];
    if(productArray.count <= indexPath.row)
        return homeCommerceCollectionCell;

    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    NSString *productImageName = productDic[@"productImgPath1"];
    [homeCommerceCollectionCell.produceImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, productImageName]] placeholderImage:[UIImage imageNamed:@"bg_pic.png"]];
    homeCommerceCollectionCell.produceNameLabel.text = productDic[@"productName"];
    homeCommerceCollectionCell.producePriceLabel.text = [NSString stringWithFormat:@"¥%.2f", (float)[productDic[@"productPrice"] floatValue]];
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
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    NSInteger productStatus = [productDic[@"productStatus"] integerValue];
    if(productStatus == 0) {
        [appDelegate.window makeToast:@"该产品已下架"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        [self setFavouriteProduct:productDic];
        return;
    }
    [CommonData sharedInstance].selectedProductID = [NSString stringWithFormat:@"%ld", [productDic[@"productId"] longValue]];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMECOMMERCEDETAIL_VIEW_NOTIFICATION object:indexPath];
}

- (void)setFavouriteProduct:(NSDictionary *)favouriteDic {
    int nKind = 1;
    NSString *favouriteID = favouriteDic[@"productId"];
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setFavourite" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:nKind] forKey:@"kind"];
    [dicParams setObject:favouriteID forKey:@"id"];
    [dicParams setObject:@"0" forKey:@"val"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETFAVOURITE Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [productArray removeObject:favouriteDic];
                [self.productCollectionView reloadData];
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

- (void)setFavouriteHot:(HotObject *)favouriteObj {
    int nKind = 2;
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setFavourite" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:nKind] forKey:@"kind"];
    [dicParams setObject:[NSNumber numberWithLong:favouriteObj.mId] forKey:@"id"];
    [dicParams setObject:@"0" forKey:@"val"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETFAVOURITE Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [reDianArray removeObject:favouriteObj];
                [self.tblRedian reloadData];
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
