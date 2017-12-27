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
#import "MyEvalTableViewCell.h"
#import "RealNameAuthenticationViewController.h"
#import "AAPullToRefresh.h"

@interface EvaluateViewController ()
{
    NSMutableArray *aryPersoanlData;
    NSMutableArray *aryEnterData;
    UINavigationController *choiceNavVC;
    UIViewController *choiceVC;
    NSString *choicePersonalKind;
    NSString *choiceEnterpriseKind;
    NSInteger nSelectedIndex;
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;
    BOOL isFontAdjusted;
}
@end

@implementation EvaluateViewController
@synthesize btnOffice, btnPersonal, imgOfficeLine, imgPersonalLine, searchBar;
@synthesize messageNumberLabel;
@synthesize bPersonal;
@synthesize tblView;
@synthesize homeChoiceBackgroundView, homeChoiceView, homeChoiceTransView, sortNameLabel, sortCheckImage;

- (void)viewDidLoad {
    [super viewDidLoad];
    isFontAdjusted = false;
    // Do any additional setup after loading the view from its nib.
    [self.tblView registerNib:[UINib nibWithNibName:@"EvalPersonalTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvalPersonalCellIdentifier"];
    [self.tblView registerNib:[UINib nibWithNibName:@"MyEvalTableViewCell" bundle:nil] forCellReuseIdentifier:@"MyEvalIdentifier"];

    nSelectedIndex = 0;
    [self hideSortView];
    [self updateSortTable];
    
    self.viewBlank.hidden = YES;
    self.viewNoNetwork.hidden = YES;
    
    [CommonData sharedInstance].choiceEvaluateIds = @"";

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideEvalChoiceView:) name:SET_EVALCHOICE_VIEW_NOTIFICATION object:nil];
//    [self onClickPersonalButton:btnPersonal];
    [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    imgPersonalLine.hidden = NO;
    imgOfficeLine.hidden = YES;
    bPersonal = YES;
    

    choicePersonalKind = @"";
    choiceEnterpriseKind = @"";
    
    tblView.dataSource = self;
    tblView.delegate = self;
    
    
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
    
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
    @try {
        for (id object in [[[searchBar subviews] firstObject] subviews])
        {
            if (object && [object isKindOfClass:[UITextField class]])
            {
                UITextField *textFieldObject = (UITextField *)object;
                //textFieldObject.returnKeyType = UIReturnKeySearch;
                textFieldObject.enablesReturnKeyAutomatically = NO;
                break;
            }
        }
    }
    @catch (NSException *exception) {
        NSLog(@"Error while customizing UISearchBar");
    }
    @finally {
        
    }

    if(self.estimationType == em_Estimation)
    {
        
    }else if(self.estimationType == em_MyEstimation)
    {
        self.lblTitle.text = @"我的评价";
    }else if(self.estimationType == em_EstimationToMe)
    {
        self.lblTitle.text = @"对我的评价";
    }
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.tblView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.tblView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;

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
    //[searchBar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
//    [searchBar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    //[[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    
    [[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"transparent.png"]];
    
    for(UIView* i in [searchBar subviews])
    {
        if([i isKindOfClass:[UITextField class]])
        {
            [((UITextField*)i) setBorderStyle:UITextBorderStyleNone];
        }
        for(UIView* j in [i subviews])
        {
            if([j isKindOfClass:[UITextField class]])
            {
                [((UITextField*)j) setBorderStyle:UITextBorderStyleNone];
                /*
                [((UITextField*)j) setAdjustsFontSizeToFitWidth:YES];
                if(!isFontAdjusted)
                {
                    [((UITextField*)j) setFont:[UIFont systemFontOfSize:[[((UITextField*)j) font] pointSize] - 1] ];
                    isFontAdjusted = true;
                }
                 */
            }
        }
    }
    
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;

    //int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    [self refreshTopItems];
    topRefreshView.showPullToRefresh = YES;
    bottomRefreshView.showPullToRefresh = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    topRefreshView.showPullToRefresh = NO;
    bottomRefreshView.showPullToRefresh = NO;
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(bPersonal)
    {
        [aryPersoanlData removeAllObjects];
        [self getEvaluateDataFromServer:choicePersonalKind Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT Kind:@"1" Keyword:[CommonData sharedInstance].searchEvaluatePersonalText];
    }else
    {
        [aryEnterData removeAllObjects];
        [self getEvaluateDataFromServer:choiceEnterpriseKind Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT Kind:@"2" Keyword:[CommonData sharedInstance].searchEvaluateEnterpriseText];
    }
}

