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


@interface FavouriteViewController ()
{
    int nTestStatus;
    NSMutableArray* arrayAccounts;
}
@end

@implementation FavouriteViewController
@synthesize viewBlank, viewBlank2, viewNoNetwork, viewNoNetwork2, messageNumberLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    //[self.favouriteItemTableView registerNib:@"FavouriteItemTableViewCell" forCellReuseIdentifier:@"FavouriteItemCell"];
    [self setTopTab:em_WoDeGuanZhu];
    [self updateNotification];
    appDelegate.notificationDelegate = self;
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMyInterestInfo" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:[NSNumber numberWithInt:0] forKey:@"start"];
    
    //[dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"to	ken"];
    
    [[WebAPI sharedInstance] sendPostRequest:@"getMyInterestInfo" Parameters:dicParams :^(NSObject *resObj){
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {

                self.lblMyAncestorCount.text = [[dicRes objectForKey:@"myAncestorCnt"] stringValue];
                self.lblFriend1Count.text = [[dicRes objectForKey:@"friend1Cnt"] stringValue];
                self.lblFriend2Count.text = [[dicRes objectForKey:@"friend2Cnt"] stringValue];
                self.lblFriend3Count.text = [[dicRes objectForKey:@"friend3Cnt"] stringValue];
                self.lblPersonalCount.text = [[dicRes objectForKey:@"personalCnt"] stringValue];
                self.lblEnterpriseCount.text = [[dicRes objectForKey:@"enterCnt"] stringValue];
                arrayAccounts = [dicRes objectForKey:@"accountsRecommended"];
                
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
                }
            }
            else {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
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
    static NSString *CellIdentifier = @"Cell";
    
    if(tableView == _favouriteItemTableView)
    {
        HomeFamiliarTableViewCell *homeFamiliarTableCell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if(homeFamiliarTableCell == nil)
        {
            homeFamiliarTableCell = (HomeFamiliarTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"HomeFamiliarTableViewCell" owner:self options:nil] objectAtIndex:0];
        }
        
        NSDictionary *friendDic = (NSDictionary *)(arrayAccounts[indexPath.row]);
        NSString *logoImageName = friendDic[@"logo"];
        NSInteger aKind = [friendDic[@"akind"] integerValue];
        if(logoImageName) {
            homeFamiliarTableCell.logoImageView.clipsToBounds = YES;
            homeFamiliarTableCell.logoImageView.layer.cornerRadius = homeFamiliarTableCell.logoImageView.frame.size.width /2;
            homeFamiliarTableCell.logoEmptyRealImageView.hidden = YES;
            homeFamiliarTableCell.logoEmptyEnterpriseLabel.hidden = YES;
            [homeFamiliarTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
            
        }else{
            if (aKind == PERSONAL_KIND) {
                homeFamiliarTableCell.logoEmptyRealImageView.hidden = NO;
                homeFamiliarTableCell.logoEmptyEnterpriseLabel.hidden = YES;
            }else {
                homeFamiliarTableCell.logoEmptyRealImageView.hidden = YES;
                homeFamiliarTableCell.logoEmptyEnterpriseLabel.hidden = NO;
            }
        }
        
        if (aKind == PERSONAL_KIND) {
            [homeFamiliarTableCell.markImageView setImage:[UIImage imageNamed:@"blank_person"]];
            homeFamiliarTableCell.nameLabel.text = friendDic[@"realname"];
            if([friendDic[@"reqCodeSenderRealname"] isEqualToString:@""]) {
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 0.f;
            }else{
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 23.f;
                int nReqCodeSenderAKind = [friendDic[@"reqCodeSenderAkind"] intValue];
                homeFamiliarTableCell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, friendDic[@"reqCodeSenderRealname"]];
            }
        }else {
            [homeFamiliarTableCell.markImageView setImage:[UIImage imageNamed:@"blank_enterprise"]];
            homeFamiliarTableCell.nameLabel.text = friendDic[@"enterName"];
            if([friendDic[@"reqCodeSenderEnterName"] isEqualToString:@""]) {
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 0.f;
            }else{
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 23.f;
                int nReqCodeSenderAKind = [friendDic[@"reqCodeSenderAkind"] intValue];
                homeFamiliarTableCell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, friendDic[@"reqCodeSenderEnterName"]];
            }
        }
        [homeFamiliarTableCell.nameLabel sizeToFit];
        [homeFamiliarTableCell.xyNameButton setTitle:friendDic[@"xyName"] forState:UIControlStateNormal];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGRect nameLabelFrame = homeFamiliarTableCell.nameLabel.frame;
            CGSize stringSize = [homeFamiliarTableCell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
            [homeFamiliarTableCell.xyNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y - 2, stringSize.width, 16)];
        });
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
        homeFamiliarTableCell.viewCountLabel.text = [NSString stringWithFormat:@"%ld%%", (long)[friendDic[@"credit"] longValue]];
        homeFamiliarTableCell.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[friendDic[@"electCnt"] longValue]];
        homeFamiliarTableCell.feedbackCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[friendDic[@"feedbackCnt"] longValue]];
        
        if([friendDic[@"interested"] integerValue] == 1) {
            homeFamiliarTableCell.interestedButton.selected = YES;
        }else{
            homeFamiliarTableCell.interestedButton.selected = NO;
        }
        
        homeFamiliarTableCell.accountID = [NSString stringWithFormat:@"%d", (int)[friendDic[@"id"] intValue]];
        return homeFamiliarTableCell;
    }else if(tableView == _chengxinlianItemTableView)
    {
        HomeFamiliarTableViewCell *homeFamiliarTableCell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if(homeFamiliarTableCell == nil)
        {
            homeFamiliarTableCell = (HomeFamiliarTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"HomeFamiliarTableViewCell" owner:self options:nil] objectAtIndex:0];
        }
        
        NSDictionary *friendDic = (NSDictionary *)(arrayAccounts[indexPath.row]);
        NSString *logoImageName = friendDic[@"logo"];
        NSInteger aKind = [friendDic[@"akind"] integerValue];
        if(logoImageName) {
            homeFamiliarTableCell.logoImageView.clipsToBounds = YES;
            homeFamiliarTableCell.logoImageView.layer.cornerRadius = homeFamiliarTableCell.logoImageView.frame.size.width /2;
            homeFamiliarTableCell.logoEmptyRealImageView.hidden = YES;
            homeFamiliarTableCell.logoEmptyEnterpriseLabel.hidden = YES;
            [homeFamiliarTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
            
        }else{
            if (aKind == PERSONAL_KIND) {
                homeFamiliarTableCell.logoEmptyRealImageView.hidden = NO;
                homeFamiliarTableCell.logoEmptyEnterpriseLabel.hidden = YES;
            }else {
                homeFamiliarTableCell.logoEmptyRealImageView.hidden = YES;
                homeFamiliarTableCell.logoEmptyEnterpriseLabel.hidden = NO;
            }
        }
        
        if (aKind == PERSONAL_KIND) {
            [homeFamiliarTableCell.markImageView setImage:[UIImage imageNamed:@"blank_person"]];
            homeFamiliarTableCell.nameLabel.text = friendDic[@"realname"];
            if([friendDic[@"reqCodeSenderRealname"] isEqualToString:@""]) {
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 0.f;
            }else{
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 23.f;
                int nReqCodeSenderAKind = [friendDic[@"reqCodeSenderAkind"] intValue];
                homeFamiliarTableCell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, friendDic[@"reqCodeSenderRealname"]];
            }

        }else {
            [homeFamiliarTableCell.markImageView setImage:[UIImage imageNamed:@"blank_enterprise"]];
            homeFamiliarTableCell.nameLabel.text = friendDic[@"enterName"];
            if([friendDic[@"reqCodeSenderEnterName"] isEqualToString:@""]) {
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 0.f;
            }else{
                homeFamiliarTableCell.reqViewHeightConstraint.constant = 23.f;
                int nReqCodeSenderAKind = [friendDic[@"reqCodeSenderAkind"] intValue];
                homeFamiliarTableCell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, friendDic[@"reqCodeSenderEnterName"]];
            }
        }
        [homeFamiliarTableCell.nameLabel sizeToFit];
        [homeFamiliarTableCell.xyNameButton setTitle:friendDic[@"xyName"] forState:UIControlStateNormal];
        dispatch_async(dispatch_get_main_queue(), ^{
            CGRect nameLabelFrame = homeFamiliarTableCell.nameLabel.frame;
            CGSize stringSize = [homeFamiliarTableCell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
            [homeFamiliarTableCell.xyNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y - 2, stringSize.width, 16)];
        });
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
        homeFamiliarTableCell.viewCountLabel.text = [NSString stringWithFormat:@"%ld%%", (long)[friendDic[@"credit"] longValue]];
        homeFamiliarTableCell.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[friendDic[@"electCnt"] longValue]];
        homeFamiliarTableCell.feedbackCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[friendDic[@"feedbackCnt"] longValue]];
        
        if([friendDic[@"interested"] integerValue] == 1) {
            homeFamiliarTableCell.interestedButton.selected = YES;
        }else{
            homeFamiliarTableCell.interestedButton.selected = NO;
        }
        
        homeFamiliarTableCell.accountID = [NSString stringWithFormat:@"%d", (int)[friendDic[@"id"] intValue]];
        return homeFamiliarTableCell;
    }
    
    return nil;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if(tableView == self.favouriteItemTableView)
    {
        return arrayAccounts.count;
    }else if(tableView == self.chengxinlianItemTableView)
    {
        return arrayAccounts.count;
    }
        return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
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
    if(nTestStatus != 2)
        return;
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    vc.selectType = 1;
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onPersonal:(id)sender
{
    if(nTestStatus != 2)
        return;
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    vc.selectType = 0;
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onMyHome:(id)sender
{
    if(nTestStatus != 2)
        return;
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onFriends:(id)sender
{
    if(nTestStatus != 2)
        return;
    FavouritesTableViewController* vc = [[FavouritesTableViewController alloc] initWithNibName:@"FavouritesTableViewController" bundle:nil];
    UIButton *button = (UIButton*) sender;
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
