//
//  HotDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotDetailViewController.h"
#import "HotDetailMainCell.h"
#import "HotDetailPersonalCell.h"
#import "HotDetailOfficeCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "EvaluateDetailViewController.h"
#import "HomeFamiliarDetailViewController.h"
#import "HotEstimationViewController.h"
#import "HotEstimationListViewController.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"
#import "AAPullToRefresh.h"

@interface HotDetailViewController ()
{
    NSMutableArray *aryEvalData;
    NSMutableArray *aryOfficeData;
    NSMutableArray *evaluateHeightArray;
    
    AAPullToRefresh *topEvalRefreshView;
    AAPullToRefresh *bottomEvalRefreshView;
    NSInteger refreshEvalStartIndex;

    AAPullToRefresh *topOfficeRefreshView;
    AAPullToRefresh *bottomOfficeRefreshView;
    NSInteger refreshOfficeStartIndex;
    
    CGFloat fEvalTableContentSizeHeight;
    CGFloat fOfficeTableContentSizeHeight;
    BOOL bClickPersonal;

}
@end

@implementation HotDetailViewController
@synthesize lblTitle, contentWebView, lblRead, lblDate, contentHeight;
@synthesize scrollInfoView, tblEvalView, tblOfficeView, tblHeight, pageControl;
@synthesize btnPersonal, btnOffice, personalSeparator, officeSeparator;
@synthesize hotData;
@synthesize scrollDataViewHeight, scrollinfoviewHeight,tableContentHeight;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    lblTitle.text = hotData.strTitle;
    NSData* webData = [NSData dataWithContentsOfURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, hotData.strContent]]];
    //NSString* str = [webData base64EncodedStringWithOptions:NSUTF8StringEncoding];
    if(webData == nil)
    {
        [contentWebView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, hotData.strContent]]]];
    }else
    {
        [contentWebView loadData:[NSData dataWithContentsOfURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, hotData.strContent]]] MIMEType:@"text/html" textEncodingName:@"UTF-8" baseURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, hotData.strContent]]];
    }
    
    contentWebView.scrollView.scrollEnabled = NO;
    
    lblRead.text = [NSString stringWithFormat:@"%ld", (long)(hotData.mVisitCnt + 1) ];
    lblDate.text = [hotData.strWriteTimeString substringToIndex:hotData.strWriteTimeString.length-3];
    evaluateHeightArray = [NSMutableArray array];
    int width = SCREEN_WIDTH - 22;
    NSMutableArray *aryPath = hotData.aryImgPath;
    for (int i = 0; i < aryPath.count; i++) {
        UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * width, 0, width, 160)];
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
        [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
    }
    
    pageControl.numberOfPages = aryPath.count;
    
    tblHeight = SCREEN_HEIGHT - 430 - contentHeight;
    if (tblHeight < 350)
        tblHeight = 350;
    
    tblEvalView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, tblHeight)];
    tblEvalView.dataSource = self;
    tblEvalView.delegate = self;
    tblEvalView.separatorStyle = UITableViewCellSeparatorStyleNone;
    tblEvalView.scrollEnabled = NO;
    [tblEvalView registerNib:[UINib nibWithNibName:@"HotDetailPersonalCell" bundle:nil] forCellReuseIdentifier:@"HotDetailPersonalCellIdentifier"];

    [scrollInfoView addSubview:tblEvalView];
    
    tblOfficeView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, tblHeight)];
    tblOfficeView.dataSource = self;
    tblOfficeView.delegate = self;
    tblOfficeView.separatorStyle = UITableViewCellSeparatorStyleNone;
    tblOfficeView.scrollEnabled = NO;
    [tblOfficeView registerNib:[UINib nibWithNibName:@"HotDetailOfficeCell" bundle:nil] forCellReuseIdentifier:@"HotDetailOfficeCellIdentifier"];
    
    [scrollInfoView addSubview:tblOfficeView];
    
    [scrollInfoView setContentSize:CGSizeMake(SCREEN_WIDTH * 2, tblHeight)];
    [scrollInfoView setFrame:CGRectMake(scrollInfoView.frame.origin.x, scrollInfoView.frame.origin.y, SCREEN_WIDTH * 2, tblHeight)];
    
    scrollInfoView.delegate = self;
    int scrollDataViewHeightValue = 315 + contentHeight + tblHeight;
    [scrollDataViewHeight setConstant:scrollDataViewHeightValue];
    [_scrollDataView setContentSize:CGSizeMake(SCREEN_WIDTH, scrollDataViewHeightValue)];
    
    [scrollinfoviewHeight setConstant:tblHeight];
