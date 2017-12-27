//
//  FavouritesTableViewController.m
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "FavouritesTableViewController.h"
#import "FavouritesTableItemViewCell.h"
#import "Global.h"
#import "MGSwipeButton.h"
#import "UIImageView+WebCache.h"
#import "LPIndexSectionView.h"
#import "AAPullToRefresh.h"

enum {
    SELECT_PERSONAL = 0,
    SELECT_ENTERPRISE = 1,
    SELECT_FRIEND_0 = 2,
    SELECT_FRIEND_1 = 3,
    SELECT_FRIEND_2 = 4,
    SELECT_FRIEND_3 = 5
};

@interface FavouritesTableViewController ()
{
    NSMutableArray *aryInterData;
    NSMutableArray *aryInterName;
    NSMutableArray *aryInterCode;
    
    NSMutableArray *arySortInterCode;
    NSMutableDictionary *dicInterCode;
    NSMutableArray *aryKeyAlpha, *aryKeys;
    
    NSString *preferenceKey;
    LPIndexSectionView* _sectionView;
    
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    NSInteger refreshStartIndex;
}
@end

@implementation FavouritesTableViewController
@synthesize tblInterView;
@synthesize selectType, lblTitle;

- (void)viewDidLoad {
    [super viewDidLoad];

    aryKeys = [NSMutableArray arrayWithArray:@[ @"*", @"A", @"B", @"C", @"D", @"E", @"F", @"G", @"H", @"I", @"J", @"K", @"L", @"M", @"N", @"O", @"P", @"Q", @"R", @"S", @"T", @"U", @"V", @"W", @"X", @"Y", @"Z", @"#" ]];
    self.automaticallyAdjustsScrollViewInsets = NO;
    tblInterView.sectionIndexBackgroundColor = [UIColor clearColor];
    tblInterView.sectionIndexColor = BLACK_COLOR_102;
    [tblInterView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    [tblInterView registerNib:[UINib nibWithNibName:@"FavouritesTableItemViewCell" bundle:nil] forCellReuseIdentifier:@"FavouritesCellIdentifier"];

    aryInterData = [[NSMutableArray alloc] init];
    aryInterName = [[NSMutableArray alloc] init];
    aryInterCode = [[NSMutableArray alloc] init];
    aryKeyAlpha = [[NSMutableArray alloc] init];
    dicInterCode = [[NSMutableDictionary alloc] init];
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [tblInterView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [tblInterView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshStartIndex = 0;

    //aryCandidate = [CommonData sharedInstance].aryFavouriteCandidate;
}

- (void)refreshTopItems {
    refreshStartIndex = 0;
    if(aryInterData)
        [aryInterData removeAllObjects];
    if(aryInterName)
        [aryInterName removeAllObjects];
    if(aryInterCode)
        [aryInterCode removeAllObjects];
    if(arySortInterCode)
        [arySortInterCode removeAllObjects];
    if (selectType == SELECT_PERSONAL) {
        [self getInterestListFromServer:@"1" FriendLevel:@"" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"个人";
    }
    else if (selectType == SELECT_ENTERPRISE)
    {
        [self getInterestListFromServer:@"2" FriendLevel:@"" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"企业";
    }
    else if (selectType == SELECT_FRIEND_0)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"0" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"我的上家";
    }
    else if (selectType == SELECT_FRIEND_1)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"1" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"一度好友";
    }
    else if (selectType == SELECT_FRIEND_2)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"2" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"二度好友。";
    }
    else if (selectType == SELECT_FRIEND_3)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"3" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"三度好友";
    }
    
}

- (void)refreshBottomItems {
    refreshStartIndex = aryInterData.count;
    if (selectType == SELECT_PERSONAL) {
        [self getInterestListFromServer:@"1" FriendLevel:@"" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"个人";
    }
    else if (selectType == SELECT_ENTERPRISE)
    {
        [self getInterestListFromServer:@"2" FriendLevel:@"" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"企业";
    }
    else if (selectType == SELECT_FRIEND_0)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"0" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"我的上家";
    }
    else if (selectType == SELECT_FRIEND_1)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"1" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"1度好友";
    }
    else if (selectType == SELECT_FRIEND_2)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"2" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"2度好友";
    }
    else if (selectType == SELECT_FRIEND_3)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"3" Start:refreshStartIndex Length:REFRESH_GET_DATA_COUNT];
        lblTitle.text = @"3度好友";
    }
}

