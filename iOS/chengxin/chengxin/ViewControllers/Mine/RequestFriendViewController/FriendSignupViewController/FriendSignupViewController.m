//
//  FriendSignupViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "FriendSignupViewController.h"
#import "FriendSignupResultViewController.h"

@interface FriendSignupViewController ()

@end

@implementation FriendSignupViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction
-(IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onSignupAction:(id)sender {
    FriendSignupResultViewController *friendSignupResultVC = [[FriendSignupResultViewController alloc] initWithNibName:@"FriendSignupResultViewController" bundle:nil];
    [self.navigationController pushViewController:friendSignupResultVC animated:YES];
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
