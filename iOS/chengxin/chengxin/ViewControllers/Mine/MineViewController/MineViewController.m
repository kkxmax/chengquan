//
//  MineViewController.m
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "MineViewController.h"
#import "RequestFriendViewController.h"
#import "RealNameAuthenticationViewController.h"
#import "ChengXinRecordViewController.h"
#import "ChengXinReportViewController.h"
#import "SearchProductsViewController.h"
#import "SettingViewController.h"
#import "EvaluateViewController.h"
#import "EvalAwakenViewController.h"
#import "MyJiuCuoViewController.h"
#import "Global.h"

@interface MineViewController ()

@end

@implementation MineViewController
@synthesize messageNumberLabel;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.automaticallyAdjustsScrollViewInsets = NO;
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.lblName.text = [CommonData sharedInstance].userInfo[@"enterName"];
    self.lblCodeNumber.text = [CommonData sharedInstance].userInfo[@"code"];
    self.lblChengHuDu.text = [[CommonData sharedInstance].userInfo[@"credit"] stringValue];
    self.lblPingJia.text = [[CommonData sharedInstance].userInfo[@"feedbackCnt"] stringValue];
    self.lblDianZan.text = [[CommonData sharedInstance].userInfo[@"electCnt"] stringValue];
    
    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]]];
    [self.logoImageView setImage:[UIImage imageWithData:[NSData dataWithContentsOfURL:url]]];
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction
- (IBAction)onShowRequestFriendAction:(id)sender {
    RequestFriendViewController *requestFriendVC = [[RequestFriendViewController alloc] initWithNibName:@"RequestFriendViewController" bundle:nil];
    [self.navigationController pushViewController:requestFriendVC animated:YES];
}

- (IBAction)showNotificationView:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
}

#pragma mark - Notification Delegate
- (void)updateNotification {
    if([CommonData sharedInstance].notificationCount > 0) {
        NSString *strNotificationCount = [NSString stringWithFormat:@"%ld", [CommonData sharedInstance].notificationCount];
        messageNumberLabel.text = strNotificationCount;
        messageNumberLabel.hidden = NO;
    }else{
        messageNumberLabel.hidden = YES;
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
-(IBAction)onRealNameAuthentication:(id)sender
{
    RealNameAuthenticationViewController* realnameVC = [[RealNameAuthenticationViewController alloc] initWithNibName:@"RealNameAuthenticationViewController" bundle:nil];
    [self.navigationController pushViewController:realnameVC animated:YES];
}
-(IBAction)onChengXinReport:(id)sender
{
    ChengXinReportViewController *vc = [[ChengXinReportViewController alloc] initWithNibName:@"ChengXinReportViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onChengXinRecord:(id)sender
{
    ChengXinRecordViewController *vc = [[ChengXinRecordViewController alloc] initWithNibName:@"ChengXinRecordViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

-(IBAction)onMyRating:(id)sender
{
    EvaluateViewController *vc = [[EvaluateViewController alloc] initWithNibName:@"EvaluateViewController" bundle:nil];
    vc.bIsDetail = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

-(IBAction)onRatingToMe:(id)sender
{
    EvalAwakenViewController *vc = [[EvalAwakenViewController alloc] initWithNibName:@"EvalAwakenViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onMine:(id)sender
{
    MyJiuCuoViewController *vc = [[MyJiuCuoViewController alloc] initWithNibName:@"MyJiuCuoViewController" bundle:nil];
    [self.navigationController pushViewController:vc animated:YES];
}
-(IBAction)onMyPublication:(id)sender
{
    SearchProductsViewController* spVC = [[SearchProductsViewController alloc] initWithNibName:@"SearchProductsViewController" bundle:nil];
    [self.navigationController pushViewController:spVC animated:YES];
}
-(IBAction)onOpinion:(id)sender
{
    
}
-(IBAction)onSetting:(id)sender
{
    SettingViewController* svVC = [[SettingViewController alloc] initWithNibName:@"SettingViewController" bundle:nil];
    [self.navigationController pushViewController:svVC animated:YES];
}
@end
