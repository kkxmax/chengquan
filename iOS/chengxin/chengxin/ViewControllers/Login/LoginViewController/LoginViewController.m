//
//  LoginViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "LoginViewController.h"
#import "MainViewController.h"
#import "SignupViewController.h"
#import "ForgotPasswordViewController.h"

@interface LoginViewController ()

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    [self setNavigationBar];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark
// Navigation Set
-(void) setNavigationBar
{
    self.navigationController.navigationBar.hidden = YES;
    
}
-(IBAction)onRegister:(id)sender
{
    MainViewController *mainVC = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
    [self.navigationController pushViewController:mainVC animated:YES];
}
-(IBAction)onSignup:(id)sender
{
    SignupViewController *signupVC = [[SignupViewController alloc] initWithNibName:@"SignupViewController" bundle:nil];
    [self.navigationController pushViewController:signupVC animated:YES];
}
-(IBAction)onForgot:(id)sender
{
    ForgotPasswordViewController *forgotVC = [[ForgotPasswordViewController alloc] initWithNibName:@"ForgotPasswordViewController" bundle:nil];
    [self.navigationController pushViewController:forgotVC animated:YES];
}
-(IBAction)onSeePassword:(id)sender
{
    
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
