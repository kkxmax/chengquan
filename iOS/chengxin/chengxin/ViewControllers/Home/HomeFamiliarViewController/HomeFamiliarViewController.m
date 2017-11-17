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

@interface HomeFamiliarViewController ()
{
    NSMutableArray *friendArray;
    NSMutableArray *cellHeightArray;
}
@end

@implementation HomeFamiliarViewController
@synthesize indicatorView, currentSortOrderIndex;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
   [self.homeFamiliarTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
    
    friendArray = [NSMutableArray array];
    currentSortOrderIndex = 1;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

#pragma mark - Web api
- (void)getFriendFromServer:cityName aKind:(NSString*)akind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword {
    indicatorView.hidden = NO;
    [indicatorView startAnimating];
    [friendArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getFriendList" forKey:@"pAct"];
    [dicParams setObject:@"" forKey:@"start"];
    [dicParams setObject:@"" forKey:@"length"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:akind forKey:@"akind"];
    [dicParams setObject:xyleixingIds forKey:@"xyleixingIds"];
    [dicParams setObject:keyword forKey:@"keyword"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETFRIENDLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *friendList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < friendList.count; i++) {
                    NSDictionary *friendDic = (NSDictionary *)(friendList[i]);
                    [friendArray addObject:friendDic];
                }
                [self.homeFamiliarTableView reloadData];
                [indicatorView stopAnimating];
            }
        }
    }];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeFamiliarTableViewCell *homeFamiliarTableCell;
    homeFamiliarTableCell = (HomeFamiliarTableViewCell*)[tableView dequeueReusableCellWithIdentifier: @"HomeFamiliarCellIdentifier" forIndexPath:indexPath];
    NSDictionary *friendDic = (NSDictionary *)(friendArray[indexPath.row]);
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

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return friendArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 220.f;
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
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    NSDictionary *friendDic = (NSDictionary *)[friendArray objectAtIndex:indexPath.row];
    [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%d", (int)[friendDic[@"id"] intValue]];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEFAMILIARDETAIL_VIEW_NOTIFICATION object:indexPath];
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
