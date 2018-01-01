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
    NSMutableArray *evaluatorArray;
    NSDictionary *selectEvaluatorDic;
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

    selectEvaluatorDic = nil;
    
    tblSelectView.sectionIndexBackgroundColor = [UIColor clearColor];
    tblSelectView.sectionIndexColor = BLACK_COLOR_102;
    [tblSelectView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    
    // Do any additional setup after loading the view from its nib.
    [self.tblSelectView registerNib:[UINib nibWithNibName:@"FavouritesTableItemViewCell" bundle:nil] forCellReuseIdentifier:@"FavouritesCellIdentifier"];

    // Do any additional setup after loading the view from its nib.
    evaluatorArray = [NSMutableArray array];
    
    if (selectType == SELECT_PERSONAL ) {
        [self getEvaluatePersonalList];
        self.navTitleLabel.text = @"选择个人";
    }else if(selectType == SELECT_ENTERPRISE) {
        [self getEvaluateEnterpriseList];
        self.navTitleLabel.text = @"选择企业";
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

- (IBAction)onCancelAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onSelectAction:(id)sender {
    if(selectEvaluatorDic) {
        [CommonData sharedInstance].selectedEvaluatorDic = selectEvaluatorDic;
        [self.navigationController popViewControllerAnimated:YES];
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)getEvaluatePersonalList {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getPassedPersonalList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPASSEDPERSONALLIST Parameters:dicParams :^(NSObject *resObj) {
        [evaluatorArray removeAllObjects];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                evaluatorArray = (NSMutableArray *)(dicRes[@"data"]);
                [tblSelectView reloadData];
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

- (void)getEvaluateEnterpriseList {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getPassedEnterList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPASSEDENTERLIST Parameters:dicParams :^(NSObject *resObj) {
        [evaluatorArray removeAllObjects];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                evaluatorArray = (NSMutableArray *)(dicRes[@"data"]);
                [tblSelectView reloadData];
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

#pragma UITableViewDelegate & UITableViewDataSource

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 56.f;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return evaluatorArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    static NSString *simpleTableIdentifier = @"FavouritesCellIdentifier";
    FavouritesTableItemViewCell *cell = (FavouritesTableItemViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    NSDictionary *dic = [evaluatorArray objectAtIndex:indexPath.row];
    NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"logo"]];
    NSInteger akind = [dic[@"akind"] integerValue];
    [cell.photo sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed: akind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
    if (selectType == SELECT_PERSONAL ) {
        cell.name.text = dic[@"realname"];
    }else if(selectType == SELECT_ENTERPRISE) {
        cell.name.text = dic[@"enterName"];
    }
    else if (selectType == SELECT_HOT) {
        cell.name.text = dic[@"realname"];
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    selectEvaluatorDic = (NSDictionary *)(evaluatorArray[indexPath.row]);
}

@end
