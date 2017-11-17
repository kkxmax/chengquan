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

@interface HotDetailViewController ()
{
    NSMutableArray *aryEvalData;
    NSMutableArray *aryOfficeData;
    NSMutableArray *evaluateHeightArray;

}
@end

@implementation HotDetailViewController
@synthesize lblTitle, lblContent, lblRead, lblDate, contentHeight;
@synthesize scrollInfoView, tblEvalView, tblOfficeView, tblHeight, scrollPicView, pageControl;
@synthesize btnPersonal, btnOffice, personalSeparator, officeSeparator;
@synthesize hotData;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    //lblContent = [[UILabel alloc] initWithFrame:CGRectMake(12, 20, SCREEN_WIDTH - 24, 500)];
    //lblContent.numberOfLines = 300;
    
    lblTitle.text = hotData.strTitle;
    lblContent.text = hotData.strContent;
    [lblContent sizeToFit];
    lblRead.text = [NSString stringWithFormat:@"%ld", (long)hotData.mVisitCnt];
    lblDate.text = hotData.strWriteTimeString;
    
    int width = SCREEN_WIDTH - 22;
    NSMutableArray *aryPath = hotData.aryImgPath;
    for (int i = 0; i < aryPath.count; i++) {
        UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * width, 0, width, 160)];
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
        [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
        [scrollPicView addSubview:imgView];
    }
    
    [scrollPicView setContentSize:CGSizeMake(width * aryPath.count, 160)];
    pageControl.numberOfPages = aryPath.count;
    
    contentHeight = lblContent.frame.size.height + 40;
    
    tblHeight = scrollInfoView.frame.size.height;
    
    tblEvalView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, tblHeight)];
    tblEvalView.dataSource = self;
    tblEvalView.delegate = self;
    tblEvalView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [tblEvalView registerNib:[UINib nibWithNibName:@"HotDetailPersonalCell" bundle:nil] forCellReuseIdentifier:@"HotDetailPersonalCellIdentifier"];

    [scrollInfoView addSubview:tblEvalView];
    
    tblOfficeView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, tblHeight)];
    tblOfficeView.dataSource = self;
    tblOfficeView.delegate = self;
    tblOfficeView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [tblOfficeView registerNib:[UINib nibWithNibName:@"HotDetailOfficeCell" bundle:nil] forCellReuseIdentifier:@"HotDetailOfficeCellIdentifier"];

    [scrollInfoView addSubview:tblOfficeView];
    
    [scrollInfoView setContentSize:CGSizeMake(SCREEN_WIDTH * 2, tblHeight)];
    scrollInfoView.delegate = self;
    
    aryEvalData = [[NSMutableArray alloc] init];
    aryOfficeData = [[NSMutableArray alloc] init];
    
    [self getHotEvaluateDataFromServer];
    
    [self onClickPersonalButton:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showMoreReplyView:) name:SHOW_MORE_REPLY_VIEW_NOTIFICATION object:nil];
}

#pragma -mark NSNotification
- (void)showMoreReplyView:(NSNotification *) notification {
    if(self.tblEvalView) {
        [self.tblEvalView reloadData];
    }
}

- (void)getHotEvaluateDataFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getEstimateListForHot" forKey:@"pAct"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)hotData.mId] forKey:@"hotId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETESTIMATELISTFORHOT Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryEvalData removeAllObjects];
                for (int i = 0; i < aryData.count; i++) {
                    [aryEvalData addObject:aryData[i]];
                }
                
                if ( aryEvalData.count == 0 )
                {
                }
                else
                {
                    evaluateHeightArray = [NSMutableArray arrayWithCapacity:aryEvalData.count];
                    [self.tblEvalView reloadData];
                }
                
                [self getHotOfficeDataFromServer];
                
            }
        }
    }];

}

