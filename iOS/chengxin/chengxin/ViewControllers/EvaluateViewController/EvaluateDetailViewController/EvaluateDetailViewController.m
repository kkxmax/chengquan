//
//  EvaluateDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateDetailViewController.h"
#import "EvaluateDetailTableViewCell.h"
#import "EvaluateMoreTableViewCell.h"
#import "UIImageView+WebCache.h"
#import "Global.h"
#import "HomeFamiliarDetailViewController.h"
#import "ReformViewController.h"
#import "MSBrowseImageView.h"

@interface EvaluateDetailViewController ()
{
    NSMutableArray *aryReplyData;
    CGFloat basicCellHeight;
    NSMutableArray *evaluateHeightArray;
}
@property (nonatomic, retain) MSBrowseImageView             *browseImageView;
@end

@implementation EvaluateDetailViewController
@synthesize tblEvalView;
@synthesize dicEvalData;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    aryReplyData = [[NSMutableArray alloc] init];
    
    aryReplyData = dicEvalData[@"replys"];
    
    evaluateHeightArray = [NSMutableArray arrayWithCapacity:aryReplyData.count];

    basicCellHeight = 285.f;
    self.editBackgroundView.layer.cornerRadius = self.editBackgroundView.frame.size.height / 2;
    
    // NSNotification for Show Error
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showFixErrorView:) name:SHOW_EVALUATE_FIXED_ERROR_VIEW_NOTIFICATION object:nil];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onBackAction:(id)sender {
    if(self.hotEstimationListDelegate) {
        [self.hotEstimationListDelegate changeHotEstimationListData:self.dicEvalData];
    }
    [self.navigationController popViewControllerAnimated:YES];
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
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return aryReplyData.count + 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 0)
        return basicCellHeight;
    else
        if(evaluateHeightArray.count > (indexPath.row - 1)) {
            NSString *strReplyHeight = (NSString *) [evaluateHeightArray objectAtIndex:(indexPath.row - 1)];
            return [strReplyHeight floatValue];

        }else{
            return 105.f;
        }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if (indexPath.row == 0) {
        static NSString *simpleTableIdentifier = @"EvaluateDetailTableViewCell";
        EvaluateDetailTableViewCell *cell = (EvaluateDetailTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EvaluateDetailTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        
        
        NSInteger ownerAkind = [dicEvalData[@"ownerAkind"] integerValue];
        
        NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dicEvalData[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed:ownerAkind == 1 ? @"no_image_person.png" : @"no_image_enter.png"]];
        
        if(ownerAkind == PERSONAL_KIND) {
            cell.lblTitle.text = dicEvalData[@"ownerRealname"];
        }else{
            cell.lblTitle.text = dicEvalData[@"ownerEnterName"];
        }

        cell.lblEval.text = dicEvalData[@"kindName"];
        if([dicEvalData[@"content"] length] > 0) {
            cell.lblContent.text = [NSString stringWithFormat:@"%@", dicEvalData[@"content"]];
//            cell.lblContent.text = [NSString stringWithFormat:@"评价内容：%@", dicEvalData[@"content"]];
        }else{
            cell.lblContent.text = @"";
        }
        if([dicEvalData[@"reason"] length] > 0) {
            cell.lblMore.text = [NSString stringWithFormat:@"%@", dicEvalData[@"reason"]];
//            cell.lblMore.text = [NSString stringWithFormat:@"评价原因：%@", dicEvalData[@"reason"]];
        }else{
            cell.lblMore.text = @"";
        }
        cell.lblDate.text = [GeneralUtil getDateHourMinFrom:dicEvalData[@"writeTimeString"]];
        cell.electCountLabel.text = [NSString stringWithFormat:@"%ld", [dicEvalData[@"electCnt"] longValue]];
        cell.electCount = [dicEvalData[@"electCnt"] longValue];
        cell.evaluateID = [dicEvalData[@"id"] longValue];
        long isElectedByMe = [dicEvalData[@"isElectedByMe"] longValue];
        if(isElectedByMe == 1)
            cell.zanButton.selected = YES;
        else
            cell.zanButton.selected = NO;
        if (self.self.isHotEvaluator) {
            cell.errorButton.hidden = YES;
        } else {
            long owner = [dicEvalData[@"owner"] longValue];
            if(owner == [[CommonData sharedInstance].userInfo[@"id"] longValue])
                cell.errorButton.hidden = YES;
            else
                cell.errorButton.hidden = NO;
        }
        NSMutableArray *replyArray = dicEvalData[@"replys"];
        cell.replyContentLabel.text = [NSString stringWithFormat:@"%d", (int)(replyArray.count)];
        NSMutableArray *aryPath = dicEvalData[@"imgPaths"];
  
        for( UIView* subV in [cell.scrollThumb subviews])
        {
            [subV removeFromSuperview];
        }
        if ( aryPath == nil || aryPath.count == 0  ) {
            cell.scrollThumb.hidden = YES;
            cell.lblMoreTopSpace.constant = 10;
        }
        else
        {
            for (int i = 0; i < aryPath.count; i++)
            {
                UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
                NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
                [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
                [cell.scrollThumb addSubview:imgView];
                UIButton *actionBtn = [UIButton buttonWithType:UIButtonTypeCustom];
                [actionBtn setTitleColor:[UIColor clearColor] forState:UIControlStateNormal];
                [actionBtn setTitleColor:[UIColor clearColor] forState:UIControlStateHighlighted];
                NSString *index = [NSString stringWithFormat:@"%ld_%d",indexPath.row,i];
                [actionBtn setTitle:index forState:UIControlStateNormal];
                [actionBtn addTarget:self action:@selector(touchImage:) forControlEvents:UIControlEventTouchUpInside];
                [cell.scrollThumb addSubview:actionBtn];
                actionBtn.frame = imgView.frame;
            }
            [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 90 - 10, 80)];
        }
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.tblEvalView beginUpdates];
            [cell.lblContent sizeToFit];
            [cell.lblTitle sizeToFit];
            [cell.lblMore sizeToFit];
            basicCellHeight = cell.lblMore.frame.origin.y + cell.lblMore.frame.size.height + 53;
            [self.tblEvalView endUpdates];
        });

        return cell;
    }
    else
    {
        NSDictionary *dic = aryReplyData[indexPath.row - 1];
        static NSString *simpleTableIdentifier = @"EvaluateMoreTableViewCell";
        EvaluateMoreTableViewCell *cell = (EvaluateMoreTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EvaluateMoreTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        
        
        NSInteger ownerAkind = [dic[@"ownerAkind"] integerValue];
        
        NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed:ownerAkind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
        
        if(ownerAkind == PERSONAL_KIND) {
            cell.lblReply.text = [NSString stringWithFormat:@"%@: %@", dic[@"ownerRealname"], dic[@"content"]];
        }else{
            cell.lblReply.text = [NSString stringWithFormat:@"%@: %@", dic[@"ownerEnterName"], dic[@"content"]];
        }

        NSString *strDate = [GeneralUtil getDateHourMinFrom:dic[@"writeTimeString"]];
        cell.lblDate.text = [strDate substringWithRange:NSMakeRange(0, strDate.length-3)];
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.tblEvalView beginUpdates];
            [cell.lblReply sizeToFit];
            NSString *strReplyHeight = [NSString stringWithFormat:@"%f", cell.lblDate.frame.origin.y + cell.lblDate.frame.size.height + 11];
            if(evaluateHeightArray.count > (indexPath.row - 1))
                [evaluateHeightArray replaceObjectAtIndex:(indexPath.row - 1) withObject:strReplyHeight];
            else if(evaluateHeightArray.count == (indexPath.row - 1))
                [evaluateHeightArray insertObject:strReplyHeight atIndex:(indexPath.row - 1)];
            [self.tblEvalView endUpdates];
        });
        return cell;
    }
    
    return cell;

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
}