- (void)initlizerIndexView
{
    _sectionView = [[LPIndexSectionView alloc] initWithFrame:CGRectMake(self.tblInterView.frame.size.width - 25, 64, 30, self.tblInterView.frame.size.height) titles:aryKeys titleHeight:self.tblInterView.frame.size.height / aryKeys.count];
    [self.view addSubview:_sectionView];
    [_sectionView handleSelectTitle:^(NSString *title, NSInteger index) {
        NSUInteger ind = 0;
        if([title isEqualToString:@"#"])
        {
            for(int i = 1; i < aryKeyAlpha.count; i++)
            {
                NSUInteger keyInd = [aryKeys indexOfObject:aryKeyAlpha[i]];
                if(keyInd > aryKeys.count)
                {
                    ind = i;
                    break;
                }
            }
        }else
        {
            ind = [aryKeyAlpha indexOfObject:title];
            if([dicInterCode[@"*"] count] == 0 && ind == 0)
                return;
            if(ind > aryKeyAlpha.count)
                return;
        }
        
        [self.tblInterView scrollToRowAtIndexPath:[NSIndexPath indexPathForItem:0 inSection:ind] atScrollPosition: UITableViewScrollPositionTop animated:YES];
    }];
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

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    self.navigationController.navigationBarHidden = YES;

}

-(void) getInterestListFromServer:(NSString*)akind FriendLevel:(NSString*)friendLevel Start:(NSInteger)start Length:(NSInteger)length {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMyInterestList" forKey:@"pAct"];
    [dicParams setObject:akind forKey:@"akind"];
    [dicParams setObject:friendLevel forKey:@"friendLevel"];
    [dicParams setObject:[NSNumber numberWithInt:start] forKey:@"start"];
    [dicParams setObject:[NSNumber numberWithInt:length] forKey:@"length"];

    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETMYINTERESTLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
        [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *interList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < interList.count; i++) {
                    NSMutableDictionary *interDic = [[NSMutableDictionary alloc] initWithDictionary:(NSDictionary *)(interList[i])];
                    
                    if ([interDic[@"interested"] integerValue] != 1)
                        continue;
                    int akind = [interDic[@"akind"] intValue];
                    NSString *name;
                    if (akind == 1)
                        name = interDic[@"realname"];
                    else if (akind == 2)
                        name = interDic[@"enterName"];
                    if(name.length == 0)
                        name = interDic[@"mobile"];
                    if (name.length > 0)
                    {
                        [aryInterData addObject:interDic];
                        [aryInterName addObject:name];
                        [aryInterCode addObject:[GeneralUtil convertCnToEn:name]];
                    }
                }
                
                [self makeIndexedDictionaryData];
            }
        }
    }];
}

