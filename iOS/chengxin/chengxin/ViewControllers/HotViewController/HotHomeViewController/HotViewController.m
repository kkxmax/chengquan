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

@interface HotViewController ()
{
    NSMutableArray *aryPhoto;
    NSMutableArray *aryHotData;
}
@end

@implementation HotViewController
@synthesize tblHotView;
@synthesize messageNumberLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    tblHotView.delegate = self;
    tblHotView.dataSource = self;
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
    
    [self getHotListFromServer];

    [self updateNotification];
    appDelegate.notificationDelegate = self;

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
- (void) getHotListFromServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getHotList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETHOTLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryHotData removeAllObjects];
                for (int i = 0; i < aryData.count; i++) {
                    [aryHotData addObject:[self parseHotObjFromData:aryData[i]]];
                }
                
                if ( aryHotData.count == 0 )
                {
                    tblHotView.hidden = YES;
                    self.viewNoNetwork.hidden = YES;
                    self.viewBlank.hidden = NO;
                }
                else
                {
                    tblHotView.hidden = NO;
                    self.viewBlank.hidden = YES;
                    self.viewNoNetwork.hidden = YES;
                    [tblHotView reloadData];
                }
            }
            else
            {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
                tblHotView.hidden = YES;
                self.viewBlank.hidden = YES;
                self.viewNoNetwork.hidden = NO;
            }
        }
    }];
}

- (HotObject*) parseHotObjFromData:(NSDictionary*) data {
    HotObject *hot = [[HotObject alloc] init];
    hot.mCommentCnt = [[data valueForKey:@"commentCnt"] integerValue];
    hot.strContent = [data valueForKey:@"content"];
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
    
    HotObject *hot = aryHotData[indexPath.row];
    if ( hot.aryImgPath == nil || hot.aryImgPath.count == 0 )
        return 169.0f;
    
    return 249.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"HotTableViewCell";
    HotTableViewCell *cell = (HotTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HotTableViewCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    HotObject *hot = aryHotData[indexPath.row];
    NSString *strDate = hot.strWriteTimeString;
    
    cell.lblTitle.text = hot.strTitle;
    cell.lblContent.text = hot.strContent;
    cell.lblRead.text = [NSString stringWithFormat:@"%ld", (long)hot.mVisitCnt];
    cell.lblEval.text = [NSString stringWithFormat:@"%ld", (long)hot.mCommentCnt];
    cell.lblDate.text = [strDate substringToIndex:[strDate rangeOfString:@" "].location];

    NSMutableArray *aryPath = hot.aryImgPath;
    if ( aryPath == nil || aryPath.count == 0  ) {
        cell.scrollThumb.hidden = YES;
    }
    else
    {
        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 120, 0, 113, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 120 - 10, 80)];
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
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
