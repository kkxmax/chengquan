//
//  SignupViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SignupViewController.h"
#import "SignupSuccessViewController.h"
#import "WebAPI.h"
#import "Global.h"

@interface SignupViewController ()

@end

@implementation SignupViewController

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

- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
-(IBAction)onCompletion:(id)sender
{
    if(self.txtPhoneNumber.text.length == 0 || self.txtVerificationCode.text.length == 0)
    {
        [GeneralUtil alertInfo:@"手机号／验证码不能为空"];
        return;
    }
    if(self.txtPhoneNumber.text.length != 11)
    {
        [GeneralUtil alertInfo:@"手机号不正确"];
        return;
    }
    if(self.txtNewPassword.text.length < 6 || self.txtNewPassword.text.length > 20)
    {
        [GeneralUtil alertInfo:@"密码6-20数字和字母组成"];
        return;
    }
    if([self.txtNewPassword.text isEqualToString:self.txtConfirmPassword.text] != YES)
    {
        [GeneralUtil alertInfo:@"密码不一致"];
        return;
    }
    if(_btnAccept.selected == NO)
    {
        [GeneralUtil alertInfo:@"您需要阅读并同意接受《诚呼服务条款》"];
        return;
    }
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"register" forKey:@"pAct"];
    [dicParams setObject:self.txtVerificationCode.text forKey:@"verifyCode"];
    [dicParams setObject:self.txtPhoneNumber.text forKey:@"mobile"];
    //[dicParams setObject:self.txtRequestCode.text forKey:@"reqCode"];
    [dicParams setObject:self.txtNewPassword.text forKey:@"password"];
    
    [[WebAPI sharedInstance] sendPostRequest:@"register" Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                SignupSuccessViewController *signupSuccVC = [[SignupSuccessViewController alloc] initWithNibName:@"SignupSuccessViewController" bundle:nil];
                [self.navigationController pushViewController:signupSuccVC animated:YES];
                
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
-(IBAction)getVerificationCode:(id)sender
{
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getVerifyCode" forKey:@"pAct"];
    [dicParams setObject:self.txtPhoneNumber.text forKey:@"mobile"];
    
    [[WebAPI sharedInstance] sendPostRequest:nil Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                self.txtVerificationCode.text = dicRes[@"verifyCode"];
                
            }
            else{
                [GeneralUtil alertInfo:dicRes[@"verifyCode"]];
            }
        }
    }];

}
-(IBAction)onAcceptCheck:(id)sender
{
    self.btnAccept.selected = !self.btnAccept.selected;
}

@end