- (void)makeIndexedDictionaryData {
    [aryKeyAlpha removeAllObjects];
    [dicInterCode removeAllObjects];
    if (aryInterCode.count > 0) {
        
        [dicInterCode setValue:[NSMutableArray array] forKey:@"*"];
        
        [aryKeyAlpha insertObject:@"*" atIndex:0];
        
        preferenceKey = [NSString stringWithFormat:@"favourite%d", (int)selectType];
        NSString *data = [GeneralUtil getUserPreference:preferenceKey];
        if (data != nil && data.length > 0) {
            NSMutableArray *aryData = [NSMutableArray arrayWithArray:[data componentsSeparatedByString:@","]];
            for(int i = 0; i < aryData.count; i++)
            {
                NSString* str = aryData[i];
                NSUInteger index = [aryInterCode indexOfObject:str];
                
                if(index > aryInterCode.count)
                {
                    [aryData removeObjectAtIndex:i];
                    i--;
                }
            }
            [dicInterCode setValue:aryData forKey:@"*"];
            if(aryData.count == 0)
                [aryKeyAlpha removeObjectAtIndex:0];
        }else
        {
            [aryKeyAlpha removeObjectAtIndex:0];
        }
        
        arySortInterCode = [NSMutableArray arrayWithArray:[aryInterCode sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
            return [obj1 compare:obj2];
        }]];
        
        for(NSString *str in arySortInterCode)
        {
            NSUInteger index = [dicInterCode[@"*"] indexOfObject:str];
            if(index < [dicInterCode[@"*"] count])
                continue;
            NSString *firstChar = [[str substringToIndex:1] uppercaseString];
           // NSString *firstChar = [str substringToIndex:1] ;
            if(![aryKeyAlpha containsObject:firstChar])
            {
                NSMutableArray *charArray =  [[NSMutableArray alloc] init];
                [charArray addObject:str];
                [aryKeyAlpha addObject:firstChar];
                [dicInterCode setValue:charArray forKey:firstChar];
            }
            else
            {
                NSMutableArray *prevArray = (NSMutableArray *)[dicInterCode valueForKey:firstChar];
                [prevArray addObject:str];
                [dicInterCode setValue:prevArray forKey:firstChar];
            }
        }
        
        
    }
    [self initlizerIndexView];
    [tblInterView reloadData];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSDictionary*) entryForSection:(NSInteger) section {
    if (aryKeyAlpha.count == 0)
        return nil;
    /*
    if (section == 0) {
        NSArray *objectsInSection = dicInterCode[@"*"];
        return @{@"*" : objectsInSection};
    }
    */
    NSString *sectionTitle = aryKeyAlpha[section];
    NSArray *objectsInSection = dicInterCode[sectionTitle];
    return @{sectionTitle : objectsInSection};
}

- (NSArray *)objectsForSection:(NSInteger)section {
    NSDictionary *entry = [self entryForSection:section];
    if (entry == nil)
        return nil;
    NSArray *objects = (NSArray*)entry.allValues.firstObject;
    return objects;
}
#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return aryKeyAlpha.count;
}
/*
- (NSArray<NSString *> *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    NSMutableArray *ary = [NSMutableArray arrayWithArray:(NSArray*)aryKeyAlpha];
    if (ary.count > 0)
        [ary removeObjectAtIndex:0];
    return ary;
}
*/
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 40.0f;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    NSArray *array = [self objectsForSection:section];
    if (array == nil) {
        return 0;
    }
    return array.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    static NSString *simpleTableIdentifier = @"FavouritesCellIdentifier";
    FavouritesTableItemViewCell *cell = (FavouritesTableItemViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    if(objectsForSection.count <= indexPath.row)
        return cell;
    NSString *title = objectsForSection[indexPath.row];
    NSInteger index = [aryInterCode indexOfObject:title];
    NSDictionary *dic = [aryInterData objectAtIndex:index];
    
    NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"logo"]];
    [cell.photo sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
    cell.name.text = aryInterName[index];
    cell.tag = index;
    cell.delegate = self;
    cell.allowsMultipleSwipe = NO;
    
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    NSString *title = objectsForSection[indexPath.row];
    NSInteger index = [aryInterCode indexOfObject:title];
    NSDictionary *friendDic = [aryInterData objectAtIndex:index];
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
-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {

    //return [AlphabetsArray objectAtIndex:section];

    if([[aryKeyAlpha objectAtIndex:0] isEqualToString:@"*"])
    {
        if(section == 0)
        {
            if(selectType == SELECT_PERSONAL)
                return @"星标好友";
            else if(selectType == SELECT_ENTERPRISE)
                return @"星标企业";
            else
                return @"星标企业";
        }
    }
    
    
    NSDictionary *entry  = [self entryForSection:section];
    if (entry == nil)
        return @"";

    NSString *title = entry.allKeys.firstObject;
    return title;

}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 30;
}
-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0;
}
/*
- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    
}
 */
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

