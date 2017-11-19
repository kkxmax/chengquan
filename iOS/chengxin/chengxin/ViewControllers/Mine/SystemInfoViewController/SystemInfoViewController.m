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
    [self getMyEvalFromServer];
    lblMyEval.layer.cornerRadius = lblMyEval.frame.size.width / 2;
    lblToMeEval.layer.cornerRadius = lblToMeEval.frame.size.width / 2;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
- (void)getMyEvalFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMyEstimateList" forKey:@"pAct"];
    [dicParams setObject:@"" forKey:@"start"];
    [dicParams setObject:@"" forKey:@"length"];
    [dicParams setObject:@"" forKey:@"akind"];
    [dicParams setObject:@"" forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETMYEVALUATELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *evalList = (NSArray *)(dicRes[@"data"]);
                if(evalList.count > 0) {
                    lblMyEval.text = [NSString stringWithFormat:@"%d", (int)evalList.count];
                    lblMyEval.hidden = NO;
                }else{
                    lblMyEval.hidden = YES;
                }
                [self getOtherEvalFromServer];
            }
        }
    }];
}

- (void)getOtherEvalFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getEstimateToMeList" forKey:@"pAct"];
    [dicParams setObject:@"" forKey:@"start"];
    [dicParams setObject:@"" forKey:@"length"];
    [dicParams setObject:@"" forKey:@"akind"];
    [dicParams setObject:@"" forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETOTHEREVALUATELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *evalList = (NSArray *)(dicRes[@"data"]);
                if(evalList.count > 0) {
                    lblToMeEval.text = [NSString stringWithFormat:@"%d", (int)evalList.count];
                    lblToMeEval.hidden = NO;
                }else{
                    lblToMeEval.hidden = YES;
                }
            }
        }
    }];
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
