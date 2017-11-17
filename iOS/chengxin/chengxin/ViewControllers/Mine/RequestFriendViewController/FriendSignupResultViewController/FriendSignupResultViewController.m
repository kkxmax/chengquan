//
//  FriendSignupResultViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "FriendSignupResultViewController.h"

@interface FriendSignupResultViewController ()

@end

@implementation FriendSignupResultViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    UINavigationController *navVC = (UINavigationController *)self.parentViewController;
    [self.navigationController popToViewController:[navVC.childViewControllers objectAtIndex:(navVC.childViewControllers.count - 3)] animated:YES];
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
