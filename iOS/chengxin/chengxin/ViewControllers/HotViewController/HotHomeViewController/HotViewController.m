//
//  HotViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/24/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotViewController.h"
#import "HotTableViewCell.h"
#import "HotDetailViewController.h"
#import "WebAPI.h"
#import "Global.h"
#import "HotObject.h"
#import "UIImageView+WebCache.h"
#import "AAPullToRefresh.h"

@interface HotViewController ()
{
    NSMutableArray *aryPhoto;
    NSMutableArray *aryHotData;
    
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;
    NSMutableArray* aryHeights;
}
@end

@implementation HotViewController
@synthesize tblHotView;
@synthesize messageNumberLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    aryHeights = [[NSMutableArray alloc] init];
    tblHotView.delegate = self;
    tblHotView.dataSource = self;
    [tblHotView registerNib:[UINib nibWithNibName:@"HotTableViewCell" bundle:nil] forCellReuseIdentifier:@"HotTableCellIdentifier"];

    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
    
    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];
    UIImage *img3 = [UIImage imageNamed:@"1100"];

    aryPhoto = [[NSMutableArray alloc] init];
    [aryPhoto addObject:img1];
    [aryPhoto addObject:img2];
    [aryPhoto addObject:img3];

    aryHotData = [[NSMutableArray alloc] init];
    
    self.viewBlank.hidden = YES;
    
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.tblHotView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.tblHotView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;

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

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(aryHotData)
        [aryHotData removeAllObjects];
    [self getHotListFromServer];
}

- (void)refreshBottomItems {
    refreshStartIndex = aryHotData.count;
    [self getHotListFromServer];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (void) getHotListFromServer {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getHotList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", refreshStartIndex] forKey:@"start"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT] forKey:@"length"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETHOTLIST Parameters:dicParams :^(NSObject *resObj) {
        
        [GeneralUtil hideProgress];
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                if(aryData.count == 0)
                    self.viewBlank.hidden = NO;
                else
                    self.viewBlank.hidden = YES;
//                [aryHotData removeAllObjects];
                for (int i = 0; i < aryData.count; i++) {
                    [aryHotData addObject:[self parseHotObjFromData:aryData[i]]];
                }
                for(int i = 0; i < aryData.count; i++)
                {
                    [aryHeights addObject:[NSNumber numberWithFloat:60.0f]];
                }
                [tblHotView reloadData];
            }
            else
            {
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

- (HotObject*) parseHotObjFromData:(NSDictionary*) data {
    HotObject *hot = [[HotObject alloc] init];
    hot.mCommentCnt = [[data valueForKey:@"commentCnt"] integerValue];
    hot.strContent = [data valueForKey:@"content"];
    hot.strSummary = [data valueForKey:@"summary"];
    hot.strDownTime = [data valueForKey:@"downTime"];
    hot.mElectCnt = [data[@"electCnt"] integerValue];
    hot.mId = [data[@"id"] integerValue];
    hot.aryImgPath = data[@"imgPaths"];
    hot.bIsFavourite = [data[@"isFavourite"] integerValue] == 1? YES: NO;
    hot.mSerial = [data[@"serial"] integerValue];
    hot.mShareCnt = [data[@"shareCnt"] integerValue];
    hot.mStatus = [data[@"status"] integerValue];
    hot.strStatusName = data[@"statusName"];
    hot.strTitle = data[@"title"];
    hot.strUpTime = data[@"upTime"];
    hot.mVisitCnt = [data[@"visitCnt"] integerValue];
    hot.dicWriteTime = data[@"writeTime"];
    hot.strWriteTimeString = data[@"writeTimeString"];
    hot.mXyleixingId = [data[@"xyleixingId"] integerValue];
    hot.mXyleixingLevel1Id = [data[@"xyleixingLevel1Id"] integerValue];
    hot.mXyleixingLevel1Name = data[@"xyleixingLevel1Name"];
    hot.mXyleixingName = data[@"xyleixingName"];
    hot.isElectedByMe = [data[@"isElectedByMe"] integerValue];
    return hot;
}

- (IBAction)showNotificationView:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
}

#pragma UITableViewDelegate & UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return aryHotData.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if(aryHeights.count > indexPath.row)
        return [aryHeights[indexPath.row] floatValue];
    else
        return 250;
    /*
    if(aryHotData.count <= indexPath.row)
        return 249.0f;
    HotObject *hot = aryHotData[indexPath.row];
    if ( hot.aryImgPath == nil || hot.aryImgPath.count == 0 )
        return 169.0f;
    
    return 249.0f;
     */
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"HotTableCellIdentifier";
    HotTableViewCell *cell = (HotTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    if(aryHotData.count <= indexPath.row)
        return cell;
    HotObject *hot = aryHotData[indexPath.row];
    NSString *strDate = hot.strWriteTimeString;
    
    cell.lblTitle.text = hot.strTitle;
    cell.lblContent.text = hot.strSummary;
    cell.lblRead.text = [NSString stringWithFormat:@"%ld", (long)hot.mVisitCnt];
    cell.lblEval.text = [NSString stringWithFormat:@"%ld", (long)hot.mCommentCnt];
    cell.lblDate.text = [strDate substringToIndex:strDate.length-3];

    NSMutableArray *aryPath = hot.aryImgPath;
    for( UIView* subV in [cell.scrollThumb subviews])
    {
        [subV removeFromSuperview];
    }
    if ( aryPath == nil || aryPath.count == 0  ) {
        //cell.scrollThumb.hidden = YES;
        cell.scrollThumb.userInteractionEnabled = false;
    }
    else
    {
        cell.scrollThumb.userInteractionEnabled = true;
        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 120, 0, 113, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];

            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 120 - 7, 80)];
    }

    dispatch_async(dispatch_get_main_queue(), ^{
        [self.tblHotView beginUpdates];
        [cell.lblTitle sizeToFit];
        [cell.lblContent sizeToFit];
        NSNumber *itemHeight =  [NSNumber numberWithFloat:250 + cell.lblTitle.frame.size.height - 16 + cell.lblContent.frame.size.height - 51 - (aryPath.count == 0 ? 80 : 0)];
        if([itemHeight floatValue] < 60)
            itemHeight = [NSNumber numberWithFloat:60.0f];
        
        [aryHeights replaceObjectAtIndex:indexPath.row  withObject:itemHeight];
        
        [self.tblHotView endUpdates];
    });
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    HotDetailViewController *detailViewController = [[HotDetailViewController alloc] initWithNibName:@"HotDetailViewController" bundle:nil];
    detailViewController.hotData = aryHotData[indexPath.row];
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
    
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

@end
