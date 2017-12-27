//
//  SystemAlarmViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SystemAlarmViewController.h"
#import "SystemAlarmTableViewCell.h"
#import "Global.h"
#import "EvaluateDetailViewController.h"
#import "ReformViewController.h"
#import "HomeFamiliarDetailViewController.h"
#import "RealNameAuthenticationViewController.h"
#import "JiuCuoViewController.h"

@interface SystemAlarmViewController ()
{
    NSMutableArray *alertArray;
}
@end

@implementation SystemAlarmViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.tblContentView registerNib:[UINib nibWithNibName:@"SystemAlarmTableViewCell" bundle:nil] forCellReuseIdentifier:@"SystemAlarmCellIdentifier"];
    // Do any additional setup after loading the view from its nib.
    alertArray = [NSMutableArray array];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self getAlertFromServer];
}

- (void)getAlertFromServer {
    [alertArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getSystemNoticeList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETNOTICELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                alertArray = (NSMutableArray *)(dicRes[@"data"]);
                [self.tblContentView reloadData];
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

#pragma mark -  UITableViewDelegate & UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return alertArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 66.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"SystemAlarmCellIdentifier";
    SystemAlarmTableViewCell *cell = (SystemAlarmTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    NSDictionary *alertDic = (NSDictionary *)[alertArray objectAtIndex:indexPath.row]; //msgTitle, msgContent, writeTimeString
    cell.titleLabel.text = alertDic[@"msgTitle"];
    cell.contentLabel.text = alertDic[@"msgContent"];
    cell.timeLabel.text = alertDic[@"writeTimeString"];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    NSDictionary *alertDic = (NSDictionary *)[alertArray objectAtIndex:indexPath.row]; //msgTitle, msgContent, writeTimeString
    NSInteger alertKind = [alertDic[@"kind"] integerValue];
    if(alertKind == 1) { // Evaluate Detail
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"getEstimate" forKey:@"pAct"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        [dicParams setObject:alertDic[@"estimateId"] forKey:@"estimateId"];
        [[WebAPI sharedInstance] sendPostRequest:ACTION_GETESTIMATE Parameters:dicParams :^(NSObject *resObj) {
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    NSMutableDictionary *evalDic = (NSMutableDictionary *)dicRes[@"estimateInfo"];
                    EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
                    detailViewController.dicEvalData = evalDic;
                    detailViewController.isHotEvaluator = NO;
                    [self.navigationController pushViewController:detailViewController animated:YES];
                }
            }
        }];

    }else if(alertKind == 2) { //Realname Certification
        RealNameAuthenticationViewController *realNameVC = [[RealNameAuthenticationViewController alloc] initWithNibName:@"RealNameAuthenticationViewController" bundle:nil];
        [[CommonData sharedInstance].userInfo setValue:alertDic[@"testStatus"] forKey:@"testStatus"];
        [self.navigationController pushViewController:realNameVC animated:YES];
    }else if(alertKind == 3) { //Make Error
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"getError" forKey:@"pAct"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        [dicParams setObject:alertDic[@"errorId"] forKey:@"errorId"];
        [[WebAPI sharedInstance] sendPostRequest:ACTION_GETERROR Parameters:dicParams :^(NSObject *resObj) {
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    NSDictionary *errorDic = (NSDictionary *)dicRes[@"errorInfo"];
                    JiuCuoViewController *jiuCuoVC = [[JiuCuoViewController alloc] initWithNibName:@"JiuCuoViewController" bundle:nil];
                    jiuCuoVC.data = errorDic;
                    [self.navigationController pushViewController:jiuCuoVC animated:YES];
                }
            }
        }];
        

    }else if(alertKind == 4) { //Require Friend
        HomeFamiliarDetailViewController *homeEnterDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
        [CommonData sharedInstance].selectedFriendAccountID = alertDic[@"inviteeId"];
        [self.navigationController pushViewController:homeEnterDetailVC animated:YES];
    }
}

@end