//    [scrollInfoView layoutIfNeeded];
//    [scrollInfoView updateConstraintsIfNeeded];
    
    aryEvalData = [[NSMutableArray alloc] init];
    aryOfficeData = [[NSMutableArray alloc] init];
    

    [self onClickPersonalButton:nil];
    self.electCountLabel.text = [NSString stringWithFormat:@"%d", (int)hotData.mCommentCnt];
    if(hotData.isElectedByMe) {
        [self.imgElect setImage:[UIImage imageNamed:@"hot_zan_sel.png"]];
    } else {
        [self.imgElect setImage:[UIImage imageNamed:@"hot_zan.png"]];
    }
    
    self.favouriteCountLabel.text = [NSString stringWithFormat:@"%d", (int)hotData.mElectCnt];
    self.hotButton.selected = hotData.bIsFavourite;
    if(self.hotButton.selected)
    {
        self.lblShouChang.text = @"已收藏";
    }else
    {
        self.lblShouChang.text = @"收藏";
    }
    
    [self incViewCount];
    //
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showMoreReplyView:) name:SHOW_HOT_MORE_REPLY_VIEW_NOTIFICATION object:nil];
    // NSNotification for Update Interesting
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateInterestingView:) name:UPDATE_HOT_INTERESTING_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateEvaluateView:) name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    
    __weak typeof(self) weakSelf = self;
    topEvalRefreshView = [tblEvalView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopEvalItems];
    }];
    bottomEvalRefreshView = [tblEvalView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomEvalItems];
    }];
    
    refreshEvalStartIndex = 0;

    topOfficeRefreshView = [tblOfficeView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopOfficeItems];
    }];
    bottomOfficeRefreshView = [tblOfficeView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomOfficeItems];
    }];
    
    refreshOfficeStartIndex = 0;
    [tblEvalView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
    [tblOfficeView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
}


- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(tblEvalView == object) {
        tblEvalView.frame = CGRectMake(tblEvalView.frame.origin.x, 0, tblEvalView.frame.size.width, tblEvalView.contentSize.height);
        fEvalTableContentSizeHeight = tblEvalView.frame.size.height;
    }else if(tblOfficeView == object) {
        tblOfficeView.frame = CGRectMake(tblOfficeView.frame.origin.x, 0, tblOfficeView.frame.size.width, tblOfficeView.contentSize.height);
        fOfficeTableContentSizeHeight = tblOfficeView.frame.size.height;
    }
   // [self setScrollContentSize];
    [self setScrollInfoContentSize ];
}

- (void)dealloc
{
    [tblEvalView removeObserver:self forKeyPath:@"contentSize" context:NULL];
   
}
- (void)updateEvaluateView:(NSNotification *)notification {
    [GeneralUtil showProgress];
    NSMutableArray *evaluateMeArray = (NSMutableArray *)(notification.object);
    NSNumber *evaluateID = evaluateMeArray[0];
    NSNumber *evaluateValue = evaluateMeArray[1];
    NSNumber *evaluateIndexString = evaluateMeArray[2];
    NSInteger evaluateIndex = [evaluateIndexString integerValue];
    NSNumber *evaluateTotalValue = evaluateMeArray[3];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"elect" forKey:@"pAct"];
    [dicParams setObject:evaluateID forKey:@"estimateId"];
    [dicParams setObject:evaluateValue forKey:@"val"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_UPDATE_EVALUATE_ME Parameters:dicParams :^(NSObject *resObj) {
        
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                //[self.noNetworkView setHidden:YES];
                NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[aryEvalData objectAtIndex:evaluateIndex];
                evaluateDic[@"electCnt"]  = evaluateTotalValue;
                evaluateDic[@"isElectedByMe"] = evaluateValue;
                [aryEvalData replaceObjectAtIndex:evaluateIndex withObject:evaluateDic];
//                [self.tblEvalView reloadData];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
                //[self.noNetworkView setHidden:NO];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
//    [self.scrollDataView setContentSize:CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT + 160)];
//    [self.tblEvalView setFrame:CGRectMake(self.tblEvalView.frame.origin.x, self.tblEvalView.frame.origin.y, self.tblEvalView.frame.size.width, self.scrollInfoView.frame.size.height)];
//    [self.tblOfficeView setFrame:CGRectMake(self.tblOfficeView.frame.origin.x, self.tblOfficeView.frame.origin.y, self.tblOfficeView.frame.size.width, self.scrollInfoView.frame.size.height)];
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    //[self getHotEvaluateDataFromServer:refreshEvalStartIndex Length:REFRESH_GET_DATA_COUNT];
    //[self getHotOfficeDataFromServer:refreshOfficeStartIndex Length:REFRESH_GET_DATA_COUNT];
    [self refreshTopEvalItems];
    [self refreshTopOfficeItems];
    
    topEvalRefreshView.showPullToRefresh = YES;
    bottomEvalRefreshView.showPullToRefresh = YES;
    topOfficeRefreshView.showPullToRefresh = YES;
    bottomOfficeRefreshView.showPullToRefresh = YES;
    
    bClickPersonal = YES;

}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    topEvalRefreshView.showPullToRefresh = NO;
    bottomEvalRefreshView.showPullToRefresh = NO;
    topOfficeRefreshView.showPullToRefresh = NO;
    bottomOfficeRefreshView.showPullToRefresh = NO;
}

