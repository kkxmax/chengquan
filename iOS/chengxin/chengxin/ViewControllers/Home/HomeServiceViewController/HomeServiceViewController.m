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
#import "AAPullToRefresh.h"

@interface HomeServiceViewController ()
{
    NSMutableArray *serviceArray;
    NSString *mCityName;
    NSString *mAKind;
    NSString *mFenleiIds;
    NSString *mStart;
    NSString *mLength;
    NSString *mKeyword;
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;

}
@end

@implementation HomeServiceViewController
@synthesize homeServiceTableView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    serviceArray = [NSMutableArray array];
    [self.homeServiceTableView registerNib:[UINib nibWithNibName:@"HomeServiceTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeServiceCellIdentifier"];
    // NSNotification for Reload Changed Data
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadChangedData:) name:RELOAD_SERVICE_DATA_NOTIFICATION object:nil];

    currentSortOrderIndex = 1;
    mCityName = @"";
    mAKind = @"";
    mFenleiIds = @"";
    mStart = @"";
    mLength = @"";
    mKeyword = @"";
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.homeServiceTableView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.homeServiceTableView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;
    
    [self.homeServiceTableView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(object == self.homeServiceTableView) {
        self.homeServiceTableView.frame = CGRectMake(self.homeServiceTableView.frame.origin.x, self.homeServiceTableView.frame.origin.y, self.homeServiceTableView.frame.size.width, self.homeServiceTableView.contentSize.height);
        self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.homeServiceTableView.frame.size.height);
        if(baseDelegate)
            [baseDelegate finishedLoadingData:4];
    }
}

- (void)dealloc
{
    [self.homeServiceTableView removeObserver:self forKeyPath:@"contentSize" context:NULL];
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

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    topRefreshView.showPullToRefresh = NO;
    bottomRefreshView.showPullToRefresh = NO;
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(serviceArray)
        [serviceArray removeAllObjects];
    [self getServiceFromServer:[CommonData sharedInstance].choiceServiceCity
                         AKind:[CommonData sharedInstance].choiceServiceKind
                     FenleiIds:[CommonData sharedInstance].choiceServiceIds
                         Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                        Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                       Keyword:[CommonData sharedInstance].searchServiceText];
}

- (void)refreshBottomItems {
    refreshStartIndex = serviceArray.count;
    [self getServiceFromServer:[CommonData sharedInstance].choiceServiceCity
                         AKind:[CommonData sharedInstance].choiceServiceKind
                     FenleiIds:[CommonData sharedInstance].choiceServiceIds
                         Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                        Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                       Keyword:[CommonData sharedInstance].searchServiceText];
}

- (void)getServiceFromServer:cityName AKind:(NSString*)akind FenleiIds:(NSString*)fenleiIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword {
    [GeneralUtil showProgress];
    mCityName = cityName;
    mAKind = akind;
    mFenleiIds = fenleiIds;
    mStart = start;
    mLength = length;
    mKeyword = keyword;
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
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *serviceList = (NSArray *)(dicRes[@"data"]);
//                [serviceArray removeAllObjects];
                for(int i = 0; i < serviceList.count; i++) {
                    NSDictionary *serviceDic = (NSDictionary *)(serviceList[i]);
                    [serviceArray addObject:serviceDic];
                }
                [self.homeServiceTableView reloadData];
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
    HomeServiceTableViewCell *homeServiceTableCell = (HomeServiceTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeServiceCellIdentifier" forIndexPath:indexPath];
    if(serviceArray.count <= indexPath.row)
        return homeServiceTableCell;

    NSDictionary *serviceDic = (NSDictionary *)[serviceArray objectAtIndex:indexPath.row];
    homeServiceTableCell.nameLabel.text = serviceDic[@"name"];

    [homeServiceTableCell.nameLabel sizeToFit];
    [homeServiceTableCell.fenleiButton setTitle:serviceDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGSize stringSize = [homeServiceTableCell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
        int bw = stringSize.width + 12;
        if(homeServiceTableCell.nameLabel.frame.size.width > (tableView.frame.size.width - 12 - bw)) {
            [homeServiceTableCell.nameLabel setFrame:CGRectMake(homeServiceTableCell.nameLabel.frame.origin.x, homeServiceTableCell.nameLabel.frame.origin.y, tableView.frame.size.width - 12 - bw - homeServiceTableCell.nameLabel.frame.origin.x, homeServiceTableCell.nameLabel.frame.size.height)];
        }
        CGRect nameLabelFrame = homeServiceTableCell.nameLabel.frame;
        [homeServiceTableCell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y + 2, bw, 16)];
    });
    NSString *logoImageName = serviceDic[@"logo"];
    [homeServiceTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_item.png"]];
    
    //[homeServiceTableCell.commentTextView setText:[NSString stringWithFormat:@"简介：%@", serviceDic[@"comment"]]];
    NSAttributedString* str = homeServiceTableCell.commentTextView.attributedText;
    NSDictionary* attribute = [str attributesAtIndex:0 effectiveRange:nil];
    [homeServiceTableCell.commentTextView setAttributedText:[[NSAttributedString alloc] initWithString:[NSString stringWithFormat:@"简介：%@", serviceDic[@"comment"]] attributes:attribute ]];
    return homeServiceTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return serviceArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 120.f;
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [CommonData sharedInstance].selectedItemServiceDic = (NSDictionary *)(serviceArray[indexPath.row]);
    [CommonData sharedInstance].detailItemServiceIndex = SUB_HOME_SERVICE;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMESERVICEDETAIL_VIEW_NOTIFICATION object:indexPath];
}

#pragma mark - Action
- (void)addAction {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
    } else {
        [CommonData sharedInstance].addItemServiceIndex = SERVICE_PAGE;
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMADD_VIEW_NOTIFICATION object:nil];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
}

- (void)reloadChangedData:(NSNotification *)notification {
//    [self getServiceFromServer:[CommonData sharedInstance].choiceServiceCity
//                                  AKind:[CommonData sharedInstance].choiceServiceKind
//                              FenleiIds:[CommonData sharedInstance].choiceServiceIds
//                                  Start:@""
//                                 Length:@""
//                                Keyword:[CommonData sharedInstance].searchServiceText];
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