- (void)refreshBottomItems {
    if(bPersonal)
    {
        refreshStartIndex = aryPersoanlData.count;
        [self getEvaluateDataFromServer:choicePersonalKind Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT Kind:@"1" Keyword:[CommonData sharedInstance].searchEvaluatePersonalText];
    }else
    {
        refreshStartIndex = aryEnterData.count;
        [self getEvaluateDataFromServer:choiceEnterpriseKind Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT Kind:@"2" Keyword:[CommonData sharedInstance].searchEvaluateEnterpriseText];
    }
}

- (IBAction)onClickChoiceButton:(id)sender {
    if(self.estimationType == em_Estimation)
        [self showHomeChoiceView:nil];
    else
    {
        if(self.sortView.hidden == YES)
        {
            [self showSortView];
        }else
            [self hideSortView];
    }
}
- (void)updateSortTable {
//    if(self.estimationType == em_Estimation)
//    {
//        [arySortedPersonalData removeAllObjects];
//        [arySortedPersonalData addObjectsFromArray:aryPersoanlData];
//        [arySortedEnterData removeAllObjects];
//        [arySortedEnterData addObjectsFromArray:aryEnterData];
//        [tblView reloadData];
//        return;
//    }
//    if(nSelectedIndex == 0)
//    {
//        [arySortedPersonalData removeAllObjects];
//        [arySortedPersonalData addObjectsFromArray:aryPersoanlData];
//        [arySortedEnterData removeAllObjects];
//        [arySortedEnterData addObjectsFromArray:aryEnterData];
//    }else if(nSelectedIndex == 1)
//    {
//        [arySortedPersonalData removeAllObjects];
//        [arySortedEnterData removeAllObjects];
//        for(NSDictionary* item in aryPersoanlData)
//        {
//            if([item[@"kind"] intValue] == 1)
//            {
//                [arySortedPersonalData addObject:item];
//            }
//        }
//        for(NSDictionary* item in aryEnterData)
//        {
//            if([item[@"kind"] intValue] == 1)
//            {
//                [arySortedEnterData addObject:item];
//            }
//            
//        }
//    }else if(nSelectedIndex == 2)
//    {
//        [arySortedPersonalData removeAllObjects];
//        [arySortedEnterData removeAllObjects];
//        for(NSDictionary* item in aryPersoanlData)
//        {
//            if([item[@"kind"] intValue] == 2)
//            {
//                [arySortedPersonalData addObject:item];
//            }
//        }
//        for(NSDictionary* item in aryEnterData)
//        {
//            if([item[@"kind"] intValue] == 2)
//            {
//                [arySortedEnterData addObject:item];
//            }
//            
//        }
//    }
//    [tblView reloadData];
    for(int i = 0; i < sortNameLabel.count; i++) {
        if(i == nSelectedIndex) {
            [[sortNameLabel objectAtIndex:i] setTextColor:[UIColor blackColor]];
            [[sortCheckImage objectAtIndex:i] setHidden:NO];
        }else{
            [[sortNameLabel objectAtIndex:i] setTextColor:RGB_COLOR(128, 128, 128)];
            [[sortCheckImage objectAtIndex:i] setHidden:YES];
        }
    }
}

-(void)showSortView
{
    self.sortView.hidden = NO;
    self.sortBackView.hidden = NO;
    self.sortEmptyView.hidden = NO;
}
-(void)hideSortView
{
    self.sortView.hidden = YES;
    self.sortBackView.hidden = YES;
    self.sortEmptyView.hidden = YES;
}
- (IBAction)onSortSelection:(id)sender
{
    UIButton *button = (UIButton *)sender;
    nSelectedIndex = button.tag;
    [self refreshTopItems];
    [self updateSortTable];
    [self hideSortView];
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
    if(self.estimationType == em_Estimation) {
        if(bPersonal) {
            choicePersonalKind = [CommonData sharedInstance].choiceEvaluateIds;
        }else{
            choiceEnterpriseKind = [CommonData sharedInstance].choiceEvaluateIds;
        }
    }
    homeChoiceTransView.hidden = YES;
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceBackgroundView.hidden = YES;
        [choiceNavVC.view removeFromSuperview];
    }];
    [self refreshTopItems];
}

