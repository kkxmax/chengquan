//
//  HomeEnterpriseViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeEnterpriseViewController.h"
//#import "HomeEnterpriseTableViewCell.h"
#import "HomeFamiliarTableViewCell.h"

#import "Global.h"
#import "UIImageView+WebCache.h"
#import "AAPullToRefresh.h"

@interface HomeEnterpriseViewController ()
{
    NSMutableArray *enterArray;

    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;

}
@end

@implementation HomeEnterpriseViewController
@synthesize homeEnterpriseTableView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.homeEnterpriseTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
    // NSNotification for Update Interesting
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateInterestingView:) name:UPDATE_ENTERPRISE_INTERESTING_NOTIFICATION object:nil];
    
    // NSNotification for Reload Changed Data
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadChangedData:) name:RELOAD_ENTERPRISE_DATA_NOTIFICATION object:nil];

    enterArray = [NSMutableArray array];
    currentSortOrderIndex = 1;
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.homeEnterpriseTableView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.homeEnterpriseTableView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;

    [self.homeEnterpriseTableView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
    
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(baseDelegate && [keyPath isEqualToString:@"contentSize"]) {
        self.homeEnterpriseTableView.frame = CGRectMake(self.homeEnterpriseTableView.frame.origin.x, self.homeEnterpriseTableView.frame.origin.y, self.homeEnterpriseTableView.frame.size.width, self.homeEnterpriseTableView.contentSize.height);
        self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.homeEnterpriseTableView.frame.size.height);
        [baseDelegate finishedLoadingData:1];
    }
    
}

- (void)dealloc
{
    [self.homeEnterpriseTableView removeObserver:self forKeyPath:@"contentSize" context:NULL];
}

-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_ENTERPRISE_INTERESTING_NOTIFICATION object:nil];
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(enterArray)
        [enterArray removeAllObjects];
    [self getEnterFromServer:[CommonData sharedInstance].choiceEnterpriseCity
                               EnterKind:[CommonData sharedInstance].choiceEnterpriseKind
                            XyleixingIds:[CommonData sharedInstance].choiceEnterpriseIds
                                   Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                                  Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                                 Keyword:[CommonData sharedInstance].searchEnterpriseText];
}

- (void)refreshBottomItems {
    refreshStartIndex = enterArray.count;
    [self getEnterFromServer:[CommonData sharedInstance].choiceEnterpriseCity
                   EnterKind:[CommonData sharedInstance].choiceEnterpriseKind
                XyleixingIds:[CommonData sharedInstance].choiceEnterpriseIds
                       Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                      Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                     Keyword:[CommonData sharedInstance].searchEnterpriseText];
}

