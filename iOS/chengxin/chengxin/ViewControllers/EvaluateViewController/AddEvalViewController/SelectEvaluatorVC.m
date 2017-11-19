//
//  SelectEvaluatorVC.m
//  chengxin
//
//  Created by seniorcoder on 11/12/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SelectEvaluatorVC.h"
#import "FavouritesTableItemViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"

@interface SelectEvaluatorVC ()
{
    NSMutableArray *aryPersoanlData;
    NSMutableArray *aryEnterData;
    
    NSMutableArray *aryEvalData;
    NSMutableArray *aryEvalName;
    NSMutableArray *aryEvalCode;
    
    NSMutableArray *arySortEvalCode;
    NSMutableDictionary *dicEvalCode;
    NSMutableArray *aryKeyAlpha;
}
@end

enum {
    SELECT_PERSONAL = 1,
    SELECT_ENTERPRISE = 2,
    SELECT_HOT = 3
};

@implementation SelectEvaluatorVC
@synthesize selectType;
@synthesize tblSelectView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    aryPersoanlData = [[NSMutableArray alloc] init];
    aryEnterData = [[NSMutableArray alloc] init];
    [tblSelectView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    
    tblSelectView.sectionIndexBackgroundColor = [UIColor clearColor];
    tblSelectView.sectionIndexColor = BLACK_COLOR_102;
    [tblSelectView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    aryEvalData = [[NSMutableArray alloc] init];
    aryEvalName = [[NSMutableArray alloc] init];
    aryEvalCode = [[NSMutableArray alloc] init];
    aryKeyAlpha = [[NSMutableArray alloc] init];
    dicEvalCode = [[NSMutableDictionary alloc] init];
    
    if (selectType == SELECT_PERSONAL || selectType == SELECT_ENTERPRISE) {
        [self getEvaluateDataFromServer:@"" Start:@"" Length:@""];
    }
    else if (selectType == SELECT_HOT)
    {
        
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void) getEvaluateDataFromServer:(NSString*)xyleixingIds Start:(NSString*)start Length:(NSString*)length {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getAccountListForEstimate" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:start forKey:@"start"];
    [dicParams setObject:length forKey:@"length"];
    
    if ( xyleixingIds != nil && xyleixingIds.length > 0 )
        [dicParams setObject:xyleixingIds forKey:@"xyleixingIds"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETACCOUNTLISTFORESTIMATE Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryPersoanlData removeAllObjects];
                [aryEnterData removeAllObjects];
                
                for (int i = 0; i < aryData.count; i++) {
                    NSMutableDictionary *dic = [[NSMutableDictionary alloc] initWithDictionary:aryData[i]];
                    if (selectType == SELECT_PERSONAL) {
                        if ([dic[@"akind"] integerValue] != SELECT_PERSONAL)
                            continue;
                    }
                    else if (selectType == SELECT_ENTERPRISE)
                    {
                        if ([dic[@"akind"] integerValue] != SELECT_ENTERPRISE)
                            continue;
                    }
                    
                    NSString *name;
                    if (selectType == SELECT_PERSONAL)
                        name = dic[@"realname"];
                    else if (selectType == SELECT_ENTERPRISE)
                        name = dic[@"entername"];
                    else
                        name = dic[@"realname"];
                    
                    if (name.length > 0)
                    {
                        [aryEvalData addObject:dic];
                        [aryEvalName addObject:name];
                        [aryEvalCode addObject:[GeneralUtil convertCnToEn:name]];
                    }
                }
                
                if (aryEvalCode.count > 0) {
                    arySortEvalCode = [NSMutableArray arrayWithArray:[aryEvalCode sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
                        return [obj1 compare:obj2];
                    }]];
                    
                    for(NSString *str in arySortEvalCode)
                    {
                        NSString *firstChar = [str substringToIndex:1];
                        if(![aryKeyAlpha containsObject:firstChar])
                        {
                            NSMutableArray *charArray =  [[NSMutableArray alloc] init];
                            [charArray addObject:str];
                            [aryKeyAlpha addObject:firstChar];
                            [dicEvalCode setValue:charArray forKey:firstChar];
                        }
                        else
                        {
                            NSMutableArray *prevArray = (NSMutableArray *)[dicEvalCode valueForKey:firstChar];
                            [prevArray addObject:str];
                            [dicEvalCode setValue:prevArray forKey:firstChar];
                        }
                    }
                        
                    [tblSelectView reloadData];
                }
            }
        }
    }];
}

- (NSDictionary*) entryForSection:(NSInteger) section {
    if (aryKeyAlpha.count == 0)
        return nil;
    
    NSString *sectionTitle = aryKeyAlpha[section];
    NSArray *objectsInSection = dicEvalCode[sectionTitle];
    return @{sectionTitle : objectsInSection};
}

- (NSArray *)objectsForSection:(NSInteger)section {
    NSDictionary *entry = [self entryForSection:section];
    if (entry == nil)
        return nil;
    NSArray *objects = (NSArray*)entry.allValues.firstObject;
    return objects;
}

#pragma UITableViewDelegate & UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return aryKeyAlpha.count;
}

- (NSArray<NSString *> *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    NSMutableArray *ary = [NSMutableArray arrayWithArray:(NSArray*)aryKeyAlpha];
    return ary;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
    NSDictionary *entry  = [self entryForSection:section];
    if (entry == nil)
        return @"";
    
    NSString *title = entry.allKeys.firstObject;
    return title;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
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
    NSInteger index = [aryEvalCode indexOfObject:title];
    NSDictionary *dic = [aryEvalData objectAtIndex:index];
    
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
    cell.name.text = aryEvalName[index];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {

    NSArray *objectsForSection = [self objectsForSection:indexPath.section];
    NSString *title = objectsForSection[indexPath.row];
    NSInteger index = [aryEvalCode indexOfObject:title];
    NSDictionary *dic = [aryEvalData objectAtIndex:index];

    if (selectType == SELECT_PERSONAL) {
        self.rootVC.evalName = dic[@"realname"];
    }
    else
    {
        self.rootVC.evalName = dic[@"enterName"];
    }
    self.rootVC.evalId = dic[@"id"];
    
    [self.navigationController popViewControllerAnimated:YES];
}

@end
