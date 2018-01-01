//
//  HomeFamiliarDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeFamiliarDetailViewController.h"
#import "EvalTopTableViewCell.h"
#import "EvaluateTableViewCell.h"
#import "HomeCommerceCollectionViewCell.h"
#import "HomeItemTableViewCell.h"
#import "HomeServiceTableViewCell.h"
#import "BlankTableViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "EvaluateDetailViewController.h"
#import "HomeCommerceDetailViewController.h"
#import "HomeItemDetailViewController.h"
#import "WriteEvalViewController.h"
#import "WebViewController.h"
#import "ReformViewController.h"
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import "MOBShareSDKHelper.h"

@interface HomeFamiliarDetailViewController ()
{
    int height;
    NSMutableArray *evaluateArray;
    NSMutableArray *productArray;
    NSMutableArray *itemArray;
    NSMutableArray *serviceArray;
    NSMutableArray *evaluateHeightArray;
    int nFrontEvaluateCount, nBackEvaluateCount;
    NSString *reqCodeSenderId;
    NSString *callString;
    NSString *currentAccountID;
    
    CGFloat fEvaluateTableViewHeight;
    CGFloat fProductTableViewHeight;
    CGFloat fItemTableViewHeight;
    CGFloat fServiceTableViewHeight;
    
    CGFloat fScrollViewContentHeight;
    BOOL hideCompaynInfoFlag;
}
@end

enum {
    SELECT_EVAL = 0,
    SELECT_PROD,
    SELECT_ITEM,
    SELECT_SERV
};

@implementation HomeFamiliarDetailViewController
@synthesize selectType, friendDictionary;
@synthesize consOfficeDetail, imgShowHide, lblShowHide;
@synthesize btnEval, btnItem, btnProduct, btnService, sepEval, sepItem, sepProduct, sepService, btnEvaluate;
@synthesize btnFavourite, lblAllEval, lblBackEval, lblFrontEval, viewWriteMessage;
@synthesize tblItemView, tblServiceView, tblEvaluateView, collectProductView;
@synthesize scrollContentView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    // Initialize Array
    evaluateArray = [NSMutableArray array];
    productArray = [NSMutableArray array];
    itemArray = [NSMutableArray array];
    serviceArray = [NSMutableArray array];
    evaluateHeightArray = [NSMutableArray array];
    nFrontEvaluateCount = 0;
    nBackEvaluateCount = 0;
    reqCodeSenderId = @"";
    
    btnEvaluate.layer.cornerRadius = btnEvaluate.frame.size.height / 2;
    btnFavourite.layer.cornerRadius = btnFavourite.frame.size.height / 2;

    lblAllEval.layer.cornerRadius = 12.0f;
    lblBackEval.layer.cornerRadius = 12.0f;
    lblFrontEval.layer.cornerRadius = 12.0f;
    viewWriteMessage.layer.cornerRadius = 17.0f;
    
    hideCompaynInfoFlag = NO;
    
    height = scrollContentView.frame.size.height;
    tblEvaluateView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, height)];
    [tblEvaluateView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    tblEvaluateView.dataSource = self;
    tblEvaluateView.delegate = self;
    tblEvaluateView.scrollEnabled = NO;
    [tblEvaluateView registerNib:[UINib nibWithNibName:@"EvaluateTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvaluateTableViewCellIdentifier"];
    [tblEvaluateView registerNib:[UINib nibWithNibName:@"EvalTopTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvalTopCellIdentifier"];
    [tblEvaluateView registerNib:[UINib nibWithNibName:@"BlankTableViewCell" bundle:nil] forCellReuseIdentifier:@"BlankCellIdentifier"];
    [scrollContentView addSubview:tblEvaluateView];
    
    [self createProductView];
    [self createItemView];
    [self createServiceView];
    [scrollContentView setContentSize:CGSizeMake(SCREEN_WIDTH * 4, height)];
    scrollContentView.delegate = self;
    
    selectType = SELECT_EVAL;
    [self onClickEvalButton:nil];
    
    // initialize notification
    
    // NSNotification for EvaluteDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showMoreReplyView:) name:SHOW_MORE_REPLY_VIEW_NOTIFICATION object:nil];
    // NSNotification for EvaluteTopTableViewCell
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showAllFrontBackView:) name:SHOW_ALLFRONTBACK_VIEW_NOTIFICATION object:nil];
    // NSNotification for Update EvaluateTableView
    
    // NSNotification for Show EvaluateDetailzView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showEvaluateDetailView:) name:UPDATE_EVALUATE_DETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for Show Error
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showFixErrorView:) name:SHOW_FIXED_ERROR_VIEW_NOTIFICATION object:nil];
    currentAccountID = [CommonData sharedInstance].selectedFriendAccountID;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateEvaluateView:) name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    
    [self incViewCount];
    [tblEvaluateView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
    [collectProductView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
    [tblItemView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
    [tblServiceView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(tblEvaluateView == object) {
        tblEvaluateView.frame = CGRectMake(tblEvaluateView.frame.origin.x, tblEvaluateView.frame.origin.y, tblEvaluateView.frame.size.width, tblEvaluateView.contentSize.height);
        fEvaluateTableViewHeight = tblEvaluateView.frame.size.height;
    }else if(collectProductView == object) {
        collectProductView.frame = CGRectMake(collectProductView.frame.origin.x, collectProductView.frame.origin.y, collectProductView.frame.size.width, collectProductView.contentSize.height);
        fProductTableViewHeight = collectProductView.frame.size.height;
    }else if(tblItemView == object) {
        tblItemView.frame = CGRectMake(tblItemView.frame.origin.x, tblItemView.frame.origin.y, tblItemView.frame.size.width, tblItemView.contentSize.height);
        fItemTableViewHeight = tblItemView.frame.size.height;
    }else if(tblServiceView == object) {
        tblServiceView.frame = CGRectMake(tblServiceView.frame.origin.x, tblServiceView.frame.origin.y, tblServiceView.frame.size.width, tblServiceView.contentSize.height);
        fServiceTableViewHeight = tblServiceView.frame.size.height;
    }
    [self setScrollContentSize];
}

- (void)dealloc
{
    [tblEvaluateView removeObserver:self forKeyPath:@"contentSize" context:NULL];
    [collectProductView removeObserver:self forKeyPath:@"contentSize" context:NULL];
    [tblItemView removeObserver:self forKeyPath:@"contentSize" context:NULL];
    [tblServiceView removeObserver:self forKeyPath:@"contentSize" context:NULL];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.noNetworkView setHidden:NO];
    [self getDataFromServer];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
}
-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma -mark NSNotification
- (void)showMoreReplyView:(NSNotification *) notification {
    if(self.tblEvaluateView) {
        [self.tblEvaluateView reloadData];
    }
}

- (void)showAllFrontBackView:(NSNotification *) notification {
    NSString *strFrontBackIndex = (NSString *)(notification.object);
    NSInteger nFrontBackIndex = [strFrontBackIndex integerValue];
    [evaluateArray removeAllObjects];
    switch (nFrontBackIndex) {
        case 0:
            evaluateArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)friendDictionary[@"estimates"]];
            break;
        case 1:
        {
            NSMutableArray *tmpEvaluateArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)friendDictionary[@"estimates"]];
            for(int i = 0; i < tmpEvaluateArray.count; i++) {
                NSDictionary *tmpDic = (NSDictionary *)(tmpEvaluateArray[i]);
                if([tmpDic[@"kind"] intValue] == 1) {
                    [evaluateArray addObject:tmpDic];
                }
            }
        }
            break;
        case 2:
        {
            NSMutableArray *tmpEvaluateArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)friendDictionary[@"estimates"]];
            for(int i = 0; i < tmpEvaluateArray.count; i++) {
                NSDictionary *tmpDic = (NSDictionary *)(tmpEvaluateArray[i]);
                if([tmpDic[@"kind"] intValue] == 2) {
                    [evaluateArray addObject:tmpDic];
                }
            }
        }
            break;
        default:
            break;
    }
    [tblEvaluateView reloadData];
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
                [self.noNetworkView setHidden:YES];
                
                NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[evaluateArray objectAtIndex:evaluateIndex];
                evaluateDic[@"electCnt"]  = evaluateTotalValue;
                evaluateDic[@"isElectedByMe"] = evaluateValue;
                [evaluateArray replaceObjectAtIndex:evaluateIndex withObject:evaluateDic];