- (NSArray *)swipeTableCell:(MGSwipeTableCell *) cell swipeButtonsForDirection:(MGSwipeDirection)direction
              swipeSettings:(MGSwipeSettings *) swipeSettings expansionSettings:(MGSwipeExpansionSettings *) expansionSettings; {
    swipeSettings.transition = MGSwipeTransitionStatic;
    
    if (direction == MGSwipeDirectionRightToLeft) {
        expansionSettings.buttonIndex = -1;
        expansionSettings.fillOnTrigger = NO;
        //return [NSMutableArray array];
        return [self createRightButtons:cell.tag];
    } else {
        expansionSettings.buttonIndex = -1;
        expansionSettings.fillOnTrigger = NO;
        return [NSMutableArray array];
    }
}

- (BOOL)swipeTableCell:(MGSwipeTableCell*) cell tappedButtonAtIndex:(NSInteger) index direction:(MGSwipeDirection)direction fromExpansion:(BOOL) fromExpansion
{
    NSLog(@"Delegate: button tapped, %@ position, index %d, from Expansion: %@",
          direction == MGSwipeDirectionLeftToRight ? @"left" : @"right", (int)index, fromExpansion ? @"YES" : @"NO");
    
    if (direction == MGSwipeDirectionRightToLeft && index == 0) {
        return YES;
    }
    
    return YES;
}

- (void)onClickStartSetButton:(UIButton*) button {

    NSString *code = [aryInterCode objectAtIndex:button.tag];
    NSString *key = [[code substringToIndex:1] uppercaseString];
    NSMutableArray *aryCandidate = [dicInterCode objectForKey:@"*"];
    
    NSMutableArray *aryData = [dicInterCode objectForKey:key];
    
    if ([aryCandidate containsObject:code])
    {
        if(aryData == nil)
        {
            NSMutableArray* newArray = [[NSMutableArray alloc] init];
            [newArray addObject:code];
            [dicInterCode setValue:newArray forKey:key];
        }else
        {
            [aryData addObject:code];
        }
        [aryCandidate removeObjectAtIndex:[aryCandidate indexOfObject:code]];
        [self refreshStars];
        [self makeIndexedDictionaryData];
        [tblInterView reloadData];
    }else
    {
        if ( aryData.count > 0 ) {
            NSString *strCandidate;
            for (int i = 0; i < aryData.count; i++) {
                NSString *cName = aryData[i];
                if ([cName isEqualToString:code]) {
                    strCandidate = cName;
                    [aryData removeObjectAtIndex:i];
                    break;
                }
            }
            
            [aryCandidate addObject:strCandidate];
            [dicInterCode setValue:aryCandidate forKey:@"*"];
            if(aryCandidate.count > 0)
            {
                if([aryKeyAlpha[0] isEqualToString:@"*"] == false)
                    [aryKeyAlpha insertObject:@"*" atIndex:0];
            }
            
            if ( aryData.count == 0 ) {
                [dicInterCode removeObjectForKey:key];
                [aryKeyAlpha removeObject:key];
            }
            else
                [dicInterCode setValue:aryData forKey:key];
            
            [tblInterView reloadData];
            [self refreshStars];
        }
    }
    
}
-(void)refreshStars
{
    NSMutableArray *aryStartData = [dicInterCode valueForKey:@"*"];
    
    if (aryStartData.count > 0) {
        NSString *strData;
        for (int i = 0; i < aryStartData.count; i++) {
            if (i == 0)
                strData = aryStartData[i];
            else
                strData = [NSString stringWithFormat:@"%@,%@", strData, aryStartData[i]];
        }
        [GeneralUtil setUserPreference:preferenceKey value:strData];
    }else
    {
        [GeneralUtil setUserPreference:preferenceKey value:@""];
    }
}
- (void)onClickFavouriteButton:(UIButton*) button {
    
    NSDictionary *dic = aryInterData[button.tag];
    NSString* interCode = aryInterCode[button.tag];
 
    for(int i = 0; i < [dicInterCode[@"*"] count]; i++)
    {
        NSString* str = dicInterCode[@"*"][i];
        if([str isEqualToString:interCode] == YES)
        {
            [dicInterCode[@"*"] removeObjectAtIndex:i];
            i--;
        }
    }
    [self refreshStars];
    [self setInterestedToServer:button.tag AccountId:dic[@"id"] Value:@"0"];

}

