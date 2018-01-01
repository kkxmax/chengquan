//
//  HomeFamiliarViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeFamiliarViewController.h"
#import "HomeFamiliarTableViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "AAPullToRefresh.h"

@interface HomeFamiliarViewController ()
{
    NSMutableArray *friendArray;
    NSMutableArray *cellHeightArray;
    
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;
}
@end

@implementation HomeFamiliarViewController
@synthesize currentSortOrderIndex;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
   [self.homeFamiliarTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
    // NSNotification for Update Interesting
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateInterestingView:) name:UPDATE_FAMILIAR_INTERESTING_NOTIFICATION object:nil];
    // NSNotification for Reload Changed Data
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadChangedData:) name:RELOAD_FAMILIAR_DATA_NOTIFICATION object:nil];

    friendArray = [NSMutableArray array];
    currentSortOrderIndex = 1;

    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.homeFamiliarTableView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.homeFamiliarTableView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;
    [self.homeFamiliarTableView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
    
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(object == self.homeFamiliarTableView) {
        self.homeFamiliarTableView.frame = CGRectMake(self.homeFamiliarTableView.frame.origin.x, self.homeFamiliarTableView.frame.origin.y, self.homeFamiliarTableView.frame.size.width, self.homeFamiliarTableView.contentSize.height);
        self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.homeFamiliarTableView.frame.size.height);
        if(baseDelegate)
            [baseDelegate finishedLoadingData:0];
    }
}

- (void)dealloc
{
    [self.homeFamiliarTableView removeObserver:self forKeyPath:@"contentSize" context:NULL];
}

-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_FAMILIAR_INTERESTING_NOTIFICATION object:nil];

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
    if(friendArray)
        [friendArray removeAllObjects];
    [self getFriendFromServer:[CommonData sharedInstance].choiceFamiliarCity
                        aKind:[CommonData sharedInstance].choiceFamiliarKind
                 XyleixingIds:[CommonData sharedInstance].choiceFamiliarIds
                        Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                       Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                      Keyword:[CommonData sharedInstance].searchFamiliarText];

}

