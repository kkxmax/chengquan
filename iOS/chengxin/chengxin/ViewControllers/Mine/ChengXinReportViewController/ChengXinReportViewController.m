//
//  ChengXinReportViewController.m
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChengXinReportViewController.h"
#import "WebAPI.h"
#import "Global.h"


@interface ChengXinReportViewController ()

@end

@implementation ChengXinReportViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.lblName.text = [CommonData sharedInstance].userInfo[@"enterName"];
    self.lblCodeNumber.text = [CommonData sharedInstance].userInfo[@"code"];
    self.lblChengHuDu.text = [[CommonData sharedInstance].userInfo[@"credit"] stringValue];
    self.lblFeedback.text = [[CommonData sharedInstance].userInfo[@"feedbackCnt"] stringValue];
    self.lblDianZan.text = [[CommonData sharedInstance].userInfo[@"electCnt"] stringValue];
    self.lblAddress.text = [CommonData sharedInstance].userInfo[@"addr"];
    self.lblCompanyWebURL.text = [CommonData sharedInstance].userInfo[@"weburl"];
    self.lblWeixinNumber.text = [CommonData sharedInstance].userInfo[@"weixin"];
    self.lblPosition.text = [CommonData sharedInstance].userInfo[@"job"];
    self.lblWorkExperience.text = [CommonData sharedInstance].userInfo[@"experience"];
    self.lblPersonalHistory.text = [CommonData sharedInstance].userInfo[@"history"];
    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [CommonData sharedInstance].userInfo[@"logo"]]];
    [self.logoImageView setImage:[UIImage imageWithData:[NSData dataWithContentsOfURL:url]]];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    
    
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