- (void)setInterestedToServer:(NSInteger)index AccountId:(NSString*)accountId Value:(NSString*) val {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setInterest" forKey:@"pAct"];
    [dicParams setObject:accountId forKey:@"accountId"];
    [dicParams setObject:val forKey:@"val"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETINTEREST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [aryInterData removeObjectAtIndex:index];
                [aryInterName removeObjectAtIndex:index];
                [aryInterCode removeObjectAtIndex:index];
                
                [self makeIndexedDictionaryData];
                
            }else{
                
            }
        }
    }];
    
}

- (NSArray *)createRightButtons:(int)number
{
    NSString *code = [aryInterCode objectAtIndex:number];
    NSMutableArray *aryData = dicInterCode[@"*"];
    
    NSMutableArray * result = [NSMutableArray array];
    
    UIButton *btnFavourite = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 120.f, 55.f)];
    btnFavourite.tag = number;
    [btnFavourite setBackgroundColor:BLACK_COLOR_245];
    [btnFavourite setTitle:@"取消关注" forState:UIControlStateNormal];
    [btnFavourite setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
    [btnFavourite.titleLabel setFont:FONT_14];
    [btnFavourite addTarget:self action:@selector(onClickFavouriteButton:) forControlEvents:UIControlEventTouchUpInside];
    [result addObject:btnFavourite];

    
   
    UIButton *btnStartSet = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 120.f, 55.f)];
    
    if ([aryData containsObject:code]) {
        btnStartSet.tag = number;
        [btnStartSet setBackgroundImage:[UIImage imageNamed:@"orange_button_bg"] forState:UIControlStateNormal];
        [btnStartSet setTitle:@"取消星标关注" forState:UIControlStateNormal];
        [btnStartSet setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
        [btnStartSet.titleLabel setFont:FONT_14];
        [btnStartSet addTarget:self action:@selector(onClickStartSetButton:) forControlEvents:UIControlEventTouchUpInside];
    }else
    {
        btnStartSet.tag = number;
        [btnStartSet setBackgroundImage:[UIImage imageNamed:@"orange_button_bg"] forState:UIControlStateNormal];
        [btnStartSet setTitle:@"设为星标关注" forState:UIControlStateNormal];
        [btnStartSet setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
        [btnStartSet.titleLabel setFont:FONT_14];
        [btnStartSet addTarget:self action:@selector(onClickStartSetButton:) forControlEvents:UIControlEventTouchUpInside];
    }
    
   
    
    [result addObject:btnStartSet];
                                 
    return result;
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
    NSMutableArray *aryStartData = [dicInterCode valueForKey:@"*"];
    
    if (aryStartData.count > 0) {
        NSString *strData;
        for (int i = 0; i < aryStartData.count; i++) {
            if (i == 0)
                strData = aryStartData[i];
            else
                strData = [NSString stringWithFormat:@"%@,%@", strData, aryStartData[i]];
        }
        [GeneralUtil setUserPreference:preferenceKey value:strData];
    }
}
@end
