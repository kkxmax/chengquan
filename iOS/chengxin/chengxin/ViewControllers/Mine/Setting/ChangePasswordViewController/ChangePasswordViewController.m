//
//  ChangePasswordViewController.m
//  chengxin
//
//  Created by common on 7/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChangePasswordViewController.h"
#import "GeneralUtil.h"
#import "Global.h"


@interface ChangePasswordViewController ()

@end

@implementation ChangePasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
-(IBAction)onCompletion:(id)sender
{
    if(self.txtOldPassword.text.length == 0)
    {
        [GeneralUtil alertInfo:@"旧密码不能为空"];
        return;
    }
    if(self.txtOldPassword.text.length < 6 || self.txtOldPassword.text.length > 20)
    {
        [GeneralUtil alertInfo:@"旧密码6-20字符"];
        return;
    }
    if(self.txtNewPassword.text.length == 0)
    {
        [GeneralUtil alertInfo:@"新密码不能为空"];
        return;
    }
    if(self.txtNewPassword.text.length < 6 || self.txtNewPassword.text.length > 20)
    {
        [GeneralUtil alertInfo:@"新密码6-20数字和字母组成"];
        return;
    }
    
    if([self.txtNewPassword.text isEqualToString:self.txtConfirmationPassword.text] != YES)
    {
        [GeneralUtil alertInfo:@"密码不一致"];
        return;
    }
    
    if([self checkValidation])
    {
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"resetPassword" forKey:@"pAct"];
        [dicParams setObject:self.txtOldPassword.text forKey:@"oldPass"];
        [dicParams setObject:self.txtNewPassword.text forKey:@"newPass"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        
        [[WebAPI sharedInstance] sendPostRequest:ACTION_RESETPASSWORD Parameters:dicParams :^(NSObject *resObj){
            
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    [GeneralUtil alertInfo:@"成功！"];
                }
                else {
                    [GeneralUtil alertInfo:dicRes[@"msg"]];
                }
            }
        }];

    }
}
-(BOOL)checkValidation
{
    if(self.txtOldPassword.text.length == 0)
    {
        [GeneralUtil alertInfo:@"Old password must not be empty!"];
        return false;
    }
    if(self.txtNewPassword.text.length == 0)
    {
        [GeneralUtil alertInfo:@"New password must not be empty!"];
        return false;
    }
    if(self.txtConfirmationPassword.text.length == 0)
    {
        [GeneralUtil alertInfo:@"Confirmation password must not be empty!"];
        return false;
    }
    if(![self.txtNewPassword.text isEqualToString:self.txtConfirmationPassword.text])
    {
        [GeneralUtil alertInfo:@"New password and confirmation password must be the same!"];
        return false;
    }
    return true;
}
@end