//                [self.tblEvaluateView reloadData];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                                                 duration:3.0
                                                 position:CSToastPositionCenter
                                                    style:nil];
                [self.noNetworkView setHidden:NO];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                             duration:3.0
                                             position:CSToastPositionCenter
                                                style:nil];
        }
    }];
}

- (void)showEvaluateDetailView:(NSNotification *)notification {
    EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
    NSNumber *evaluteIndex = (NSNumber *)(notification.object);
    if(evaluateArray.count <= [evaluteIndex integerValue]) {
        return;
    }
    NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[evaluateArray objectAtIndex:[evaluteIndex integerValue]];
    detailViewController.dicEvalData = evaluateDic;
    detailViewController.isHotEvaluator = NO;
    [self.navigationController pushViewController:detailViewController animated:YES];
}

- (void)showFixErrorView:(NSNotification *)notification {
    NSNumber *evaluateIndex = (NSNumber *)(notification.object);
    if(evaluateArray.count <= [evaluateIndex integerValue]) {
        return;
    }
    ReformViewController *reformVC = [[ReformViewController alloc] initWithNibName:@"ReformViewController" bundle:nil];
    reformVC.reformAccountDic = (NSDictionary *)[evaluateArray objectAtIndex:[evaluateIndex integerValue]];
    reformVC.isOwnerFlag = YES;
    [self.navigationController pushViewController:reformVC animated:YES];
}

