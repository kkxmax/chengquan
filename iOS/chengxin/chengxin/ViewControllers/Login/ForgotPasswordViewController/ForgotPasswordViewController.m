//
//  ForgotPasswordViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ForgotPasswordViewController.h"
#import "WebAPI.h"
#import "GeneralUtil.h"
#import "Global.h"
#import "CommonData.h"
@interface ForgotPasswordViewController ()

@end

@implementation ForgotPasswordViewController
{
    int seconds;
    NSTimer* verifyTimer;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self setNavigationBar];
    self.btnComplete.layer.cornerRadius = 5;
    self.txtPhoneNumber.text = self.phoneNumber;
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(IBAction)onCompletion:(id)sender
{
    if(self.txtPhoneNumber.text.length == 0)
    {
        [appDelegate.window makeToast:@"手机号不能为空"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    if(self.txtPhoneNumber.text.length != 11)
    {
        [appDelegate.window makeToast:@"手机号不正确"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        
        return;
    }
    
    if(self.txtVerificationCode.text.length == 0)
    {
        [appDelegate.window makeToast:@"验证码不能为空"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    
    if(self.txtNewPassword.text.length == 0)
    {
        [appDelegate.window makeToast:@"密码不能为空"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
        
    }
    if(self.txtNewPassword.text.length < 6 || self.txtNewPassword.text.length > 20)
    {
        [appDelegate.window makeToast:@"密码6-20数字和字母组成"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        
        return;
    }
    if([self.txtNewPassword.text isEqualToString:self.txtConfirmPassword.text] != YES)
    {
        [appDelegate.window makeToast:@"密码不一致"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"forgotPassword" forKey:@"pAct"];
    [dicParams setObject:self.txtVerificationCode.text forKey:@"verifyCode"];
    [dicParams setObject:self.txtPhoneNumber.text forKey:@"mobile"];
    [dicParams setObject:self.txtNewPassword.text forKey:@"password"];
    //[dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:@"forgotPassword" Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.navigationController popViewControllerAnimated:YES];
                [appDelegate.window makeToast:@"密码已改修成功！"
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
            else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }
    }];
}

-(IBAction)onGetVerificationCode:(id)sender
{
    [self.txtPhoneNumber resignFirstResponder];
    [self.txtVerificationCode resignFirstResponder];
    [self.txtNewPassword resignFirstResponder];
    [self.txtConfirmPassword resignFirstResponder];

    if(self.txtPhoneNumber.text.length == 0)
    {
        [appDelegate.window makeToast:@"手机号不能为空"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    if(self.txtPhoneNumber.text.length != 11)
    {
        [appDelegate.window makeToast:@"手机号不正确"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        
        return;
    }
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getVerifyCode" forKey:@"pAct"];
    [dicParams setObject:self.txtPhoneNumber.text forKey:@"mobile"];

    [[WebAPI sharedInstance] sendPostRequest:@"getVerifyCode" Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [appDelegate.window makeToast:@"您好，已将验证码发送到您的手机，请注意查收。"
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
                seconds = 60;
                self.btnVerificationCode.enabled = NO;
                
                if([NSTimer instancesRespondToSelector:@selector(scheduledTimerWithTimeInterval:repeats:block:)])
                {
                    verifyTimer = [NSTimer scheduledTimerWithTimeInterval:1 repeats:YES block:^(NSTimer * _Nonnull timer) {
                        if(seconds > 0)
                        {
                            [self.btnVerificationCode setTitle:[NSString stringWithFormat: @"%d秒可重发", seconds] forState: UIControlStateNormal];
                            seconds--;
                        }else
                        {
                            [timer invalidate];
                            [self.btnVerificationCode setTitle:@"获取验证码" forState:UIControlStateNormal];
                            self.btnVerificationCode.enabled = YES;
                            
                        }
                        
                    }];
                }else
                {
                    verifyTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDown:) userInfo:nil repeats:YES];
                }
            }
            else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }
    }];

}
-(void)countDown:(id)userInfo
{
    if(seconds > 0)
    {
        [self.btnVerificationCode setTitle:[NSString stringWithFormat: @"%d秒可重发", seconds] forState: UIControlStateNormal];
        seconds--;
    }else
    {
        [verifyTimer invalidate];
        [self.btnVerificationCode setTitle:@"获取验证码" forState:UIControlStateNormal];
        self.btnVerificationCode.enabled = YES;
        
    }
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if(textField == self.txtPhoneNumber)
    {
        if(range.location >= 11)
            return NO;
        else
            return YES;
    }else if(textField == self.txtNewPassword || textField == self.txtConfirmPassword)
    {
       // if([string isEqualToString:@" "])
        //    return NO;
        if(range.location >= 20)
            return NO;
        else
            return YES;
    }
    return YES;
}

@end
