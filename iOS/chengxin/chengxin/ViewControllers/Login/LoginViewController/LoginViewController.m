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
{
    UIView* maskView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    [self setNavigationBar];
    
    //self.passwordInput.text = [GeneralUtil getUserPreference:@"password"];
    self.phoneNumberInput.text = [GeneralUtil getUserPreference:@"phoneNumber"];
    self.btnLogin.layer.cornerRadius = 5;
    
    NSString *phone = [GeneralUtil getUserPreference:@"phone"];
    NSString *password = [GeneralUtil getUserPreference:@"password"];
    if( (phone != nil && phone.length != 0) && (password != nil && password.length != 0))
    {
        self.phoneNumberInput.text = phone;
        self.passwordInput.text = password;
        [self onRegister:nil];
    }
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.phoneNumberInput.text = [GeneralUtil getUserPreference:@"phoneNumber"];
    self.passwordInput.text = [GeneralUtil getUserPreference:@"password"];
    self.btnLogin.layer.cornerRadius = 5;
    
    NSString *phone = [GeneralUtil getUserPreference:@"phone"];
    NSString *password = [GeneralUtil getUserPreference:@"password"];
    
        
    if(maskView)
    {
        [maskView removeFromSuperview];
    }
    if( (phone != nil && phone.length != 0) && (password != nil && password.length != 0))
    {
        maskView = [[UIView alloc] initWithFrame:self.view.frame];
        [maskView setBackgroundColor:[UIColor whiteColor]];
        [self.view addSubview:maskView];
    }
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
    if(self.phoneNumberInput.text.length == 0)
    {
        [appDelegate.window makeToast:@"手机号不能为空"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    if(self.phoneNumberInput.text.length != 11)
    {
        [appDelegate.window makeToast:@"手机号不正确"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    if(self.passwordInput.text.length == 0)
    {
        [appDelegate.window makeToast:@"密码不能为空"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    if(self.passwordInput.text.length < 6 || self.passwordInput.text.length > 20)
    {
        [appDelegate.window makeToast:@"密码不正确"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }

    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"login" forKey:@"pAct"];
    [dicParams setObject:self.phoneNumberInput.text forKey:@"mobile"];
    [dicParams setObject:self.passwordInput.text forKey:@"password"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_LOGIN Parameters:dicParams :^(NSObject *resObj) {
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [GeneralUtil setUserPreference:@"phone" value:self.phoneNumberInput.text];
                [GeneralUtil setUserPreference:@"password" value:self.passwordInput.text];
                [GeneralUtil setUserPreference:@"phoneNumber" value:self.phoneNumberInput.text];
                [CommonData sharedInstance].tokenName = dicRes[@"token"];
                [CommonData sharedInstance].userInfo = dicRes[@"userInfo"];
                MainViewController *mainVC = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
                [self.navigationController pushViewController:mainVC animated:YES];
                
                self.passwordInput.text = @"";
                AppDelegate* app = (AppDelegate*)[UIApplication sharedApplication].delegate;
                app.isAccountDuplicated = false;
            }else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else
        {
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
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
    forgotVC.phoneNumber = self.phoneNumberInput.text;
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
#pragma mark - UITextFieldDelegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if(textField == self.phoneNumberInput)
    {
        if(range.location >= 11)
            return NO;
        else
            return YES;
    }else if(textField == self.passwordInput)
    {
        //if([string isEqualToString:@" "])
        //    return NO;
        if(range.location >= 20)
            return NO;
        else
            return YES;
    }
    return YES;
}

@end
