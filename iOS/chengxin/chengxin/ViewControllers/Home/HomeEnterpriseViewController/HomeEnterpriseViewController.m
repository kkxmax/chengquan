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

@interface HomeEnterpriseViewController ()
{
    NSMutableArray *enterArray;
}
@end

@implementation HomeEnterpriseViewController
@synthesize homeEnterpriseTableView, indicatorView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.homeEnterpriseTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
    
    enterArray = [NSMutableArray array];
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

- (void)getEnterFromServer:cityName EnterKind:(NSString*)enterKind XyleixingIds:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword  {
    indicatorView.hidden = NO;
    [indicatorView startAnimating];
    [enterArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getEnterList" forKey:@"pAct"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:start forKey:@"start"];
    [dicParams setObject:length forKey:@"length"];
    [dicParams setObject:enterKind forKey:@"enterKind"];
    [dicParams setObject:keyword forKey:@"keyword"];

    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETENTERLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *enterList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < enterList.count; i++) {
                    NSDictionary *enterDic = (NSDictionary *)(enterList[i]);
                    [enterArray addObject:enterDic];
                }
                [self.homeEnterpriseTableView reloadData];
                [indicatorView stopAnimating];
            }
        }
    }];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeFamiliarTableViewCell *cell = (HomeFamiliarTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeFamiliarCellIdentifier" forIndexPath:indexPath];
    NSDictionary *enterDic = (NSDictionary *)(enterArray[indexPath.row]);
    NSString *logoImageName = enterDic[@"logo"];
    NSInteger aKind = [enterDic[@"akind"] integerValue];
    if(logoImageName) {
        cell.logoImageView.clipsToBounds = YES;
        cell.logoImageView.layer.cornerRadius = cell.logoImageView.frame.size.width /2;
        cell.logoEmptyRealImageView.hidden = YES;
        cell.logoEmptyEnterpriseLabel.hidden = YES;
        [cell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
        
    }else{
        if (aKind == PERSONAL_KIND) {
            cell.logoEmptyRealImageView.hidden = NO;
            cell.logoEmptyEnterpriseLabel.hidden = YES;
        }else {
            cell.logoEmptyRealImageView.hidden = YES;
            cell.logoEmptyEnterpriseLabel.hidden = NO;
        }
    }
    
    if (aKind == PERSONAL_KIND) {
        [cell.markImageView setImage:[UIImage imageNamed:@"blank_person"]];
        cell.nameLabel.text = enterDic[@"realname"];
        if([enterDic[@"reqCodeSenderRealname"] isEqualToString:@""]) {
            cell.reqViewHeightConstraint.constant = 0.f;
        }else{
            cell.reqViewHeightConstraint.constant = 23.f;
            int nReqCodeSenderAKind = [enterDic[@"reqCodeSenderAkind"] intValue];
            cell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, enterDic[@"reqCodeSenderRealname"]];
        }
    }else {
        [cell.markImageView setImage:[UIImage imageNamed:@"blank_enterprise"]];
        cell.nameLabel.text = enterDic[@"enterName"];
        if([enterDic[@"reqCodeSenderEnterName"] isEqualToString:@""]) {
            cell.reqViewHeightConstraint.constant = 0.f;
        }else{
            cell.reqViewHeightConstraint.constant = 23.f;
            int nReqCodeSenderAKind = [enterDic[@"reqCodeSenderAkind"] intValue];
            cell.reqCodeSenderLabel.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, enterDic[@"reqCodeSenderEnterName"]];
        }
    }
    [cell.nameLabel sizeToFit];
    [cell.xyNameButton setTitle:enterDic[@"xyName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGRect nameLabelFrame = cell.nameLabel.frame;
        CGSize stringSize = [cell.xyNameButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
        [cell.xyNameButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y - 2, stringSize.width, 16)];
    });
    cell.codeLabel.text = enterDic[@"code"];
    NSArray *productsArray = (NSArray *)(enterDic[@"products"]);
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
        cell.productsLabel.text = productNames;
        cell.productViewHeightConstraint.constant = 23.f;
        
    }else{
        cell.productViewHeightConstraint.constant = 0.f;
    }
    NSArray *itemsArray = (NSArray *)(enterDic[@"items"]);
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
        cell.itemLabel.text = itemNames;
        cell.itemViewHeightConstraint.constant = 23.f;
    }else{
        cell.itemViewHeightConstraint.constant = 0.f;
    }
    NSArray *servicessArray = (NSArray *)(enterDic[@"services"]);
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
        cell.serviceLabel.text = serviceNames;
        cell.serviceViewHeightConstraint.constant = 23.f;
    }else{
        cell.serviceViewHeightConstraint.constant = 0.f;
    }
    cell.viewCountLabel.text = [NSString stringWithFormat:@"%ld%%", (long)[enterDic[@"credit"] longValue]];
    cell.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[enterDic[@"electCnt"] longValue]];
    cell.feedbackCountLabel.text = [NSString stringWithFormat:@"%ld", (long)[enterDic[@"feedbackCnt"] longValue]];
    
    if([enterDic[@"interested"] integerValue] == 1) {
        cell.interestedButton.selected = YES;
    }else{
        cell.interestedButton.selected = NO;
    }
    
    cell.accountID = [NSString stringWithFormat:@"%d", (int)[enterDic[@"id"] intValue]];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return enterArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 220.f;
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
    NSInteger aKind = [enterDic[@"akind"] integerValue];
    if (aKind == PERSONAL_KIND) {
        if([enterDic[@"reqCodeSenderRealname"] isEqualToString:@""]) {
            homeTableCellHeight -= 23.f;
        }
    }else{
        if([enterDic[@"reqCodeSenderEnterName"] isEqualToString:@""]) {
            homeTableCellHeight -= 23.f;
        }
    }
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    NSDictionary *enterDic = (NSDictionary *)[enterArray objectAtIndex:indexPath.row];
    [CommonData sharedInstance].selectedFriendAccountID = [NSString stringWithFormat:@"%d", (int)[enterDic[@"id"] intValue]];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEFAMILIARDETAIL_VIEW_NOTIFICATION object:indexPath];
}


@end
