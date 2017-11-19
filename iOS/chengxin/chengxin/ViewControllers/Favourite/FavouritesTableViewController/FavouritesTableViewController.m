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
    NSMutableArray *aryKeyAlpha;
    
    NSString *preferenceKey;

}
@end

@implementation FavouritesTableViewController
@synthesize tblInterView;
@synthesize selectType, lblTitle;

- (void)viewDidLoad {
    [super viewDidLoad];
        
    self.automaticallyAdjustsScrollViewInsets = NO;
    tblInterView.sectionIndexBackgroundColor = [UIColor clearColor];
    tblInterView.sectionIndexColor = BLACK_COLOR_102;
    [tblInterView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    aryInterData = [[NSMutableArray alloc] init];
    aryInterName = [[NSMutableArray alloc] init];
    aryInterCode = [[NSMutableArray alloc] init];
    aryKeyAlpha = [[NSMutableArray alloc] init];
    dicInterCode = [[NSMutableDictionary alloc] init];
    
    //aryCandidate = [CommonData sharedInstance].aryFavouriteCandidate;
    
    if (selectType == SELECT_PERSONAL) {
        [self getInterestListFromServer:@"1" FriendLevel:@""];
        lblTitle.text = @"个人";
    }
    else if (selectType == SELECT_ENTERPRISE)
    {
        [self getInterestListFromServer:@"2" FriendLevel:@""];
        lblTitle.text = @"企业";
    }
    else if (selectType == SELECT_FRIEND_0)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"0"];
        lblTitle.text = @"我的上家";
    }
    else if (selectType == SELECT_FRIEND_1)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"1"];
        lblTitle.text = @"1度好友";
    }
    else if (selectType == SELECT_FRIEND_2)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"2"];
        lblTitle.text = @"2度好友";
    }
    else if (selectType == SELECT_FRIEND_3)
    {
        [self getInterestListFromServer:@"" FriendLevel:@"3"];
        lblTitle.text = @"3度好友";
    }
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    self.navigationController.navigationBarHidden = YES;

}

-(void) getInterestListFromServer:(NSString*)akind FriendLevel:(NSString*)friendLevel {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMyInterestList" forKey:@"pAct"];
    [dicParams setObject:akind forKey:@"akind"];
    [dicParams setObject:friendLevel forKey:@"friendlevel"];

    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETMYINTERESTLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *interList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < interList.count; i++) {
                    NSMutableDictionary *interDic = [[NSMutableDictionary alloc] initWithDictionary:(NSDictionary *)(interList[i])];
                    
                    if ([interDic[@"interested"] integerValue] != 1)
                        continue;
                    
                    NSString *name;
                    if (selectType == SELECT_PERSONAL)
                        name = interDic[@"realname"];
                    else if (selectType == SELECT_ENTERPRISE)
                        name = interDic[@"entername"];
                    else
                        name = interDic[@"realname"];
                    
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
    if (aryInterCode.count > 0) {
        
        [aryKeyAlpha removeAllObjects];
        [dicInterCode removeAllObjects];
        
        arySortInterCode = [NSMutableArray arrayWithArray:[aryInterCode sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
            return [obj1 compare:obj2];
        }]];
        
        for(NSString *str in arySortInterCode)
        {
            NSString *firstChar = [str substringToIndex:1];
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
        
        [dicInterCode setValue:[NSMutableArray array] forKey:@"*"];
        
        [aryKeyAlpha insertObject:@"*" atIndex:0];
        
        preferenceKey = [NSString stringWithFormat:@"favourite%d", (int)selectType];
        NSString *data = [GeneralUtil getUserPreference:preferenceKey];
        if (data != nil && data.length > 0) {
            NSMutableArray *aryData = [NSMutableArray arrayWithArray:[data componentsSeparatedByString:@","]];
            [dicInterCode setValue:aryData forKey:@"*"];
        }
    }
    
    [tblInterView reloadData];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSDictionary*) entryForSection:(NSInteger) section {
    if (aryKeyAlpha.count == 0)
        return nil;
    
    if (section == 0) {
        NSArray *objectsInSection = dicInterCode[@"*"];
        return @{@"*" : objectsInSection};
    }
    
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

- (NSArray<NSString *> *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    NSMutableArray *ary = [NSMutableArray arrayWithArray:(NSArray*)aryKeyAlpha];
    if (ary.count > 0)
        [ary removeObjectAtIndex:0];
    return ary;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 56.f;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    NSArray *array = [self objectsForSection:section];
    if (array == nil) {
        return 0;
    }
    return array.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    NSString *title = objectsForSection[indexPath.row];
    NSInteger index = [aryInterCode indexOfObject:title];
    NSDictionary *dic = [aryInterData objectAtIndex:index];
    
    static NSString *simpleTableIdentifier = @"FavouritesTableItemViewCell";
    FavouritesTableItemViewCell *cell = (FavouritesTableItemViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"FavouritesTableItemViewCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"logo"]];
    [cell.photo sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
    cell.name.text = aryInterName[index];
    cell.tag = index;
    cell.delegate = self;
    cell.allowsMultipleSwipe = NO;
    
    return cell;
}

-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {

    //return [AlphabetsArray objectAtIndex:section];

    if(section == 0)
        return @"星表企业";
    
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
    NSString *key = [code substringToIndex:1];
    NSMutableArray *aryCandidate = [dicInterCode objectForKey:@"*"];
    
    NSMutableArray *aryData = [dicInterCode objectForKey:key];
    
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
        
        if ( aryData.count == 0 ) {
            [dicInterCode removeObjectForKey:key];
            [aryKeyAlpha removeObject:key];
        }
        else
            [dicInterCode setValue:aryData forKey:key];
        
        [tblInterView reloadData];
    }
}

- (void)onClickFavouriteButton:(UIButton*) button {
    
    NSDictionary *dic = aryInterData[button.tag];
    
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

    if ([aryData containsObject:code]) {
        return result;
    }
   
    UIButton *btnStartSet = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 120.f, 55.f)];
    btnStartSet.tag = number;
    [btnStartSet setBackgroundImage:[UIImage imageNamed:@"orange_button_bg"] forState:UIControlStateNormal];
    [btnStartSet setTitle:@"设为星标关注" forState:UIControlStateNormal];
    [btnStartSet setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
    [btnStartSet.titleLabel setFont:FONT_14];
    [btnStartSet addTarget:self action:@selector(onClickStartSetButton:) forControlEvents:UIControlEventTouchUpInside];
    
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