- (void)showHomeChoiceView:(NSNotification *) notification {
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    HomeChoiceBusinessViewController *vc = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    vc.mChoice = CHOICE_EVAL;
    vc.isCancelButton = false;
    if(bPersonal) {
        [CommonData sharedInstance].choiceEvaluateIds = choicePersonalKind;
    }else{
        [CommonData sharedInstance].choiceEvaluateIds = choiceEnterpriseKind;
    }

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

- (void) getEvaluateDataFromServer:(NSString*)xyleixingIds Start:(NSInteger)start Length:(NSInteger)length Kind:(NSString *)kind Keyword:(NSString *)keyword{
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    if(self.estimationType == em_Estimation) {
        [dicParams setObject:@"getAccountListForEstimate" forKey:@"pAct"];
        if ( xyleixingIds != nil && xyleixingIds.length > 0 ) {
            [dicParams setObject:xyleixingIds forKey:@"xyleixingIds"];
        }
    }
    else if(self.estimationType == em_MyEstimation) {
        [dicParams setObject:@"getMyEstimateList" forKey:@"pAct"];
        [dicParams setObject:[NSString stringWithFormat:@"%d", nSelectedIndex] forKey:@"kind"];
    }
    else if(self.estimationType == em_EstimationToMe) {
        [dicParams setObject:@"getEstimateToMeList" forKey:@"pAct"];
        [dicParams setObject:[NSString stringWithFormat:@"%d", nSelectedIndex] forKey:@"kind"];
    }
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    if(kind != nil)
        [dicParams setObject:kind forKey:@"akind"];
    if (start != -1)
        [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)start] forKey:@"start"];
    if (length != -1)
        [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)length] forKey:@"length"];


    [dicParams setObject:keyword forKey:@"keyword"];

    [[WebAPI sharedInstance] sendPostRequest:dicParams[@"pAct"] Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                for (int i = 0; i < aryData.count; i++) {
                    NSMutableDictionary *dic = [[NSMutableDictionary alloc] initWithDictionary:aryData[i]];
                    if(self.estimationType == em_Estimation)
                    {
                        if( [dic[@"akind"] integerValue] == PERSONAL_KIND) // personal
                            [aryPersoanlData addObject:dic];
                        else if( [dic[@"akind"] integerValue] == ENTERPRISE_KIND) // Enterprise
                            [aryEnterData addObject:dic];
                    }else if(self.estimationType == em_MyEstimation)
                    {
                        if( [dic[@"targetAccountAkind"] integerValue] == PERSONAL_KIND) // personal
                            [aryPersoanlData addObject:dic];
                        else if( [dic[@"targetAccountAkind"] integerValue] == ENTERPRISE_KIND) // Enterprise
                            [aryEnterData addObject:dic];
                    }else if(self.estimationType == em_EstimationToMe)
                    {
                        if( [dic[@"ownerAkind"] integerValue] == PERSONAL_KIND) // personal
                            [aryPersoanlData addObject:dic];
                        else if( [dic[@"ownerAkind"] integerValue] == ENTERPRISE_KIND) // Enterprise
                            [aryEnterData addObject:dic];
                    }
                }
                [tblView reloadData];
                if((bPersonal && aryPersoanlData.count == 0) || (!bPersonal && aryEnterData.count == 0))
                {
                    self.viewBlank.hidden = NO;
                }else
                {
                    self.viewBlank.hidden = YES;
                }
               
            }
            else
            {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
                tblView.hidden = YES;
                self.viewBlank.hidden = YES;
                self.viewNoNetwork.hidden = NO;
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
    imgPersonalLine.hidden = NO;
    imgOfficeLine.hidden = YES;
    
    bPersonal = YES;
    
    if(aryPersoanlData) {
        if(aryPersoanlData.count > 0) {
            [tblView reloadData];
        }else{
            [self refreshTopItems];
        }
    }
    if((bPersonal && aryPersoanlData.count == 0) || (!bPersonal && aryEnterData.count == 0))
    {
        self.viewBlank.hidden = NO;
    }else
    {
        self.viewBlank.hidden = YES;
    }
}

