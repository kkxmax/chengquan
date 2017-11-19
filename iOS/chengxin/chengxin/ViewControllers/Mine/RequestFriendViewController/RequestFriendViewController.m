//
//  RequestFriendViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "RequestFriendViewController.h"
#import "FriendSignupViewController.h"

@interface RequestFriendViewController ()

@end

@implementation RequestFriendViewController
@synthesize adapterBackgroundView, adapterImageView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // Make Circle Image
    adapterBackgroundView.layer.cornerRadius = adapterBackgroundView.frame.size.width / 2;
    adapterImageView.layer.cornerRadius = adapterImageView.frame.size.width / 2;
    adapterBackgroundView.hidden = NO;
    adapterImageView.hidden = NO;
}

#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onFriendSignup:(id)sender {
    FriendSignupViewController *friendSignupVC = [[FriendSignupViewController alloc] initWithNibName:@"FriendSignupViewController" bundle:nil];
    [self.navigationController pushViewController:friendSignupVC animated:YES];
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