- (void)setData {
    NSString *logoImageName = friendDictionary[@"logo"];
    NSInteger aKind = [friendDictionary[@"akind"] integerValue];
    long accountID = [friendDictionary[@"id"] longValue];
    long mineID = [[CommonData sharedInstance].userInfo[@"id"] longValue];
    if(accountID == mineID) {
        self.favoriteEvaluateView.hidden = YES;
    }else{
        self.favoriteEvaluateView.hidden = NO;
    }
    [self.imgAvatar sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:aKind == 1 ? @"no_image_person.png" : @"no_image_enter.png"]];
    if (aKind == PERSONAL_KIND) {
        self.navTitleLabel.text = @"个人信息";
        self.officeMarkLabel.text = @"个人";
        self.lblName.text = friendDictionary[@"realname"];
        self.personalCompanyLabel.text = friendDictionary[@"enterName"];
        self.personalJobLabel.text = friendDictionary[@"job"];
        self.personalNetworkLabel.text = friendDictionary[@"weburl"];
        self.personalAddressLabel.text = [NSString stringWithFormat:@"%@ %@ %@", friendDictionary[@"provinceName"], friendDictionary[@"cityName"], friendDictionary[@"addr"]];
        self.personalWeixinLabel.text = friendDictionary[@"weixin"];
        self.personalView.hidden = NO;
        self.minimizeBarView.hidden = YES;
        self.minimizeBarViewHeight.constant = 0;
        self.viewOfficeDetail.hidden = YES;
        self.officeContentView.hidden = YES;
        fScrollViewContentHeight = 550;

    }else {
        self.navTitleLabel.text = @"企业信息";
        self.officeMarkLabel.text = @"企业";//friendDictionary[@"enterKindName"];
        self.lblName.text = friendDictionary[@"enterName"];
        self.lblRecommend.text = friendDictionary[@"recommend"];
        self.lblOfficeInfo.text = friendDictionary[@"comment"];
        self.lblMainJob.text = friendDictionary[@"mainJob"];
        self.lblWeiXin.text = friendDictionary[@"weixin"];
        self.lblBossName.text = friendDictionary[@"bossName"];
        self.lblBossJob.text = friendDictionary[@"bossJob"];
        self.lblBossMobile.text = friendDictionary[@"bossMobile"];
        self.lblBossWeiXin.text = friendDictionary[@"bossWeixin"];
        self.lblAddr.text = [NSString stringWithFormat:@"%@ %@ %@", friendDictionary[@"provinceName"], friendDictionary[@"cityName"], friendDictionary[@"addr"]];
        NSString *certImageName = friendDictionary[@"enterCertImage"];
        [self.imgBusinessCert sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, certImageName]] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        self.personalView.hidden = YES;
        self.minimizeBarView.hidden = NO;
        self.minimizeBarViewHeight.constant = 36;
        self.viewOfficeDetail.hidden = NO;
        self.officeContentView.hidden = NO;

        [self.lblRecommend sizeToFit];
        [self.lblOfficeInfo sizeToFit];
        self.officeContentViewHeight.constant = 114 + self.lblRecommend.frame.size.height + self.lblOfficeInfo.frame.size.height;
        fScrollViewContentHeight = 945 + self.lblRecommend.frame.size.height + self.lblOfficeInfo.frame.size.height;
    }
    if([self.lblName.text isEqualToString:@""])
        self.lblName.text = friendDictionary[@"mobile"];
    callString = friendDictionary[@"mobile"];
    reqCodeSenderId = [NSString stringWithFormat:@"%ld", [friendDictionary[@"reqCodeSenderId"] longValue]];
    int nReqCodeSenderAKind = [friendDictionary[@"reqCodeSenderAkind"] intValue];
    NSString *reqName = @"";
    if([friendDictionary[@"reqCodeSenderId"] longValue] > 0) {
        if(nReqCodeSenderAKind == PERSONAL_KIND) {
            reqName = friendDictionary[@"reqCodeSenderRealname"];
        }else{
            reqName = friendDictionary[@"reqCodeSenderEnterName"];
        }
        if([reqName isEqualToString:@""]) {
            reqName = friendDictionary[@"reqCodeSenderMobile"];
        }
    }
    if([friendDictionary[@"inviterFriendLevel"] isEqualToString:@""]) {
        self.lblReqCodeSender.text = reqName;
        self.personalReqsenderLabel.text = reqName;
    }else{
        self.lblReqCodeSender.text = [NSString stringWithFormat:@"%@-%@", friendDictionary[@"inviterFriendLevel"], reqName];
        self.personalReqsenderLabel.text = [NSString stringWithFormat:@"%@-%@", friendDictionary[@"inviterFriendLevel"], reqName];
    }
    [self.lblName sizeToFit];
    NSString *strXYName = friendDictionary[@"xyName"];
    if(strXYName.length == 0) {
        self.btnXYName.hidden = YES;
    }else{
        self.btnXYName.hidden = NO;
        [self.btnXYName setTitle:friendDictionary[@"xyName"] forState:UIControlStateNormal];
        CGSize stringSize = [self.btnXYName.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.0]}];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGRect nameLabelFrame = self.lblName.frame;
            CGSize titleSize = [self.lblName.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.0]}];
            int nNextLine = 0;
            while (titleSize.width >= self.lblName.frame.size.width) {
                titleSize.width -= self.lblName.frame.size.width;
                nNextLine ++;
            }
            [self.btnXYName setFrame:CGRectMake(nameLabelFrame.origin.x + titleSize.width + 3, nameLabelFrame.origin.y + 21 * nNextLine, stringSize.width, 16)];
        });
    }
    self.lblCode.text = friendDictionary[@"code"];
    if(self.lblCode.text.length == 0) {
        self.lblCode.text = @"请认证";
    }

    if([friendDictionary[@"credit"] longValue] == 0) {
        self.lblViewCount.text = @"暂无";
    }else{
        self.lblViewCount.text = [NSString stringWithFormat:@"%ld%%", (long)[friendDictionary[@"credit"] longValue]];
    }
    self.lblElectCount.text = [NSString stringWithFormat:@"%ld", (long)[friendDictionary[@"electCnt"] longValue]];
    self.lblFeedbackCount.text = [NSString stringWithFormat:@"%ld", (long)[friendDictionary[@"feedbackCnt"] longValue]];
    self.lblWebUrl.text = friendDictionary[@"weburl"];
    
    if([friendDictionary[@"interested"] integerValue] == 1) {
        self.btnFavourite.selected = NO;
    }else{
        self.btnFavourite.selected = YES;
    }

    [evaluateArray removeAllObjects];
    nFrontEvaluateCount = 0;
    nBackEvaluateCount = 0;
    evaluateArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)friendDictionary[@"estimates"]];
    NSMutableArray *tmpEvaluateArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)friendDictionary[@"estimates"]];
    for(int i = 0; i < tmpEvaluateArray.count; i++) {
        NSDictionary *tmpDic = (NSDictionary *)(tmpEvaluateArray[i]);
        if([tmpDic[@"kind"] intValue] == 1) {
            nFrontEvaluateCount ++;
        }else{
            nBackEvaluateCount ++;
        }
    }

    itemArray = (NSMutableArray *)friendDictionary[@"items"];
    productArray = (NSMutableArray *)friendDictionary[@"products"];
    serviceArray = (NSMutableArray *)friendDictionary[@"services"];
    [evaluateHeightArray removeAllObjects];
    for(int i = 0; i < evaluateArray.count; i++) {
        [evaluateHeightArray addObject:@"0"];
    }
    [tblEvaluateView reloadData];
    [collectProductView reloadData];
    [tblItemView reloadData];
    [tblServiceView reloadData];
}
- (IBAction)onBackAction:(id)sender {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_EVALUATE_VIEW_NOTIFICATION object:nil];
    if([CommonData sharedInstance].detailFamiliarEnterpriseIndex == SUB_HOME_PERSONAL)
        [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_FAMILIAR_DATA_NOTIFICATION object:nil];
    else
        [[NSNotificationCenter defaultCenter] postNotificationName:RELOAD_ENTERPRISE_DATA_NOTIFICATION object:nil];
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)incViewCount {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"incViewCount" forKey:@"pAct"];
    [dicParams setObject:currentAccountID forKey:@"accountId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:@"1" forKey:@"kind"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_INCVIEWCOUNT Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
            }
        }
    }];
}
- (void)getDataFromServer {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getAccountDetail" forKey:@"pAct"];
    [dicParams setObject:currentAccountID forKey:@"accountId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETACCOUNTDETAIL Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.noNetworkView setHidden:YES];
                friendDictionary = (NSDictionary *)(dicRes[@"account"]);
                [self setData];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                                                 duration:3.0
                                                 position:CSToastPositionCenter
                                                    style:nil];
                [self.noNetworkView setHidden:NO];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                             duration:3.0
                                             position:CSToastPositionCenter
                                                style:nil];
        }
    }];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark -  UITableViewDelegate & UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    if (tableView == tblEvaluateView) {
        return 2;
    }
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (tableView == tblEvaluateView) {
        if ( section == 0 )
            return 1;
        else {
            return evaluateArray.count;
        }
    } else if (tableView == tblItemView) {
        return itemArray.count;
    } else {
        return serviceArray.count;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (tableView == tblEvaluateView)
    {
        if ( indexPath.section == 0) {
                return 45.f;
        }
        else
        {
            if(evaluateArray.count  == 0) {
                return 390.f;
            }
            NSDictionary *evaluateDic = (NSDictionary *)[evaluateArray objectAtIndex:indexPath.row];
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
                    replyHeight += 40;
                }
                return 247.f + replyHeight;
            }
        }
    }
    else if (tableView == tblItemView)
        //return 155.f;
        return 155.f;
    else
        return 155.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if ( tableView == tblEvaluateView)
    {
        if (indexPath.section == 0) {
            static NSString *simpleTableIdentifier = @"EvalTopCellIdentifier";
            EvalTopTableViewCell *cell = (EvalTopTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            [cell.allSideButton setTitle:[NSString stringWithFormat:@"全部(%d)", (int)(nFrontEvaluateCount + nBackEvaluateCount) ] forState:UIControlStateNormal];
            [cell.allSideButton setTitle:[NSString stringWithFormat:@"全部(%d)", (int)(nFrontEvaluateCount + nBackEvaluateCount) ] forState:UIControlStateSelected];
            [cell.frontSideButton setTitle:[NSString stringWithFormat:@"正面评价(%d)", nFrontEvaluateCount ] forState:UIControlStateNormal];
            [cell.frontSideButton setTitle:[NSString stringWithFormat:@"正面评价(%d)", nFrontEvaluateCount ] forState:UIControlStateSelected];
            [cell.backSideButton setTitle:[NSString stringWithFormat:@"负面评价(%d)", nBackEvaluateCount] forState:UIControlStateNormal];
            [cell.backSideButton setTitle:[NSString stringWithFormat:@"负面评价(%d)", nBackEvaluateCount] forState:UIControlStateSelected];
            return cell;
        }
        else {
            static NSString *simpleTableIdentifier = @"EvaluateTableViewCellIdentifier";
            EvaluateTableViewCell *cell = (EvaluateTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
            cell.moreReplyView.hidden = YES;
            cell.replyContentLabel.hidden = YES;
            NSDictionary *evaluateDic = (NSDictionary *)[evaluateArray objectAtIndex:indexPath.row];
            NSInteger ownerAkind = [evaluateDic[@"ownerAkind"] integerValue];
            NSString *strUserName = @"";
            if(ownerAkind == PERSONAL_KIND) {
                strUserName = evaluateDic[@"ownerRealname"];
            }else{
                strUserName = evaluateDic[@"ownerEnterName"];
            }
            
            if(strUserName.length > EVALUATE_DETAIL_TITLE_MAX_LENGTH) {
                strUserName = [NSString stringWithFormat:@"%@…", [strUserName substringWithRange:NSMakeRange(0, EVALUATE_DETAIL_TITLE_MAX_LENGTH)]];
            }
            cell.ownerNameLabel.text = strUserName;
//            [cell.ownerNameLabel sizeToFit];
            cell.ownerContentTextView.text = evaluateDic[@"content"];
            cell.evaluateKindLabel.text = evaluateDic[@"kindName"];
            NSString *logoImageName = evaluateDic[@"ownerLogo"];
            if(logoImageName)
                [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:ownerAkind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
            
            if([evaluateDic[@"isFalse"] longValue] == 1) {
                cell.isFalseImageView.hidden = NO;
            }else{
                cell.isFalseImageView.hidden = YES;
            }
            for( UIView* subV in [cell.scrollThumb subviews])
            {
                [subV removeFromSuperview];
            }
            NSMutableArray *evaluateImageArray = evaluateDic[@"imgPaths"];
            for (int i = 0; i < evaluateImageArray.count; i++)
            {
                UIImageView *imgView = [[UIImageView alloc] init];
                [imgView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [evaluateImageArray objectAtIndex:i]]] placeholderImage:[UIImage imageNamed:@"no_image.png"]];
                [imgView setFrame:CGRectMake(i * 90, 0, 80, 80)];
                [cell.scrollThumb addSubview:imgView];
            }
            [cell.scrollThumb setContentSize:CGSizeMake(evaluateImageArray.count * 90 - 10, 80)];
            
            NSString *timeText = evaluateDic[@"writeTimeString"];
            cell.writeTimeLabel.text = [timeText substringWithRange:NSMakeRange(0, timeText.length - 3)];
            cell.electCountLabel.text = [NSString stringWithFormat:@"%ld", [evaluateDic[@"electCnt"] longValue]];
            cell.electCount = [evaluateDic[@"electCnt"] longValue];
            cell.evaluateID = [evaluateDic[@"id"] longValue];
            cell.cellIndex = indexPath.row;
            long isElectedByMe = [evaluateDic[@"isElectedByMe"] longValue];
            if(isElectedByMe == 1)
                cell.zanButton.selected = YES;
            else
                cell.zanButton.selected = NO;
            long owner = [evaluateDic[@"owner"] longValue];
            if(owner == [[CommonData sharedInstance].userInfo[@"id"] longValue])
                cell.errorCorrectButton.hidden = YES;
            else
                cell.errorCorrectButton.hidden = NO;
            
            cell.moreReplyView.hidden = YES;
            NSMutableArray *replyArray = evaluateDic[@"replys"];
            cell.replyLabel.text = [NSString stringWithFormat:@"%d", (int)(replyArray.count)];
            if(replyArray.count > 0) {
                cell.replyContentLabel.hidden = NO;
                if(replyArray.count > 1) {
                    cell.moreReplyView.hidden = NO;
                    cell.moreReplyLabel.text = [NSString stringWithFormat:@"展开全部回复（%d条）>", (int)(replyArray.count)];
                }else{
                    cell.moreReplyView.hidden = YES;
                }
                NSString *totalContent = @"";
                for(int i = 0; i < replyArray.count; i++) {
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
                    if(!cell.moreReplyButton.selected)
                        break;
                }
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self.tblEvaluateView beginUpdates];
                    cell.replyContentLabel.text = totalContent;
                    [cell.replyContentLabel sizeToFit];
                    NSString *strReplyHeight = [NSString stringWithFormat:@"%f", cell.replyContentLabel.frame.size.height];
                    [evaluateHeightArray replaceObjectAtIndex:indexPath.row withObject:strReplyHeight];
                    [self.tblEvaluateView endUpdates];
                });
            }
            return cell;
        }
    }
    else if ( tableView == tblItemView )
    {
        if(itemArray.count > 0) {
            static NSString *simpleTableIdentifier = @"HomeItemCellIdentifier";
            HomeItemTableViewCell *cell = (HomeItemTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            NSDictionary *itemDic = (NSDictionary *)[itemArray objectAtIndex:indexPath.row];
            cell.nameLabel.text = itemDic[@"name"];
            [cell.nameLabel sizeToFit];
            [cell.fenleiButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
            dispatch_async(dispatch_get_main_queue(), ^{
                CGSize stringSize = [cell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
                int bw = stringSize.width + 12;
                if(cell.nameLabel.frame.size.width > (tableView.frame.size.width - 12 - bw)) {
                    [cell.nameLabel setFrame:CGRectMake(cell.nameLabel.frame.origin.x, cell.nameLabel.frame.origin.y, tableView.frame.size.width - 12 - bw - cell.nameLabel.frame.origin.x, cell.nameLabel.frame.size.height)];
                }
                CGRect nameLabelFrame = cell.nameLabel.frame;
                [cell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y, bw, 16)];
            });
            NSString *logoImageName = itemDic[@"logo"];
            [cell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_item.png"]];
            
            cell.commentTextView.text = itemDic[@"comment"];
            return cell;
        }
    }
    else if ( tableView == tblServiceView)
    {
        if(serviceArray.count > 0) {
            static NSString *simpleTableIdentifier = @"HomeServiceCellIdentifier";
            HomeServiceTableViewCell *cell = (HomeServiceTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            NSDictionary *serviceDic = (NSDictionary *)[serviceArray objectAtIndex:indexPath.row];
            cell.nameLabel.text = serviceDic[@"name"];
            [cell.nameLabel sizeToFit];
            [cell.fenleiButton setTitle:serviceDic[@"fenleiName"] forState:UIControlStateNormal];
            dispatch_async(dispatch_get_main_queue(), ^{
                CGSize stringSize = [cell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
                int bw = stringSize.width + 12;
                if(cell.nameLabel.frame.size.width > (tableView.frame.size.width - 12 - bw)) {
                    [cell.nameLabel setFrame:CGRectMake(cell.nameLabel.frame.origin.x, cell.nameLabel.frame.origin.y, tableView.frame.size.width - 12 - bw - cell.nameLabel.frame.origin.x, cell.nameLabel.frame.size.height)];
                }
                CGRect nameLabelFrame = cell.nameLabel.frame;
                [cell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y, bw, 16)];
            });
            NSString *logoImageName = serviceDic[@"logo"];
            [cell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_item.png"]];
            

            
            cell.commentTextView.text = serviceDic[@"comment"];
            
            return cell;
        }
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (selectType == SELECT_EVAL) {
        if(evaluateArray.count == 0)
            return;
        EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
        NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[evaluateArray objectAtIndex:indexPath.row];
        detailViewController.dicEvalData = evaluateDic;
        detailViewController.isHotEvaluator = NO;
        [self.navigationController pushViewController:detailViewController animated:YES];
    }else if (selectType == SELECT_ITEM){
        [CommonData sharedInstance].selectedItemServiceDic = (NSDictionary *)(itemArray[indexPath.row]);
        HomeItemDetailViewController *detailVC = [[HomeItemDetailViewController alloc] initWithNibName:@"HomeItemDetailViewController" bundle:nil];
        [CommonData sharedInstance].detailItemServiceIndex = SUB_HOME_ITEM;
        [self.navigationController pushViewController:detailVC animated:YES];
    }else if(selectType == SELECT_SERV) {
        [CommonData sharedInstance].selectedItemServiceDic = (NSDictionary *)(serviceArray[indexPath.row]);
        [CommonData sharedInstance].detailItemServiceIndex = SUB_HOME_SERVICE;
        HomeItemDetailViewController *detailVC = [[HomeItemDetailViewController alloc] initWithNibName:@"HomeItemDetailViewController" bundle:nil];
        
        [self.navigationController pushViewController:detailVC animated:YES];
    }
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == scrollContentView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        if (page == SELECT_EVAL) {
            selectType = SELECT_EVAL;
        }
        else if (page == SELECT_PROD)
        {
            selectType = SELECT_PROD;
        }
        else if (page == SELECT_ITEM)
        {
            selectType = SELECT_ITEM;
        }
        else if (page == SELECT_SERV)
        {
            selectType = SELECT_SERV;
        }
        
        [self selectItem:selectType];
    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if(scrollView == self.bgScrollView)
        self.bgScrollView.bounces = (self.bgScrollView.contentOffset.y > 100);
}

#pragma mark - UICollectionView Delegate, DataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HomeCommerceCellIdentifier" forIndexPath:indexPath];
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    NSString *productImageName = productDic[@"imgPath1"];
    [homeCommerceCollectionCell.produceImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, productImageName]] placeholderImage:[UIImage imageNamed:@"bg_pic.png"]];
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
        if(IS_IPHONE_5)
        {
            return CGSizeMake(156.f, 200);
        }
        else if(IS_IPHONE_6)
        {
            return CGSizeMake(184.f, 244);
        }
        else if(IS_IPHONE_6P)
        {
            return CGSizeMake(202.f, 244);
        }
    }
    return CGSizeMake(184.f, 244);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    [CommonData sharedInstance].selectedProductID = [NSString stringWithFormat:@"%ld", [productDic[@"id"] longValue]];
    HomeCommerceDetailViewController *productDetailVC = [[HomeCommerceDetailViewController alloc] initWithNibName:@"HomeCommerceDetailViewController" bundle:nil];
    [self.navigationController pushViewController:productDetailVC animated:YES];
}

- (IBAction)onClickShowHideButton:(id)sender {
    
    if (!hideCompaynInfoFlag) {
        hideCompaynInfoFlag = YES;
        [UIView animateWithDuration:3.0f animations:^{
            fScrollViewContentHeight -= 478.f;
            [imgShowHide setImage:[UIImage imageNamed:@"arrow_down"]];
            [lblShowHide setText:@"展开"];
            self.viewOfficeDetail.hidden = YES;
            [self setScrollContentSize];
            
        }];
    } else
    {
        hideCompaynInfoFlag = NO;
        self.viewOfficeDetail.hidden = NO;
        [UIView animateWithDuration:3.0f animations:^{
            fScrollViewContentHeight += 478.f;
            [imgShowHide setImage:[UIImage imageNamed:@"arrow_up"]];
            [lblShowHide setText:@"收起"];
            [self setScrollContentSize];
        }];
    }
}

- (void) selectItem:(NSInteger) type {
    [btnEval.titleLabel setTextColor:BLACK_COLOR_153];
    [btnProduct.titleLabel setTextColor:BLACK_COLOR_153];
    [btnItem.titleLabel setTextColor:BLACK_COLOR_153];
    [btnService.titleLabel setTextColor:BLACK_COLOR_153];
    
    sepEval.hidden = YES;
    sepProduct.hidden = YES;
    sepItem.hidden = YES;
    sepService.hidden = YES;
    
    if (type == SELECT_EVAL) {
        [btnEval.titleLabel setTextColor:BLACK_COLOR_51];
        sepEval.hidden = NO;
    }
    else if (type == SELECT_PROD) {
        [btnProduct.titleLabel setTextColor:BLACK_COLOR_51];
        sepProduct.hidden = NO;
    }
    else if (type == SELECT_ITEM) {
        [btnItem.titleLabel setTextColor:BLACK_COLOR_51];
        sepItem.hidden = NO;
    }
    else if (type == SELECT_SERV) {
        [btnService.titleLabel setTextColor:BLACK_COLOR_51];
        sepService.hidden = NO;
    }

}

- (IBAction)onClickEvalButton:(id)sender {
    
    selectType = SELECT_EVAL;
    
    [self selectItem:selectType];
    
    [tblEvaluateView reloadData];
    [scrollContentView setContentOffset:CGPointMake(0, 0)];
//    [self setScrollContentSize];

}

- (void) createProductView {
    if ( collectProductView == nil ) {
        
        UICollectionViewFlowLayout* flowLayout = [[UICollectionViewFlowLayout alloc]init];
        flowLayout.itemSize = CGSizeMake(100, 100);
        flowLayout.minimumInteritemSpacing = 7.0f;
        flowLayout.minimumLineSpacing = 9.0f;
        collectProductView = [[UICollectionView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, height) collectionViewLayout:flowLayout];
        [collectProductView setBackgroundColor:[UIColor clearColor]];
        [collectProductView registerNib:[UINib nibWithNibName:@"HomeCommerceCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HomeCommerceCellIdentifier"];
        
        //collectProductView = [[UICollectionView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, height)];
        collectProductView.dataSource = self;
        collectProductView.delegate = self;
        collectProductView.scrollEnabled = NO;
        [scrollContentView addSubview:collectProductView];
    }
}

- (IBAction)onClickProductButton:(id)sender {
    
    selectType = SELECT_PROD;
    
    [self selectItem:selectType];

    [collectProductView reloadData];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
//    [self setScrollContentSize];
}

- (void) createItemView {
    
    if (tblItemView == nil) {
        tblItemView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH * 2, 0, SCREEN_WIDTH, height)];
        [tblItemView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblItemView.dataSource = self;
        tblItemView.delegate = self;
        tblItemView.scrollEnabled = NO;
        [tblItemView registerNib:[UINib nibWithNibName:@"HomeItemTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeItemCellIdentifier"];
        [scrollContentView addSubview:tblItemView];
    }
}

- (IBAction)onClickItemButton:(id)sender {
    
    selectType = SELECT_ITEM;
    
    [self selectItem:selectType];

    [tblItemView reloadData];
    
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH * 2, 0)];
//    [self setScrollContentSize];
    
}