- (void)getEnterFromServer:cityName EnterKind:(NSString*)enterKind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword  {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getEnterList" forKey:@"pAct"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", refreshStartIndex] forKey:@"start"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT] forKey:@"length"];
    [dicParams setObject:enterKind forKey:@"enterKind"];
    [dicParams setObject:keyword forKey:@"keyword"];
    [dicParams setObject:xyleixingIds forKey:@"xyleixingIds"];
    [dicParams setObject:[CommonData sharedInstance].searchCodeText forKey:@"keywordCode"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETENTERLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *enterList = (NSArray *)(dicRes[@"data"]);
//                [enterArray removeAllObjects];
                for(int i = 0; i < enterList.count; i++) {
                    NSDictionary *enterDic = (NSDictionary *)(enterList[i]);
                    [enterArray addObject:enterDic];
                }
                [self.homeEnterpriseTableView reloadData];
                if(baseDelegate) {
                    [baseDelegate stopLoadingIndicator];
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
        [GeneralUtil hideProgress];
    }];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeFamiliarTableViewCell *homeFamiliarTableCell = (HomeFamiliarTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeFamiliarCellIdentifier" forIndexPath:indexPath];
    if(enterArray.count <= indexPath.row)
        return homeFamiliarTableCell;

    NSDictionary *friendDic = (NSDictionary *)(enterArray[indexPath.row]);
    NSString *logoImageName = friendDic[@"logo"];
//    NSInteger enterKind = [friendDic[@"enterKind"] integerValue];
    
    [homeFamiliarTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_enter.png"]];
    
    /*
    if (enterKind == ENTER_TYPE_PERSONAL) {
        [homeFamiliarTableCell.userTypeLabel setText:@"个体户"];
    } else {
        [homeFamiliarTableCell.userTypeLabel setText:@"公司"];
    }
    */
    homeFamiliarTableCell.nameLabel.text = [GeneralUtil getUserName:friendDic];
    homeFamiliarTableCell.reqViewHeightConstraint.constant = 0.f;
    
    [homeFamiliarTableCell.nameLabel sizeToFit];
    NSString *xyName= friendDic[@"xyName"];
    if ([xyName isEqualToString:@""]) {
        [homeFamiliarTableCell.xyNameButton setHidden:YES];
    } else {
        [homeFamiliarTableCell.xyNameButton setHidden:NO];
        [homeFamiliarTableCell.xyNameButton setTitle:friendDic[@"xyName"] forState:UIControlStateNormal];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGSize stringSize = [homeFamiliarTableCell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
            int bw = stringSize.width + 12;
            if(homeFamiliarTableCell.nameLabel.frame.size.width > (tableView.frame.size.width - 75 - bw - homeFamiliarTableCell.nameLabel.frame.origin.x)) {
                [homeFamiliarTableCell.nameLabel setFrame:CGRectMake(homeFamiliarTableCell.nameLabel.frame.origin.x, homeFamiliarTableCell.nameLabel.frame.origin.y, tableView.frame.size.width - 75 - bw - homeFamiliarTableCell.nameLabel.frame.origin.x, homeFamiliarTableCell.nameLabel.frame.size.height)];
            }
            CGRect nameLabelFrame = homeFamiliarTableCell.nameLabel.frame;
            [homeFamiliarTableCell.xyNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y + 2, bw, 16)];
        });
    }
    
    homeFamiliarTableCell.codeLabel.text = friendDic[@"code"];
    NSArray *productsArray = (NSArray *)(friendDic[@"products"]);
    
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
        homeFamiliarTableCell.productsLabel.text = productNames;
        homeFamiliarTableCell.productViewHeightConstraint.constant = 23.f;
        
    }else{
        homeFamiliarTableCell.productViewHeightConstraint.constant = 0.f;
    }
    
    NSArray *itemsArray = (NSArray *)(friendDic[@"items"]);
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
        homeFamiliarTableCell.itemLabel.text = itemNames;
        homeFamiliarTableCell.itemViewHeightConstraint.constant = 23.f;
    }else{
        homeFamiliarTableCell.itemViewHeightConstraint.constant = 0.f;
    }
    
    NSArray *servicessArray = (NSArray *)(friendDic[@"services"]);
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
        homeFamiliarTableCell.serviceLabel.text = serviceNames;
        homeFamiliarTableCell.serviceViewHeightConstraint.constant = 23.f;
    }else{
        homeFamiliarTableCell.serviceViewHeightConstraint.constant = 0.f;
    }
    
    
    if([friendDic[@"credit"] longValue] == 0) {
        homeFamiliarTableCell.viewCountLabel.text = @"暂无";
    }else{
        homeFamiliarTableCell.viewCountLabel.text = [NSString stringWithFormat:@"%ld%%", (long)[friendDic[@"credit"] longValue]];
    }
    homeFamiliarTableCell.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[friendDic[@"electCnt"] longValue]];
    homeFamiliarTableCell.feedbackCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[friendDic[@"feedbackCnt"] longValue]];
    
    if([friendDic[@"interested"] integerValue] == 1) {
        homeFamiliarTableCell.interestedButton.selected = NO;
    }else{
        homeFamiliarTableCell.interestedButton.selected = YES;
    }
    
    homeFamiliarTableCell.accountID = [NSString stringWithFormat:@"%d", (int)[friendDic[@"id"] intValue]];
    homeFamiliarTableCell.cellIndex = indexPath.row;
    homeFamiliarTableCell.cellType = SUB_HOME_ENTERPRISE;
    return homeFamiliarTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return enterArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 220.f;
    if(enterArray.count <= indexPath.row)
        return 0.f;
    NSDictionary *enterDic = (NSDictionary *)(enterArray[indexPath.row]);
    NSArray *productsArray = (NSArray *)(enterDic[@"products"]);
    if(productsArray.count == 0 )
        homeTableCellHeight -= 23.f;
    NSArray *itemsArray = (NSArray *)(enterDic[@"items"]);
    if(itemsArray.count == 0 )
        homeTableCellHeight -= 23.f;
    NSArray *servicessArray = (NSArray *)(enterDic[@"services"]);
    if(servicessArray.count == 0 )
        homeTableCellHeight -= 23.f;
    // for enterprise cell, reqCodeSender should not be shown
    homeTableCellHeight -= 23.f;
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *enterDic = (NSDictionary *)[enterArray objectAtIndex:indexPath.row];
    int nTestStatus = [enterDic[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [appDelegate.window makeToast:@"未认证的熟人／企业"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }

    [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%d", (int)[enterDic[@"id"] intValue]];
    [CommonData sharedInstance].detailFamiliarEnterpriseIndex = SUB_HOME_ENTERPRISE;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEFAMILIARDETAIL_VIEW_NOTIFICATION object:indexPath];
}

- (void)updateInterestingView:(NSNotification *)notification {
    NSDictionary *interstedDic = (NSDictionary *)(notification.object);
    NSInteger cellIndex = [interstedDic[@"cellIndex"] integerValue];
    BOOL isInterested = [interstedDic[@"isInterested"] boolValue];
    NSMutableDictionary *enterDic = (NSMutableDictionary *)(enterArray[cellIndex]);
    enterDic[@"interested"] = isInterested? @"1" : @"0";
    [enterArray replaceObjectAtIndex:cellIndex withObject:enterDic];
    [self.homeEnterpriseTableView reloadData];
}

- (void)reloadChangedData:(NSNotification *)notification {
//    [self getEnterFromServer:[CommonData sharedInstance].choiceEnterpriseCity
//                               EnterKind:[CommonData sharedInstance].choiceEnterpriseKind
//                            XyleixingIds:[CommonData sharedInstance].choiceEnterpriseIds
//                                   Start:@""
//                                  Length:@""
//                                 Keyword:[CommonData sharedInstance].searchEnterpriseText];
}


@end
