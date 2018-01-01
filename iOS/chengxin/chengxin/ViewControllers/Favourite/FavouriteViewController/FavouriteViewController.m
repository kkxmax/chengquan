//
//  FavouriteViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "FavouriteViewController.h"
#import "FavouriteItemTableViewCell.h"
#import "FavouritesTableViewController.h"
#import "Global.h"
#import "CommonData.h"
#import "WebAPI.h"
#import "HomeFamiliarTableViewCell.h"
#import "UIImageView+WebCache.h"
#import "AAPullToRefresh.h"
#import "CXLFavouritesTableViewController.h"

@interface FavouriteViewController ()
{
    int nTestStatus;
    NSMutableArray* arrayAccounts;
    NSString* reqCodeSenderId;
    
    AAPullToRefresh *topFavouriteItemRefreshView;
    AAPullToRefresh *bottomFavouriteItemRefreshView;
    NSInteger refreshStartIndex;

    AAPullToRefresh *topChengxinLianItemRefreshView;
    AAPullToRefresh *bottomChengxinLianItemRefreshView;
}
@end

@implementation FavouriteViewController
@synthesize viewBlank, viewBlank2, viewNoNetwork, viewNoNetwork2, messageNumberLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.favouriteItemTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
    [self.chengxinlianItemTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
    
    [self setTopTab:em_WoDeGuanZhu];
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
    arrayAccounts = [NSMutableArray array];
    __weak typeof(self) weakSelf = self;
    topFavouriteItemRefreshView = [self.wodeScroll addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomFavouriteItemRefreshView = [self.wodeScroll addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    refreshStartIndex = 0;

    topChengxinLianItemRefreshView = [self.chengxinlianScroll addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomChengxinLianItemRefreshView = [self.chengxinlianScroll addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];

}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
    [self refreshTopItems];
    topFavouriteItemRefreshView.showPullToRefresh = YES;
    bottomFavouriteItemRefreshView.showPullToRefresh = YES;
    topChengxinLianItemRefreshView.showPullToRefresh = YES;
    bottomChengxinLianItemRefreshView.showPullToRefresh = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    topFavouriteItemRefreshView.showPullToRefresh = NO;
    bottomFavouriteItemRefreshView.showPullToRefresh = NO;
    topChengxinLianItemRefreshView.showPullToRefresh = NO;
    bottomChengxinLianItemRefreshView.showPullToRefresh = NO;
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(arrayAccounts)
        [arrayAccounts removeAllObjects];
    [self getFavouriteDataFromServer:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
}

- (void)refreshBottomItems {
    refreshStartIndex = arrayAccounts.count;
    [self getFavouriteDataFromServer:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
}


- (void) getFavouriteDataFromServer:(NSInteger)start Length:(NSInteger)length{
    [GeneralUtil showProgress];
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMyInterestInfo" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:start] forKey:@"start"];
    [dicParams setObject:[NSNumber numberWithInt:length] forKey:@"length"];
    
    //[dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"to	ken"];
    
    [[WebAPI sharedInstance] sendPostRequest:@"getMyInterestInfo" Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topFavouriteItemRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomFavouriteItemRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [topChengxinLianItemRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomChengxinLianItemRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                
                self.lblMyAncestorCount.text = [NSString stringWithFormat:@"%@人", [[dicRes objectForKey:@"myAncestorCnt"] stringValue]];
                self.lblFriend1Count.text = [NSString stringWithFormat:@"%@人", [[dicRes objectForKey:@"friend1Cnt"] stringValue]];
                self.lblFriend2Count.text = [NSString stringWithFormat:@"%@人", [[dicRes objectForKey:@"friend2Cnt"] stringValue]];
                self.lblFriend3Count.text = [NSString stringWithFormat:@"%@人", [[dicRes objectForKey:@"friend3Cnt"] stringValue]];
                self.lblPersonalCount.text = [NSString stringWithFormat:@"%@人", [[dicRes objectForKey:@"personalCnt"] stringValue]];
                self.lblEnterpriseCount.text = [NSString stringWithFormat:@"%@人", [[dicRes objectForKey:@"enterCnt"] stringValue]];
                NSArray *friendList = (NSArray *)(dicRes[@"accountsRecommended"]);
                //                [friendArray removeAllObjects];
                for(int i = 0; i < friendList.count; i++) {
                    NSDictionary *friendDic = (NSDictionary *)(friendList[i]);
                    [arrayAccounts addObject:friendDic];
                }

                if (arrayAccounts.count == 0) {
                    viewBlank.hidden = NO;
                    viewBlank2.hidden = NO;
                    self.favouriteItemTableView.hidden = YES;
                    self.chengxinlianItemTableView.hidden = YES;
                }
                else {
                    viewBlank.hidden = YES;
                    viewBlank2.hidden = YES;
                    viewNoNetwork.hidden = YES;
                    viewNoNetwork2.hidden = YES;
                    self.favouriteItemTableView.hidden = NO;
                    self.chengxinlianItemTableView.hidden = NO;
                    
                    [self.favouriteItemTableView reloadData];
                    [self.chengxinlianItemTableView reloadData];
                    [self setTopTab:tab_selection];
                }
            }
            else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
                if (arrayAccounts.count == 0) {
                    viewNoNetwork.hidden = NO;
                    viewNoNetwork2.hidden = NO;
                    self.favouriteItemTableView.hidden = YES;
                    self.chengxinlianContentsView.hidden = YES;
                }
            }
        }
    }];

}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma
- (void)setUI
{
    
    //self.navigationController.navigationBar.hidden = YES;
}

- (void)setTopTab:(Favourite_Tab)selection
{
    if(selection == em_WoDeGuanZhu)
    {
        if([self.chengxinlianContentsView superview] != nil)
            [self.chengxinlianContentsView removeFromSuperview];
        if([self.myFavouriteContentsView superview] != self.contentsContainer)
        {
            [self.contentsContainer addSubview:self.myFavouriteContentsView];
            [self.myFavouriteContentsView setFrame:CGRectMake(0, 0, self.contentsContainer.frame.size.width, self.contentsContainer.frame.size.height)];
        }
        self.lblMyFavouriteUnderline.hidden = NO;
        self.lblChengXinLianUnderline.hidden = YES;
        [self.btnChengXinLian setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnMyFavourite setTitleColor:[UIColor colorWithRed:246/255.0f green:184/255.0f blue:17/255.0f alpha:1]  forState:UIControlStateNormal];
        
        [self.wodeScroll setContentSize:CGSizeMake(SCREEN_WIDTH, 137 + self.favouriteItemTableView.contentSize.height)];
        self.favouriteItemTableView.frame = CGRectMake(self.favouriteItemTableView.frame.origin.x, self.favouriteItemTableView.frame.origin.y, self.favouriteItemTableView.frame.size.width, self.favouriteItemTableView.contentSize.height);
        
    }else if(selection == em_ChengXinLian)
    {
        if([self.myFavouriteContentsView superview] != nil)
            [self.myFavouriteContentsView removeFromSuperview];
        if([self.chengxinlianContentsView superview] != self.contentsContainer)
        {
            [self.contentsContainer addSubview:self.chengxinlianContentsView];
            [self.chengxinlianContentsView setFrame:CGRectMake(0, 0, self.contentsContainer.frame.size.width, self.contentsContainer.frame.size.height)];
        }
        self.lblMyFavouriteUnderline.hidden = YES;
        self.lblChengXinLianUnderline.hidden = NO;
        [self.btnMyFavourite setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnChengXinLian setTitleColor:[UIColor colorWithRed:246/255.0f green:184/255.0f blue:17/255.0f alpha:1]  forState:UIControlStateNormal]  ;
        [self.chengxinlianScroll setContentSize:CGSizeMake(SCREEN_WIDTH, 227 + self.chengxinlianItemTableView.contentSize.height)];
        self.chengxinlianItemTableView.frame = CGRectMake(self.chengxinlianItemTableView.frame.origin.x, self.chengxinlianItemTableView.frame.origin.y, self.chengxinlianItemTableView.frame.size.width, self.chengxinlianItemTableView.contentSize.height);
    }
    tab_selection = selection;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == self.favouriteItemTableView)
    {
        CGFloat homeTableCellHeight = 220.f;
        NSDictionary *friendDic = (NSDictionary *)(arrayAccounts[indexPath.row]);
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
    }else if(tableView == self.chengxinlianItemTableView)
    {
        CGFloat homeTableCellHeight = 220.f;
        NSDictionary *friendDic = (NSDictionary *)(arrayAccounts[indexPath.row]);
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
        return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"HomeFamiliarCellIdentifier";
    
    if(tableView == _favouriteItemTableView || tableView == _chengxinlianItemTableView)
    {
        HomeFamiliarTableViewCell *homeFamiliarTableCell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if(arrayAccounts.count <= indexPath.row)
            return homeFamiliarTableCell;

        NSDictionary *friendDic = (NSDictionary *)(arrayAccounts[indexPath.row]);
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
        return homeFamiliarTableCell;
    }
    
    return nil;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView == self.favouriteItemTableView || tableView == self.chengxinlianItemTableView)
    {
        return arrayAccounts.count;
    }
    return 0;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *friendDic = (NSDictionary *)[arrayAccounts objectAtIndex:indexPath.row];
    
    int testStatus = [friendDic[@"testStatus"] intValue];
    if(testStatus != 2) {
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
-(IBAction)onMyFavourite:(id)sender
{
    [self setTopTab:em_WoDeGuanZhu];
}
-(IBAction)onTrustSeries:(id)sender
{
    [self setTopTab:em_ChengXinLian];
}
-(IBAction)onEnterprise:(id)sender
{
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    vc.selectType = 1;
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onPersonal:(id)sender
{
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    vc.selectType = 0;
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onMyHome:(id)sender
{
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onFriends:(id)sender
{
    UIButton *button = (UIButton*) sender;
    if(button.tag == 0)
    {
        FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
        vc.selectType = 2;
        [self.navigationController pushViewController:vc animated:YES];
        return;
    }
    CXLFavouritesTableViewController* vc = [[CXLFavouritesTableViewController alloc] initWithNibName:@"CXLFavouritesTableViewController" bundle:nil];
    
    switch (button.tag) {
        case 0:
            vc.selectType = 2;
            break;
        case 1:
            vc.selectType = 3;
            break;
        case 2:
            vc.selectType = 4;
            break;
        case 3:
            vc.selectType = 5;
            break;
        default:
            break;
    }
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)showNotificationView:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
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