- (void) createServiceView {
    
    if (tblServiceView == nil) {
        
        tblServiceView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH * 3, 0, SCREEN_WIDTH, height)];
        [tblServiceView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblServiceView.dataSource = self;
        tblServiceView.delegate = self;
        tblServiceView.scrollEnabled = NO;
        [tblServiceView registerNib:[UINib nibWithNibName:@"HomeServiceTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeServiceCellIdentifier"];
        [scrollContentView addSubview:tblServiceView];
    }
}

- (IBAction)onClickServiceButton:(id)sender {
    
    selectType = SELECT_SERV;
    
    [self selectItem:selectType];

    [tblServiceView reloadData];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH * 3, 0)];
//    [self setScrollContentSize];
    
}

- (IBAction)onClickFavouriteButton:(id)sender {
    self.btnFavourite.selected = !self.btnFavourite.selected;
    [self setInterested];
}

- (void)setInterested {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setInterest" forKey:@"pAct"];
    [dicParams setObject:currentAccountID forKey:@"accountId"];
    if(self.btnFavourite.isSelected) {
        [dicParams setObject:@"0" forKey:@"val"];
    }else{
        [dicParams setObject:@"1" forKey:@"val"];
    }
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETINTEREST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
            }
        }
    }];
}
- (IBAction)onReqAccountShowAction:(id)sender {
    if([friendDictionary[@"reqCodeSenderId"] longValue] > 0) {
        HomeFamiliarDetailViewController *homeEnterDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
        [CommonData sharedInstance].selectedFriendAccountID = reqCodeSenderId;
        [self.navigationController pushViewController:homeEnterDetailVC animated:YES];
    }
}

