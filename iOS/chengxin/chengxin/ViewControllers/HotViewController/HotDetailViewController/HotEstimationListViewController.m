//
//  HotEstimationListViewController.m
//  chengxin
//
//  Created by common on 5/13/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotEstimationListViewController.h"
#import "HotDetailPersonalCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "EvaluateDetailViewController.h"

@interface HotEstimationListViewController ()<HotEstimationListDelegate>

@end

@implementation HotEstimationListViewController
{
    NSMutableArray *evaluateHeightArray;
    EvaluateDetailViewController *detailViewController;
}
@synthesize aryEvalData;
- (void)viewDidLoad {
    [super viewDidLoad];
    evaluateHeightArray = [[NSMutableArray alloc] init];
    [evaluateHeightArray removeAllObjects];
    for(int i = 0; i < aryEvalData.count; i++) {
        [evaluateHeightArray addObject:@"0"];
    }
    [self.estimationTable reloadData];
    [self.estimationTable registerNib:[UINib nibWithNibName:@"HotDetailPersonalCell" bundle:nil] forCellReuseIdentifier:@"HotDetailPersonalCellIdentifier"];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateEvaluateView:) name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    detailViewController = nil;
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return aryEvalData.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    //[self updateTableScrollHeight];

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
            return 158.f + replyHeight;
        }

        return 0;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] init];

    {
        NSString *simpleTableIdentifier = @"HotDetailPersonalCellIdentifier";
        HotDetailPersonalCell *cell = (HotDetailPersonalCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
        
        cell.cellIndex = indexPath.row;
        NSDictionary *dic = aryEvalData[indexPath.row];
        
        
        NSInteger ownerAkind = [dic[@"ownerAkind"] integerValue];
        
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:ownerAkind ? @"no_image_person.png" : @"no_image_enter.png"]];
        
        if(ownerAkind == PERSONAL_KIND) {
            cell.lblTitle.text = dic[@"ownerRealname"];
        }else{
            cell.lblTitle.text = dic[@"ownerEnterName"];
        }
        NSString *strDate = dic[@"writeTimeString"];
        cell.lblDate.text = strDate;
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
        cell.electCount = [dic[@"electCnt"] longValue];
        long isElectedByMe = [dic[@"isElectedByMe"] longValue];
        if(isElectedByMe == 1)
            cell.zanButton.selected = YES;
        else
            cell.zanButton.selected = NO;
        
        cell.lblReply.text = @"";
        cell.moreReplyView.hidden = YES;
        
        NSMutableArray *replyArray = dic[@"replys"];
        cell.evaluateCountLabel.text = [NSString stringWithFormat:@"%d", (int)replyArray.count];
        if(replyArray.count > 0) {
            cell.lblReply.hidden = NO;
            if(replyArray.count > 1) {
                cell.moreReplyView.hidden = NO;
                cell.moreReplyLabel.text = [NSString stringWithFormat:@"展开全部回复（%d条）>", (int)(replyArray.count)];
            }else{
                cell.moreReplyView.hidden = YES;
            }
            NSString *totalContent = @"";
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
                NSString *replyText = [NSString stringWithFormat:@"%@ : %@", ownerName, replyContent];
                totalContent = [NSString stringWithFormat:@"%@%@\n\n", totalContent, replyText];
                //                if(!cell.moreReplyButton.selected)
                //                    break;
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.estimationTable beginUpdates];
                cell.lblReply.text = totalContent;
                [cell.lblReply sizeToFit];
                NSString *strReplyHeight = [NSString stringWithFormat:@"%f", cell.lblReply.frame.size.height];
                [evaluateHeightArray replaceObjectAtIndex:indexPath.row withObject:strReplyHeight];
                [self.estimationTable endUpdates];
            });
        }
        return cell;
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
    NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[aryEvalData objectAtIndex:indexPath.row];
    detailViewController.dicEvalData = evaluateDic;
    detailViewController.isHotEvaluator = YES;
    detailViewController.hotEstimationListDelegate = self;
    [self.navigationController pushViewController:detailViewController animated:YES];
  }

- (void)updateEvaluateView:(NSNotification *)notification {
    [GeneralUtil showProgress];
    NSMutableArray *evaluateMeArray = (NSMutableArray *)(notification.object);
    NSNumber *evaluateID = evaluateMeArray[0];
    NSNumber *evaluateValue = evaluateMeArray[1];
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
               // [self.noNetworkView setHidden:YES];
                if(detailViewController) {
                    detailViewController.dicEvalData[@"electCnt"]  = evaluateTotalValue;
                    detailViewController.dicEvalData[@"isElectedByMe"] = evaluateValue;
                }
//                NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[aryEvalData objectAtIndex:evaluateIndex];
//                [aryEvalData replaceObjectAtIndex:evaluateIndex withObject:evaluateDic];
//                [self.estimationTable reloadData];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
               // [self.noNetworkView setHidden:NO];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
}


-(IBAction)onBack:(id)sender
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - HotEstimationListDelegate Function
- (void)changeHotEstimationListData:(NSMutableDictionary *)hotEstimationDic {
    if(aryEvalData && aryEvalData.count > 0) {
        NSInteger objIndex = [aryEvalData indexOfObject:hotEstimationDic];
        [aryEvalData replaceObjectAtIndex:objIndex withObject:hotEstimationDic];
    }
    [self.estimationTable reloadData];
}

@end
