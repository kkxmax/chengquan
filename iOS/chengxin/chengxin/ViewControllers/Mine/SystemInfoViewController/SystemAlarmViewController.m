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
    [self getAlertFromServer];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)getAlertFromServer {
    [alertArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getNoticeList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETNOTICELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                alertArray = (NSMutableArray *)(dicRes[@"data"]);
                [self.tblContentView reloadData];
            }
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
    
    
}

@end
