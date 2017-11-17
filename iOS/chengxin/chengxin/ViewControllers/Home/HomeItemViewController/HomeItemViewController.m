//
//  HomeItemViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemViewController.h"
#import "HomeItemTableViewCell.h"
#import "HomeItemDetailViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"

@interface HomeItemViewController ()
{
    NSMutableArray *itemArray;
}
@end

@implementation HomeItemViewController
@synthesize homeItemTableView, indicatorView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    itemArray = [NSMutableArray array];
    [self.homeItemTableView registerNib:[UINib nibWithNibName:@"HomeItemTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeItemCellIdentifier"];
    currentSortOrderIndex = 1;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.hidden = YES;
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        self.addButton.hidden = YES;
    }else{
        self.addButton.hidden = NO;
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)getItemFromServer:cityName AKind:(NSString*)akind FenleiIds:(NSString*)fenleiIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword {
    indicatorView.hidden = NO;
    [indicatorView startAnimating];
    [itemArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getItemList" forKey:@"pAct"];
    [dicParams setObject:start forKey:@"start"];
    [dicParams setObject:length forKey:@"length"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:akind forKey:@"akind"];
    [dicParams setObject:fenleiIds forKey:@"fenleiIds"];
    [dicParams setObject:keyword forKey:@"keyword"];

    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETITEMLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *itemList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < itemList.count; i++) {
                    NSDictionary *itemDic = (NSDictionary *)(itemList[i]);
                    [itemArray addObject:itemDic];
                }
                [self.homeItemTableView reloadData];
                [indicatorView stopAnimating];
            }
        }
    }];
}
#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeItemTableViewCell *homeItemTableCell = (HomeItemTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeItemCellIdentifier" forIndexPath:indexPath];
    NSDictionary *itemDic = (NSDictionary *)[itemArray objectAtIndex:indexPath.row];
    NSInteger aKind = [itemDic[@"akind"] integerValue];
    if (aKind == PERSONAL_KIND) {
        homeItemTableCell.nameLabel.text = itemDic[@"realname"];
    }else {
        homeItemTableCell.nameLabel.text = itemDic[@"enterName"];
    }
    [homeItemTableCell.nameLabel sizeToFit];
    [homeItemTableCell.fenleiButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGRect nameLabelFrame = homeItemTableCell.nameLabel.frame;
        CGSize stringSize = [homeItemTableCell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
        [homeItemTableCell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
    });
    NSString *logoImageName = itemDic[@"logo"];
    if(logoImageName) {
        [homeItemTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
        homeItemTableCell.noImageLabel.hidden = YES;
    }else{
        homeItemTableCell.noImageLabel.hidden = NO;
    }
    
    homeItemTableCell.commentTextView.text = itemDic[@"comment"];
    return homeItemTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return itemArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 155.f;
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    [CommonData sharedInstance].selectedItemServiceDic = (NSDictionary *)(itemArray[indexPath.row]);
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION object:indexPath];
}

#pragma mark - Action
- (IBAction)addAction:(id)sender {
    [CommonData sharedInstance].addItemServiceIndex = ITEM_PAGE;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMADD_VIEW_NOTIFICATION object:nil];
}
@end