- (void)refreshTopEvalItems {
    refreshEvalStartIndex = 0;
    if(aryEvalData)
        [aryEvalData removeAllObjects];
    [self getHotEvaluateDataFromServer:refreshEvalStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshBottomEvalItems {
    refreshEvalStartIndex = aryEvalData.count;
    [self getHotEvaluateDataFromServer:refreshEvalStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshTopOfficeItems {
    refreshOfficeStartIndex = 0;
    if(aryOfficeData)
        [aryOfficeData removeAllObjects];
    [self getHotOfficeDataFromServer:refreshOfficeStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshBottomOfficeItems {
    refreshOfficeStartIndex = aryOfficeData.count;
    [self getHotOfficeDataFromServer:refreshOfficeStartIndex Length:REFRESH_GET_DATA_COUNT];
}

-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}
- (void)incViewCount {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"incViewCount" forKey:@"pAct"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)hotData.mId] forKey:@"hotId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:@"2" forKey:@"kind"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_INCVIEWCOUNT Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
//                [self refreshTopEvalItems];
            }
        }
    }];
}
#pragma -mark NSNotification
- (void)showMoreReplyView:(NSNotification *) notification {
    if(self.tblEvalView) {
        [self.tblEvalView reloadData];
    }
}

- (void)getHotEvaluateDataFromServer:(NSInteger)start Length:(NSInteger)length {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getEstimateListForHot" forKey:@"pAct"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)hotData.mId] forKey:@"hotId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:start] forKey:@"start"];
    [dicParams setObject:[NSNumber numberWithInt:length] forKey:@"length"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETESTIMATELISTFORHOT Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topEvalRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomEvalRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
//                [aryEvalData removeAllObjects];
                for (int i = 0; i < aryData.count; i++) {
                    [aryEvalData addObject:aryData[i]];
                }
                if ( aryEvalData.count > 0 )
                {
                    [evaluateHeightArray removeAllObjects];
                    for(int i = 0; i < aryEvalData.count; i++) {
                        [evaluateHeightArray addObject:@"0"];
                    }
                    [self.tblEvalView reloadData];
                    [self.tblEvalView layoutIfNeeded];
                    fEvalTableContentSizeHeight = self.tblEvalView.contentSize.height;
                    self.tblEvalView.frame = CGRectMake(self.tblEvalView.frame.origin.x, self.tblEvalView.frame.origin.y, self.tblEvalView.frame.size.width, fEvalTableContentSizeHeight);
                    [self setScrollInfoContentSize];
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

- (void)getHotOfficeDataFromServer:(NSInteger)start Length:(NSInteger)length {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getPartnerList" forKey:@"pAct"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)hotData.mId] forKey:@"hotId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:start] forKey:@"start"];
    [dicParams setObject:[NSNumber numberWithInt:length] forKey:@"length"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPARTNERLIST Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topOfficeRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomOfficeRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
//                [aryOfficeData removeAllObjects];
                for (int i = 0; i < aryData.count; i++) {
                    [aryOfficeData addObject:aryData[i]];
                }
                
                [self.tblOfficeView reloadData];
                [self.tblOfficeView layoutIfNeeded];
                fOfficeTableContentSizeHeight = self.tblOfficeView.contentSize.height;
                self.tblOfficeView.frame = CGRectMake(self.tblOfficeView.frame.origin.x, self.tblOfficeView.frame.origin.y, self.tblOfficeView.frame.size.width, fOfficeTableContentSizeHeight);
                [self setScrollInfoContentSize];
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
    /*
    if ( scrollView == scrollPicView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        [pageControl setCurrentPage:page];
    }
     */
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

#pragma UITableViewDelegate & UITableViewDataSource
- (void)updateTableScrollHeight {
/*    CGFloat fTableHeight;
    if(self.tblEvalView.contentSize.height > self.tblOfficeView.contentSize.height) {
        fTableHeight = self.tblEvalView.contentSize.height;
    }else{
        fTableHeight = self.tblOfficeView.contentSize.height;
    }
    [self.scrollDataView setContentSize:CGSizeMake(SCREEN_WIDTH, 394 + fTableHeight)];
    [self.scrollInfoView setFrame:CGRectMake(self.scrollInfoView.frame.origin.x, self.scrollInfoView.frame.origin.y, self.scrollInfoView.frame.size.width, fTableHeight)];
    [self.scrollInfoView setContentSize:CGSizeMake(self.scrollInfoView.frame.size.width, fTableHeight)];
    [self.tblEvalView setFrame:CGRectMake(self.tblEvalView.frame.origin.x, self.tblEvalView.frame.origin.y, self.tblEvalView.frame.size.width, fTableHeight)];
    [self.tblEvalView setContentSize:CGSizeMake(self.tblEvalView.frame.size.width, fTableHeight)];
    [self.tblOfficeView setFrame:CGRectMake(self.tblOfficeView.frame.origin.x, self.tblOfficeView.frame.origin.y, self.tblOfficeView.frame.size.width, fTableHeight)];
    [self.tblOfficeView setContentSize:CGSizeMake(self.tblOfficeView.frame.size.width, fTableHeight)];*/
    if(officeSeparator.hidden) {
        [self onClickPersonalButton:nil];
    }else{
        [self onClickOfficeButton:nil];
    }
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if ( tableView == tblEvalView)
        return aryEvalData.count;
    else if (tableView == tblOfficeView)
        return aryOfficeData.count;
    
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {

//    [self updateTableScrollHeight];
    if ( tableView == tblEvalView) {
        if(aryEvalData.count <= indexPath.row)
            return 173.f;

        NSDictionary *evaluateDic = (NSDictionary *)[aryEvalData objectAtIndex:indexPath.row];
        NSMutableArray *replyArray = evaluateDic[@"replys"];
        if(replyArray.count == 0) {
            return 173.f;
        }else{
            CGFloat replyHeight = 0.f;
            if(evaluateHeightArray.count > indexPath.row) {
                NSString *strReplyHeight = (NSString *) [evaluateHeightArray objectAtIndex:indexPath.row];
                replyHeight = [strReplyHeight floatValue];
            }
//            if(replyArray.count > 1) {
//                replyHeight += 20;
//            }
            //return 158.f + replyHeight;
            return 192.f + replyHeight;
        }
    }
    else if (tableView == tblOfficeView) {
        CGFloat homeTableCellHeight = 180.f;
        if(aryOfficeData.count <= indexPath.row)
            return homeTableCellHeight;
        NSDictionary *friendDic = (NSDictionary *)(aryOfficeData[indexPath.row]);
        NSArray *productsArray = (NSArray *)(friendDic[@"products"]);
        if(productsArray.count == 0 )
            homeTableCellHeight -= 23.f;
        NSArray *itemsArray = (NSArray *)(friendDic[@"items"]);
        if(itemsArray.count == 0 )
            homeTableCellHeight -= 23.f;
        NSArray *servicessArray = (NSArray *)(friendDic[@"services"]);
        if(servicessArray.count == 0 )
            homeTableCellHeight -= 23.f;
/*        NSInteger aKind = [friendDic[@"akind"] integerValue];
        if (aKind == PERSONAL_KIND) {
            if([friendDic[@"reqCodeSenderRealname"] isEqualToString:@""]) {
                homeTableCellHeight -= 23.f;
            }
        }else{
            if([friendDic[@"reqCodeSenderEnterName"] isEqualToString:@""]) {
                homeTableCellHeight -= 23.f;
            }
        }*/
        return homeTableCellHeight;
    }
    return 0;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if ( tableView == tblEvalView)
    {
        NSString *simpleTableIdentifier = @"HotDetailPersonalCellIdentifier";
        HotDetailPersonalCell *cell = (HotDetailPersonalCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
        if(aryEvalData.count <= indexPath.row)
            return cell;
        NSDictionary *dic = aryEvalData[indexPath.row];
        cell.cellIndex = indexPath.row;
        cell.electCount = [aryEvalData[indexPath.row][@"electCnt"] intValue];
        cell.evaluateID = [aryEvalData[indexPath.row][@"id"] intValue];
        
        NSInteger ownerAkind = [dic[@"ownerAkind"] integerValue];
        
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:ownerAkind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
        
        NSString *strUserName = @"";
        if(ownerAkind == PERSONAL_KIND) {
            strUserName = dic[@"ownerRealname"];
        }else{
            strUserName = dic[@"ownerEnterName"];
        }
        if(strUserName.length > EVALUATE_DETAIL_TITLE_MAX_LENGTH) {
            strUserName = [NSString stringWithFormat:@"%@…", [strUserName substringWithRange:NSMakeRange(0, EVALUATE_DETAIL_TITLE_MAX_LENGTH)]];
        }
        cell.lblTitle.text = strUserName;

        NSString *strDate = dic[@"writeTimeString"];
        cell.lblDate.text = [strDate substringWithRange:NSMakeRange(0, strDate.length - 3)];
        for( UIView* subV in [cell.scrollThumb subviews])
        {
            [subV removeFromSuperview];
        }
        NSArray *aryPath = dic[@"imgPaths"];

        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 90 - 10, 80)];
        cell.lblContent.text = dic[@"content"];
        cell.lblElect.text = [NSString stringWithFormat:@"%ld", [dic[@"electCnt"] longValue]];
        long isElectedByMe = [dic[@"isElectedByMe"] longValue];
        if(isElectedByMe == 1)
            cell.zanButton.selected = YES;
        else
            cell.zanButton.selected = NO;

        cell.lblReply.text = @"";
        cell.moreReplyView.hidden = YES;
        //cell.separatorLine.hidden = YES;
        
        NSMutableArray *replyArray = dic[@"replys"];
        cell.evaluateCountLabel.text = [NSString stringWithFormat:@"%d", (int)replyArray.count];
        if(replyArray.count > 0) {
            cell.lblReply.hidden = NO;
            if(replyArray.count > 1) {
                cell.moreReplyLabel.text = [NSString stringWithFormat:@"查看全部%d条回复 >", (int)(replyArray.count)];
            }
            cell.moreReplyView.hidden = NO;
            //cell.separatorLine.hidden = NO;
            NSMutableAttributedString *totalContent = [[NSMutableAttributedString alloc] initWithString:@""];
//            for(int i = 0; i < replyArray.count; i++) {
            if(replyArray.count > 0) {
                NSDictionary *replyDic = (NSDictionary *)[replyArray objectAtIndex:0];
                NSInteger ownerAkind = [replyDic[@"ownerAkind"] integerValue];
                NSString *ownerName;
                if(ownerAkind == PERSONAL_KIND) {
                    ownerName = replyDic[@"ownerRealname"];
                }else{
                    ownerName = replyDic[@"ownerEnterName"];
                }
                NSString *replyContent = replyDic[@"content"];
                NSMutableAttributedString *replyText = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@: ", ownerName] attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12], NSForegroundColorAttributeName:RGB_COLOR(23, 133, 229)}];

                                                 //stringWithFormat:@"%@ : %@", ownerName, replyContent];
                [replyText appendAttributedString:[[NSAttributedString alloc]  initWithString:replyContent attributes:@{NSForegroundColorAttributeName:RGB_COLOR(102, 102, 102), NSFontAttributeName:[UIFont systemFontOfSize:12]} ]];
                totalContent = replyText;
//                if(!cell.moreReplyButton.selected)
//                    break;
            }
            cell.lblReply.attributedText = totalContent;
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.tblEvalView beginUpdates];
                [cell.lblReply sizeToFit];
                NSString *strReplyHeight = [NSString stringWithFormat:@"%f", cell.lblReply.frame.size.height];
                [evaluateHeightArray replaceObjectAtIndex:indexPath.row withObject:strReplyHeight];
                [self.tblEvalView endUpdates];
            });
        }
        return cell;
    }
    else if ( tableView == tblOfficeView)
    {
        NSString *simpleTableIdentifier = @"HotDetailOfficeCellIdentifier";
        HotDetailOfficeCell *cell = (HotDetailOfficeCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
        if(aryOfficeData.count <= indexPath.row)
            return cell;

        NSDictionary *dic = aryOfficeData[indexPath.row];
        NSString *logoImageName = dic[@"logo"];
        NSInteger aKind = [dic[@"akind"] integerValue];
        if(logoImageName) {
            [cell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:aKind == 1 ? @"no_image_person.png" : @"no_image_enter.png"]];
            
        }
        NSString *strUserName;
        if (aKind == PERSONAL_KIND) {
            strUserName = dic[@"realname"];
            cell.officeMarkLabel.text = @"个人";
        }else {
            strUserName = dic[@"enterName"];
            cell.officeMarkLabel.text = @"企业";//dic[@"enterKindName"];
        }
        
        if([strUserName isEqualToString:@""])
            strUserName = dic[@"mobile"];
        
        if(strUserName.length > HOME_NAME_MAX_LENGTH) {
            strUserName = [NSString stringWithFormat:@"%@…", [strUserName substringWithRange:NSMakeRange(0, HOME_NAME_MAX_LENGTH)]];
        }
        cell.nameLabel.text = strUserName;

        int nReqCodeSenderAKind = [dic[@"reqCodeSenderAkind"] intValue];
        NSString *reqName = @"";
        if([dic[@"reqCodeSenderId"] longValue] > 0) {
            if(nReqCodeSenderAKind == PERSONAL_KIND) {
                reqName = dic[@"reqCodeSenderRealname"];
            }else{
                reqName = dic[@"reqCodeSenderEnterName"];
            }
            if([reqName isEqualToString:@""]) {
                reqName = dic[@"reqCodeSenderMobile"];
            }
            cell.reqViewHeightConstraint.constant = 23.f;
        }else{
            cell.reqViewHeightConstraint.constant = 0.f;
        }
        if([dic[@"inviterFriendLevel"] isEqualToString:@""]) {
            cell.reqCodeSenderLabel.text = reqName;
        }else{
            cell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%@-%@", dic[@"inviterFriendLevel"], reqName];
        }
        if(cell.reqCodeSenderLabel.text.length == 0) {
            cell.reqView.hidden = YES;
        }else{
            cell.reqView.hidden = NO;
        }
        [cell.nameLabel sizeToFit];
        NSString *xyName= dic[@"xyName"];
        if ([xyName isEqualToString:@""]) {
            [cell.xyNameButton setHidden:YES];
        } else {
            if(xyName.length > HOME_TAG_MAX_LENGTH) {
                xyName = [NSString stringWithFormat:@"%@…", [xyName substringWithRange:NSMakeRange(0, HOME_TAG_MAX_LENGTH)]];
            }
            [cell.xyNameButton setHidden:NO];
            [cell.xyNameButton setTitle:xyName forState:UIControlStateNormal];
        }
        dispatch_async(dispatch_get_main_queue(), ^{
            CGSize stringSize = [cell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
            int bw = stringSize.width + 12;

//            if(cell.nameLabel.frame.size.width > (tableView.frame.size.width - 75 - stringSize.width - cell.nameLabel.frame.origin.x)) {
//                [cell.nameLabel setFrame:CGRectMake(cell.nameLabel.frame.origin.x, cell.nameLabel.frame.origin.y, tableView.frame.size.width - 75 - stringSize.width - cell.nameLabel.frame.origin.x, cell.nameLabel.frame.size.height)];
//            }
            CGRect nameLabelFrame = cell.nameLabel.frame;
            [cell.xyNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y + 2, bw, 16)];
        });
        
        cell.codeLabel.text = dic[@"code"];
        NSArray *productsArray = (NSArray *)(dic[@"products"]);
        
        if(productsArray.count > 0 ) {
            NSString *productNames = @"";
            for(int i = 0; i < productsArray.count; i++) {
                NSDictionary *productDic = (NSDictionary *)(productsArray[i]);
                if(i == 0){
                    productNames = productDic[@"name"];
                }else{
                    productNames = [NSString stringWithFormat:@"%@,%@", productNames, productDic[@"name"]];
                }
            }
            cell.productsLabel.text = productNames;
            cell.productViewHeightConstraint.constant = 23.f;
            
        }else{
            cell.productViewHeightConstraint.constant = 0.f;
        }
        
        NSArray *itemsArray = (NSArray *)(dic[@"items"]);
        if(itemsArray.count > 0 ) {
            NSString *itemNames = @"";
            for(int i = 0; i < itemsArray.count; i++) {
                NSDictionary *itemDic = (NSDictionary *)(itemsArray[i]);
                if(i == 0){
                    itemNames = itemDic[@"name"];
                }else{
                    itemNames = [NSString stringWithFormat:@"%@,%@", itemNames, itemDic[@"name"]];
                }
            }
            cell.itemLabel.text = itemNames;
            cell.itemViewHeightConstraint.constant = 23.f;
        }else{
            cell.itemViewHeightConstraint.constant = 0.f;
        }
        
        NSArray *servicessArray = (NSArray *)(dic[@"services"]);
        if(servicessArray.count > 0 ) {
            NSString *serviceNames = @"";
            for(int i = 0; i < servicessArray.count; i++) {
                NSDictionary *serviceDic = (NSDictionary *)(servicessArray[i]);
                if(i == 0){
                    serviceNames = serviceDic[@"name"];
                }else{
                    serviceNames = [NSString stringWithFormat:@"%@,%@", serviceNames, serviceDic[@"name"]];
                }
            }
            cell.serviceLabel.text = serviceNames;
            cell.serviceViewHeightConstraint.constant = 23.f;
        }else{
            cell.serviceViewHeightConstraint.constant = 0.f;
        }
        
        
        if([dic[@"interested"] integerValue] == 1) {
            cell.interestedButton.selected = NO;
        }else{
            cell.interestedButton.selected = YES;
        }
        
        cell.accountID = [NSString stringWithFormat:@"%d", (int)[dic[@"id"] intValue]];
        cell.cellIndex = indexPath.row;
        cell.cellType = SUB_HOME_PERSONAL;
        return cell;
    }
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (tableView == tblEvalView) {
        EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
        NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[aryEvalData objectAtIndex:indexPath.row];
        detailViewController.dicEvalData = evaluateDic;
        detailViewController.isHotEvaluator = YES;
        [self.navigationController pushViewController:detailViewController animated:YES];
    }
    else if ( tableView == tblOfficeView)
    {
        NSDictionary *friendDic = (NSDictionary *)[aryOfficeData objectAtIndex:indexPath.row];
        [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%d", (int)[friendDic[@"id"] intValue]];
        HomeFamiliarDetailViewController *vc = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
        [self.navigationController pushViewController:vc animated:YES];
    }
}

