//
//  HomeChoiceAllCityViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceCityTableViewCell.h"
#import "LPIndexSectionView.h"
#import "Global.h"

@interface HomeChoiceAllCityViewController ()
{
    NSMutableArray *aryCityData;
    NSMutableArray *aryCityName;
    NSMutableArray *aryCityCode;

    NSMutableArray *arySortCityCode;
    NSMutableDictionary *dicCityCode;
    NSMutableArray *aryKeyAlpha;
    LPIndexSectionView *_sectionView;
}
@end

@implementation HomeChoiceAllCityViewController
@synthesize choiceCityTableView;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [choiceCityTableView registerNib:[UINib nibWithNibName:@"HomeChoiceCityTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeChoiceCityCellIdentifier"];
    
    aryCityData = [[NSMutableArray alloc] init];
    aryCityName = [[NSMutableArray alloc] init];
    aryCityCode = [[NSMutableArray alloc] init];
    aryKeyAlpha = [[NSMutableArray alloc] init];
    dicCityCode = [[NSMutableDictionary alloc] init];
    
    self.homeIndex = -1;
    [self getCityListFromServer];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)initlizerIndexView
{
    _sectionView = [[LPIndexSectionView alloc] initWithFrame:CGRectMake(self.view.frame.size.width - 35, 20, 30, self.view.frame.size.height - 50) titles:aryKeyAlpha titleHeight:(self.view.frame.size.height - 100) / aryKeyAlpha.count];
    [self.view addSubview:_sectionView];
    [_sectionView handleSelectTitle:^(NSString *title, NSInteger index) {
        
        [self.choiceCityTableView scrollToRowAtIndexPath:[NSIndexPath indexPathForItem:0 inSection:index] atScrollPosition: UITableViewScrollPositionTop animated:YES];
    }];
}
-(void) getCityListFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getCityList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCITYLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *cityList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < cityList.count; i++) {
                    NSDictionary *provDic = (NSDictionary *)(cityList[i]);
                    for(int j = 0; j < [provDic[@"cities"] count]; j++)
                    {
                        NSDictionary* cityDic = [provDic[@"cities"] objectAtIndex:j];
                        [aryCityData addObject:cityDic];
                        [aryCityName addObject:cityDic[@"name"]];
                        [aryCityCode addObject:cityDic[@"pinyin"]];
                    }
                    
                }
                
                if (aryCityCode.count > 0) {
                    arySortCityCode = [NSMutableArray arrayWithArray:[aryCityCode sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
                        return [obj1 compare:obj2];
                    }]];
                    
                    for(NSString *str in arySortCityCode)
                    {
                        NSString *firstChar = [[str substringToIndex:1] uppercaseString];
                        if(![aryKeyAlpha containsObject:firstChar])
                        {
                            NSMutableArray *charArray =  [[NSMutableArray alloc] init];
                            [charArray addObject:str];
                            [aryKeyAlpha addObject:firstChar];
                            [dicCityCode setValue:charArray forKey:firstChar];
                        }
                        else
                        {
                            NSMutableArray *prevArray = (NSMutableArray *)[dicCityCode valueForKey:firstChar];
                            [prevArray addObject:str];
                            [dicCityCode setValue:prevArray forKey:firstChar];
                        }
                    }
                    [self initlizerIndexView];
                    [choiceCityTableView reloadData];
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
    }];
}

#pragma mark - IBAction
- (IBAction)cancelAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)selectAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    NSString *title = objectsForSection[indexPath.row];
    NSString *cityName = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
    HomeChoiceCityTableViewCell *homeChoiceCityTableCell = (HomeChoiceCityTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeChoiceCityCellIdentifier" forIndexPath:indexPath];
    [homeChoiceCityTableCell.cityNameLabel setText:cityName];
    NSString *strChoiceCity = @"";
    switch (self.homeIndex) {
        case -1:
            strChoiceCity = [CommonData sharedInstance].choiceCity;
            break;
        case SUB_HOME_PERSONAL:
            strChoiceCity = [CommonData sharedInstance].choiceFamiliarCity;
            break;
        case SUB_HOME_ENTERPRISE:
            strChoiceCity = [CommonData sharedInstance].choiceEnterpriseCity;
            break;
        case SUB_HOME_COMMERCE:
            strChoiceCity = [CommonData sharedInstance].choiceCommerceCity;
            break;
        case SUB_HOME_ITEM:
            strChoiceCity = [CommonData sharedInstance].choiceItemCity;
            break;
        case SUB_HOME_SERVICE:
            strChoiceCity = [CommonData sharedInstance].choiceServiceCity;
            break;
            
        default:
            break;
    }
    if ([cityName isEqualToString:strChoiceCity])
        homeChoiceCityTableCell.backgroundColor = BLACK_COLOR_229;
    else
        homeChoiceCityTableCell.backgroundColor = [UIColor clearColor];
    
    return homeChoiceCityTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSArray *array = [self objectsForSection:section];
    if (array == nil) {
        return 0;
    }
    return array.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 45.f;
    return homeTableCellHeight;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return aryKeyAlpha.count;
}
/*
- (NSArray<NSString *> *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    return aryKeyAlpha;
}
*/
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    NSString *title = objectsForSection[indexPath.row];
    switch (self.homeIndex) {
        case -1:
            [CommonData sharedInstance].choiceCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
            break;
        case SUB_HOME_PERSONAL:
            [CommonData sharedInstance].choiceFamiliarCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
            break;
        case SUB_HOME_ENTERPRISE:
            [CommonData sharedInstance].choiceEnterpriseCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
            break;
        case SUB_HOME_COMMERCE:
            [CommonData sharedInstance].choiceCommerceCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
            break;
        case SUB_HOME_ITEM:
            [CommonData sharedInstance].choiceItemCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
            break;
        case SUB_HOME_SERVICE:
            [CommonData sharedInstance].choiceServiceCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
            break;
            
        default:
            break;
    }
    [tableView reloadData];
}



- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
    //return [AlphabetsArray objectAtIndex:section];
    
    NSDictionary *entry  = [self entryForSection:section];
    if (entry == nil)
        return @"";
    
    NSString *title = entry.allKeys.firstObject;
    return title;
    
}

- (NSDictionary*) entryForSection:(NSInteger) section {
    if (aryKeyAlpha.count == 0)
        return nil;
    NSString *sectionTitle = aryKeyAlpha[section];
    NSArray *objectsInSection = dicCityCode[sectionTitle];
    return @{sectionTitle : objectsInSection};
}

- (NSArray *)objectsForSection:(NSInteger)section {
    NSDictionary *entry = [self entryForSection:section];
    if (entry == nil)
        return nil;
    NSArray *objects = (NSArray*)entry.allValues.firstObject;
    return objects;
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
