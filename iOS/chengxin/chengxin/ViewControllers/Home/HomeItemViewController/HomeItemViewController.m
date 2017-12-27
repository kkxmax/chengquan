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
#import "AAPullToRefresh.h"

@interface HomeItemViewController ()
{
    NSMutableArray *itemArray;
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

@implementation HomeItemViewController
@synthesize homeItemTableView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    itemArray = [NSMutableArray array];
    [self.homeItemTableView registerNib:[UINib nibWithNibName:@"HomeItemTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeItemCellIdentifier"];
    
    // NSNotification for Reload Changed Data
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadChangedData:) name:RELOAD_ITEM_DATA_NOTIFICATION object:nil];

    currentSortOrderIndex = 1;
    mCityName = @"";
    mAKind = @"";
    mFenleiIds = @"";
    mStart = @"";
    mLength = @"";
    mKeyword = @"";
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.homeItemTableView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.homeItemTableView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;
    
    [self.homeItemTableView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionOld context:NULL];

}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary  *)change context:(void *)context
{
    // You will get here when the reloadData finished
    if(baseDelegate && [keyPath isEqualToString:@"contentSize"]) {
        self.homeItemTableView.frame = CGRectMake(self.homeItemTableView.frame.origin.x, self.homeItemTableView.frame.origin.y, self.homeItemTableView.frame.size.width, self.homeItemTableView.contentSize.height);
        self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.homeItemTableView.frame.size.height);
        [baseDelegate finishedLoadingData:3];
    }
    
}

- (void)dealloc
{
    [self.homeItemTableView removeObserver:self forKeyPath:@"contentSize" context:NULL];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.hidden = YES;
    [self refreshTopItems];
    topRefreshView.showPullToRefresh = YES;
    bottomRefreshView.showPullToRefresh = YES;
//    [self getItemFromServer:mCityName AKind:mAKind FenleiIds:mFenleiIds Start:mStart Length:mLength Keyword:mKeyword];
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
    if(itemArray)
        [itemArray removeAllObjects];
    [self getItemFromServer:[CommonData sharedInstance].choiceItemCity
                            AKind:[CommonData sharedInstance].choiceItemKind
                        FenleiIds:[CommonData sharedInstance].choiceItemIds
                            Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                           Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                          Keyword:[CommonData sharedInstance].searchItemText];
}

- (void)refreshBottomItems {
    refreshStartIndex = itemArray.count;
    [self getItemFromServer:[CommonData sharedInstance].choiceItemCity
                      AKind:[CommonData sharedInstance].choiceItemKind
                  FenleiIds:[CommonData sharedInstance].choiceItemIds
                      Start:[NSString stringWithFormat:@"%d", refreshStartIndex]
                     Length:[NSString stringWithFormat:@"%d", REFRESH_GET_DATA_COUNT]
                    Keyword:[CommonData sharedInstance].searchItemText];
}


- (void)getItemFromServer:cityName AKind:(NSString*)akind FenleiIds:(NSString*)fenleiIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword {
    [GeneralUtil showProgress];
    mCityName = cityName;
    mAKind = akind;
    mFenleiIds = fenleiIds;
    mStart = start;
    mLength = length;
    mKeyword = keyword;
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
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *itemList = (NSArray *)(dicRes[@"data"]);
//                [itemArray removeAllObjects];
                for(int i = 0; i < itemList.count; i++) {
                    NSDictionary *itemDic = (NSDictionary *)(itemList[i]);
                    [itemArray addObject:itemDic];
                }
                [self.homeItemTableView reloadData];
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
    HomeItemTableViewCell *homeItemTableCell = (HomeItemTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeItemCellIdentifier" forIndexPath:indexPath];
    if(itemArray.count <= indexPath.row)
        return homeItemTableCell;

    NSDictionary *itemDic = (NSDictionary *)[itemArray objectAtIndex:indexPath.row];
    homeItemTableCell.nameLabel.text = itemDic[@"name"];
    [homeItemTableCell.nameLabel sizeToFit];
    [homeItemTableCell.fenleiButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGSize stringSize = [homeItemTableCell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0]}];
        int bw = stringSize.width + 12;
        if(homeItemTableCell.nameLabel.frame.size.width > (tableView.frame.size.width - 12 - bw)) {
            [homeItemTableCell.nameLabel setFrame:CGRectMake(homeItemTableCell.nameLabel.frame.origin.x, homeItemTableCell.nameLabel.frame.origin.y, tableView.frame.size.width - 12 - bw - homeItemTableCell.nameLabel.frame.origin.x, homeItemTableCell.nameLabel.frame.size.height)];
        }
        CGRect nameLabelFrame = homeItemTableCell.nameLabel.frame;
        [homeItemTableCell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 6, nameLabelFrame.origin.y + 2, bw, 16)];
    });
    NSString *logoImageName = itemDic[@"logo"];
    [homeItemTableCell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]] placeholderImage:[UIImage imageNamed:@"no_image_item.png"]];
    
    //homeItemTableCell.commentTextView.text = [NSString stringWithFormat:@"简介：%@", itemDic[@"comment"]];
    
    NSAttributedString* str = homeItemTableCell.commentTextView.attributedText;
    NSDictionary* attribute = [str attributesAtIndex:0 effectiveRange:nil];
    [homeItemTableCell.commentTextView setAttributedText:[[NSAttributedString alloc] initWithString:[NSString stringWithFormat:@"简介：%@", itemDic[@"comment"]] attributes:attribute ]];
    
    return homeItemTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return itemArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 120;
    return homeTableCellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [CommonData sharedInstance].selectedItemServiceDic = (NSDictionary *)(itemArray[indexPath.row]);
    [CommonData sharedInstance].detailItemServiceIndex = SUB_HOME_ITEM;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION object:indexPath];
}

#pragma mark - Action
- (void)addAction {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
    } else {
        [CommonData sharedInstance].addItemServiceIndex = ITEM_PAGE;
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEITEMADD_VIEW_NOTIFICATION object:nil];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
}

- (void)reloadChangedData:(NSNotification *)notification {
//    [self getItemFromServer:[CommonData sharedInstance].choiceItemCity
//                            AKind:[CommonData sharedInstance].choiceItemKind
//                        FenleiIds:[CommonData sharedInstance].choiceItemIds
//                            Start:@""
//                           Length:@""
//                          Keyword:[CommonData sharedInstance].searchItemText];
}

@end
