//
//  EvaluateViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateViewController.h"
#import "EvalPersonalTableViewCell.h"
#import "WriteEvalViewController.h"
#import "UIImageView+WebCache.h"
#import "EvaluateDetailViewController.h"
#import "HomeChoiceBusinessViewController.h"
#import "Global.h"
#import "HomeFamiliarDetailViewController.h"

@interface EvaluateViewController ()
{
    NSMutableArray *aryPersoanlData;
    NSMutableArray *aryEnterData;
    UINavigationController *choiceNavVC;
    UIViewController *choiceVC;
}
@end

@implementation EvaluateViewController
@synthesize btnOffice, btnPersonal, imgOfficeLine, imgPersonalLine, searchBar;
@synthesize messageNumberLabel;
@synthesize bPersonal;
@synthesize tblView;
@synthesize homeChoiceBackgroundView, homeChoiceView, homeChoiceTransView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [CommonData sharedInstance].choiceXyleixingIds = @"";

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideEvalChoiceView:) name:SET_EVALCHOICE_VIEW_NOTIFICATION object:nil];
    [self onClickPersonalButton:btnPersonal];
    
    tblView.dataSource = self;
    tblView.delegate = self;
    
    bPersonal = YES;
    
    if (self.bIsDetail) {
        self.viewDetailNavBar.hidden = NO;
        self.viewGeneralNavBar.hidden = YES;
    }
    else
    {
        self.viewDetailNavBar.hidden = YES;
        self.viewGeneralNavBar.hidden = NO;
    }
    
    aryPersoanlData = [[NSMutableArray alloc] init];
    aryEnterData = [[NSMutableArray alloc] init];
    
    [self getEvaluateDataFromServer:nil Start:-1 Length:-1];
    
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
}
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    // Customize Search Bar
    [searchBar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
    [searchBar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
}

- (IBAction)onClickChoiceButton:(id)sender {
    [self showHomeChoiceView:nil];
}

#pragma mark - Action
- (IBAction)handleSwipe:(UISwipeGestureRecognizer *)recognizer
{
    if(recognizer.direction == UISwipeGestureRecognizerDirectionRight && !homeChoiceView.isHidden)
    {
        [self hideEvalChoiceView:nil];
    }
}

- (void)hideEvalChoiceView:(NSNotification *) notification {
    homeChoiceTransView.hidden = YES;
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceBackgroundView.hidden = YES;
        [choiceNavVC.view removeFromSuperview];
    }];
    
    [self getEvaluateDataFromServer:[CommonData sharedInstance].choiceXyleixingIds Start:-1 Length:-1];

}

- (void)showHomeChoiceView:(NSNotification *) notification {
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    HomeChoiceBusinessViewController *vc = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    vc.mChoice = CHOICE_EVAL;
    vc.isSingleSelectionMode = NO;
    choiceVC = vc;

    [choiceNavVC popViewControllerAnimated:NO];
    [choiceNavVC pushViewController:choiceVC animated:NO];
    
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceTransView.hidden = NO;
    }];
}

- (void) getEvaluateDataFromServer:(NSString*)xyleixingIds Start:(NSInteger)start Length:(NSInteger)length {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getAccountListForEstimate" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    if (start != -1)
        [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)start] forKey:@"start"];
    if (length != -1)
        [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)length] forKey:@"length"];

    if ( xyleixingIds != nil && xyleixingIds.length > 0 )
        [dicParams setObject:xyleixingIds forKey:@"xyleixingIds"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETACCOUNTLISTFORESTIMATE Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryPersoanlData removeAllObjects];
                [aryEnterData removeAllObjects];
                
                for (int i = 0; i < aryData.count; i++) {
                    NSMutableDictionary *dic = [[NSMutableDictionary alloc] initWithDictionary:aryData[i]];
                    if( [dic[@"akind"] integerValue] == PERSONAL_KIND) // personal
                        [aryPersoanlData addObject:dic];
                    else if( [dic[@"akind"] integerValue] == ENTERPRISE_KIND) // Enterprise
                        [aryEnterData addObject:dic];
                }
                
                if (bPersonal)
                {
                    if (aryPersoanlData.count > 0)
                    {
                        [tblView reloadData];
                        tblView.hidden = NO;
                        self.viewBlank.hidden = YES;
                    }
                    else
                    {
                        tblView.hidden = YES;
                        self.viewBlank.hidden = NO;
                    }
                }
                else
                {
                    if (aryEnterData.count > 0) {
                        [tblView reloadData];
                        tblView.hidden = NO;
                        self.viewBlank.hidden = YES;
                        self.viewNoNetwork.hidden = YES;
                    }
                    else
                    {
                        tblView.hidden = YES;
                        self.viewNoNetwork.hidden = YES;
                        self.viewBlank.hidden = NO;
                    }
                }
            }
            else
            {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
                tblView.hidden = YES;
                self.viewBlank.hidden = YES;
                self.viewNoNetwork.hidden = NO;
            }
        }
    }];
}

- (IBAction)onClickPersonalButton:(UIButton *)sender {
    
    [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    imgPersonalLine.hidden = NO;
    imgOfficeLine.hidden = YES;
    
    bPersonal = YES;
    
    [tblView reloadData];
    
}

- (IBAction)onClickOfficeButton:(UIButton *)sender {
    
    [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    imgOfficeLine.hidden = NO;
    imgPersonalLine.hidden = YES;
    
    bPersonal = NO;
    
    [tblView reloadData];

}

- (IBAction)onClickWriteEvaluation:(id)sender {
    WriteEvalViewController *detailViewController = [[WriteEvalViewController alloc] initWithNibName:@"WriteEvalViewController" bundle:nil];
    detailViewController.isEvaluate = YES;
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
}

- (IBAction)showNotificationView:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
}

#pragma UITableViewDelegate & UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (bPersonal)
        return aryPersoanlData.count;
    
    return aryEnterData.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 128.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"EvalPersonalTableViewCell";
    EvalPersonalTableViewCell *cell = (EvalPersonalTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EvalPersonalTableViewCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
        cell.backgroundColor = [UIColor clearColor];
    }
    
    NSMutableDictionary *dic = nil;
    if (bPersonal) {
        dic = aryPersoanlData[indexPath.row];
        [cell.imgType setImage:[UIImage imageNamed:@"personal"]];
        cell.lblTitle.text = dic[@"realname"];
        
    }
    else
    {
        dic = aryEnterData[indexPath.row];
        [cell.imgType setImage:[UIImage imageNamed:@"office"]];
        cell.lblTitle.text = dic[@"enterName"];
    }
    
    NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"logo"]];
    [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
    [cell.lblTitle sizeToFit];
    cell.constrainLeftTitle.constant = cell.lblTitle.frame.origin.x + cell.lblTitle.frame.size.width + 10;
    cell.lblMobile.text = dic[@"mobile"];
    cell.lblEval.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"credit"] integerValue]];
    cell.lblRatio.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"feedbackCnt"] integerValue]];
    cell.lblFrontEval.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"positiveFeedbackCnt"] integerValue]];
    cell.lblBackEval.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"negativeFeedbackCnt"] integerValue]];

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    //EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
    HomeFamiliarDetailViewController *detailViewController = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
    
    if (bPersonal) {
        NSDictionary *dic = aryPersoanlData[indexPath.row];
        [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%ld", (long)[dic[@"id"] integerValue]];
    }
    else
    {
        NSDictionary *dic = aryEnterData[indexPath.row];
        [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%ld", (long)[dic[@"id"] integerValue]];
    }

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