#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    if (scrollView == self.scrollInfoView) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        if (page == 0) {
            [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
            [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
            personalSeparator.hidden = NO;
            officeSeparator.hidden = YES;
        }
        else if (page == 1)
        {
            [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
            [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
            officeSeparator.hidden = NO;
            personalSeparator.hidden = YES;
        }
    }
}

#pragma mark - UITextFieldDelegate
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    self.electTextTrailingConstraint.constant = 0;
    self.electSendButton.hidden = NO;
    if(textField == self.electContentTextField)
    {
        self.electContentTextField.text = @"";
    }
    return YES;
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField {
    self.electTextTrailingConstraint.constant = 204.f;
    self.electSendButton.hidden = YES;
    return YES;
}
-(IBAction)onWriteEstimation:(id)sender
{
    HotEstimationViewController *vc = [[HotEstimationViewController alloc] initWithNibName:@"HotEstimationViewController" bundle:nil];
    vc.hotData = hotData;
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onEstimationList:(id)sender
{
    HotEstimationListViewController* vc = [[HotEstimationListViewController alloc] initWithNibName:@"HotEstimationListViewController" bundle:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    [self.navigationController pushViewController:vc animated:YES];
    vc.aryEvalData = aryEvalData;
}
-(IBAction)onElect:(id)sender
{
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"electHot" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInteger:hotData.mId] forKey:@"hotId"];
    [dicParams setObject:[NSNumber numberWithInt:hotData.isElectedByMe == 1 ? 0 : 1] forKey:@"val"];
    [GeneralUtil showProgress];
    [[WebAPI sharedInstance] sendPostRequest:@"electHot" Parameters:dicParams :^(NSObject *resObj) {
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                if(hotData.isElectedByMe)
                {
                    hotData.mElectCnt -= 1;
                    hotData.isElectedByMe = 0;
                    [self.imgElect setImage:[UIImage imageNamed:@"hot_zan.png"]];
                }else
                {
                    hotData.mElectCnt += 1;
                    hotData.isElectedByMe = 1;
                    [self.imgElect setImage:[UIImage imageNamed:@"hot_zan_sel.png"]];
                }
                
                self.favouriteCountLabel.text = [NSString stringWithFormat:@"%ld", (long)hotData.mElectCnt];
            }
            else {
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
- (IBAction)onClickPersonalButton:(UIButton *)sender {
    [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    personalSeparator.hidden = NO;
    officeSeparator.hidden = YES;

    bClickPersonal = YES;
    [scrollInfoView setContentOffset:CGPointMake(0, 0)];
    [self setScrollInfoContentSize];
}

- (IBAction)onClickOfficeButton:(UIButton *)sender {
    [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    officeSeparator.hidden = NO;
    personalSeparator.hidden = YES;
    bClickPersonal = NO;
    [scrollInfoView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
    [self setScrollInfoContentSize];
}

- (void)setScrollInfoContentSize {
    CGFloat fWebViewIncreaseHeight = 0;
    if(contentWebView.scrollView.contentSize.height > 193) {
        [contentWebView.scrollView setFrame:CGRectMake(contentWebView.scrollView.frame.origin.x, contentWebView.scrollView.frame.origin.y, contentWebView.scrollView.frame.size.width, contentWebView.scrollView.contentSize.height)];
        [contentWebView setFrame:CGRectMake(contentWebView.frame.origin.x, contentWebView.frame.origin.y, contentWebView.frame.size.width, contentWebView.scrollView.contentSize.height)];
        if(self.tableContentHeight.constant == 275.f){
            fWebViewIncreaseHeight = contentWebView.scrollView.contentSize.height - 193;
            self.tableContentHeight.constant += fWebViewIncreaseHeight;
        }
    }else{
        [contentWebView.scrollView setFrame:CGRectMake(contentWebView.scrollView.frame.origin.x, contentWebView.scrollView.frame.origin.y, contentWebView.scrollView.frame.size.width, 193)];
        [contentWebView setFrame:CGRectMake(contentWebView.frame.origin.x, contentWebView.frame.origin.y, contentWebView.frame.size.width, 193)];
        self.tableContentHeight.constant = 275.f;
    }
    if(bClickPersonal) {
        if(fEvalTableContentSizeHeight == 0) {
            self.blankView.hidden = NO;
            [self.scrollDataView setContentSize:CGSizeMake(self.scrollDataView.frame.size.width, self.tableContentHeight.constant + self.tableContentView.frame.size.height + 238 + fWebViewIncreaseHeight)];
            self.scrollinfoviewHeight.constant = 238;
        }else{
            self.blankView.hidden = YES;
            [self.scrollDataView setContentSize:CGSizeMake(self.scrollDataView.frame.size.width, self.tableContentHeight.constant + self.tableContentView.frame.size.height + fEvalTableContentSizeHeight + fWebViewIncreaseHeight)];
            self.scrollinfoviewHeight.constant = fEvalTableContentSizeHeight;
        }
    }else{
        if(fOfficeTableContentSizeHeight == 0) {
            self.blankView.hidden = NO;
            [self.scrollDataView setContentSize:CGSizeMake(self.scrollDataView.frame.size.width, self.tableContentHeight.constant + self.tableContentView.frame.size.height + 238 + fWebViewIncreaseHeight)];
            self.scrollinfoviewHeight.constant = 238;
        }else{
            self.blankView.hidden = YES;
            [self.scrollDataView setContentSize:CGSizeMake(self.scrollDataView.frame.size.width, self.tableContentHeight.constant + self.tableContentView.frame.size.height + fOfficeTableContentSizeHeight + fWebViewIncreaseHeight)];
            self.scrollinfoviewHeight.constant = fOfficeTableContentSizeHeight;
        }
    }
    self.scrollDataViewHeight.constant = self.scrollDataView.contentSize.height;
}
- (IBAction)onClickNavBackButton:(id)sender {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onSendElectContent:(id)sender {
    [self.view endEditing:YES];
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"leaveEstimate" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:@"2" forKey:@"type"];
    [dicParams setObject:[NSNumber numberWithInteger:hotData.mId] forKey:@"hotId"];
    [dicParams setObject:self.electContentTextField.text forKey:@"content"];
    
    [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_LEAVEESTIMATE Parameters:dicParams UploadImages: nil :^(NSObject *resObj) {
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSMutableDictionary *electDic = [[NSMutableDictionary alloc] init];
                [electDic setObject:[CommonData sharedInstance].userInfo[@"akind"] forKey:@"ownerAkind"];
                [electDic setObject:[CommonData sharedInstance].userInfo[@"logo"] forKey:@"ownerLogo"];
                [electDic setObject:[CommonData sharedInstance].userInfo[@"realname"] forKey:@"ownerRealname"];
                [electDic setObject:[CommonData sharedInstance].userInfo[@"enterName"] forKey:@"ownerEnterName"];
                [electDic setObject:self.electContentTextField.text forKey:@"content"];
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                [formatter setTimeZone:[NSTimeZone systemTimeZone]];
                NSString *formatString = @"yyyy-MM-dd HH:mm:ss";
                [formatter setDateFormat:formatString];
                NSDate *currentDate = [NSDate date];
                NSString *dateString = [formatter stringFromDate:currentDate];
                [electDic setObject:dateString forKey:@"writeTimeString"];
                [aryEvalData insertObject:electDic atIndex:0];
                self.electContentTextField.text = @"";
                hotData.mCommentCnt += 1;
                self.electCountLabel.text = [NSString stringWithFormat:@"%d", (int)hotData.mCommentCnt];
                [self.tblEvalView reloadData];
            }
            else {
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

- (IBAction)onHotAction:(id)sender {
    
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setFavourite" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:@"2" forKey:@"kind"];
    [dicParams setObject:[NSNumber numberWithInteger:hotData.mId]  forKey:@"id"];
    [dicParams setObject:self.hotButton.selected? @"0" : @"1" forKey:@"val"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETFAVOURITE Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                self.hotButton.selected = !self.hotButton.selected;
                if(self.hotButton.selected)
                {
                    self.lblShouChang.text = @"已收藏";
                    [appDelegate.window makeToast:@"收藏成功"
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }else
                {
                    self.lblShouChang.text = @"收藏";
                    [appDelegate.window makeToast:@"收藏取消"
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
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

- (void)updateInterestingView:(NSNotification *)notification {
    NSDictionary *interstedDic = (NSDictionary *)(notification.object);
    NSInteger cellIndex = [interstedDic[@"cellIndex"] integerValue];
    BOOL isInterested = [interstedDic[@"isInterested"] boolValue];
    NSMutableDictionary *friendDic = (NSMutableDictionary *)(aryOfficeData[cellIndex]);
    friendDic[@"interested"] = isInterested? @"1" : @"0";
    [aryOfficeData replaceObjectAtIndex:cellIndex withObject:friendDic];
    [self.tblOfficeView reloadData];
}

- (IBAction)onShareAction:(id)sender {
    [self shareMenu];
}

#pragma mark ShareSDK
- (void)shareMenu
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    NSArray* imageArray = @[[[NSBundle mainBundle] pathForResource:@"no_image@2x" ofType:@"png"]];
    
    NSString *hotImageName = @"";
    if (hotData.aryImgPath.count > 0) {
        hotImageName = hotData.aryImgPath[0];
    }
    
    if (hotImageName) {
        NSURL *imgUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, hotImageName]];
        imageArray = @[imgUrl];
    }
    NSString *shareTitle = hotData.strTitle;
    NSString *url = [NSString stringWithFormat:@"%@%@%ld", BASE_WEB_URL, @"/redian.html?hotId=",(long)hotData.mId];
    NSString *shareText = [[[NSAttributedString alloc] initWithData:[hotData.strSummary dataUsingEncoding:NSUTF8StringEncoding] options:@{NSDocumentTypeDocumentAttribute:NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute:[NSNumber numberWithInt:NSUTF8StringEncoding]} documentAttributes:nil error:nil] string];
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
    [dicParams setObject:[NSNumber numberWithInteger:7] forKey:@"kind"];
    
    [dicParams setObject:[NSNumber numberWithInteger:hotData.mId] forKey:@"id"];
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