- (IBAction)onCompanyShowAction:(id)sender {
    if([friendDictionary[@"enterId"] longValue] > 0) {
        HomeFamiliarDetailViewController *homeEnterDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
        [CommonData sharedInstance].selectedFriendAccountID = friendDictionary[@"enterId"];
        [self.navigationController pushViewController:homeEnterDetailVC animated:YES];
    }
}

- (IBAction)onNetworkShowAction:(id)sender {
    if(![friendDictionary[@"weburl"] isEqualToString:@""]) {
        NSURL* url = [NSURL URLWithString:friendDictionary[@"weburl"]];
        if([[url absoluteString] containsString:@"http"] == false)
        {
            url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [url absoluteString] ]];
        }
        [[UIApplication sharedApplication] openURL:url];
        /*
        WebViewController *webVC = [[WebViewController alloc] initWithNibName:@"WebViewController" bundle:nil];
        webVC.webUrl = friendDictionary[@"weburl"];
        [self.navigationController pushViewController:webVC animated:YES];
         */
    }
}	

- (IBAction)onEvaluate:(id)sender {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
    } else {
        WriteEvalViewController *writeEvalVC = [[WriteEvalViewController alloc] initWithNibName:@"WriteEvalViewController" bundle:nil];
        writeEvalVC.isEvaluate = YES;
        if(friendDictionary)
            writeEvalVC.evalAccountDictionary = friendDictionary;
        [self.navigationController pushViewController:writeEvalVC animated:YES];
    }
}

