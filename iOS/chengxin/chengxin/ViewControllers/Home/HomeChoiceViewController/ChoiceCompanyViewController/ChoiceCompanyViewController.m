//
//  HomeChoiceAllCityViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChoiceCompanyViewController.h"
#import "ChoiceCompanyTableViewCell.h"

#import "Global.h"

@interface ChoiceCompanyViewController ()
{
    NSMutableArray *aryCompanyData;
    NSMutableArray *aryCompanyName;
    NSMutableArray *aryCompanyCode;

    NSMutableArray *arySortCompanyCode;
    NSMutableDictionary *dicCompanyCode;
    NSMutableArray *aryKeyAlpha;
}
@end

@implementation ChoiceCompanyViewController
@synthesize choiceCompanyTableView;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [choiceCompanyTableView registerNib:[UINib nibWithNibName:@"ChoiceCompanyTableViewCell" bundle:nil] forCellReuseIdentifier:@"ChoiceCompanyCellIdentifier"];
    
    aryCompanyData = [[NSMutableArray alloc] init];
    aryCompanyName = [[NSMutableArray alloc] init];
    aryCompanyCode = [[NSMutableArray alloc] init];
    aryKeyAlpha = [[NSMutableArray alloc] init];
    dicCompanyCode = [[NSMutableDictionary alloc] init];
    
    self.companyIndex = -1;
    [self getCityListFromServer];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) getCityListFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_GETCOMPANYLIST forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCOMPANYLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *cityList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < cityList.count; i++) {
                    NSDictionary *cityDic = (NSDictionary *)(cityList[i]);
                    [aryCompanyData addObject:cityDic];
                    [aryCompanyName addObject:cityDic[@"enterName"]];
                    [aryCompanyCode addObject:cityDic[@"enterNamePinyin"]];
                }
                
                if (aryCompanyCode.count > 0) {
                    arySortCompanyCode = [NSMutableArray arrayWithArray:[aryCompanyCode sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
                        return [obj1 compare:obj2];
                    }]];
                    
                    for(NSString *str in arySortCompanyCode)
                    {
                        NSString *firstChar = [str substringToIndex:1];
                        if(![aryKeyAlpha containsObject:firstChar])
                        {
                            NSMutableArray *charArray =  [[NSMutableArray alloc] init];
                            [charArray addObject:str];
                            [aryKeyAlpha addObject:firstChar];
                            [dicCompanyCode setValue:charArray forKey:firstChar];
                        }
                        else
                        {
                            NSMutableArray *prevArray = (NSMutableArray *)[dicCompanyCode valueForKey:firstChar];
                            [prevArray addObject:str];
                            [dicCompanyCode setValue:prevArray forKey:firstChar];
                        }
                    }
                    
                    [choiceCompanyTableView reloadData];
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
    [CommonData sharedInstance].choiceCompany = @"";
    [CommonData sharedInstance].choiceCompanyId = @"";
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
    NSString *companyName = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
    ChoiceCompanyTableViewCell *homeChoiceCityTableCell = (ChoiceCompanyTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"ChoiceCompanyCellIdentifier" forIndexPath:indexPath];
    [homeChoiceCityTableCell.companyNameLabel setText:companyName];
    NSString *strChoiceCity = @"";
    switch (self.companyIndex) {
        case -1:
            strChoiceCity = [CommonData sharedInstance].choiceCompany;
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
    if ([companyName isEqualToString:strChoiceCity])
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
    switch (self.companyIndex) {
        case -1:
            [CommonData sharedInstance].choiceCompany = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
            [CommonData sharedInstance].choiceCompanyId = [[aryCompanyData objectAtIndex:[aryCompanyCode indexOfObject:title]] objectForKey:@"id"];
            [CommonData sharedInstance].choiceXyleixingId = [aryCompanyData objectAtIndex:[aryCompanyCode indexOfObject:title]][@"xyleixingId"];
            [CommonData sharedInstance].selectedBusiness = [aryCompanyData objectAtIndex:[aryCompanyCode indexOfObject:title]][@"xyName"];
            break;
        case SUB_HOME_PERSONAL:
            [CommonData sharedInstance].choiceFamiliarCity = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
            break;
        case SUB_HOME_ENTERPRISE:
            [CommonData sharedInstance].choiceEnterpriseCity = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
            break;
        case SUB_HOME_COMMERCE:
            [CommonData sharedInstance].choiceCommerceCity = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
            break;
        case SUB_HOME_ITEM:
            [CommonData sharedInstance].choiceItemCity = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
            break;
        case SUB_HOME_SERVICE:
            [CommonData sharedInstance].choiceServiceCity = [aryCompanyName objectAtIndex: [aryCompanyCode indexOfObject:title]];
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
    NSArray *objectsInSection = dicCompanyCode[sectionTitle];
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
