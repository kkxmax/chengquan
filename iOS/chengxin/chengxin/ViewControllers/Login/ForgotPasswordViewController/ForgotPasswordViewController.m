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
                
            }
            else {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
            }
        }
    }];
}

-(IBAction)onGetVerificationCode:(id)sender
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
                [GeneralUtil alertInfo:@"Error"];
            }
        }
    }];

}


@end
