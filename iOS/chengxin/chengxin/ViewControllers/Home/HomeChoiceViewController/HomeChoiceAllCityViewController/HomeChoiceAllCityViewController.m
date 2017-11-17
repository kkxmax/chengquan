//
//  HomeChoiceAllCityViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceCityTableViewCell.h"
#import "Global.h"

@interface HomeChoiceAllCityViewController ()
{
    NSMutableArray *aryCityData;
    NSMutableArray *aryCityName;
    NSMutableArray *aryCityCode;

    NSMutableArray *arySortCityCode;
    NSMutableDictionary *dicCityCode;
    NSMutableArray *aryKeyAlpha;
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
    
    [self getCityListFromServer];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
                    NSDictionary *cityDic = (NSDictionary *)(cityList[i]);
                    [aryCityData addObject:cityDic];
                    [aryCityName addObject:cityDic[@"name"]];
                    [aryCityCode addObject:cityDic[@"code"]];
                }
                
                if (aryCityCode.count > 0) {
                    arySortCityCode = [NSMutableArray arrayWithArray:[aryCityCode sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
                        return [obj1 compare:obj2];
                    }]];
                    
                    for(NSString *str in arySortCityCode)
                    {
                        NSString *firstChar = [str substringToIndex:1];
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
                    
                    [choiceCityTableView reloadData];
                }
            }
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
    
    if ([cityName isEqualToString:[CommonData sharedInstance].choiceCity])
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

- (NSArray<NSString *> *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    return aryKeyAlpha;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    NSString *title = objectsForSection[indexPath.row];
    [CommonData sharedInstance].choiceCity = [aryCityName objectAtIndex: [aryCityCode indexOfObject:title]];
    
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
