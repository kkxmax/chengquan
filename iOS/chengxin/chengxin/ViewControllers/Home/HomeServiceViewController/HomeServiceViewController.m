//
//  HomeServiceViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeServiceViewController.h"
#import "HomeServiceTableViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"

@interface HomeServiceViewController ()
{
    NSMutableArray *serviceArray;
}
@end

@implementation HomeServiceViewController
@synthesize homeServiceTableView, indicatorView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    serviceArray = [NSMutableArray array];
    [self.homeServiceTableView registerNib:[UINib nibWithNibName:@"HomeServiceTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeServiceCellIdentifier"];
    currentSortOrderIndex = 1;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
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

- (void)getServiceFromServer:cityName AKind:(NSString*)akind FenleiIds:(NSString*)fenleiIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword {
    indicatorView.hidden = NO;
    [indicatorView startAnimating];
    [serviceArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getServiceList" forKey:@"pAct"];
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
                NSArray *serviceList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < serviceList.count; i++) {
                    NSDictionary *serviceDic = (NSDictionary *)(serviceList[i]);
                    [serviceArray addObject:serviceDic];
                }
                [self.homeServiceTableView reloadData];
                [indicatorView stopAnimating];
            }
        }
    }];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeServiceTableViewCell *homeServiceTableCell = (HomeServiceTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeServiceCellIdentifier" forIndexPath:indexPath];
    NSDictionary *serviceDic = (NSDictionary *)[serviceArray objectAtIndex:indexPath.row];
    NSInteger aKind = [serviceDic[@"akind"] integerValue];
    if (aKind == PERSONAL_KIND) {
        homeServiceTableCell.nameLabel.text = serviceDic[@"realname"];
    }else {
        homeServiceTableCell.nameLabel.text = serviceDic[@"enterName"];
    }
    [homeServiceTableCell.nameLabel sizeToFit];
    [homeServiceTableCell.fenleiButton setTitle:serviceDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGRect nameLabelFrame = homeServiceTableCell.nameLabel.frame;
        CGSize stringSize = [homeServiceTableCell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
        [homeServiceTableCell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
    });
    NSString *logoImageName = serviceDic[@"logo"];
    if(logoImageName) {
        [homeServiceTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
        homeServiceTableCell.noImageLabel.hidden = YES;
    }else{
        homeServiceTableCell.noImageLabel.hidden = NO;
    }
    
    homeServiceTableCell.commentTextView.text = serviceDic[@"comment"];
    return homeServiceTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return serviceArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 155.f;
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    [CommonData sharedInstance].selectedItemServiceDic = (NSDictionary *)(serviceArray[indexPath.row]);
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMESERVICEDETAIL_VIEW_NOTIFICATION object:indexPath];
}

#pragma mark - Action
- (IBAction)addAction:(id)sender {
    [CommonData sharedInstance].addItemServiceIndex = SERVICE_PAGE;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMADD_VIEW_NOTIFICATION object:nil];
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