- (IBAction)onCallingAction:(id)sender {
    if(![callString isEqualToString:@""]) {
        NSString *URLString = [@"tel:" stringByAppendingString:callString];
        NSURL *URL = [NSURL URLWithString:URLString];
//        if([[URL absoluteString] containsString:@"http"] == false)
//        {
//            URL = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", [URL absoluteString] ]];
//        }
        [[UIApplication sharedApplication] openURL:URL];
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"onContact" forKey:@"pAct"];
        [dicParams setObject:friendDictionary[@"id"] forKey:@"accountId"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        
        [[WebAPI sharedInstance] sendPostRequest:@"onContact" Parameters:dicParams :^(NSObject *resObj) {
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            //[GeneralUtil hideProgress];
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    
                }else{
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                         duration:3.0
                                         position:CSToastPositionCenter
                                            style:nil];
                }
            }else{
                [appDelegate.window makeToast:@"网络不连接"
                                     duration:3.0
                                     position:CSToastPositionCenter
                                        style:nil];
            }
        }];
    }
}
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
}

- (void)setScrollContentSize {
    switch (selectType) {
        case SELECT_EVAL:
            if(fEvaluateTableViewHeight > 244) {
                self.scrollContentViewHeight.constant = fEvaluateTableViewHeight;
                self.scrollViewContentHeight.constant = fScrollViewContentHeight + fEvaluateTableViewHeight - 244;
            }else{
                if(fEvaluateTableViewHeight == 45) {
                    self.scrollContentViewHeight.constant = 244.f;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight;
                }else{
                    self.scrollContentViewHeight.constant = fEvaluateTableViewHeight;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight + fEvaluateTableViewHeight - 244;
                }
            }
            if(evaluateArray.count > 0) {
                self.blankView.hidden = YES;
            }else{
                self.blankView.hidden = NO;
            }
            break;
        case SELECT_PROD:
            if(fProductTableViewHeight > 244) {
                self.scrollContentViewHeight.constant = fProductTableViewHeight;
                self.scrollViewContentHeight.constant = fScrollViewContentHeight + fProductTableViewHeight - 244;
            }else{
                if(fProductTableViewHeight == 0) {
                    self.scrollContentViewHeight.constant = 244.f;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight;
                }else{
                    self.scrollContentViewHeight.constant = fProductTableViewHeight;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight + fProductTableViewHeight - 244;
                }
            }
            if(productArray.count > 0) {
                self.blankView.hidden = YES;
            }else{
                self.blankView.hidden = NO;
            }
            break;
        case SELECT_ITEM:
            if(fItemTableViewHeight > 244) {
                self.scrollContentViewHeight.constant = fItemTableViewHeight;
                self.scrollViewContentHeight.constant = fScrollViewContentHeight + fItemTableViewHeight - 244;
            }else{
                if(fItemTableViewHeight == 0) {
                    self.scrollContentViewHeight.constant = 244.f;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight;
                }else{
                    self.scrollContentViewHeight.constant = fItemTableViewHeight;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight + fItemTableViewHeight - 244;
                }
            }
            if(itemArray.count > 0) {
                self.blankView.hidden = YES;
            }else{
                self.blankView.hidden = NO;
            }
            break;
        case SELECT_SERV:
            if(fServiceTableViewHeight > 244) {
                self.scrollContentViewHeight.constant = fServiceTableViewHeight;
                self.scrollViewContentHeight.constant = fScrollViewContentHeight + fServiceTableViewHeight - 244;
            }else{
                if(fServiceTableViewHeight == 0) {
                    self.scrollContentViewHeight.constant = 244.f;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight;
                }else{
                    self.scrollContentViewHeight.constant = fServiceTableViewHeight;
                    self.scrollViewContentHeight.constant = fScrollViewContentHeight + fServiceTableViewHeight - 244;
                }
            }
            if(serviceArray.count > 0) {
                self.blankView.hidden = YES;
            }else{
                self.blankView.hidden = NO;
            }
            break;
        default:
            break;
    }
    NSInteger aKind = [friendDictionary[@"akind"] integerValue];
    if(aKind != PERSONAL_KIND)
        self.lblOfficeContentBottom.constant = self.selectView.frame.size.height + self.scrollContentViewHeight.constant + 36 + 49;
    [self.bgScrollView setContentSize:CGSizeMake(SCREEN_WIDTH, self.scrollViewContentHeight.constant + 124)];
}

