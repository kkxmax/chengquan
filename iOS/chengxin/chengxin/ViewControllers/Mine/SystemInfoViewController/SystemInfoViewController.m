//
//  SystemInfoViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SystemInfoViewController.h"
#import "SystemAlarmViewController.h"
#import "EvalAwakenViewController.h"
#import "Global.h"

@interface SystemInfoViewController ()

@end

@implementation SystemInfoViewController
@synthesize lblMyEval, lblToMeEval;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    lblMyEval.layer.cornerRadius = lblMyEval.frame.size.width / 2;
    lblToMeEval.layer.cornerRadius = lblToMeEval.frame.size.width / 2;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    if([CommonData sharedInstance].myEstimateCnt > 0) {
        lblMyEval.text = [NSString stringWithFormat:@"%ld", [CommonData sharedInstance].myEstimateCnt];
        lblMyEval.hidden = NO;
    }else{
        lblMyEval.hidden = YES;
    }
    if([CommonData sharedInstance].estimateToMeCnt > 0) {
        lblToMeEval.text = [NSString stringWithFormat:@"%ld", [CommonData sharedInstance].estimateToMeCnt];
        lblToMeEval.hidden = NO;
    }else{
        lblToMeEval.hidden = YES;
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onAlarmAction:(id)sender {
    SystemAlarmViewController *systemAlarmVC = [[SystemAlarmViewController alloc] initWithNibName:@"SystemAlarmViewController" bundle:nil];
    [self.navigationController pushViewController:systemAlarmVC animated:YES];
}

- (IBAction)onMineEvaluateAction:(id)sender {
    EvalAwakenViewController *evalAwakenVC = [[EvalAwakenViewController alloc] initWithNibName:@"EvalAwakenViewController" bundle:nil];
    evalAwakenVC.isMineEvaluate = YES;
    [self.navigationController pushViewController:evalAwakenVC animated:YES];
}

- (IBAction)onOtherEvaluateAction:(id)sender {
    EvalAwakenViewController *evalAwakenVC = [[EvalAwakenViewController alloc] initWithNibName:@"EvalAwakenViewController" bundle:nil];
    evalAwakenVC.isMineEvaluate = NO;
    [self.navigationController pushViewController:evalAwakenVC animated:YES];
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
