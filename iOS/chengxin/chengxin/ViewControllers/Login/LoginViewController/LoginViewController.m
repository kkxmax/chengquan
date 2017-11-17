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
#import "WebAPI.h"
#import "Global.h"


@interface LoginViewController ()
{
}
@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    [self setNavigationBar];
    
    self.passwordInput.text = [GeneralUtil getUserPreference:@"password"];
    self.phoneNumberInput.text = [GeneralUtil getUserPreference:@"phoneNumber"];
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
    if(self.phoneNumberInput.text.length == 0 || self.passwordInput.text.length == 0)
    {
        [GeneralUtil alertInfo:@"手机号／密码不能为空"];
        return;
    }
    if(self.phoneNumberInput.text.length != 11)
    {
        [GeneralUtil alertInfo:@"手机号不正确"];
        return;
    }
    if(self.passwordInput.text.length < 6 || self.passwordInput.text.length > 20)
    {
        [GeneralUtil alertInfo:@"密码6-20字符"];
        return;
    }

    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"login" forKey:@"pAct"];
    [dicParams setObject:self.phoneNumberInput.text forKey:@"mobile"];
    [dicParams setObject:self.passwordInput.text forKey:@"password"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_LOGIN Parameters:dicParams :^(NSObject *resObj) {
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [GeneralUtil setUserPreference:@"password" value:self.passwordInput.text];
                [GeneralUtil setUserPreference:@"phoneNumber" value:self.phoneNumberInput.text];
                [CommonData sharedInstance].tokenName = dicRes[@"token"];
                [CommonData sharedInstance].userInfo = dicRes[@"userInfo"];
                MainViewController *mainVC = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
                [self.navigationController pushViewController:mainVC animated:YES];
            }
            else {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
            }
        }else
        {
            [GeneralUtil alertInfo:@"网络不连接"];
        }
    }];
}

-(IBAction)onSignup:(id)sender
{
    SignupViewController *signupVC;
    
    if(IS_IPHONE_4_OR_LESS)
        signupVC= [[SignupViewController alloc] initWithNibName:@"SignupViewController_iphone4" bundle:nil];
    else
        signupVC= [[SignupViewController alloc] initWithNibName:@"SignupViewController" bundle:nil];
    [self.navigationController pushViewController:signupVC animated:YES];
}
-(IBAction)onForgot:(id)sender
{
    ForgotPasswordViewController *forgotVC;
    if(IS_IPHONE_4_OR_LESS)
        forgotVC = [[ForgotPasswordViewController alloc] initWithNibName:@"ForgotPasswordViewController_iphone4" bundle:nil];
    else
        forgotVC = [[ForgotPasswordViewController alloc] initWithNibName:@"ForgotPasswordViewController" bundle:nil];
    [self.navigationController pushViewController:forgotVC animated:YES];
}
-(IBAction)onSeePassword:(id)sender
{
    [self.passwordEye setSelected:!self.passwordEye.selected];
    self.passwordInput.secureTextEntry = !self.passwordEye.selected;
}
-(IBAction)onDeletePhoneNumber:(id)sender
{
    self.phoneNumberInput.text = @"";
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