- (void)getHotOfficeDataFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getPartnerList" forKey:@"pAct"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)hotData.mId] forKey:@"hotId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPARTNERLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryOfficeData removeAllObjects];
                for (int i = 0; i < aryData.count; i++) {
                    [aryOfficeData addObject:aryData[i]];
                }
                
                if (aryOfficeData.count == 0)
                {
                }
                else
                {
                    [self.tblOfficeView reloadData];
                }
            }
        }
    }];
    
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == scrollPicView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        [pageControl setCurrentPage:page];
    }
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

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if ( tableView == tblEvalView)
        return aryEvalData.count;
    else if (tableView == tblOfficeView)
        return aryOfficeData.count;
    
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {

    if ( tableView == tblEvalView) {
        NSDictionary *evaluateDic = (NSDictionary *)[aryEvalData objectAtIndex:indexPath.row];
        NSMutableArray *replyArray = evaluateDic[@"replys"];
        if(replyArray.count == 0) {
            return 247.f;
        }else{
            CGFloat replyHeight = 0.f;
            if(evaluateHeightArray.count > indexPath.row) {
                NSString *strReplyHeight = (NSString *) [evaluateHeightArray objectAtIndex:indexPath.row];
                replyHeight = [strReplyHeight floatValue];
            }
            if(replyArray.count > 1) {
                replyHeight += 20;
            }
            return 247.f + replyHeight;
        }
    }
    else if (tableView == tblOfficeView)
        return 135.0f;
    
    return 0;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if ( tableView == tblEvalView)
    {
        NSString *simpleTableIdentifier = @"HotDetailPersonalCellIdentifier";
        HotDetailPersonalCell *cell = (HotDetailPersonalCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];

        NSDictionary *dic = aryEvalData[indexPath.row];
        
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
        cell.lblTitle.text = dic[@"ownerRealname"];
        NSString *strDate = dic[@"writeTimeString"];
        cell.lblDate.text = [strDate substringToIndex:[strDate rangeOfString:@" "].location];
        NSArray *aryPath = dic[@"imgPaths"];

        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 90 - 10, 80)];
        
        NSArray *aryReply = dic[@"replys"];
        cell.lblElect.text = [NSString stringWithFormat:@"(%ld)", (long)aryReply.count];

        cell.lblReply.text = @"";
        
        if(aryReply.count == 0) {
            cell.moreReplyView.hidden = YES;
        }else {
            NSString *totalContent;
            for(int i = 0; i < aryReply.count; i++) {
                NSDictionary *replyDic = (NSDictionary *)[aryReply objectAtIndex:0];
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
                if(!cell.moreReplyButton.selected)
                    break;
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.tblEvalView beginUpdates];
                cell.lblReply.text = totalContent;
                [cell.lblReply sizeToFit];
                NSString *strReplyHeight = [NSString stringWithFormat:@"%f", cell.lblReply.frame.size.height];
                if(evaluateHeightArray.count > indexPath.row)
                    [evaluateHeightArray replaceObjectAtIndex:indexPath.row withObject:strReplyHeight];
                else
                    [evaluateHeightArray insertObject:strReplyHeight atIndex:indexPath.row];
                [self.tblEvalView endUpdates];
            });
            if(aryReply.count > 1) {
                cell.moreReplyView.hidden = NO;
            }else{
                cell.moreReplyView.hidden = YES;
            }
        }
        return cell;
    }
    else if ( tableView == tblOfficeView)
    {
        NSString *simpleTableIdentifier = @"HotDetailOfficeCellIdentifier";
        HotDetailOfficeCell *cell = (HotDetailOfficeCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
        
        NSDictionary *dic = aryOfficeData[indexPath.row];
        
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"logo"]];
        if (imgPath.length > 0) {
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
            cell.lblLogo.hidden = YES;
        }
        
        cell.lblTitle.text = dic[@"enterName"];
        [cell.lblTitle sizeToFit];
        
        [cell.btnXY setTitle:dic[@"xyName"] forState:UIControlStateNormal];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGRect nameLabelFrame = cell.lblTitle.frame;
            CGSize stringSize = [cell.btnXY.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
            [cell.btnXY setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y - 2, stringSize.width, 16)];
        });
        
        cell.lblRatio.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"credit"] integerValue]];
        
        cell.lblProduct.text = @"";
        NSArray *prodArray = (NSArray *)(dic[@"products"]);
        if(prodArray.count > 0 ) {
            NSString *prodNames = @"";
            for(int i = 0; i < prodArray.count; i++) {
                NSDictionary *prodDic = (NSDictionary *)(prodArray[i]);
                if(i == 0){
                    prodNames = prodDic[@"name"];
                }else{
                    prodNames = [NSString stringWithFormat:@"%@,%@", prodNames, prodDic[@"name"]];
                }
            }
            cell.lblProduct.text = prodNames;
        }
        
        cell.lblService.text = @"";
        NSArray *srvArray = (NSArray *)(dic[@"services"]);
        if(srvArray.count > 0 ) {
            NSString *srvNames = @"";
            for(int i = 0; i < srvArray.count; i++) {
                NSDictionary *srvDic = (NSDictionary *)(srvArray[i]);
                if(i == 0){
                    srvNames = srvDic[@"name"];
                }else{
                    srvNames = [NSString stringWithFormat:@"%@,%@", srvNames, srvDic[@"name"]];
                }
            }
            cell.lblService.text = srvNames;
        }
        
        cell.accountID = [NSString stringWithFormat:@"%d", (int)[dic[@"id"] intValue]];

        return cell;

    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (tableView == tblEvalView) {
        EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
        NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[aryEvalData objectAtIndex:indexPath.row];
        detailViewController.dicEvalData = evaluateDic;
        [self.navigationController pushViewController:detailViewController animated:YES];
    }
    else if ( tableView == tblOfficeView)
    {
        NSDictionary *friendDic = (NSDictionary *)[aryEvalData objectAtIndex:indexPath.row];
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

- (IBAction)onClickPersonalButton:(UIButton *)sender {
    [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    personalSeparator.hidden = NO;
    officeSeparator.hidden = YES;

    [scrollInfoView setContentOffset:CGPointMake(0, 0)];
   
}

- (IBAction)onClickOfficeButton:(UIButton *)sender {
    [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    officeSeparator.hidden = NO;
    personalSeparator.hidden = YES;
    
    [scrollInfoView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
}

- (IBAction)onClickNavBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