- (void)refreshBottomItems {
    refreshStartIndex = friendArray.count;
    [self getFriendFromServer:[CommonData sharedInstance].choiceFamiliarCity
                        aKind:[CommonData sharedInstance].choiceFamiliarKind
                 XyleixingIds:[CommonData sharedInstance].choiceFamiliarIds
                        Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                       Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                      Keyword:[CommonData sharedInstance].searchFamiliarText];
}
#pragma mark - Web api
- (void)getFriendFromServer:cityName aKind:(NSString*)akind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getFriendList" forKey:@"pAct"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", refreshStartIndex] forKey:@"start"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT] forKey:@"length"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:akind forKey:@"akind"];
    [dicParams setObject:xyleixingIds forKey:@"xyleixingIds"];
    [dicParams setObject:keyword forKey:@"keyword"];
    [dicParams setObject:[CommonData sharedInstance].searchCodeText forKey:@"keywordCode"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETFRIENDLIST Parameters:dicParams :^(NSObject *resObj) {
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *friendList = (NSArray *)(dicRes[@"data"]);
//                [friendArray removeAllObjects];
                for(int i = 0; i < friendList.count; i++) {
                    NSDictionary *friendDic = (NSDictionary *)(friendList[i]);
                    [friendArray addObject:friendDic];
                }
                [self.homeFamiliarTableView reloadData];
                
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
    HomeFamiliarTableViewCell *homeFamiliarTableCell;
    homeFamiliarTableCell = (HomeFamiliarTableViewCell*)[tableView dequeueReusableCellWithIdentifier: @"HomeFamiliarCellIdentifier" forIndexPath:indexPath];
    if(friendArray.count <= indexPath.row)
        return homeFamiliarTableCell;
    NSDictionary *friendDic = (NSDictionary *)(friendArray[indexPath.row]);
    NSString *logoImageName = friendDic[@"logo"];
    NSInteger aKind = [friendDic[@"akind"] integerValue];
    NSInteger testStatus = [friendDic[@"testStatus"] integerValue];
    
    if (testStatus == TEST_STATUS_PASSED) {
        if (aKind == PERSONAL_KIND) {
            [homeFamiliarTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_person.png"]];
            [homeFamiliarTableCell.userTypeLabel setText:@"个人"];
        } else {
            [homeFamiliarTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_enter.png"]];
            [homeFamiliarTableCell.userTypeLabel setText:@"企业"];
        }
    } else { //未认证
        [homeFamiliarTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_person.png"]];
        [homeFamiliarTableCell.userTypeLabel setText:@"未认证"];
    }
    
    NSString *strUserName = [GeneralUtil getUserName:friendDic];
    if(strUserName.length > HOME_NAME_MAX_LENGTH) {
        strUserName = [NSString stringWithFormat:@"%@…", [strUserName substringWithRange:NSMakeRange(0, HOME_NAME_MAX_LENGTH)]];
    }
    homeFamiliarTableCell.nameLabel.text = strUserName;
    int nReqCodeSenderAKind = [friendDic[@"reqCodeSenderAkind"] intValue];
    NSString *reqName = @"";
    if([friendDic[@"reqCodeSenderId"] longValue] > 0) {
        if(nReqCodeSenderAKind == PERSONAL_KIND) {
            reqName = friendDic[@"reqCodeSenderRealname"];
        }else{
            reqName = friendDic[@"reqCodeSenderEnterName"];
        }
        if([reqName isEqualToString:@""]) {
            reqName = friendDic[@"reqCodeSenderMobile"];
        }
        homeFamiliarTableCell.reqViewHeightConstraint.constant = 23.f;
    } else {
        homeFamiliarTableCell.reqViewHeightConstraint.constant = 0.f;
    }
    if([friendDic[@"inviterFriendLevel"] isEqualToString:@""]) {
        homeFamiliarTableCell.reqCodeSenderLabel.text = reqName;
    } else {
        homeFamiliarTableCell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%@-%@", friendDic[@"inviterFriendLevel"], reqName];
    }
    [homeFamiliarTableCell.nameLabel sizeToFit];
    NSString *xyName= friendDic[@"xyName"];

    if ([xyName isEqualToString:@""]) {
        [homeFamiliarTableCell.xyNameButton setHidden:YES];
    } else {
        if(xyName.length > HOME_TAG_MAX_LENGTH) {
            xyName = [NSString stringWithFormat:@"%@…", [xyName substringWithRange:NSMakeRange(0, HOME_TAG_MAX_LENGTH)]];
        }
        [homeFamiliarTableCell.xyNameButton setHidden:NO];
        [homeFamiliarTableCell.xyNameButton setTitle:xyName forState:UIControlStateNormal];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGSize stringSize = [homeFamiliarTableCell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
            int bw = stringSize.width + 12;
//            if(homeFamiliarTableCell.nameLabel.frame.size.width > (tableView.frame.size.width - 75 - bw - homeFamiliarTableCell.nameLabel.frame.origin.x)) {
//                [homeFamiliarTableCell.nameLabel setFrame:CGRectMake(homeFamiliarTableCell.nameLabel.frame.origin.x, homeFamiliarTableCell.nameLabel.frame.origin.y, tableView.frame.size.width - 75 - bw - homeFamiliarTableCell.nameLabel.frame.origin.x, homeFamiliarTableCell.nameLabel.frame.size.height)];
//            }
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
    homeFamiliarTableCell.cellType = SUB_HOME_PERSONAL;
    return homeFamiliarTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return friendArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 220.f;
    if(friendArray.count <= indexPath.row)
        return 0.f;
    NSDictionary *friendDic = (NSDictionary *)(friendArray[indexPath.row]);
    NSArray *productsArray = (NSArray *)(friendDic[@"products"]);
    if(productsArray.count == 0 )
        homeTableCellHeight -= 23.f;
    NSArray *itemsArray = (NSArray *)(friendDic[@"items"]);
    if(itemsArray.count == 0 )
        homeTableCellHeight -= 23.f;
    NSArray *servicessArray = (NSArray *)(friendDic[@"services"]);
    if(servicessArray.count == 0 )
        homeTableCellHeight -= 23.f;
    NSInteger aKind = [friendDic[@"akind"] integerValue];
    if (aKind == PERSONAL_KIND) {
        if([friendDic[@"reqCodeSenderRealname"] isEqualToString:@""]) {
            homeTableCellHeight -= 23.f;
        }
    }else{
        if([friendDic[@"reqCodeSenderEnterName"] isEqualToString:@""]) {
            homeTableCellHeight -= 23.f;
        }
    }
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *friendDic = (NSDictionary *)[friendArray objectAtIndex:indexPath.row];
    int nTestStatus = [friendDic[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [appDelegate.window makeToast:@"未认证的熟人／企业"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%d", (int)[friendDic[@"id"] intValue]];
    [CommonData sharedInstance].detailFamiliarEnterpriseIndex = SUB_HOME_PERSONAL;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEFAMILIARDETAIL_VIEW_NOTIFICATION object:indexPath];
}

- (void)updateInterestingView:(NSNotification *)notification {
    NSDictionary *interstedDic = (NSDictionary *)(notification.object);
    NSInteger cellIndex = [interstedDic[@"cellIndex"] integerValue];
    BOOL isInterested = [interstedDic[@"isInterested"] boolValue];
    NSMutableDictionary *friendDic = (NSMutableDictionary *)(friendArray[cellIndex]);
    friendDic[@"interested"] = isInterested? @"1" : @"0";
    [friendArray replaceObjectAtIndex:cellIndex withObject:friendDic];
    [self.homeFamiliarTableView reloadData];
}

- (void)reloadChangedData:(NSNotification *)notification {
//    [self getFriendFromServer:[CommonData sharedInstance].choiceFamiliarCity
//                                  aKind:[CommonData sharedInstance].choiceFamiliarKind
//                           XyleixingIds:[CommonData sharedInstance].choiceFamiliarIds
//                                  Start:@""
//                                 Length:@""
//                                Keyword:[CommonData sharedInstance].searchFamiliarText];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