- (IBAction)onShareAction:(id)sender {
    [self shareMenu];
}

#pragma mark ShareSDK
- (void)shareMenu
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    NSArray* imageArray = @[[[NSBundle mainBundle] pathForResource:@"logo_new@2x" ofType:@"png"]];
    long accountID = [friendDictionary[@"id"] longValue];
    long mineID = [[CommonData sharedInstance].userInfo[@"id"] longValue];
    NSString *url = @"";
    NSString *shareText = @"";
    NSString *realName = @"";
    NSString *shareTitle = @"";
    NSString *xyName = friendDictionary[@"xyName"];
    NSString *job = friendDictionary[@"job"];
    NSString *credit = friendDictionary[@"credit"];
    NSString *positiveFeedbackCnt = friendDictionary[@"positiveFeedbackCnt"];
    NSString *negativeFeedbackCnt = friendDictionary[@"negativeFeedbackCnt"];
    NSString *mainJob = friendDictionary[@"mainJob"];
    NSString *recommend = friendDictionary[@"recommend"];
    
    NSInteger akind = [friendDictionary[@"akind"] integerValue];
    if (akind == PERSONAL_KIND) {
        url = [NSString stringWithFormat:@"%@%@%ld%s%ld", BASE_WEB_URL, @"/geren.html?accountId=",accountID,"&shareUserId=",mineID];
        realName = friendDictionary[@"realname"];
        shareTitle = @"【好友详情】您的好友给您分享了一份好友详情，立即查看！";
        shareText = [NSString stringWithFormat:@"%@, %@, %@, 诚信度%@%%, %@个正面评价, %@个负面评价，点击查看完整信息！", realName,xyName,job,credit,positiveFeedbackCnt,negativeFeedbackCnt];
    }
    else {
        url = [NSString stringWithFormat:@"%@%@%ld%s%ld", BASE_WEB_URL, @"/qiye.html?accountId=",accountID,"&shareUserId=",mineID];
        realName = friendDictionary[@"enterName"];
        shareTitle = @"【企业详情】您的好友给您分享了一份企业详情，立即查看！";
        shareText = [NSString stringWithFormat:@"%@, 诚信度%@%%, %@, 我们承诺：%@，点击查看完整信息！", realName,credit,mainJob,recommend];
    }
    
    
    [shareParams SSDKSetupShareParamsByText:shareText
                                     images:imageArray
                                        url:[NSURL URLWithString:url]
                                      title: shareTitle
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
    NSInteger akind = [friendDictionary[@"akind"] integerValue];
    if (akind == PERSONAL_KIND)
        [dicParams setObject:[NSNumber numberWithInteger:4] forKey:@"kind"];
    else
        [dicParams setObject:[NSNumber numberWithInteger:5] forKey:@"kind"];
    
    [dicParams setObject:friendDictionary[@"id"] forKey:@"id"];
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