- (IBAction)onClickOfficeButton:(UIButton *)sender {
    
    [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    imgOfficeLine.hidden = NO;
    imgPersonalLine.hidden = YES;
    
    bPersonal = NO;
    if(aryEnterData) {
        if(aryEnterData.count > 0) {
            [tblView reloadData];
        }else{
            [self refreshTopItems];
        }
    }
    if((bPersonal && aryPersoanlData.count == 0) || (!bPersonal && aryEnterData.count == 0))
    {
        self.viewBlank.hidden = NO;
    }else
    {
        self.viewBlank.hidden = YES;
    }
}

- (IBAction)onClickWriteEvaluation:(id)sender {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
    {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
        return;
    }
    WriteEvalViewController *detailViewController = [[WriteEvalViewController alloc] initWithNibName:@"WriteEvalViewController" bundle:nil];
    detailViewController.isEvaluate = YES;
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
//        RealNameAuthenticationViewController* vc = [[RealNameAuthenticationViewController alloc] initWithNibName:@"RealNameAuthenticationViewController" bundle:nil];
//        [self.navigationController pushViewController:vc animated:YES];
    }
}
- (IBAction)showNotificationView:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onHideChoiceView:(UITapGestureRecognizer *)recognizer {
    [[NSNotificationCenter defaultCenter] postNotificationName:SET_EVALCHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onEmpty:(id)sender
{
    [self hideSortView];
}
#pragma UITableViewDelegate & UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (bPersonal)
        return aryPersoanlData.count;
    
    return aryEnterData.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if(self.estimationType == em_Estimation)
    {
        return 128.0f;
    }else if(self.estimationType == em_MyEstimation)
    {
        return 220;
    }else if(self.estimationType == em_EstimationToMe)
    {
        return 220;
    }
    return 128.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if(self.estimationType == em_Estimation)
    {
        static NSString *simpleTableIdentifier = @"EvalPersonalCellIdentifier";
        EvalPersonalTableViewCell *cell = (EvalPersonalTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        if(bPersonal) {
            if(aryPersoanlData.count <= indexPath.row)
                return cell;
        }else{
            if(aryEnterData.count <= indexPath.row)
                return cell;
        }

        NSMutableDictionary *dic = nil;
        if (bPersonal) {
            dic = aryPersoanlData[indexPath.row];
            [cell.userTypeLabel setText:@"个人"];
        }
        else
        {
            dic = aryEnterData[indexPath.row];
            [cell.userTypeLabel setText:@"企业"];
        }
        
        NSInteger aKind;
        aKind = [dic[@"akind"] integerValue];
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"logo"] ];
        
        if (aKind == PERSONAL_KIND) {
            cell.lblTitle.text = dic[@"realname"];
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image_person.png"]];
        }else {
            cell.lblTitle.text = dic[@"enterName"];
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image_enter.png"]];
        }
        if([cell.lblTitle.text isEqualToString:@""])
            cell.lblTitle.text = dic[@"mobile"];
        
        [cell.lblTitle sizeToFit];
        if([dic[@"xyName"] length] == 0) {
            cell.xyNameButton.hidden = YES;
        }else{
            cell.xyNameButton.hidden = NO;
            [cell.xyNameButton setTitle:dic[@"xyName"] forState:UIControlStateNormal];
            dispatch_async(dispatch_get_main_queue(), ^{
                CGSize stringSize = [cell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
                int bw = stringSize.width + 12;
                if(cell.lblTitle.frame.size.width > (tableView.frame.size.width - 12 - bw - cell.lblTitle.frame.origin.x)) {
                    [cell.lblTitle setFrame:CGRectMake(cell.lblTitle.frame.origin.x, cell.lblTitle.frame.origin.y, tableView.frame.size.width - 12 - bw - cell.lblTitle.frame.origin.x, cell.lblTitle.frame.size.height)];
                }
                CGRect nameLabelFrame = cell.lblTitle.frame;
                [cell.xyNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y + 2, bw, 16)];
            });
        }
        
        cell.lblMobile.text = dic[@"code"];
        
        if([dic[@"credit"] integerValue] == 0) {
            cell.lblRatio.text = @"暂无";
        }else{
            cell.lblRatio.text = [NSString stringWithFormat:@"%ld%%", (long)[dic[@"credit"] integerValue]];
        }
        cell.lblEval.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"feedbackCnt"] integerValue]];
        cell.lblFrontEval.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"positiveFeedbackCnt"] integerValue]];
        cell.lblBackEval.text = [NSString stringWithFormat:@"%ld", (long)[dic[@"negativeFeedbackCnt"] integerValue]];
        
        return cell;
    } else if(self.estimationType == em_MyEstimation)
    {
        static NSString *simpleTableIdentifier = @"MyEvalIdentifier";
        MyEvalTableViewCell *cell = (MyEvalTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        if(bPersonal) {
            if(aryPersoanlData.count <= indexPath.row)
                return cell;
        }else{
            if(aryEnterData.count <= indexPath.row)
                return cell;
        }
        NSMutableDictionary *dic = nil;
        if (bPersonal) {
            dic = aryPersoanlData[indexPath.row];
            [cell.imgType setImage:[UIImage imageNamed:@"personal"]];
        }
        else
        {
            dic = aryEnterData[indexPath.row];
            [cell.imgType setImage:[UIImage imageNamed:@"office"]];
        }
        
        NSInteger aKind = [dic[@"targetAccountAkind"] integerValue];
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"targetAccountLogo"]];
        
        if (aKind == PERSONAL_KIND) {
            if(dic[@"targetAccountRealname"] != nil)
                cell.lblName.text = dic[@"targetAccountRealname"];
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image_person.png"]];
        } else {
            cell.lblName.text = dic[@"targetAccountEnterName"];
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image_enter.png"]];
        }
        if([cell.lblName.text isEqualToString:@""])
            cell.lblName.text = dic[@"targetAccountMobile"];
        
        cell.lblEstimationType.text = dic[@"kindName"];
        cell.lblContent.text = [NSString stringWithFormat:@"评价内容: %@", dic[@"content"]];
        cell.lblWriteTime = dic[@"writeTimeString"];
        cell.lblElectCnt.text = [NSString stringWithFormat:@"点赞:%d", [dic[@"targetAccountElectCnt"] intValue]];
        cell.lblEstimationCnt.text = [NSString stringWithFormat:@"评价:%d", [dic[@"targetAccountFeedbackCnt"] intValue]];
        
        NSArray* aryPath = dic[@"imgPaths"];
        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 120, 0, 113, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 120 - 10, 80)];
        
        return cell;
    }else if(self.estimationType == em_EstimationToMe)
    {
        static NSString *simpleTableIdentifier = @"MyEvalIdentifier";
        MyEvalTableViewCell *cell = (MyEvalTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        if(bPersonal) {
            if(aryPersoanlData.count <= indexPath.row)
                return cell;
        }else{
            if(aryEnterData.count <= indexPath.row)
                return cell;
        }
        NSMutableDictionary *dic = nil;
        if (bPersonal) {
            dic = aryPersoanlData[indexPath.row];
            [cell.imgType setImage:[UIImage imageNamed:@"personal"]];
        }
        else
        {
            dic = aryEnterData[indexPath.row];
            [cell.imgType setImage:[UIImage imageNamed:@"office"]];
        }
        
        NSInteger aKind = [dic[@"ownerAkind"] integerValue];
        NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"ownerLogo"]];
        
        if (aKind == PERSONAL_KIND) {
            cell.lblName.text = dic[@"ownerRealname"];
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image_person.png"]];
        }else {
            cell.lblName.text = dic[@"ownerEnterName"];
            [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image_enter.png"]];
        }
        if([cell.lblName.text isEqualToString:@""])
            cell.lblName.text = dic[@"ownerMobile"];
        
        cell.lblEstimationType.text = dic[@"kindName"];
        cell.lblContent.text = [NSString stringWithFormat:@"评价内容: %@", dic[@"content"]];
        cell.lblWriteTime = dic[@"writeTimeString"];
        cell.lblElectCnt.text = [NSString stringWithFormat:@"点赞:%d", [dic[@"ownerElectCnt"] intValue]];
        cell.lblEstimationCnt.text = [NSString stringWithFormat:@"评价:%d", [dic[@"ownerFeedbackCnt"] intValue]];
        
        NSArray* aryPath = dic[@"imgPaths"];
        for (int i = 0; i < aryPath.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 120, 0, 113, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 120 - 10, 80)];
        
        return cell;
    }
    

    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if(self.estimationType != em_MyEstimation)
    {
        NSDictionary *dic = bPersonal ? aryPersoanlData[indexPath.row] : aryEnterData[indexPath.row];
        
        int nTestStatus = [dic[@"testStatus"] intValue];
        if(nTestStatus != 2) {
            [appDelegate.window makeToast:@"未认证的熟人／企业"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
            return;
        }
    }
    
    
    HomeFamiliarDetailViewController *detailViewController = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
    NSString *idKeyName = @"";
    if(self.estimationType == em_Estimation) {
        idKeyName = @"id";
    }else if(self.estimationType == em_MyEstimation){
        idKeyName = @"accountId";
    }else if(self.estimationType == em_EstimationToMe)
    {
        idKeyName = @"owner";
    }
    if (bPersonal) {
        NSDictionary *dic = aryPersoanlData[indexPath.row];
        [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%ld", (long)[dic[idKeyName] integerValue]];
    }
    else
    {
        NSDictionary *dic = aryEnterData[indexPath.row];
        [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%ld", (long)[dic[idKeyName] integerValue]];
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

- (IBAction)onShowSearchAction:(id)sender {
    NSNumber *boolPersonal = [NSNumber numberWithBool:bPersonal];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HISTORYSEARCH_VIEW_NOTIFICATION object:boolPersonal];
}

- (IBAction)handleTapChoiceView:(UITapGestureRecognizer *)recognizer {
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
}
@end