- (void)showFixErrorView:(NSNotification *)notification {
    ReformViewController *reformVC = [[ReformViewController alloc] initWithNibName:@"ReformViewController" bundle:nil];
    reformVC.reformAccountDic = self.dicEvalData;
    reformVC.isOwnerFlag = YES;
    [self.navigationController pushViewController:reformVC animated:YES];
}

- (IBAction)onSendFeedback:(id)sender {
    [self.view endEditing:YES];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"leaveReply" forKey:@"pAct"];
    [dicParams setObject:dicEvalData[@"id"] forKey:@"estimateId"];
    [dicParams setObject:self.feedbackTextField.text forKey:@"content"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_LEAVEREPLY Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSMutableDictionary *newReplyDic = [[NSMutableDictionary alloc] init];
                [newReplyDic setObject:[CommonData sharedInstance].userInfo[@"akind"] forKey:@"ownerAkind"];
                [newReplyDic setObject:[CommonData sharedInstance].userInfo[@"logo"] forKey:@"ownerLogo"];
                [newReplyDic setObject:[CommonData sharedInstance].userInfo[@"realname"] forKey:@"ownerRealname"];
                [newReplyDic setObject:[CommonData sharedInstance].userInfo[@"enterName"] forKey:@"ownerEnterName"];
                [newReplyDic setObject:self.feedbackTextField.text forKey:@"content"];
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                [formatter setTimeZone:[NSTimeZone systemTimeZone]];
                NSString *formatString = @"yyyy-MM-dd HH:mm:ss";
                [formatter setDateFormat:formatString];
                NSDate *currentDate = [NSDate date];
                NSString *dateString = [formatter stringFromDate:currentDate];
                [newReplyDic setObject:dateString forKey:@"writeTimeString"];
                [aryReplyData insertObject:newReplyDic atIndex:0];
                dicEvalData[@"replys"] = aryReplyData;
                self.feedbackTextField.text = @"";
                [self.tblEvalView reloadData];
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

#pragma mark - 查看大图
- (void)touchImage:(UIButton *)sender
{
    NSString *title = sender.currentTitle;
    NSArray *index = [title componentsSeparatedByString:@"_"];
    NSInteger inx = ((NSString *)index.lastObject).integerValue;
    NSDictionary *evaluateDic = dicEvalData;
    NSArray *evaluateImageArray = evaluateDic[@"imgPaths"];
    NSMutableArray *imageURL = [NSMutableArray arrayWithCapacity:evaluateImageArray.count];
    
    for (NSString *absoulteURL in evaluateImageArray) {
        NSString *url = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, absoulteURL];
        [imageURL addObject:url];
    }
    
    if (self.browseImageView) {
        [self.browseImageView setBigImageArray:imageURL withCurrentIndex:inx];
        return;
    }
    self.browseImageView = [[MSBrowseImageView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    UIWindow *keyWindow = [UIApplication sharedApplication].keyWindow;
    [keyWindow addSubview:self.browseImageView];
    __weak typeof(self) weakSelf = self;
    [self.browseImageView setRemoveView:^{
        [weakSelf.browseImageView removeFromSuperview];
        weakSelf.browseImageView = nil;
    }];
    [self.browseImageView setBigImageArray:imageURL withCurrentIndex:inx];
}

@end
